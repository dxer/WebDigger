package org.digger.spider.selector;

import java.util.ArrayList;
import java.util.List;

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
        return doc.select("title").text();
    }

    public String css(String cssQuery) {
        String ret = null;
        Elements elements = doc.select(cssQuery);
        if (elements != null && elements.size() > 0) {
            Element e = elements.get(0);
            ret = e.text();
        }

        return ret;
    }
    
    public Elements selElements(String cssQuery) {
        if (doc == null || Strings.isNullOrEmpty(cssQuery)) {
            return null;
        }

        Selector selector = new Selector(doc);
        return doc.select(cssQuery);
    }

    public List<String> selList(String cssQuery) {
        List<String> results = new ArrayList<String>();
        
        return results;
    }

}
