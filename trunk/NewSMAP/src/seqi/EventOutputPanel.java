package seqi;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * EventOutputPanel displays the panel containing parameters 
 * to specify the start and end time of the data segment
 * to be written out for each selected seismic event.
 * 
 * Copyright (C) 2010, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Oct 26, 2010
 */
public class EventOutputPanel extends JPanel {
	
	/**
	 * The UI for users to input the start and end time of 
	 * the interval of the requested data, 
	 * relative to the origin time of the earth quakes.
	 */
	private JSpinner startHour;
	/**
	 * The UI for users to input the start and end time of 
	 * the interval of the requested data, 
	 * relative to the origin time of the earth quakes.
	 */
	private JSpinner startMin;
	/**
	 * The UI for users to input the start and end time of 
	 * the interval of the requested data, 
	 * relative to the origin time of the earth quakes.
	 */
	private JSpinner startSec;
	/**
	 * The UI for users to input the start and end time of 
	 * the interval of the requested data, 
	 * relative to the origin time of the earth quakes.
	 */
	private JSpinner endHour;
	/**
	 * The UI for users to input the start and end time of 
	 * the interval of the requested data, 
	 * relative to the origin time of the earth quakes.
	 */
	private JSpinner endMin;
	/**
	 * The UI for users to input the start and end time of 
	 * the interval of the requested data, 
	 * relative to the origin time of the earth quakes.
	 */
	private JSpinner endSec;
	
	/**
	 * 
	 */
	private JButton outputButton;
	
	
	/**
	 * 
	 */
	private JFileChooser fc;
	
	
	/**
	 * The combo box to select the reference time (origin 
	 * or arrival time of events)
	 */
	private JComboBox refTimeSelector;
	
	/**
	 * Combo boxes for users to select the phase and propagation
	 * model of the seismic wave. 
	 */
	private JComboBox phaseComboBox, propagationModelCB;
	
	
	/**
	 * A UI component for selecting the destination location 
	 * for computing travel time.
	 */
	private DestinationSelector destSelector;	
	
	/**
	 * Label displaying error message if the start and end time 
	 * are out of order.
	 */
	private JLabel checkTimeLabel;
	
	/** A listener for start and end time change. */
	private ChangeListener timeChangeListener; 
	
	/**
	 * The filter panel which filters events to be written out 
	 * through this EventOutputPanel. 
	 */
	private final FilterControlPanel filterPanel;
	
	/**
	 * The main application object/instance associated with 
	 * this EventOutputPanel object.
	 */
	private SEQI mainApp;
	
	/**
	 * Reference time choices: event's origin time 
	 * and event's arrival time at a selected destination.
	 */
	private static final String REF_TIME_CHOICES[] = {"Event's Origin Time",  "Event's Arrival Time"};
	
	/**
	 * All the possible phases for which travel time 
	 * of an event can be computed. 
	 */
	// static final String[] PHASE_NAMES = new String[]{"Earliest", "P", "S", "PcP", "ScS", "PKiKP", "SKiKS"};
	static final String[] PHASE_NAMES = new String[]{"Earliest", "P", "S", "Pn", "Sn", "PcP", "ScS", 
		"Pdiff", "Sdiff", "PKP", "SKS", "PKiKP", "SKiKS", "PKIKP", "SKIKS"};
	
	/** 
	 * Create a panel for users to input the start and end time of 
	 * the interval of the requested data, 
	 * relative to the origin time of the earth quakes.
	 * 
	 * @param mainApp The set of stations to be displayed. 
	 * 
	 * @param filterPanel The filter panel which filters events to be 
	 * 	written out through this EventOutputPanel.
	 * 
	 * @return
	 */
	public EventOutputPanel(SEQI mainApp, FilterControlPanel filterPanel){

		this.mainApp = mainApp;
		
		this.filterPanel = filterPanel;
		this.setLayout(new GridBagLayout());
		
		// Create the cards JPanel with a CardLayout
		final JPanel cards = new JPanel();
		cards.setLayout(new CardLayout());		
		JPanel originTimePanel = new JPanel();
		originTimePanel.setToolTipText("Origin time of seismic events");
		cards.add(originTimePanel, REF_TIME_CHOICES[0]);
		
		
		JPanel arrivalTimePanel = createArrivalTimePanel();
		arrivalTimePanel.setToolTipText("Arrival time of seismic events at a station");
		cards.add(arrivalTimePanel, REF_TIME_CHOICES[1]);
		
		
		// Combo box for card selection.
		JPanel referenceTimePanel = new JPanel(); 
		referenceTimePanel.setLayout(new BorderLayout()); 
		referenceTimePanel.setBorder(BorderFactory.createTitledBorder("Reference Time"));
		
		
		refTimeSelector = new JComboBox(REF_TIME_CHOICES);
        refTimeSelector.setEditable(false);        
        refTimeSelector.addItemListener(new ItemListener() {		
			public void itemStateChanged(ItemEvent e) {
				CardLayout cl = (CardLayout)(cards.getLayout());
		        cl.show(cards, (String)e.getItem());
			}
		});
        refTimeSelector.setSelectedIndex(1); // Use arrival time by default
        
        
        refTimeSelector.setSelectedIndex(1); // choose arrival time by default
        referenceTimePanel.add(refTimeSelector, BorderLayout.NORTH);
        referenceTimePanel.add(cards, BorderLayout.CENTER);
        
        
        // Add the main components
		GridBagConstraints c = new GridBagConstraints();
        
		// The reference time panel
		c.gridx = 0; 
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;				
		this.add(referenceTimePanel, c);
        
		// The start and end time panel
        c.gridx = 0;
        c.gridy = 1;
        JPanel timePanel = createTimeSegmentPanel(); 
        this.add(timePanel, c);
        
        // The button panel
        c.gridx = 0;
        c.gridy = 2;		
        JPanel buttonPanel = createButtonPanel(); 
        this.add(buttonPanel, c); 
        
        
		// Time change listeners
		timeChangeListener = new ChangeListener(){
			public void stateChanged(ChangeEvent e) {								
				if (checkStartEndTime() == false) {					
					checkTimeLabel.setText("End time is before start time");
					outputButton.setEnabled(false);
				} else if (EventOutputPanel.this.filterPanel.getFilteredEvents() != null) {
					checkTimeLabel.setText(" ");
					outputButton.setEnabled(true);
				} else {
					outputButton.setEnabled(false);
				}
			}
		};
		
	}
	
	/** 
	 * Enable/disable the reference time selector
	 * If disabled, only origin time can be selected as reference time.
	 * 
	 * @param b true to enable, false to disable.
	 */
	protected void setRefTimeSelectorEnabled(boolean b) {
        if (b == false) {
        	// Select "origin time" as reference time
        	refTimeSelector.setSelectedIndex(0);
        }
        
        // This has to be executed after setSelectedIndex 
        // in case we need to disable refTimeSelector.
        refTimeSelector.setEnabled(b);       
	}
	
				
	/** Time Panel */
	private JPanel createArrivalTimePanel() {
		// Create the time panel (with respect to the origin time of the earthquakes)
		JPanel arrivalTimePanel = new JPanel(new FlowLayout());
		arrivalTimePanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0; 
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
        
        // Phase label 
		c.gridx = 0; 
		c.gridy = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
        arrivalTimePanel.add(new JLabel("Phase"), c);
        
        // Model label 
		c.gridx = 1; 
		c.gridy = 1;
		arrivalTimePanel.add(new JLabel("Model"), c);
        
        // Phase combobox 
		c.gridx = 0; 
		c.gridy = 2;
		c.weightx = 0.5;
		// phaseComboBox = new JComboBox(new String[]{"Earliest", "P", "S", "Pn", "Sn", "PcP", 
		//		"ScS", "Pdiff", "Sdiff", "PKP", "SKS", "PKiKP", "SKiKS", "PKIKP", "SKIKS"});
		phaseComboBox = new JComboBox(PHASE_NAMES);		
		arrivalTimePanel.add(phaseComboBox, c);
		
        // Model label 
		c.gridx = 1; 
		c.gridy = 2;
		c.weightx = 0.5;
		propagationModelCB = new JComboBox(new String[]{"iasp91", "ak135", "prem", "qdt", 
				"1066a", "1066b", "alfs", "herrin", "jb", "sp6", "pwdk"});
		arrivalTimePanel.add(propagationModelCB, c);
		
		// UI to select a destination station
		c.gridx = 0; 
		c.gridy = 3;		
		c.gridwidth = 2;
		c.gridheight = 1;
		destSelector = new DestinationSelector(mainApp);
        arrivalTimePanel.add(destSelector, c);
		        
        // Add a separator
        // c.gridy = 2;		
		// arrivalTimePanel.add(new JSeparator(), c);
		
		return arrivalTimePanel;
	}

	/** 
	 * Create a Panel containing the start and end time of the data cut
	 * with respect to the reference time. 
	 */
	private JPanel createTimeSegmentPanel() {

		// Create the time panel for selecting the start and end time of the data segment extracted.
		JPanel timePanel = new JPanel();
		timePanel.setBorder(BorderFactory.createTitledBorder("Cut-off Time Range"));
		timePanel.setToolTipText("Start and end time of the data requested \n relative to the chosen reference time.");
		timePanel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		// Instruction labels 
//		JLabel instruction1 = new JLabel("Start and End Points"); 
//		JLabel instruction2 = new JLabel("relative to Reference Time");				
//		c.gridx = 0; 
//		c.gridy = 0;
//		c.weightx = 1; 
//		c.weighty = 0.1;
//		c.fill = GridBagConstraints.BOTH;	
//		c.anchor = GridBagConstraints.CENTER;
//		timePanel.add(instruction1, c);		 
//		
//		c.gridy = 1;		
//		timePanel.add(instruction2, c);
//
//		c.gridy = 2;
//		timePanel.add(new JLabel("   "), c);
		
		// First row				
		c.gridx = 0; 
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;		
		c.weightx = 1;
		c.weighty = 0.35;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		
		// Start time panel.
		JPanel startTimePanel = createStartTimePanel();
		timePanel.add(startTimePanel, c);
	
		c.gridy = 4;
		timePanel.add(new JLabel("    "), c);
		
		// End time panel.
		JPanel endTimePanel = createEndTimePanel();
		c.gridy = 5;
		timePanel.add(endTimePanel, c);
		
		// Label displaying error message about the time order 
		// between the input start and end time.
		c.gridy = 4;
		c.weighty = 0.1;
		checkTimeLabel = new JLabel(" ");
		checkTimeLabel.setFont(new Font("Arial", Font.ITALIC, 12));
		checkTimeLabel.setForeground(Color.RED);
		timePanel.add(checkTimeLabel, c);
		
		return timePanel;
	}
	
	private JPanel createButtonPanel() {
		// Button panel
		JPanel buttonPanel = new JPanel();

		// Output to file button.
		outputButton = new JButton("Generate Time Intervals ...");
		buttonPanel.add(outputButton);
		
		// Disable search until all the search criteria have been specified.
		// outputButton.setEnabled(false);
		fc = new JFileChooser(new File("."));
		outputButton.addActionListener(new ActionListener(){
			/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				
				// If we use the arrival time
				if (refTimeSelector.getSelectedItem().equals(REF_TIME_CHOICES[1])) {
	            	fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

	            	int returnVal = fc.showDialog(EventOutputPanel.this, "Output Time Intervals");			
					
			        if (returnVal == JFileChooser.APPROVE_OPTION) {	
		            				            	
		            	// Retrieve the list of destination stations or destination point.		            	
		            	StationInfo[] stations = EventOutputPanel.this.destSelector.getSelectedStations();
		                
		            	if (stations != null) {
		            		// Write to a collection of files located under the chosen directory.
			            	// Each filename contains the station code and a time stamp.  
				            File selectedDir = fc.getSelectedFile();
			            		
		            		// Define the date/time format for the output directory corresponding 
		            		// to each station.
		            		String dateTimeFormat = "yyyy-MM-dd_HH:mm:ss";
			                SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
			                Calendar cal = Calendar.getInstance();				            	
			                String timeString = sdf.format(cal.getTime());
			                
			            	for (StationInfo stationInfo : stations) {
			    				System.out.println("Writing time intervals for station " + stationInfo.station + " ...");
			            		
			            		float destLon = stationInfo.lon;
				            	float destLat = stationInfo.lat;
				            	String stationCode = stationInfo.station;
				            	

				            	String phaseName = (String)EventOutputPanel.this.phaseComboBox.getSelectedItem();
				            	String modelName = (String)EventOutputPanel.this.propagationModelCB.getSelectedItem();
				            	
				            	File stationFile = new File(selectedDir, stationCode + "_" + phaseName + "_" + modelName + "_" + timeString + ".txt"); 
				            	
				            	EventWriter.writeSegmentTimeWrtArrivalTime(EventOutputPanel.this.filterPanel.getFilteredEvents(), stationFile, 
				            			destLon, destLat, phaseName, modelName, 
										((Integer)startHour.getValue()).intValue(), ((Integer)startMin.getValue()).intValue(), ((Integer)startSec.getValue()).intValue(), 
										((Integer)endHour.getValue()).intValue(), ((Integer)endMin.getValue()).intValue(), ((Integer)endSec.getValue()).intValue());
				            	
				            	System.out.println("Finished for station " + stationInfo.station);
			            	}
		            	} else {
		            		System.out.println("No station selected.");
		            	}         
			        }
	            } 
	            else if (refTimeSelector.getSelectedItem().equals(REF_TIME_CHOICES[0])) { // if we use the origin time.
	            	
	            	fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	            	
	            	int returnVal = fc.showDialog(EventOutputPanel.this, "Output Time Intervals");			
					
			        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            	// Write to the selected file.
			            File file = fc.getSelectedFile();
			            						
		            	EventWriter.writeSegmentTimeWrtOriginTime(EventOutputPanel.this.filterPanel.getFilteredEvents(), file, 
							((Integer)startHour.getValue()).intValue(), ((Integer)startMin.getValue()).intValue(), ((Integer)startSec.getValue()).intValue(), 
							((Integer)endHour.getValue()).intValue(), ((Integer)endMin.getValue()).intValue(), ((Integer)endSec.getValue()).intValue());
			        }
	            }		            
			}
		});		
		
		return buttonPanel;
	}
	
	
	private JPanel createStartTimePanel() {
		JPanel startTimePanel = new JPanel();
		startTimePanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		// First row
		JLabel startLabel = new JLabel("Starts at");
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 6;
		c.gridheight = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		startTimePanel.add(startLabel, c);


		// Second row - row to specify time
		startHour = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
		startHour.addChangeListener(timeChangeListener);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0.33;
		c.fill = GridBagConstraints.HORIZONTAL;
		startTimePanel.add(startHour, c);
		
		
		JLabel hourLabel = new JLabel("hrs");
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		startTimePanel.add(hourLabel, c);

		startMin = new JSpinner(new SpinnerNumberModel(2, 0, 59, 1));
		startMin.addChangeListener(timeChangeListener);
		c.gridx = 2;
		c.gridy = 1;
		c.weightx = 0.33;
		c.fill = GridBagConstraints.HORIZONTAL;
		startTimePanel.add(startMin, c);

		JLabel minLabel = new JLabel("mins");
		c.gridx = 3;
		c.gridy = 1;
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		startTimePanel.add(minLabel, c);

		startSec = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
		startSec.addChangeListener(timeChangeListener);
		c.gridx = 4;
		c.gridy = 1;
		c.weightx = 0.33;
		c.fill = GridBagConstraints.HORIZONTAL;
		startTimePanel.add(startSec, c);
		
		JLabel secondLabel = new JLabel("secs");		
		c.gridx = 5;
		c.gridy = 1;
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		startTimePanel.add(secondLabel, c);
		
		// Last row
		JLabel refTimeLabel = new JLabel("before the reference time");
     	c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 6;
		c.gridheight = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		startTimePanel.add(refTimeLabel, c);

		return startTimePanel;
	}

	
	
	/** Panel to enter end time. */
	private JPanel createEndTimePanel() {
		JPanel endTimePanel = new JPanel(); 
		endTimePanel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		// First row
		JLabel endLabel = new JLabel("Ends at");		
		c.gridx = 0; 
		c.gridy = 0;
		c.gridwidth = 6;
		c.gridheight = 1;		
		c.weightx = 1; 
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;		
		endTimePanel.add(endLabel, c);

		
		// Third row - row of text fields.		
		endHour = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
		endHour.addChangeListener(timeChangeListener);
		
		c.gridx = 0; 
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0.33;
		c.fill = GridBagConstraints.HORIZONTAL;
		endTimePanel.add(endHour, c);
		
		JLabel separationLabel1 = new JLabel("hrs");
		c.gridx = 1; 
		c.gridy = 1;
		c.weightx = 0; 
		c.fill = GridBagConstraints.NONE;
		endTimePanel.add(separationLabel1, c);
		
		// default value = 10 mins after earthquake's origin time
		endMin = new JSpinner(new SpinnerNumberModel(10, 0, 59, 1));
		endMin.addChangeListener(timeChangeListener);
		c.gridx = 2; 
		c.gridy = 1;
		c.weightx = 0.33;
		c.fill = GridBagConstraints.HORIZONTAL;
		endTimePanel.add(endMin, c);
		
		
		JLabel separationLabel2 = new JLabel("mins");
		c.gridx = 3; 
		c.gridy = 1;
		c.weightx = 0; 
		c.fill = GridBagConstraints.NONE;
		endTimePanel.add(separationLabel2, c);
		
		
		endSec = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
		endSec.addChangeListener(timeChangeListener);
		c.gridx = 4; 
		c.gridy = 1;
		c.weightx = 0.33;
		c.fill = GridBagConstraints.HORIZONTAL;
		endTimePanel.add(endSec, c);
		
		
		JLabel secondLabel = new JLabel("secs");		
		c.gridx = 5;
		c.gridy = 1;
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		endTimePanel.add(secondLabel, c);	
		
		
		// Last row
		JLabel refTimeLabel = new JLabel("after the reference time");
     	c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 6;
		c.gridheight = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		endTimePanel.add(refTimeLabel, c);
		
		return endTimePanel;
	}
	
	

	/**
	 * Check that the end time is after the start time.
	 * endTime > startTime.
	 * @return true if this condition holds, false otherwise.
	 */
	private boolean checkStartEndTime() {
		int sHour = ((Integer)startHour.getValue()).intValue();
		int sMin = ((Integer)startMin.getValue()).intValue();
		int sSec = ((Integer)startSec.getValue()).intValue();
		
		int eHour = ((Integer)endHour.getValue()).intValue();
		int eMin = ((Integer)endMin.getValue()).intValue();
		int eSec = ((Integer)endSec.getValue()).intValue();
		
		// End time <= start time in the following cases.
		if (eHour < sHour)
			return false;
		
		if ((eHour == sHour) && (eMin < sMin))
			return false;
		
		if ((eHour == sHour) && (eMin == sMin) && (eSec <= sSec))
			return false;
		
		// Otherwise true
		return true;
	}
	
	
	
	

	/** 
	 * A class containing the UI to help users select
	 * the destination of seismic events propagation. 
	 */
	private class DestinationSelector extends JPanel implements Observer {
		
		// A label which displays 
		// the destination location (and station name if available)
		private JLabel destInfoLabel;

		
		// JRadioButtons that provide an option to select the current epi-center
		// of the distance range filter or a new station location
		// as destinations for computing travel time of events.
		private JRadioButton selectCurrentCenter, selectNewCenter;
		
		// The destination longitude and latitude for computing 
		// the travel time.
		// private float destLon, destLat;
		
		// A list of stations selected by this DestinationSelector
		// private StationInfo[] selectedStations;
		
		private SEQI mainApp;

		// A dialog asking users to choose a destination location
		// for computing the arrival times of events. 
		private StationSelectionDialog selectionDialog;		

		
		DestinationSelector(SEQI mainApp) {
			this.mainApp = mainApp;
			
			// Add listener for the change in epi-center location
			mainApp.getMapView().addObserver(this);
			
			this.setLayout(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
	        
			// The reference time panel
			c.gridx = 0; 
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.gridwidth = 1;
			c.gridheight = 1;
			c.fill = GridBagConstraints.BOTH;
			c.anchor = GridBagConstraints.CENTER;			
			JLabel blankLabel = new JLabel(" ");			
	        this.add(blankLabel, c);
	        
	        c.gridy = 1;
			JLabel chooseDestLabel = new JLabel("Select Destinations");
			this.add(chooseDestLabel, c);
	        
	        // Add a group of radio buttons for selecting options here.
	        final String currentCenterString = "Range Filter's Epi-centre"; 
	        selectCurrentCenter = new JRadioButton(currentCenterString);	        
	        selectCurrentCenter.setActionCommand(currentCenterString);
	        selectCurrentCenter.setEnabled(false);	        
	        
	        // Register a listener to display the location of the currently 
	        // selected epi-center.        
//	        selectCurrentCenter.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					
//					// Display the location of the current epicenter
//					// of the DistanceRangeFilter
//					// float[] centerCoords = DestinationSelector.this.mainApp.getMapView().getEpiCenterCoords();
//					//DestinationSelector.this.destLon = centerCoords[0];
//					//DestinationSelector.this.destLat = centerCoords[1];
//					
//					// If the center of the distance range filter has been specified.
//					//if (centerCoords != null && centerCoords.length == 2)
//					//	DestinationSelector.this.destInfoLabel.setText
//					//	("("+ DestinationSelector.this.destLon + ", "
//					//		+ DestinationSelector.this.destLat + ")");
//					
//				}
//			});
	        
	        
	        final String newCenterString = "Other Stations";
	        selectNewCenter = new JRadioButton(newCenterString);   
	        selectNewCenter.setActionCommand(newCenterString);
	        
	        //Register a listener for selecting a new destination station.
	        selectNewCenter.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (DestinationSelector.this.selectionDialog == null) {
						DestinationSelector.this.selectionDialog
							= new StationSelectionDialog(DestinationSelector.this.mainApp);
					}
					else {
						DestinationSelector.this.selectionDialog.pack();
						DestinationSelector.this.selectionDialog.setVisible(true);												
					}

				}
			});
	        
	        
	        // Group the radio buttons.
	        ButtonGroup selectCenterGroup = new ButtonGroup();
	        selectCenterGroup.add(selectCurrentCenter);
	        selectCenterGroup.add(selectNewCenter);       
	        
	        	        
	        c.gridy = 2;
	        this.add(selectCurrentCenter, c);
	        
	        c.gridy = 3;
	        this.add(selectNewCenter, c);
	        
	        
	        // Add a label displaying the destination location (and station name if available)
	        //JLabel coordsString = new JLabel("Dest Coords (long, lat)"); 
	        //c.gridy = 3;
	        //this.add(coordsString, c);
	        
	        //destInfoLabel = new JLabel("    ");
	        //c.gridy = 4;
	        //this.add(destInfoLabel, c);
		}
		
//		
//		/**
//		 * @return the longitude of the destination location for computing the travel time.
//		 */
//		float getDestLon() {
//			return destLon;
//		}
//		
//		/**
//		 * @return the latitude of the destination location for computing the travel time.
//		 */
//		float getDestLat() {
//			return destLat;		
//		}
		

		/**
		 * @return The list of stations selected via this interface.
		 * If Range Filter's Epi-centre is selected, then we select the stations used 
		 * as the epi-centres for the distance range filter.
		 * Otherwise we take the list of stations from the StationSelectionDialog.
		 *   
		 */
		public StationInfo[] getSelectedStations() {
			if (selectCurrentCenter.isSelected()) {
				EventFilter filter = mainApp.getFilterControlPanel().getDistanceRangeFilterPanel().getFilter();
				
				if (filter instanceof UnionDistanceRangeFilter) {
					List<StationInfo> filterStations = ((UnionDistanceRangeFilter)filter).getStations();
					StationInfo[] allStations = new StationInfo[filterStations.size()];
					return filterStations.toArray(allStations);
				}
				else if (filter instanceof AtomicDistanceRangeFilter) {
					// Return a surrogate station in case we have selected a single point as the epi-centre  
					float lat = ((AtomicDistanceRangeFilter)filter).getRefLat();
					float lon = ((AtomicDistanceRangeFilter)filter).getRefLon();
					StationInfo station = new StationInfo("Unspecified", "Unspecified", lat, lon, 0, 
							(GregorianCalendar)GregorianCalendar.getInstance(), 
							(GregorianCalendar)GregorianCalendar.getInstance(),
							"Unspecified", "Unspecified");
					return new StationInfo[]{station};
				} else{ 
					// The program should never reach this case, 
					// unless there is another option for select the epi centre for the distance range filter.
					return null;
				}
				
			} else if (selectNewCenter.isSelected()) {
				return selectionDialog.getSelectedStations();
			} else { // The program should never reach this case, 
					 // unless the JRadioButton group contains members other than 
					 // the selectCurrentCenter and the selectCurrentCenter buttons					 
				return null;
			} 
			 
		}


		/**
		 * Update the interface upon the change in the given observable. 
		 */
		public void update(Observable o, Object arg) {
			
			// Update the label displaying the epi-center coordinates.
			// and set the center of reference for the filter.
			if (o instanceof UIChangeNotifier && o == mainApp.getMapView().getMapChangeNotifier()) {
				
				float[] centerCoords = mainApp.getMapView().getEpiCenterCoords();
				
				// If the center of the distance range filter has been specified.
				if (centerCoords != null && centerCoords.length == 2) {
					
					// Enable the option of selecting the current epi-center
					selectCurrentCenter.setEnabled(true);
					
					// if (selectCurrentCenter.isSelected())						
					//	destInfoLabel.setText("(" + centerCoords[0] + ", "+ centerCoords[1] + ")");
				} else {
					
					// Enable the option of selecting the current epi-center
					selectCurrentCenter.setEnabled(false);					
				}
			}
		}		
	}
}
