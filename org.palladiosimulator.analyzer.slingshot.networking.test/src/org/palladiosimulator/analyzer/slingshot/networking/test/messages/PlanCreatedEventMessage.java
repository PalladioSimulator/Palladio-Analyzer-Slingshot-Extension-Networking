package org.palladiosimulator.analyzer.slingshot.networking.test.messages;

import java.util.List;

import org.palladiosimulator.analyzer.slingshot.networking.data.EventMessage;

/**
 *
 * @author Sarah Stieß
 *
 */
public class PlanCreatedEventMessage extends EventMessage<List<String>> {

    public static final String MESSAGE_MAPPING_IDENTIFIER = "PlanCreated";

    public PlanCreatedEventMessage(final List<String> payload, final String creator) {
        super(MESSAGE_MAPPING_IDENTIFIER, payload, creator);
	}

}

