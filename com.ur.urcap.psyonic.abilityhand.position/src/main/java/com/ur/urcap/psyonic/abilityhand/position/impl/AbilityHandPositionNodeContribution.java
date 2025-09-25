package com.ur.urcap.psyonic.abilityhand.position.impl;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.undoredo.UndoRedoManager;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class AbilityHandPositionNodeContribution implements ProgramNodeContribution {
    private static final String SERVER_URL_KEY = "server_url";
    private static final String DEFAULT_SERVER_URL = "http://localhost:40405"; // Assume a default XML-RPC server URL for the hand
    private static final String INDEX_KEY = "index";
    private static final String MIDDLE_KEY = "middle";
    private static final String RING_KEY = "ring";
    private static final String PINKY_KEY = "pinky";
    private static final String THUMB_FLEXOR_KEY = "thumb_flexor";
    private static final String THUMB_OPPOSITION_KEY = "thumb_opposition";
    private static final int DEFAULT_POSITION = 0;

    private final ProgramAPIProvider apiProvider;
    private final AbilityHandPositionNodeView view;
    private final DataModel model;
    private XmlRpcClient xmlRpcClient;
    
    private final UndoRedoManager undoRedoManager;

    public AbilityHandPositionNodeContribution(ProgramAPIProvider apiProvider, AbilityHandPositionNodeView view, DataModel model) {
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
        view.updateSliders(
                getPosition(INDEX_KEY),
                getPosition(MIDDLE_KEY),
                getPosition(RING_KEY),
                getPosition(PINKY_KEY),
                getPosition(THUMB_FLEXOR_KEY),
                getPosition(THUMB_OPPOSITION_KEY)
        );
    }

    @Override
    public void closeView() {
        // No cleanup needed
    }

    @Override
    public String getTitle() {
        return "Ability Hand Position Command";
    }

    @Override
    public boolean isDefined() {
    	return true;
//        return xmlRpcClient != null; // Considered defined if connection is established
    }

    @Override
    public void generateScript(ScriptWriter writer) {
        String xmlRpcVar = "ability_hand_rpc";
        writer.assign(xmlRpcVar, "rpc_factory(\"xmlrpc\", \"" + getServerUrl() + "\")");
        writer.appendLine(xmlRpcVar + ".set_position([\"" + getPosition(INDEX_KEY) + "\",\"" + getPosition(MIDDLE_KEY) + "\", \"" + getPosition(RING_KEY) + "\", \"" + getPosition(PINKY_KEY) + "\",\"" + getPosition(THUMB_FLEXOR_KEY) + "\",\"" + getPosition(THUMB_OPPOSITION_KEY) + "\"])");

    }

    public void updatePosition(String key, int value) {
    	
    	undoRedoManager.recordChanges(new UndoableChanges() {
			
			@Override
			public void executeChanges() {
				model.set(key, value);
			}
		});

    }

    private int getPosition(String key) {
        return model.get(key, DEFAULT_POSITION);
    }

    private String getServerUrl() {
        return model.get(SERVER_URL_KEY, DEFAULT_SERVER_URL);
    }
}