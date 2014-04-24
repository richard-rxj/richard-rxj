package seqi;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;

/**
 * This class is in charge of providing a user interface which cooperates 
 * all the event filters and exports the filtered events.
 * It also acts as a facade for communicating catalog data between the SEQI class
 * and the filters.
 *  
 */
@SuppressWarnings("serial")
public class FilterControlPanel extends JPanel implements java.util.Observer {
	/**
	 * The associated MapView
	 */
	private MapView map;
	
	/**
	 * The main application object/instance which gets the control from
	 * this ControlPanel object.
	 */
	private SEQI mainApp;
	
	/**
	 * The TabbedPane holding different filter UI in different tabs.
	 */
	private JTabbedPane tabPane;
	
	/**
	 * The distance range panel. 
	 */
	private final DistanceRangeFilterPanel distPanel;
	
 	// Buttons to trigger filtering and output
	private JButton filterButton;
	
	// The current list of filtered events.
	private EventList filteredEvents;
	
	// The set of event filters current under controlled via this ControlPanel.
	// private Set<EventFilter> eventFilters = new HashSet<EventFilter>();
	
	// A panel for users to input the start and end time of the
	// data requested, relative to the origin time of the earth quake events.
	private EventOutputPanel eventOutputPanel;	
	
	public FilterControlPanel(SEQI mainApp) {
		
		this.mainApp = mainApp;
		
		map = mainApp.getMapView();
		
		// Register interest in changes in the MapView. 
    	map.addObserver(this);
    	
    	// The whole panel UI
    	JPanel containerPanel = new JPanel();
    	containerPanel.setLayout(new GridBagLayout());
    	
		// Filter panel
		JPanel filterPanel = new JPanel();		
		filterPanel.setBorder(BorderFactory.createTitledBorder("Event filters"));
		filterPanel.setLayout(new GridBagLayout());		
		GridBagConstraints c1 = new GridBagConstraints();
		
		// Instruction label
		JLabel instruction = new JLabel("Select a filter below");				
		c1.gridx = 0; 
		c1.gridy = 0;
		c1.gridwidth = 1;
		c1.gridheight = 1;
		c1.weightx = 1;
		c1.weighty = 0;
		c1.fill = GridBagConstraints.BOTH;
		c1.anchor = GridBagConstraints.FIRST_LINE_START;
		filterPanel.add(instruction, c1);		 
		
		// A tabbed pane showing different filters.
		tabPane = new JTabbedPane(JTabbedPane.NORTH, JTabbedPane.WRAP_TAB_LAYOUT);
		
		distPanel = new DistanceRangeFilterPanel(mainApp);		
		tabPane.addTab("Distance Range", distPanel);
		//eventFilters.add(distPanel.getFilter());
		
		final MagnitudeRangeFilterPanel magPanel = new MagnitudeRangeFilterPanel();		
		tabPane.addTab("Magnitude Range", magPanel);
		//eventFilters.add(magPanel.getFilter());
		
		final DepthRangeFilterPanel depthPanel = new DepthRangeFilterPanel();
		tabPane.addTab("Depth Range", depthPanel);
		//eventFilters.add(depthPanel.getFilter());;
		
		final TimeWindowFilterPanel timePanel = new TimeWindowFilterPanel();
		tabPane.addTab("Time Window", timePanel);
		//eventFilters.add(timePanel.getFilter());
		
		final StationDateFilterPanel stationPanel = new StationDateFilterPanel(mainApp);
		tabPane.addTab("Station Deployment", stationPanel);
		//eventFilters.add(timePanel.getFilter());
		
		final CoordinateWindowFilterPanel coordPanel = new CoordinateWindowFilterPanel(mainApp.getMapView());
		tabPane.addTab("Coordinates Window", coordPanel);
		//eventFilters.add(coordPanel.getFilter());
		
		// Select the DistanceRangeFilter by default
		tabPane.setSelectedIndex(0);
		
		// If the DistanceRangeFilterPanel is selected,
		// fire a notification to the EventOutputPanel
		// to enable the choice of Arrival Time as reference time.
		tabPane.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
//				if (tabPane.getSelectedComponent() == distPanel) {
//					FilterControlPanel.this.eventOutputPanel.setRefTimeSelectorEnabled(true);	
//				} else {
//					FilterControlPanel.this.eventOutputPanel.setRefTimeSelectorEnabled(false);
//				}				
				
				// Check if the current filter has sufficient input
				FilterPanel fPanel = (FilterPanel)tabPane.getSelectedComponent();
				EventFilter filter = fPanel.getFilter();
				 
				if (filter.hasValidInput() && map.getEvents() != null 
						&& (!map.getEvents().getEvents().isEmpty()))
					filterButton.setEnabled(true);
				else 
					filterButton.setEnabled(false);	
			}
		});

		// Add the filter panel
		c1.gridx = 0; 
		c1.gridy = 1;		 
		c1.weighty = 1;
		c1.fill = GridBagConstraints.BOTH;		
		c1.anchor = GridBagConstraints.CENTER;
		filterPanel.add(tabPane, c1);
		
		// Filter button
		JPanel buttonPanel = new JPanel();
		filterButton = new JButton("Filter");
		c1.gridx = 0; 
		c1.gridy = 2; 
		c1.weighty = 0;
		c1.fill = GridBagConstraints.HORIZONTAL;		
		buttonPanel.add(filterButton);		
		filterPanel.add(buttonPanel, c1);
		
		// Disable search until all the search criteria have been met.
		filterButton.setEnabled(false);
		filterButton.addActionListener(new ActionListener(){
			/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				FilterPanel fPanel = (FilterPanel)tabPane.getSelectedComponent();
				
				// if no event catalog loaded yet, ignore it
				// EventList catalogEvents = ControlPanel.this.mainApp.getCatalogEvents();
				
				// Filter the current resulting list of events
				// if this list is still empty, do nothing
				if (filteredEvents != null) {
					filteredEvents = fPanel.getFilter().filter(filteredEvents);
										
					// Set the filter to be drawn.
					map.setFilter(fPanel.getFilter());
					
					// Plot just the filtered events.
					map.setEvents(filteredEvents);
					
					// Disable the filter button until any change in the user input is made. 
					// filterButton.setEnabled(false);
				}
			}
		});		
		
		// Add the filter panel to the whole panel
		GridBagConstraints c = new GridBagConstraints();		
		c.gridx = 0; 
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;		
		c.weightx = 1; 
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;		
		// c.anchor = GridBagConstraints.LINE_START;
		containerPanel.add(filterPanel, c);
		
		// A panel for users to input the start and end time of the
		// data requested, relative to the origin time of the earth quakes.
		// Component blankArea = Box.createRigidArea(new Dimension(200, 40)); 
		c.gridx = 0; 
		c.gridy = 1;
		// containerPanel.add(blankArea, c);
		containerPanel.add(new JSeparator(), c);
		
		// A panel for users to input the start and end time of the
		// data requested, relative to the origin time of the earth quakes.
		eventOutputPanel = new EventOutputPanel(mainApp, this);
		c.gridx = 0; 
		c.gridy = 2;
		// c.anchor = GridBagConstraints.LINE_START;		
		containerPanel.add(eventOutputPanel, c);
		
		// Add the container of the filter and dataPeriod panels
    	this.add(containerPanel);
	}
	

	/**
	 * Update the interface upon the change in the given observable. 
	 */
	public void update(Observable o, Object arg) {
		// Update the label displaying the epi-center coordinates.
		// and set the center of reference for the filter.
		if (o instanceof UIChangeNotifier && o == map.getMapChangeNotifier()) {
			
			FilterPanel fPanel = (FilterPanel)tabPane.getSelectedComponent();
			EventFilter filter = fPanel.getFilter();
			 
			if (filter.hasValidInput() && map.getEvents() != null 
					&& (!map.getEvents().getEvents().isEmpty()))
				filterButton.setEnabled(true);
		}
	}
	
	// Set the current list of events resulting from filtering.	
	protected void setFilteredEvents(EventList filteredEvents) {
		this.filteredEvents = filteredEvents;
	}
	
	protected EventList getFilteredEvents() {
		return filteredEvents;
	}
	
	
	/**
	 * @return The AbstractDistanceRangeFilter current in use.
	 */
	protected DistanceRangeFilterPanel getDistanceRangeFilterPanel() {
		return distPanel;
	} 
	
//	/**
//	 * @return The set of event filters controlled via this ControlPanel.
//	 */
//	public Set<EventFilter> getEventFilters() {
//		return eventFilters;
//	}
//	
//	/**
//	 * @param filterClass The subtype of EventFilter that is the specific 
//	 * 		type of filter to be retrieved.
//	 * 
//	 * @return An event filter of the given type, which is currently under 
//	 * 		the control of this ControlPanel.
//	 */
//	protected EventFilter getEventFilter(java.lang.Class filterClass) {		
//		for (EventFilter filter : eventFilters) {
//			if (filterClass.isInstance(filter)) {
//				return filter;
//			}
//		} 
//		return null;
//	}
	
	
}
