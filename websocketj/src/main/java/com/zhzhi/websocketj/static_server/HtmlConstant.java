package com.zhzhi.websocketj.static_server;

/**
 * default html page
 * @author zhzhi
 *
 */
public class HtmlConstant {

	public static String html404 = "<h1 align='center'>File Not Found</h1>"
								 + "<div align='center'>you can only access static files on this server, such as html, css, js and images etc.</div>";
	
	public static String htmlHome = "<h2 align='center' style='margin-top:30px;'>Welcome to websocketj world!</h2>"
								  + "<div align='center' style='margin-top:20px;'>"
								  + "<p>how to use?</p>"
								  + "</div>"
								  
								  + "<div style='width:1200px;font-size:14px;margin-left:auto;margin-right:auto;'>"
			                      + "<ul>"
								  + "<li>step1: develop your serverside program, MUST surpport stdin/stdout. you can use any language on any platform, java, go, C#, C++ and etc, depends on yourself.</li>"
			                      + "<li>step2: develop your frontend program(html), CAN receive data from backend and send data to it with WebSocket technology.</li>"
								  + "<li>step3: put your html(css/js/image) static resources into a directory on the server, say '/usr/local/websocketj/html/'</li>"
			                      + "<li>step4: start the websocketj with parameters, say 'websocketj --wsport=8081 --ssport=8082 --staticDir=/usr/local/websocketj/html/ yourprogram args...'</li>"
			                      + "<li>step5: visit the html page via any web browser that surpports WebSocket protocal. Now you can operate the serverside program with your local browser!</li>"
								  + "</ul>"
			                      + "</div>"
								  
			                      + "<br/>"
			                      + "<div align='center' style='font-size:12px;'>"
								  + "<p><a href='https://github.com/sherlockchou86'>follow me on github</a></p>"
			                      + "<p><a href='https://github.com/sherlockchou86'>fork on github</a></p>"
								  + "</div>";
}
