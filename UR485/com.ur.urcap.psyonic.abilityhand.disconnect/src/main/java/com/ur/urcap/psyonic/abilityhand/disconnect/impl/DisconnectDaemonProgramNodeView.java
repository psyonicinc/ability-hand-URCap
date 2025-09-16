package com.ur.urcap.psyonic.abilityhand.disconnect.impl;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DisconnectDaemonProgramNodeView implements SwingProgramNodeView<DisconnectDaemonProgramNodeContribution> {

	private final Style style;

	private JLabel titlePreviewLabel;
	private JLabel messagePreviewLabel;

	public DisconnectDaemonProgramNodeView(Style style) {
		this.style = style;
	}

	@Override
	public void buildUI(JPanel panel, ContributionProvider<DisconnectDaemonProgramNodeContribution> provider) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(createInfo());
		panel.add(createVerticalSpacing());
	}

	private Box createInfo() {
		Box infoBox = Box.createVerticalBox();
		infoBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		infoBox.add(new JLabel("This node allows the communication to the Psyonic Ability Hand. ALWAYS PUT FIRST!"));
		return infoBox;
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

	public void setTitlePreview(String title) {
		titlePreviewLabel.setText(title);
	}

	public void setMessagePreview(String message) {
		messagePreviewLabel.setText(message);
	}
}
