package org.palladiosimulator.analyzer.slingshot.networking.util;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.palladiosimulator.analyzer.slingshot.core.extension.SystemBehaviorExtension;
import org.palladiosimulator.analyzer.slingshot.eventdriver.annotations.Subscribe;
import org.palladiosimulator.analyzer.slingshot.eventdriver.annotations.eventcontract.OnEvent;
import org.palladiosimulator.analyzer.slingshot.networking.SlingshotWebsocketClient;
import org.palladiosimulator.analyzer.slingshot.networking.data.EventMessage;
import org.palladiosimulator.analyzer.slingshot.networking.data.GreetingMessage;
import org.palladiosimulator.analyzer.slingshot.networking.data.NetworkingConstants;

@OnEvent(when = EventMessage.class)
public class EventMessageDispatcher implements SystemBehaviorExtension {
	private static final Logger LOGGER = Logger.getLogger(SlingshotWebsocketClient.class.getName());

	private final SlingshotWebsocketClient client;

	private final String clientName;

	@Inject
	public EventMessageDispatcher(final SlingshotWebsocketClient client,
			@Named(NetworkingConstants.CLIENT_NAME) final String clientName) {
		this.client = client;
		this.clientName = clientName;
		client.sendMessage(new GreetingMessage(clientName));
	}

	@Subscribe
	public void onEventMessage(final EventMessage<?> message) {
		if (message.getCreator() == clientName) {
			client.sendMessage(message);
		} else {
			LOGGER.info(String.format("No dispatch because message creator is %s, but this client is %s.",
					message.getCreator(), this.clientName));
		}
	}
}
