package com.ur.urcap.psyonic.abilityhand.disconnect.impl;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class DisconnectDaemonProgramNodeService implements SwingProgramNodeService<DisconnectDaemonProgramNodeContribution, DisconnectDaemonProgramNodeView> {

	public DisconnectDaemonProgramNodeService() {
	}

	@Override
	public String getId() {
		return "DisconnectDaemonSwingNode";
	}

	@Override
	public String getTitle(Locale locale) {
		return "Ability Hand Disconnect Daemon";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setChildrenAllowed(true);
	}

	@Override
	public DisconnectDaemonProgramNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new DisconnectDaemonProgramNodeView(style);
	}

	@Override
	public DisconnectDaemonProgramNodeContribution createNode(ProgramAPIProvider apiProvider, DisconnectDaemonProgramNodeView view, DataModel model, CreationContext context) {
		return new DisconnectDaemonProgramNodeContribution(apiProvider, view, model);
	}

}
