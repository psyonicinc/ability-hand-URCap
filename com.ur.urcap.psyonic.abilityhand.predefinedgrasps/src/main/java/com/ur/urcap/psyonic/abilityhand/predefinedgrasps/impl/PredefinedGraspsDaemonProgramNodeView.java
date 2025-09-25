package com.ur.urcap.psyonic.abilityhand.predefinedgrasps.impl;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class PredefinedGraspsDaemonProgramNodeView implements SwingProgramNodeView<PredefinedGraspsDaemonProgramNodeContribution> {
	
	private static final String GRASP_PLACEHOLDER = "";

	private final Style style;

	private JLabel errorLabel;

	private JSlider speedSlider;

	private ContributionProvider<PredefinedGraspsDaemonProgramNodeContribution> contributionProvider;

	private JComboBox graspsComboBox;

	public PredefinedGraspsDaemonProgramNodeView(Style style) {
		this.style = style;
		
	}

	@Override
	public void buildUI(JPanel panel, final ContributionProvider<PredefinedGraspsDaemonProgramNodeContribution> provider) {
		this.contributionProvider = provider;
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel("Select the desired grasp"), Component.LEFT_ALIGNMENT);
		panel.add(createVerticalSpacing());
		panel.add(createGraspSelectionSection());

		panel.add(createVerticalSpacing());

		panel.add(createSliderBox("SPEED", provider));

		errorLabel = new JLabel();
		errorLabel.setForeground(java.awt.Color.RED);
		panel.add(errorLabel);
	}

	public void updateView() {
		updateGraspCombobox();
	}

	private Box createGraspSelectionSection() {
		Box section = Box.createHorizontalBox();
		section.setAlignmentX(Component.LEFT_ALIGNMENT);

		graspsComboBox = createComboBox(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox comboBox = (JComboBox) e.getSource();

				String selectedGrasp = (String) comboBox.getSelectedItem();

				contributionProvider.get().onGraspSelected(selectedGrasp);

				updateGraspCombobox();
			}
		});


		section.add(graspsComboBox);

		return section;
	}

	private Box createSliderBox(String label, final ContributionProvider<PredefinedGraspsDaemonProgramNodeContribution> provider) {
        Box box = Box.createHorizontalBox();
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.add(new JLabel(label + ":"));
        box.add(createVerticalSpacing(10));

        JSlider slider = new JSlider(0, 100, 0);
        slider.setPreferredSize(new Dimension(200, 30));

        JLabel valueLabel = new JLabel(String.valueOf(slider.getValue()));
        valueLabel.setPreferredSize(new Dimension(40, 30));
        box.add(valueLabel);

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = slider.getValue();
                valueLabel.setText(String.valueOf(value));
                String key = label.toLowerCase().replace(" ", "_");
                provider.get().updatePosition(key, value);
            }
        });

		speedSlider = slider;

        box.add(slider);
        return box;
    }

    public void updateSliders(int speed) {
        speedSlider.setValue(speed);

    }

	private void updateGraspCombobox() {
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		PredefinedGraspsDaemonProgramNodeContribution contribution = contributionProvider.get();


		model.addElement(GRASP_PLACEHOLDER);


		String[] grasps = { "Open", "Power", "Key", "Pinch", "Tripod Opened", "Sign of the Horns", "Cylinder", "Mouse Grasp", "Power/Key Switch", "Point", "Rude...", "Hook", "Relax", "Sleeve", "Peace", "Tripod Closed", "Hang Loose", "Handshake", "Fixed Pinch"};
		
		for (String grasp : grasps) {
			model.addElement(grasp);
		}

		String selected = contribution.getSelectedGrasp();
		if (!selected.isEmpty()) {
			model.setSelectedItem(selected);
			
		} else {
			model.setSelectedItem(GRASP_PLACEHOLDER);
		}

		

		graspsComboBox.setModel(model);
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

	public void showError(String message) {
        errorLabel.setText(message);
    }


	
	public JComboBox createComboBox(final ActionListener actionListener) {
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		model.addElement(GRASP_PLACEHOLDER);
		String[] grasps = { "Open", "Power", "Key", "Pinch", "Tripod Opened", "Sign of the Horns", "Cylinder", "Mouse Grasp", "Power/Key Switch", "Point", "Rude...", "Hook", "Relax", "Sleeve", "Peace", "Tripod Closed", "Hang Loose", "Handshake", "Fixed Pinch"};
		
		for (String grasp : grasps) {
			model.addElement(grasp);
		}
		
		JComboBox jComboBox = new JComboBox(model);

		jComboBox.setPreferredSize(style.getInputfieldSize());
		jComboBox.setMaximumSize(style.getInputfieldSize());
		jComboBox.setMinimumSize(style.getInputfieldSize());

		jComboBox.addActionListener(actionListener);

		return jComboBox;
	}

}
