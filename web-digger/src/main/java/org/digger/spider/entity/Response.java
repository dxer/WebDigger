package org.digger.spider.entity;

import java.util.Map;

/**
 * 
 * 
 * @class Response
 * @author linghf
 * @version 1.0
 * @since 2016年4月11日
 */
public class Response {

    private String url;

    private int status;

    private Map<String, String> headers;

    private String body;

    private Request request;

    private Map<String, String> meta;

    private Item item = new Item();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
        this.item.setRequest(request);
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }

    public String xpath(String xpath) {
        return null;
    }

    public String css(String cssQuery) {
        return null;
    }

    public Item getItem() {
        return item;
    }

    public void put(String key, Object value) {
        this.item.put(key, value);
    }

}
