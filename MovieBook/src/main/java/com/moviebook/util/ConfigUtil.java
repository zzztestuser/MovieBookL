package com.moviebook.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * Helper class to access config.properties
 * 
 * @author Lua Choon Ngee
 *
 */
public final class ConfigUtil {

	private static Properties config;

	private static final String CONFIG_PATH = "/config/config.properties";

	static {
		config = new Properties();
		try (InputStream in = ConfigUtil.class.getResourceAsStream(CONFIG_PATH)) {
			config.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		return StringUtils.trim(config.getProperty(key));
	}

	public static int getPropertyAsInteger(String key) {
		return Integer.parseInt(StringUtils.trim(config.getProperty(key)));
	}

}
