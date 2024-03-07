package org.palladiosimulator.analyzer.slingshot.networking;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.inject.Singleton;

import org.palladiosimulator.analyzer.slingshot.common.events.DESEvent;

@Singleton
public class SimulationEventBuffer {
	private Queue<DESEvent> queue = new ConcurrentLinkedQueue<>();

	public void addMessage(DESEvent message) {
		queue.add(message);
	}
	
	public DESEvent poll() {
		return queue.poll();
	}
	
}
