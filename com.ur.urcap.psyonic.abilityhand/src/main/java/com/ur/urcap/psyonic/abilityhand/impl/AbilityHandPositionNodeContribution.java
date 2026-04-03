package com.ur.urcap.psyonic.abilityhand.impl;

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
    private static final String INDEX_KEY = "index";
    private static final String MIDDLE_KEY = "middle";
    private static final String RING_KEY = "ring";
    private static final String PINKY_KEY = "pinky";
    private static final String THUMB_FLEXOR_KEY = "thumb_flexor";
    private static final String THUMB_OPPOSITION_KEY = "thumb_opposition";
    private static final int DEFAULT_POSITION = 0;
    private boolean liveTracking = false;
    private static final String CHECKBOX_KEY = "false";

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
        view.setCheckbox(model.get(CHECKBOX_KEY, false));

        if (getInstallation().isDaemonEnabled() && liveTracking) {
            try {
                getDaemonInterface().stopGripThread();
                getDaemonInterface().stopPositionThread();
                getDaemonInterface().startPositionThread();
                List<Double> cmd = Arrays.asList(
                        (double) getPosition(INDEX_KEY),
                        (double) getPosition(MIDDLE_KEY),
                        (double) getPosition(RING_KEY),
                        (double) getPosition(PINKY_KEY),
                        (double) getPosition(THUMB_FLEXOR_KEY),
                        (double) getPosition(THUMB_OPPOSITION_KEY)
                        );
                getDaemonInterface().setPosition(cmd);

            } catch (Exception e) {
                view.showError("Failed to start position thread");
                e.printStackTrace();
                }
            }
        }

    @Override
    public void closeView() {
    }

    @Override
    public String getTitle() {
        return "Ability Hand Position Command";
    }

    @Override
    public boolean isDefined() {
    	return true;
    }

    @Override
    public void generateScript(ScriptWriter writer) {
        MyDaemonInstallationNodeContribution install = getInstallation();
        writer.assign("ah_daemon", install.getXMLRPCVariable());

        // writer.appendLine("ah_daemon.stopPositionThread()"); /////////////
        writer.appendLine("ah_daemon.startPositionThread()"); ////////////
        writer.appendLine(
        "ah_daemon.setPosition([" +
        (double) getPosition(INDEX_KEY) + "," +
        (double) getPosition(MIDDLE_KEY) + "," +
        (double) getPosition(RING_KEY) + "," +
        (double) getPosition(PINKY_KEY) + "," +
        (double) getPosition(THUMB_FLEXOR_KEY) + "," +
        (double) getPosition(THUMB_OPPOSITION_KEY) +
        "])" );
        // writer.appendLine("ah_daemon.stopPositionThread()"); ////////////

    }

    private MyDaemonInstallationNodeContribution getInstallation(){
		return apiProvider.getProgramAPI().getInstallationNode(MyDaemonInstallationNodeContribution.class);
	}

    public void updatePosition(String key, int value) {
    	
    	undoRedoManager.recordChanges(new UndoableChanges() {
			
			@Override
			public void executeChanges() {
				model.set(key, value);
			}
		});

    }

	public void onCheckboxChanged(final boolean checked) {
		undoRedoManager.recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				model.set(CHECKBOX_KEY, checked);
			}
		});
	}


    public void setLiveTracking(boolean value) {
        this.liveTracking = value;
        if (value == true) {

            try {
                    List<Double> cmd = Arrays.asList(
                    (double) getPosition(INDEX_KEY),
                    (double) getPosition(MIDDLE_KEY),
                    (double) getPosition(RING_KEY),
                    (double) getPosition(PINKY_KEY),
                    (double) getPosition(THUMB_FLEXOR_KEY),
                    (double) getPosition(THUMB_OPPOSITION_KEY)
                    );
                    getDaemonInterface().startPositionThread();
                    getDaemonInterface().setPosition(cmd);
                } catch (Exception e) {
                    view.showError("Failed to update hand position");
                    e.printStackTrace();
                    }
            }
            
    }

    public boolean isLiveTracking() {
        return liveTracking;
    }

    private XmlRpcMyDaemonInterface getDaemonInterface() {
    return getInstallation().getXmlRpcDaemonInterface();
    }

    private int getPosition(String key) {
        return model.get(key, DEFAULT_POSITION);
    }

    public void updateHandPosition() {

        if (liveTracking) {

            try {
                List<Double> cmd = Arrays.asList(
                (double) getPosition(INDEX_KEY),
                (double) getPosition(MIDDLE_KEY),
                (double) getPosition(RING_KEY),
                (double) getPosition(PINKY_KEY),
                (double) getPosition(THUMB_FLEXOR_KEY),
                (double) getPosition(THUMB_OPPOSITION_KEY)
                );
                getDaemonInterface().setPosition(cmd);
            } catch (Exception e) {
                view.showError("Failed to update hand position");
                e.printStackTrace();
            }
        }
    }

}