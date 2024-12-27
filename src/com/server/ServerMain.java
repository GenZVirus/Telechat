package com.server;

public class ServerMain {
	
	private int port;
	private Server server;
	
	public ServerMain(int port) {
		this.port = port;
		server = new Server(8192);
	}
	
	public static void main(String[] args) {
		int port;
		if(args.length != 1) {
			System.out.println("Usage: java -jar ServerChat.jar [port]");
			return;
		}
		port = Integer.parseInt(args[0]);
		new ServerMain(port);
	}

}
