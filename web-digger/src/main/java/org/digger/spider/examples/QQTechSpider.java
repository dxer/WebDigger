package org.digger.spider.examples;

import org.digger.spider.Spider;
import org.digger.spider.annotation.FieldRule;
import org.digger.spider.annotation.FieldRule.FieldType;
import org.digger.spider.entity.CrawlerModel;

/**
 * 
 * @class QQTechSpider
 * @author linghf
 * @version 1.0
 * @since 2016年7月7日
 */
public class QQTechSpider extends Spider {

    public static class QQTechModel extends CrawlerModel {

        @FieldRule(type = FieldType.CSS, expr = "#C-Main-Article-QQ > div.hd > h1")
        private String title;

        @FieldRule(type = FieldType.CSS, expr = "#C-Main-Article-QQ > div.hd > div > div > span.pubTime")
        private String pubTime;

        @FieldRule(type = FieldType.CSS, expr = "#Cnt-Main-Article-QQ")
        private String content;

        public QQTechModel(){
            super();
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPubTime() {
            return pubTime;
        }

        public void setPubTime(String pubTime) {
            this.pubTime = pubTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

    }

    //
    // @Override
    // public void parser(org.digger.spider.entity.Response response) {
    // if (response != null) {
    // ItemLoader loader = new ItemLoader(response);
    // loader.addCss("title", "#C-Main-Article-QQ > div.hd > h1");
    // loader.addCss("pubTime", "#C-Main-Article-QQ > div.hd > div > div > span.pubTime");
    // loader.addCss("content", "#Cnt-Main-Article-QQ");
    // }
    // }

    public static void main(String[] args) {
        Spider spider = new QQTechSpider();
        spider.addStartUrls("http://tech.qq.com").setFollowed(true).addAllows("^http://tech.qq.com/a/.*htm$");
        spider.setCrawlModel(QQTechModel.class);

        spider.start();
    }
}
