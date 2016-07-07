package org.digger.spider.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期类型相关操作类
 * 
 * @class DateUtil
 * @author linghf
 * @version 1.0
 * @since 2016年7月7日
 */
public class DateUtil {

    private static String defaultDatePattern = "yyyy-MM-dd HH:mm:ss";

    public static final long ONE_MINUTE_MILLISECOND = 60 * 1000L;

    public static final long ONE_HOUR_MILLISECOND = 60 * ONE_MINUTE_MILLISECOND;

    /**
     * 一天所对应的毫秒数
     */
    public static final long ONE_DAY_MILLISECOND = 24 * ONE_HOUR_MILLISECOND;

    /**
     * 一周所对应的毫秒数
     */
    public static final long ONE_WEEK_MILLISECOND = 7 * ONE_DAY_MILLISECOND;

    public static final long ONE_MONTH_MILLISECOND = 30 * ONE_DAY_MILLISECOND;

    public static final long ONE_YEAR_MILLISECOND = 365 * ONE_DAY_MILLISECOND;

    /**
     * 从配置文件中返回配置项"date.format",默认的日期格式符 (yyyy-MM-dd)，
     * 
     * @return a string representing the date pattern on the UI
     */

    private static final String[] SMART_DATE_FORMATS = new String[] {"yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd HH:mm:ss",
                    "yyyy-MM-dd HH:mm", "yyyy.MM.dd HH:mm", "yyyyMMddHHmmss", "yyyyMMddHHmm", "yyyy-MM-dd",
                    "yyyy.MM.dd", "yyyyMMdd"};

    /**
     * 获取日期的年份
     * 
     * @param date
     * @return 日期的年份
     */
    public static int getYear(Date date) {
        return getCalendar(date).get(Calendar.YEAR);
    }

    /**
     * 获取日期的月份（0-11）
     * 
     * @param date
     * @return 日期的月份（0-11）
     */
    public static int getMonth(Date date) {
        return getCalendar(date).get(Calendar.MONTH);
    }

    /**
     * 获取日期的一个月中的某天
     * 
     * @param date
     * @return 日期的一个月中的某天(1-31)
     */
    public static int getDay(Date date) {
        return getCalendar(date).get(Calendar.DATE);
    }

    /**
     * 获取日期的一个星期中的某天
     * 
     * @param date
     * @return 日期的星期中日期(1:sunday-7:SATURDAY)
     */
    public static int getWeek(Date date) {
        return getCalendar(date).get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取日期的当月1号所对应的星期中的某天
     * 
     * @param date
     * @return 日期的星期中日期(1:sunday-7:SATURDAY)
     */
    public static int getWeekOfFirstDayOfMonth(Date date) {
        return getWeek(getFirstDayOfMonth(date));
    }

    /**
     * 获取日期的当月最后1天所对应的星期中的某天
     * 
     * @param date
     * @return 日期的星期中日期(1:sunday-7:SATURDAY)
     */
    public static int getWeekOfLastDayOfMonth(Date date) {
        return getWeek(getLastDayOfMonth(date));
    }

    /**
     * 将日期字符串按指定的格式转为Date类型
     * 
     * @param strDate 待解析的日期字符串
     * @param format 日期格式
     * @return 字符串对应的日期对象
     */
    public static final Date parseDate(String strDate, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        try {
            return df.parse(strDate);
        } catch (ParseException pe) {
            return null;
        }
    }

    /**
     * 自动判断常见的中文日期格式并解析为日期对象
     * 
     * @param strDate
     * @return
     */
    public static final Date parseDateSmart(String strDate) {
        if (strDate == null || strDate.length() <= 0) {
            return null;
        }
        for (String fmt: SMART_DATE_FORMATS) {
            Date d = parseDate((String) strDate, fmt);
            if (d != null) {
                String s = DateUtil.formatDate(d, fmt);
                if (strDate.equals(s))
                    return d;
            }
        }
        try {
            long time = Long.parseLong(strDate);
            return new Date(time);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将日期字符串按系统配置中指定默认格式(getDatePattern()返回的格式)转为Date类型
     * 
     * @param strDate 待解析的日期字符串
     * @return 字符串对应的日期对象
     */
    public static Date parseDate(String strDate) {
        return parseDate(strDate, defaultDatePattern);
    }

    /**
     * <p>
     * 检查所给的年份是否是闰年
     * </p>
     * 
     * @param year 年
     * @return 检查结果: true - 是闰年; false - 是平年
     */
    public static boolean isLeapYear(int year) {
        if (year / 4 * 4 != year) {
            return false; // 不能被4整除
        } else if (year / 100 * 100 != year) {
            return true; // 能被4整除，不能被100整除
        } else if (year / 400 * 400 != year) {
            return false; // 能被100整除，不能被400整除
        } else {
            return true; // 能被400整除
        }
    }

    /**
     * 是否是周末
     * 
     * @param date
     * @return
     */
    public static boolean isWeekend(Date date) {
        Calendar c = Calendar.getInstance();
        if (date != null)
            c.setTime(date);
        int weekDay = c.get(Calendar.DAY_OF_WEEK);
        return (weekDay == Calendar.SUNDAY || weekDay == Calendar.SATURDAY);
    }

    public static boolean isWeekend() {
        return isWeekend(null);
    }

    /**
     * 按照默认格式化样式格式化当前系统时间
     * 
     * @return 日期字符串
     */
    public static String getCurrentTime() {
        return formatDate(new Date());
    }

    /**
     * 按照默认格式化样式格式化当前系统时间
     * 
     * @param format String 日期格式化标准
     * @return String 日期字符串。
     */
    public static String getCurrentTime(String format) {
        return formatDate(new Date(), format);
    }

    /**
     * 按照指定格式化样式格式化指定的日期
     * 
     * @param date 待格式化的日期
     * @param format 日期格式
     * @return 日期字符串
     */
    public static String formatDate(Date date, String format) {
        if (date == null)
            date = new Date();
        if (format == null)
            format = defaultDatePattern;
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format);
        return formatter.format(date);
    }

    /**
     * 按照默认格式化样式格式化指定的日期
     * 
     * @param date 待格式化的日期
     * @return 日期字符串
     */
    public static String formatDate(Date date) {
        long offset = System.currentTimeMillis() - date.getTime();
        String pos = "前";
        if (offset < 0) {
            pos = "后";
            offset = -offset;
        }
        if (offset >= ONE_YEAR_MILLISECOND)
            return formatDate(date, defaultDatePattern);

        StringBuilder sb = new StringBuilder();
        if (offset >= 2 * ONE_MONTH_MILLISECOND) {
            return sb.append((offset + ONE_MONTH_MILLISECOND / 2) / ONE_MONTH_MILLISECOND).append("个月").append(pos)
                            .toString();
        }
        if (offset > ONE_WEEK_MILLISECOND) {
            return sb.append((offset + ONE_WEEK_MILLISECOND / 2) / ONE_WEEK_MILLISECOND).append("周").append(pos)
                            .toString();
        }
        if (offset > ONE_DAY_MILLISECOND) {
            return sb.append((offset + ONE_DAY_MILLISECOND / 2) / ONE_DAY_MILLISECOND).append("天").append(pos)
                            .toString();
        }
        if (offset > ONE_HOUR_MILLISECOND) {
            return sb.append((offset + ONE_HOUR_MILLISECOND / 2) / ONE_HOUR_MILLISECOND).append("小时").append(pos)
                            .toString();
        }
        if (offset > ONE_MINUTE_MILLISECOND) {
            return sb.append((offset + ONE_MINUTE_MILLISECOND / 2) / ONE_MINUTE_MILLISECOND).append("分钟").append(pos)
                            .toString();
        }
        return sb.append(offset / 1000).append("秒").append(pos).toString();
    }

    /**
     * 将date的时间部分清零
     * 
     * @param day
     * @return 返回Day将时间部分清零后对应日期
     */
    public static Date getCleanDay(Date day) {
        return getCleanDay(getCalendar(day));
    }

    /**
     * 获取day对应的Calendar对象
     * 
     * @param day
     * @return 返回date对应的Calendar对象
     */
    public static Calendar getCalendar(Date day) {
        Calendar c = Calendar.getInstance();
        if (day != null)
            c.setTime(day);
        return c;
    }

    private static Date getCleanDay(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.clear(Calendar.MINUTE);
        c.clear(Calendar.SECOND);
        c.clear(Calendar.MILLISECOND);
        return c.getTime();
    }

    /**
     * 根据year，month，day构造日期对象
     * 
     * @param year 年份（4位长格式）
     * @param month 月份（1-12)
     * @param day 天（1-31）
     * @return 日期对象
     */
    public static Date makeDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        getCleanDay(c);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);
        return c.getTime();
    }

    private static Date getFirstCleanDay(int datePart, Date date) {
        Calendar c = Calendar.getInstance();
        if (date != null)
            c.setTime(date);
        c.set(datePart, 1);
        return getCleanDay(c);
    }

    private static Date add(int datePart, int detal, Date date) {
        Calendar c = Calendar.getInstance();
        if (date != null)
            c.setTime(date);
        c.add(datePart, detal);
        return c.getTime();
    }

    /**
     * 日期date所在星期的第一天00:00:00对应日期对象
     * 
     * @param date
     * @return 日期所在星期的第一天00:00:00对应日期对象
     */
    public static Date getFirstDayOfWeek(Date date) {
        return getFirstCleanDay(Calendar.DAY_OF_WEEK, date);
    }

    /**
     * 当前日期所在星期的第一天00:00:00对应日期对象
     * 
     * @return 当前日期所在星期的第一天00:00:00对应日期对象
     */
    public static Date getFirstDayOfWeek() {
        return getFirstDayOfWeek(null);
    }

    /**
     * 日期date所在月份的第一天00:00:00对应日期对象
     * 
     * @param date
     * @return 日期所在月份的第一天00:00:00对应日期对象
     */
    public static Date getFirstDayOfMonth(Date date) {
        return getFirstCleanDay(Calendar.DAY_OF_MONTH, date);
    }

    /**
     * 当前日期所在月份的第一天00:00:00对应日期对象
     * 
     * @return 当前日期所在月份的第一天00:00:00对应日期对象
     */
    public static Date getFirstDayOfMonth() {
        return getFirstDayOfMonth(null);
    }

    /**
     * 当前日期所在月份的最后一天00:00:00对应日期对象
     * 
     * @return 当前日期所在月份的最后一天00:00:00对应日期对象
     */
    public static Date getLastDayOfMonth() {
        return getLastDayOfMonth(null);
    }

    /**
     * 日期date所在月份的最后一天00:00:00对应日期对象
     * 
     * @param date
     * @return 日期date所在月份的最后一天00:00:00对应日期对象
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar c = getCalendar(getFirstDayOfMonth(date));
        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DATE, -1);
        return getCleanDay(c);
    }

    /**
     * 日期date所在季度的第一天00:00:00对应日期对象
     * 
     * @param date
     * @return 日期date所在季度的第一天00:00:00对应日期对象
     */
    public static Date getFirstDayOfSeason(Date date) {
        Date d = getFirstDayOfMonth(date);
        int delta = DateUtil.getMonth(d) % 3;
        if (delta > 0)
            d = DateUtil.getDateAfterMonths(d, -delta);
        return d;
    }

    /**
     * 当前日期所在季度的第一天00:00:00对应日期对象
     * 
     * @return 当前日期所在季度的第一天00:00:00对应日期对象
     */
    public static Date getFirstDayOfSeason() {
        return getFirstDayOfMonth(null);
    }

    /**
     * 日期date所在年份的第一天00:00:00对应日期对象
     * 
     * @param date
     * @return 日期date所在年份的第一天00:00:00对应日期对象
     */
    public static Date getFirstDayOfYear(Date date) {
        return makeDate(getYear(date), 1, 1);
    }

    /**
     * 当前日期年度的第一天00:00:00对应日期对象
     * 
     * @return 当前日期年度第一天00:00:00对应日期对象
     */
    public static Date getFirstDayOfYear() {
        return getFirstDayOfYear(new Date());
    }

    /**
     * 计算N周后的日期
     * 
     * @param start 开始日期
     * @param weeks 可以为负，表示前N周
     * @return 新的日期
     */
    public static Date getDateAfterWeeks(Date start, int weeks) {
        return getDateAfterMs(start, weeks * ONE_WEEK_MILLISECOND);
    }

    /**
     * 计算N月后的日期
     * 
     * @param start 开始日期
     * @param months 可以为负，表示前N月
     * @return 新的日期
     */
    public static Date getDateAfterMonths(Date start, int months) {
        return add(Calendar.MONTH, months, start);
    }

    /**
     * 计算N年后的日期
     * 
     * @param start 开始日期
     * @param years 可以为负，表示前N年
     * @return 新的日期
     */
    public static Date getDateAfterYears(Date start, int years) {
        return add(Calendar.YEAR, years, start);
    }

    /**
     * 计算N天后的日期
     * 
     * @param start 开始日期
     * @param days 可以为负，表示前N天
     * @return 新的日期
     */
    public static Date getDateAfterDays(Date start, int days) {
        return getDateAfterMs(start, days * ONE_DAY_MILLISECOND);
    }

    /**
     * 计算N毫秒后的日期
     * 
     * @param start 开始日期
     * @param ms 可以为负，表示前N毫秒
     * @return 新的日期
     */
    public static Date getDateAfterMs(Date start, long ms) {
        return new Date(start.getTime() + ms);
    }

    /**
     * 计算2个日期之间的间隔的周期数
     * 
     * @param start 开始日期
     * @param end 结束日期
     * @param msPeriod 单位周期的毫秒数
     * @return 周期数
     */
    public static long getPeriodNum(Date start, Date end, long msPeriod) {
        return getIntervalMs(start, end) / msPeriod;
    }

    /**
     * 计算2个日期之间的毫秒数
     * 
     * @param start 开始日期
     * @param end 结束日期
     * @return 毫秒数
     */
    public static long getIntervalMs(Date start, Date end) {
        return end.getTime() - start.getTime();
    }

    /**
     * 计算2个日期之间的天数
     * 
     * @param start 开始日期
     * @param end 结束日期
     * @return 天数
     */
    public static int getIntervalDays(Date start, Date end) {
        return (int) getPeriodNum(start, end, ONE_DAY_MILLISECOND);
    }

    /**
     * 计算2个日期之间的周数
     * 
     * @param start 开始日期
     * @param end 结束日期
     * @return 周数
     */
    public static int getIntervalWeeks(Date start, Date end) {
        return (int) getPeriodNum(start, end, ONE_WEEK_MILLISECOND);
    }

    /**
     * 比较日期前后关系
     * 
     * @param base 基准日期
     * @param date 待比较的日期
     * @return 如果date在base之前或相等返回true，否则返回false
     */
    public static boolean before(Date base, Date date) {
        return date.before(base) || date.equals(base);
    }

    /**
     * 比较日期前后关系
     * 
     * @param base 基准日期
     * @param date 待比较的日期
     * @return 如果date在base之后或相等返回true，否则返回false
     */
    public static boolean after(Date base, Date date) {
        return date.after(base) || date.equals(base);
    }

    /**
     * 返回对应毫秒数大的日期
     * 
     * @param date1
     * @param date2
     * @return 返回对应毫秒数大的日期
     */
    public static Date max(Date date1, Date date2) {
        if (date1.getTime() > date2.getTime())
            return date1;
        else
            return date2;
    }

    /**
     * 返回对应毫秒数小的日期
     * 
     * @param date1
     * @param date2
     * @return 返回对应毫秒数小的日期
     */
    public static Date min(Date date1, Date date2) {
        if (date1.getTime() < date2.getTime())
            return date1;
        else
            return date2;
    }

    /**
     * 判断date是否在指定的时期范围（start~end）内
     * 
     * @param start 时期开始日期
     * @param end 时期结束日期
     * @param date 待比较的日期
     * @return 如果date在指定的时期范围内，返回true，否则返回false
     */
    public static boolean inPeriod(Date start, Date end, Date date) {
        return (end.after(date) || end.equals(date)) && (start.before(date) || start.equals(date));
    }

    public static final String[] zodiacArray = {"猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"};

    public static final String[] constellationArray = {"水瓶座", "双鱼座", "牡羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座",
                    "天蝎座", "射手座", "魔羯座"};

    private static final int[] constellationEdgeDay = {20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};

    /**
     * 根据日期获取生肖
     * 
     * @return
     */
    public static String date2Zodica(Date time) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        return year2Zodica(c.get(Calendar.YEAR));
    }

    public static String year2Zodica(int year) {
        return zodiacArray[year % 12];
    }

    /**
     * 根据日期获取星座
     * 
     * @param time
     * @return
     */
    public static String date2Constellation(Date time) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        if (day < constellationEdgeDay[month]) {
            month = month - 1;
        }
        if (month >= 0) {
            return constellationArray[month];
        }
        // default to return 魔羯
        return constellationArray[11];
    }

}
