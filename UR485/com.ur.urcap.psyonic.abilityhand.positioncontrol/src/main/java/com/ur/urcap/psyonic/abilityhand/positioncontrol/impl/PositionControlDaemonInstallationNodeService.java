package com.ur.urcap.psyonic.abilityhand.positioncontrol.impl;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.ContributionConfiguration;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class PositionControlDaemonInstallationNodeService implements SwingInstallationNodeService<PositionControlDaemonInstallationNodeContribution, PositionControlDaemonInstallationNodeView> {

	private final PositionControlDaemonService daemonService;

	public PositionControlDaemonInstallationNodeService(PositionControlDaemonService daemonService) {
		this.daemonService = daemonService;
	}

	@Override
	public String getTitle(Locale locale) {
		return "Psyonic Ability Hand Position Control";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
	}

	@Override
	public PositionControlDaemonInstallationNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new PositionControlDaemonInstallationNodeView(style);
	}

	@Override
	public PositionControlDaemonInstallationNodeContribution createInstallationNode(InstallationAPIProvider apiProvider, PositionControlDaemonInstallationNodeView view, DataModel model, CreationContext context) {
		return new PositionControlDaemonInstallationNodeContribution(apiProvider, view, model, daemonService, new XmlRpcMyDaemonInterface(), context);
	}

}
