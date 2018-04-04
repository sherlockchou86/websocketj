package com.zhzhi.websocketj.server;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.zhzhi.websocketj.utils.Logger;

/**
 * websocket server
 * @author zhzhi
 *
 */
public class WebSocketJServer extends WebSocketServer {
	
	private IOnMsgReceived onMsgReceived;
	
	public WebSocketJServer(String host, int port) {
		super(new InetSocketAddress(host, port));
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		Logger.log("new connection from " + conn.getRemoteSocketAddress());
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		Logger.log("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		Logger.log("received message from "	+ conn.getRemoteSocketAddress() + ": " + message);
		
		// notify the string message, ignore the client parameter currently
		if (onMsgReceived != null) {
			onMsgReceived.onMsgReceived(message);
		}
	}

	@Override
	public void onMessage( WebSocket conn, ByteBuffer message ) {
		Logger.log("received ByteBuffer from " + conn.getRemoteSocketAddress());
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		Logger.log("an error occured on connection " + conn.getRemoteSocketAddress()  + ":" + ex);
	}
	
	@Override
	public void onStart() {
		Logger.log("web socket server started successfully!");
	}
	
	/**
	 * broadcast the msg to all clients
	 * @param msg
	 */
	public void sendToAll(String msg) {
		broadcast(msg);
		Logger.log("sent messsage to all clients :" + msg);
	}
	
	/**
	 * 
	 * @param listener
	 */
	public void addMsgReceivedListener(IOnMsgReceived listener) {
		onMsgReceived = listener;
	}
}
