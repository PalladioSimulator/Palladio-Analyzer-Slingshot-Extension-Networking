package org.palladiosimulator.analyzer.slingshot.stateexploration.explorer.networking.messages;

import org.palladiosimulator.analyzer.slingshot.networking.data.EventMessage;

/**
 * Proof of Concept
 */
public class TestMessage extends EventMessage<String> {

	public TestMessage(String payload) {
		super("Test", payload);
	}
}
