package com.hacker.mybatis.freemarker;


import com.hacker.mybatis.AbstractGenerateMojo;
import com.hacker.mybatis.config.ConstVal;
import com.hacker.mybatis.config.builder.ConfigBuilder;
import com.hacker.mybatis.config.po.TableInfo;
import freemarker.template.TemplateException;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * freemarker引擎 生成文件
 * @author tuonioooo
 * @since 2016/8/30
 */
@Mojo(name = "fmToGenerate", threadSafe = true)
public class FreemarkerGenerateMojo extends AbstractGenerateMojo {

    /**
     * freemarker引擎
     */
    private Configuration engine;
    /**
     * 输出文件
     */
    private Map<String, String> outputFiles;

    public void execute() throws MojoExecutionException, MojoFailureException {
        log.info("==========================准备生成文件...==========================");
        // 初始化配置
        initConfig();
        // 初始化输出文件路径模板
        initOutputFiles(config);
        // 创建输出文件路径
        mkdirs(config.getPathInfo());
        // 获取上下文
        Map<String, Map<String,Object>> ctxData = analyzeData(config);
        // 循环生成文件
        for (Map.Entry<String, Map<String,Object>> ctx : ctxData.entrySet()) {
            batchOutput(ctx.getKey(), ctx.getValue());
        }
        //打开输出目录
        try {
            String osName = System.getProperty("os.name");
            if (osName != null) {
                if (osName.contains("Mac")) {
                    Runtime.getRuntime().exec("open " + getOutputDir());
                } else if (osName.contains("Windows")) {
                    Runtime.getRuntime().exec("cmd /c start " + getOutputDir());
                } else {
                    log.info("文件输出目录:" + getOutputDir());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("==========================文件生成完成！！！==========================");
    }

    /**
     * 初始化配置
     */
    @Override
    protected void initConfig() {
        if (null == config) {
            setOutputDir(getOutputDir());
            config = new ConfigBuilder(getPackageInfo(), getDataSource(), getStrategy(), getOutputDir());
        }
    }

    /**
     * 分析数据
     *
     * @param config 总配置信息
     * @return 解析数据结果集
     */
    private Map<String, Map<String,Object>> analyzeData(ConfigBuilder config) {
        List<TableInfo> tableList = config.getTableInfoList();
        Map<String, String> packageInfo = config.getPackageInfo();
        Map<String, Map<String,Object>> ctxData = new HashMap<String, Map<String,Object>>();
        String superClass = config.getSuperClass().substring(config.getSuperClass().lastIndexOf(".") + 1);
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String currentTime = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date());
        for (TableInfo tableInfo : tableList) {
            Map<String,Object> ctx = new HashMap<String, Object>();
            ctx.put("package", packageInfo);
            ctx.put("table", tableInfo);
            ctx.put("entity", tableInfo.getEntityName());
            ctx.put("tsNameUpperFirst", tableInfo.getTsNameUpperFirst());
            ctx.put("tsNameLowerFirst", toLowerCaseFirst(tableInfo.getTsNameUpperFirst()));
            ctx.put("entityLowerCase", toLowerCaseFirst(tableInfo.getEntityName()));//首字母小写
            ctx.put("idGenType", config.getIdType());
            ctx.put("isStrategyKey", config.isStrategyKey());
            ctx.put("idClassType", config.getIdClassType());
            ctx.put("superClassPackage", config.getSuperClass());
            ctx.put("superClass", superClass);
            ctx.put("enableCache", isEnableCache());
            ctx.put("author", getAuthor());
            ctx.put("date", date);
            ctx.put("currentTime", currentTime);
            ctxData.put(tableInfo.getEntityName(), ctx);
        }
        return ctxData;
    }

    /**
     * 首字母转小写
     * @param s
     * @return
     */
    public String toLowerCaseFirst(String s){
        if(StringUtils.isEmpty(s)){
            return "";
        }
        if(Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    /**
     * 首字母转大写
     * @param s
     * @return
     */
    public String toUpperCaseFirst(String s){
        if(StringUtils.isEmpty(s)){
            return "";
        }
        if(Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    /**
     * 处理输出目录
     *
     * @param pathInfo 路径信息
     */
    private void mkdirs(Map<String, String> pathInfo) {
        for (Map.Entry<String, String> entry : pathInfo.entrySet()) {
            File dir = new File(entry.getValue());
            if (!dir.exists()) {
                boolean result = dir.mkdirs();
                if (result) {
                    log.info("创建目录： [" + entry.getValue() + "]");
                }
            }
        }
    }

    /**
     * 初始化输出目录
     */
    private void initOutputFiles(ConfigBuilder config) {
        outputFiles = new HashMap<String, String>();
        Map<String, String> pathInfo = config.getPathInfo();
        outputFiles.put(ConstVal.ENTITY, pathInfo.get(ConstVal.ENTITY_PATH) + ConstVal.ENTITY_NAME);
        outputFiles.put(ConstVal.MAPPER, pathInfo.get(ConstVal.MAPPER_PATH) + ConstVal.MAPPER_NAME);
        outputFiles.put(ConstVal.QO, pathInfo.get(ConstVal.QO_PATH) + ConstVal.QO_NAME);
        outputFiles.put(ConstVal.VO, pathInfo.get(ConstVal.VO_PATH) + ConstVal.VO_NAME);
        outputFiles.put(ConstVal.XML, pathInfo.get(ConstVal.XML_PATH) + ConstVal.XML_NAME);
        outputFiles.put(ConstVal.SERIVCE, pathInfo.get(ConstVal.SERIVCE_PATH) + ConstVal.SERVICE_NAME);
        outputFiles.put(ConstVal.SERVICEIMPL, pathInfo.get(ConstVal.SERVICEIMPL_PATH) + ConstVal.SERVICEIMPL_NAME);
        outputFiles.put(ConstVal.CONTROLLER, pathInfo.get(ConstVal.CONTROLLER_PATH) + ConstVal.CONTROLLER_NAME);


        ////////////////////// TS 前端配置  ////////////////////
        outputFiles.put(ConstVal.TS_MODELS, pathInfo.get(ConstVal.TS_MODELS_PATH) + ConstVal.TS_MODELS_NAME);

        // 根据表字段处理动态输出目录配置上文件后缀名
        for (TableInfo tableInfo: config.getTableInfoList()){
            String tableName = tableInfo.getName();
            outputFiles.put(ConstVal.TS_PAGES.concat(tableName), pathInfo.get(ConstVal.TS_PAGES_PATH.concat(tableName)) + ConstVal.TS_PAGES_NAME);
            outputFiles.put(ConstVal.TS_PAGES_COMPONENT.concat(tableName), pathInfo.get(ConstVal.TS_PAGES_COMPONENT_PATH.concat(tableName)) + ConstVal.TS_PAGES_COMPONENT_NAME);
        }
        outputFiles.put(ConstVal.TS_SERVICES, pathInfo.get(ConstVal.TS_SERVICES_PATH) + ConstVal.TS_SERVICES_NAME);
    }

    /**
     * 合成上下文与模板
     *
     * @param context vm上下文
     */
    private void batchOutput(String entityName, Map<String,Object> context) {
        try {
            String entityFile = String.format(outputFiles.get(ConstVal.ENTITY), entityName.concat(ConstVal.ENTITY));
            String mapperFile = String.format(outputFiles.get(ConstVal.MAPPER), entityName);
            String qoFile = String.format(outputFiles.get(ConstVal.QO), entityName);
            String voFile = String.format(outputFiles.get(ConstVal.VO), entityName);
            String xmlFile = String.format(outputFiles.get(ConstVal.XML), entityName);
            String serviceFile = String.format(outputFiles.get(ConstVal.SERIVCE), entityName);
            String controllerFile = String.format(outputFiles.get(ConstVal.CONTROLLER), entityName);


            // 前端 ts 配置
            String tsModelsOutPutFile = String.format(outputFiles.get(ConstVal.TS_MODELS), context.get("tsNameLowerFirst"));
            TableInfo tableInfo = (TableInfo) context.get("table");
            String tsPagesOutPutFile = String.format(outputFiles.get(ConstVal.TS_PAGES.concat(tableInfo.getName())), "index");
            String tsPagesDataOutPutFile = String.format(outputFiles.get(ConstVal.TS_PAGES.concat(tableInfo.getName())), "data.d");
            String tsPagesCreateComponentOutPutFile = String.format(outputFiles.get(ConstVal.TS_PAGES_COMPONENT.concat(tableInfo.getName())), "CreateForm");
            String tsPagesUpdateComponentOutPutFile = String.format(outputFiles.get(ConstVal.TS_PAGES_COMPONENT.concat(tableInfo.getName())), "UpdateForm");
            String tsServicesOutPutFile = String.format(outputFiles.get(ConstVal.TS_SERVICES), context.get("tsNameLowerFirst"));

            // 根据override标识来判断是否需要创建文件
            if (isCreate(entityFile)) {
                vmToFile(context, ConstVal.FREEMARKER_TEMPLATE_ENTITY, entityFile);
            }
            if (isCreate(mapperFile)) {
                vmToFile(context, ConstVal.FREEMARKER_TEMPLATE_MAPPER, mapperFile);
            }
            if (isCreate(qoFile)) {
                vmToFile(context, ConstVal.FREEMARKER_TEMPLATE_QO, qoFile);
            }
            if (isCreate(voFile)) {
                vmToFile(context, ConstVal.FREEMARKER_TEMPLATE_VO, voFile);
            }
             if (isCreate(xmlFile)) {
                vmToFile(context, ConstVal.FREEMARKER_TEMPLATE_MAPPER_XML, xmlFile);
            }
            if (isCreate(serviceFile)) {
                vmToFile(context, ConstVal.FREEMARKER_TEMPLATE_SERVICE, serviceFile);
            }
            if (isCreate(controllerFile)) {
                vmToFile(context, ConstVal.FREEMARKER_TEMPLATE_CONTROLLER, controllerFile);
            }

            // 前端 ts 配置
            if (isCreate(tsModelsOutPutFile)) {
                vmToFile(context, ConstVal.FREEMARKER_TEMPLATE_TS_MODELS, tsModelsOutPutFile);
            }
            if (isCreate(tsPagesOutPutFile)) {
                vmToFile(context, ConstVal.FREEMARKER_TEMPLATE_TS_PAGES, tsPagesOutPutFile);
            }
            if (isCreate(tsServicesOutPutFile)) {
                vmToFile(context, ConstVal.FREEMARKER_TEMPLATE_TS_PAGES_DATA, tsPagesDataOutPutFile);
            }
            if (isCreate(tsServicesOutPutFile)) {
                vmToFile(context, ConstVal.FREEMARKER_TEMPLATE_TS_PAGES_COMPONENT_CREATE, tsPagesCreateComponentOutPutFile);
            }
            if (isCreate(tsServicesOutPutFile)) {
                vmToFile(context, ConstVal.FREEMARKER_TEMPLATE_TS_PAGES_COMPONENT_UPDATE, tsPagesUpdateComponentOutPutFile);
            }
            if (isCreate(tsServicesOutPutFile)) {
                vmToFile(context, ConstVal.FREEMARKER_TEMPLATE_TS_SERVICES, tsServicesOutPutFile);
            }
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将模板转化成为文件
     * @param context      内容对象
     * @param templatePath 模板文件
     * @param outputFile   文件生成的目录
     */
    private void vmToFile(Map<String,Object> context, String templatePath, String outputFile) throws TemplateException{
        Configuration engine = getFreemarkerEngine();
        try{
            Template template = engine.getTemplate(templatePath, ConstVal.UTF8);
            System.out.println(template.getName());
            FileOutputStream fos = new FileOutputStream(outputFile);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, ConstVal.UTF8));
            template.process(context, writer);
            writer.close();
            log.info("模板:" + templatePath + ";  文件:" + outputFile);
        }catch (IOException e){
            log.error("【模板:" + templatePath + "】, 不存在");
            return;
        }
    }

    /**
     * 设置模版引擎，主要指向获取模版路径
     */
    private Configuration getFreemarkerEngine() {
        if (engine == null) {
            engine = new Configuration(Configuration.VERSION_2_3_25);
            try {
                //加载class内的资源
                //engine.setDirectoryForTemplateLoading(new File(FreemarkerGenerateMojo.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "/template/freemarker"));
                //加载jar包内的资源
                engine.setClassForTemplateLoading(FreemarkerGenerateMojo.class, ConstVal.TEMPLATE_VERSION);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return engine;
    }

    /**
     * 检测文件是否存在
     *
     * @return 是否
     */
    private boolean isCreate(String filePath) {
        File file = new File(filePath);
        return !file.exists() || isFileOverride();
    }
}
