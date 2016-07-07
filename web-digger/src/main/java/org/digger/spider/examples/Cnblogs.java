package org.digger.spider.examples;

import org.digger.spider.annotation.Field;
import org.digger.spider.annotation.FieldType;
import org.digger.spider.entity.OutputModel;

/**
 * 
 * @class Cnblogs
 * @author linghf
 * @version 1.0
 * @since 2016年7月7日
 */
public class Cnblogs extends OutputModel {

    @Field(type = FieldType.CSS, expr = "#cb_post_title_url")
    private String title;

    @Field(type = FieldType.CSS, expr = "#cnblogs_post_body")
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
