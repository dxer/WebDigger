package org.digger.spider.selector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.base.Strings;

/**
 * 
 * @class Selector
 * @author linghf
 * @version 1.0
 * @since 2016年4月15日
 */
public class Selector {

    private Document doc = null;

    public Selector(Document doc){
        if (doc != null) {
            this.doc = doc;
        }
    }

    public Document getDoc() {
        return this.doc;
    }

    public String getTitle() {
        return doc != null? doc.select("title").text(): null;
    }

    public String cssText(String cssQuery) {
        if (doc == null || Strings.isNullOrEmpty(cssQuery)) {
            return null;
        }
        String ret = null;
        Elements elements = doc.select(cssQuery);
        if (elements != null && elements.size() > 0) {
            Element e = elements.get(0);
            ret = e.text();
        }

        return ret;
    }

    public String cssHref(String cssQuery) {
        String href = null;

        return href;
    }

    public Elements selElements(String cssQuery) {
        if (doc == null || Strings.isNullOrEmpty(cssQuery)) {
            return null;
        }

        return doc.select(cssQuery);
    }

    public List<String> selList(String cssQuery) {
        if (doc == null || Strings.isNullOrEmpty(cssQuery)) {
            return null;
        }
        List<String> results = new ArrayList<String>();
        Elements elements = selElements(cssQuery);
        if (elements != null && elements.size() > 0) {
            for (Element e: elements) {
                results.add(e.text());
            }
        }
        return results;
    }

    public List<String> foreach(String parentQuery, String childQuery) {
        List<String> result = null;
        if (Strings.isNullOrEmpty(parentQuery) || Strings.isNullOrEmpty(childQuery)) {
            return result;
        }

        Elements eles = selElements(parentQuery);
        if (eles == null || eles.size() <= 0) {
            return result;
        }

        result = new ArrayList<String>();
        for (Element e: eles) {
            result.add(e.select(childQuery).text());
        }

        return result;
    }

    public List<Map<String, String>> foreach(String parentQuery, Map<String, String> map) {
        List<Map<String, String>> result = null;
        if (doc == null || Strings.isNullOrEmpty(parentQuery) || map == null || map.isEmpty()) {
            return result;
        }

        Elements eles = selElements(parentQuery);
        if (eles == null || eles.size() <= 0) {
            return result;
        }

        Map<String, String> resultMap = null;
        result = new ArrayList<Map<String, String>>();
        for (Element e: eles) {
            resultMap = new HashMap<String, String>();
            for (String fieldName: map.keySet()) {
                String cssQuery = map.get(fieldName);
                if (!Strings.isNullOrEmpty(cssQuery)) {

                }
                resultMap.put(fieldName, e.select(cssQuery).text());
            }
            result.add(resultMap);
        }

        return result;

    }

}
