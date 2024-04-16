package org.palladiosimulator.analyzer.slingshot.networking.events;

public abstract class Message<T> {
    private final String event;
    private final T payload;
    private final String creator;

    public Message(String event, T payload, String creator) {
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

