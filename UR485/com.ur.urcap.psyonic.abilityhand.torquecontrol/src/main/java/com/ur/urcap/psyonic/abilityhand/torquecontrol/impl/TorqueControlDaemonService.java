package com.ur.urcap.psyonic.abilityhand.torquecontrol.impl;

import com.ur.urcap.api.contribution.DaemonContribution;
import com.ur.urcap.api.contribution.DaemonService;

import java.net.MalformedURLException;
import java.net.URL;


public class TorqueControlDaemonService implements DaemonService {

	private DaemonContribution daemonContribution;

	public TorqueControlDaemonService() {
	}

	@Override
	public void init(DaemonContribution daemonContribution) {
		this.daemonContribution = daemonContribution;
		try {
			daemonContribution.installResource(new URL("file:com/ur/urcap/psyonic/abilityhand/torquecontrol/impl/daemon/"));
		} catch (MalformedURLException e) {	}
	}

	@Override
	public URL getExecutable() {
		try {
			// Two equivalent example daemons are available:
			return new URL("file:com/ur/urcap/psyonic/abilityhand/torquecontrol/impl/daemon/torque-control.py"); // Python executable
//			return new URL("file:com/ur/urcap/examples/mydaemonswing/impl/daemon/HelloWorld"); // C++ executable
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public DaemonContribution getDaemon() {
		return daemonContribution;
	}

}
