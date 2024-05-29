package org.palladiosimulator.analyzer.slingshot.networking.data;

import java.util.UUID;

import org.palladiosimulator.analyzer.slingshot.common.events.DESEvent;

public abstract class SimulationEventMessage<T> extends Message<T> implements DESEvent {
	private final String eventId = UUID.randomUUID().toString();
	private double time;
	
	
	public SimulationEventMessage(final String event, final T payload) {
		super(event, payload, "Explorer");
	}
	
	public SimulationEventMessage(final String event, final T payload, final String creator) {
		super(event, payload, creator);
	}

	@Override
	public final String getId() {
		return this.eventId;
	}

	@Override
	public final String getName() {
		return this.getEvent();
	}

	@Override
	public double time() {
		return this.time;
	}

	@Override
	public void setTime(final double time) {
		this.time = time;
		
	}

	@Override
	public final double delay() {
		return 0;
	}

}
