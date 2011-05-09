package org.springdot.gpsd.client;

import org.springdot.gpsd.client.msg.NmeaMessage;

public interface MessageListener <T extends NmeaMessage> {
	
	void handle(T msg);
}
