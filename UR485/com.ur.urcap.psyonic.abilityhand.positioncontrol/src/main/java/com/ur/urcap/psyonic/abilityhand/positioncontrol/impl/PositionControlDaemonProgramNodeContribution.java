package com.ur.urcap.psyonic.abilityhand.positioncontrol.impl;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

import java.awt.EventQueue;

public class PositionControlDaemonProgramNodeContribution implements ProgramNodeContribution {
	private static final String INDEXPOSITIONKEY = "index_position";
	private static final String MIDDLEPOSITIONKEY = "middle_position";
	private static final String RINGPOSITIONKEY = "ring_position";
	private static final String PINKYPOSITIONKEY = "pinky_position";
	private static final String THUMBPOSITIONKEY = "thumb_position";
	private static final String THUMBROTPOSITIONKEY = "thumbrot_position";

	private final ProgramAPIProvider apiProvider;
	private final PositionControlDaemonProgramNodeView view;
	private final DataModel model;
	private final XmlRpcMyDaemonInterface daemonStatusMonitor;
	private final KeyboardInputFactory keyboardInputFactory;

	public PositionControlDaemonProgramNodeContribution(ProgramAPIProvider apiProvider,
										   PositionControlDaemonProgramNodeView view,
										   DataModel model) {
		keyboardInputFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory();
		this.apiProvider = apiProvider;
		this.view = view;
		this.model = model;
		this.daemonStatusMonitor = getInstallation().getDaemonStatusMonitor();

	}

	@Override
	public void openView() {
		view.setIndexPositionValueText(getIndexPositionValue());
		view.setMiddlePositionValueText(getMiddlePositionValue());
		view.setRingPositionValueText(getRingPositionValue());
		view.setPinkyPositionValueText(getPinkyPositionValue());
		view.setThumbPositionValueText(getThumbPositionValue());
		view.setThumbRotPositionValueText(getThumbRotPositionValue());
		daemonStatusMonitor.startMonitorThread();

		//UI updates from non-GUI threads must use EventQueue.invokeLater (or SwingUtilities.invokeLater)
		updateUI();
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
		return "Position Control Daemon: ";
	}

	@Override
	public boolean isDefined() {
		return daemonStatusMonitor.isDaemonReachable() 
		&& !getIndexPositionValue().isEmpty()
		 && !getMiddlePositionValue().isEmpty()
		  && !getRingPositionValue().isEmpty()
		   && !getPinkyPositionValue().isEmpty()
		    && !getThumbPositionValue().isEmpty()
			 && !getThumbRotPositionValue().isEmpty();
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		// Interact with the daemon process through XML-RPC calls
		// Note, alternatively plain sockets can be used.

		writer.assign("PCdaemon_index", getInstallation().getXMLRPCVariable() + ".get_index_flex(\"" + getIndexPositionValue() + "\")");
		writer.assign("PCdaemon_middle", getInstallation().getXMLRPCVariable() + ".get_middle_flex(\"" + getMiddlePositionValue() + "\")");
		writer.assign("PCdaemon_ring", getInstallation().getXMLRPCVariable() + ".get_ring_flex(\"" + getRingPositionValue() + "\")");
		writer.assign("PCdaemon_pinky", getInstallation().getXMLRPCVariable() + ".get_pinky_flex(\"" + getPinkyPositionValue() + "\")");
		writer.assign("PCdaemon_thumb", getInstallation().getXMLRPCVariable() + ".get_thumb_flex(\"" + getThumbPositionValue() + "\")");
		writer.assign("PCdaemon_thumbrot", getInstallation().getXMLRPCVariable() + ".get_thumb_rot(\"" + getThumbRotPositionValue() + "\")");
		writer.assign("PCdaemon_motor_status", getInstallation().getXMLRPCVariable() + ".send_motor_positions()");
		writer.appendLine("popup(\"Finger Positions Sent!\", title=\"Position Control\", blocking=True)");
		writer.writeChildren();
	}

	public KeyboardTextInput getIndexInputForTextField() {
		KeyboardTextInput keyboardTextInput = keyboardInputFactory.createStringKeyboardInput();
		keyboardTextInput.setInitialValue(getIndexPositionValue());
		return keyboardTextInput;
	}

	public KeyboardTextInput getMiddleInputForTextField() {
		KeyboardTextInput keyboardTextInput = keyboardInputFactory.createStringKeyboardInput();
		keyboardTextInput.setInitialValue(getMiddlePositionValue());
		return keyboardTextInput;
	}

	public KeyboardTextInput getRingInputForTextField() {
		KeyboardTextInput keyboardTextInput = keyboardInputFactory.createStringKeyboardInput();
		keyboardTextInput.setInitialValue(getRingPositionValue());
		return keyboardTextInput;
	}

	public KeyboardTextInput getPinkyInputForTextField() {
		KeyboardTextInput keyboardTextInput = keyboardInputFactory.createStringKeyboardInput();
		keyboardTextInput.setInitialValue(getPinkyPositionValue());
		return keyboardTextInput;
	}

	public KeyboardTextInput getThumbInputForTextField() {
		KeyboardTextInput keyboardTextInput = keyboardInputFactory.createStringKeyboardInput();
		keyboardTextInput.setInitialValue(getThumbPositionValue());
		return keyboardTextInput;
	}

	public KeyboardTextInput getThumbRotInputForTextField() {
		KeyboardTextInput keyboardTextInput = keyboardInputFactory.createStringKeyboardInput();
		keyboardTextInput.setInitialValue(getThumbRotPositionValue());
		return keyboardTextInput;
	}

	public KeyboardInputCallback<String> getCallbackForIndexTextField() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				setIndexPositionValue(value);
				view.setIndexPositionValueText(value);
			}
		};
	}

	public KeyboardInputCallback<String> getCallbackForMiddleTextField() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				setMiddlePositionValue(value);
				view.setMiddlePositionValueText(value);
			}
		};
	}

	public KeyboardInputCallback<String> getCallbackForRingTextField() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				setRingPositionValue(value);
				view.setRingPositionValueText(value);
			}
		};
	}

	public KeyboardInputCallback<String> getCallbackForPinkyTextField() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				setPinkyPositionValue(value);
				view.setPinkyPositionValueText(value);
			}
		};
	}

	public KeyboardInputCallback<String> getCallbackForThumbTextField() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				setThumbPositionValue(value);
				view.setThumbPositionValueText(value);
			}
		};
	}

	public KeyboardInputCallback<String> getCallbackForThumbRotTextField() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				setThumbRotPositionValue(value);
				view.setThumbRotPositionValueText(value);
			}
		};
	}

	private String getIndexPositionValue() {
		return model.get(INDEXPOSITIONKEY, "");
	}

	private String getMiddlePositionValue() {
		return model.get(MIDDLEPOSITIONKEY, "");
	}

	private String getRingPositionValue() {
		return model.get(RINGPOSITIONKEY, "");
	}

	private String getPinkyPositionValue() {
		return model.get(PINKYPOSITIONKEY, "");
	}

	private String getThumbPositionValue() {
		return model.get(THUMBPOSITIONKEY, "");
	}

	private String getThumbRotPositionValue() {
		return model.get(THUMBROTPOSITIONKEY, "");
	}

	private void setIndexPositionValue(String index_position_value) {
		if ("".equals(index_position_value)) {
			model.remove(INDEXPOSITIONKEY);
		} else {
			model.set(INDEXPOSITIONKEY, index_position_value);
		}
	}

	private void setMiddlePositionValue(String middle_position_value) {
		if ("".equals(middle_position_value)) {
			model.remove(MIDDLEPOSITIONKEY);
		} else {
			model.set(MIDDLEPOSITIONKEY, middle_position_value);
		}
	}

	private void setRingPositionValue(String ring_position_value) {
		if ("".equals(ring_position_value)) {
			model.remove(RINGPOSITIONKEY);
		} else {
			model.set(RINGPOSITIONKEY, ring_position_value);
		}
	}

	private void setPinkyPositionValue(String pinky_position_value) {
		if ("".equals(pinky_position_value)) {
			model.remove(PINKYPOSITIONKEY);
		} else {
			model.set(PINKYPOSITIONKEY, pinky_position_value);
		}
	}

	private void setThumbPositionValue(String thumb_position_value) {
		if ("".equals(thumb_position_value)) {
			model.remove(THUMBPOSITIONKEY);
		} else {
			model.set(THUMBPOSITIONKEY, thumb_position_value);
		}
	}

	private void setThumbRotPositionValue(String thumbrot_position_value) {
		if ("".equals(thumbrot_position_value)) {
			model.remove(THUMBROTPOSITIONKEY);
		} else {
			model.set(THUMBROTPOSITIONKEY, thumbrot_position_value);
		}
	}

	private PositionControlDaemonInstallationNodeContribution getInstallation() {
		return apiProvider.getProgramAPI().getInstallationNode(PositionControlDaemonInstallationNodeContribution.class);
	}
}
