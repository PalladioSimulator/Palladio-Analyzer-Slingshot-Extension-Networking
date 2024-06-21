package org.palladiosimulator.analyzer.slingshot.networking.test;

import javax.inject.Inject;

import org.palladiosimulator.analyzer.slingshot.core.extension.SystemBehaviorExtension;
import org.palladiosimulator.analyzer.slingshot.eventdriver.annotations.Subscribe;
import org.palladiosimulator.analyzer.slingshot.eventdriver.annotations.eventcontract.OnEvent;
import org.palladiosimulator.analyzer.slingshot.networking.data.EventMessage;
import org.palladiosimulator.analyzer.slingshot.networking.data.Message;
import org.palladiosimulator.analyzer.slingshot.networking.util.GsonProvider;


@OnEvent(when = EventMessage.class)
public class DeserializerTest implements SystemBehaviorExtension {

	@Inject
	private GsonProvider gsonProvider;

	@Subscribe
	public void onEventMessage(final EventMessage<?> message) {

		final String s = serialize(message);

		final Message<?> m = deserialize(s);

		System.out.println(m.getClass());
	}

	public Message<?> deserialize(final String arg) {
		System.out.println("DESERIALIZE MESSAGE: " + arg);
		try {
			final var message = this.gsonProvider.getGson().fromJson(arg, Message.class);
			return message;

		} catch (final Throwable e) {
			System.out.println("Error on deserializing message. No event was dispatched" + e);
		}

		return null;
	}

	public String serialize(final Message<?> m) {
		System.out.println("SERIALIZE MESSAGE: " + m);
		try {
			final String message = this.gsonProvider.getGson().toJson(m);
			return message;

		} catch (final Throwable e) {
			System.out.println("Error on deserializing message. No event was dispatched" + e);
		}
		return null;
	}

}
