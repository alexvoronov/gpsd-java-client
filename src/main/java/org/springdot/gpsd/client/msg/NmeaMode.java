package org.springdot.gpsd.client.msg;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public enum NmeaMode {
	NO_VALUE_YET, NO_FIX, TWO_D, THREE_D;
	
	public static class Deserializer implements JsonDeserializer<NmeaMode>{
		@Override
		public NmeaMode deserialize(JsonElement lmt, Type typ, JsonDeserializationContext ctx) throws JsonParseException {
			return NmeaMode.values()[lmt.getAsInt()];
		}
	}
}
