package com.orangehrm.utils;

import com.orangehrm.exceptions.FrameworkException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Loads config.properties once (eagerly) and exposes typed getters.
 * Demonstrates: static init block, exception handling, encapsulation.
 */
public final class ConfigReader {

    private static final Properties PROPS = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            PROPS.load(fis);
        } catch (IOException e) {
            throw new FrameworkException("Failed to load config.properties", e);
        }
    }

    private ConfigReader() {}

    public static String get(String key) {
        String value = PROPS.getProperty(key);
        if (value == null) throw new FrameworkException("Missing key in config: " + key);
        return value;
    }

    public static int getInt(String key) {
        try {
            return Integer.parseInt(get(key));
        } catch (NumberFormatException e) {
            throw new FrameworkException("Property " + key + " is not an int", e);
        }
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }
}
