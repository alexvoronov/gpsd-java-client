package org.springdot.gpsd.client;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springdot.gpsd.client.msg.NmeaMode;
import org.springdot.gpsd.client.msg.SKY;
import org.springdot.gpsd.client.msg.TPV;

public class GpxWriter implements MessageListener{
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	private PrintWriter out = new PrintWriter(System.out);
	private boolean trkOpen;
	private SKY sky;
	
	private void wr(String s){
		out.print(s);
		out.flush();
	}
	
	private void beginFile(){
        wr("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
          +"<gpx xmlns=\"http://www.topografix.com/GPX/1.1\"\n"
          +"     xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
          +"     xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 http://www.garmin.com/xmlschemas/GpxExtensionsv3.xsd http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd\">\n"
          +"     creator=\"gpsd-java-client\"\n"
          +"     version=\"1.1\">\n"
          +"  <metadata>\n"
          +"    <name>gpsd-java-client</name>\n"
          +"    <author>Max Spring</author>\n"
          +"  </metadata>\n");
	}
	
	private void endFile(){
        if (trkOpen) endTrk();
        wr("</gpx>\n");
	}

	private void beginTrk(){
        wr("  <trk>\n"
          +"    <trkseg>\n");
	}

	private void endTrk(){
        wr("    </trkseg>\n"
          +"  </trk>\n");
	}
	
	private void wrXdop(String tag, double v){
		if (!Double.isNaN(v)){
			out.printf("        <"+tag+">%.1f</"+tag+">\n",v);
		}
	}
	
	@Override
	public void handle(SKY msg) {
		sky = msg;
	}
	
	@Override
	public void handle(TPV msg) {
		out.print("      <trkpt lat=\""+msg.getLat()+"\" lon=\""+msg.getLon()+"\">\n");
		if (!Double.isNaN(msg.getAlt())) out.print("        <ele>"+msg.getAlt()+"</ele>\n");

		out.print("        <time>"+TIME_FORMAT.format(new Date((long)msg.getTime()))+"</time>\n");
		
		NmeaMode mode = msg.getMode();
		if (mode != null) out.print("        <fix>"+mode.getGpxStr()+"</fix>\n");
		if (sky != null){
			int nofSatellites = sky.getNofSatellites();
			if (mode != NmeaMode.NO_FIX && nofSatellites > 0) out.print("        <sat>"+nofSatellites+"</sat>\n");
			wrXdop("hdop",sky.getHdop());
			wrXdop("vdop",sky.getVdop());
			wrXdop("pdop",sky.getPdop());
		}
		out.print("      </trkpt>\n");
		out.flush();
	}
}
