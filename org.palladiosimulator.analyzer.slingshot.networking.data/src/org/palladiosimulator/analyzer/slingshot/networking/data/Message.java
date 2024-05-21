package org.palladiosimulator.analyzer.slingshot.networking.data;

public abstract class Message<T> {
	private final String event;
	private final T payload;
	private final String creator;

	public Message(final String event, final T payload, final String creator) {
		this.event = event;
		this.payload = payload;
		this.creator = creator;
	}

	public String getEvent() {
		return event;
	}

	public T getPayload() {
		return payload;
	}

	public String getCreator() {
		return creator;
	}
}

