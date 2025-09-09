package com.ur.urcap.psyonic.abilityhand.torquecontrol.impl;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

import java.awt.EventQueue;

public class TorqueControlDaemonProgramNodeContribution implements ProgramNodeContribution {
	private static final String TORQUEKEY = "torque";
	private static final String INDEXINITTORQUEKEY = "index_init_torque";
	private static final String MIDDLEINITTORQUEKEY = "middle_init_torque";
	private static final String RINGINITTORQUEKEY = "ring_init_torque";
	private static final String PINKYINITTORQUEKEY = "pinky_init_torque";
	private static final String THUMBINITTORQUEKEY = "thumb_init_torque";
	private static final String THUMBROTINITTORQUEKEY = "thumbrot_init_torque";
	private static final String INDEXTORQUEKEY = "index_torque";
	private static final String MIDDLETORQUEKEY = "middle_torque";
	private static final String RINGTORQUEKEY = "ring_torque";
	private static final String PINKYTORQUEKEY = "pinky_torque";
	private static final String THUMBTORQUEKEY = "thumb_torque";

	private final ProgramAPIProvider apiProvider;
	private final TorqueControlDaemonProgramNodeView view;
	private final DataModel model;
	private final XmlRpcMyDaemonInterface daemonStatusMonitor;
	private final KeyboardInputFactory keyboardInputFactory;

	public TorqueControlDaemonProgramNodeContribution(ProgramAPIProvider apiProvider,
										   TorqueControlDaemonProgramNodeView view,
										   DataModel model) {
		keyboardInputFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory();
		this.apiProvider = apiProvider;
		this.view = view;
		this.model = model;
		this.daemonStatusMonitor = getInstallation().getDaemonStatusMonitor();

	}

	@Override
	public void openView() {
		// view.setTorqueValueText(getTorqueValue());
		view.setIndexInitTorqueValueText(getIndexInitTorqueValue());
		view.setMiddleInitTorqueValueText(getMiddleInitTorqueValue());
		view.setRingInitTorqueValueText(getRingInitTorqueValue());
		view.setPinkyInitTorqueValueText(getPinkyInitTorqueValue());
		view.setThumbInitTorqueValueText(getThumbInitTorqueValue());
		view.setThumbRotInitTorqueValueText(getThumbRotInitTorqueValue());
		view.setIndexTorqueValueText(getIndexTorqueValue());
		view.setMiddleTorqueValueText(getMiddleTorqueValue());
		view.setRingTorqueValueText(getRingTorqueValue());
		view.setPinkyTorqueValueText(getPinkyTorqueValue());
		view.setThumbTorqueValueText(getThumbTorqueValue());
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
		return "Torque Control Daemon: ";
	}

	@Override
	public boolean isDefined() {
		return daemonStatusMonitor.isDaemonReachable()
		&& !getIndexInitTorqueValue().isEmpty()
		 && !getMiddleInitTorqueValue().isEmpty()
		  && !getRingInitTorqueValue().isEmpty()
		   && !getPinkyInitTorqueValue().isEmpty()
		    && !getThumbInitTorqueValue().isEmpty()
			 && !getThumbRotInitTorqueValue().isEmpty()
			  && !getIndexTorqueValue().isEmpty()
			   && !getMiddleTorqueValue().isEmpty()
			    && !getRingTorqueValue().isEmpty()
				 && !getPinkyTorqueValue().isEmpty()
				  && !getThumbTorqueValue().isEmpty();
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		// Interact with the daemon process through XML-RPC calls
		// Note, alternatively plain sockets can be used.

		writer.assign("TCdaemon_init_position", 
		getInstallation().getXMLRPCVariable() + ".set_init_position(\""
		+ getIndexInitTorqueValue() + "\", \"" 
		+ getMiddleInitTorqueValue() + "\", \"" 
		+ getRingInitTorqueValue() + "\", \"" 
		+ getPinkyInitTorqueValue() + "\", \"" 
		+ getThumbInitTorqueValue() + "\", \"" 
		+ getThumbRotInitTorqueValue() + "\")");
		writer.assign("TCdaemon_torque", 
		getInstallation().getXMLRPCVariable() + ".set_torque(\""
		+ getIndexTorqueValue() + "\", \"" 
		+ getMiddleTorqueValue() + "\", \"" 
		+ getRingTorqueValue() + "\", \"" 
		+ getPinkyTorqueValue() + "\", \"" 
		+ getThumbTorqueValue() + "\")");
		writer.assign("TCdaemon_motor_command_flag", getInstallation().getXMLRPCVariable() + ".send_motor_commands()");
		writer.appendLine("popup(\"Finger Torques Sent!\", title=\"Torque Control\", blocking=True)");
		writer.writeChildren();
	}

	public KeyboardTextInput getTorqueInputForTextField() {
		KeyboardTextInput keyboardTextInput = keyboardInputFactory.createStringKeyboardInput();
		keyboardTextInput.setInitialValue(getTorqueValue());
		return keyboardTextInput;
	}

	public KeyboardTextInput getIndexInitInputForTextField() {
		KeyboardTextInput keyboardTextInput = keyboardInputFactory.createStringKeyboardInput();
		keyboardTextInput.setInitialValue(getIndexInitTorqueValue());
		return keyboardTextInput;
	}

	public KeyboardTextInput getMiddleInitInputForTextField() {
		KeyboardTextInput keyboardTextInput = keyboardInputFactory.createStringKeyboardInput();
		keyboardTextInput.setInitialValue(getMiddleInitTorqueValue());
		return keyboardTextInput;
	}

	public KeyboardTextInput getRingInitInputForTextField() {
		KeyboardTextInput keyboardTextInput = keyboardInputFactory.createStringKeyboardInput();
		keyboardTextInput.setInitialValue(getRingInitTorqueValue());
		return keyboardTextInput;
	}

	public KeyboardTextInput getPinkyInitInputForTextField() {
		KeyboardTextInput keyboardTextInput = keyboardInputFactory.createStringKeyboardInput();
		keyboardTextInput.setInitialValue(getPinkyInitTorqueValue());
		return keyboardTextInput;
	}

	public KeyboardTextInput getThumbInitInputForTextField() {
		KeyboardTextInput keyboardTextInput = keyboardInputFactory.createStringKeyboardInput();
		keyboardTextInput.setInitialValue(getThumbInitTorqueValue());
		return keyboardTextInput;
	}

	public KeyboardTextInput getThumbRotInitInputForTextField() {
		KeyboardTextInput keyboardTextInput = keyboardInputFactory.createStringKeyboardInput();
		keyboardTextInput.setInitialValue(getThumbRotInitTorqueValue());
		return keyboardTextInput;
	}

	public KeyboardTextInput getIndexTorqueInputForTextField() {
		KeyboardTextInput keyboardTextInput = keyboardInputFactory.createStringKeyboardInput();
		keyboardTextInput.setInitialValue(getIndexTorqueValue());
		return keyboardTextInput;
	}

	public KeyboardTextInput getMiddleTorqueInputForTextField() {
		KeyboardTextInput keyboardTextInput = keyboardInputFactory.createStringKeyboardInput();
		keyboardTextInput.setInitialValue(getMiddleTorqueValue());
		return keyboardTextInput;
	}

	public KeyboardTextInput getRingTorqueInputForTextField() {
		KeyboardTextInput keyboardTextInput = keyboardInputFactory.createStringKeyboardInput();
		keyboardTextInput.setInitialValue(getRingTorqueValue());
		return keyboardTextInput;
	}

	public KeyboardTextInput getPinkyTorqueInputForTextField() {
		KeyboardTextInput keyboardTextInput = keyboardInputFactory.createStringKeyboardInput();
		keyboardTextInput.setInitialValue(getPinkyTorqueValue());
		return keyboardTextInput;
	}

	public KeyboardTextInput getThumbTorqueInputForTextField() {
		KeyboardTextInput keyboardTextInput = keyboardInputFactory.createStringKeyboardInput();
		keyboardTextInput.setInitialValue(getThumbTorqueValue());
		return keyboardTextInput;
	}





	// public KeyboardInputCallback<String> getCallbackForTorqueTextField() {
	// 	return new KeyboardInputCallback<String>() {
	// 		@Override
	// 		public void onOk(String value) {
	// 			setTorqueValue(value);
	// 			view.setTorqueValueText(value);
	// 		}
	// 	};
	// }

	public KeyboardInputCallback<String> getCallbackForIndexInitTextField() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				setIndexInitTorqueValue(value);
				view.setIndexInitTorqueValueText(value);
			}
		};
	}

	public KeyboardInputCallback<String> getCallbackForMiddleInitTextField() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				setMiddleInitTorqueValue(value);
				view.setMiddleInitTorqueValueText(value);
			}
		};
	}

	public KeyboardInputCallback<String> getCallbackForRingInitTextField() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				setRingInitTorqueValue(value);
				view.setRingInitTorqueValueText(value);
			}
		};
	}

	public KeyboardInputCallback<String> getCallbackForPinkyInitTextField() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				setPinkyInitTorqueValue(value);
				view.setPinkyInitTorqueValueText(value);
			}
		};
	}

	public KeyboardInputCallback<String> getCallbackForThumbInitTextField() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				setThumbInitTorqueValue(value);
				view.setThumbInitTorqueValueText(value);
			}
		};
	}

	public KeyboardInputCallback<String> getCallbackForThumbRotInitTextField() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				setThumbRotInitTorqueValue(value);
				view.setThumbRotInitTorqueValueText(value);
			}
		};
	}

	public KeyboardInputCallback<String> getCallbackForIndexTorqueTextField() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				setIndexTorqueValue(value);
				view.setIndexTorqueValueText(value);
			}
		};
	}

	public KeyboardInputCallback<String> getCallbackForMiddleTorqueTextField() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				setMiddleTorqueValue(value);
				view.setMiddleTorqueValueText(value);
			}
		};
	}

	public KeyboardInputCallback<String> getCallbackForRingTorqueTextField() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				setRingTorqueValue(value);
				view.setRingTorqueValueText(value);
			}
		};
	}

	public KeyboardInputCallback<String> getCallbackForPinkyTorqueTextField() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				setPinkyTorqueValue(value);
				view.setPinkyTorqueValueText(value);
			}
		};
	}

	public KeyboardInputCallback<String> getCallbackForThumbTorqueTextField() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				setThumbTorqueValue(value);
				view.setThumbTorqueValueText(value);
			}
		};
	}




	private String getTorqueValue() {
		return model.get(TORQUEKEY, "");
	}

	private String getIndexInitTorqueValue() {
		return model.get(INDEXINITTORQUEKEY, "");
	}

	private String getMiddleInitTorqueValue() {
		return model.get(MIDDLEINITTORQUEKEY, "");
	}

	private String getRingInitTorqueValue() {
		return model.get(RINGINITTORQUEKEY, "");
	}

	private String getPinkyInitTorqueValue() {
		return model.get(PINKYINITTORQUEKEY, "");
	}

	private String getThumbInitTorqueValue() {
		return model.get(THUMBINITTORQUEKEY, "");
	}

	private String getThumbRotInitTorqueValue() {
		return model.get(THUMBROTINITTORQUEKEY, "");
	}

	private String getIndexTorqueValue() {
		return model.get(INDEXTORQUEKEY, "");
	}

	private String getMiddleTorqueValue() {
		return model.get(MIDDLETORQUEKEY, "");
	}

	private String getRingTorqueValue() {
		return model.get(RINGTORQUEKEY, "");
	}

	private String getPinkyTorqueValue() {
		return model.get(PINKYTORQUEKEY, "");
	}

	private String getThumbTorqueValue() {
		return model.get(THUMBTORQUEKEY, "");
	}


	private void setIndexInitTorqueValue(String index_init_torque_value) {
		if ("".equals(index_init_torque_value)) {
			model.remove(INDEXINITTORQUEKEY);
		} else {
			model.set(INDEXINITTORQUEKEY, index_init_torque_value);
		}
	}

	private void setMiddleInitTorqueValue(String middle_init_torque_value) {
		if ("".equals(middle_init_torque_value)) {
			model.remove(MIDDLEINITTORQUEKEY);
		} else {
			model.set(MIDDLEINITTORQUEKEY, middle_init_torque_value);
		}
	}

	private void setRingInitTorqueValue(String ring_init_torque_value) {
		if ("".equals(ring_init_torque_value)) {
			model.remove(RINGINITTORQUEKEY);
		} else {
			model.set(RINGINITTORQUEKEY, ring_init_torque_value);
		}
	}

	private void setPinkyInitTorqueValue(String pinky_init_torque_value) {
		if ("".equals(pinky_init_torque_value)) {
			model.remove(PINKYINITTORQUEKEY);
		} else {
			model.set(PINKYINITTORQUEKEY, pinky_init_torque_value);
		}
	}

	private void setThumbInitTorqueValue(String thumb_init_torque_value) {
		if ("".equals(thumb_init_torque_value)) {
			model.remove(THUMBINITTORQUEKEY);
		} else {
			model.set(THUMBINITTORQUEKEY, thumb_init_torque_value);
		}
	}

	private void setThumbRotInitTorqueValue(String thumbrot_init_torque_value) {
		if ("".equals(thumbrot_init_torque_value)) {
			model.remove(THUMBROTINITTORQUEKEY);
		} else {
			model.set(THUMBROTINITTORQUEKEY, thumbrot_init_torque_value);
		}
	}

	private void setIndexTorqueValue(String index_torque_value) {
		if ("".equals(index_torque_value)) {
			model.remove(INDEXTORQUEKEY);
		} else {
			model.set(INDEXTORQUEKEY, index_torque_value);
		}
	}

	private void setMiddleTorqueValue(String middle_torque_value) {
		if ("".equals(middle_torque_value)) {
			model.remove(MIDDLETORQUEKEY);
		} else {
			model.set(MIDDLETORQUEKEY, middle_torque_value);
		}
	}

	private void setRingTorqueValue(String ring_torque_value) {
		if ("".equals(ring_torque_value)) {
			model.remove(RINGTORQUEKEY);
		} else {
			model.set(RINGTORQUEKEY, ring_torque_value);
		}
	}

	private void setPinkyTorqueValue(String pinky_torque_value) {
		if ("".equals(pinky_torque_value)) {
			model.remove(PINKYTORQUEKEY);
		} else {
			model.set(PINKYTORQUEKEY, pinky_torque_value);
		}
	}

	private void setThumbTorqueValue(String thumb_torque_value) {
		if ("".equals(thumb_torque_value)) {
			model.remove(THUMBTORQUEKEY);
		} else {
			model.set(THUMBTORQUEKEY, thumb_torque_value);
		}
	}




	private TorqueControlDaemonInstallationNodeContribution getInstallation() {
		return apiProvider.getProgramAPI().getInstallationNode(TorqueControlDaemonInstallationNodeContribution.class);
	}
}
