package org.springdot.gpsd.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class GpsdClient {
	private int port = 2947;
	private Socket sock;
	private BufferedReader rdr;
	private BufferedWriter wrt;
	
	public void connect() throws Exception{
		InetAddress host = InetAddress.getByName("127.0.0.1");
		System.out.println("host="+host);
		sock = new Socket(host,port);
		rdr = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		wrt = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		
		wrt.write("w+r-\n");
		wrt.flush();
		
		System.out.println("flushed");
		
		Thread th = new Thread(new Runnable(){
			@Override
			public void run() {
				while (rdr != null){
					String s;
					try {
						s = rdr.readLine();
						if (s == null) break;
						System.out.println(s);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		th.start();
		
		Thread.sleep(500);
		System.out.println("sending watch");
		wrt.write("?WATCH={\"nmea\":true};\n");
		wrt.flush();
	}

	public static void main(String[] args) throws Exception {
		System.out.println("hello world");
		new GpsdClient().connect();
	}
}
