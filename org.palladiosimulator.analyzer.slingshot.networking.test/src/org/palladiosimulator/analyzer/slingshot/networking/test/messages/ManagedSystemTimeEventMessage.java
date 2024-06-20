package org.palladiosimulator.analyzer.slingshot.networking.test.messages;

import java.util.Set;

import org.palladiosimulator.analyzer.slingshot.networking.data.EventMessage;
import org.palladiosimulator.analyzer.slingshot.networking.test.messages.ManagedSystemTimeEventMessage.PlanStep;

/**
 *
 * @author Sarah Stieß
 *
 */
public class ManagedSystemTimeEventMessage extends EventMessage<PlanStep> {

    public static final String MESSAGE_MAPPING_IDENTIFIER = "PlanStepApplied";

    public ManagedSystemTimeEventMessage(final PlanStep payload, final String creator) {
        super(MESSAGE_MAPPING_IDENTIFIER, payload, creator);
    }

    public record PlanStep(Double pointInTime, Set<String> policies) {

    }
}

