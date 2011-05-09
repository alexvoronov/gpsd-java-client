package org.springdot.gpsd.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springdot.gpsd.client.msg.NmeaMessage;
import org.springdot.gpsd.client.msg.NmeaMode;
import org.springdot.gpsd.client.msg.TPV;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class GpsdClient {
	public final static int DEFAULT_PORT = 2947;
	
	private InetAddress host;
	private int port;
	private Socket sock;
	private BufferedReader rdr;
	private BufferedWriter wrt;
	
	private Map<Class<? extends NmeaMessage>,Set<MessageListener>> msgListeners = new HashMap<Class<? extends NmeaMessage>,Set<MessageListener>>();
	
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
//					System.out.println(s);
					handleLine(s);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	 	
	 	private void handleLine(String line){
			Class<? extends NmeaMessage> type;
	 		
	 		if (line.startsWith("{\"class\":\"TPV\"")){
				type = TPV.class;
			}else{
				return;
			}
			
 			NmeaMessage msg = gson.fromJson(line,type);
			Set<MessageListener> listeners = msgListeners.get(type);
			if (listeners != null){
				for (MessageListener listener : listeners){
					listener.handle(msg);
				}
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
	
	public void register(Class<? extends NmeaMessage> type, MessageListener listener){
		Set<MessageListener> listeners = msgListeners.get(type);
		if (listeners == null){
			listeners = new HashSet<MessageListener>();
			msgListeners.put(type,listeners);
		}
		listeners.add(listener);
	}
	
	private void send(String s) throws IOException{
		wrt.write(s+"\n");
		wrt.flush();
	}
}
