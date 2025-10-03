
package com.ur.urcap.psyonic.abilityhand.impl;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.undoredo.UndoRedoManager;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;


public class MyDaemonProgramNodeContribution implements ProgramNodeContribution {

	private final ProgramAPIProvider apiProvider;
	private final MyDaemonProgramNodeView view;
	private final DataModel model;
	private final UndoRedoManager undoRedoManager;
	
	private static final String BAUD_KEY = "baudrate";
	private static final Integer DEFAULT_BAUD = 0;
	
	private static final String SIMULATED_KEY = "simulated";
	private static final boolean DEFAULT_SIMULATED = false;


	public MyDaemonProgramNodeContribution(ProgramAPIProvider apiProvider, MyDaemonProgramNodeView view, DataModel model) {
		this.apiProvider = apiProvider;
		this.view = view;
		this.model = model;
		this.undoRedoManager = this.apiProvider.getProgramAPI().getUndoRedoManager();
	}

	@Override
	public void openView() {
		view.setBaudComboBoxItems(getBaudItems());
		view.setBaudComboBoxSelection(getBaud());
	}

	@Override
	public void closeView() {
	}

	@Override
	public String getTitle() {
		if (getBaud().equals(0)) {
			return "Ability Hand - Baud: auto";
		}
		else {
			return "Ability Hand - Baud: \"" + getBaud() + "\"";
		}
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		// Assign XMLRPC Variable
		MyDaemonInstallationNodeContribution install = getInstallation();
		writer.assign("daemon", install.getXMLRPCVariable());
		
		// Connect to Hand
		String urBool = getSimulated() ? "True" : "False";
		writer.appendLine("result = daemon.connect("+getBaud()+","+urBool+")");
		writer.appendLine("popup(\"Ability Hand Connected?: \" + to_str(result), title=\"Ability Hand Connection\", warning=False, error=False, blocking=False)");

		
		// Execute Children Nodes
		writer.writeChildren();
		
		// Disconnect Hand
		writer.appendLine("result = daemon.disconnect()");
		writer.appendLine("popup(\"Ability Hand Disconnected?: \" + to_str(result), title=\"Ability Hand Disconnect\", warning=False, error=False, blocking=False)");

	}
	
	@Override
	public boolean isDefined() {
		return true;
	}
	

	private MyDaemonInstallationNodeContribution getInstallation(){
		return apiProvider.getProgramAPI().getInstallationNode(MyDaemonInstallationNodeContribution.class);
	}
	
	public void onBaudSelection(final Integer output) {
		undoRedoManager.recordChanges(new UndoableChanges() {
			
			@Override
			public void executeChanges() {
				model.set(BAUD_KEY, output);
			}
		});
	}
	
	public void onSimulatedSelection(final boolean simulated) {
		undoRedoManager.recordChanges(new UndoableChanges() {
			
			@Override
			public void executeChanges() {
				model.set(SIMULATED_KEY, simulated);
			}
		});
	}
	
	private Integer getBaud() {
		return model.get(BAUD_KEY, DEFAULT_BAUD);
	}
	
	private boolean getSimulated() {
		return model.get(SIMULATED_KEY, DEFAULT_SIMULATED);
	}
	
	private Integer[] getBaudItems() {
		Integer[] items = {
				0,
				460800,
				921600,
				1000000,
				1200,
				2400,
				4800,
				9600,
				14400,
				19200,
				28800,
				31250,
				38400,
				56000,
				57500,
				76800,
				115200,
				230400,
				250000,
		};
		return items;
	}
}
