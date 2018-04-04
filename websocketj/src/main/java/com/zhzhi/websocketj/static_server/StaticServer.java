package com.zhzhi.websocketj.static_server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.zhzhi.websocketj.utils.Logger;

/**
 * a simple static web server
 * NOTE:
 * just handle static resource http request, such as html, css, js and image
 * the request uri MUST end with full file name, such as http://192.168.1.100:8081/html/index.html
 * 
 * @author zhzhi
 *
 */
public class StaticServer {
	private int port = -1;
	private String host;
	
	private String static_dir;
	
	private ServerSocket server_socket;
	private int backlog = 100;
	
	private ExecutorService pool;
	private int maxThreads = 50;
	
	public StaticServer(int port, String host, String staticDir) {
		this.port = port;
		this.host = host;
		
		this.static_dir = staticDir;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean run() {
		if (port == -1 || host == null || static_dir == null) {
			Logger.error("static server startup parameters invalid!");
			return false;
		}
		
		if (server_socket != null) {
			Logger.error("static server is already started!");
			return false;
		}
		
		try {
			server_socket = new ServerSocket(port, backlog, InetAddress.getByName(host));
			
			// start accept socket
			new Thread(new Runnable(){
				@Override
				public void run() {
					while (server_socket != null) {	
						try {
							Socket client_socket = server_socket.accept();
							
							if (pool == null) {
								pool = Executors.newFixedThreadPool(maxThreads);
							}
							
							// create new handler to handle this request
							HttpHandler handler = new HttpHandler(client_socket, static_dir);						
							pool.execute(handler);
							
						} catch (IOException e) {
							
						}			
					}
				}
			}).start();
			
		} catch (Exception e) {
			Logger.error("static server start failed, ex :" + e.getMessage());
		}
		Logger.log("static web server started successfully!");
		return true;
	}
	
	/**
	 * 
	 */
	public void stop() {
		if (server_socket != null) {
			try {
				server_socket.close();
			} catch (IOException e) {
				
			}
			server_socket = null;
		}
	}
	
	
}
