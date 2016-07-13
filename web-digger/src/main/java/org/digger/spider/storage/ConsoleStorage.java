package org.digger.spider.storage;

import org.digger.spider.entity.Item;

/**
 * 
 * @class ConsoleStorage
 * @author linghf
 * @version 1.0
 * @since 2016年4月13日
 */
public class ConsoleStorage implements Storage {

    public void persist(Item item) {
        if (item != null) {
            System.out.println(item.toString());
        }
    }
}
