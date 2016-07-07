package org.digger.spider.entity;

import java.util.Map;

import org.digger.spider.selector.JXDoc;

import com.google.common.base.Strings;

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

    private String html;

    private Request request;

    private Map<String, String> meta;

    private Item item = new Item();

    private JXDoc jxDoc;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public JXDoc getJXDoc() {
        return this.jxDoc;
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

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
        if (!Strings.isNullOrEmpty(this.html)) {
            jxDoc = new JXDoc(this.html);
        }
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

    public Item getItem() {
        return item;
    }

    public void put(String key, Object value) {
        this.item.put(key, value);
    }

    public OutputModel getOutputModel() {
        return item.getOutputModel();
    }

    public void setOutputModel(OutputModel model) {
        item.setOutputModel(model);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Resopnse { ");
        sb.append(" url = ").append(request.getUrl());

        sb.append(", status = ").append(status);
        sb.append(", item = ").append(item);

        sb.append("}");
        return sb.toString();
    }
}
