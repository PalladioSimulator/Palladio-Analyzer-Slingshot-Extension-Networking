package org.palladiosimulator.analyzer.slingshot.networking.test;

import java.util.List;

import org.palladiosimulator.analyzer.slingshot.core.extension.SystemBehaviorExtension;
import org.palladiosimulator.analyzer.slingshot.eventdriver.annotations.Subscribe;
import org.palladiosimulator.analyzer.slingshot.eventdriver.annotations.eventcontract.OnEvent;
import org.palladiosimulator.analyzer.slingshot.workflow.events.WorkflowLaunchConfigurationBuilderInitialized;

@OnEvent(when = WorkflowLaunchConfigurationBuilderInitialized.class)
public class ConfigurationFixes implements SystemBehaviorExtension {

	public static final String ALLOC_FILE_NAME = "allocationModel"; 
	
	@Subscribe
	public void onWorkflowConfigurationInitialized(final WorkflowLaunchConfigurationBuilderInitialized event) {
		event.getConfiguration(ALLOC_FILE_NAME, 
				"", 
				(conf, modelFile) -> conf.setAllocationFiles(List.of()));
	}
}