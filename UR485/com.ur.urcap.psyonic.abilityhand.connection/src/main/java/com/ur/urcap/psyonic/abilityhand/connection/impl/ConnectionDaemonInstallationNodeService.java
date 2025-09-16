package com.ur.urcap.psyonic.abilityhand.connection.impl;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.ContributionConfiguration;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class ConnectionDaemonInstallationNodeService implements SwingInstallationNodeService<ConnectionDaemonInstallationNodeContribution, ConnectionDaemonInstallationNodeView> {

	private final ConnectionDaemonService daemonService;

	public ConnectionDaemonInstallationNodeService(ConnectionDaemonService daemonService) {
		this.daemonService = daemonService;
	}

	@Override
	public String getTitle(Locale locale) {
		return "Psyonic Ability Hand Connection Port";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
	}

	@Override
	public ConnectionDaemonInstallationNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new ConnectionDaemonInstallationNodeView(style);
	}

	@Override
	public ConnectionDaemonInstallationNodeContribution createInstallationNode(InstallationAPIProvider apiProvider, ConnectionDaemonInstallationNodeView view, DataModel model, CreationContext context) {
		return new ConnectionDaemonInstallationNodeContribution(apiProvider, view, model, daemonService, new XmlRpcMyDaemonInterface(), context);
	}

}
