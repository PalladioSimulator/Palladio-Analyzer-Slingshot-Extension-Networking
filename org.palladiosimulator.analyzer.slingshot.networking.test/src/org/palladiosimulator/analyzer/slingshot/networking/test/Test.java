package org.palladiosimulator.analyzer.slingshot.networking.test;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.palladiosimulator.analyzer.slingshot.core.Slingshot;
import org.palladiosimulator.analyzer.slingshot.core.events.SimulationStarted;
import org.palladiosimulator.analyzer.slingshot.core.extension.SimulationBehaviorExtension;
import org.palladiosimulator.analyzer.slingshot.eventdriver.annotations.Subscribe;
import org.palladiosimulator.analyzer.slingshot.eventdriver.annotations.eventcontract.OnEvent;
import org.palladiosimulator.analyzer.slingshot.networking.data.NetworkingConstants;
import org.palladiosimulator.analyzer.slingshot.networking.test.injection.ApplyPolicyEventMessage;
import org.palladiosimulator.spd.ScalingPolicy;
import org.palladiosimulator.spd.SpdFactory;

@OnEvent(when = SimulationStarted.class)
public class Test implements SimulationBehaviorExtension {

	private final String clientName;

	@Inject
	public Test(@Named(NetworkingConstants.CLIENT_NAME) final String clientName) {
		this.clientName = clientName;
	}

	@Subscribe
	public void onEventMessage(final SimulationStarted message) {

		ScalingPolicy policy = SpdFactory.eINSTANCE.createScalingPolicy();
		policy.setEntityName("policy1");
		policy.setId("_7VIU0MAgEe6AcKIPVXJsSw");

		ScalingPolicy policy2 = SpdFactory.eINSTANCE.createScalingPolicy();
		policy2.setEntityName("policy2");
		policy2.setId("_7VIU0MAgEe6AcKIPVXJsSw");

		Map<Double, Set<ScalingPolicy>> map = Map.of(1.0, Set.of(policy), 3.5, Set.of(policy, policy2));

		Slingshot.getInstance().getSystemDriver()
				.postEvent(new ApplyPolicyEventMessage(new ApplyPolicyEventMessage.Plan(map), clientName));
	}

}
