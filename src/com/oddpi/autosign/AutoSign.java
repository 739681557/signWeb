package com.oddpi.autosign;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class AutoSign {
	
	public static void loginPost(String user_name, String password) throws IOException {
		String login = "";
		String tmpDir = "D:/tmp/";
		//String tmpDir = "/tmp/";
		String tmpNum = "";
		String str = "http://10.1.12.62:8000/sso/login";
		
		/*SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:MM:ss");
		System.out.println(user_name + "签到时间:" + sf.format(new Date()));*/
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmss");
		tmpNum = sdf.format(new Date());
		URL url = new URL("http://10.1.12.xx:8000/sso/login");
		HttpURLConnection connection = null;
		connection = (HttpURLConnection) url.openConnection();// 建立链接
		connection.setInstanceFollowRedirects(false);
		connection.setRequestProperty("Connection", "keep-alive");
		connection
				.setRequestProperty(
						"User-Agent",
						"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/51.0.2704.79 Chrome/51.0.2704.79 Safari/537.36");
		connection.addRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		// connection.disconnect();
		// String str = connection.getHeaderField("");//获得重定向的url地址
		// System.out.println(str);
		URL newURL = new URL(str);
		String cookies = getCookies(connection);
		HttpURLConnection conn = (HttpURLConnection) newURL.openConnection();
		System.out.println(cookies);
		conn.setRequestProperty("Referer", str);// 浏览器向 WEB 服务器表明自己是从哪个 网页/URL
												// 获得/点击 当前请求中的网址/URL
		conn.setRequestProperty("Cookie", cookies); // 发送设置cookie：
		conn.setRequestProperty("Connection", "keep-alive");
		conn.setRequestProperty(
				"User-Agent",
				"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/51.0.2704.79 Chrome/51.0.2704.79 Safari/537.36");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(),
				"utf-8");
		login = login + "username=" + user_name + "&" + "password=" + password
				+ "&lt=e1s1" + "&_eventId=submit";
		out.write(login);
		out.flush();
		out.close();
		InputStream inputStream = conn.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream, "utf-8"));
		reader.close();
		// 链接到personal页面
		String headerName = null;
		StringBuilder myCookies = new StringBuilder();
		// myCookies.append(cookies + ";");
		for (int i = 1; (headerName = conn.getHeaderFieldKey(i)) != null; i++) {
			if (headerName.equals("Set-Cookie")) {
				String cookie = conn.getHeaderField(i);
				cookie = cookie.substring(0, cookie.indexOf(";"));
				String cookieName = cookie.substring(0, cookie.indexOf("="));
				String cookieValue = cookie.substring(cookie.indexOf("=") + 1,
						cookie.length());
				myCookies.append(cookieName + "=");
				myCookies.append(cookieValue + ";");
			}
		}
		// URL purl = new URL("http://10.1.12.62:8000/sso/login");
		String signUrl, week;
		week = getWeekOfDate(new Date());
		if( week.equals("星期六") || week.equals("星期日") ) {
			signUrl = "http://10.1.xx.xx/xxx/workAttendance.do?method=list";
		} else {
			signUrl = "http://10.1.xx.xx/xxx/workAttendance.do?method=add";
		}
		URL purl = new URL(signUrl);
		HttpURLConnection pconn = (HttpURLConnection) purl.openConnection();
		pconn.setRequestProperty("Referer", str);
		pconn.setRequestProperty("Cookie", myCookies.toString());
		pconn.connect();

		InputStream inputStream1 = pconn.getInputStream();
		int chByte = 0;
		FileOutputStream fileOut = new FileOutputStream(new File(tmpDir + user_name + tmpNum + ".txt"));
		chByte = inputStream1.read();
		while (chByte != -1) {
			fileOut.write(chByte);
			chByte = inputStream1.read();
		}
		fileOut.close();

		readLoginJsoup(tmpDir + user_name + tmpNum + ".txt");
	}

	private static String getWeekOfDate(Date dt) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	private static String getCookies(HttpURLConnection conn) {
		// TODO Auto-generated method stub
		// StringBuffer cookies = new StringBuffer();
		StringBuilder cookies = new StringBuilder();
		String headName;

		for (int i = 1; (headName = conn.getHeaderField(i)) != null; i++) {
			StringTokenizer st = new StringTokenizer(headName, "; ");
			while (st.hasMoreTokens()) {
				cookies.append(st.nextToken() + "; ");
			}
		}
		return cookies.toString();
	}

	private static void readLoginJsoup(String outPath) throws IOException {
		// 解析html文档
		File input = new File(outPath);
		// Document doc = Jsoup.parse(input, "UTF-8");
		Document doc = Jsoup.parse(input, "GB2312");
		// for(Element ele : doc.getElementsByClass("zhnc").select("ul")){
		// if(!ele.select("li").toString().equals("")){
		// String text = ele.select("li").text();
		// System.out.println("user_name is:"+text);
		// }
		// }
		Elements ele = doc.getElementsByTag("script").eq(0);
		if (!ele.toString().equals("")) {
			String eleStr = ele.toString();
			String reg = "[^\u4e00-\u9fa5]";
			eleStr = eleStr.replaceAll(reg, "");
			System.out.println(eleStr);
		} else {
			System.out.println("签到失败");
		}

	}
}
