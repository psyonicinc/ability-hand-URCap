package com.ur.urcap.psyonic.abilityhand.position.impl;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

public class AbilityHandPositionNodeView implements SwingProgramNodeView<AbilityHandPositionNodeContribution> {
    private JSlider indexSlider;
    private JSlider middleSlider;
    private JSlider ringSlider;
    private JSlider pinkySlider;
    private JSlider thumbFlexorSlider;
    private JSlider thumbOppositionSlider;
    private JLabel indexValueLabel;
    private JLabel middleValueLabel;
    private JLabel ringValueLabel;
    private JLabel pinkyValueLabel;
    private JLabel thumbValueLabel;
    private JLabel thumbOppValueLabel;
    private JLabel errorLabel;

    @Override
    public void buildUI(JPanel panel, ContributionProvider<AbilityHandPositionNodeContribution> provider) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(createVerticalSpacing(10));

        // Sliders
        panel.add(createSliderBox("Index", provider));
        panel.add(createSliderBox("Middle", provider));
        panel.add(createSliderBox("Ring", provider));
        panel.add(createSliderBox("Pinky", provider));
        panel.add(createSliderBox("Thumb Flexor", provider));
        panel.add(createSliderBox("Thumb Opposition", provider));

        // Error label
        errorLabel = new JLabel();
        errorLabel.setForeground(java.awt.Color.RED);
        panel.add(errorLabel);
    }

    private Box createSliderBox(String label, final ContributionProvider<AbilityHandPositionNodeContribution> provider) {
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

        box.add(slider);
        return box;
    }

    public void updateSliders(int index, int middle, int ring, int pinky, int thumbFlexor, int thumbOpposition) {
        indexSlider.setValue(index);
        middleSlider.setValue(middle);
        ringSlider.setValue(ring);
        pinkySlider.setValue(pinky);
        thumbFlexorSlider.setValue(thumbFlexor);
        thumbOppositionSlider.setValue(thumbOpposition);
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