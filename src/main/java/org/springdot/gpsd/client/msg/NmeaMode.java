package org.springdot.gpsd.client.msg;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public enum NmeaMode {
	NO_VALUE_YET("no-value-yet"), NO_FIX("none"), TWO_D("2d"), THREE_D("3d");
	
	private String gpxs;
	
	private NmeaMode(String gpxs){
		this.gpxs = gpxs;
	}
	
	public String getGpxStr(){
		return gpxs;
	}
	
	public static class Deserializer implements JsonDeserializer<NmeaMode>{
		@Override
		public NmeaMode deserialize(JsonElement lmt, Type typ, JsonDeserializationContext ctx) throws JsonParseException {
			return NmeaMode.values()[lmt.getAsInt()];
		}
	}
}
