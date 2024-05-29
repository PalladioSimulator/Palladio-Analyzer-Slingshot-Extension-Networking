package org.palladiosimulator.analyzer.slingshot.networking;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;

import org.palladiosimulator.analyzer.slingshot.core.extension.AbstractSlingshotExtension;
import org.palladiosimulator.analyzer.slingshot.networking.data.GreetingMessage;
import org.palladiosimulator.analyzer.slingshot.networking.data.Message;
import org.palladiosimulator.analyzer.slingshot.networking.data.SimulationEventBuffer;
import org.palladiosimulator.analyzer.slingshot.networking.util.EventMessageDispatcher;
import org.palladiosimulator.analyzer.slingshot.networking.util.GsonProvider;

import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;

public class NetworkingModule extends AbstractSlingshotExtension {

	@Override
	protected void configure() {

		MapBinder.newMapBinder(binder(), new TypeLiteral<String>() {}, new TypeLiteral<Class<? extends Message<?>>>() {})
		.addBinding(GreetingMessage.MESSAGE_MAPPING_IDENTIFIER).toInstance(GreetingMessage.class);
		
		MapBinder.newMapBinder(binder(), Type.class, Object.class);

		bind(GsonProvider.class);
		try {
			final var client = new SlingshotWebsocketClient(new URI("ws://localhost:9006"));
			bind(SlingshotWebsocketClient.class).toInstance(client);
			this.requestInjection(client);
		} catch (final URISyntaxException e) {
			e.printStackTrace();
		}
		install(EventMessageDispatcher.class);
		bind(SimulationEventBuffer.class);
		install(SimulationEventInserter.class);
	}

}
