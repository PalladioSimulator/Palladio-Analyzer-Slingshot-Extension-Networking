package org.palladiosimulator.analyzer.slingshot.networking.util;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.palladiosimulator.analyzer.slingshot.core.extension.SystemBehaviorExtension;
import org.palladiosimulator.analyzer.slingshot.eventdriver.annotations.Subscribe;
import org.palladiosimulator.analyzer.slingshot.eventdriver.annotations.eventcontract.OnEvent;
import org.palladiosimulator.analyzer.slingshot.networking.SlingshotWebsocketClient;
import org.palladiosimulator.analyzer.slingshot.networking.data.EventMessage;
import org.palladiosimulator.analyzer.slingshot.networking.data.NetworkingConstants;


@OnEvent(when = EventMessage.class)
public class EventMessageDispatcher implements SystemBehaviorExtension {
	private static final Logger LOGGER = Logger.getLogger(SlingshotWebsocketClient.class.getName());

	@Inject
	private SlingshotWebsocketClient client;

	@Inject
	@Named(NetworkingConstants.CLIENT_NAME)
	private String clientName;

	@Subscribe
	public void onEventMessage(final EventMessage<?> message) {
		if (message.getCreator() == clientName) {
			client.sendMessage(message);
		} else {
			LOGGER.info(String.format(
					"No dispatch because message creator is %s, but this client is %s.", message.getCreator(),
					this.clientName));
		}
	}
}
