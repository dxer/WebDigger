package org.digger.spider.examples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.digger.spider.Spider;
import org.digger.spider.entity.Item;
import org.digger.spider.entity.Response;
import org.digger.spider.selector.Selector;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

        Elements els = selector.selElements("#subject_list > ul > li");

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (Element e: els) {
            Map<String, String> map = new HashMap<String, String>();
            String name = e.select("div.info > h2 > a").text();
            String pub = e.select("div.info > div.pub").text();
            String score = e.select("div.info > div.star.clearfix > span.rating_nums").text();
            String comments = e.select("div.info > div.star.clearfix > span.pl").text();
            String intro = e.select("div.info > p").text();

            map.put("name", name);
            map.put("score", score);
            map.put("pub", pub);
            map.put("comments", comments);
            map.put("intro", intro);

            list.add(map);
        }
        response.put("list", list);
    }

    /**
     * 自定义的Storage，这里仅仅是将文件打印输出
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
