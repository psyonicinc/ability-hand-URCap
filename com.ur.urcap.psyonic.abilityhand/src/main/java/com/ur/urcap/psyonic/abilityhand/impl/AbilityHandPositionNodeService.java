package com.ur.urcap.psyonic.abilityhand.impl;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class AbilityHandPositionNodeService implements SwingProgramNodeService<AbilityHandPositionNodeContribution, AbilityHandPositionNodeView> {

	@Override
	public String getId() {
		return "AbilityHandPositionNode";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setChildrenAllowed(false);
	}

	@Override
	public String getTitle(Locale locale) {
		return "Ability Hand Position Command";
	}

	@Override
	public AbilityHandPositionNodeView createView(ViewAPIProvider apiProvider) {
		return new AbilityHandPositionNodeView();
	}

	@Override
	public AbilityHandPositionNodeContribution createNode(ProgramAPIProvider apiProvider,
			AbilityHandPositionNodeView view, DataModel model, CreationContext context) {
		return new AbilityHandPositionNodeContribution(apiProvider, view, model);
	}
}