package com.ur.urcap.psyonic.abilityhand.impl;

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


public class AbilityHandGripProgramNodeView implements SwingProgramNodeView<AbilityHandGripProgramNodeContribution> {
	
	private final Style style;

	private JLabel errorLabel;

	private JSlider speedSlider;

	private ContributionProvider<AbilityHandGripProgramNodeContribution> contributionProvider;

	private JComboBox graspsComboBox;

	public AbilityHandGripProgramNodeView(Style style) {
		this.style = style;
		
	}

	@Override
	public void buildUI(JPanel panel, final ContributionProvider<AbilityHandGripProgramNodeContribution> provider) {
		this.contributionProvider = provider;
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel("Select the desired grip"), Component.LEFT_ALIGNMENT);
		panel.add(createVerticalSpacing());
		panel.add(createGraspSelectionSection());

		panel.add(createVerticalSpacing());

		panel.add(createSliderBox("SPEED", provider));
		panel.add(new JLabel("(set SPEED to 0 to stop grip)"), Component.LEFT_ALIGNMENT);


		errorLabel = new JLabel();
		errorLabel.setForeground(java.awt.Color.RED);
		panel.add(errorLabel);
	}

	public void updateView() {
		updateGraspCombobox();
	}

	private int getGripIndex(String[] grips, String selectedGrasp) {
		for (int i = 0; i < grips.length; i++) {
			if (grips[i].equals(selectedGrasp)) {
				return i;
			}
		}
		return 0; //return Open if fails
	}


	private Box createGraspSelectionSection() {
		Box section = Box.createHorizontalBox();
		section.setAlignmentX(Component.LEFT_ALIGNMENT);

		graspsComboBox = createComboBox(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox comboBox = (JComboBox) e.getSource();
				int index;
				String selectedGrasp = (String) comboBox.getSelectedItem();
				String[] grasps = { "Open", "Power", "Key", "Pinch", "Tripod Opened", "Sign of the Horns", "Cylinder", "Mouse Grasp", "Power/Key Switch", "Point", "Rude...", "Hook", "Relax", "Sleeve", "Peace", "Tripod Closed", "Hang Loose", "Handshake", "Fixed Pinch", "User Grip 7", "User Grip 8", "User Grip 9", "User Grip 10", "User Grip 11", "Trigger Grip", "User Grip 12", "User Grip 13", "User Grip 14", "User Grip 15", "User Grip 16", "User Grip 17", "User Grip 18", "Finger Wave"};
				
				index = getGripIndex(grasps, selectedGrasp);
				contributionProvider.get().onGraspSelected(selectedGrasp, index);

				updateGraspCombobox();
			}
		});


		section.add(graspsComboBox);

		return section;
	}

	private Box createSliderBox(String label, final ContributionProvider<AbilityHandGripProgramNodeContribution> provider) {
        Box box = Box.createHorizontalBox();
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.add(new JLabel(label + ":"));
        box.add(createVerticalSpacing(10));

        JSlider slider = new JSlider(0, 255, 0);
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
		AbilityHandGripProgramNodeContribution contribution = contributionProvider.get();

		String[] grasps = { "Open", "Power", "Key", "Pinch", "Tripod Opened", "Sign of the Horns", "Cylinder", "Mouse Grasp", "Power/Key Switch", "Point", "Rude...", "Hook", "Relax", "Sleeve", "Peace", "Tripod Closed", "Hang Loose", "Handshake", "Fixed Pinch", "User Grip 7", "User Grip 8", "User Grip 9", "User Grip 10", "User Grip 11", "Trigger Grip", "User Grip 12", "User Grip 13", "User Grip 14", "User Grip 15", "User Grip 16", "User Grip 17", "User Grip 18", "Finger Wave"};
		
		for (String grasp : grasps) {
			model.addElement(grasp);
		}

		String selected = contribution.getSelectedGrasp();
		if (!selected.isEmpty()) {
			model.setSelectedItem(selected);
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
		String[] grasps = { "Open", "Power", "Key", "Pinch", "Tripod Opened", "Sign of the Horns", "Cylinder", "Mouse Grasp", "Power/Key Switch", "Point", "Rude...", "Hook", "Relax", "Sleeve", "Peace", "Tripod Closed", "Hang Loose", "Handshake", "Fixed Pinch", "User Grip 7", "User Grip 8", "User Grip 9", "User Grip 10", "User Grip 11", "Trigger Grip", "User Grip 12", "User Grip 13", "User Grip 14", "User Grip 15", "User Grip 16", "User Grip 17", "User Grip 18", "Finger Wave"};
		
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
