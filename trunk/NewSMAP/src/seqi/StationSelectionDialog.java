package seqi;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * StationSelectionDialog is a dialog asking users 
 * to choose the locations of destination stations   
 * for filtering events based on the geodesic distance 
 * or for computing the arrival times of events.
 * 
 * Copyright (C) 2011, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Oct 10, 2011
 */
public class StationSelectionDialog extends JDialog {
	
	private StationSelectionPanel stationSelectionPanel;
			
	StationSelectionDialog(SEQI mainApp) {
		super(mainApp, "Destination Station", true);				
		setLayout(new GridBagLayout());		
		GridBagConstraints c = new GridBagConstraints();
      
		// Add the top label
		c.gridx = 0; 
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		
		JPanel labelPanel = new JPanel();
		JLabel label = new JLabel("Networks and stations");
		labelPanel.add(label);		
		add(labelPanel, c);
		
		
		// Add the station list		
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		stationSelectionPanel = new StationSelectionPanel(mainApp);		
		add(stationSelectionPanel, c);		

		// A button to confirm selection
		JPanel buttonPanel = new JPanel();
		JButton okButton = new JButton("OK");
		buttonPanel.add(okButton);
		
		okButton.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				// remember selected station.				
				StationSelectionDialog.this.setVisible(false);			
			}
		});

		c.gridy = 2;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;		
		add(buttonPanel, c);
		
		
		// setPreferredSize(new Dimension(300, 200));		
		setLocationRelativeTo(mainApp);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		pack();
		setVisible(true);
	}
	
	public StationInfo[] getSelectedStations() {
		return stationSelectionPanel.getSelectedStations();
	}

	/**
	 * @return The JPanel containing the list of stations being displayed in the dialog. 
	 */
	public StationSelectionPanel getStationSelectionPanel() {
		return stationSelectionPanel;
	}
	
}