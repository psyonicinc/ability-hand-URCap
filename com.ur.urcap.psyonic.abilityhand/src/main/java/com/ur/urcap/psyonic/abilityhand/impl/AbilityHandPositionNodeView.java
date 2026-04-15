package com.ur.urcap.psyonic.abilityhand.impl;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Component;
import java.awt.Font;

public class AbilityHandPositionNodeView implements SwingProgramNodeView<AbilityHandPositionNodeContribution> {

    private JSlider indexSlider;
    private JSlider middleSlider;
    private JSlider ringSlider;
    private JSlider pinkySlider;
    private JSlider thumbFlexorSlider;
    private JSlider thumbOppositionSlider;

    private JCheckBox liveTrackingCheckbox;

    private JLabel indexValueLabel;
    private JLabel middleValueLabel;
    private JLabel ringValueLabel;
    private JLabel pinkyValueLabel;
    private JLabel thumbValueLabel;
    private JLabel thumbOppValueLabel;

    private JButton savePositionBtn;
    private JComboBox<String> getPositionBox;
    private JLabel waypointDisplayLabel;


    private JLabel errorLabel;


    @Override
    public void buildUI(JPanel panel, ContributionProvider<AbilityHandPositionNodeContribution> provider) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(createVerticalSpacing(10));

        // ── Sliders ───────────────────────────────────────────────────────────
        panel.add(createSliderBox("Index", provider));
        panel.add(createSliderBox("Middle", provider));
        panel.add(createSliderBox("Ring", provider));
        panel.add(createSliderBox("Pinky", provider));
        panel.add(createSliderBox("Thumb Flexor", provider));
        panel.add(createSliderBox("Thumb Opposition", provider));

        panel.add(createVerticalSpacing(10));

        // ── Save button + position box side by side ───────────────────────────
        JPanel middleRow = new JPanel(new GridBagLayout());
        middleRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 8);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;

        savePositionBtn = new JButton("Save Current Position as Waypoint");
        savePositionBtn.addActionListener(e -> provider.get().savePositionPoint());
        gbc.gridx = 0;
        middleRow.add(savePositionBtn, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        middleRow.add(createPositionBox(provider), gbc);

        // Label directly under createPositionBox
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(2, 0, 0, 8);
        waypointDisplayLabel = new JLabel(" ");
        waypointDisplayLabel.setFont(waypointDisplayLabel.getFont().deriveFont(Font.PLAIN, 11f));
        middleRow.add(waypointDisplayLabel, gbc);

        panel.add(middleRow);

        panel.add(createVerticalSpacing(10));

        // ── Checkbox ──────────────────────────────────────────────────────────
        liveTrackingCheckbox = new JCheckBox("Live Position Tracking");
        liveTrackingCheckbox.setAlignmentX(Component.LEFT_ALIGNMENT);
        liveTrackingCheckbox.addActionListener(e -> {
            provider.get().onCheckboxChanged(liveTrackingCheckbox.isSelected());
            provider.get().setLiveTracking(liveTrackingCheckbox.isSelected());
        });
        panel.add(liveTrackingCheckbox);

        panel.add(createVerticalSpacing(10));

        // ── Error label ───────────────────────────────────────────────────────
        errorLabel = new JLabel();
        errorLabel.setForeground(java.awt.Color.RED);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(errorLabel);
    }

    private Box createSliderBox(String label, final ContributionProvider<AbilityHandPositionNodeContribution> provider) {
        Box verticalBox = Box.createVerticalBox();
        verticalBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        verticalBox.add(new JLabel(label + ":"));
        verticalBox.add(createVerticalSpacing(5));

        Box sliderBox = Box.createHorizontalBox();
        sliderBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSlider slider = new JSlider(0, 120, 0);
        slider.setPreferredSize(new Dimension(150, 30));
        sliderBox.add(slider);
        sliderBox.add(createVerticalSpacing(20));

        JLabel valueLabel = new JLabel(String.valueOf(slider.getValue()));
        valueLabel.setPreferredSize(new Dimension(40, 30));
        sliderBox.add(valueLabel);

        verticalBox.add(sliderBox);

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = slider.getValue();
                valueLabel.setText(String.valueOf(value));
                String key = label.toLowerCase().replace(" ", "_");
                provider.get().updatePosition(key, value);

                if (liveTrackingCheckbox != null && liveTrackingCheckbox.isSelected()) {
                    provider.get().updateHandPosition();
                }

            }
        });

        switch (label) {
            case "Index": indexSlider = slider;
                break;
            case "Middle": middleSlider = slider;
                break;
            case "Ring": ringSlider = slider;
                break;
            case "Pinky": pinkySlider = slider;
                break;
            case "Thumb Flexor": thumbFlexorSlider = slider;
                break;
            case "Thumb Opposition": thumbOppositionSlider = slider;
                break;
        }

        // box.add(slider);
        return verticalBox;
    }

    public void updateSliders(int index, int middle, int ring, int pinky, int thumbFlexor, int thumbOpposition) {
        indexSlider.setValue(index);
        middleSlider.setValue(middle);
        ringSlider.setValue(ring);
        pinkySlider.setValue(pinky);
        thumbFlexorSlider.setValue(thumbFlexor);
        thumbOppositionSlider.setValue(thumbOpposition);

    }

    private JComboBox createPositionBox(final ContributionProvider<AbilityHandPositionNodeContribution> provider) {
        getPositionBox = new JComboBox<>();
        getPositionBox.addActionListener(e -> {
                String selected = (String) getPositionBox.getSelectedItem();
                if (selected != null) {
                    provider.get().onWaypointSelected(selected);
                }
            });
        Dimension d = getPositionBox.getPreferredSize();
        getPositionBox.setPreferredSize(new Dimension(100, d.height));
        getPositionBox.setMaximumSize(new Dimension(100, 60));
        getPositionBox.setMinimumSize(new Dimension(100, 60));
        
        return getPositionBox;
    }

    public void setDropdownItems(java.util.List<String> names, String selectedName) {
        getPositionBox.removeAllItems();
        for (String name : names) {
            getPositionBox.addItem(name);
        }
        if (selectedName != null && !selectedName.isEmpty()) {
            getPositionBox.setSelectedItem(selectedName);
        }
    }

    public void setWaypointDisplay(String name, double[] positions) {
        if (name==null || name.isEmpty()) {
            waypointDisplayLabel.setText(" ");
        } 
        else {
            waypointDisplayLabel.setText(name + ": " + formatWaypoint(positions));
        }
    }

    private String formatWaypoint(double[] positions) {
        StringBuilder sb = new StringBuilder("[");
        for (int i=0; i<positions.length; i++) {
            if (i>0) sb.append(", ");
            sb.append(String.format("%.2f", positions[i]));
        }
        return sb.append("]").toString();
    }



    public void setCheckbox(boolean checked) {
		liveTrackingCheckbox.setSelected(checked);
	}

    public void showError(String message) {
        errorLabel.setText(message);
    }

    private Component createVerticalSpacing(int height) {
        return Box.createRigidArea(new Dimension(0, height));
    }

    private Component createHorizontalSpacing(int width) {
        return Box.createRigidArea(new Dimension(width, 0));
    }
}