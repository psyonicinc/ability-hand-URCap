package com.ur.urcap.psyonic.abilityhand.positioncontrol.impl;

import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeView;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PositionControlDaemonInstallationNodeView implements SwingInstallationNodeView<PositionControlDaemonInstallationNodeContribution> {

	private final Style style;
	private JButton connectButton;
	private JButton disconnectButton;
	// private JLabel statusLabel;

	public PositionControlDaemonInstallationNodeView(Style style) {
		this.style = style;
	}

	@Override
	public void buildUI(JPanel panel, PositionControlDaemonInstallationNodeContribution contribution) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(createInfo());
		panel.add(createVerticalSpacing());

		// panel.add(createInput(contribution));
		// panel.add(createVerticalSpacing(style.getLargeVerticalSpacing()));

		panel.add(createConnectDisconnectButtons(contribution));
		panel.add(createVerticalSpacing());

		// panel.add(createStatusInfo());
	}

	private Box createInfo() {
		Box infoBox = Box.createVerticalBox();
		infoBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		JTextPane pane = new JTextPane();
		pane.setBorder(BorderFactory.createEmptyBorder());
		SimpleAttributeSet attributeSet = new SimpleAttributeSet();
		StyleConstants.setLineSpacing(attributeSet, 0.5f);
		StyleConstants.setLeftIndent(attributeSet, 0f);
		pane.setParagraphAttributes(attributeSet, false);
		pane.setText("This installation node allows the position control of each motor in the Psyonic Ability Hand. \nThere are 6 motors. One for each finger flexion and one to control the thumb rotation. \nFor each flexion, the values are between 0-100. \nFor the thumb rotation, the values are between -100-0.");
		pane.setEditable(false);
		pane.setMaximumSize(pane.getPreferredSize());
		pane.setBackground(infoBox.getBackground());
		infoBox.add(pane);
		return infoBox;
	}

	private Box createConnectDisconnectButtons(final PositionControlDaemonInstallationNodeContribution contribution) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);

		connectButton = new JButton("Connect");
		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				contribution.onConnectClick();
			}
		});
		box.add(connectButton);

		box.add(createHorizontalSpacing());

		disconnectButton = new JButton("Disconnect");
		disconnectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				contribution.onDisconnectClick();
			}
		});
		box.add(disconnectButton);

		return box;
	}

	public void setConnectButtonEnabled(boolean enabled) {
		connectButton.setEnabled(enabled);
	}

	public void setDisconnectButtonEnabled(boolean enabled) {
		disconnectButton.setEnabled(enabled);
	}

	private Component createVerticalSpacing(int space) {
		return Box.createRigidArea(new Dimension(0, space));
	}

	private Component createVerticalSpacing() {
		return createVerticalSpacing(style.getVerticalSpacing());
	}

	private Component createHorizontalSpacing() {
		return Box.createRigidArea(new Dimension(style.getHorizontalSpacing(), 0));
	}
}
