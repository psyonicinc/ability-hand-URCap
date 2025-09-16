package com.ur.urcap.psyonic.abilityhand.connection.impl;

import com.ur.urcap.api.contribution.DaemonService;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	@Override
	public void start(final BundleContext context) throws Exception {
		ConnectionDaemonService daemonService = new ConnectionDaemonService();
		ConnectionDaemonInstallationNodeService installationNodeService = new ConnectionDaemonInstallationNodeService(daemonService);

		context.registerService(SwingInstallationNodeService.class, installationNodeService, null);
		context.registerService(SwingProgramNodeService.class, new ConnectionDaemonProgramNodeService(), null);
		context.registerService(DaemonService.class, daemonService, null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}
}
