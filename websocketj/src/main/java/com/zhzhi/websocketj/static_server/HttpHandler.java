package com.zhzhi.websocketj.static_server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import com.zhzhi.websocketj.utils.Logger;

/**
 * handle http request
 * NOTE:
 * only one http request for one handler, then close the socket and quit the thread.
 * @author zhzhi
 *
 */
public class HttpHandler implements Runnable {
	// client socket, we need resolve url, paramters and others from this socket's input stream
	private Socket request_socket;
	// static resource directory
	private String static_dir;
	
	public HttpHandler(Socket request_socket, String static_dir) {
		this.request_socket = request_socket;
		this.static_dir = static_dir;
	}
	
	@Override
	public void run() {
		try {
			InputStream request = request_socket.getInputStream();
			OutputStream response = request_socket.getOutputStream();
			
			// read request string from socket, such as
            // GET /index.html HTTP/1.1
            // Host: 127.0.0.1:8081
            // Connection: keep-alive
            // Cache-Control: max-age=0
			ByteArrayOutputStream result = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024 * 512];
			int length = request.read(buffer);
			result.write(buffer, 0, length);
			
			String request_str = result.toString("utf-8");
			
			Logger.rawLog("----------------received http request----------------");
			Logger.rawLog(request_str);
			Logger.rawLog("-----------------------------------------------------");
			
			// get uri, such as /index.html
			HttpUriParser parser = new HttpUriParser();
			String uri = parser.parse(request_str);
			
			if (uri == null) {
				// send 404 page
				send404Response(response);
			} else if(uri.equals("/") || uri.equals("")) {
				// send default home page
				sendDefaultPageResponse(response);
			} else {
				// get static resource bytes from uri
				HttpUriRouter router = new HttpUriRouter(static_dir);
				byte[] response_bytes =  router.bytesFromUri(uri);
				
				if (response_bytes == null) {
					// send 404
					send404Response(response);
				} else {
					// send static resource
					send200Response(response_bytes, uri, response);
				}
			}
			
			try {
				request_socket.close();
			} catch (Exception e) {
				
			}
		} catch (Exception e) {
			Logger.error("handle http reqeust failed just now!");
		}
	}
	
	/**
	 * send 404 response to client
	 * @return
	 */
	private void send404Response(OutputStream oupt) {
		try {
			byte[] bytes = HtmlConstant.html404.getBytes("utf-8");
			
			String status_line = "HTTP/1.1 404 File Not Found\r\n"; 
			String head_lines = "Content-Type: text/html;charset=utf-8\r\n"
	                          + "Content-Length: " + bytes.length + "\r\n";
			String space_line = "\r\n";

			oupt.write(status_line.getBytes("utf-8"));
			oupt.write(head_lines.getBytes("utf-8"));
			oupt.write(space_line.getBytes("utf-8"));
			oupt.write(bytes);
			oupt.flush();
			
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * send default home page to client
	 * @param oupt
	 */
	private void sendDefaultPageResponse(OutputStream oupt) {
		try {
			byte[] bytes = HtmlConstant.htmlHome.getBytes("utf-8");
			
			String status_line = "HTTP/1.1 200 OK\r\n"; 
			String head_lines = "Content-Type: text/html;charset=utf-8\r\n"
	                          + "Content-Length: " + bytes.length + "\r\n";
			String space_line = "\r\n";

			oupt.write(status_line.getBytes("utf-8"));
			oupt.write(head_lines.getBytes("utf-8"));
			oupt.write(space_line.getBytes("utf-8"));
			oupt.write(bytes);
			oupt.flush();
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * send 200 response to client
	 * ----------------------------
	 * HTTP/1.1 200 OK\r\n
	 * Content-Type:text/html;charset=UTF-8\r\n
	 * Content-Length:1234\r\n
	 * ...
	 * <space line>
	 * data
	 * ----------------------------
	 * @param content
	 * @param uri
	 */
	private void send200Response(byte[] content, String uri, OutputStream oupt) {
		String status_line = "HTTP/1.1 200 OK\r\n";
		String header_lines = "Content-Type: " + getContentType(uri) + "\r\n"
				            + "Content-Length: " + content.length + "\r\n";
		String space_line = "\r\n";
		
		try {
			oupt.write(status_line.getBytes("utf-8"));
			oupt.write(header_lines.getBytes("utf-8"));
			oupt.write(space_line.getBytes("utf-8"));
			oupt.write(content);
			
			oupt.flush();
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * get content type from uri
	 * @param uri
	 * @return
	 */
	private String getContentType(String uri) {
		if (uri.endsWith(".html")) {
			return "text/html;charset=utf-8";
		} else if(uri.endsWith(".css")) {
			return "text/css;charset=utf-8";
		} else if(uri.endsWith(".js")) {
			return "text/javascript;charset=utf-8";
		} else if(uri.endsWith("png")) {
			return "image/png";
		} else if(uri.endsWith("gif")) {
			return "image/gif";
		} else if(uri.endsWith("jpeg") || uri.endsWith("jpg")) {
			return "image/jpg";
		} else {
			return "";
		}
	}
}
