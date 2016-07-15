package org.digger.spider.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @class NetworkUtils
 * @author linghf
 * @version 1.0
 * @since 2016年7月15日
 */
public class NetworkUtils {

    private static final String IP_REXP = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

    private static Pattern IP_PAT = Pattern.compile(IP_REXP);

    public static boolean isIPAddr(String addr) {
        if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
            return false;
        }
        /**
         * 判断IP格式和范围
         */
        try {
            Matcher mat = IP_PAT.matcher(addr);
            return mat.find();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isPort(int port) {
        if (port > 0 && port < 65536) {
            return true;
        }

        return false;
    }

    public static boolean isValidIPPort(String host, int port) {
        if (isIPAddr(host) && isPort(port)) {
            return true;
        }
        return false;
    }
}
