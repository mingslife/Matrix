package com.mingslife.matrix.entity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class RequestReport {
	private BufferedReader reader;
	private String version;
	private String url;
	private String method;
	private Map<String, String> headers = new HashMap<String, String>();

	public BufferedReader getReader() {
		return reader;
	}

	public void setReader(BufferedReader reader) {
		this.reader = reader;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public String getHeader(String header) {
		return headers.get(header);
	}

	public void setHeader(String header, String value) {
		headers.put(header, value);
	}

	public RequestReport(InputStream inputStream) {
		this.reader = new BufferedReader(new InputStreamReader(inputStream));
		try {
			StringBuilder stringBuilder = new StringBuilder();
			String temp = reader.readLine();
			String[] firstLine = temp.split(" ");
			this.method = firstLine[0];
			this.url = firstLine[1];
			this.version = firstLine[2];
			stringBuilder.append(temp + "\r\n");
			while (!(temp = reader.readLine()).equals("")) {
				int index;
				if ((index = temp.indexOf(':')) != -1) {
					String header = temp.substring(0, index);
					String value = temp.substring(index + 1).trim();
					headers.put(header, value);
				}
				stringBuilder.append(temp + "\r\n");
			}
			String requestReport = stringBuilder.toString();
//			System.out.println(requestReport);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
