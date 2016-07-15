package org.digger.spider.conf;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @class DiggerConf
 * @author linghf
 * @version 1.0
 * @since 2016年7月15日
 */
public class DiggerConf extends Configuration {

    private static final Log LOG = LogFactory.getLog(DiggerConf.class);

    public static final String WORK_THREAD_NUM = "digger.worker.thread.num";

    public static final String HTTP_PROXY_ENABLED = "digger.http.proxy.enable";

    public static final String RANDOM_USER_AGENT_ENABLED = "digger.random.ua.enable";

    public DiggerConf(){}

    public DiggerConf(boolean loadDefaults){
        if (loadDefaults) {
            Properties props = System.getProperties();
            addProps(props);
        }
    }

    public DiggerConf(String fileName, boolean loadDefaults){
        super(fileName);
        if (loadDefaults) {
            Properties props = System.getProperties();
            addProps(props);
        }
    }

    private void addProps(Properties props) {
        if (props != null && !props.isEmpty()) {
            Iterator<?> it = props.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                String key = entry.getKey();
                String value = entry.getValue();
                if (key != null && value != null) {
                    if (key.startsWith("digger.")) {
                        set(key, value);
                    }
                }
            }
        }
    }

    public void setThreadNum(int threadNum) {
        if (threadNum <= 0) {
            threadNum = 3;
        }
        setInt(WORK_THREAD_NUM, threadNum);
    }

    public int getThreadNum() {
        return getInt(WORK_THREAD_NUM, 3);
    }

    public void setHttpProxyEnabled(boolean newValue) {
        setBoolean(HTTP_PROXY_ENABLED, newValue);
    }

    public boolean getHttpProxyEnabled() {
        return getBoolean(HTTP_PROXY_ENABLED, false);
    }

    public void setRandomUAEnabled(boolean newValue) {
        setBoolean(RANDOM_USER_AGENT_ENABLED, newValue);
    }

    public boolean getRandomUAEnabled() {
        return getBoolean(RANDOM_USER_AGENT_ENABLED, false);
    }

}
