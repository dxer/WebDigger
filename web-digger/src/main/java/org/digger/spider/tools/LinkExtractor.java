/**
 * Copyright (c) 2016 21CN.COM . All rights reserved.
 * 
 * Description: web-digger
 * 
 * <pre>
 * Modified log:
 * ------------------------------------------------------
 * Ver.		Date		Author			Description
 * ------------------------------------------------------
 * 1.0		2016年7月6日	linghf		created.
 * </pre>
 */
package org.digger.spider.tools;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.digger.spider.entity.Response;
import org.digger.spider.selector.Selector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.base.Strings;

/**
 * 
 * @class LinkExtractor
 * @author linghf
 * @version 1.0
 * @since 2016年7月6日
 */
public class LinkExtractor {

    private static boolean isSuitAllowDomain(String url, String allowDomain) {
        if (!Strings.isNullOrEmpty(url) && !Strings.isNullOrEmpty(allowDomain) &&
            (url.startsWith("http://") || url.startsWith("https://"))) {
            int index = url.indexOf("://");
            String domain = url.substring(index + 3);
            if (domain.contains("/")) {
                domain = domain.substring(0, domain.indexOf("/"));
            }
            if (domain.equals(allowDomain)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSuitAllow(String url, String allow) {
        if (!Strings.isNullOrEmpty(url) && !Strings.isNullOrEmpty(allow)) {
            Pattern pattern = Pattern.compile(allow);
            Matcher matcher = pattern.matcher(url);
            return matcher.matches();
        }

        return false;
    }

    private static boolean isSuit(String url, List<String> allows, List<String> allowDomains) {
        boolean isSuit = true;
        if (isSuit && allowDomains != null && allowDomains.size() > 0) {
            isSuit = false;
            for (String allowDomain: allowDomains) {
                if (isSuitAllowDomain(url, allowDomain)) {
                    isSuit = true;
                    break;
                }
            }
        }

        if (isSuit && allows != null && allows.size() > 0) {
            isSuit = false;
            for (String allow: allows) {
                if (isSuitAllow(url, allow)) {
                    isSuit = true;
                    break;
                }
            }
        }

        return isSuit;
    }

    public static Set<String> extract(Response response, LinkFilter filter) {
        Set<String> urls = null;
        if (response != null && filter != null) {
            urls = new HashSet<String>();
            Selector selector = response.getSelector();
            Document doc = null;
            if (selector != null) {
                doc = selector.getDoc();
            }

            if (doc == null) {
                return urls;
            }

            Elements links = doc.select("a[href]");
            Elements media = doc.select("[src]");

            List<String> allows = filter.getAllows();

            List<String> allowDomains = filter.getAllowDomains();

            for (Element src: media) {
                String url = src.attr("abs:src");
                System.out.println(url);

                if (!Strings.isNullOrEmpty(url)) {
                    if (isSuit(url, allows, allowDomains)) {
                        urls.add(url);
                    }
                }
            }

            for (Element link: links) {
                String url = link.attr("abs:href");
                System.out.println(url);

                if (!Strings.isNullOrEmpty(url)) {
                    if (isSuit(url, allows, allowDomains)) {
                        urls.add(url);
                    }
                }
            }
        }

        return urls;
    }

    public static void main(String[] args) throws IOException {
        // System.out.println(isSuitAllowDomain("http://dxer.github.io/2016/06/21/hbase_filter/", "dxer.github.io"));

        String url = "http://dxer.github.io";
        print("Fetching %s...", url);

        Document doc = Jsoup.connect(url).get();
        System.out.println(doc.html());
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");

        print("\nMedia: (%d)", media.size());
        for (Element src: media) {
            System.out.println(src.attr("abs:src"));
            // if (src.tagName().equals("img"))
            // print(" * %s: <%s> %sx%s (%s)", src.tagName(), src.attr("abs:src"), src.attr("width"),
            // src.attr("height"), trim(src.attr("alt"), 20));
            // else
            // print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
        }

        // print("\nImports: (%d)", imports.size());
        // for (Element link: imports) {
        // print(" * %s <%s> (%s)", link.tagName(), link.attr("abs:href"), link.attr("rel"));
        // }

        print("\nLinks: (%d)", links.size());
        for (Element link: links) {
            System.out.println(link.attr("abs:href"));
            // print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
        }
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width - 1) + ".";
        else
            return s;
    }
}
