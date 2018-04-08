package com.zhzhi.websocketj.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

import com.zhzhi.websocketj.utils.Logger;

/**
 * external process
 * @author zhzhi
 *
 */
public class TargetProcess {
	
	private Process process;
	
	private InputStream inputStream;
	private OutputStream outputStream;
	
	private IOnOutputPrinted onOutputPrinted;
	
	private String command;
	public String getCommand() {
		return command;
	}

	public TargetProcess(String command) {
		this.command = command;
	}
	
	/**
	 * start the external process, try to get the STDIN and STDOUT
	 * @return
	 */
	public boolean start() {
		if (process != null) {
			return false;
		}
		
		try {
			process = Runtime.getRuntime().exec(command);
			inputStream = process.getInputStream();
			outputStream = process.getOutputStream();
			
			new Thread(new Runnable() {
				
				// read output from external process's STDOUT
				@Override
				public void run() {
					try {
						// using GB2312 for Chinese OS, you can switch to utf-8 if you encounter the encoding is not right
						BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "GB2312"));
						String output = null; 
						 while (process != null) {  
							 output = br.readLine();
							 if(output != null) {
								 if (onOutputPrinted != null) {
									 onOutputPrinted.onOutputPrinted(output);
								 }
							 } else {
								 try {
								 	 Thread.sleep(500);
								 } catch (InterruptedException e) {
									 e.printStackTrace();
								 }
							 }
						 }  
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				
			}).start();
			
		} catch (IOException e) {
			e.printStackTrace();
			Logger.error("start process failed! ex: " + e.getMessage());
			process = null;
			return false;
		}
		Logger.log("process started successfully!");
		return true;
	}
	
	/**
	 * stop the external process
	 */
	public void stop() {
		if (outputStream != null) {
			try {
				outputStream.close();
			} catch(IOException e) {
				
			}
		}
		
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (process != null) {
			process.destroy();
		}
		process = null;
	}
	
	
	/**
	 * input to target process's STDIN
	 * @param input
	 */
	public void write(String input) {
		if (outputStream != null) {
			try {
				PrintStream ps = new PrintStream(outputStream, true); 
				ps.println(input);
			} catch (Exception e) {
				Logger.error("write failed! ex: " + e.getMessage());
			}
		}
	}
	
	/**
	 * 
	 * @param listener
	 */
	public void addOutputPrintedListener(IOnOutputPrinted listener) {
		onOutputPrinted = listener;
	}
}
