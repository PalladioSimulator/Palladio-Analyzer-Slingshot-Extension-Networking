package org.palladiosimulator.analyzer.slingshot.networking.test.injection;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.inject.Named;

import org.palladiosimulator.analyzer.slingshot.networking.data.EventMessage;
import org.palladiosimulator.analyzer.slingshot.networking.data.NetworkingConstants;
import org.palladiosimulator.spd.ScalingPolicy;

import com.google.inject.Inject;

/**
 * 
 * @author Sarah Stieß
 *
 */
public class ApplyPolicyEventMessage extends EventMessage<org.palladiosimulator.analyzer.slingshot.networking.test.injection.ApplyPolicyEventMessage.Plan> {
	
	public static final String MESSAGE_MAPPING_IDENTIFIER = ApplyPolicyEventMessage.class.getSimpleName();	
	
    public ApplyPolicyEventMessage(final Plan payload, final String creator) {
		super(MESSAGE_MAPPING_IDENTIFIER, payload, creator);
	}

    public static class Plan {

        private final String id;
        private final Map<Double, Set<ScalingPolicy>> planSteps;

        public Plan(final Map<Double, Set<ScalingPolicy>> planSteps) {
            this.id = UUID.randomUUID()
                .toString();

            this.planSteps = planSteps;
        }
   }
}

