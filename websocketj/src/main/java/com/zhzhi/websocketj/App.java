package com.zhzhi.websocketj;


import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.java_websocket.server.WebSocketServer;

import com.zhzhi.websocketj.process.IOnOutputPrinted;
import com.zhzhi.websocketj.process.TargetProcess;
import com.zhzhi.websocketj.server.IOnMsgReceived;
import com.zhzhi.websocketj.server.WebSocketJServer;
import com.zhzhi.websocketj.static_server.StaticServer;
import com.zhzhi.websocketj.utils.Logger;

/**
 * the application startup entrance 
 *
 */
public class App 
{
    public static void main(String[] args)
    {
    	if(args.length == 1 && (args[0].equals("--help") || args[0].equals("--h"))) {
    		printHelp();
    	} else if(args.length >= 4) {
        	
    		Map<String, String> parameters = parseParameters(args);
    		
    		if (parameters == null) {
        		Logger.error("invalid parameters! usage help:");
        		printHelp();
        		return;
    		}
    		
    		// listen at all host
    		String host = "0.0.0.0";
    		int wsport = Integer.parseInt(parameters.get("wsport"));
    		int ssport = Integer.parseInt(parameters.get("ssport"));
    		String static_dir = parameters.get("staticDir");		
    		String command = parameters.get("command");
    		
    		// append the parameters to the end of command line
    		for(int i = 4; i < parameters.size(); ++i) {
    			command += " " + parameters.get("command_param" + i);
    		}
    		
    		Logger.log("[websocket server port]->" + wsport + ", [static server port]->" + ssport + ", [static resource directory]->" + static_dir + ", [command]->" + command);
    		
    		Logger.log("starting external process ...");
    		final TargetProcess process = new TargetProcess(command);
    		if (!process.start()) {
    			return;
    		}
    		
    		Logger.log("initializing static web server ...");
    		final StaticServer static_server = new StaticServer(ssport, host, static_dir);
    		if (!static_server.run()) {
    			return;
    		}
    		    		    		
    		Logger.log("initializing websocket server ...");
    		final WebSocketJServer server = new WebSocketJServer(host, wsport);
    		server.addMsgReceivedListener(new IOnMsgReceived(){
				@Override
				public void onMsgReceived(String msg) {
					process.write(msg);
				}
    			
    		});
    		process.addOutputPrintedListener(new IOnOutputPrinted() {
				@Override
				public void onOutputPrinted(String output) {
					server.sendToAll(output);
				}	
    		});
    		server.run();

    	} else {
    		Logger.error("invalid parameters! usage help:");
    		printHelp();
    	}
    }
    
    /**
     * print help
     */
    private static void printHelp() {
    	System.out.println("-----------------------------usage---------------------------");
    	System.out.println("websocketj --help");
    	System.out.println("websocketj --wsport=[port] --ssport=[port] --staticDir=[directory] [command] [command args...]");
    	System.out.println();
    	System.out.println("--wsport:     the port that will be listened by WebSocket server.");
    	System.out.println("--ssport:     the port that will be listened by static server, which can handle static resource request.");
    	System.out.println("--staticDir:  the local static resource directory which will be mapped to uri.");
    	System.out.println("command:      start external process, can be a executable path or command line.");
    	System.out.println("command args: the parameters to start the external process.");
    	System.out.println("-------------------------------------------------------------");
    }
    
    /**
     * parse commandline paramters
     * @param args
     * @return
     */
    private static Map<String, String> parseParameters(String[] args) {
    	Map<String, String> params = new HashMap<String, String>();
    	
    	for(int i = 0; i < args.length; ++i) {
    		if (args[i].startsWith("--wsport=")) {
    			params.put("wsport", args[i].split("=")[1]);
    		} else if (args[i].startsWith("--ssport=")) {
    			params.put("ssport", args[i].split("=")[1]);
    		} else if (args[i].startsWith("--staticDir=")) {
    			params.put("staticDir", args[i].split("=")[1]);
    		} else {
    			if (i == 3) {
    				params.put("command", args[i]);
    			} else {
    				params.put("command_param" + i, args[i]);
    			}
    		}
    	}
    	
    	if(!params.containsKey("wsport") 
    			|| !params.containsKey("ssport") 
    			|| !params.containsKey("staticDir") 
    			|| !params.containsKey("command")){
    		return null;
    	}
    	
    	return params;
    }
}
