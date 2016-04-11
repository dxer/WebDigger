package org.digger.spider.entity;

import org.digger.spider.Spider;

/**
 * 
 * 
 * @class Request
 * @author linghf
 * @version 1.0
 * @since 2016年4月11日
 */
public class Request {

    private String url;

    private Spider spider;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Spider getSpider() {
        return spider;
    }

    public void setSpider(Spider spider) {
        this.spider = spider;
    }

}
