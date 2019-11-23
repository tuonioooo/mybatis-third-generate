package com.hacker.mybatis.config;

import java.io.File;
import java.nio.charset.Charset;

/**
 * 定义常量
 *
 * @author YangHu
 * @since 2016/8/31
 */
public class ConstVal {

    public static final String TABLE_SUFFIX = "_t";

    public static final String TABLE_SUFFIX_ENTITY = "T";

    public static final String DEFAULT_MIDDLE_DIR = "src\\test\\java";
    public static final String DEFAULT_PACKAGE_PARENT = "com.hacker";

    public static final String PACKAGE_PARENT = "Parent";
    public static final String ENTITY = "Entity";
    public static final String SERIVCE = "Service";
    public static final String SERVICEIMPL = "ServiceImpl";
    public static final String CONTROLLER = "Controller";
    public static final String MAPPER = "Mapper";
    public static final String XML = "Xml";
    public static final String HTML = "Html";



    public static final String ENTITY_PATH = "entity_path";
    public static final String SERIVCE_PATH = "serivce_path";
    public static final String SERVICEIMPL_PATH = "serviceimpl_path";
    public static final String MAPPER_PATH = "mapper_path";
    public static final String XML_PATH = "xml_path";
    public static final String CONTROLLER_PATH = "controller_path";
    public static final String HTML_PATH = "html_path";

    public static final String JAVA_TMPDIR = "java.io.tmpdir";
    public static final String UTF8 = Charset.forName("UTF-8").name();
    public static final String UNDERLINE = "_";

    public static final String JAVA_SUFFIX = ".java";
    public static final String XML_SUFFIX = ".xml";

    public static final String TEMPLATE_ENTITY = "/template/entity.java.vm";
    public static final String TEMPLATE_MAPPER = "/template/mapper.java.vm";
    public static final String TEMPLATE_XML = "/template/mapper.xml.vm";
    public static final String TEMPLATE_SERVICE = "/template/service.java.vm";
    public static final String TEMPLATE_SERVICEIMPL = "/template/serviceImpl.java.vm";

    public static final String FREEMARKER_TEMPLATE_ENTITY = "/entity.java.ftl";
    public static final String FREEMARKER_TEMPLATE_MAPPER = "/mapper.java.ftl";
    public static final String FREEMARKER_TEMPLATE_MAPPER_XML = "/mapper.xml.ftl";
    public static final String FREEMARKER_TEMPLATE_SERVICE = "/service.java.ftl";
    public static final String FREEMARKER_TEMPLATE_CONTROLLER = "/controller.java.ftl";

    public static final String ENTITY_NAME = File.separator + "%s" + JAVA_SUFFIX;
    public static final String MAPPER_NAME = File.separator + "%s" + MAPPER + JAVA_SUFFIX;
    public static final String XML_NAME = File.separator + "%s" + MAPPER + XML_SUFFIX;
    public static final String SERVICE_NAME = File.separator + "%s" + SERIVCE + JAVA_SUFFIX;
    public static final String SERVICEIMPL_NAME = File.separator + "%s" + SERVICEIMPL + JAVA_SUFFIX;
    public static final String CONTROLLER_NAME = File.separator + "%s" + CONTROLLER + JAVA_SUFFIX;

    // 配置使用classloader加载资源
    public static final String VM_LOADPATH_KEY = "file.resource.loader.class";
    public static final String VM_LOADPATH_VALUE = "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";
}
