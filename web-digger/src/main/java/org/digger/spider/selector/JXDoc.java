package org.digger.spider.selector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.common.base.Strings;

/**
 * 
 * @class JXDoc
 * @author linghf
 * @version 1.0
 * @since 2016年4月17日
 */
public class JXDoc {

    private Document doc;

    private String html;

    private Selector selector;

    private Xpath xpath;

    public JXDoc(Document doc){
        if (doc != null) {
            this.doc = doc;
            selector = new Selector(this.doc);
        }
    }

    public JXDoc(String html){
        this.html = html;
        if (!Strings.isNullOrEmpty(html)) {
            this.doc = Jsoup.parse(html);
            selector = new Selector(this.doc);
        }
    }

    public Document doc() {
        return doc;
    }

    public String html() {
        return this.html;
    }

    public Selector getSelector() {
        return selector;
    }

    public Xpath getXpath() {
        return xpath;
    }

}
