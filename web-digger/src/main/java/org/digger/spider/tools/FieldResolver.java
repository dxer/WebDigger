package org.digger.spider.tools;

import java.lang.reflect.Field;

import org.digger.spider.annotation.FieldType;
import org.digger.spider.entity.OutputModel;
import org.digger.spider.entity.Response;
import org.digger.spider.selector.JXDoc;
import org.digger.spider.selector.Selector;
import org.digger.spider.selector.Xpath;

import com.google.common.base.Strings;

public class FieldResolver {

    private static void setValue(Field field, String type, OutputModel model, String value) {
        try {
            if (type.endsWith("String")) {
                field.set(model, value);
            } else if (type.endsWith("int") || type.endsWith("Integer")) {
                field.set(model, Integer.parseInt(value)); // 给属性设值
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void resolve(Response response, Class<? extends OutputModel> claz) {
        if (response != null && claz != null) {
            try {
                OutputModel model = claz.newInstance();

                JXDoc jxDoc = response.getJXDoc();

                Selector selector = jxDoc.getSelector();
                Xpath xpath = jxDoc.getXpath();

                Field[] fields = claz.getDeclaredFields();
                if (fields != null && fields.length > 0) {
                    for (int i = 0, length = fields.length; i < length; i++) {
                        Field f = fields[i];
                        f.setAccessible(true);
                        org.digger.spider.annotation.Field field = f
                                        .getAnnotation(org.digger.spider.annotation.Field.class);
                        if (field != null) {
                            String expr = field.expr();
                            FieldType fieldType = field.type();
                            String value = null;
                            String type = f.getType().toString();// 得到此属性的类型
                            if (!Strings.isNullOrEmpty(expr)) {
                                if (fieldType == FieldType.CSS) {
                                    value = selector.css(expr);
                                } else if (fieldType == FieldType.XPATH) {
                                    value = xpath.xpath(expr);
                                }
                                setValue(f, type, model, value);
                            }
                        }
                    }
                }

                response.setOutputModel(model);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
