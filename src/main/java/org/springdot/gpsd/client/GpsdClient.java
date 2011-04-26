package org.springdot.gpsd.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.Socket;

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
	
	private class NmeaModeDeserializer implements JsonDeserializer<NmeaMode>{
		@Override
		public NmeaMode deserialize(JsonElement lmt, Type typ, JsonDeserializationContext ctx) throws JsonParseException {
			return NmeaMode.values()[lmt.getAsInt()];
		}
	}
	
	private class Reader implements Runnable{
		@Override
		public void run() {
			GsonBuilder gbuilder = new GsonBuilder();
			gbuilder.registerTypeAdapter(NmeaMode.class,new NmeaModeDeserializer());
			Gson gson = gbuilder.create();
			
			while (rdr != null){
				String s;
				try {
					s = rdr.readLine();
					if (s == null) break;
					System.out.println(s);
					if (s.startsWith("{\"class\":\"TPV\"")){
						TPV tpv = gson.fromJson(s,TPV.class);
						System.out.println("tpv="+tpv);
					}
				} catch (IOException e) {
					e.printStackTrace();
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
	
	private void send(String s) throws IOException{
		wrt.write(s+"\n");
		wrt.flush();
	}
}
