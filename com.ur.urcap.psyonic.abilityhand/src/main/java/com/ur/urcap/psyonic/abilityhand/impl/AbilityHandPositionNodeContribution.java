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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class AbilityHandPositionNodeContribution implements ProgramNodeContribution {
    private static final String INDEX_KEY = "index";
    private static final String MIDDLE_KEY = "middle";
    private static final String RING_KEY = "ring";
    private static final String PINKY_KEY = "pinky";
    private static final String THUMB_FLEXOR_KEY = "thumb_flexor";
    private static final String THUMB_OPPOSITION_KEY = "thumb_opposition";
    private static final int DEFAULT_POSITION = 0;
    private static String WAYPOINT_NAMES = "waypoint_names";
    private static String WAYPOINT_PRE = "waypoint_";
    private static String WAYPOINT_SELECTED = "selected_waypoint";

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

        writer.appendLine("ah_daemon.startPositionThread()");
        
        writer.appendLine(
        "ah_daemon.setPosition([" +
        (double) getPosition(INDEX_KEY) + "," +
        (double) getPosition(MIDDLE_KEY) + "," +
        (double) getPosition(RING_KEY) + "," +
        (double) getPosition(PINKY_KEY) + "," +
        (double) getPosition(THUMB_FLEXOR_KEY) + "," +
        (double) getPosition(THUMB_OPPOSITION_KEY) +
        "])" );

        writer.appendLine("sleep(0.35)");

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
                    getDaemonInterface().stopGripThread();
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

    public void savePositionPoint() {
        String input = JOptionPane.showInputDialog(null, "Enter a Name for Hand Waypoint", JOptionPane.PLAIN_MESSAGE);
        if (input == null || input.trim().isEmpty()) return;
        final String name = input.trim();

        int[] positions;
        try {
            positions = getDaemonInterface().getCurrPosition();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Daemon Call Failed " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        final int[] finalPositions = positions;
        undoRedoManager.recordChanges(new UndoableChanges() {
            @Override
            public void executeChanges() {
                saveEntry(name, finalPositions);
                model.set(WAYPOINT_SELECTED, name);
            }
        });

        updateView();

    }

    public void onWaypointSelected(String name) {
        undoRedoManager.recordChanges(new UndoableChanges() {
            @Override
            public void executeChanges() {
                model.set(WAYPOINT_SELECTED, name);
                
            }
        });

        if (view != null) {
            int[] positions = getWaypointPositions(name);
            view.setWaypointDisplay(name, positions);
            updatePosition("index", (int) positions[0]);
            updatePosition("middle", (int) positions[1]);
            updatePosition("ring", (int) positions[2]);
            updatePosition("pinky", (int) positions[3]);
            updatePosition("thumb_flexor", (int) positions[4]);
            updatePosition("thumb_opposition", (int) -positions[5]);
            view.updateSliders(getPosition(INDEX_KEY), getPosition(MIDDLE_KEY), getPosition(RING_KEY), getPosition(PINKY_KEY), getPosition(THUMB_FLEXOR_KEY), getPosition(THUMB_OPPOSITION_KEY));
            
            if (getDaemonInterface().isDaemonReachable() && liveTracking) {
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
                    view.showError("Failed to start waypoint");
                    e.printStackTrace();
                }
                
            }
        }
    }

    private void saveEntry(String name, int[] positions) {
                List<String> names = getWaypointNames();
                if (!names.contains(name)) {
                    names.add(name);
                    model.set(WAYPOINT_NAMES, listToString(names));
                }
                model.set(WAYPOINT_PRE + name, arrayToString(positions));
    }

    private List<String> getWaypointNames() {
        String raw = model.get(WAYPOINT_NAMES, "");
        List<String> list = new ArrayList<>();
        if (!raw.isEmpty()) {
            for (String s : raw.split(",")) {
                if (!s.trim().isEmpty()) list.add(s.trim());
            }
        }
        return list;
    }

    private int[] getWaypointPositions(String name) {
        String raw = model.get(WAYPOINT_PRE + name, "");
        if (raw.isEmpty()) return new int[6];
        String[] parts = raw.split(",");
        int[] positions = new int[parts.length];
        for (int i=0; i<parts.length; i++) {
            positions[i] = Integer.parseInt(parts[i].trim());
        }
        return positions;
    }

    public void deleteSelectedWaypoint() {
        String selected = model.get(WAYPOINT_SELECTED, "");
        if (selected.isEmpty()) return;

        int result = JOptionPane.showConfirmDialog(null,
            "Delete \"" + selected + "\"?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
        if (result != JOptionPane.YES_OPTION) return;

        undoRedoManager.recordChanges(new UndoableChanges() {
            @Override
            public void executeChanges() {
                
                List<String> names = getWaypointNames();
                names.remove(selected);
                model.set(WAYPOINT_NAMES, listToString(names));

                
                model.remove(WAYPOINT_PRE + selected);

                
                if (names.isEmpty()) {
                    model.set(WAYPOINT_SELECTED, "");
                } else {
                    model.set(WAYPOINT_SELECTED, names.get(0));
                }
            }
        });

        updateView();
    }

    private void updateView() {
        if (view==null) return;
        List<String> names = getWaypointNames();
        String selected = model.get(WAYPOINT_SELECTED, "");
        view.setDropdownItems(names, selected);
        if (!selected.isEmpty()) {
            int [] positions = getWaypointPositions(selected);
            positions[5] = -positions[5];
            view.setWaypointDisplay(selected, positions);
        }
    }

    private String arrayToString(int[] vals) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < vals.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(vals[i]);
        }
        return sb.toString();
    }

    private String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(list.get(i));
        }
        return sb.toString();
    }

}