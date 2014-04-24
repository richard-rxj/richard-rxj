package seqi;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.Observable;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * DistanceRangeFilterPanel
 * 
 * Copyright (C) 2009, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Sep 11, 2009
 */
public class DistanceRangeFilterPanel extends FilterPanel implements java.util.Observer {
	
	/**
	 * The filter currently used in this DistanceRangeFilterPanel 
	 * (either the pointFilter or stationsFilter,
	 * as below).
	 */
	private AbstractDistanceRangeFilter filter;
	
	/**
	 * The DistanceRangeFilter with the center of reference selected at 
	 * any point on the map.
	 */	
	private AtomicDistanceRangeFilter pointFilter;
	
	/**
	 * The DistanceRangeFilterUnion which collects the results of 
	 * DistanceRangeFilters, each with the reference center at a station.
	 */	
	private UnionDistanceRangeFilter stationsFilter;  
	
	
	/**
	 * The associated MapView
	 */
	private MapView map;
	
	/**
	 * The application instance associated with this DistanceRangeFilterPanel.
	 */
	private SEQI mainApp;
	
	
	/** GUI elements */
	// text fields to enter the min and max distance in degrees
	private JFormattedTextField minDistField, maxDistField;
	
	private JRadioButton pointSelector, stationSelector; 
	
	// The label displaying error messages when
	// minDist >= maxDist
	private JLabel checkDistLabel;
	
	private JLabel pointCoordsLabel2;
	
	private StationSelectionDialog stationSelectionDialog;
	
	public DistanceRangeFilterPanel(final SEQI mainApp) {
		
		// Prepare the filters.
		pointFilter = new AtomicDistanceRangeFilter();
		stationsFilter = new UnionDistanceRangeFilter();
		
		// Initialise the filter with the point reference type.
		filter = pointFilter;
		
		this.mainApp = mainApp;		
		MapView mapView = mainApp.getMapView(); 
		this.map = mapView;
		
		// Register interest in changes in the MapView.
		//filter.addObserver(mapView);
		//filter.addObserver(this);
		mapView.addObserver(this);
		
		//// Prepare GUI 
		// Text fields for the min and max distance 
		// (in degrees) from a point. 
		GridBagLayout gridbag = new GridBagLayout();
		this.setLayout(gridbag);		
		GridBagConstraints c = new GridBagConstraints();
		this.setToolTipText("Filter events by distance from a point or station");
		
		
		// JPanel containing range information.
		JPanel rangePanel = new JPanel();
		rangePanel.setLayout(new GridBagLayout());
		rangePanel.setBorder(new TitledBorder("Distance Range"));		
				
		GridBagConstraints c1 = new GridBagConstraints();
		
		
		// Min distance		
		JLabel minLabel = new JLabel("Min (degree)");
		c1.gridx = 0; 
		c1.gridy = 0;
		c1.gridwidth = 1;
		c1.gridheight = 1;		
		c1.weightx = 0; 
		c1.weighty = 0;
		c1.fill = GridBagConstraints.HORIZONTAL;		
		rangePanel.add(minLabel, c1);
		
		// listener for the value change in the min and max distance fields.
		PropertyChangeListener minmaxChangeListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				float maxDist = ((Number)maxDistField.getValue()).floatValue();				
				float minDist = ((Number)minDistField.getValue()).floatValue(); 
				
				if (minDist < maxDist) {
					
					// Set the min and max distance for both filters.
					pointFilter.setMinAngle(minDist);
					pointFilter.setMaxAngle(maxDist);
					
					stationsFilter.setMinAngle(minDist);
					stationsFilter.setMaxAngle(maxDist);
					
					map.setInnerRadius(minDist);
					map.setOuterRadius(maxDist);				
					
					checkDistLabel.setText(" ");
				} else {
					checkDistLabel.setText("Error: min >= max distance");
				}
				
				
			}
		};
		
		minDistField = new JFormattedTextField(NumberFormat.getInstance());
		minDistField.setValue(new Float(0));
		c1.gridx = 1; 
		c1.gridy = 0;
		c1.weightx = 1;	
		c1.fill = GridBagConstraints.HORIZONTAL;
		rangePanel.add(minDistField, c1);		
		minDistField.addPropertyChangeListener("value", minmaxChangeListener);
		
		
		// Max distance		
		JLabel maxLabel = new JLabel("Max (degree)");
		c1.gridx = 0; 
		c1.gridy = 1;	
		c1.weightx = 0;
		c1.fill = GridBagConstraints.HORIZONTAL;
		rangePanel.add(maxLabel, c1);
		

		maxDistField = new JFormattedTextField(NumberFormat.getInstance());
		maxDistField.setValue(new Float(20));        
		c1.gridx = 1; 
		c1.gridy = 1;	
		c1.weightx = 1;		
		c1.fill = GridBagConstraints.HORIZONTAL;
		rangePanel.add(maxDistField, c1);		
		maxDistField.addPropertyChangeListener("value", minmaxChangeListener);
		
		
		// Update the filters' parameters from the text fields above.
		filter.setMinAngle(((Number)minDistField.getValue()).floatValue());
		filter.setMaxAngle(((Number)maxDistField.getValue()).floatValue());
		
	
		// Label which displays an error message when minDist >= maxDist
		c1.gridx = 0;
		c1.gridy = 2;
		c1.gridwidth = 2;
		checkDistLabel = new JLabel(" ");
		checkDistLabel.setFont(new Font("Arial", Font.ITALIC, 12));
		checkDistLabel.setForeground(Color.RED);
		rangePanel.add(checkDistLabel, c1);
		
		
		// Add range panel to the main panel.
		c.gridx = 0; 
		c.gridy = 0;
		c.gridheight = 1;		
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		this.add(rangePanel, c);
		

		// JPanel containing center of reference information.
		JPanel refCenterPanel = new JPanel();
		refCenterPanel.setLayout(new GridBagLayout());
		refCenterPanel.setBorder(new TitledBorder("Centre of Reference"));
				
		GridBagConstraints c2 = new GridBagConstraints();
		
//		final JLabel centerTypeLabel = new JLabel("Center Type");
//		c2.gridx = 0;
//		c2.gridy = 0;
//		c2.gridwidth = 1;
//		c2.gridheight = 1;
//		c2.weightx = 1;
//		c2.weighty = 0;
//		c2.fill = GridBagConstraints.HORIZONTAL;
//		refCenterPanel.add(centerTypeLabel, c2);
		
		
		// Combo box for center type selection.		
//		final JComboBox centerTypeSelector = new JComboBox(new String[]{"Station(s)", "Point"});
//		c2.gridx = 1;
//		c2.gridy = 0;
//		c2.gridwidth = 1;
//		c2.gridheight = 1;
//		c2.weightx = 1;
//		c2.weighty = 0;
//		refCenterPanel.add(centerTypeSelector, c2);

		
		// A network and station list in case users opt for selecting stations
//		final StationSelectionPanel stationListPanel = new StationSelectionPanel(mainApp);
//		stationListPanel.setToolTipText("Select station(s) on the map or from the list");
//		c2.gridy = 5;
//		c2.weightx = 1;
//		c2.weighty = 1;
//		c2.fill = GridBagConstraints.BOTH;
//		refCenterPanel.add(stationListPanel, c2);

		

		// Add a component to display the selected point's coordinates.
		final JLabel pointCoordsLabel1 = new JLabel("Coordinates (long, lat)");

		// Add a component to display the epi-center coordinates.
		pointCoordsLabel2 = new JLabel(" ");

		// Add listener to register the list of reference stations 
		// of the distance range filter.
//		stationListPanel.addListSelectionListener(new ListSelectionListener() {
//			public void valueChanged(ListSelectionEvent e) {
//				stationsFilter.removeAllStations();
//				stationsFilter.addStations(stationListPanel.getSelectedStations());
//			}
//		});
		
		
		// Add two buttons to select the center type between Point and Station(s)
		pointSelector = new JRadioButton("Point");
		pointSelector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				map.setEpiCenterType(MapView.EpiCenterType.POINT);
				
				filter = pointFilter;
				
				// Disable the station list
				// stationListPanel.setEnabled(false);
				
				// Enable the point's coordinate label
				pointCoordsLabel1.setEnabled(true);
				pointCoordsLabel2.setEnabled(true);
			}
		});
				
		c2.gridx = 0; 
		c2.gridy = 0;
		c2.weightx = 1;
		c2.weighty = 1;
		c2.gridwidth = 1;
		c2.gridheight = 1;
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.anchor = GridBagConstraints.CENTER;		
		refCenterPanel.add(pointSelector, c2);
		
		c2.gridy = 1;
		refCenterPanel.add(pointCoordsLabel1, c2);

		c2.gridy = 2;
		refCenterPanel.add(pointCoordsLabel2, c2);
				
		
		stationSelector = new JRadioButton("Station(s)");
		stationSelector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				map.setEpiCenterType(MapView.EpiCenterType.STATION);
				
				filter = stationsFilter;
				
				// Enable the station list
				// stationListPanel.setEnabled(true);
				if (stationSelectionDialog == null) {
					stationSelectionDialog
						= new StationSelectionDialog(mainApp);

					final StationSelectionPanel stationListPanel 
						= stationSelectionDialog.getStationSelectionPanel();
					
					// Add all the selected stations on the station list
					// to be those selected by the filter.
					// This is because at the initialisation of the stationListPanel
					// no list selection listener has been added to the list.
					// Therefore, the selected stations wouldn't be taken as 
					// those used in the station filter. 
					stationsFilter.removeAllStations();
					stationsFilter.addStations(stationListPanel.getSelectedStations());							
					
					// Add listeners for changes in the list selection.										
					stationListPanel.addListSelectionListener(new ListSelectionListener() {
						public void valueChanged(ListSelectionEvent e) {
							stationsFilter.removeAllStations();
							stationsFilter.addStations(stationListPanel.getSelectedStations());							
						}
					});					
				} else {
					stationSelectionDialog.pack();
					stationSelectionDialog.setVisible(true);
				}
				
				
				// Disable the point's coordinate label
				pointCoordsLabel1.setEnabled(false);
				pointCoordsLabel2.setEnabled(false);
			}
		});
		c2.gridy = 3;
		refCenterPanel.add(stationSelector, c2);
		
		// Group the two buttons
		ButtonGroup refTypeSelectButtonGroup = new ButtonGroup();
		
		refTypeSelectButtonGroup.add(pointSelector);
		refTypeSelectButtonGroup.add(stationSelector);
		
		
		// Initialise the ref type selection.
		pointSelector.setSelected(true);
		// stationListPanel.setEnabled(false);
		
		
		
		// TODO: Propagate this setting to the DistanceRangeFilter GUI
		// mapView.setEpiCenterType(EpiCenterType.STATION);
		
//		centerTypeSelector.addItemListener(new ItemListener() {
//			public void itemStateChanged(ItemEvent e) {
//				// Store the current mouse mode
//				// MouseMode curMode = DistanceRangeFilterPanel.this.map.getMouseMode();
//				
//				// Set mouse mode for station or point selection. 				
//				// DistanceRangeFilterPanel.this.map.setMouseMode(MouseMode.CLICK_SELECT);
//				
//				if (centerTypeSelector.getSelectedItem().equals("Station")) {
//					map.setEpiCenterType(MapView.EpiCenterType.STATION);	
//				} else if (centerTypeSelector.getSelectedItem().equals("Point")) {
//					map.setEpiCenterType(MapView.EpiCenterType.POINT);
//				}	
//			}
//		});
//		// set the initial selection.
//		centerTypeSelector.setSelectedIndex(0);
//		map.setEpiCenterType(MapView.EpiCenterType.STATION);
		
		
//		// Create a JPanel displaying the content of each center type
//		// with some flexibility of flipping between center types.
//		//final JPanel centerInfo = new JPanel();		
//		//centerInfo.setLayout(new CardLayout());
//		
//		// Blank panel if users want to select a point on the map
//		//JPanel pointCenterPanel = new JPanel();
//		//pointCenterPanel.setToolTipText("Click a single point on the map");
//		//centerInfo.add(pointCenterPanel, "Point");
//		
		// A network and station list in case users opt for selecting stations
//		JPanel stationListPanel = createStationSelector();
//		stationListPanel.setToolTipText("Select station(s) on the map or from the list");
//		//centerInfo.add(stationSelector, "Station(s)");
//		
////		// Add the centerInfo panel
////		c2.gridx = 0;
////		c2.gridy = 1;
////		c2.gridwidth = 2;
////		c2.weightx = 1;
////		c2.weighty = 1;
////		refCenterPanel.add(centerInfo, c2);
//		
//		
//		c2.gridy = 3;		
//		c2.weightx = 1;
//		c2.weighty = 1;
//		c2.fill = GridBagConstraints.BOTH;
//		refCenterPanel.add(stationListPanel, c2);
		

		// Enable switching between the center types.		
//		centerTypeSelector.setEditable(false);                
//		centerTypeSelector.addItemListener(new ItemListener() {		
//			public void itemStateChanged(ItemEvent e) {
//				CardLayout cl = (CardLayout)(centerInfo.getLayout());
//		        cl.show(centerInfo, (String)e.getItem());
//		        
//		        if (centerTypeSelector.getSelectedItem().equals("Station(s)")) {
//					map.setEpiCenterType(MapView.EpiCenterType.STATION);	
//				} else if (centerTypeSelector.getSelectedItem().equals("Point")) {
//					map.setEpiCenterType(MapView.EpiCenterType.POINT);
//				}	
//			}
//		});
		
		// Show the station list by default.
//		centerTypeSelector.setSelectedIndex(0);
//		CardLayout cl = (CardLayout)(centerInfo.getLayout());
//      cl.show(centerInfo, "Station(s)");		
		
        
        
		// 14-July-2010: This button becomes redundant because 
		// of the simplification in the mouse mode. There is no longer 
		// a CLICK_SELECT mouse mode.
		// A button to change the mouse mode between the current one 
		// and the spatial coord selection mode.   
//		final JButton mouseModeButton = new JButton("Select Center");
//		c.gridx = 0; 
//		c.gridy = 1;
//		c.gridwidth = 2;
//		c.gridheight = 1;
//		c.weightx = 1; 
//		c.weighty = 0;
//		c.fill = GridBagConstraints.NONE;
//		c.anchor = GridBagConstraints.CENTER;
//		refCenterPanel.add(mouseModeButton, c);
//		
//		// Add listeners to switch between the normal mouse modes
//		// and the selection by clicking mode.
//		mouseModeButton.addActionListener(new ActionListener(){
//			/* (non-Javadoc)
//			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
//			 */
//			public void actionPerformed(ActionEvent e) {
//				MouseMode curMode = map.getMouseMode();
//				
//				if (!curMode.equals(MouseMode.CLICK_SELECT)) {
//					// Set mouse mode for station or point selection. 				
//					map.enterMouseMode(MouseMode.CLICK_SELECT);					
//					mouseModeButton.setText("Confirm Selection");
//				} else {
//					map.revertMouseMode();
//					mouseModeButton.setText("Select Center");
//				}	
//			}
//		});		
		
		
		
		// Add center of reference panel to the main panel.
		c.gridx = 0;
		c.gridy = 1;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		this.add(refCenterPanel, c);	
		
		
		// Add a button to plot great circle paths from a single reference 
		// center to all the filtered events.
		final JCheckBox plotGCPath =  new JCheckBox("Plot great circle paths");
		plotGCPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MapView map = DistanceRangeFilterPanel.this.map;
				if (plotGCPath.isSelected()) {
					map.setPlotGCPathEnabled(true);
					map.repaint();
				} else {
					map.setPlotGCPathEnabled(false);
					map.repaint();
				}
			}
		});
		c.gridy = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(plotGCPath, c);		
	}
	
		
	
	/**
	 * Update the interface upon the change in the given observable. 
	 */
	public void update(Observable o, Object arg) {
		// Update the label displaying the epi-center coordinates.
		// and set the center of reference for the filter.
		if (o instanceof UIChangeNotifier && o == map.getMapChangeNotifier()) {
			float[] centerCoords = map.getEpiCenterCoords();
			
			// If we use a point as the centre of reference.
			if (pointSelector.isSelected() && 
					centerCoords != null && centerCoords.length == 2) {
				
				// set the reference polar coordinates.			
				pointFilter.setRefLon(centerCoords[0]);
				pointFilter.setRefLat(centerCoords[1]);
	
				// Display longitude and latitude up to 3 decimal places
				pointCoordsLabel2.setText("("+ NumberFormat.getInstance().format(centerCoords[0]) 
						+ ", " +  NumberFormat.getInstance().format(centerCoords[1]) + ")");
			}
		}
	}
	
	

	/**
	 * @return The EventFilter which will take input information 
	 * from this DistanceRangePanel.
	 */
	public EventFilter getFilter() {
		return filter;
	}

}
