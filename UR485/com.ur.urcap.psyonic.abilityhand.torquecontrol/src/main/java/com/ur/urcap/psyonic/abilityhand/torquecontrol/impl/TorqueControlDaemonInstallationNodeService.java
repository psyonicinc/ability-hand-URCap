package com.ur.urcap.psyonic.abilityhand.torquecontrol.impl;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.ContributionConfiguration;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class TorqueControlDaemonInstallationNodeService implements SwingInstallationNodeService<TorqueControlDaemonInstallationNodeContribution, TorqueControlDaemonInstallationNodeView> {

	private final TorqueControlDaemonService daemonService;

	public TorqueControlDaemonInstallationNodeService(TorqueControlDaemonService daemonService) {
		this.daemonService = daemonService;
	}

	@Override
	public String getTitle(Locale locale) {
		return "Psyonic Ability Hand Torque Control";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
	}

	@Override
	public TorqueControlDaemonInstallationNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new TorqueControlDaemonInstallationNodeView(style);
	}

	@Override
	public TorqueControlDaemonInstallationNodeContribution createInstallationNode(InstallationAPIProvider apiProvider, TorqueControlDaemonInstallationNodeView view, DataModel model, CreationContext context) {
		return new TorqueControlDaemonInstallationNodeContribution(apiProvider, view, model, daemonService, new XmlRpcMyDaemonInterface(), context);
	}

}
