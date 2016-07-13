package org.digger.spider.tools;

import java.lang.reflect.Field;
import java.util.Date;

import org.digger.spider.annotation.FieldRule;
import org.digger.spider.annotation.FieldRule.FieldType;
import org.digger.spider.entity.CrawlerModel;
import org.digger.spider.entity.Response;
import org.digger.spider.selector.JXDoc;
import org.digger.spider.selector.Selector;
import org.digger.spider.selector.Xpath;

import com.google.common.base.Strings;

public class FieldResolver {

    private static void setValue(Field field, FieldRule fieldRule, String type, CrawlerModel model, String value) {
        try {

            if (type.endsWith("String")) {
                field.set(model, value);
            } else if (type.endsWith("int") || type.endsWith("Integer")) {
                field.set(model, Integer.parseInt(value)); // 给属性设值
            } else if (type.endsWith("long") || type.endsWith("Long")) {
                field.set(model, Long.parseLong(value));
            } else if (type.endsWith("double") || type.endsWith("Double")) {
                field.set(model, Double.parseDouble(value));
            } else if (type.endsWith(Date.class.getName())) {
                String format = fieldRule.format(); // 获得时间格式
                if (format != null && format.length() > 0) {
                    field.set(model, DateUtil.parseDate(value, format));
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void resolve(Response response, Class<? extends CrawlerModel> claz) {
        if (response != null && claz != null) {
            try {
                CrawlerModel model = null;
                try {
                    model = claz.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (model == null) {
                    throw new RuntimeException();
                }

                JXDoc jxDoc = response.getJXDoc();

                Selector selector = jxDoc.getSelector();
                Xpath xpath = jxDoc.getXpath();

                Field[] fields = claz.getDeclaredFields();
                if (fields != null && fields.length > 0) {
                    for (int i = 0, length = fields.length; i < length; i++) {
                        Field f = fields[i];
                        f.setAccessible(true);
                        FieldRule fieldRule = f.getAnnotation(FieldRule.class);
                        if (fieldRule != null) {
                            String expr = fieldRule.expr();
                            FieldType fieldType = fieldRule.type();
                            String value = null;
                            String type = f.getType().toString();// 得到此属性的类型
                            if (!Strings.isNullOrEmpty(expr)) {
                                if (fieldType == FieldType.CSS) {
                                    value = selector.cssText(expr);
                                } else if (fieldType == FieldType.XPATH) {
                                    value = xpath.xpath(expr);
                                }
                                setValue(f, fieldRule, type, model, value);
                            }
                        }
                    }
                }

                response.setCrawlerModel(model);
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
