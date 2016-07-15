package org.digger.spider.proxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.base.Strings;

/**
 * 
 * @class HttpProxyRool
 * @author linghf
 * @version 1.0
 * @since 2016年7月15日
 */
public class HttpProxyRool {

    private static List<HttpProxy> proxys = new ArrayList<HttpProxy>();

    static {

    }

    public static void addHttpProxy(String host, int port) {
        HttpProxy proxy = new HttpProxy(host, port);

        if (proxy.isVaild()) {
            proxys.add(proxy);
        }
    }

    public static void load(String fileName) {
        if (!Strings.isNullOrEmpty(fileName)) {

            File file = new File(fileName);

            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    String[] strs = null;
                    if (line.contains("\t")) {
                        strs = line.split("\t");
                    } else if (line.contains(",")) {
                        strs = line.split(",");
                    }

                    if (strs != null && strs.length == 2) {
                        try {
                            String host = strs[0].trim();

                            int port = Integer.parseInt(strs[1].trim());
                            addHttpProxy(host, port);
                        } catch (Exception e) {
                            continue;
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    public static HttpProxy getHttpHost() {
        HttpProxy proxy = null;
        if (proxys != null && proxys.size() > 0) {
            Random rand = new Random();
            int randNum = rand.nextInt(proxys.size()); // 随机ip

            proxy = proxys.get(randNum);
        }

        return proxy;

    }
}
