package org.digger.spider.selector;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
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

    Document doc = null;

    public Selector(String html){
        if (!Strings.isNullOrEmpty(html)) {
            doc = Jsoup.parse(html);
        }
    }

    public Document getDoc() {
        return this.doc;
    }

    public String xpath(String xpath) {

        return null;
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

    public Set<String> extracLinks(Set<String> allowedDomains, Set<String> regs) {
        Set<String> urls = new HashSet<String>();
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");

        for (Element src: media) {
            String url = src.attr("abs:src");
            if (!Strings.isNullOrEmpty(url)) {
                urls.add(url);
            }
        }

        System.out.printf("\nLinks: (%d)", links.size());
        for (Element link: links) {
            String url = link.attr("abs:href");
            if (!Strings.isNullOrEmpty(url)) {
                urls.add(url);
            }
        }
        return urls;
    }

}
