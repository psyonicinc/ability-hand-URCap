package com.ur.urcap.psyonic.abilityhand.predefinedgrasps.impl;

import com.ur.urcap.api.contribution.DaemonContribution;
import com.ur.urcap.api.contribution.DaemonService;

import java.net.MalformedURLException;
import java.net.URL;


public class PredefinedGraspsDaemonService implements DaemonService {

	private DaemonContribution daemonContribution;

	public PredefinedGraspsDaemonService() {
	}

	@Override
	public void init(DaemonContribution daemonContribution) {
		this.daemonContribution = daemonContribution;
		try {
			daemonContribution.installResource(new URL("file:com/ur/urcap/psyonic/abilityhand/predefinedgrasps/impl/daemon/"));
		} catch (MalformedURLException e) {	}
	}

	@Override
	public URL getExecutable() {
		try {
			// Two equivalent example daemons are available:
			return new URL("file:com/ur/urcap/psyonic/abilityhand/predefinedgrasps/impl/daemon/predefined-grasps.py"); // Python executable
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public DaemonContribution getDaemon() {
		return daemonContribution;
	}

}
