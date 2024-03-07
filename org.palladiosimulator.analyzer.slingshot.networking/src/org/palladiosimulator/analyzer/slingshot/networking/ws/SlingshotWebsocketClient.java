package org.palladiosimulator.analyzer.slingshot.networking.ws;

import java.net.URI;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.palladiosimulator.analyzer.slingshot.common.events.DESEvent;
import org.palladiosimulator.analyzer.slingshot.common.events.SystemEvent;
import org.palladiosimulator.analyzer.slingshot.core.Slingshot;
import org.palladiosimulator.analyzer.slingshot.core.api.SimulationDriver;
import org.palladiosimulator.analyzer.slingshot.core.api.SystemDriver;
import org.palladiosimulator.analyzer.slingshot.networking.SimulationEventBuffer;


@Singleton
public class SlingshotWebsocketClient extends WebSocketClient {
	private static final Logger LOGGER = Logger.getLogger(SlingshotWebsocketClient.class.getName());
	private static final int RECONNECT_DELAY = 5000;
	private SystemDriver systemDriver;
	private SimulationDriver simulationDriver;
    private final ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<>();
	@Inject
	private GsonProvider gsonProvider;
	@Inject
	private SimulationEventBuffer simulationEventBuffer;
	final Object lock = new Object();
	private Thread reconnectionThread = new Thread(() -> {
		if(this.isOpen()) {
        	synchronized(lock) {
        		try {
					lock.wait();
				} catch (InterruptedException e) {
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
	        } catch (InterruptedException e) {
	            LOGGER.error("Reconnection thread interrupted", e);
	        }
		}
    });
	

	public SlingshotWebsocketClient(URI serverUri) {
		super(serverUri);
		try {
			this.connectBlocking();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			this.reconnectionThread.start();
		}
	}

    @Override
    public void onClose(int arg0, String arg1, boolean arg2) {
        System.out.println("Websocket Connection Closed :(");
        LOGGER.warn("Websocket Connection Closed :(");
        attemptReconnect();
    }

    @Override
    public void onError(Exception e) {
        System.out.println("Server Error: " + e);
        LOGGER.error("Server Error: ", e);
        attemptReconnect();
    }

	@Override
	public void onMessage(String arg) {
		System.out.println("MESSAGE: " + arg);
		try {
			var message = this.gsonProvider.getGson().fromJson(arg, Message.class);
			System.out.println(message.getEvent());
			System.out.println(message.getClass());
			if(message instanceof SystemEvent eventMessage) {
				getSystemDriver().postEvent(eventMessage);
			} else if (message instanceof DESEvent eventMessage) {
				//getSimulationDriver().scheduleEvent(eventMessage);
				this.simulationEventBuffer.addMessage(eventMessage);
			} else {
				System.out.println("Received Message could not be dispatched, as it is not a EventMessage (Or SystemEvent): " + message);
				LOGGER.warn("Received Message could not be dispatched, as it is not a EventMessage (Or SystemEvent): " + message);
			}
		} catch(Throwable e) {
			System.out.println("Error on deserializing message. No event was dispatched" + e);
			LOGGER.error("Error on deserializing message. No event was dispatched", e);
		}  
	}

    @Override
    public void onOpen(ServerHandshake arg0) {
        System.out.println("Connection Open. Sending '" + messageQueue.size() + "' messages in queue");
        LOGGER.info("Connection Open. Sending '" + messageQueue.size() + "' messages in queue");
        flushMessageQueue();
    }

    public void sendMessage(Message<?> message) {
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
