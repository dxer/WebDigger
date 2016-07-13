package org.digger.spider.entity;

import java.util.HashMap;
import java.util.Map;

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

    private CrawlModel model;

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

    public void setOutputModel(CrawlModel model) {
        this.model = model;
    }

    public CrawlModel getOutputModel() {
        return this.model;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String url = null;
        if (request != null) {
            url = request.getUrl();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("url", url);
        map.put("fields", fields);

        sb.append("Item=");

        if (model != null) {
            map.put("model", model);
        }

        sb.append(JSON.toJSONString(map));

        return sb.toString();
    }
}
