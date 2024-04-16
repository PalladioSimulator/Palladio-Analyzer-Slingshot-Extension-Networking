package org.palladiosimulator.analyzer.slingshot.networking;

import java.net.URI;
import java.net.URISyntaxException;

import org.palladiosimulator.analyzer.slingshot.core.extension.AbstractSlingshotExtension;
import org.palladiosimulator.analyzer.slingshot.networking.util.EventMessageDispatcher;
import org.palladiosimulator.analyzer.slingshot.networking.util.GsonProvider;
import org.palladiosimulator.analyzer.slingshot.networking.util.SimulationEventBuffer;


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
		install(SimulationEventInserter.class);
	}

}
