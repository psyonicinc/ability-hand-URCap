package com.ur.urcap.psyonic.abilityhand.impl;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class AbilityHandDutyNodeService implements SwingProgramNodeService<AbilityHandDutyNodeContribution, AbilityHandDutyNodeView> {

	@Override
	public String getId() {
		return "AbilityHandDutyNode";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setChildrenAllowed(false);
	}

	@Override
	public String getTitle(Locale locale) {
		return "Ability Hand Duty Command";
	}

	@Override
	public AbilityHandDutyNodeView createView(ViewAPIProvider apiProvider) {
		return new AbilityHandDutyNodeView();
	}

	@Override
	public AbilityHandDutyNodeContribution createNode(ProgramAPIProvider apiProvider,
			AbilityHandDutyNodeView view, DataModel model, CreationContext context) {
		return new AbilityHandDutyNodeContribution(apiProvider, view, model);
	}
}