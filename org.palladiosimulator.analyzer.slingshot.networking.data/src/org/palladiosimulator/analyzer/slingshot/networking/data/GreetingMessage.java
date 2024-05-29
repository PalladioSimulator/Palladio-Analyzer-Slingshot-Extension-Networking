package org.palladiosimulator.analyzer.slingshot.networking.data;

/**
 * 
 * @author Sarah Stieß
 *
 */
public class GreetingMessage extends Message<String>{

	public static final String MESSAGE_MAPPING_IDENTIFIER = "Greetings"; // GreetingMessage.class.getSimpleName();

	
	public GreetingMessage(final String creator) {
		super(MESSAGE_MAPPING_IDENTIFIER, "Hello from " + creator, creator);
	}

}
