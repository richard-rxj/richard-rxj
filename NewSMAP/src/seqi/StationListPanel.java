package seqi;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * StationListPanel shows all stations loaded into this application, grouped by
 * projects they belong to.
 * 
 * Copyright (C) 2009, Research School of Earth Sciences, The Australian
 * National University
 * 
 * @author huynh
 * 
 *         Created on: Aug 28, 2009
 */
public class StationListPanel extends JPanel {

	/**
	 * A map from each network name to the list of stations deployed for that
	 * project.
	 */
	private final Map<String, Set<StationInfo>> networkToStations = new HashMap<String, Set<StationInfo>>();

	private final SEQI mainApp;

	/** 
	 * The list of stations selected via this JPanel.  
	 */
	private StationInfo[] selectedStations;

	/**
	 * A JList displaying all the stations of a network selected by users.   
	 */
	private final JList stationList;
	
	
	/**
	 * A JComboBox displaying the list of available station networks.
	 */
	private final JComboBox networkBox;
	
	
	/**
	 * Create a StationListPanel that is associated with a given MapView.
	 * 
	 * @param mapView
	 *            the view of the event and station map in the world.
	 */
	public StationListPanel(SEQI mainApp) {
		this.mainApp = mainApp;

		// The set of stations to be displayed.
		Set<StationInfo> allStations = mainApp.getAllStations();

		// Organize the stations by project name.
		for (StationInfo stationInfo : allStations) {
			Set<StationInfo> networkStations = networkToStations
					.get(stationInfo.networkFullName);
			if (networkStations == null) {
				networkStations = new HashSet<StationInfo>();
				networkToStations.put(stationInfo.networkFullName,
						networkStations);
			}
			networkStations.add(stationInfo);
		}

		// Display of the network (project) names.
		String[] networkNames = new String[networkToStations.keySet().size()];
		networkToStations.keySet().toArray(networkNames);
		Arrays.sort(networkNames); // sort network names alphabetically.
		networkBox = new JComboBox(networkNames);
		this.setLayout(new BorderLayout());
		add(networkBox, BorderLayout.NORTH);

		// Display of the station names belonging to the displayed network
		// (project).
		stationList = new JList();
		// stationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane stationListScroller = new JScrollPane(stationList);
		add(stationListScroller, BorderLayout.CENTER);

		// Listener for network selection
		networkBox.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				String networkName = (String) networkBox.getSelectedItem();
				Set<StationInfo> networkStations = networkToStations
						.get(networkName);

				if (networkStations != null) {
					StationInfo[] stationArr = new StationInfo[networkStations
							.size()];
					networkStations.toArray(stationArr);
					Arrays.sort(stationArr); // sort the stations first

					stationList.setListData(stationArr);
				}
			}
		});

		// Listener for station selection within a network
		stationList.addListSelectionListener(new ListSelectionListener() {
			/**
			 * Select a station on the map while clicking on its list display.
			 * De-select any other station.
			 * 
			 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
			 */
			public void valueChanged(ListSelectionEvent e) {
				// Display selected stations in the associated MapView
				StationListPanel.this.mainApp
						.setSelectedStations(StationListPanel.this.getSelectedStations());
			}

		});

		// set default network to be selected
		if (!networkToStations.isEmpty())
			networkBox.setSelectedIndex(0);

		// Border
		setBorder(BorderFactory.createTitledBorder("Networks & stations"));

		setPreferredSize(new Dimension(200, 100));
	}
	
	
	public StationInfo[] getSelectedStations() {		
		Object[] selectedObjs = stationList.getSelectedValues();
		int nSelected = selectedObjs.length;		
		selectedStations = new StationInfo[nSelected];
		for (int i = 0; i < nSelected; i++) {
			selectedStations[i] = (StationInfo) selectedObjs[i];
		}

		return selectedStations;
	}
	
	
	/**
	 * Select a list of stations using their indices in the
	 * JList component.
	 * 
	 * @param  The list of indices of the selected stations.
	 */	
	public void setSelectedStations(int[] indices) {
		stationList.setSelectedIndices(indices);
	}
	
	/**
	 * Select all the stations in the currently selected network.
	 */
	public void selectAllStations() {
		int nStations = getNumStations();		
		stationList.addSelectionInterval(0, nStations - 1);
	}
	
	
	/**
	 * Deselect all the stations in the currently selected network.
	 */
	public void deselectAllStations() {	
		stationList.setSelectedIndices(new int[]{});
	}


	/**
	 * @return The number of stations in the currently selected network.
	 */
	public int getNumStations() {
		return stationList.getModel().getSize();
	}
	
	
	/**
	 * Enable/disable this Component and all its children. 
	 */	
	public void setEnabled(boolean b) {
		networkBox.setEnabled(b);
		stationList.setEnabled(b);		
	}
	
	
	/**
	 * Add a listener to listen for users events occuring to the 
	 * station list. 
	 */
	public void addListSelectionListener(ListSelectionListener l) {
		stationList.addListSelectionListener(l);
	}
	
}
