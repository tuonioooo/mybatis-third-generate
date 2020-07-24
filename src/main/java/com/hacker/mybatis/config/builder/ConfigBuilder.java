package com.hacker.mybatis.config.builder;


import com.hacker.mybatis.config.ConstVal;
import com.hacker.mybatis.config.DataSourceConfig;
import com.hacker.mybatis.config.PackageConfig;
import com.hacker.mybatis.config.StrategyConfig;
import com.hacker.mybatis.config.po.TableField;
import com.hacker.mybatis.config.po.TableInfo;
import com.hacker.mybatis.config.rules.*;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 配置汇总 传递给文件生成工具
 *
 * @author YangHu
 * @since 2016/8/30
 */
public class ConfigBuilder {

    /**
     * SQL连接
     */
    private Connection connection;
    /**
     * SQL语句类型
     */
    private QuerySQL querySQL;
    /**
     * service超类定义
     */
    private String superClass;
    /**
     * ID的生成策略字符串类型
     */
    private String idType;

    /**
     * ID的类型
     */
    private String idClassType;

    /**
     * 数据库表信息
     */
    private List<TableInfo> tableInfoList;

    /**
     * 包配置详情
     */
    private Map<String, String> packageInfo;
    /**
     * 路径配置信息
     */
    private Map<String, String> pathInfo;


    /**
     * 在构造器中处理配置
     *
     * @param outputDir        输出目录
     * @param packageConfig    包配置
     * @param dataSourceConfig 数据源配置
     * @param strategyConfig   表配置
     */
    public ConfigBuilder(PackageConfig packageConfig, DataSourceConfig dataSourceConfig,
                         StrategyConfig strategyConfig, String outputDir) {
        handlerDataSource(dataSourceConfig);
        handlerStrategy(strategyConfig);
        handlerPackage(outputDir, packageConfig);
    }

    //************************ 曝露方法 BEGIN*****************************

    /**
     * 所有包配置信息
     *
     * @return 包配置
     */
    public Map<String, String> getPackageInfo() {
        return packageInfo;
    }

    /**
     * 所有路径配置
     *
     * @return 路径配置
     */
    public Map<String, String> getPathInfo() {
        return pathInfo;
    }

    /**
     * 获取超类定义
     *
     * @return 完整超类名称
     */
    public String getSuperClass() {
        return superClass;
    }

    /**
     * 获取id生成方式
     *
     * @return id生成方式
     */
    public String getIdType() {
        return idType;
    }

    /**
     * 判断是否是自定义ID生成策略
     * 自定义生成策略：id_worker("ID_WORKER"), uuid("UUID"), input("INPUT");
     * 自动生成策略：auto("AUTO")
     *
     * @return
     */
    public Boolean isStrategyKey() {
        return idType == IdStrategy.uuid.getValue() ||
                idType == IdStrategy.id_worker.getValue() ||
                idType == IdStrategy.input.getValue();
    }

    /**
     * 表信息
     *
     * @return 所有表信息
     */
    public List<TableInfo> getTableInfoList() {
        return tableInfoList;
    }

    //****************************** 曝露方法 END**********************************

    /**
     * 处理包配置
     *
     * @param config PackageConfig
     */
    private void handlerPackage(String outputDir, PackageConfig config) {
        packageInfo = new HashMap<String, String>();
        String package_parent = StringUtils.isEmpty(config.getParent()) ? ConstVal.DEFAULT_PACKAGE_PARENT : config.getParent();
        packageInfo.put(ConstVal.PACKAGE_PARENT, package_parent);
        packageInfo.put(ConstVal.ENTITY, joinPackage(package_parent, config.getEntity()));
        packageInfo.put(ConstVal.MAPPER, joinPackage(package_parent, config.getMapper()));
        packageInfo.put(ConstVal.QO, joinPackage(package_parent, config.getQo()));
        packageInfo.put(ConstVal.VO, joinPackage(package_parent, config.getVo()));
        packageInfo.put(ConstVal.XML, joinPackage(package_parent, config.getXml()));
        packageInfo.put(ConstVal.SERIVCE, joinPackage(package_parent, config.getService()));
        packageInfo.put(ConstVal.SERVICEIMPL, joinPackage(package_parent, config.getServiceImpl()));
        packageInfo.put(ConstVal.CONTROLLER, joinPackage(package_parent, config.getController()));



        //前端ts 配置
        String package_web_parent = StringUtils.isEmpty(config.getParent()) ? ConstVal.DEFAULT_WEB_PACKAGE_PARENT : config.getParent();
        packageInfo.put(ConstVal.TS_MODELS, joinPackage(package_web_parent, config.getTsModels()));
        // 根据表字段处理动态包
        for (TableInfo tableInfo: tableInfoList){
            String dymaicPagesPackage = config.getTsPages().concat(".").concat(toLowerCaseFirst(tableInfo.getTsNameUpperFirst()));
            String dymaicComponentsPackage = dymaicPagesPackage.concat(".").concat(toLowerCaseFirst(config.getTsPagesComponent()));
            packageInfo.put(ConstVal.TS_PAGES.concat(tableInfo.getName()), joinPackage(package_web_parent, dymaicPagesPackage));
            packageInfo.put(ConstVal.TS_PAGES_COMPONENT.concat(tableInfo.getName()), joinPackage(package_web_parent, dymaicComponentsPackage));

        }
        packageInfo.put(ConstVal.TS_SERVICES, joinPackage(package_web_parent, config.getTsServices()));




        pathInfo = new HashMap<String, String>();

        pathInfo.put(ConstVal.ENTITY_PATH, joinPath(outputDir, packageInfo.get(ConstVal.ENTITY)));
        pathInfo.put(ConstVal.MAPPER_PATH, joinPath(outputDir, packageInfo.get(ConstVal.MAPPER)));
        pathInfo.put(ConstVal.QO_PATH, joinPath(outputDir, packageInfo.get(ConstVal.QO)));
        pathInfo.put(ConstVal.VO_PATH, joinPath(outputDir, packageInfo.get(ConstVal.VO)));
        pathInfo.put(ConstVal.XML_PATH, joinPath(outputDir, packageInfo.get(ConstVal.XML)));
        pathInfo.put(ConstVal.SERIVCE_PATH, joinPath(outputDir, packageInfo.get(ConstVal.SERIVCE)));
        pathInfo.put(ConstVal.SERVICEIMPL_PATH, joinPath(outputDir, packageInfo.get(ConstVal.SERVICEIMPL)));
        pathInfo.put(ConstVal.CONTROLLER_PATH, joinPath(outputDir, packageInfo.get(ConstVal.CONTROLLER)));


        //前端ts 配置
        pathInfo.put(ConstVal.TS_MODELS_PATH, joinPath(outputDir, packageInfo.get(ConstVal.TS_MODELS)));
        // 根据表字段处理动态输出路径
        for (TableInfo tableInfo: tableInfoList){
            pathInfo.put(ConstVal.TS_PAGES_PATH.concat(tableInfo.getName()), joinPath(outputDir, packageInfo.get(ConstVal.TS_PAGES.concat(tableInfo.getName()))));
            pathInfo.put(ConstVal.TS_PAGES_COMPONENT_PATH.concat(tableInfo.getName()), joinPath(outputDir, packageInfo.get(ConstVal.TS_PAGES_COMPONENT.concat(tableInfo.getName()))));
        }
        pathInfo.put(ConstVal.TS_SERVICES_PATH, joinPath(outputDir, packageInfo.get(ConstVal.TS_SERVICES)));
    }

    /**
     * 处理数据源配置
     *
     * @param config DataSourceConfig
     */
    private void handlerDataSource(DataSourceConfig config) {
        connection = config.getConn();
        querySQL = getQuerySQL(config.getDbType());
    }

    /**
     * 处理数据库表
     * 加载数据库表、列、注释相关数据集
     *
     * @param config StrategyConfig
     */
    private void handlerStrategy(StrategyConfig config) {
        processTypes(config);
        tableInfoList = getTablesInfo(config);
    }

    /**
     * 处理superClassName,IdClassType,IdStrategy配置
     *
     * @param config 策略配置
     */
    private void processTypes(StrategyConfig config) {
        if (StringUtils.isBlank(config.getSuperServiceClass())) {
            if (IdClassType.longtype == config.getServiceIdType()) {
                superClass = IdClassType.longtype.getPakageName();
            } else {
                superClass = IdClassType.stringtype.getPakageName();
            }
        } else {
            superClass = config.getSuperServiceClass();
        }

        if (config.getIdGenType() == IdStrategy.auto) {
            idType = IdStrategy.auto.getValue();
        } else if (config.getIdGenType() == IdStrategy.input) {
            idType = IdStrategy.input.getValue();
        } else if (config.getIdGenType() == IdStrategy.uuid) {
            idType = IdStrategy.uuid.getValue();
        } else {
            idType = IdStrategy.id_worker.getValue();
        }

        if (config.getServiceIdType() == IdClassType.longtype) {
            idClassType = IdClassType.longtype.getType();
        } else if (config.getServiceIdType() == IdClassType.stringtype) {
            idClassType = IdClassType.stringtype.getType();
        } else {

        }
    }


    /**
     * 处理表对应的类名称
     *
     * @param tableList 表名称
     * @param strategy  命名策略
     * @return 补充完整信息后的表
     */
    private List<TableInfo> processTable(List<TableInfo> tableList, NamingStrategy strategy) {
        for (TableInfo tableInfo : tableList) {
            String tempName = handleSuffixT(capitalFirst(processName(tableInfo.getName(), strategy)));
            tableInfo.setEntityName(tempName);
            tableInfo.setTsNameUpperFirst(tempName);
            tableInfo.setMapperName(tableInfo.getEntityName() + ConstVal.MAPPER);
            tableInfo.setQoName(tableInfo.getEntityName() + ConstVal.QO);
            tableInfo.setVoName(tableInfo.getEntityName() + ConstVal.VO);
            tableInfo.setXmlName(tableInfo.getMapperName());
            tableInfo.setServiceName(tableInfo.getEntityName() + ConstVal.SERIVCE);
            tableInfo.setServiceImplName(tableInfo.getEntityName() + ConstVal.SERVICEIMPL);
        }
        return tableList;
    }

    /**
     * 获取所有的数据库表信息
     *
     * @return 表信息
     */
    private List<TableInfo> getTablesInfo(StrategyConfig config) {
        boolean isInclude = (null != config.getInclude() && config.getInclude().length > 0);
        boolean isExclude = (null != config.getExclude() && config.getExclude().length > 0);
        if (isInclude && isExclude) {
            throw new RuntimeException("<strategy> 标签中 <include> 与 <exclude> 只能配置一项！");
        }
        List<TableInfo> tableList = new ArrayList<TableInfo>();
        Set<String> notExistTables = new HashSet<String>();
        NamingStrategy strategy = config.getNaming();
        PreparedStatement pstate = null;
        try {
            pstate = connection.prepareStatement(querySQL.getTableCommentsSql());
            ResultSet results = pstate.executeQuery();
            while (results.next()) {
                String tableName = results.getString(querySQL.getTableName());
                if (StringUtils.isNotBlank(tableName)) {
                    String tableComment = results.getString(querySQL.getTableComment());
                    TableInfo tableInfo = new TableInfo();
                    if (isInclude) {
                        for (String includeTab : config.getInclude()) {
                            if (includeTab.equalsIgnoreCase(tableName)) {
                                tableInfo.setName(tableName);
                                tableInfo.setComment(tableComment);
                            } else {
                                notExistTables.add(includeTab);
                            }
                        }
                    } else if (isExclude) {
                        for (String excludeTab : config.getExclude()) {
                            if (!excludeTab.equalsIgnoreCase(tableName)) {
                                tableInfo.setName(tableName);
                                tableInfo.setComment(tableComment);
                            } else {
                                notExistTables.add(excludeTab);
                            }
                        }
                    } else {
                        tableInfo.setName(tableName);
                        tableInfo.setComment(tableComment);
                    }
                    if (StringUtils.isNotBlank(tableInfo.getName())) {
                        List<TableField> fieldList = getListFields(tableInfo.getName(), strategy);
                        tableInfo.setFields(fieldList);
                        tableList.add(tableInfo);
                    }
                } else {
                    System.err.println("当前数据库为空！！！");
                }
            }
            // 将已经存在的表移除
            for (TableInfo tabInfo : tableList) {
                notExistTables.remove(tabInfo.getName());
            }
            if (notExistTables.size() > 0) {
                System.err.println("表 " + notExistTables + " 在数据库中不存在！！！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //释放资源
            try {
                if (pstate != null) {
                    pstate.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return processTable(tableList, strategy);
    }

    /**
     * 将字段信息与表信息关联
     *
     * @param tableName 表名称
     * @param strategy  命名策略
     * @return 表信息
     */
    private List<TableField> getListFields(String tableName, NamingStrategy strategy) throws SQLException {
        boolean havedId = false;

        // 此处为了兼容Oracle查询语句， 参数格式的时候放两个tableName
        PreparedStatement pstate =
                connection.prepareStatement(String.format(querySQL.getTableFieldsSql(), tableName, tableName));
        ResultSet results = pstate.executeQuery();

        List<TableField> fieldList = new ArrayList<TableField>();
        while (results.next()) {
            TableField field = new TableField();
            String key = results.getString(querySQL.getFieldKey());
            // 避免多重主键设置，目前只取第一个找到ID，并放到list中的索引为0的位置
            boolean isId = StringUtils.isNotBlank(key) && key.toUpperCase().equals("PRI");
            //处理ID
            if (isId && !havedId) {
                field.setKeyFlag(true);
                havedId = true;
            } else {
                field.setKeyFlag(false);
            }
            //处理其它信息
            field.setName(results.getString(querySQL.getFieldName()));
            field.setType(results.getString(querySQL.getFieldType()));
            //转换大写类型
            field.setCapitalType(processMySqlTypeToUpperCase(processFiledType(field.getType())));
            field.setPropertyName(processName(field.getName(), strategy));
            field.setPropertyType(processFiledType(field.getType()));
            field.setTsPropertyType(processMySqlToTypeScriptType(processFiledType(field.getType())));
            field.setComment(results.getString(querySQL.getFieldComment()));
            fieldList.add(field);
        }
        return fieldList;
    }

    public static void main(String[] args) {

        String str = "int(10)";

        System.out.println("str.substring(0, str.indexOf(\"(\")) = " + str.substring(0, str.indexOf("(")));

    }


    /**
     * 连接路径字符串
     *
     * @param parentDir   路径常量字符串
     * @param packageName 包名
     * @return 连接后的路径
     */
    private String joinPath(String parentDir, String packageName) {
        if (StringUtils.isEmpty(parentDir)) {
            parentDir = System.getProperty(ConstVal.JAVA_TMPDIR);
        }
        if (!StringUtils.endsWith(parentDir, File.separator)) {
            parentDir += File.separator;
        }
        packageName = packageName.replaceAll("\\.", "\\" + File.separator);
        return parentDir + packageName;
    }

    /**
     * 连接父子包名
     *
     * @param parent     父包名
     * @param subPackage 子包名
     * @return 连接后的包名
     */
    private String joinPackage(String parent, String subPackage) {
        if (StringUtils.isBlank(parent)) {
            return subPackage;
        }
        return parent + "." + subPackage;
    }

    /**
     * 处理字段类型
     *
     * @return 转换成JAVA包装类型
     */
    private String processFiledType(String type) {
        if (QuerySQL.MYSQL == querySQL) {
            return processMySqlType(type);
        } else if (QuerySQL.ORACLE == querySQL) {
            return processOracleType(type);
        }
        return null;
    }

    /**
     * 处理字段名称
     *
     * @return 根据策略返回处理后的名称
     */
    private String processName(String name, NamingStrategy strategy) {
        name = name.toLowerCase();
        String propertyName = "";
        if (strategy == NamingStrategy.remove_prefix_and_camel) {
            propertyName = NamingStrategy.removePrefixAndCamel(name);
        } else if (strategy == NamingStrategy.underline_to_camel) {
            propertyName = NamingStrategy.underlineToCamel(name);
        } else if (strategy == NamingStrategy.remove_prefix) {
            propertyName = NamingStrategy.removePrefix(name);
        } else {
            propertyName = name;
        }
        return propertyName;
    }

    /**
     * MYSQL字段类型转换
     *
     * @param type 字段类型
     * @return JAVA类型
     */
    private String processMySqlType(String type) {
        String t = type.toLowerCase();
        if (t.contains("char")) {
            return "String";
        } else if (t.contains("bigint")) {
            return "Long";
        } else if (t.contains("int")) {
            return "Integer";
        } else if (t.contains("date") || t.contains("timestamp")) {
            return "Date";
        } else if (t.contains("text")) {
            return "String";
        } else if (t.contains("bit")) {
            return "Boolean";
        } else if (t.contains("decimal")) {
            return "BigDecimal";
        } else if (t.contains("blob")) {
            return "byte[]";
        } else if (t.contains("float")) {
            return "Float";
        } else if (t.contains("double")) {
            return "Double";
        }
        return "String";
    }

    /**
     * MYSQL 数据表字段类型转换
     * @param type 字段类型
     * @return JAVA类型
     */
    private String processMySqlTypeToUpperCase(String type) {
        String t = type.toLowerCase();
        if (t.contains("char")) {
            return "VARCHAR";
        } else if (t.contains("bigint")) {
            return "LONG";
        } else if (t.contains("int")) {
            return "INTEGER";
        } else if (t.contains("date") || t.contains("timestamp")) {
            return "DATE";
        } else if (t.contains("text")) {
            return "String";
        } else if (t.contains("bit")) {
            return "BOOLEAN";
        } else if (t.contains("decimal")) {
            return "BIGDECIMAL";
        } else if (t.contains("blob")) {
            return "byte[]";
        } else if (t.contains("float")) {
            return "FLOAT";
        } else if (t.contains("double")) {
            return "DOUBLE";
        }
        return "VARCHAR";
    }

    /**
     * MYSQL字段类型转换 typscript
     *
     * @param type 字段类型
     * @return JAVA类型
     */
    private String processMySqlToTypeScriptType(String type) {
        if (type.equalsIgnoreCase("string")) {
            return "string";
        } else if (type.equalsIgnoreCase("long") ||
                type.equalsIgnoreCase("integer") ||
                type.equalsIgnoreCase("float") ||
                type.equalsIgnoreCase("double") ||
                type.equalsIgnoreCase("BigDecimal")) {
            return "number";
        } else if (type.equalsIgnoreCase("date")) {
            return "Date";
        } else if (type.equalsIgnoreCase("boolean")) {
            return "boolean";
        }
        return "any";
    }

    /**
     * ORACLE字段类型转换
     *
     * @param type 字段类型
     * @return JAVA类型
     */
    private String processOracleType(String type) {
        String t = type.toUpperCase();
        if (t.contains("CHAR")) {
            return "String";
        } else if (t.contains("DATE") || t.contains("TIMESTAMP")) {
            return "Date";
        } else if (t.contains("NUMBER")) {
            return "Double";
        } else if (t.contains("FLOAT")) {
            return "Float";
        } else if (t.contains("BLOB")) {
            return "Object";
        } else if (t.contains("RAW")) {
            return "byte[]";
        }
        return "String";
    }

    /**
     * 获取当前的SQL类型
     *
     * @return DB类型
     */
    private QuerySQL getQuerySQL(DbType dbType) {
        for (QuerySQL qs : QuerySQL.values()) {
            if (qs.getDbType().equals(dbType.getValue())) {
                return qs;
            }
        }
        return QuerySQL.MYSQL;
    }

    /**
     * 实体首字母大写
     * @param name 待转换的字符串
     * @return 转换后的字符串
     */
    private String capitalFirst(String name) {
        if (StringUtils.isNotBlank(name)) {
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        return "";
    }

    /**
     * 处理结尾为T的表
     * @param name
     * @return
     */
    private String handleSuffixT(String name){
        if (StringUtils.isBlank(name)) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        if(name.endsWith("t") || name.endsWith("T") || name.endsWith("0")){
            stringBuilder.append(name, 0, name.length()-1);
        }
        return stringBuilder.toString();
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

    public Connection getConnection() {
        return connection;
    }

    public QuerySQL getQuerySQL() {
        return querySQL;
    }

    public String getIdClassType() {
        return idClassType;
    }
}
