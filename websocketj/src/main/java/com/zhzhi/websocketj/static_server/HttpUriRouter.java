package com.zhzhi.websocketj.static_server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import com.zhzhi.websocketj.utils.Logger;

/**
 * uri router
 * get static resource byte stream from specific uri
 * @author zhzhi
 *
 */
public class HttpUriRouter {
	String OS = System.getProperty("os.name").toLowerCase();  
	private String static_dir;
	
	public HttpUriRouter(String static_dir) {
		this.static_dir = static_dir;
	}
	
	public byte[] bytesFromUri(String uri) {
		byte[] result =  null;

		try {
			String separator = "";
			
			if(OS.indexOf("windows") >= 0) {
				separator = "\\";
			} else {
				separator = "/";
			}
			
			if(!static_dir.endsWith(separator)) {
				static_dir += separator;
			}
			
			String file_path = static_dir;
			
			String[] units = uri.split("/");
			
			for(int i = 0; i < units.length; ++i) {
				if (!units[i].isEmpty()) {
					if (i != units.length - 1) {
						file_path += (units[i] + separator);
					} else {
						file_path += units[i];
					}
				}
			}
			
			File f  = new File(file_path);
			FileInputStream fs = new FileInputStream(f);
			
			ByteArrayOutputStream result_tmp = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length;
			while ((length = fs.read(buffer)) != -1) {
			    result_tmp.write(buffer, 0, length);
			}
			
			result = result_tmp.toByteArray();
			
		} catch (Exception e) {
			Logger.error("read static resource from disk failed just now!");
		}
		return result;
	}
}
