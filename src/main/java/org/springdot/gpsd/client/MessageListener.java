package org.springdot.gpsd.client;

import org.springdot.gpsd.client.msg.SKY;
import org.springdot.gpsd.client.msg.TPV;

public interface MessageListener{
	void handle(SKY msg);
	void handle(TPV msg);
}
