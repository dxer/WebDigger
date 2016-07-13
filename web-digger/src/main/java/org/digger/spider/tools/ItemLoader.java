package org.digger.spider.tools;

import org.digger.spider.entity.Item;
import org.digger.spider.entity.Response;

import com.google.common.base.Strings;

/**
 * 
 * @class ItemLoader
 * @author linghf
 * @version 1.0
 * @since 2016年4月16日
 */
public class ItemLoader {

    private Response response;

    public ItemLoader(Response response){
        this.response = response;
    }

    public void addXpath(String name, String xpath) {
        if (!Strings.isNullOrEmpty(name) && !Strings.isNullOrEmpty(xpath)) {
            String value = response.getJXDoc().getXpath().xpath(xpath);

            response.put(name, value);

        }
    }

    public void addCss(String name, String cssQuesy) {
        if (response != null && !Strings.isNullOrEmpty(name) && !Strings.isNullOrEmpty(cssQuesy)) {
            String value = response.getJXDoc().getSelector().cssText(cssQuesy);

            response.put(name, value);

        }
    }

    public void addValue(String name, String value) {
        if (response != null && !Strings.isNullOrEmpty(name) && !Strings.isNullOrEmpty(value)) {
            response.put(name, value);
        }
    }

    public Item loadItem() {
        return response.getItem();
    }
}
