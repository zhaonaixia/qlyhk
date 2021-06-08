package com.cd.qlyhk.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {

    public static final String           FormatDayToChinaTime24H = "yyyy年MM月dd日";

    public final static SimpleDateFormat g_SimpleDateFormat_II   = new SimpleDateFormat("yyyyMM");

    public static String                 DATETIME_FORMAT         = "yyyy-MM-dd HH:mm:ss";

    public static String                 DATE_FORMAT             = "yyyy-MM-dd";

    public static String                 DATE_FORMAT_MONTH       = "yyyy-MM";

    public static String                 TIME_FORMAT             = "HH:mm:ss";


    /**
     * 返回年份
     * 
     * @param date 日期
     * @return 返回年份
     */
    public static int getYear(Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return c.get(java.util.Calendar.YEAR);
    }

    /**
     * 返回月份
     * @param date 日期
     * @return 返回月份
     */
    public static int getMonth(Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return c.get(java.util.Calendar.MONTH) + 1;
    }

    /**
     * 返回日份
     * @param date 日期
     * @return 返回日份
     */
    public static int getDay(Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return c.get(java.util.Calendar.DAY_OF_MONTH);
    }

    /**
     * Parse date like "format".
     */
    public static Date parseDateByFormat(String d, String format) {
        try {
            return new SimpleDateFormat(format).parse(d);
        } catch (Exception e) {}
        return null;
    }

    /**
     * Parse date like "yyyy-MM-dd".
     */
    public static Date parseDate(String d) {
        try {
            return new SimpleDateFormat(DATE_FORMAT).parse(d);
        } catch (Exception e) {}
        return null;
    }

    /**
     * Parse date like "yyyy-MM".
     */
    public static Date parseDateMonth(String d) {
        try {
            return new SimpleDateFormat(DATE_FORMAT_MONTH).parse(d);
        } catch (Exception e) {}
        return null;
    }

    /**
     * Parse date like "yyyy-MM-dd HH:mm:ss".
     */
    public static Date parseDateTime(String d) {
        try {
            return new SimpleDateFormat(DATETIME_FORMAT).parse(d);
        } catch (Exception e) {}
        return null;
    }

    /**
     * Parse date like "HH:mm:ss".
     */
    public static Date parseTime(String d) {
        try {
            return new SimpleDateFormat(TIME_FORMAT).parse(d);
        } catch (Exception e) {}
        return null;
    }

    /**
     * 取得指定月份的最后一天
     * @param strdate String
     * @return String
     */
    public static String getMonthEnd(String strdate) {
        Date date = parseDate(getMonthBegin(strdate));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return formatDate(calendar.getTime());
    }

    /**
     * 取得指定月份的第一天
     * @param strdate String
     * @return String
     */
    public static String getMonthBegin(String strdate) {
        Date date = parseDate(strdate);
        return formatDateByFormat(date, "yyyy-MM") + "-01";
    }

    /**
     * 取得指定年份的第一月
     * @param strdate String
     * @return String
     */
    public static String getYearBegin(String strdate, String format) {
        Date date = parseDateByFormat(strdate, format);
        return formatDateByFormat(date, "yyyy") + "-01";
    }

    /**
     * 以指定的格式来格式化日期
     * @param date Date
     * @param format String
     * @return String
     */
    public static String formatDateByFormat(Date date, String format) {
        String result = "";
        if (date != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                result = sdf.format(date);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 常用的格式化日期
     * @param date Date
     * @return String
     */
    public static String formatDate(Date date) {
        return formatDateByFormat(date, "yyyy-MM-dd");
    }

    /**
     * 常用的格式化日期时间
     * @param date Date
     * @return String
     */
    public static String formatDatetime(Date date) {
        return formatDateByFormat(date, DATETIME_FORMAT);
    }
    
    /**
     * @根据当前日期计算n天后的日期
     * @return String
     */
    public static Date afterNDay(Date date, int n) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, n);
        Date destDay = c.getTime();
        return destDay;
    }
    
    /**
     * @根据当前时间计算n小时之后的时间
     * @return String
     */
    public static Date afterNHours(Date date, int n) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, n);
        Date destDay = c.getTime();
        return destDay;
    }
    
    /**
     * @根据当前日期计算n月后的日期
     * @return String
     */
    public static Date afterNMonth(Date date, int n) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, n);
        Date destDay = c.getTime();
        return destDay;
    }

    /**
     * 两个日期间的天数
     */
    public static int getIntervalDays(Date startday, Date endday) {
        if (startday.after(endday)) {
            Date cal = startday;
            startday = endday;
            endday = cal;
        }
        long sl = startday.getTime();
        long el = endday.getTime();
        long ei = el - sl;
        return (int) (ei / (1000 * 60 * 60 * 24));
    }

    public static boolean verifyDate(String birthday) {
        String[] array = birthday.split("-");
        if (null == array || array.length != 3 || array[0].length() != 4 || array[1].length() != 2 || array[2].length() != 2)
            return false;
        int curyear = DateUtil.getYear(new Date());
        try {
            int studyear = Integer.parseInt(array[0]);
            if (studyear <= 1900 || studyear > curyear)
                return false;
            String curDateStr = formatDateByFormat(new Date(), DATE_FORMAT);
            if (birthday.compareTo(curDateStr) >= 0)
                return false;
            return checkDate(birthday);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean checkDate(String date) {
        // 判断年月日的正则表达式，接受输入格式为2010-12-24，可接受平年闰年的日期
        String regex = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcherObj = pattern.matcher(date);
        return matcherObj.matches();
    }

    public static String formatDateAdd_(String str) throws ParseException {
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMM");
        SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM");
        String sfstr = "";
        sfstr = sf2.format(sf1.parse(str));
        return sfstr;
    }

    /**
     *
     * 获得本月的前（后）几个月。(yyyyMM)
     * <p>
     * %方法详述（简单方法可不必详述）%。
     * </p>
     *
     * @param month 月数字(数字大于1为后，小于1为前)
     * @return
     */
    public static String getBAMonth(int monthNum) {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MONTH, monthNum);
        return g_SimpleDateFormat_II.format(calendar.getTime());
    }

    /***
     * 日期月份加减月份
     * @param datetime
     * @param num 正数：加月份 负数：减月份
     * @return yyyyMM
     */
    public static String addAndReduceMonth(String datetime, int num) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Date date = null;
        try {
            date = sdf.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.add(Calendar.MONTH, num);
        date = cl.getTime();
        return sdf.format(date);
    }
    
    /**
	 * 取当月第一天
	 */
	public static String getFirstDayOfMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat simpleFormate = new SimpleDateFormat("yyyy-MM-dd");
		return simpleFormate.format(calendar.getTime());
	}
    
	/**
	 * 取当月最后一天
	 */
	public static String getLastDayOfMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		SimpleDateFormat simpleFormate = new SimpleDateFormat("yyyy-MM-dd");
		return simpleFormate.format(calendar.getTime());
	}
	
	/**
	 * 取指定月最后一天
	 */
	public static String getLastDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		SimpleDateFormat simpleFormate = new SimpleDateFormat("yyyy-MM-dd");
		return simpleFormate.format(calendar.getTime());
	}
	
    /**
	 * 取当月最后一天
	 
	public static String getLastDayOfMonth(Date sDate1) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(sDate1);
		calendar.set(Calendar.DATE, 1);
		calendar.roll(Calendar.DATE, -1);
		SimpleDateFormat simpleFormate = new SimpleDateFormat("yyyy-MM-dd");
		return simpleFormate.format(calendar.getTime());
	}*/
	
	/**
	 * 取上一个月
	 */
	public static String getLastMonth(Date date, String format) {
		String result = "";
        if (date != null) {
            try {
            	Calendar cl = Calendar.getInstance();
                cl.setTime(date);
                cl.add(Calendar.MONTH, -1);
                date = cl.getTime();
                
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                result = sdf.format(date);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
	}
	
	/**
     * 秒转换为指定格式的日期
     * @param second
     * @param patten
     * @return
     */
	public static String secondToDate(long second,String patten) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(second * 1000);//转换为毫秒
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat(patten);
        String dateString = format.format(date);
        return dateString;
    }
    /**
     * 返回日时分秒
     * @param second
     * @return
     */
	public static String secondToTime(long second) {
        long days = second / 86400;//转换天数
        second = second % 86400;//剩余秒数
        long hours = second / 3600;//转换小时数
        second = second % 3600;//剩余秒数
        long minutes = second / 60;//转换分钟
        second = second % 60;//剩余秒数
        if (0 < days){
            return days + "天，"+hours+"小时，"+minutes+"分，"+second+"秒";
        }else {
            return hours+"小时，"+minutes+"分，"+second+"秒";
        }
    }

}