# mybatis-third-generate 简介
* mybatis-plus 代码生成工具的 maven 插件版本
* 所有配置均包含在以下的xml中
```
<plugin>
        <!-- mvn mybatis-generate:fmToGenerate-->
				<groupId>com.hacker.mybatis</groupId>
				<artifactId>mybatis-third-generate</artifactId>
				<version>1.0</version>
				<configuration>
					<!-- 输出目录(默认java.io.tmpdir) -->
					<!--<outputDir>d:/mybatisplus</outputDir>-->
					<!-- 是否覆盖同名文件(默认false) -->
					<fileOverride>true</fileOverride>
					<!-- mapper.xml 中添加二级缓存配置(默认true) -->
					<enableCache>true</enableCache>
					<!-- 开发者名称 -->
					<author>tuonioooo</author>
					<!-- 数据源配置，(必配) -->
					<dataSource>
						<driverName>com.mysql.jdbc.Driver</driverName>
						<url>jdbc:mysql://127.0.0.1:3306/admin_backstage?useUnicode=true&amp;useSSL=false</url>
						<username>root</username>
						<password>root</password>
					</dataSource>
					<strategy>
						<!-- 字段生成策略，四种类型，从名称就能看出来含义
                        nochange(默认),
                        underline_to_camel,
                        remove_prefix,
                        remove_prefix_and_camel -->
						<naming>underline_to_camel</naming>
						<!-- ID策略 是LONG还是STRING类型(默认stringtype)-->
						<serviceIdType>longtype</serviceIdType>
						<!--Entity中的ID生成策略（默认 id_worker）-->
						<idGenType>auto</idGenType>
						<!--自定义超类-->
						<!--<superServiceClass>net.hyman.base.BaseService</superServiceClass>-->
						<!-- 要包含的表 与exclude 二选一配置-->
						<include>
							<property>wp_postmeta</property>
							<property>wp_posts</property>
							<!--
							<property>wp_article_temp</property>
							<property>wp_termmeta</property>
							<property>wp_term_taxonomy</property>
							<property>wp_term_relationships</property>
							-->
						</include>
						<!-- 要排除的表 -->
						<!--<exclude>-->
						<!--<property>schema_version</property>-->
						<!--<property>schema_version</property>-->
						<!--</exclude>-->
					</strategy>
					<packageInfo>
						<!-- 父级包名称，如果不写，下面的service等就需要写全包名(默认com.hacker) -->
						<parent>com.artron.ise.api</parent>
						<!--service包名(默认service)-->
						<service>service</service>
						<!--serviceImpl包名(默认service.impl)-->
						<serviceImpl>service.impl</serviceImpl>
						<!--entity包名(默认entity)-->
						<entity>bean</entity>
						<!--mapper包名(默认mapper)-->
						<mapper>mapper</mapper>
						<!--xml包名(默认mapper.xml)-->
						<xml>mapper.xml</xml>
						<!--controller包名(默认controller)-->
						<controller>controller</controller>
					</packageInfo>
					<student>
						<name>allen</name>
						<age>28</age>
					</student>
					<!-- 模板引擎配置 -->
				</configuration>
</plugin>
```
> 注意：使用该插件时，需要加入build中，还需配置具体mvn 运行命令：
1. mvn mybatis-generate:fmToGenerate  使用freemarker模板引擎生成代码
2. mvn mybatis-generate:generate      使用velocity模板引擎生成代码
