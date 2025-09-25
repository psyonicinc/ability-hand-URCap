package com.ur.urcap.psyonic.abilityhand.predefinedgrasps.impl;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class PredefinedGraspsDaemonProgramNodeService implements SwingProgramNodeService<PredefinedGraspsDaemonProgramNodeContribution, PredefinedGraspsDaemonProgramNodeView> {

	public PredefinedGraspsDaemonProgramNodeService() {
	}

	@Override
	public String getId() {
		return "PredefinedGraspsDaemonSwingNode";
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
	public PredefinedGraspsDaemonProgramNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new PredefinedGraspsDaemonProgramNodeView(style);
	}

	@Override
	public PredefinedGraspsDaemonProgramNodeContribution createNode(ProgramAPIProvider apiProvider, PredefinedGraspsDaemonProgramNodeView view, DataModel model, CreationContext context) {
		return new PredefinedGraspsDaemonProgramNodeContribution(apiProvider, view, model);
	}

}
