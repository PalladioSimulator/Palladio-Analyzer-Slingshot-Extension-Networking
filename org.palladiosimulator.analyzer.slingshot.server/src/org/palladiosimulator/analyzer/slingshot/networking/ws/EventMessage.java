package org.palladiosimulator.analyzer.slingshot.networking.ws;

import java.util.UUID;

import org.palladiosimulator.analyzer.slingshot.common.events.SystemEvent;


/**
 * Superclass of all messages that are dispatchable as event
 *
 * @param <T> Payload type
 */
public abstract class EventMessage<T> extends Message<T> implements SystemEvent {
	private final UUID id = UUID.randomUUID();
	
	public EventMessage(String event, T payload) {
		super(event, payload, "Explorer");
	}
	
	public EventMessage(String event, T payload, String creator) {
		super(event, payload, creator);
	}

	@Override
	public String getId() {
		return id.toString();
	}

	@Override
	public String getName() {
		return getEvent();
	}

}
