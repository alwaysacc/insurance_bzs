package com.bzs.utils.dateUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

public class DateUtil {
	public static void main(String[] args) {
		// 1543075199
		// System.out.println(stampToDate("1543075199000"));

		/*
		 * int status=compareDate("2018-12-12 9:12:12","2018-12-18",null);
		 * System.out.println(status);
		 */
		stringToDate("2018-12-12 9:12:12");
		getStringDate(null);
	}
	public static Date getDateToDate(Date date, String style) {
		DateFormat dFormat3 = new SimpleDateFormat(style);
		try {
			date = dFormat3.parse(dFormat3.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;

	}
	//日期转字符串
	public static String getDateToString(Date date, String style) {
		DateFormat dFormat = new SimpleDateFormat(style);
		return dFormat.format(date);
	}

	public static Date getStringToDate(String date, String style) {
		if (date.indexOf("/") > -1) {
			date = date.replace("/", "-");
		}
		DateFormat dFormat = new SimpleDateFormat(style);
		Date dat = null;
		try {
			dat = dFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dat;
	}

	public static Object getNowDateStringType(String style, String type) {
		Date date = new Date();
		DateFormat dFormat = new SimpleDateFormat(style);
		if ("string".equals(type)) {

			return dFormat.format(date);
		} else {

			try {
				return dFormat.parse(dFormat.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;

	}

	/*
	 * 时间字符串转时间戳 返回值 秒
	 */

	public static String dateToStamp(String s) {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = simpleDateFormat.parse(s);
			long ts = date.getTime() / 1000;
			res = String.valueOf(ts);
			return res;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "";
	}

	/*
	 * 将时间戳转换为时间
	 */
	public static String stampToDate(String seconds, String format) {

		if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
			return "";
		}
		if (format == null || format.isEmpty()) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(Long.valueOf(seconds + "000")));
		// SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		/*
		 * long lt = new Long(s); Date date = new Date(lt); res =
		 * simpleDateFormat.format(date);
		 */
		// return res;
	}

	/**
	 * 比较日期大小，date1>date2 返回1，date1<date2返回0，date1=date2返回2，其中一个为空返回 -1
	 * 
	 * @param date1
	 *            时间字符换1
	 * @param date2
	 *            时间字符串2
	 * @param datePattern
	 *            日期格式，为空默认为"yyyy-MM-dd"
	 * @return date1>date2 返回1，date1<date2返回0，date1=date2返回2，其中一个为空返回 -1
	 */
	public static int compareDate(String date1, String date2, String datePattern) {
		if (date1 != null && !"".equals(date1) && date2 != null
				&& !"".equals("date2")) {
			if (datePattern == null || "".equals(datePattern)) {
				datePattern = "yyyy-MM-dd";
			}
			SimpleDateFormat format = new SimpleDateFormat(datePattern);
			try {
				Date date3 = format.parse(date1);
				Date date4 = format.parse(date2);
				return compareDateByGetTime(date3, date4);
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
		return -1;

	}

	/**
	 * 
	 * @param date1
	 *            日期格式
	 * @param date2
	 *            日期格式
	 * @return date1>date2 返回1，date1<date2返回0，date1=date2返回2，其中一个为空返回 -1
	 */
	public static int compareDateByGetTime(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return -1;
		}
		if (date1.getTime() < date2.getTime()) {
			System.out.println(date1 + "在" + date2 + "前面");
			return 0;
		} else if (date1.getTime() > date2.getTime()) {
			System.out.println(date1 + "在" + date2 + "后面");
			return 1;
		} else {
			System.out.println("是同一天的同一时间");
			return 2;
		}
	}

	public void testcompareDate() throws ParseException {
		/*Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2019-01-09");
		System.out.println("ssss" + date.getTime());
		System.out.println(DateUtil.stampToDate("1547046000",
				"yyyy-MM-dd HH:mm:ss"));
		System.out.println(getStringToDate(
				DateUtil.stampToDate("1547046000", "yyyy-MM-dd HH:mm:ss"),
				"yyyy-MM-dd HH:mm:ss"));*/
		int status = compareDate("14:12:12", "13:12:12", "HH:mm:ss");
		System.out.println(status);
		Calendar date=Calendar.getInstance();
		int year=date.get(Calendar.YEAR);
		int month=date.get(Calendar.MONTH);
		month++;
		int day=date.get(Calendar.DAY_OF_MONTH);
		int hour=date.get(Calendar.HOUR_OF_DAY);
		int minute=date.get(Calendar.MINUTE);
		int second=date.get(Calendar.SECOND);
		String AddDate=year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
		System.out.println(AddDate);
		getStandardBJDate();
	}

	/**
	 * 字符串转换为java.util.Date<br>
	 * 支持格式为 yyyy.MM.dd G 'at' hh:mm:ss z 如 '2017-12-12 AD at 22:10:59 PSD'<br>
	 * yy/MM/dd HH:mm:ss 如 '2017/12/12 17:55:00'<br>
	 * yy/MM/dd HH:mm:ss pm 如 '2017/12/12 17:55:00 pm'<br>
	 * yy-MM-dd HH:mm:ss 如 '2017-12-12 17:55:00' <br>
	 * yy-MM-dd HH:mm:ss am 如 '2017-12-12 17:55:00 am' <br>
	 * 
	 * @param time
	 *            String 字符串<br>
	 * @return Date 日期<br>
	 */
	public static Date stringToDate(String time) {
		SimpleDateFormat formatter;
		int tempPos = time.indexOf("AD");
		time = time.trim();
		formatter = new SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss z");
		if (tempPos > -1) {
			time = time.substring(0, tempPos) + "公元"
					+ time.substring(tempPos + "AD".length());// china
			formatter = new SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss z");
		}
		tempPos = time.indexOf("-");
		if (tempPos > -1 && (time.indexOf(" ") < 0)) {
			formatter = new SimpleDateFormat("yyyyMMddHHmmssZ");
		} else if ((time.indexOf("/") > -1) && (time.indexOf(" ") > -1)) {
			formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		} else if ((time.indexOf("-") > -1) && (time.indexOf(" ") > -1)) {
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} else if ((time.indexOf("/") > -1) && (time.indexOf("am") > -1)
				|| (time.indexOf("pm") > -1)) {
			formatter = new SimpleDateFormat("yyyy-MM-dd KK:mm:ss a");
		} else if ((time.indexOf("-") > -1) && (time.indexOf("am") > -1)
				|| (time.indexOf("pm") > -1)) {
			formatter = new SimpleDateFormat("yyyy-MM-dd KK:mm:ss a");
		}
		ParsePosition pos = new ParsePosition(0);
		Date ctime = formatter.parse(time, pos);
		return ctime;
	}

	/**
	 * 将字符串时间格式转为yyyy-MM-dd格式
	 * 
	 * @param date
	 * @return
	 */
	public static String getStringDate(String date) {
		if(StringUtils.isNotBlank(date)){
			int endIndex = date.indexOf(" ");
			if ((date.indexOf("/") > -1) && (date.indexOf(" ") > -1)) {
				date = date.replace("/", "-");
				date = date.substring(0, endIndex);
			}
			if ((date.indexOf("-") > -1) && (date.indexOf(" ") > -1)) {
				date = date.substring(0, endIndex);
			}
			//return date;
		}else{
			date="";
		}
		System.out.println(date);
		return date;

	}
	
	public  static String getStandardBJDate(){
		  Calendar calendar = Calendar.getInstance(Locale.CHINA);
	        Date date = calendar.getTime();
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String dateString = dateFormat.format(date);
	        System.out.println(dateString);
		return null;
	}
}
