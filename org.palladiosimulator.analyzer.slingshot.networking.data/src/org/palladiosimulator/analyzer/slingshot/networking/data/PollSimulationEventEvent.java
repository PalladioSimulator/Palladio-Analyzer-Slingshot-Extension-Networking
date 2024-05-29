package org.palladiosimulator.analyzer.slingshot.networking.data;

import java.util.UUID;

import org.palladiosimulator.analyzer.slingshot.common.events.DESEvent;

public class PollSimulationEventEvent implements DESEvent {
	private String eventId = UUID.randomUUID().toString();
	private double time = 0;
	private final double delayValue;
	
	public PollSimulationEventEvent(double delay) {
		this.delayValue = delay;
	}
	
	
	public PollSimulationEventEvent(PollSimulationEventEvent clonee) {
		this.delayValue = clonee.delayValue;
	}


	@Override
	public String getId() {
		return eventId;
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public double time() {
		return time;
	}

	@Override
	public void setTime(double time) {
		this.time = time;
	}

	@Override
	public double delay() {
		return delayValue;
	}

}
