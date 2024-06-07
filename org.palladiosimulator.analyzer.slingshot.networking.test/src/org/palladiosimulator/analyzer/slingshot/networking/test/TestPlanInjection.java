package org.palladiosimulator.analyzer.slingshot.networking.test;

import java.util.ArrayList;
import java.util.List;
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
import org.palladiosimulator.analyzer.slingshot.networking.test.messages.PlanCreatedEventMessage;
import org.palladiosimulator.analyzer.slingshot.networking.test.messages.StateExploredEventMessage;
import org.palladiosimulator.analyzer.slingshot.networking.test.messages.StateGraphNode;
import org.palladiosimulator.spd.ScalingPolicy;
import org.palladiosimulator.spd.SpdFactory;
import org.palladiosimulator.spd.SpdPackage;

@OnEvent(when = SimulationStarted.class)
public class TestPlanInjection implements SimulationBehaviorExtension {

	private final String clientName;
	
	private final String ID_outAt55 = "_7VIU0MAgEe6AcKIPVXJsSw";
	private final String ID_triggeroften = "_ChzohdxdEe6N3Y2J2Emr6g";
	private final String ID_outAt10= "_DzRl1cAhEe6AcKIPVXJsSw";
	
	
	private final ScalingPolicy policy_outAt10;

	@Inject
	public TestPlanInjection(@Named(NetworkingConstants.CLIENT_NAME) final String clientName) {
		this.clientName = clientName;
		
		policy_outAt10 = SpdFactory.eINSTANCE.createScalingPolicy();
		policy_outAt10.setId(ID_outAt10);
	
	}

	@Subscribe
	public void onEventMessage(final SimulationStarted message) {
		
		// sending an receiving at Injector works. 
		
		
		List<String> stateIds = List.of("s1", "s2", "s3", "s4", "s5", "t4", "t5");
		ScalingPolicy[] policyIds = {null, null, policy_outAt10, policy_outAt10, null, null, policy_outAt10};
		List<Double> stateTimes = List.of(1d, 2., 3., 4., 5., 4., 5.);

		
		
		List<StateGraphNode> states = new ArrayList<>();
		
		
		for (int i = 0; i < stateIds.size(); i++) {
			states.add(new StateGraphNode(stateIds.get(i), stateTimes.get(i), policyIds[i]));
		}

		for (StateGraphNode stateGraphNode : states) {
			Slingshot.getInstance().getSystemDriver()
					.postEvent(new StateExploredEventMessage(stateGraphNode, clientName));
		}
		

		
		List<String> plan1 = List.of("s1", "s2");
		List<String> plan2 = List.of("s1", "s2", "s3", "s4", "s5");
		List<String> plan3 = List.of("s1", "s2", "s3", "t4", "t5");
		
		
		Slingshot.getInstance().getSystemDriver()
		.postEvent(new PlanCreatedEventMessage(plan1, clientName));
		Slingshot.getInstance().getSystemDriver()
		.postEvent(new PlanCreatedEventMessage(plan2, clientName));
		Slingshot.getInstance().getSystemDriver()
		.postEvent(new PlanCreatedEventMessage(plan3, clientName));

	}

}
