package org.digger.spider;

/**
 * 
 * @class TestSpider
 * @author linghf
 * @version 1.0
 * @since 2016年4月11日
 */
public class TestSpider extends Spider {

    public static void main(String[] args) {
        TestSpider spider = new TestSpider();
        spider.setName("test");

        spider.addStartUrls("http://news.qq.com/world_index.shtml", "http://mil.qq.com/mil_index.htm");

        Digger.getInstance().register(spider).start();
    }

}
