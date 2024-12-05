package org.palladiosimulator.analyzer.slingshot.networking;

import java.net.URI;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.palladiosimulator.analyzer.slingshot.common.events.SystemEvent;
import org.palladiosimulator.analyzer.slingshot.core.Slingshot;
import org.palladiosimulator.analyzer.slingshot.core.api.SimulationDriver;
import org.palladiosimulator.analyzer.slingshot.core.api.SystemDriver;
import org.palladiosimulator.analyzer.slingshot.networking.data.Message;
import org.palladiosimulator.analyzer.slingshot.networking.util.GsonProvider;


@Singleton
public class SlingshotWebsocketClient extends WebSocketClient {
	private static final Logger LOGGER = Logger.getLogger(SlingshotWebsocketClient.class.getName());
	private static final int RECONNECT_DELAY = 5000;
	private SystemDriver systemDriver;
	private SimulationDriver simulationDriver;
	private final ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<>();
	@Inject
	private GsonProvider gsonProvider;

	final Object lock = new Object();
	private final Thread reconnectionThread = new Thread(() -> {
		if(this.isOpen()) {
			synchronized(lock) {
				try {
					lock.wait();
				} catch (final InterruptedException e) {
					LOGGER.error("Reconnection thread interrupted", e);
				}
			}
		}
		while(true) {
			try {
				Thread.sleep(RECONNECT_DELAY);
				this.reconnectBlocking();
				if(this.isOpen()) {
					System.out.println("Connection Open. Waiting for disconnect...");
					LOGGER.trace("Connection Open. Waiting for disconnect...");
					synchronized(lock) {
						lock.wait();
					}
				} else {
					System.out.println("Reconnection failed. Retry after " + RECONNECT_DELAY + "ms");
					LOGGER.info("Reconnection failed. Retry after " + RECONNECT_DELAY + "ms");
				}
			} catch (final InterruptedException e) {
				LOGGER.error("Reconnection thread interrupted", e);
			}
		}
	});


	public SlingshotWebsocketClient(final URI serverUri) {
		super(serverUri);
		try {
			this.connectBlocking();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		} finally {
			this.reconnectionThread.start();
		}
	}

	@Override
	public void onClose(final int arg0, final String arg1, final boolean arg2) {
		System.out.println("Websocket Connection Closed :(");
		LOGGER.warn("Websocket Connection Closed :(");
		attemptReconnect();
	}

	@Override
	public void onError(final Exception e) {
		System.out.println("Server Error: " + e);
		LOGGER.error("Server Error: ", e);
		attemptReconnect();
	}

	@Override
	public void onMessage(final String arg) {
		System.out.println("MESSAGE: " + arg);
		try {
			final var message = this.gsonProvider.getGson().fromJson(arg, Message.class);
			System.out.println(message.getEvent());
			System.out.println(message.getClass());
			if(message instanceof final SystemEvent eventMessage) {
				getSystemDriver().postEvent(eventMessage);
			} else {
				System.out.println("Received Message could not be dispatched, as it is not a EventMessage (Or SystemEvent): " + message);
				LOGGER.warn("Received Message could not be dispatched, as it is not a EventMessage (Or SystemEvent): " + message);
			}
		} catch(final Throwable e) {
            System.out.println("Error on deserializing message. No event was dispatched.");
            e.printStackTrace();
            LOGGER.error("Error on deserializing message. No event was dispatched ", e);
		}
	}

	@Override
	public void onOpen(final ServerHandshake arg0) {
		System.out.println("Connection Open. Sending '" + messageQueue.size() + "' messages in queue");
		LOGGER.info("Connection Open. Sending '" + messageQueue.size() + "' messages in queue");
		flushMessageQueue();
	}

	public void sendMessage(final Message<?> message) {
		if (this.isOpen()) {
			this.send(this.gsonProvider.getGson().toJson(message));
		} else {
			messageQueue.add(this.gsonProvider.getGson().toJson(message));
		}
	}

	private void attemptReconnect() {
		System.out.println("RECONNECT");
		synchronized(lock) {
			lock.notifyAll();
		}
	}

	private void flushMessageQueue() {
		while (!messageQueue.isEmpty()) {
			this.send(messageQueue.poll());
		}
	}

	private SystemDriver getSystemDriver() {
		if(this.systemDriver == null) {
			this.systemDriver = Slingshot.getInstance().getSystemDriver();
		}
		return this.systemDriver;
	}

	private SimulationDriver getSimulationDriver() {
		if(this.simulationDriver == null) {
			this.simulationDriver = Slingshot.getInstance().getSimulationDriver();
		}
		return this.simulationDriver;
	}
}
