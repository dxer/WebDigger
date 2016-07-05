package org.digger.spider.storage;

import java.util.Map;

import org.digger.spider.entity.Item;

/**
 * 
 * @class ConsoleStorage
 * @author linghf
 * @version 1.0
 * @since 2016年4月13日
 */
public class ConsoleStorage implements Storage {

    public void processItem(Item item) {
        StringBuilder sb = new StringBuilder();
        sb.append("url = ").append(item.getRequest().getUrl()).append("\n");

        if (item != null) {
            Map<String, Object> fields = item.getAll();
            if (fields != null && !fields.isEmpty()) {
                for (String key: fields.keySet()) {
                    sb.append("\t");
                    sb.append(key).append(" = ").append(fields.get(key));
                }
                sb.append("\n");
            } else {
                sb.append("\t");
                sb.append("null").append("\n");
            }
        }

        System.out.println(sb.toString());
    }
}
