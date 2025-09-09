package com.ur.urcap.psyonic.abilityhand.predefinedgrasps.impl;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.ContributionConfiguration;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class PredefinedGraspsDaemonInstallationNodeService implements SwingInstallationNodeService<PredefinedGraspsDaemonInstallationNodeContribution, PredefinedGraspsDaemonInstallationNodeView> {

	private final PredefinedGraspsDaemonService daemonService;

	public PredefinedGraspsDaemonInstallationNodeService(PredefinedGraspsDaemonService daemonService) {
		this.daemonService = daemonService;
	}

	@Override
	public String getTitle(Locale locale) {
		return "Psyonic Ability Predefined Grasp Selection";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
	}

	@Override
	public PredefinedGraspsDaemonInstallationNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new PredefinedGraspsDaemonInstallationNodeView(style);
	}

	@Override
	public PredefinedGraspsDaemonInstallationNodeContribution createInstallationNode(InstallationAPIProvider apiProvider, PredefinedGraspsDaemonInstallationNodeView view, DataModel model, CreationContext context) {
		return new PredefinedGraspsDaemonInstallationNodeContribution(apiProvider, view, model, daemonService, new XmlRpcMyDaemonInterface(), context);
	}

}
