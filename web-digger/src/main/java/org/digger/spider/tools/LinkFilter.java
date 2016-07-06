package org.digger.spider.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @class LinkFilter
 * @author linghf
 * @version 1.0
 * @since 2016年4月16日
 */
public class LinkFilter {

    private List<String> allows = new ArrayList<String>();

    // private List<String> deny;

    private List<String> allowDomains = new ArrayList<String>();

    public List<String> getAllows() {
        return allows;
    }

    public void setAllows(List<String> allows) {
        this.allows = allows;
    }

    public void addAllows(String... allows) {
        if (allows != null && allows.length > 0) {
            for (String allow: allows) {
                this.allows.add(allow);
            }
        }
    }

    public List<String> getAllowDomains() {
        return allowDomains;
    }

    public void setAllowDomains(List<String> allowDomains) {
        this.allowDomains = allowDomains;
    }

    public void addAllowDomains(String... allowDomains) {
        if (allowDomains != null && allowDomains.length > 0) {
            for (String allowDomain: allowDomains) {
                this.allowDomains.add(allowDomain);
            }
        }
    }

    // private List<String> denyDomains;

}
