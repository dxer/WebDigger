/**
 * Copyright (c) 2016 21CN.COM . All rights reserved.
 * 
 * Description: web-digger
 * 
 * <pre>
 * Modified log:
 * ------------------------------------------------------
 * Ver.		Date		Author			Description
 * ------------------------------------------------------
 * 1.0		2016年4月11日	linghf		created.
 * </pre>
 */
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
        Spider spider = new TestSpider();
        spider.setName("test");

        spider.addStartUrls("http://news.qq.com/world_index.shtml", "http://mil.qq.com/mil_index.htm");

        Digger digger = new Digger();
        digger.register(spider);

        digger.start();
    }

}
