package com.ur.urcap.psyonic.abilityhand.impl;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class AbilityHandGripNodeService implements SwingProgramNodeService<AbilityHandGripNodeContribution, AbilityHandGripNodeView> {

	public AbilityHandGripNodeService() {
	}

	@Override
	public String getId() {
		return "AbilityHandGripSwingNode";
	}

	@Override
	public String getTitle(Locale locale) {
		return "Ability Hand Grip Command";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setChildrenAllowed(false);
	}

	@Override
	public AbilityHandGripNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new AbilityHandGripNodeView(style);
	}

	@Override
	public AbilityHandGripNodeContribution createNode(ProgramAPIProvider apiProvider, AbilityHandGripNodeView view, DataModel model, CreationContext context) {
		return new AbilityHandGripNodeContribution(apiProvider, view, model);
	}

}
