package com.nostalgiarunner.watson.stt;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpeechToText {
	
	static String agent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36";

	public static void submit(File input){
		System.out.println(getSTTToken());
	}

//	private String getSTTToken(){
//		BufferedReader reader = null;
//	    try {
//	    	String[] orig = getCSRF().split(" BREAK ");
//	    	String cookie = orig[1];
//	    	String csrf = orig[0];
//	        SSLSocketFactory factory=(SSLSocketFactory) SSLSocketFactory.getDefault();
//	        SSLSocket s = (SSLSocket) factory.createSocket(InetAddress.getByName("speech-to-text-demo.mybluemix.net"), 443);
//	    	PrintWriter pw = new PrintWriter(s.getOutputStream());
//	    	pw.println("POST /api/token HTTP/1.1");
//	    	pw.println("Host: speech-to-text-demo.mybluemix.net");
//	    	pw.println("Connection: keep-alive");
//	    	pw.println("Content-Length: 0");
//	    	pw.println("csrf-token: " + csrf);
//	    	pw.println("Origin: https://speech-to-text-demo.mybluemix.net");
//	    	pw.println("User-Agent: " + agent);
//	    	pw.println("Accept: */*");
//	    	pw.println("Referer: https://speech-to-text-demo.mybluemix.net/");
//	    	pw.println("Accept-Encoding: gzip, deflate, br");
//	    	pw.println("Accept-Language: en-US,en;q=0.8");
//	    	pw.println("Cookie: _csrf=" + cookie);
//	    	pw.println("");
//	    	pw.flush();
//	    	BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
//	    	try {
//	    	String token = null;
//	    	String t;
//	    	while((t = br.readLine()) != null) {
//	    	if (!t.equals("0")){
//	    	token = t;
//	    	} else {
//	    		br.close();
//	    		s.close();
//	    		return token;
//	    	}
//	    	}
//	    	br.close();
//	    	s.close();
//	    	return null;
//	    	} catch (IOException e){
//	    	}
//	    } catch (Exception e){
//	    	e.printStackTrace();
//	    }
//		return null;
//	}
	
	public static String getSTTToken(){
		BufferedReader reader = null;
	    try {
	    	String[] orig = getCSRF().split(" BREAK ");
	    	String csrf = orig[1];
	    	String cookie = orig[0];
	    	System.out.println(csrf);
	    	System.out.println(cookie);
	        URL url = new URL("https://speech-to-text-demo.mybluemix.net/api/token");
	        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			uc.setRequestMethod( "POST" );
			uc.setRequestProperty("Host", "speech-to-text-demo.mybluemix.net");
			uc.setRequestProperty("Connection", "keep-alive");
			uc.setRequestProperty("Content-Length", "0");
			uc.setRequestProperty("Cache-Control", "no-cache");
			uc.setRequestProperty("csrf-token", csrf);
			uc.setRequestProperty("Origin", "https://speech-to-text-demo.mybluemix.net");
			uc.setRequestProperty("User-Agent", agent);
			uc.setRequestProperty("Accept", "*/*");
			uc.setRequestProperty("Referer", "https://speech-to-text-demo.mybluemix.net/");
			uc.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
			uc.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
			uc.setRequestProperty("Cookie", "_csrf=" + cookie);

	        reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
	        StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[1024];
	        while ((read = reader.read(chars)) != -1)
	            buffer.append(chars, 0, read); 
	        uc.disconnect();
	        reader.close();
	        return buffer.toString();
	    } catch (Exception e) {
			e.printStackTrace();
	    }
		return null;
	}
	
	public static String getCSRF(){
		try {
			Response resp = Jsoup.connect("https://speech-to-text-demo.mybluemix.net/")
					.header("Host", "speech-to-text-demo.mybluemix.net")
					.header("Connection", "keep-alive")
					.header("Upgrade-Insecure-Requests", "1")
					.header("User-Agent", agent)
					.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
					.header("Accept-Encoding", "gzip, deflate, sdch, br")
					.header("Accept-Language", "en-US,en;q=0.8")
					.execute();
			Document doc = resp.parse();
			Elements metaTags = doc.getElementsByTag("meta");
			for (Element metaTag : metaTags) {
				  String content = metaTag.attr("content");
				  String name = metaTag.attr("name");
				  if (name.equals("ct")){
					  return content + " BREAK " + resp.cookie("_csrf");
				  }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
