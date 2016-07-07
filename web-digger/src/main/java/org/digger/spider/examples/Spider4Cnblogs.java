package org.digger.spider.examples;

import org.digger.spider.Spider;
import org.digger.spider.tools.LinkFilter;

/**
 * 
 * @class Spider4Cnblogs
 * @author linghf
 * @version 1.0
 * @since 2016年7月7日
 */
public class Spider4Cnblogs extends Spider {

    public static void main(String[] args) {
        Spider4Cnblogs spider = new Spider4Cnblogs();
        spider.addStartUrls("http://www.cnblogs.com/lgfeng/");
        spider.setFollowed(true);
        LinkFilter filter = new LinkFilter();
        filter.addAllows("^http://www.cnblogs.com/lgfeng/p.*html$");
        spider.setFilter(filter).setOutPutModel(Cnblogs.class);

        spider.start();
    }
}
