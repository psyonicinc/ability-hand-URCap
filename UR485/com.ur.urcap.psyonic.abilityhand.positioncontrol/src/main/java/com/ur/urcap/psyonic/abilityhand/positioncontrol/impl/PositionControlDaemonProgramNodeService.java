package com.ur.urcap.psyonic.abilityhand.positioncontrol.impl;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class PositionControlDaemonProgramNodeService implements SwingProgramNodeService<PositionControlDaemonProgramNodeContribution, PositionControlDaemonProgramNodeView> {

	public PositionControlDaemonProgramNodeService() {
	}

	@Override
	public String getId() {
		return "PositionControlDaemonSwingNode";
	}

	@Override
	public String getTitle(Locale locale) {
		return "Ability Hand Position Control Daemon";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setChildrenAllowed(true);
	}

	@Override
	public PositionControlDaemonProgramNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new PositionControlDaemonProgramNodeView(style);
	}

	@Override
	public PositionControlDaemonProgramNodeContribution createNode(ProgramAPIProvider apiProvider, PositionControlDaemonProgramNodeView view, DataModel model, CreationContext context) {
		return new PositionControlDaemonProgramNodeContribution(apiProvider, view, model);
	}

}
