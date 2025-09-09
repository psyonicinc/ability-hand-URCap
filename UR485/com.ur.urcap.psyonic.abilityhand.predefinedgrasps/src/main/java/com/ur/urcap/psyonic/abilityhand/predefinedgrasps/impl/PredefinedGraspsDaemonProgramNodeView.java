package com.ur.urcap.psyonic.abilityhand.predefinedgrasps.impl;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;
// import com.ur.urcap.psyonic.abilityhand.predefinedgrasps.impl.UIComponentFactory;

import javax.swing.*;
import java.awt.*;
// import java.awt.event.MouseAdapter;
// import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class PredefinedGraspsDaemonProgramNodeView implements SwingProgramNodeView<PredefinedGraspsDaemonProgramNodeContribution> {
	
	private static final String GRASP_PLACEHOLDER = "";

	private final Style style;
	// private final UIComponentFactory uiFactory;

	private ContributionProvider<PredefinedGraspsDaemonProgramNodeContribution> contributionProvider;

	private JComboBox graspsComboBox;

	public PredefinedGraspsDaemonProgramNodeView(Style style) {
		this.style = style;
		// this.uiFactory = new UIComponentFactory(style);
	}

	@Override
	public void buildUI(JPanel panel, final ContributionProvider<PredefinedGraspsDaemonProgramNodeContribution> provider) {
		this.contributionProvider = provider;
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel("Select the desired grasp"), Component.LEFT_ALIGNMENT);
		panel.add(createVerticalSpacing());
		panel.add(createGraspSelectionSection());
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
			// model.addElement(GRASP_PLACEHOLDER);
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

// private Box createDropDown(final ContributionProvider<PredefinedGraspsDaemonProgramNodeContribution> provider) {
// 		String[] grasps = { "Open", "Power", "Key", "Pinch", "Tripod Opened", "Sign of the Horns", "Cylinder", "Mouse Grasp", "Power/Key Switch", "Point", "Rude...", "Hook", "Relax", "Sleeve", "Peace", "Tripod Closed", "Hang Loose", "Handshake", "Fixed Pinch"};
// 		final JComboBox graspDropDown = new JComboBox(grasps);
// 		graspDropDown.setMaximumSize(new Dimension(200, 30));
// 		graspDropDown.addActionListener(new ActionListener() {
// 			@Override
// 			public void actionPerformed(ActionEvent e) {
// 				if (provider.get() != null) {
// 					String selectedGrasp = (String) graspDropDown.getSelectedItem();
// 					if (selectedGrasp != null) {
// 						provider.get().onGraspSelected(selectedGrasp);
// 					}
// 				}
// 			}
// 		});

// 		SwingUtilities.invokeLater(new Runnable() {
//         @Override
//         public void run() {
//             try {
//                 String currentGrasp = provider.get().getSelectedGrasp();
//                 graspDropDown.setSelectedItem(currentGrasp);
//             } catch (Exception e) {
//                 System.err.println("Could not set initial grasp selection: " + e.getMessage());
//             }
//         }
//     	});

// 		Box box = Box.createHorizontalBox();
// 		box.add(new JLabel("Select Grasp:"));
// 		box.add(graspDropDown);
// 		return box;
// 	}