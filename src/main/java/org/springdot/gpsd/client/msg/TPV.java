package org.springdot.gpsd.client.msg;

import com.google.gson.annotations.SerializedName;

public final class TPV{
	@SerializedName("class")
	private String clazz;
	private String tag;
	private String device;
	private double time;
	private double ept;
	private double lat;
	private double lon;
	private double alt;
	private double track;
	private double speed;
	private double climb;
	private NmeaMode mode;
	
	public String getClazz() {
		return clazz;
	}

	public String getTag() {
		return tag;
	}

	public String getDevice() {
		return device;
	}

	public double getTime() {
		return time;
	}

	public double getEpt() {
		return ept;
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	public double getAlt() {
		return alt;
	}

	public double getTrack() {
		return track;
	}

	public double getSpeed() {
		return speed;
	}

	public double getClimb() {
		return climb;
	}

	public NmeaMode getMode() {
		return mode;
	}

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
			 + " mode="+mode;
	}
}
