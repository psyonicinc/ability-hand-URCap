package com.ur.urcap.psyonic.abilityhand.impl;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MyDaemonProgramNodeView implements SwingProgramNodeView<MyDaemonProgramNodeContribution> {

	private final Style style;
	private JComboBox<Integer> baudComboBox = new JComboBox<Integer>();
	private final JCheckBox simulatedCheckBox = new JCheckBox("Simulated Hand");

	public MyDaemonProgramNodeView(Style style) {
		this.style = style;
	}

	@Override
	public void buildUI(JPanel panel, ContributionProvider<MyDaemonProgramNodeContribution> provider) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(createDescription("Select Baud Rate ( Auto Detect == 0 )"));
		panel.add(createBaudComboBox(baudComboBox, provider));
		panel.add(createSpacer(20));
		panel.add(createCheckBox(simulatedCheckBox, provider));
	}
	
	public void setBaudComboBoxItems(Integer[] items) {
		baudComboBox.removeAllItems();
		baudComboBox.setModel(new DefaultComboBoxModel<Integer>(items));
	}
	
	public void setBaudComboBoxSelection(Integer item) {
		baudComboBox.setSelectedItem(item);
	}
	
	public void setCheckBoxSelected(boolean selected) {
		simulatedCheckBox.setSelected(selected);
	}
	
	private Box createDescription(String desc) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel label = new JLabel(desc);
		box.add(label);
		return box;
	}
	
	private Box createBaudComboBox(final JComboBox<Integer> combo, final ContributionProvider<MyDaemonProgramNodeContribution> provider) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel label = new JLabel("Baud Rate: ");
		
		combo.setPreferredSize(new Dimension(104,30));
		combo.setMaximumSize(combo.getPreferredSize());
		
		combo.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					provider.get().onBaudSelection((Integer)e.getItem());
				}
			}
		});
		
		box.add(label);
		box.add(combo);
		return box;
	}
	
	private Box createCheckBox(final JCheckBox checkbox, final ContributionProvider<MyDaemonProgramNodeContribution> provider) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		checkbox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				provider.get().onSimulatedSelection(checkbox.isSelected());
				
			}
		});
		
		box.add(checkbox);
		return box;
	}
	
	private Component createSpacer(int height) {
		return Box.createRigidArea(new Dimension(0, height));
	}



}
