package com.ur.urcap.psyonic.abilityhand.impl;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class AbilityHandTorqueNodeService implements SwingProgramNodeService<AbilityHandTorqueNodeContribution, AbilityHandTorqueNodeView> {

	@Override
	public String getId() {
		return "AbilityHandTorqueNode";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setChildrenAllowed(false);
	}

	@Override
	public String getTitle(Locale locale) {
		return "Ability Hand Torque Command";
	}

	@Override
	public AbilityHandTorqueNodeView createView(ViewAPIProvider apiProvider) {
		return new AbilityHandTorqueNodeView();
	}

	@Override
	public AbilityHandTorqueNodeContribution createNode(ProgramAPIProvider apiProvider,
			AbilityHandTorqueNodeView view, DataModel model, CreationContext context) {
		return new AbilityHandTorqueNodeContribution(apiProvider, view, model);
	}
}