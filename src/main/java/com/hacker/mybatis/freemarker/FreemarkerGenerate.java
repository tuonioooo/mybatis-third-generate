//package com.hacker.mybatis.freemarker;
//
//
//import com.hacker.mybatis.AbstractGenerateMojo;
//import com.hacker.mybatis.PropertiesUtils;
//import com.hacker.mybatis.config.ConstVal;
//import com.hacker.mybatis.config.DataSourceConfig;
//import com.hacker.mybatis.config.PackageConfig;
//import com.hacker.mybatis.config.StrategyConfig;
//import com.hacker.mybatis.config.builder.ConfigBuilder;
//import com.hacker.mybatis.config.po.TableInfo;
//import freemarker.template.Configuration;
//import freemarker.template.Template;
//import freemarker.template.TemplateException;
//import org.apache.maven.plugin.MojoExecutionException;
//import org.apache.maven.plugin.MojoFailureException;
//
//import java.io.*;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
///**
// * Created by daizhao.
// * User: tony
// * Date: 2018-4-14
// * Time: 11:25
// * info: freemarker生成代码启动器
// */
//public class FreemarkerGenerate extends AbstractGenerateMojo {
//
//    private static Properties properties;
//
//    private static PackageConfig packageConfig;
//
//    private static DataSourceConfig dataSourceConfig;
//
//    private static StrategyConfig strategyConfig;
//
//    private static String outputDir;
//
//    private static boolean fileOverride;
//
//    private static boolean enableCache;
//
//    private static String author;
//
//    /**
//     * freemarker引擎
//     */
//    private Configuration engine;
//    /**
//     * 输出文件
//     */
//    private Map<String, String> outputFiles;
//
//    static {
//        properties = PropertiesUtils.getProperties("freemarker/init.properties");
//        packageConfig = new PackageConfig(properties);
//        dataSourceConfig = new DataSourceConfig(properties);
//        strategyConfig = new StrategyConfig(properties);
//        outputDir = properties.getProperty("outputDir");
//        fileOverride = properties.getProperty("fileOverride").equals("1");
//        enableCache = properties.getProperty("enableCache").equals("1");
//        author = properties.getProperty("author");
//    }
//
//    private static class FreemarkerGenerateInstance {
//        private static final FreemarkerGenerate instance = new FreemarkerGenerate();
//    }
//
//    public static FreemarkerGenerate getInstance() {
//        return FreemarkerGenerateInstance.instance;
//    }
//
//    private FreemarkerGenerate() {
//
//    }
//
//    @Override
//    public void execute() throws MojoExecutionException, MojoFailureException {
//        log.info("==========================准备生成文件...==========================");
//        // 初始化配置
//        initConfig();
//        // 初始化输出文件路径模板
//        initOutputFiles();
//        // 创建输出文件路径
//        mkdirs(config.getPathInfo());
//        // 获取上下文
//        Map<String, Map<String,Object>> ctxData = analyzeData(config);
//        // 循环生成文件
//        for (Map.Entry<String, Map<String,Object>> ctx : ctxData.entrySet()) {
//            batchOutput(ctx.getKey(), ctx.getValue());
//        }
//        //打开输出目录
//        try {
//            String osName = System.getProperty("os.name");
//            if (osName != null) {
//                if (osName.contains("Mac")) {
//                    Runtime.getRuntime().exec("open " + getOutputDir());
//                } else if (osName.contains("Windows")) {
//                    Runtime.getRuntime().exec("cmd /c start " + getOutputDir());
//                } else {
//                    log.info("文件输出目录:" + getOutputDir());
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        log.info("==========================文件生成完成！！！==========================");
//    }
//
//
//
//    /**
//     * 初始化配置
//     */
//    @Override
//    protected void initConfig() {
//        if (null == config) {
//            setOutputDir(outputDir);
//            config = new ConfigBuilder(packageConfig, dataSourceConfig, strategyConfig, getOutputDir());
//        }
//    }
//
//    /**
//     * 分析数据
//     *
//     * @param config 总配置信息
//     * @return 解析数据结果集
//     */
//    private Map<String, Map<String,Object>> analyzeData(ConfigBuilder config) {
//        List<TableInfo> tableList = config.getTableInfoList();
//        Map<String, String> packageInfo = config.getPackageInfo();
//        Map<String, Map<String,Object>> ctxData = new HashMap<String, Map<String,Object>>();
//        String superClass = config.getSuperClass().substring(config.getSuperClass().lastIndexOf(".") + 1);
//        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        for (TableInfo tableInfo : tableList) {
//            Map<String,Object> ctx = new HashMap<String, Object>();
//            ctx.put("package", packageInfo);
//            ctx.put("table", tableInfo);
//            ctx.put("entity", tableInfo.getEntityName());
//            ctx.put("idGenType", config.getIdType());
//            ctx.put("isStrategyKey", config.isStrategyKey());
//            ctx.put("idClassType", config.getIdClassType());
//            ctx.put("superClassPackage", config.getSuperClass());
//            ctx.put("superClass", superClass);
//            ctx.put("enableCache", enableCache);
//            ctx.put("author", author);
//            ctx.put("date", date);
//            ctxData.put(tableInfo.getEntityName(), ctx);
//        }
//        return ctxData;
//    }
//
//    /**
//     * 处理输出目录
//     *
//     * @param pathInfo 路径信息
//     */
//    private void mkdirs(Map<String, String> pathInfo) {
//        for (Map.Entry<String, String> entry : pathInfo.entrySet()) {
//            File dir = new File(entry.getValue());
//            if (!dir.exists()) {
//                boolean result = dir.mkdirs();
//                if (result) {
//                    log.info("创建目录： [" + entry.getValue() + "]");
//                }
//            }
//        }
//    }
//
//    /**
//     * 初始化输出目录
//     */
//    private void initOutputFiles() {
//        outputFiles = new HashMap<String, String>();
//        Map<String, String> pathInfo = config.getPathInfo();
//        outputFiles.put(ConstVal.ENTITY, pathInfo.get(ConstVal.ENTITY_PATH) + ConstVal.ENTITY_NAME);
//        outputFiles.put(ConstVal.MAPPER, pathInfo.get(ConstVal.MAPPER_PATH) + ConstVal.MAPPER_NAME);
//        outputFiles.put(ConstVal.QO, pathInfo.get(ConstVal.QO_PATH) + ConstVal.QO_NAME);
//        outputFiles.put(ConstVal.VO, pathInfo.get(ConstVal.VO_PATH) + ConstVal.VO_NAME);
//        outputFiles.put(ConstVal.XML, pathInfo.get(ConstVal.XML_PATH) + ConstVal.XML_NAME);
//        outputFiles.put(ConstVal.SERIVCE, pathInfo.get(ConstVal.SERIVCE_PATH) + ConstVal.SERVICE_NAME);
//        outputFiles.put(ConstVal.SERVICEIMPL, pathInfo.get(ConstVal.SERVICEIMPL_PATH) + ConstVal.SERVICEIMPL_NAME);
//        outputFiles.put(ConstVal.CONTROLLER, pathInfo.get(ConstVal.CONTROLLER_PATH) + ConstVal.CONTROLLER_NAME);
//    }
//
//    /**
//     * 合成上下文与模板
//     *
//     * @param context vm上下文
//     */
//    private void batchOutput(String entityName, Map<String,Object> context) {
//        try {
//            String entityFile = String.format(outputFiles.get(ConstVal.ENTITY), entityName);
//            String xmlFile = String.format(outputFiles.get(ConstVal.XML), entityName);
//            String serviceFile = String.format(outputFiles.get(ConstVal.SERIVCE), entityName);
//            String controllerFile = String.format(outputFiles.get(ConstVal.CONTROLLER), entityName);
//
//            // 根据override标识来判断是否需要创建文件
//            if (isCreate(entityFile)) {
//                vmToFile(context, ConstVal.FREEMARKER_TEMPLATE_ENTITY, entityFile);
//            }
//            if (isCreate(xmlFile)) {
//                vmToFile(context, ConstVal.FREEMARKER_TEMPLATE_MAPPER_XML, xmlFile);
//            }
//            if (isCreate(serviceFile)) {
//                vmToFile(context, ConstVal.FREEMARKER_TEMPLATE_SERVICE, serviceFile);
//            }
//            if (isCreate(controllerFile)) {
//                vmToFile(context, ConstVal.FREEMARKER_TEMPLATE_CONTROLLER, controllerFile);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (TemplateException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 将模板转化成为文件
//     * @param context      内容对象
//     * @param templatePath 模板文件
//     * @param outputFile   文件生成的目录
//     */
//    private void vmToFile(Map<String,Object> context, String templatePath, String outputFile) throws IOException, TemplateException {
//        Configuration freemarker = getFreemarkerEngine();
//        Template template = engine.getTemplate(templatePath, ConstVal.UTF8);
//        System.out.println(template.getName());
//        FileOutputStream fos = new FileOutputStream(outputFile);
//        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, ConstVal.UTF8));
//        template.process(context, writer);
//        writer.close();
//        log.info("模板:" + templatePath + ";  文件:" + outputFile);
//    }
//
//    /**
//     * 设置模版引擎，主要指向获取模版路径
//     */
//    private Configuration getFreemarkerEngine() {
//        if (engine == null) {
//            engine = new Configuration();
//            try {
//                //加载class内的资源
//                //engine.setDirectoryForTemplateLoading(new File(FreemarkerGenerateMojo.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "/template/freemarker"));
//                //加载jar包内的资源
//                engine.setClassForTemplateLoading(FreemarkerGenerateMojo.class,"/template/freemarker");
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        return engine;
//    }
//
//    /**
//     * 检测文件是否存在
//     *
//     * @return 是否
//     */
//    private boolean isCreate(String filePath) {
//        File file = new File(filePath);
//        return !file.exists() || isFileOverride();
//    }
//
//    public static void main(String[] args) {
//        FreemarkerGenerate freemarkerGenerate = FreemarkerGenerate.getInstance();
//        try {
//            freemarkerGenerate.execute();
//        } catch (MojoExecutionException e) {
//            e.printStackTrace();
//        } catch (MojoFailureException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
