package org.digger.spider.entity;

import java.util.HashMap;
import java.util.Map;

import sun.org.mozilla.javascript.internal.json.JsonParser;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @class Item
 * @author linghf
 * @version 1.0
 * @since 2016年4月12日
 */
public class Item {

    private Map<String, Object> fields = new HashMap<String, Object>();

    private OutputModel model;

    private Request request;

    public Item setRequest(Request request) {
        this.request = request;
        return this;
    }

    public Request getRequest() {
        return this.request;
    }

    public Object get(String key) {
        Object o = fields.get(key);
        if (o == null) {
            return null;
        }
        return fields.get(key);
    }

    public Map<String, Object> getAll() {
        return fields;
    }

    public Item put(String key, Object value) {
        fields.put(key, value);
        return this;
    }

    public void setOutputModel(OutputModel model) {
        this.model = model;
    }

    public OutputModel getOutputModel() {
        return this.model;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String url = null;
        if (request != null) {
            url = request.getUrl();
        }
        sb.append("Item={").append("url=").append(url).append(", fields=").append(JSON.toJSONString(fields));
        if (model != null) {
            sb.append(", model=");
            sb.append(JSON.toJSONString(model));
        }

        sb.append("}");
        return sb.toString();
    }
}
