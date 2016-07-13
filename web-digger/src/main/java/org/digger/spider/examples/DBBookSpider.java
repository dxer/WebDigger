package org.digger.spider.examples;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.digger.spider.Spider;
import org.digger.spider.entity.Item;
import org.digger.spider.entity.Response;
import org.digger.spider.selector.Selector;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @class DBBookSpider
 * @author linghf
 * @version 1.0
 * @since 2016年7月13日
 */
public class DBBookSpider extends Spider {

    /**
     * 自定义的processor，负责解析页面书籍的相关信息
     */
    @Override
    public void process(Response response) {
        Selector selector = response.getJXDoc().getSelector();

        Map<String, String> fieldMap = new HashMap<String, String>();
        fieldMap.put("name", "div.info > h2 > a");
        fieldMap.put("pub", "div.info > div.pub");
        fieldMap.put("score", "div.info > div.star.clearfix > span.rating_nums");
        fieldMap.put("comments", "div.info > div.star.clearfix > span.pl");
        fieldMap.put("intro", "div.info > p");

        List<Map<String, String>> list = selector.foreach("#subject_list > ul > li", fieldMap);

        response.put("list", list);
    }

    /**
     * 自定义的Storage，这里仅仅是将信息打印输出
     */
    @Override
    public void persist(Item item) {
        List<Map<String, String>> list = (List<Map<String, String>>) item.get("list");
        if (list != null && !list.isEmpty()) {
            for (Map<String, String> map: list) {
                System.out.println(JSON.toJSONString(map));
            }
        }

    }

    public static void main(String[] args) {
        new DBBookSpider()
                        .addStartUrls("https://book.douban.com/tag/中国文学",
                            "https://book.douban.com/tag/中国文学?start=20&type=T",
                            "https://book.douban.com/tag/中国文学?start=40&type=T").setFollowed(false).start(); // 启动爬虫
    }
}
