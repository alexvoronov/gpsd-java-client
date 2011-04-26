package org.springdot.gpsd.client;

import java.net.InetAddress;

public class Main {

	public static void main(String[] args) throws Exception {
		final GpsdClient gc = new GpsdClient(InetAddress.getByName("127.0.0.1"));
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				gc.close();
			}
		});
		gc.connect();
	}
}
