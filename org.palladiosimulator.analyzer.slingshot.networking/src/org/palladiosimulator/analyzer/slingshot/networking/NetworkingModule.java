package org.palladiosimulator.analyzer.slingshot.networking;

import java.net.URI;
import java.net.URISyntaxException;

import org.palladiosimulator.analyzer.slingshot.core.extension.AbstractSlingshotExtension;
import org.palladiosimulator.analyzer.slingshot.networking.ws.EventMessageDispatcher;
import org.palladiosimulator.analyzer.slingshot.networking.ws.GsonProvider;
import org.palladiosimulator.analyzer.slingshot.networking.ws.SlingshotWebsocketClient;


public class NetworkingModule extends AbstractSlingshotExtension {

	@Override
	protected void configure() {
		bind(GsonProvider.class);
		try {
			var client = new SlingshotWebsocketClient(new URI("ws://localhost:9006"));
			bind(SlingshotWebsocketClient.class).toInstance(client);
			this.requestInjection(client);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		install(EventMessageDispatcher.class);
		bind(SimulationEventBuffer.class);
	}

}
