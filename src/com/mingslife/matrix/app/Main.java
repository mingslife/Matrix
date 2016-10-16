package com.mingslife.matrix.app;

import java.net.ServerSocket;
import java.net.Socket;

import com.mingslife.matrix.core.Logger;
import com.mingslife.matrix.core.Receiver;

public class Main {
	private static Logger logger = new Logger(Main.class);

	private static int SERVER_PORT = 3000;

	public void start() throws Exception {
		ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
		logger.info("Server is started on " + SERVER_PORT + ".");
		while (true) {
			Socket socket = serverSocket.accept();
			Receiver receiver = new Receiver(socket);
			new Thread(receiver).start();
		}
	}

	public void stop() {}

	public static void main(String[] args) {
		try {
			new Main().start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
