package org.palladiosimulator.analyzer.slingshot.networking.test;

import java.lang.reflect.Type;

import javax.inject.Named;

import org.palladiosimulator.analyzer.slingshot.core.extension.AbstractSlingshotExtension;
import org.palladiosimulator.analyzer.slingshot.networking.data.Message;
import org.palladiosimulator.analyzer.slingshot.networking.data.NetworkingConstants;
import org.palladiosimulator.analyzer.slingshot.networking.test.injection.ApplyPolicyEventMessage;
import org.palladiosimulator.spd.ScalingPolicy;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;

public class TestModule extends AbstractSlingshotExtension {

	@Override
	protected void configure() {
		
		install(ConfigurationFixes.class);

		
		final var gsonBinder = MapBinder.newMapBinder(binder(), Type.class, Object.class);

		gsonBinder.addBinding(ScalingPolicy.class).toInstance(new JsonSerializer<ScalingPolicy>() {
			@Override
			public JsonElement serialize(final ScalingPolicy src, final Type typeOfSrc, final JsonSerializationContext context) {
				final JsonObject jsonScalingPolicy = new JsonObject();
				jsonScalingPolicy.addProperty("id", src.getId());
				return jsonScalingPolicy;
			}
		});

		
//		install(DeserializerTest.class);
//		install(Test.class);
		
		install(TestPlanInjection.class);
		
		
		
	}
	
    @Provides
    @Named(NetworkingConstants.CLIENT_NAME)
    public String clientName() {
        return "Test";
    }

}
