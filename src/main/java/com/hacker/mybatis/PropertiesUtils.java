package com.hacker.mybatis;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * 对属性文件操作的工具类
 * 获取，新增，修改
 * 注意：	以下方法读取属性文件会缓存问题,在修改属性文件时，不起作用，
 *　InputStream in = PropertiesUtils.class.getResourceAsStream("/config.properties");
 *　解决办法：
 *　String savePath = PropertiesUtils.class.getResource("/config.properties").getPath();
 * 说明：
 * 如果是打成Jar包
 * 在jar文件中查找资源和在文件系统中查找资源的方式是不一样的，尽量使用Stream流的方式操作资源文件。 
 * 请用InputStream in = PropertiesUtils.class.getResourceAsStream("/config.properties");
 */
public class PropertiesUtils {

	/**
	 * 获取属性文件的数据 根据key获取值
	 * @param fileName 文件名　(注意：加载的是src下的文件,如果在某个包下．请把包名加上)
	 * @param key
	 * @return
	 */
	public static String findPropertiesKey(String fileName, String key) {

		try {
			Properties prop = getProperties(fileName);
			return prop.getProperty(key);
		} catch (Exception e) {
			return "";
		}

	}

	/**
	 * 返回　Properties
	 * @param fileName 文件名　(注意：加载的是src下的文件,如果在某个包下．请把包名加上)
	 * @param 
	 * @return
	 */
	public static Properties getProperties(String fileName) {
		Properties prop = new Properties();
		String savePath = PropertiesUtils.class.getResource(fileName).getPath();
		//以下方法读取属性文件会缓存问题
		//		InputStream in = PropertiesUtils.class
		//				.getResourceAsStream("/config.properties");
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(savePath));
			prop.load(in);
		} catch (Exception e) {
			return null;
		}
		return prop;
	}
	
	/**
	 * 返回　Properties
	 * @param fileName 文件名　(注意：加载的是src下的文件,如果在某个包下．请把包名加上)
	 * @param 
	 * @return
	 */
	public static Properties loadProperties(String fileName) {
		Properties prop = new Properties();
		//以下方法读取属性文件会缓存问题
		//在jar文件中查找资源和在文件系统中查找资源的方式是不一样的，尽量使用Stream流的方式操作资源文件。
		//		InputStream in = PropertiesUtils.class
		//				.getResourceAsStream("/config.properties");
		//prop.load(new FileInputStream(PropertiesUtils.class.getResource(fileName).getFile()));
		
		try {
			InputStream in = PropertiesUtils.class.getResourceAsStream(fileName);
			prop.load(in);
			
		} catch (Exception e) {
			return null;
		}
		return prop;
	}
	
	public static Properties loadProperties2(String fileName) {
		Properties prop = new Properties();
		try {
			InputStream in = PropertiesUtils.class.getResourceAsStream(fileName);
			prop.load(in);
		} catch (Exception e) {
			return null;
		}
		return prop;
	}
	
	
	public static void main(String[] args) {
		Properties pros = PropertiesUtils.loadProperties("/config/application.properties");
		System.out.println(pros);
	}

	public static Properties getjdbcProperties() {
		Properties prop = new Properties();
		String savePath = PropertiesUtils.class.getResource("/jdbc.properties").getPath();
		//以下方法读取属性文件会缓存问题
		//		InputStream in = PropertiesUtils.class
		//				.getResourceAsStream("/config.properties");
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(savePath));
			prop.load(in);
			in.close();
		} catch (Exception e) {
			return null;
		}
		return prop;
	}

	/**
	 * 写入properties信息
	 * 
	 * @param key
	 *            名称
	 * @param value
	 *            值
	 */
	public static void modifyProperties(String key, String value) {
		try {
			// 从输入流中读取属性列表（键和元素对）
			Properties prop = getProperties("/config.properties");
			prop.setProperty(key, value);
			String path = PropertiesUtils.class.getResource("/config.properties").getPath();
			FileOutputStream outputFile = new FileOutputStream(path);
			prop.store(outputFile, "modify");
			outputFile.close();
			outputFile.flush();
		} catch (Exception e) {
		}
	}
}
