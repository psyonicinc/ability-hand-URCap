package com.ur.urcap.psyonic.abilityhand.positioncontrol.impl;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PositionControlDaemonProgramNodeView implements SwingProgramNodeView<PositionControlDaemonProgramNodeContribution> {

	private final Style style;
	private JTextField indexInputField;
	private JTextField middleInputField;
	private JTextField ringInputField;
	private JTextField pinkyInputField;
	private JTextField thumbInputField;
	private JTextField thumbRotInputField;

	private JLabel titlePreviewLabel;
	private JLabel messagePreviewLabel;

	public PositionControlDaemonProgramNodeView(Style style) {
		this.style = style;
	}

	@Override
	public void buildUI(JPanel panel, ContributionProvider<PositionControlDaemonProgramNodeContribution> provider) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(createInfo());
		panel.add(createVerticalSpacing());

		panel.add(createIndexInput(provider));
		panel.add(createVerticalSpacing(style.getVerticalSpacing()));

		panel.add(createMiddleInput(provider));
		panel.add(createVerticalSpacing(style.getVerticalSpacing()));

		panel.add(createRingInput(provider));
		panel.add(createVerticalSpacing(style.getVerticalSpacing()));

		panel.add(createPinkyInput(provider));
		panel.add(createVerticalSpacing(style.getVerticalSpacing()));

		panel.add(createThumbInput(provider));
		panel.add(createVerticalSpacing(style.getVerticalSpacing()));

		panel.add(createThumbRotInput(provider));
		panel.add(createVerticalSpacing(style.getVerticalSpacing()));
		// panel.add(createPreviewInfo());
	}

	private Box createInfo() {
		Box infoBox = Box.createVerticalBox();
		infoBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		infoBox.add(new JLabel("This installation node allows the position control of each motor in the Psyonic Ability Hand. \\nThere are 6 motors. One for each finger flexion and one to control the thumb rotation. \nFor each flexion, the values are between 0-100. \nFor the thumb rotation, the values are between -100-0.\""));
		return infoBox;
	}

	private Box createIndexInput(final ContributionProvider<PositionControlDaemonProgramNodeContribution> provider) {
		Box inputBox = Box.createHorizontalBox();
		inputBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		inputBox.add(new JLabel("Index Flexion:"));
		inputBox.add(createHorizontalSpacing());

		indexInputField = new JTextField();
		indexInputField.setFocusable(false);
		indexInputField.setPreferredSize(style.getInputfieldSize());
		indexInputField.setMaximumSize(indexInputField.getPreferredSize());
		indexInputField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = provider.get().getIndexInputForTextField();
				keyboardInput.show(indexInputField, provider.get().getCallbackForIndexTextField());
			}
		});
		inputBox.add(indexInputField);

		return inputBox;
	}

	private Box createMiddleInput(final ContributionProvider<PositionControlDaemonProgramNodeContribution> provider) {
		Box inputBox = Box.createHorizontalBox();
		inputBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		inputBox.add(new JLabel("Middle Flexion:"));
		inputBox.add(createHorizontalSpacing());

		middleInputField = new JTextField();
		middleInputField.setFocusable(false);
		middleInputField.setPreferredSize(style.getInputfieldSize());
		middleInputField.setMaximumSize(middleInputField.getPreferredSize());
		middleInputField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = provider.get().getMiddleInputForTextField();
				keyboardInput.show(middleInputField, provider.get().getCallbackForMiddleTextField());
			}
		});
		inputBox.add(middleInputField);

		return inputBox;
	}

	private Box createRingInput(final ContributionProvider<PositionControlDaemonProgramNodeContribution> provider) {
		Box inputBox = Box.createHorizontalBox();
		inputBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		inputBox.add(new JLabel("Ring Flexion:"));
		inputBox.add(createHorizontalSpacing());

		ringInputField = new JTextField();
		ringInputField.setFocusable(false);
		ringInputField.setPreferredSize(style.getInputfieldSize());
		ringInputField.setMaximumSize(ringInputField.getPreferredSize());
		ringInputField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = provider.get().getRingInputForTextField();
				keyboardInput.show(ringInputField, provider.get().getCallbackForRingTextField());
			}
		});
		inputBox.add(ringInputField);

		return inputBox;
	}

	private Box createPinkyInput(final ContributionProvider<PositionControlDaemonProgramNodeContribution> provider) {
		Box inputBox = Box.createHorizontalBox();
		inputBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		inputBox.add(new JLabel("Pinky Flexion:"));
		inputBox.add(createHorizontalSpacing());

		pinkyInputField = new JTextField();
		pinkyInputField.setFocusable(false);
		pinkyInputField.setPreferredSize(style.getInputfieldSize());
		pinkyInputField.setMaximumSize(pinkyInputField.getPreferredSize());
		pinkyInputField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = provider.get().getPinkyInputForTextField();
				keyboardInput.show(pinkyInputField, provider.get().getCallbackForPinkyTextField());
			}
		});
		inputBox.add(pinkyInputField);

		return inputBox;
	}

	private Box createThumbInput(final ContributionProvider<PositionControlDaemonProgramNodeContribution> provider) {
		Box inputBox = Box.createHorizontalBox();
		inputBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		inputBox.add(new JLabel("Thumb Flexion:"));
		inputBox.add(createHorizontalSpacing());

		thumbInputField = new JTextField();
		thumbInputField.setFocusable(false);
		thumbInputField.setPreferredSize(style.getInputfieldSize());
		thumbInputField.setMaximumSize(thumbInputField.getPreferredSize());
		thumbInputField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = provider.get().getThumbInputForTextField();
				keyboardInput.show(thumbInputField, provider.get().getCallbackForThumbTextField());
			}
		});
		inputBox.add(thumbInputField);

		return inputBox;
	}

	private Box createThumbRotInput(final ContributionProvider<PositionControlDaemonProgramNodeContribution> provider) {
		Box inputBox = Box.createHorizontalBox();
		inputBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		inputBox.add(new JLabel("Thumb Rotation:"));
		inputBox.add(createHorizontalSpacing());

		thumbRotInputField = new JTextField();
		thumbRotInputField.setFocusable(false);
		thumbRotInputField.setPreferredSize(style.getInputfieldSize());
		thumbRotInputField.setMaximumSize(thumbRotInputField.getPreferredSize());
		thumbRotInputField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = provider.get().getThumbRotInputForTextField();
				keyboardInput.show(thumbRotInputField, provider.get().getCallbackForThumbRotTextField());
			}
		});
		inputBox.add(thumbRotInputField);

		return inputBox;
	}


	private Component createHorizontalSpacing() {
		return Box.createRigidArea(new Dimension(style.getHorizontalSpacing(), 0));
	}

	private Component createVerticalSpacing() {
		return createVerticalSpacing(style.getVerticalSpacing());
	}

	private Component createVerticalSpacing(int space) {
		return Box.createRigidArea(new Dimension(0, space));
	}

	public void setIndexPositionValueText(String t) {
		indexInputField.setText(t);
	}

	public void setMiddlePositionValueText(String t) {
		middleInputField.setText(t);
	}

	public void setRingPositionValueText(String t) {
		ringInputField.setText(t);
	}

	public void setPinkyPositionValueText(String t) {
		pinkyInputField.setText(t);
	}

	public void setThumbPositionValueText(String t) {
		thumbInputField.setText(t);
	}

	public void setThumbRotPositionValueText(String t) {
		thumbRotInputField.setText(t);
	}

	public void setTitlePreview(String title) {
		titlePreviewLabel.setText(title);
	}

	public void setMessagePreview(String message) {
		messagePreviewLabel.setText(message);
	}
}
