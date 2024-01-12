package org.palladiosimulator.analyzer.slingshot.networking.ws;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.palladiosimulator.analyzer.slingshot.core.extension.SystemBehaviorExtension;
import org.palladiosimulator.analyzer.slingshot.eventdriver.annotations.Subscribe;
import org.palladiosimulator.analyzer.slingshot.eventdriver.annotations.eventcontract.OnEvent;




@OnEvent(when = EventMessage.class)
public class EventMessageDispatcher implements SystemBehaviorExtension {
	private static final Logger LOGGER = Logger.getLogger(SlingshotWebsocketClient.class.getName());
	
	@Inject
	private SlingshotWebsocketClient client;
	
	@Subscribe
	public void onEventMessage(EventMessage<?> message) {
		if(message.getCreator() == "Explorer") {
			client.sendMessage(message);
		} else {
			System.out.println("No dispatch because not an explorer message: " + message);
			LOGGER.info("No dispatch because not an explorer message: " + message);
		}
	}
	
}
