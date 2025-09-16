package com.ur.urcap.psyonic.abilityhand.connection.impl;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

import java.awt.EventQueue;

public class ConnectionDaemonProgramNodeContribution implements ProgramNodeContribution {

	private final ProgramAPIProvider apiProvider;
	private final ConnectionDaemonProgramNodeView view;
	private final DataModel model;
	private final XmlRpcMyDaemonInterface daemonStatusMonitor;
	private final KeyboardInputFactory keyboardInputFactory;

	public ConnectionDaemonProgramNodeContribution(ProgramAPIProvider apiProvider,
										   ConnectionDaemonProgramNodeView view,
										   DataModel model) {
		keyboardInputFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory();
		this.apiProvider = apiProvider;
		this.view = view;
		this.model = model;
		this.daemonStatusMonitor = getInstallation().getDaemonStatusMonitor();

	}

	@Override
	public void openView() {
		daemonStatusMonitor.startMonitorThread();

		//UI updates from non-GUI threads must use EventQueue.invokeLater (or SwingUtilities.invokeLater)
		// updateUI();
	}

	private void updateUI() {
		String message;
		try {
			// Provide a real-time preview of the daemon state
			if (daemonStatusMonitor.isDaemonReachable()) {

			} else {

			}
		} catch (Exception e) {
			System.err.println("Could not retrieve essential data from the daemon process for the preview.");
			message = "<Daemon disconnected>";
		}
	}


	@Override
	public void closeView() {
		daemonStatusMonitor.stopMonitorThread();
	}

	@Override
	public String getTitle() {
		return "Connection Port Daemon: ";
	}

	@Override
	public boolean isDefined() {
		return daemonStatusMonitor.isDaemonReachable();
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		// Interact with the daemon process through XML-RPC calls
		// Note, alternatively plain sockets can be used.
		writer.assign("daemon_connection", getInstallation().getXMLRPCVariable() + ".connect_to_hand()");
		writer.assign("daemon_server", getInstallation().getXMLRPCVariable() + ".start_server_socket()");
		writer.writeChildren();
	}


	private ConnectionDaemonInstallationNodeContribution getInstallation() {
		return apiProvider.getProgramAPI().getInstallationNode(ConnectionDaemonInstallationNodeContribution.class);
	}
}
