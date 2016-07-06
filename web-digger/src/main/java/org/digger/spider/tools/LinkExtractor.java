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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.digger.spider.entity.Response;
import org.digger.spider.selector.Selector;
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

                if (!Strings.isNullOrEmpty(url)) {
                    if (isSuit(url, allows, allowDomains)) {
                        urls.add(url);
                    }
                }
            }

            for (Element link: links) {
                String url = link.attr("abs:href");

                if (!Strings.isNullOrEmpty(url)) {
                    if (isSuit(url, allows, allowDomains)) {
                        urls.add(url);
                    }
                }
            }
        }

        return urls;
    }

    public static void main(String[] args) {
        System.out.println(isSuitAllowDomain("http://dxer.github.io/2016/06/21/hbase_filter/", "dxer.github.io"));
    }
}
