package com.mingslife.matrix.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

import com.mingslife.matrix.entity.Configuration;
import com.mingslife.matrix.entity.RequestReport;

public class Receiver implements Runnable {
	private static Logger logger = new Logger(Receiver.class);

	private Socket socket;

	@Override
	public void run() {
		try {
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
//			PrintWriter out = new PrintWriter(outputStream, true);
			RequestReport requestReport = new RequestReport(inputStream);
			String method = requestReport.getMethod();
			String url = requestReport.getUrl();
//			String path = "F:/matrix" + url;
			int queryIndex = url.indexOf("?");
			String path = "F:/github/mingslife/LuminoSGCC" + (queryIndex == -1 ? url : url.substring(0, queryIndex));
			int statusCode;
			String statusMessage;
			File file = new File(path);
			if (file.exists() && !file.isDirectory()) {
				long lastModified = file.lastModified();
				String ifModifiedSinceString = requestReport.getHeader("If-Modified-Since");
				boolean useCache = false;
				if (ifModifiedSinceString != null) {
//					System.out.println(ifModifiedSinceString);
					try {
						long ifModifedSince = Long.parseLong(ifModifiedSinceString);
						useCache = lastModified == ifModifedSince;
					} catch (Exception e) {
					}
				}
				String responseReport;
				if (useCache) {
					statusCode = 304;
					statusMessage = "Not Modified";
					responseReport = "HTTP/1.1 304 Not Modified\r\nServer: " + Configuration.DISPLAY_SERVER_NAME + "\r\n\r\n";
					outputStream.write(responseReport.getBytes());
				} else {
					statusCode = 200;
					statusMessage = "OK";
					responseReport = "HTTP/1.1 200 OK\r\nLast-Modified: " + lastModified + "\r\nServer: " + Configuration.DISPLAY_SERVER_NAME + "\r\n\r\n";
					outputStream.write(responseReport.getBytes());
					outputStream.flush();
					InputStream in = new FileInputStream(file);
					byte[] buffer = new byte[102400];
					int len = 0;
					while ((len = in.read(buffer, 0, 102400)) != -1) {
						outputStream.write(buffer, 0, len);
						outputStream.flush();
					}
					in.close();
				}
			} else {
				statusCode = 404;
				statusMessage = "Not Found";
				String content = "<html><head><title>Matrix</title></head><body><p>Page Not Found!</p></body></html>";
				String responseReport = "HTTP/1.1 404 Not Found\r\nDate: " + new Date() + "\r\nServer: " + Configuration.DISPLAY_SERVER_NAME + "\r\nContent-Type: text/html;charset=UTF-8\r\n\r\n" + content;
				outputStream.write(responseReport.getBytes());
			}
			outputStream.flush();
			outputStream.close();
			
			logger.info(method + " " + url + " " + statusCode + " " + statusMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Receiver(Socket socket) {
		this.socket = socket;
	}
}
