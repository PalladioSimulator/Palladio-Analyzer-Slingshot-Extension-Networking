package org.palladiosimulator.analyzer.slingshot.networking.util;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.palladiosimulator.analyzer.slingshot.networking.data.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

@Singleton
public class GsonProvider {
	private final Map<Type, Object> gsonAdapters;
	private final Map<String, Class<? extends Message<?>>> messageTypes;
	private final Gson gson;
	
	@Inject
	public GsonProvider(final Map<Type, Object> gsonAdapters, final Map<String, Class<? extends Message<?>>> messageTypes) {
		this.gsonAdapters = gsonAdapters;
		this.messageTypes = messageTypes;
		this.gson = createGson();
	}
	
	public synchronized Gson getGson() {
		return gson;
	}
	
    private Gson createGson() {
        final GsonBuilder builder = new GsonBuilder();
		
        // Register Type Adapters
		this.gsonAdapters.forEach((type, adapter) -> {
			(adapter instanceof final List<?> adapterList ? adapterList : List.of(adapter)).forEach(a -> builder.registerTypeAdapter(type, a));
		});

		
		// Register Adapter that maps messages to the correct type
		builder.registerTypeAdapter(Message.class, new JsonDeserializer<Message<?>>() {
			@Override
			public Message<?> deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
					throws JsonParseException {
				if(json.isJsonObject()) {
					final var event = json.getAsJsonObject().get("event");
					if(event != null) {
						final var eventString = event.getAsString();
						if(messageTypes.containsKey(eventString)) {
							return context.deserialize(json, messageTypes.get(eventString));
						} else {
							throw new RuntimeException("Invalid message type: " + event);
						}
					}
				}
				throw new RuntimeException("Failed to parse message: " + json);
			}
			
		});
		
        return builder.create();
    }
}
