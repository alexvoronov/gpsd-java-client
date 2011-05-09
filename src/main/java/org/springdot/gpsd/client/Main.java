package org.springdot.gpsd.client;

import java.net.InetAddress;

import org.springdot.gpsd.client.msg.TPV;

public class Main {
	
	private static class TpvListener implements MessageListener<TPV>{
		@Override
		public void handle(TPV msg) {
			System.out.println("msg: "+msg);
		}
	}

	public static void main(String[] args) throws Exception {
		final GpsdClient gc = new GpsdClient(InetAddress.getByName("127.0.0.1"));
		gc.register(TPV.class,new TpvListener());
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				gc.close();
			}
		});
		gc.connect();
	}
}
