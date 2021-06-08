package com.cd.rdf.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.cd.rdf.constants.BaseConstant;

/**
 * 
 * <pre>
 * 用于格式化和分析日期的实用类。
 * </pre>
 * @author luoshifei luoshifei
 * @version 1.00.00
 * <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容: 
 * </pre>
 */

public class DateTimeUtil {

	/**
	 * 获取现在的时间戳。
	 * 
	 * @return Timestamp
	 */
	public static Timestamp nowTimestamp() {
		return getTimestamp(System.currentTimeMillis());
	}

	/**
	 * 获得当前日期。
	 * 
	 * @return 当前日期
	 */
	public static Date nowDate() {
		return new Date();
	}

	/**
	 * 获取时间戳。
	 * 
	 * @param time long
	 * @return Timestamp
	 */
	public static Timestamp getTimestamp(long time) {
		return new Timestamp(time);
	}

	/**
	 * 格式化的日期。
	 * 
	 * @param date 日期
	 * 
	 * @return 格式化的日期
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, BaseConstant.DATETIME_FORMAT_STR);
	}
	/**
	 * 格式化的日期。
	 * 
	 * @param date Date
	 * @param pattern String
	 * @return String
	 */
	public static String formatDateTime(Date date, String pattern) {
		return formatDate(date, pattern);
	}

	/**
	 * 格式化的日期。
	 * 
	 * @param date Date
	 * @return String
	 */
	public static String formatDate(Date date) {
		return formatDate(date, BaseConstant.DATE_FORMAT_STR);
	}

	/**
	 * 格式化的日期。
	 * 
	 * @param date Date
	 * @param pattern String
	 * 
	 * @return String
	 */
	public static String formatDate(Date date, String pattern) {
		if (date == null) {
			return "";
		}
		if (pattern == null) {
			pattern = BaseConstant.DATE_FORMAT_STR;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * 格式化的时间。
	 * 
	 * @param date Date
	 * 
	 * @return String
	 */
	public static String formatTime(Date date) {
		return formatDate(date, BaseConstant.TIME_FORMAT_STR);
	}

	/**
	 * 格式化的时间。
	 * 
	 * @param date Date
	 * @param pattern String
	 * 
	 * @return String
	 */
	public static String formatTime(Date date, String pattern) {
		if (date == null) {
			return "";
		}
		if (pattern == null) {
			pattern = BaseConstant.TIME_FORMAT_STR;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * 字符串转换为Date类型。
	 * 
	 * @param datetime String
	 * @return Date
	 */
	public static Date parseDateTime(String datetime) {
		SimpleDateFormat formatter = new SimpleDateFormat(BaseConstant.DATETIME_FORMAT_STR);
		if (datetime == null || datetime.equals("")) {
			return null;
		}
		try {
			return formatter.parse(datetime);
		} catch (ParseException e) {
			return parseDate(datetime);
		}
	}

	/**
	 * 字符串转换为Date类型。
	 * 
	 * @param date String
	 * @return Date
	 */
	public static Date parseDate(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat(BaseConstant.DATE_FORMAT_STR);
		if (date == null || date.equals("")) {
			return null;
		}
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

   /**
    *  转换。
    *  
    * @param datetime Date
    * 
    * @return Date
    */
	public static Date parseDate(Date datetime) {
		SimpleDateFormat formatter = new SimpleDateFormat(BaseConstant.DATE_FORMAT_STR);
		if (datetime == null) {
			return null;
		}
		try {
			return formatter.parse(formatter.format(datetime));
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 将Object类型的数据格式化成String类型。
	 * 
	 * @param o Object
	 * 
	 * @return String
	 */
	public static String formatDate(Object o) {
		if (o == null) {
			return "";
		}
		if (o.getClass() == String.class) {
			return formatDate((String) o);
		}
		if (o.getClass() == Date.class) {
			return formatDate((Date) o);
		}
		if (o.getClass() == Timestamp.class) {
			return formatDate(new Date(((Timestamp) o).getTime()));
		} else {
			return o.toString();
		}
	}

	/**
	 * 将Object类型的数据格式化成String类型。
	 * 
	 * @param o Object
	 * 
	 * @return String
	 */
	public static String formatDateTime(Object o) {
		if (o.getClass() == String.class) {
			return formatDateTime((String) o);
		}
		if (o.getClass() == Date.class) {
			return formatDateTime((Date) o);
		}
		if (o.getClass() == Timestamp.class) {
			return formatDateTime(new Date(((Timestamp) o).getTime()));
		} else {
			return o.toString();
		}
	}

	/**
	 * 获取年份。
	 * 
	 * @param date Date
	 * @return int
	 */
	public static int getYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(1);
	}

	/**
	 * 增加。
	 * 
	 * @param date Date
	 * @param field int
	 * @param amount int
	 * 
	 * @return Date
	 */
	public static Date add(Date date, int field, int amount) {
		if (date == null) {
			date = new Date();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(field, amount);
		return cal.getTime();
	}

	/**
	 * 添加毫秒。
	 * 
	 * @param date Date
	 * @param amount int
	 * @return Date
	 */
	public static Date addMilliSecond(Date date, int amount) {
		return add(date, 14, amount);
	}

	/**
	 * 添加秒。
	 * 
	 * @param date Date
	 * @param amount int
	 * @return Date
	 */
	public static Date addSecond(Date date, int amount) {
		return add(date, 13, amount);
	}

	/**
	 * 添加分钟。
	 * 
	 * @param date Date
	 * @param amount  int
	 * 
	 * @return Date
	 */
	public static Date addMinute(Date date, int amount) {
		return add(date, 12, amount);
	}

	/**
	 * 添加小时。
	 * 
	 * @param date Date
	 * @param amount int
	 * 
	 * @return Date
	 */
	public static Date addHour(Date date, int amount) {
		return add(date, 10, amount);
	}

	/**
	 * 添加天。
	 * 
	 * @param date Date
	 * @param amount int
	 * 
	 * @return Date
	 */
	public static Date addDay(Date date, int amount) {
		return add(date, 5, amount);
	}

	/**
	 * 添加月。
	 * 
	 * @param date Date
	 * @param amount int
	 * 
	 * @return Date
	 */
	public static Date addMonth(Date date, int amount) {
		return add(date, 2, amount);
	}

	/**
	 * 添加年。
	 * 
	 * @param date  Date
	 * @param amount int
	 * 
	 * @return Date
	 */
	public static Date addYear(Date date, int amount) {
		return add(date, 1, amount);
	}

}
