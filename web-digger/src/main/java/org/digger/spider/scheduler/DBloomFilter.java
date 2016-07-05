package org.digger.spider.scheduler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

/**
 * BloomFilter，如果数据过大，采用多个过滤器方式来降低错误率
 * 
 * @class DBloomFilter
 * @author linghf
 * @version 1.0
 * @since 2016年3月22日
 */
public class DBloomFilter {

    private List<BloomFilter<CharSequence>> bloomFilters = null;

    private static int THRESHOLD = 2 << 26;

    private int filterNum;

    private int capacity;

    private static HashFunction hf = Hashing.md5();

    public DBloomFilter(int size, String backupFileName){
        init(size);
    }

    public DBloomFilter(int size){
        init(size);
    }

    private void init(int size) {
        if (size >= 0) {
            bloomFilters = new ArrayList<BloomFilter<CharSequence>>();
            if (size >= 2 << 24) {
                filterNum = size / THRESHOLD + 1;
                for (int i = 0; i < filterNum; i++) {
                    BloomFilter<CharSequence> bloomFilter = BloomFilter.create(
                        Funnels.stringFunnel(Charset.forName("UTF-8")), THRESHOLD, 0.001f);
                    bloomFilters.add(bloomFilter);
                }
                capacity = THRESHOLD;
            } else {
                int offset = 0;
                for (int i = 0;; i++) {
                    if (size < 2 << i) {
                        offset = i;
                        break;
                    }
                }
                BloomFilter<CharSequence> bloomFilter = BloomFilter.create(
                    Funnels.stringFunnel(Charset.forName("UTF-8")), 2 << offset, 0.001f);
                bloomFilters.add(bloomFilter);
                filterNum = bloomFilters.size();
                capacity = 2 << offset;
            }
        }
    }

    /**
     * 从文件恢复数据到bloomfilter
     * 
     * @param file
     */
    public void recoverFromFile(String file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                add(line, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public void add(String value) {
        add(value, true);
    }

    /**
     * 添加数据
     * 
     * @param value
     */
    private void add(String value, boolean isLog) {
        if (!Strings.isNullOrEmpty(value)) {
            if (bloomFilters != null && bloomFilters.size() > 0) {
                BloomFilter<CharSequence> bloomFilter = null;
                if (bloomFilters.size() == 1) {
                    bloomFilter = bloomFilters.get(0);
                } else {
                    int index = (int) (hash(value) % (filterNum - 1));
                    bloomFilter = bloomFilters.get(index);
                }
                if (bloomFilter != null && !bloomFilter.mightContain(value)) {
                    bloomFilter.put(value);
                }
            }
        }
    }

    public void addAll(List<String> values) {
        if (values != null && values.size() > 0) {
            for (String value: values) {
                add(value);
            }
        }
    }

    /**
     * 判断数据是否存在
     * 
     * @param value
     * @return
     */
    public boolean contains(String value) {
        if (Strings.isNullOrEmpty(value)) {
            return false;
        }

        if (bloomFilters != null && bloomFilters.size() > 0) {
            BloomFilter<CharSequence> bloomFilter = null;
            if (bloomFilters.size() == 1) {
                bloomFilter = bloomFilters.get(0);
            } else {
                int index = (int) (hash(value) % (filterNum - 1));
                bloomFilter = bloomFilters.get(index);
            }
            if (bloomFilter != null) {
                return bloomFilter.mightContain(value);
            }
        }
        return false;
    }

    /**
     * 获取过滤器的个数
     * 
     * @return
     */
    public int getFilterCount() {
        return filterNum;
    }

    /**
     * 获取最大容量
     * 
     * @return
     */
    public int getCapacity() {
        return capacity;
    }

    public void setFilterCount(int filterCount) {
        this.filterNum = filterCount;
    }

    private static long hash(String string) {
        HashCode hc = hf.newHasher().putString(string, Charsets.UTF_8).hash();
        return hc.asInt();
    }

    public static void main(String[] args) {
        // System.out.println(2 << 26);
        DBloomFilter filter = new DBloomFilter(2 << 23);
        filter.recoverFromFile("d:/degree.log");

        System.out.println(filter.contains("15362041490"));
        System.out.println(filter.contains("18672995788"));
        System.out.println(filter.contains("15920410060"));
        System.out.println(filter.contains("15876503759"));
        System.out.println(filter.contains("15919334781"));
        System.out.println(filter.contains("13662374434"));
        System.out.println(filter.contains("13828350360"));
        System.out.println(filter.contains("13631420925"));

        for (int i = 0; i < 3; i++) {
            if (filter.contains("18926128622" + "_" + i)) {
                System.err.println("-------------");

                break;
            }
        }
    }
}
