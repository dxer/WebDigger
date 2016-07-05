package org.digger.spider.storage;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.digger.spider.entity.Item;

/**
 * 
 * @class FileStorage
 * @author linghf
 * @version 1.0
 * @since 2016年4月15日
 */
public class FileStorage implements Storage {

    private void write(String fileName, String content) {
        DataOutputStream dos = null;

        try {
            dos = new DataOutputStream(new FileOutputStream(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                }
            }
        }

    }

    public void processItem(Item item) {
        String fileName = DigestUtils.md5Hex(item.getRequest().getUrl().getBytes());

        String content = null;

        write(fileName, content);

    }

}
