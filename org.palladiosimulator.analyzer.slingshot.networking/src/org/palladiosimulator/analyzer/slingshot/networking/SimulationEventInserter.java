package org.palladiosimulator.analyzer.slingshot.networking;

import javax.inject.Inject;

import org.palladiosimulator.analyzer.slingshot.core.events.SimulationStarted;
import org.palladiosimulator.analyzer.slingshot.core.extension.SimulationBehaviorExtension;
import org.palladiosimulator.analyzer.slingshot.eventdriver.annotations.Subscribe;
import org.palladiosimulator.analyzer.slingshot.eventdriver.annotations.eventcontract.OnEvent;
import org.palladiosimulator.analyzer.slingshot.eventdriver.returntypes.Result;
import org.palladiosimulator.analyzer.slingshot.networking.events.PollSimulationEventEvent;
import org.palladiosimulator.analyzer.slingshot.networking.util.SimulationEventBuffer;


@OnEvent(when = PollSimulationEventEvent.class)
@OnEvent(when = SimulationStarted.class)
public class SimulationEventInserter implements SimulationBehaviorExtension {
	private static final double DELAY = 1; // every 1000ms 
	@Inject
	private SimulationEventBuffer simulationEventBuffer;
	
	@Subscribe
	public Result<?> onSimulationStartedEvent(SimulationStarted sim) {
		System.out.println("sim started");
		return Result.of(new PollSimulationEventEvent(DELAY));
	}
	
	@Subscribe
	public Result<?> onPollSimulationEvent(PollSimulationEventEvent sim) {
		// TODO discuss: should we always only poll a single event or all available?
		System.out.println("Polling at " + sim.time());
		var event = simulationEventBuffer.poll();
		if(event == null) {
			return Result.of(new PollSimulationEventEvent(DELAY));
		}
		return Result.of(event, new PollSimulationEventEvent(DELAY));
		
	}
}
