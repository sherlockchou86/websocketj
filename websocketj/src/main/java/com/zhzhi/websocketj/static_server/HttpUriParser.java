package com.zhzhi.websocketj.static_server;

public class HttpUriParser {
	public String parse(String request_str) {
		String uri = null;
		// we can do more logic check here
        String[] lines = request_str.split("\r\n");  
        if (lines.length > 0) {
        	// lines[0] is 'GET /index.html HTTP/1.1'
        	uri = lines[0].split(" ")[1];
        }		
		return uri;
	}
}
