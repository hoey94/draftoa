package com.starsoft.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置文件读取
 * 
 * @author 赵士星
 * 
 */
public class ConfigUtil {

	private static Logger _log = LoggerFactory.getLogger(ConfigUtil.class);
	
	private Map<String, String> configMap = new HashMap<String, String>();

	public ConfigUtil(String configPath){
		try {
			Properties prop = new Properties();
			prop.load(this.getClass().getClassLoader().getResourceAsStream(configPath));
			for (Object key : prop.keySet()) {
				configMap.put(key.toString(), prop.get(key).toString());
			}
		} catch (Exception e) {
			_log.error(e.getMessage());
		}
	}

	public String getPropByName(String name) {
		return configMap.get(name);
	}

	public Map<String, String> getPropMap() {
		return configMap;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String name : configMap.keySet()) {
			sb.append(name + ":" + configMap.get(name));
			sb.append("--");
		}

		return sb.toString();
	}
}