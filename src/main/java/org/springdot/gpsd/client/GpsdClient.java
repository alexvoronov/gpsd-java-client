package org.springdot.gpsd.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import org.springdot.gpsd.client.msg.NmeaMode;
import org.springdot.gpsd.client.msg.SKY;
import org.springdot.gpsd.client.msg.TPV;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GpsdClient {
	public final static int DEFAULT_PORT = 2947;
	
	private InetAddress host;
	private int port;
	private Socket sock;
	private BufferedReader rdr;
	private BufferedWriter wrt;
	
	private Set<MessageListener> msgListeners = new HashSet<MessageListener>();
	
	private class Reader implements Runnable{
	 	private Gson gson;
	 	
		Reader(){
			GsonBuilder gbuilder = new GsonBuilder();
			gbuilder.registerTypeAdapter(NmeaMode.class,new NmeaMode.Deserializer());
			gson = gbuilder.create();
		}
		
		@Override
		public void run() {
			while (rdr != null){
				String s;
				try {
					s = rdr.readLine();
					if (s == null) break;
					System.out.println(s);
					handleLine(s);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	 	
	 	private void handleLine(String line){
	 		if (line.startsWith("{\"class\":\"TPV\"")){
	 			TPV msg = gson.fromJson(line,TPV.class);
				for (MessageListener listener : msgListeners){
					listener.handle(msg);
				}
	 		}else if (line.startsWith("{\"class\":\"SKY\"")){
	 			SKY msg = gson.fromJson(line,SKY.class);
				for (MessageListener listener : msgListeners){
					listener.handle(msg);
				}
	 		}else{
				System.out.println(line);
				return;
			}
	 	}
	}
	
	public GpsdClient(InetAddress host) {
		this(host,DEFAULT_PORT);
	}

	public GpsdClient(InetAddress host, int port) {
		this.host = host;
		this.port = port;
	}

	public void connect(){
		try {
			sock = new Socket(host,port);
			rdr = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			wrt = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			
			send("w+r-");
			
			Thread th = new Thread(new Reader());
			th.start();
			
			Thread.sleep(500);
			System.out.println("sending watch");
			send("?WATCH={\"enable\":true,\"json\":true};");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void close(){
		System.out.println("closing GpdsClient");
		try {
			rdr.close();
			wrt.close();
			sock.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void register(MessageListener listener){
		msgListeners.add(listener);
	}
	
	private void send(String s) throws IOException{
		wrt.write(s+"\n");
		wrt.flush();
	}
}
