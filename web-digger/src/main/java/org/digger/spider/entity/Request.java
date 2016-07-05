package org.digger.spider.entity;

import java.util.Map;

import org.digger.spider.Spider;

import com.google.common.base.Strings;

/**
 * 
 * 
 * @class Request
 * @author linghf
 * @version 1.0
 * @since 2016年4月11日
 */
public class Request {

    /**
     * 请求的url
     */
    private String url;

    /**
     * 深度
     */
    private Integer depth;

    /**
     * 处理的spider
     */
    private Spider spider;

    /**
     * http的请求方法
     */
    private HttpMethod method = HttpMethod.GET;

    /**
     * 请求头
     */
    private Map<String, String> headers;

    /**
     * 属性的初始值
     */
    private Map<String, Object> meta;

    /**
     * post时候的请求体
     */
    private String body;

    /**
     * cookies
     * 
     */
    private Map<String, String> cookies;

    /**
     * 编码格式
     */
    private String encoding = "UTF-8";

    /**
     * 优先级
     */
    private int priority = 0;

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

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public boolean vertify() {
        if (!Strings.isNullOrEmpty(url) && spider != null) {
            return true;
        }
        return false;
    }

}
