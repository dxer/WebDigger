package org.digger.spider.conf;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @class Configuration
 * @author linghf
 * @version 1.0
 * @since 2016年7月15日
 */
public class Configuration {

    private static final Log LOG = LogFactory.getLog(Configuration.class);

    private ConcurrentHashMap<String, String> setting = new ConcurrentHashMap<String, String>();

    public Configuration(){}

    public Configuration(String file){
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            Properties properties = new Properties();
            properties.load(in);
            putAll(properties);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public Configuration(InputStream in){
        if (in != null) {
            Properties properties = new Properties();
            try {
                properties.load(in);
                putAll(properties);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public void putAll(Properties props) {
        if (props != null && !props.isEmpty()) {
            Iterator<?> it = props.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                String key = entry.getKey();
                String value = entry.getValue();
                if (key != null && value != null) {
                    setting.put(key, value);
                }
            }
        }
    }

    public void putAll(Map<String, String> map) {
        if (map != null && !map.isEmpty()) {
            setting.putAll(map);
        }
    }

    public Map<String, String> getAll() {
        return setting;
    }

    public String get(String key, String defaultValue) {
        if (setting == null) {
            return defaultValue;
        }

        return setting.get(key) == null? defaultValue: setting.get(key);
    }

    public String get(String name) {
        String result = null;

        if (setting != null) {
            return setting.get(name);
        }

        return result;
    }

    public void set(String name, String value) {
        setting.put(name, value);
    }

    public String getTrimmed(String name) {
        String value = get(name);

        if (null == value) {
            return null;
        } else {
            return value.trim();
        }
    }

    public String[] getTrimmedStrings(String name) {
        String[] emptyStringArray = {};
        String valueString = get(name);
        if (null == valueString || valueString.trim().isEmpty()) {
            return emptyStringArray;
        }

        return valueString.trim().split("\\s*,\\s*");
    }

    public void setBoolean(String name, boolean value) {
        set(name, Boolean.toString(value));
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        String valueString = getTrimmed(name);
        if (null == valueString || valueString.isEmpty()) {
            return defaultValue;
        }

        valueString = valueString.toLowerCase();

        if ("true".equals(valueString)) {
            return true;
        } else if ("false".equals(valueString)) {
            return false;
        } else {
            return defaultValue;
        }
    }

    public int getInt(String name, int defaultValue) {
        String valueString = getTrimmed(name);
        if (valueString == null) {
            return defaultValue;
        }

        String hexString = getHexDigits(valueString);
        if (hexString != null) {
            return Integer.parseInt(hexString, 16);
        }
        return Integer.parseInt(valueString);
    }

    public void setInt(String name, int value) {
        set(name, Integer.toString(value));
    }

    public long getLong(String name, long defaultValue) {
        String valueString = getTrimmed(name);
        return valueString == null? defaultValue: Long.parseLong(valueString);
    }

    public void setLong(String name, long value) {
        set(name, Long.toString(value));
    }

    public float getFloat(String name, float defaultValue) {
        String valueString = getTrimmed(name);
        return valueString == null? defaultValue: Float.parseFloat(valueString);
    }

    public void setFloat(String name, float value) {
        set(name, Float.toString(value));
    }

    public double getDouble(String name, double defaultValue) {
        String valueString = getTrimmed(name);
        return valueString == null? defaultValue: Double.parseDouble(valueString);
    }

    public void setDouble(String name, double value) {
        set(name, Double.toString(value));
    }

    public void remove(String name) {
        if (setting != null) {
            setting.remove(name);
        }
    }

    private String getHexDigits(String value) {
        boolean negative = false;
        String str = value;
        String hexString = null;
        if (value.startsWith("-")) {
            negative = true;
            str = value.substring(1);
        }
        if (str.startsWith("0x") || str.startsWith("0X")) {
            hexString = str.substring(2);
            if (negative) {
                hexString = "-" + hexString;
            }
            return hexString;
        }
        return null;
    }

    public void print() {
        for (String name: setting.keySet()) {
            String value = setting.get(name);
            LOG.info(name + "=" + value);
        }
    }
}
