package org.palladiosimulator.analyzer.slingshot.networking.util;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.palladiosimulator.analyzer.slingshot.core.extension.SystemBehaviorExtension;
import org.palladiosimulator.analyzer.slingshot.eventdriver.annotations.Subscribe;
import org.palladiosimulator.analyzer.slingshot.eventdriver.annotations.eventcontract.OnEvent;
import org.palladiosimulator.analyzer.slingshot.networking.SlingshotWebsocketClient;
import org.palladiosimulator.analyzer.slingshot.networking.data.EventMessage;




@OnEvent(when = EventMessage.class)
public class EventMessageDispatcher implements SystemBehaviorExtension {
	private static final Logger LOGGER = Logger.getLogger(SlingshotWebsocketClient.class.getName());

	@Inject
	private SlingshotWebsocketClient client; // make it nullable and only active, if connection is available.

	@Subscribe
	public void onEventMessage(final EventMessage<?> message) {
		if(message.getCreator() == "Explorer") {
			client.sendMessage(message);
		} else {
			System.out.println("No dispatch because not an explorer message: " + message);
			LOGGER.info("No dispatch because not an explorer message: " + message);
		}
	}

}
