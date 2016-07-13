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

    public Elements selElements(String parentQuery, String childQuery) {
        if (doc == null || Strings.isNullOrEmpty(parentQuery) || Strings.isNullOrEmpty(childQuery)) {
            return null;
        }
        Elements elements = new Elements();
        Elements eles = selElements(parentQuery);
        if (eles != null && eles.size() > 0) {
            for (Element e: eles) {
                Elements els = e.select(childQuery);
                if (els != null && els.size() > 0) {
                    elements.addAll(els);
                }
            }
        }
        return elements;
    }

    public List<String> selList(String parentQuery, String childQuery) {
        if (doc == null || Strings.isNullOrEmpty(parentQuery) || Strings.isNullOrEmpty(childQuery)) {
            return null;
        }
        List<String> results = new ArrayList<String>();

        Elements eles = selElements(parentQuery);
        if (eles != null && eles.size() > 0) {
            for (Element el: eles) {
                Elements els = el.select(childQuery);
                if (els != null && els.size() > 0) {
                    for (Element e: els) {
                        String txt = e.text();
                        if (!Strings.isNullOrEmpty(txt)) {
                            results.add(txt);
                        }
                    }
                }
            }
        }

        return results;
    }

}
