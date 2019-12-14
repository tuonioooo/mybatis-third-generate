<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${entity}Mapper">

	<sql id="${entity?uncap_first}"> ${table.name} </sql>

	<sql id="base_column">
	    <#list table.fields as field>
            ${field.name}<#sep>,</#sep>
        </#list>
	</sql>

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

    <!-- 插入 -->
    <#if isStrategyKey>
    <insert id="insert" parameterType="${entity}">
        insert into <include refid="${entity?uncap_first}"/>  (
        <#list table.fields as field>
            <#if field.name??>
                ${field.name}<#sep>,</#sep>
            <#else>
            </#if>
        </#list>
        ) values (
        <#list table.fields as field>
            <#if field.name??>
                ${r'#{'}${field.propertyName}}<#sep>,</#sep>
            <#else>
            </#if>
        </#list>
        )
    </insert>
    <#else>
    <insert id="insert" parameterType="${entity}" keyProperty="${table.PRI}" useGeneratedKeys="true">
        insert into <include refid="${entity?uncap_first}"/>  (
        <#list table.fields as field>
            <#if !field.keyFlag>
                ${field.name}<#sep>,</#sep>
            <#else>
            </#if>
        </#list>
        ) values (
        <#list table.fields as field>
            <#if !field.keyFlag>
                ${r'#{'}${field.propertyName}}<#sep>,</#sep>
            <#else>
            </#if>
        </#list>
        )
    </insert>
    </#if>
	<!-- 更新 -->
    <update id="update" parameterType="${entity}">
        update <include refid="${entity?uncap_first}"/>
        <set>
        <#list table.fields as field>
            <#if field.name??>
                <if test="${field.propertyName} != null">
                    ${field.name} = ${r'#{'}${field.propertyName}}<#sep>,</#sep>
                </if>
            <#else>
            </#if>
        </#list>
        </set>
        <where>
            <#list table.fields as field>
                <#if field.keyFlag>
                    ${field.name} = ${r'#{'}${field.propertyName}}
                </#if>
            </#list>
        </where>
    </update>

    <!--分页-->
    <select id="findByListPage" parameterType="page" resultMap="beanMap">
         select <include refid="base_column"/>
         from   <include refid="${entity?uncap_first}"/>
         <where>
            <include refid="page_condition_sql"/>
        </where>
    </select>

    <!--查询-->
    <select id="get" parameterType="${idClassType}" resultMap="beanMap">
        select <include refid="base_column"/>
        from   <include refid="${entity?uncap_first}"/>
        <where>
            <#list table.fields as field>
                <#if field.keyFlag>
                    ${field.name} = ${r'#{'}${field.propertyName}}
                </#if>
            </#list>
        </where>
    </select>

    <!--删除-->
    <delete id="delete" parameterType="${idClassType}" flushCache="true">
        DELETE FROM <include refid="${entity?uncap_first}"/>
        <where>
           <#list table.fields as field>
               <#if field.keyFlag>
                   ${field.name} = ${r'#{'}${field.propertyName}}
               </#if>
           </#list>
        </where>
    </delete>

    <!-- 动态组装复合查询条件 -->
    <sql id="page_condition_sql">
        <!-- Equal query -->
        <if test="pd.id != null and pd.id != ''"> and id= ${r"#{pd.id}"}</if>
        <if test="pd.username != null and pd.username != ''"> and username like CONCAT(CONCAT('%', ${r"#{pd.username}"}), '%')</if>
    </sql>
</mapper>