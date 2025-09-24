package com.ur.urcap.psyonic.abilityhand.predefinedgrasps.impl;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.undoredo.UndoRedoManager;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;

import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.awt.Event;
import java.awt.EventQueue;
import java.awt.GradientPaint;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.swing.SwingUtilities;

public class PredefinedGraspsDaemonProgramNodeContribution implements ProgramNodeContribution {
	private static final String GRASPKEY = "selected_grasp";
    private static final String SERVER_URL_KEY = "server_url";
    private static final String DEFAULT_SERVER_URL = "http://localhost:40405"; // Assume a default XML-RPC server URL for the hand
	private final ProgramAPIProvider apiProvider;
	private final PredefinedGraspsDaemonProgramNodeView view;
	private final DataModel model;
	private XmlRpcClient xmlRpcClient;

	private final UndoRedoManager undoRedoManager;

	public PredefinedGraspsDaemonProgramNodeContribution(ProgramAPIProvider apiProvider,
										   PredefinedGraspsDaemonProgramNodeView view,
										   DataModel model) {
		// keyboardInputFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory();
		this.apiProvider = apiProvider;
		this.view = view;
		this.model = model;

		this.undoRedoManager = this.apiProvider.getProgramAPI().getUndoRedoManager();
		establishXmlRpcConnection();

	}

	private void establishXmlRpcConnection() {
        String serverUrl = getServerUrl();
        try {
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL(serverUrl));
            xmlRpcClient = new XmlRpcClient();
            xmlRpcClient.setConfig(config);
        } catch (MalformedURLException e) {
            // Handle connection error, perhaps log or show in view
            view.showError("Invalid server URL: " + serverUrl);
        }
    }


	@Override
	public void openView() {
		view.updateView();
	}


	@Override
	public void closeView() {
	}

	@Override
	public String getTitle() {
		return "Predefined Grasp Daemon: ";
	}

	@Override
	public boolean isDefined() {
		return !getSelectedGrasp().isEmpty();
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		// Interact with the daemon process through XML-RPC calls
		// Note, alternatively plain sockets can be used.

        String xmlRpcVar = "ability_hand_rpc";
        writer.assign(xmlRpcVar, "rpc_factory(\"xmlrpc\", \"" + getServerUrl() + "\")");
        writer.appendLine(xmlRpcVar + ".set_grip(\"" + getSelectedGrasp() + "\")");

	}



	public void onGraspSelected(final String grasp) {
		if (model != null && grasp != null) {
			try {
				apiProvider.getProgramAPI().getUndoRedoManager().recordChanges(new UndoableChanges() {
					public void executeChanges() {
						model.set(GRASPKEY, grasp);
					}
				});
			} catch (Exception e) {
				System.err.println("Could not set grasp selection: " + e.getMessage());
			}
		}
	}

	public String getSelectedGrasp() {
    try {
        String value = model.get(GRASPKEY, "");  // "Open" is now just the default value
        System.out.println("getSelectedGrasp() returning: '" + value + "'");
        return value;
    } catch (Exception e) {
        System.err.println("Error getting selected grasp: " + e.getMessage());
        return "";  // Return default on error
    }
}

    private String getServerUrl() {
        return model.get(SERVER_URL_KEY, DEFAULT_SERVER_URL);
    }

}
