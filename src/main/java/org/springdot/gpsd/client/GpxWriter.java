package org.springdot.gpsd.client;

import java.io.PrintWriter;

import org.springdot.gpsd.client.msg.TPV;

public class GpxWriter implements MessageListener<TPV>{
	
	private PrintWriter out;
	
	private writeHeader(){
        out.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
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

	@Override
	public void handle(TPV msg) {
	}
}
