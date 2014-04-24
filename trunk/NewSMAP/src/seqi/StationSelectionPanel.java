package seqi;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ListSelectionListener;

/**
 * StationSelectionPanel is a UI class 
 * that contains a StationListPanel to allow station list selection.
 * 
 * Copyright (C) 2011, Research School of Earth Sciences,
 * The Australian National University.
 * 
 * @author huynh
 * 
 * Created on: Oct 10, 2011
 */
public class StationSelectionPanel extends JPanel {
	
	final StationListPanel stationListPanel;
	final JRadioButton selectAll, deselectAll;

	/**
	 * Create a station list from which to select 
		stationSelector = new JRadioButton("Station(s)");
	 * the center of reference for distance range filters. 
	 */
	public StationSelectionPanel(SEQI mainApp) {
		super();
		setLayout(new GridBagLayout());
		
		// Add the list of stations
		stationListPanel = new StationListPanel(mainApp);		
		stationListPanel.setBorder(BorderFactory.createEmptyBorder());		

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; 
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 1;		
		c.weightx = 0; 
		c.weighty = 0;
		c.fill = GridBagConstraints.BOTH;
		add(stationListPanel, c);
		
		// Add two radio buttons to select and deselect stations
		selectAll = new JRadioButton("Select All");
		selectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stationListPanel.selectAllStations();
			}
		});
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;		
		c.fill = GridBagConstraints.HORIZONTAL;
		add(selectAll, c);

		
		deselectAll = new JRadioButton("Deselect All");
		deselectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stationListPanel.deselectAllStations();	
			}
		});		
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;		
		c.fill = GridBagConstraints.HORIZONTAL;
		add(deselectAll, c);
		
		// Group the two buttons
		ButtonGroup selectButtonGroup = new ButtonGroup();
        selectButtonGroup.add(selectAll);
        selectButtonGroup.add(deselectAll);
	}
	
	
	/**
	 * Enable/disable this Component and all its children. 
	 */
	public void setEnabled(boolean b) {
		super.setEnabled(b);
		
		stationListPanel.setEnabled(b);
		selectAll.setEnabled(b);
		deselectAll.setEnabled(b);
	}
	
	
	/**
	 * Add a listener to listen for users events occuring to the 
	 * station list. 
	 */
	public void addListSelectionListener(ListSelectionListener l) {
		stationListPanel.addListSelectionListener(l);
	}
	
	public StationInfo[] getSelectedStations() {
		return stationListPanel.getSelectedStations();
	}
}