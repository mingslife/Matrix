package com.mingslife.matrix.core;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	private Class<?> c;

	public void info(String message) {
		String output = "[INFO][" + getCurrentDate() + "][" + c.getName() + "] " + message;
		System.out.println(output);
	}

	public void warn(String message) {
		String output = "[WARN][" + getCurrentDate() + "][" + c.getName() + "] " + message;
		System.out.println(output);
	}

	public void error(String message) {
		String output = "[ERROR][" + getCurrentDate() + "][" + c.getName() + "] " + message;
		System.out.println(output);
	}

	public void debug(String message) {
		String output = "[DEBUG][" + getCurrentDate() + "][" + c.getName() + "] " + message;
		System.out.println(output);
	}

	private static String getCurrentDate() {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(date);
	}

	public Logger(Class<?> c) {
		this.c = c;
	}
}
