package seqi;

import java.awt.BorderLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * StationDateFilterPanel provides an interface to filter
 * out events falling outside the deployment dates of 
 * a number of selected stations. 
 * 
 * Copyright (C) 2010, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Dec 3, 2010
 */
public class StationDateFilterPanel extends FilterPanel {

	private StationListPanel stationListDisplay;
	
	/**
	 * A filter that retrieves events between the start and end dates of a
	 * specified number of filters. As a result of station selection, only the
	 * events falling into the date range of the selected stations are displayed
	 * on the map.
	 */	
	private StationDateFilter stationDateFilter; 
			
	// private JButton previewButton;
	
	public StationDateFilterPanel(final SEQI mainApp) {
		
		setLayout(new BorderLayout());
		
		this.setToolTipText("Retain events falling into the deployment dates of a number of stations");

		// Initialised the filter
		stationDateFilter = new StationDateFilter();
		
		// Add network and station list (a drop down menu)					
		stationListDisplay = new StationListPanel(mainApp);
		add(stationListDisplay, BorderLayout.CENTER);
		
		// Add listeners to display filtered events on the map
		// corresponding to the selected stations.
		stationListDisplay.addListSelectionListener(new ListSelectionListener() {
			
			public void valueChanged(ListSelectionEvent e) {
				
				// Collect the list of stations whose deployment dates 
				// are treated as reference for filtering.				
				// Clear all the stations for the date range filter before
				// adding the selected ones.
				StationDateFilterPanel.this.stationDateFilter.clearAllStations();
				StationInfo[] selectedStations = stationListDisplay.getSelectedStations();
				int nSelected = selectedStations.length;				
				for (int i = 0; i < nSelected; i++) {				
					StationDateFilterPanel.this.stationDateFilter.addStation(selectedStations[i]);
				}
				
				// Filter events for the selected station and display them.
				EventList allCatEvents = mainApp.getCatalogEvents();
				MapView mapView = mainApp.getMapView();
				FilterControlPanel filterControlPanel = mainApp.getFilterControlPanel();

				// If any catalog has been loaded and some stations have been
				// selected.
				if (allCatEvents != null) {
					if (nSelected >= 1) {
						EventList stationEvents = StationDateFilterPanel.this.stationDateFilter
								.filter(allCatEvents);
						mapView.setEvents(stationEvents);

						// Update the initial list of events to be filtered.
						filterControlPanel.setFilteredEvents(stationEvents);

					} else {
						mapView.setEvents(allCatEvents);

						// Update the initial list of events to be filtered.
						filterControlPanel.setFilteredEvents(allCatEvents);
					}
				}
			}
		});

//		// A button to preview filter output
//		JPanel buttonPanel = new JPanel();
//		previewButton = new JButton("Preview Filtered Events");
//		buttonPanel.add(previewButton);
//		add(buttonPanel, BorderLayout.SOUTH);
//		
//		previewButton.addActionListener(new ActionListener() {			
//			public void actionPerformed(ActionEvent e) {
//				// remember selected station.
//			}
//		});		
	}


	
	public EventFilter getFilter() {
		return stationDateFilter;
	}		
}
