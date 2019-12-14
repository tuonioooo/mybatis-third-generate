<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${entity}Mapper">
	<resultMap id="beanMap" type="${package.Entity}.${entity}Entity">
        <#list table.fields as field>
            <#if field.keyFlag?? >
            <#if  (field.keyFlag?string('yes', 'no'))=='yes'>
                <id column="${field.name}" property="${field.propertyName}" jdbcType="${field.capitalType}"/>
            <#else>
                <result column="${field.name}" property="${field.propertyName}" jdbcType="${field.capitalType}"/>
            </#if>
            </#if>
        </#list>
	</resultMap>
</mapper>
