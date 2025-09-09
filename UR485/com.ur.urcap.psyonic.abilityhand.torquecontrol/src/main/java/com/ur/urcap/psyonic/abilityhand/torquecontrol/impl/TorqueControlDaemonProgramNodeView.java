package com.ur.urcap.psyonic.abilityhand.torquecontrol.impl;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TorqueControlDaemonProgramNodeView implements SwingProgramNodeView<TorqueControlDaemonProgramNodeContribution> {

	private final Style style;
	private JTextField torqueInputField;
	private JTextField indexInitInputField;
	private JTextField middleInitInputField;
	private JTextField ringInitInputField;
	private JTextField pinkyInitInputField;
	private JTextField thumbInitInputField;
	private JTextField thumbRotInitInputField;
	private JTextField indexTorqueInputField;
	private JTextField middleTorqueInputField;
	private JTextField ringTorqueInputField;
	private JTextField pinkyTorqueInputField;
	private JTextField thumbTorqueInputField;
	private JTextField thumbRotTorqueInputField;

	private JLabel titlePreviewLabel;
	private JLabel messagePreviewLabel;

	public TorqueControlDaemonProgramNodeView(Style style) {
		this.style = style;
	}

	@Override
	public void buildUI(JPanel panel, ContributionProvider<TorqueControlDaemonProgramNodeContribution> provider) {

		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);
		
		gbc.weightx = 0.5;

		// gbc.gridx = 0; gbc.gridy = 0;
		// gbc.gridwidth = 1;
		// panel.add(new JLabel("Grip Strength (Torque)"), gbc);

		// gbc.gridx = 1; gbc.gridy = 0;
		// gbc.gridwidth = 2;
		// panel.add(createTorqueInput(provider), gbc);

		gbc.gridx = 0; gbc.gridy = 0;
		gbc.gridwidth = 1;
		panel.add(new JLabel(""), gbc);
		gbc.gridx = 1; gbc.gridy = 0;
		panel.add(new JLabel("Initial Position"), gbc);
		gbc.gridx = 2; gbc.gridy = 0;
		panel.add(new JLabel("Torque per Finger"), gbc);

		int row = 1;

		gbc.gridx = 0;
		gbc.gridy = row++;
		panel.add(new JLabel("Index Joint"), gbc);

		gbc.gridx = 0;
		gbc.gridy = row++;
		panel.add(new JLabel("Middle Joint"), gbc);

		gbc.gridx = 0;
		gbc.gridy = row++;
		panel.add(new JLabel("Ring Joint"), gbc);

		gbc.gridx = 0;
		gbc.gridy = row++;
		panel.add(new JLabel("Pinky Joint"), gbc);

		gbc.gridx = 0;
		gbc.gridy = row++;
		panel.add(new JLabel("Thumb Joint"), gbc);

		gbc.gridx = 0;
		gbc.gridy = row++;
		panel.add(new JLabel("Thumb Rotation"), gbc);


		row = 1;

		gbc.gridx = 1;
		gbc.gridy = row++;
		panel.add(createIndexInitInput(provider), gbc);

		gbc.gridx = 1;
		gbc.gridy = row++;
		panel.add(createMiddleInitInput(provider), gbc);

		gbc.gridx = 1;
		gbc.gridy = row++;
		panel.add(createRingInitInput(provider), gbc);

		gbc.gridx = 1;
		gbc.gridy = row++;
		panel.add(createPinkyInitInput(provider), gbc);

		gbc.gridx = 1;
		gbc.gridy = row++;
		panel.add(createThumbInitInput(provider), gbc);

		gbc.gridx = 1;
		gbc.gridy = row++;
		panel.add(createThumbRotInitInput(provider), gbc);

		row = 1;

		gbc.gridx = 2;
		gbc.gridy = row++;
		panel.add(createIndexTorqueInput(provider), gbc);

		gbc.gridx = 2;
		gbc.gridy = row++;
		panel.add(createMiddleTorqueInput(provider), gbc);

		gbc.gridx = 2;
		gbc.gridy = row++;
		panel.add(createRingTorqueInput(provider), gbc);

		gbc.gridx = 2;
		gbc.gridy = row++;
		panel.add(createPinkyTorqueInput(provider), gbc);

		gbc.gridx = 2;
		gbc.gridy = row++;
		panel.add(createThumbTorqueInput(provider), gbc);

	}

	// private Box createTorqueInput(final ContributionProvider<TorqueControlDaemonProgramNodeContribution> provider) {
	// 	Box inputBox = Box.createHorizontalBox();
	// 	inputBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		
	// 	torqueInputField = new JTextField();
	// 	torqueInputField.setFocusable(false);
	// 	torqueInputField.setPreferredSize(style.getInputfieldSize());
	// 	torqueInputField.setMaximumSize(torqueInputField.getPreferredSize());
	// 	torqueInputField.addMouseListener(new MouseAdapter() {
	// 		@Override
	// 		public void mousePressed(MouseEvent e) {
	// 			KeyboardTextInput keyboardInput = provider.get().getTorqueInputForTextField();
	// 			keyboardInput.show(torqueInputField, provider.get().getCallbackForTorqueTextField());
	// 		}
	// 	});
		
	// 	inputBox.add(torqueInputField);

	// 	return inputBox;
	// }


	private Box createIndexInitInput(final ContributionProvider<TorqueControlDaemonProgramNodeContribution> provider) {
		Box inputBox = Box.createHorizontalBox();
		inputBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		
		indexInitInputField = new JTextField();
		indexInitInputField.setFocusable(false);
		indexInitInputField.setPreferredSize(style.getInputfieldSize());
		indexInitInputField.setMaximumSize(indexInitInputField.getPreferredSize());
		indexInitInputField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = provider.get().getIndexInitInputForTextField();
				keyboardInput.show(indexInitInputField, provider.get().getCallbackForIndexInitTextField());
			}
		});
		inputBox.add(indexInitInputField);

		return inputBox;
	}

	private Box createMiddleInitInput(final ContributionProvider<TorqueControlDaemonProgramNodeContribution> provider) {
		Box inputBox = Box.createHorizontalBox();
		inputBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		
		middleInitInputField = new JTextField();
		middleInitInputField.setFocusable(false);
		middleInitInputField.setPreferredSize(style.getInputfieldSize());
		middleInitInputField.setMaximumSize(middleInitInputField.getPreferredSize());
		middleInitInputField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = provider.get().getMiddleInitInputForTextField();
				keyboardInput.show(middleInitInputField, provider.get().getCallbackForMiddleInitTextField());
			}
		});
		inputBox.add(middleInitInputField);

		return inputBox;
	}

	private Box createRingInitInput(final ContributionProvider<TorqueControlDaemonProgramNodeContribution> provider) {
		Box inputBox = Box.createHorizontalBox();
		inputBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		
		ringInitInputField = new JTextField();
		ringInitInputField.setFocusable(false);
		ringInitInputField.setPreferredSize(style.getInputfieldSize());
		ringInitInputField.setMaximumSize(ringInitInputField.getPreferredSize());
		ringInitInputField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = provider.get().getRingInitInputForTextField();
				keyboardInput.show(ringInitInputField, provider.get().getCallbackForRingInitTextField());
			}
		});
		inputBox.add(ringInitInputField);

		return inputBox;
	}

	private Box createPinkyInitInput(final ContributionProvider<TorqueControlDaemonProgramNodeContribution> provider) {
		Box inputBox = Box.createHorizontalBox();
		inputBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		
		pinkyInitInputField = new JTextField();
		pinkyInitInputField.setFocusable(false);
		pinkyInitInputField.setPreferredSize(style.getInputfieldSize());
		pinkyInitInputField.setMaximumSize(pinkyInitInputField.getPreferredSize());
		pinkyInitInputField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = provider.get().getPinkyInitInputForTextField();
				keyboardInput.show(pinkyInitInputField, provider.get().getCallbackForPinkyInitTextField());
			}
		});
		inputBox.add(pinkyInitInputField);

		return inputBox;
	}

	private Box createThumbInitInput(final ContributionProvider<TorqueControlDaemonProgramNodeContribution> provider) {
		Box inputBox = Box.createHorizontalBox();
		inputBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		
		thumbInitInputField = new JTextField();
		thumbInitInputField.setFocusable(false);
		thumbInitInputField.setPreferredSize(style.getInputfieldSize());
		thumbInitInputField.setMaximumSize(thumbInitInputField.getPreferredSize());
		thumbInitInputField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = provider.get().getThumbInitInputForTextField();
				keyboardInput.show(thumbInitInputField, provider.get().getCallbackForThumbInitTextField());
			}
		});
		inputBox.add(thumbInitInputField);

		return inputBox;
	}

	private Box createThumbRotInitInput(final ContributionProvider<TorqueControlDaemonProgramNodeContribution> provider) {
		Box inputBox = Box.createHorizontalBox();
		inputBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		
		thumbRotInitInputField = new JTextField();
		thumbRotInitInputField.setFocusable(false);
		thumbRotInitInputField.setPreferredSize(style.getInputfieldSize());
		thumbRotInitInputField.setMaximumSize(thumbRotInitInputField.getPreferredSize());
		thumbRotInitInputField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = provider.get().getThumbRotInitInputForTextField();
				keyboardInput.show(thumbRotInitInputField, provider.get().getCallbackForThumbRotInitTextField());
			}
		});
		inputBox.add(thumbRotInitInputField);

		return inputBox;
	}

	private Box createIndexTorqueInput(final ContributionProvider<TorqueControlDaemonProgramNodeContribution> provider) {
		Box inputBox = Box.createHorizontalBox();
		inputBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		
		indexTorqueInputField = new JTextField();
		indexTorqueInputField.setFocusable(false);
		indexTorqueInputField.setPreferredSize(style.getInputfieldSize());
		indexTorqueInputField.setMaximumSize(indexTorqueInputField.getPreferredSize());
		indexTorqueInputField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = provider.get().getIndexTorqueInputForTextField();
				keyboardInput.show(indexTorqueInputField, provider.get().getCallbackForIndexTorqueTextField());
			}
		});
		inputBox.add(indexTorqueInputField);

		return inputBox;
	}

	private Box createMiddleTorqueInput(final ContributionProvider<TorqueControlDaemonProgramNodeContribution> provider) {
		Box inputBox = Box.createHorizontalBox();
		inputBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		
		middleTorqueInputField = new JTextField();
		middleTorqueInputField.setFocusable(false);
		middleTorqueInputField.setPreferredSize(style.getInputfieldSize());
		middleTorqueInputField.setMaximumSize(middleTorqueInputField.getPreferredSize());
		middleTorqueInputField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = provider.get().getMiddleTorqueInputForTextField();
				keyboardInput.show(middleTorqueInputField, provider.get().getCallbackForMiddleTorqueTextField());
			}
		});
		inputBox.add(middleTorqueInputField);

		return inputBox;
	}

	private Box createRingTorqueInput(final ContributionProvider<TorqueControlDaemonProgramNodeContribution> provider) {
		Box inputBox = Box.createHorizontalBox();
		inputBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		
		ringTorqueInputField = new JTextField();
		ringTorqueInputField.setFocusable(false);
		ringTorqueInputField.setPreferredSize(style.getInputfieldSize());
		ringTorqueInputField.setMaximumSize(ringTorqueInputField.getPreferredSize());
		ringTorqueInputField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = provider.get().getRingTorqueInputForTextField();
				keyboardInput.show(ringTorqueInputField, provider.get().getCallbackForRingTorqueTextField());
			}
		});
		inputBox.add(ringTorqueInputField);

		return inputBox;
	}

	private Box createPinkyTorqueInput(final ContributionProvider<TorqueControlDaemonProgramNodeContribution> provider) {
		Box inputBox = Box.createHorizontalBox();
		inputBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		
		pinkyTorqueInputField = new JTextField();
		pinkyTorqueInputField.setFocusable(false);
		pinkyTorqueInputField.setPreferredSize(style.getInputfieldSize());
		pinkyTorqueInputField.setMaximumSize(pinkyTorqueInputField.getPreferredSize());
		pinkyTorqueInputField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = provider.get().getPinkyTorqueInputForTextField();
				keyboardInput.show(pinkyTorqueInputField, provider.get().getCallbackForPinkyTorqueTextField());
			}
		});
		inputBox.add(pinkyTorqueInputField);

		return inputBox;
	}

	private Box createThumbTorqueInput(final ContributionProvider<TorqueControlDaemonProgramNodeContribution> provider) {
		Box inputBox = Box.createHorizontalBox();
		inputBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		
		thumbTorqueInputField = new JTextField();
		thumbTorqueInputField.setFocusable(false);
		thumbTorqueInputField.setPreferredSize(style.getInputfieldSize());
		thumbTorqueInputField.setMaximumSize(thumbTorqueInputField.getPreferredSize());
		thumbTorqueInputField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = provider.get().getThumbTorqueInputForTextField();
				keyboardInput.show(thumbTorqueInputField, provider.get().getCallbackForThumbTorqueTextField());
			}
		});
		inputBox.add(thumbTorqueInputField);

		return inputBox;
	}

	// private Box createThumbRotTorqueInput(final ContributionProvider<TorqueControlDaemonProgramNodeContribution> provider) {
	// 	Box inputBox = Box.createHorizontalBox();
	// 	inputBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		
	// 	thumbRotTorqueInputField = new JTextField();
	// 	thumbRotTorqueInputField.setFocusable(false);
	// 	thumbRotTorqueInputField.setPreferredSize(style.getInputfieldSize());
	// 	thumbRotTorqueInputField.setMaximumSize(thumbRotTorqueInputField.getPreferredSize());
	// 	thumbRotTorqueInputField.addMouseListener(new MouseAdapter() {
	// 		@Override
	// 		public void mousePressed(MouseEvent e) {
	// 			KeyboardTextInput keyboardInput = provider.get().getThumbRotTorqueInputForTextField();
	// 			keyboardInput.show(thumbRotTorqueInputField, provider.get().getCallbackForThumbRotFinTextField());
	// 		}
	// 	});
	// 	inputBox.add(thumbRotTorqueInputField);

	// 	return inputBox;
	// }


	private Component createHorizontalSpacing() {
		return Box.createRigidArea(new Dimension(style.getHorizontalSpacing(), 0));
	}

	private Component createVerticalSpacing() {
		return createVerticalSpacing(style.getVerticalSpacing());
	}

	private Component createVerticalSpacing(int space) {
		return Box.createRigidArea(new Dimension(0, space));
	}


	public void setTorqueValueText(String t) {
		torqueInputField.setText(t);
	}

	public void setIndexInitTorqueValueText(String t) {
		indexInitInputField.setText(t);
	}

	public void setMiddleInitTorqueValueText(String t) {
		middleInitInputField.setText(t);
	}

	public void setRingInitTorqueValueText(String t) {
		ringInitInputField.setText(t);
	}

	public void setPinkyInitTorqueValueText(String t) {
		pinkyInitInputField.setText(t);
	}

	public void setThumbInitTorqueValueText(String t) {
		thumbInitInputField.setText(t);
	}

	public void setThumbRotInitTorqueValueText(String t) {
		thumbRotInitInputField.setText(t);
	}

	public void setIndexTorqueValueText(String t) {
		indexTorqueInputField.setText(t);
	}

	public void setMiddleTorqueValueText(String t) {
		middleTorqueInputField.setText(t);
	}

	public void setRingTorqueValueText(String t) {
		ringTorqueInputField.setText(t);
	}

	public void setPinkyTorqueValueText(String t) {
		pinkyTorqueInputField.setText(t);
	}

	public void setThumbTorqueValueText(String t) {
		thumbTorqueInputField.setText(t);
	}

	public void setTitlePreview(String title) {
		titlePreviewLabel.setText(title);
	}

	public void setMessagePreview(String message) {
		messagePreviewLabel.setText(message);
	}
}
