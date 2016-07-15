package org.digger.spider.proxy.spider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.digger.spider.Spider;
import org.digger.spider.entity.Item;
import org.digger.spider.entity.Response;
import org.digger.spider.proxy.HttpProxyRool;
import org.digger.spider.selector.Selector;

import com.google.common.base.Strings;

/**
 * 
 * @class XCHttpProxySpider
 * @author linghf
 * @version 1.0
 * @since 2016年7月15日
 */
public class XCHttpProxySpider extends Spider {

    @Override
    public Spider setStartUrls(List<String> startUrls) {
        String baseUrl = "http://www.xicidaili.com/nn/";

        for (int i = 1; i < 50; i++) {
            this.startUrls.add(baseUrl + i);
        }

        return this;
    }

    @Override
    public void process(Response response) {
        Selector selector = response.getJXDoc().getSelector();

        Map<String, String> map = new HashMap<String, String>();
        map.put("ip", "> td:nth-child(2)");
        map.put("port", "> td:nth-child(3)");

        List<Map<String, String>> result = selector.foreach("#ip_list > tbody > tr", map);

        response.put("result", result);
    }

    @Override
    public void persist(Item item) {
        List<Map<String, String>> result = (List<Map<String, String>>) item.get("result");

        if (result != null && result != null) {
            for (Map<String, String> map: result) {
                if (map != null && map.size() > 0) {

                    if (!Strings.isNullOrEmpty(map.get("port")) && !Strings.isNullOrEmpty(map.get("ip"))) {
                        try {
                            int port = Integer.parseInt(map.get("port"));
                            String host = map.get("ip");

                            System.out.println(host + ":" + port);

                            HttpProxyRool.addHttpProxy(host, port);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Spider spider = new XCHttpProxySpider();
        spider.setStartUrls(null);
        spider.start();
    }

}
