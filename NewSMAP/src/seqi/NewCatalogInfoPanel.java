package seqi;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;


/**  add new feature---------------------------------------------v1,7
 * CatalogInfoPanel display the catalog file being loaded 
 * into the application.
 * 
 * Copyright (C) 2014, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author richard.rxj
 * 
 * Created on: March 30, 2014
 */
public class NewCatalogInfoPanel extends JPanel {
	
	/** The area to display detailed information of the catalog file. */
	private JTextArea catFileDetail;
	
	/** 
	 * The main application class that handles top level routines.  
	 */ 
	private final SEQI mainApp;
	
	public NewCatalogInfoPanel(SEQI mainApp) {
		this.mainApp = mainApp;
		
		this.setBorder(BorderFactory.createTitledBorder("Catalog (Start--End)"));		
		setPreferredSize(new Dimension(200, 120));		
		setLayout(new GridBagLayout());
		
		// Add the catalog list
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; 
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;		
		c.weightx = 0; 
		c.weighty = 0;
		c.anchor = GridBagConstraints.WEST;
		this.add(new JLabel("Load"), c);
		
		
		final JComboBox catCombo = new JComboBox(new String[]{"EHB", "CMT", "NEIC"});		
		catCombo.setSelectedIndex(0);
		c.gridx = 1;
		c.gridy = 0;		
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.CENTER;
		this.add(catCombo, c);	
		
		
		
		
		// Use this JSpinner to set the number of past years
		// within which to load events from		
		final JSpinner numRecentYearsField 
			= new JSpinner(new SpinnerNumberModel(2014, 1950, Integer.MAX_VALUE, 1));
		numRecentYearsField.setPreferredSize(new Dimension(60, 25));
		c.gridx = 0; 
		c.gridy = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.EAST;
		this.add(numRecentYearsField, c);
		
		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		this.add(new JLabel("   To "), c);
		
		
		// Use this JSpinner to set the number of past years
		// within which to load events from		
		final JSpinner numRecentYearsField2 
			= new JSpinner(new SpinnerNumberModel(2014, 1950, Integer.MAX_VALUE, 1));
		numRecentYearsField2.setPreferredSize(new Dimension(60, 25));
		c.gridx = 2; 
		c.gridy = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.WEST;
		this.add(numRecentYearsField2, c);
		
//		c.gridx = 2;
//		c.gridy = 1;
//		c.fill = GridBagConstraints.HORIZONTAL;
//		c.anchor = GridBagConstraints.WEST;
//		this.add(new JLabel(" years"), c);
		
		
		// Load catalog button
		final JButton loadButton = new JButton("Load");
		loadButton.addMouseListener(new MouseAdapter(){			
			public void mousePressed(MouseEvent e) {
				// Check which catalog is being loaded
				// and load the newly selected catalog if
				// it is different from the currently loaded one.				
				String selectedCat = (String)catCombo.getSelectedItem();
		
				// Retrieve the number of recent years for which to extract data.
				try {
					numRecentYearsField.commitEdit();
					numRecentYearsField2.commitEdit();
				} catch (ParseException exception) {
					// For debugging purposes
					// exception.printStackTrace();
				}
		
				int startYear = ((Number)numRecentYearsField.getValue()).intValue();
				int endYear = ((Number)numRecentYearsField2.getValue()).intValue();
				// System.out.println("Num recent years " + numRecentYears);				
				NewCatalogInfoPanel.this.mainApp.loadCatalogByName(selectedCat, startYear, endYear);
				// loadButton.setEnabled(false);
			}
		});
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(5, 0, 0, 0);		
		c.gridwidth = 2;		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.SOUTH;
		this.add(loadButton, c);
		
		
		// catalog file detail 
//		catFileDetail = new JTextArea();
//		JScrollPane scroll = new JScrollPane(catFileDetail);
//		catFileDetail.setEditable(false);
//		c.gridx = 1; 
//		c.gridy = 0;
//		c.gridwidth = 1;
//		c.gridheight = 2;
//		c.insets = new Insets(0, 2, 0, 2); 
//		c.fill = GridBagConstraints.BOTH;		
//		c.weightx = 1; 
//		c.weighty = 1;		
//		this.add(scroll, c);
	}
	
	
	/**
	 * Set and display the name of the catalog file 
	 * being loaded.
	 * @param catFileName
	 */
	public void displayCatFile(File catFile) {
		// loadedCatFile = catFile;
		// catFileDetail.setText("Loaded: \n" + catFile.getName());
	}
}
