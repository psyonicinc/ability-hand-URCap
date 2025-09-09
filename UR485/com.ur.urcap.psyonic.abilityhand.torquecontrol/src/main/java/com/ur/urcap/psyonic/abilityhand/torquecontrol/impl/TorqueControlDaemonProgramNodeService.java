package com.ur.urcap.psyonic.abilityhand.torquecontrol.impl;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class TorqueControlDaemonProgramNodeService implements SwingProgramNodeService<TorqueControlDaemonProgramNodeContribution, TorqueControlDaemonProgramNodeView> {

	public TorqueControlDaemonProgramNodeService() {
	}

	@Override
	public String getId() {
		return "TorqueControlDaemonSwingNode";
	}

	@Override
	public String getTitle(Locale locale) {
		return "Ability Hand Torque Control Daemon";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setChildrenAllowed(true);
	}

	@Override
	public TorqueControlDaemonProgramNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new TorqueControlDaemonProgramNodeView(style);
	}

	@Override
	public TorqueControlDaemonProgramNodeContribution createNode(ProgramAPIProvider apiProvider, TorqueControlDaemonProgramNodeView view, DataModel model, CreationContext context) {
		return new TorqueControlDaemonProgramNodeContribution(apiProvider, view, model);
	}

}
