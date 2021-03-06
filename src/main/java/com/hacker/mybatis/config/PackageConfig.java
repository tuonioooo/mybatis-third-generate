package com.hacker.mybatis.config;

import org.apache.maven.plugins.annotations.Parameter;

import java.util.Properties;

/**
 * 跟包相关的配置项
 *
 * @author YangHu
 * @since 2016/8/30
 */
public class PackageConfig {

    /**
     * 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
     */
    @Parameter(defaultValue = "com.baomidou")
    private String parent;

    /**
     * Entity包名
     */
    @Parameter(defaultValue = "entity")
    private String entity;

    /**
     * Service包名
     */
    @Parameter(defaultValue = "service")
    private String service;

    /**
     * Service Impl包名
     */
    @Parameter(defaultValue = "service.impl")
    private String serviceImpl;
    /**
     * Mapper包名
     */
    @Parameter(defaultValue = "mapper")
    private String mapper;

    /**
     * qo包名
     */
    @Parameter(defaultValue = "qo")
    private String qo;

    /**
     * vo包名
     */
    @Parameter(defaultValue = "vo")
    private String vo;

    /**
     * Controller包名
     */
    @Parameter(defaultValue = "controller")
    private String controller;

    /**
     * Mapper XML包名
     */
    @Parameter(defaultValue = "mapper.xml")
    private String xml;

    /**
     * ts pages 包名
     */
    @Parameter(defaultValue = "pages")
    private String tsPages;


    @Parameter(defaultValue = "pages.component")
    private String tsPagesComponent;
    /**
     * ts models 包名
     */
    @Parameter(defaultValue = "models")
    private String tsModels;

    /**
     * ts services 包名
     */
    @Parameter(defaultValue = "services")
    private String tsServices;

    public String getParent() {
        return parent;
    }

    public String getEntity() {
        return entity;
    }

    public String getService() {
        return service;
    }

    public String getServiceImpl() {
        return serviceImpl;
    }

    public String getMapper() {
        return mapper;
    }

    public String getQo() {
        return qo;
    }

    public String getVo() {
        return vo;
    }

    public String getXml() {
        return xml;
    }

    public String getController() {
        return controller;
    }

    public String getTsPages() {
        return tsPages;
    }

    public String getTsPagesComponent() {
        return tsPagesComponent;
    }

    public String getTsModels() {
        return tsModels;
    }

    public String getTsServices() {
        return tsServices;
    }


    public PackageConfig() {

    }

    public PackageConfig(Properties properties) {
        this.parent = properties.getProperty("packageConfig.parent");
        this.entity = properties.getProperty("packageConfig.entity");
        this.service = properties.getProperty("packageConfig.service");
        this.serviceImpl = properties.getProperty("packageConfig.serviceImpl");
        this.mapper = properties.getProperty("packageConfig.mapper");
        this.xml = properties.getProperty("packageConfig.xml");
        this.controller = properties.getProperty("packageConfig.controller");
    }
}
