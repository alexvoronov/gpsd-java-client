package org.springdot.gpsd.client.msg;

public class TPV {
	String clazz;
	String tag;
	String device;
	double time;
	double ept;
	double lat;
	double lon;
	double alt;
	double track;
	double speed;
	double climb;
	NmeaMode mode;
	
	@Override
	public String toString() {
		return "class="+clazz
			 + " tag="+tag
			 + " device="+device
			 + " time="+time
			 + " ept="+ept
			 + " lat="+lat
			 + " lon="+lon
			 + " alt="+alt
			 + " track="+track
			 + " speed="+speed
			 + " climb="+climb
			 + " mode="+mode
			 ;
	}
}
