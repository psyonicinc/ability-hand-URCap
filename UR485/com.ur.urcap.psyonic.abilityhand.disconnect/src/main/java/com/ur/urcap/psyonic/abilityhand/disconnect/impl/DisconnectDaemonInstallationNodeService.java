package com.ur.urcap.psyonic.abilityhand.disconnect.impl;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.ContributionConfiguration;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class DisconnectDaemonInstallationNodeService implements SwingInstallationNodeService<DisconnectDaemonInstallationNodeContribution, DisconnectDaemonInstallationNodeView> {

	private final DisconnectDaemonService daemonService;

	public DisconnectDaemonInstallationNodeService(DisconnectDaemonService daemonService) {
		this.daemonService = daemonService;
	}

	@Override
	public String getTitle(Locale locale) {
		return "Psyonic Ability Hand Disconnect Port";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
	}

	@Override
	public DisconnectDaemonInstallationNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new DisconnectDaemonInstallationNodeView(style);
	}

	@Override
	public DisconnectDaemonInstallationNodeContribution createInstallationNode(InstallationAPIProvider apiProvider, DisconnectDaemonInstallationNodeView view, DataModel model, CreationContext context) {
		return new DisconnectDaemonInstallationNodeContribution(apiProvider, view, model, daemonService, new XmlRpcMyDaemonInterface(), context);
	}

}
