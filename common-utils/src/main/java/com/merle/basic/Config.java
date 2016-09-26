package com.merle.basic;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {

    private static final Logger logger = LoggerFactory.getLogger(Config.class);

    private static String CONFIG_FILENAME = "config.xml";

    private static XMLConfiguration config = null;

    public static String[] getLocalProperties(String parent) {
        return config.getStringArray(parent);
    }

    public static int getIntLocalProperty(String name) {
        return config.getInt(name);
    }

    public static String getLocalProperty(String name) {
        return config.getString(name);
    }

    public static int getLocalProperty(String name, int defaultValue) {
        String value = getLocalProperty(name);
        if (value == null)
            return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.error("getLocalProperty(String, int)", e);
        }
        return defaultValue;
    }

    public static String getConnectionProvider() {
        return getLocalProperty("database.connectionProvider.className");
    }

    public static Object getProperties(String name) {
        return config.getProperties(name);
    }

    public static void main(String[] args) {



    }

    static {
        try {
//            AbstractConfiguration.setDefaultListDelimiter('-');
            config = new XMLConfiguration(CONFIG_FILENAME);


//            config.setDelimiterParsingDisabled(false);
//            config.setListDelimiter('x');
//            XMLConfiguration.setDelimiter(' ');//设置空格为分割符.
//            config.setExpressionEngine(new XPathExpressionEngine());
        } catch (ConfigurationException e) {
            //e.printStackTrace();
        }
    }
}