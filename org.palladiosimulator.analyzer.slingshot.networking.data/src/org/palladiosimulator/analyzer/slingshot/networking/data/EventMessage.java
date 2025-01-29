package org.palladiosimulator.analyzer.slingshot.networking.data;

import java.util.UUID;

import org.palladiosimulator.analyzer.slingshot.common.events.SystemEvent;


/**
 * Superclass of all messages that are dispatchable as event
 *
 * @param <T> Payload type
 */
public abstract class EventMessage<T> extends Message<T> implements SystemEvent {
    /*
     * maybe overwritten manually when starting the managed system via experiment automation. preset
     * for state exploration.
     */
	public static UUID CLIENT_ID = UUID.randomUUID();

    /*
     * will be overwritten manually when starting a state exploration. preset for managed system.
     */
	public static UUID EXPLORATION_ID = UUID.randomUUID();
	private final UUID id = UUID.randomUUID();
	// Copy as non static to allow serialization
	private final UUID clientId;
	private final UUID explorationId;


	public EventMessage(final String event, final T payload) {
		super(event, payload, "Explorer");
		this.clientId = CLIENT_ID;
		this.explorationId = EXPLORATION_ID;
	}

	public EventMessage(final String event, final T payload, final String creator) {
		super(event, payload, creator);
		this.clientId = CLIENT_ID;
		this.explorationId = EXPLORATION_ID;
	}

	@Override
	public String getId() {
		return id.toString();
	}

	@Override
	public String getName() {
		return getEvent();
	}

    public UUID getExplorationId() {
        return this.explorationId;
    }
}
