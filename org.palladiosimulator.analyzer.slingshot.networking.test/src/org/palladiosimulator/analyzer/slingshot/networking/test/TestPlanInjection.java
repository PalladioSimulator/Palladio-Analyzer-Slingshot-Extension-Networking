package org.palladiosimulator.analyzer.slingshot.networking.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.inject.Inject;
import javax.inject.Named;

import org.palladiosimulator.analyzer.slingshot.core.Slingshot;
import org.palladiosimulator.analyzer.slingshot.core.extension.SystemBehaviorExtension;
import org.palladiosimulator.analyzer.slingshot.eventdriver.annotations.Subscribe;
import org.palladiosimulator.analyzer.slingshot.eventdriver.annotations.eventcontract.OnEvent;
import org.palladiosimulator.analyzer.slingshot.networking.data.NetworkingConstants;
import org.palladiosimulator.analyzer.slingshot.networking.test.messages.ManagedSystemTimeEventMessage;
import org.palladiosimulator.analyzer.slingshot.networking.test.messages.PlanCreatedEventMessage;
import org.palladiosimulator.analyzer.slingshot.networking.test.messages.StateExploredEventMessage;
import org.palladiosimulator.analyzer.slingshot.networking.test.messages.StateGraphNode;
import org.palladiosimulator.spd.ScalingPolicy;
import org.palladiosimulator.spd.SpdFactory;

@OnEvent(when = ManagedSystemTimeEventMessage.class)
public class TestPlanInjection implements SystemBehaviorExtension {

    private final String clientName;

    private final String ID_outAt55 = "_7VIU0MAgEe6AcKIPVXJsSw";
    private final String ID_triggeroften = "_ChzohdxdEe6N3Y2J2Emr6g";
    private final String ID_outAt10 = "_DzRl1cAhEe6AcKIPVXJsSw";

    private final ScalingPolicy policy_outAt10;

    private Queue<PlanCreatedEventMessage> messages;

    @Inject
    public TestPlanInjection(@Named(NetworkingConstants.CLIENT_NAME) final String clientName) {
        this.clientName = clientName;

        policy_outAt10 = SpdFactory.eINSTANCE.createScalingPolicy();
        policy_outAt10.setId(ID_outAt10);
    }

    /**
     * 
     * @return non-empty queue of events.
     */
    public Queue<PlanCreatedEventMessage> initMessages() {

        List<String> stateIds = List.of("s1", "s2", "s3", "s4", "s5", "t4", "t5");
        List<Double> stateTimes = List.of(1d, 2., 3., 4., 5., 4., 5.);

        ScalingPolicy[] policyIdsArray = { null, null, policy_outAt10, policy_outAt10, null, null, policy_outAt10 };
        List<ScalingPolicy> policyIds = Arrays.asList(policyIdsArray);

        List<StateGraphNode> states = new ArrayList<>();

        for (int i = 0; i < stateIds.size(); i++) {
            states.add(new StateGraphNode(stateIds.get(i), stateTimes.get(i), policyIds.get(i)));
        }

        for (StateGraphNode stateGraphNode : states) {
            Slingshot.getInstance()
                .getSystemDriver()
                .postEvent(new StateExploredEventMessage(stateGraphNode, clientName));
        }

        List<String> plan1 = List.of("s1", "s2", "s3");
        List<String> plan2 = List.of("s1", "s2", "s3", "s4", "s5");
        List<String> plan3 = List.of("s1", "s2", "s3", "t4", "t5");

        Queue<PlanCreatedEventMessage> messages = new LinkedList<>();
        
        messages.offer(new PlanCreatedEventMessage(plan1, clientName));
        messages.offer(new PlanCreatedEventMessage(plan2, clientName));
        messages.offer(new PlanCreatedEventMessage(plan3, clientName));
        
        return messages;
    }

    @Subscribe
    public void onManagedSystemTimeEventMessage(final ManagedSystemTimeEventMessage event) {
        if(this.messages == null) {
            this.messages = this.initMessages();
        }
        
        if (!this.messages.isEmpty()) {
            Slingshot.getInstance()
                .getSystemDriver()
                .postEvent(this.messages.poll());
        }
    }

}
