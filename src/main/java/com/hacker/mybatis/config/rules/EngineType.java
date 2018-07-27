package com.hacker.mybatis.config.rules;

/**
 * 引擎配置定义
 * @author tuonioooo
 */
public enum EngineType {

    FREEMARKER("freemarker"), VELOCITY("velocity");

    private String value;

    EngineType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}

