package seqi;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import util.FileTypeFilter;
import util.GUIUtils;

public class SEQI extends JFrame {

	/** Home location of SEQI */
	// public static final String SEQI_HOME = "/home/seis/huynh/development/SEQI";
	public static final String SEQI_HOME = "/Tdata/public/SEISMOMAP/SEQI/current";

	/** Location of the station info file. */
	protected static final String INSTR_COORD_FILE = "/Tdata/public/SDC/instr.coord";
	// private static final String INSTR_COORD_FILE = "./data/instr.coord";

	/** Directory location of the catalog files. */
	protected static final String CATALOG_DIR = SEQI_HOME + "/data";
	
	private MapView mapView;
	private FilterControlPanel filterControlPanel;
	private CatalogInfoPanel catInfoPanel;
	private NewCatalogInfoPanel newcatInfoPanel;   //-------------------v1.7
	private MapInteractionInfoPanel mapInteractionInfoPanel;
	protected StationInfoDisplay stationInfoDisplay;
	
	// A list view of all the displayed events on the MapView.
	private EventListView eventListView;  
	
	// A list of all the stations currently loaded into this application.
	private Set<StationInfo> stations; 

	private CatalogFileReader catalogReader = new CatalogFileReader();

	// A cache storing the catalog events in each catalog file loaded
	// into this application. The cache helps retrieving the event list
	// straight from the memory without the need for reading the file a second
	// time.
	// This maps a catalog file to the event list stored in it.
	private Map<File, EventList> catalogCache = new HashMap<File, EventList>();

	/**
	 * The set of all events in the catalog currently loaded into this
	 * application.
	 */
	private EventList catEventList;
	
	/** 
	 * The only one copy of this application (implemented 
	 * based on the Singleton pattern)
	 */
	//private static SEQI singletonSEQI = null;
	
	/**
	 * @return The single copy of this application in memory.
	 */
//	static SEQI singletonSEQI() {
//		if (singletonSEQI == null)
//			singletonSEQI = new SEQI("Seismic Event Query Interface");
//		
//		return singletonSEQI;
//	}

	public SEQI(String title) {
		super(title);

		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());

		// Add the main component in the middle
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		contentPane.add(mainPanel, BorderLayout.CENTER);

		// load the instrument coordinate file (station info file)
		StationFileReader stationReader = new StationFileReader();
		File statCoordFile = new File(INSTR_COORD_FILE);
		stations = stationReader.parseFile(statCoordFile);

		// Add the map information panel to the top of the map panel.
		JPanel mapPanel = new JPanel(new BorderLayout());

		// the map view
		mapView = new MapView(this);
		mapView.setStations(stations);
		mapPanel.add(mapView, BorderLayout.CENTER);

		
		// Add a menubar
		JMenuBar menuBar = createMenuBar();
		setJMenuBar(menuBar);

		// Add a toolbar
		JToolBar toolBar = createToolBar();
		contentPane.add(toolBar, BorderLayout.PAGE_START);


		// The map information panel, consisting of station and
		// catalog information
		// and legends ...
		JPanel infoPanel = new JPanel();
		// infoPanel.setPreferredSize(new Dimension(800, 250));
		GridBagLayout gridbag = new GridBagLayout();
		infoPanel.setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();

		// Add the catalog list and the information panel for each catalog.
		catInfoPanel = new CatalogInfoPanel(this);
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0;
		c.weighty = 1;
		infoPanel.add(catInfoPanel, c);
		
		
		// Add the catalog list filtered by startyear and endyear----------------------v1.7
		newcatInfoPanel = new NewCatalogInfoPanel(this);
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0;
		c.weighty = 1;
		infoPanel.add(newcatInfoPanel, c);

		// Add network and station list (a drop down menu)
		// StationListPanel stationListDisplay = new StationListPanel(this);
		// c.gridx = 1;
		// c.weightx = 0;
		// infoPanel.add(stationListDisplay, c);

		// Add station detail
		stationInfoDisplay = new StationInfoDisplay();
		c.gridx = 2;
		c.weightx = 0.5;
		infoPanel.add(stationInfoDisplay, c);

		// Add a legend for event sizes and colours
		EventLegend legend = new EventLegend(DrawStyleManager.magLowerBounds,
				DrawStyleManager.scales, DrawStyleManager.depthLowerBounds,
				DrawStyleManager.colors);
		c.gridx = 3;
		c.weightx = 0.5;
		infoPanel.add(legend, c);
		mapPanel.add(infoPanel, BorderLayout.NORTH);

		// The map interaction information panel at the bottom.
		mapInteractionInfoPanel = new MapInteractionInfoPanel();
		mapPanel.add(mapInteractionInfoPanel, BorderLayout.SOUTH);

		// Add the map panel
		mainPanel.add(mapPanel, BorderLayout.CENTER);

		// the control panel (for event filtering and other tasks)
		filterControlPanel = new FilterControlPanel(this);
		filterControlPanel.setToolTipText("Control Panel");
		mainPanel.add(filterControlPanel, BorderLayout.WEST);
		

		// Set window event listeners
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Display the window
		pack();
		setSize(1400, 1000);
		setVisible(true);

		// Translate the map horizontally to
		// center the map around the Asia-Pacific region (by default)
		mapView.resetView();

	}

	private JMenuBar createMenuBar() {
		// Create the menu bar.
		JMenuBar menuBar = new JMenuBar();

		// Build the File menu.
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		fileMenu.setToolTipText("File");
		menuBar.add(fileMenu);

		// Build a group of JMenuItems under File menu

		// Open Catalogue menu item
		JMenuItem openFile = new JMenuItem("Open Catalogue ...", KeyEvent.VK_O);
		fileMenu.add(openFile);
		CatFileOpenListener openListener = new CatFileOpenListener(this);
		openFile.addActionListener(openListener);
		
		// Load Map menu item
		JMenuItem loadMap = new JMenuItem("Load Map ...", KeyEvent.VK_O);
		fileMenu.add(loadMap);
		LoadMapListener loadMapListener = new LoadMapListener(this);
		loadMap.addActionListener(loadMapListener);


		// "Quit" menu item
		JMenuItem quit = new JMenuItem("Quit", KeyEvent.VK_Q);
		fileMenu.add(quit);
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		menuBar.setBorder(new BevelBorder(BevelBorder.RAISED));

		return menuBar;
	}

	private JToolBar createToolBar() {
		JToolBar toolBar = new JToolBar("SEQI_toolbar");

		JButton saveMap = new JButton("Save Map ...");
		toolBar.add(saveMap);

		final JFileChooser saveFC = new JFileChooser(".");
		Set<FileTypeFilter> fileFilters = GUIUtils.getImageFileFilters();
		for (FileTypeFilter fileFilter : fileFilters) {
			saveFC.addChoosableFileFilter(fileFilter);
		}

		saveMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * Save the buffered image of the map in the selected format.
				 * The selected item will be the name of the format to use. Use
				 * the format name to initialize the file suffix. Format names
				 * typically correspond to suffixes
				 */
				int rval = saveFC.showSaveDialog(SEQI.this);
				
				if (rval == JFileChooser.APPROVE_OPTION) {
					File saveFile = saveFC.getSelectedFile();
					String filterExt = ((FileTypeFilter) saveFC.getFileFilter())
							.getExtension();
					
					
					/*
					 * Write the filtered image in the selected format, to the
					 * file chosen by the user.
					 */
					try {
						// If the file name doesn't end with an extension,
						// we append the selected extension to the name.
						String fName = saveFile.getName();
						int dotLoc = fName.lastIndexOf('.');

						String fileExt = fName.substring(dotLoc + 1);

						if (!filterExt.equalsIgnoreCase(fileExt))
							saveFile = new File(saveFile.getAbsolutePath()
									+ '.' + filterExt);

						BufferedImage bufferedImage = SEQI.this.mapView.getBufferedMap();
						ImageIO.write(bufferedImage, filterExt, saveFile);

					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		toolBar.addSeparator();

		// Add the catalog list and the information panel for each catalog.
		JButton defaultView = new JButton("Default View");
		toolBar.add(defaultView);
		defaultView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mapView.resetView();
			}
		});

//		JButton refreshView = new JButton("Refresh");
//		toolBar.add(refreshView);
//		refreshView.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				// mapView.repaint();
//				mapView.clearFilterMarkers();
//			}		
//		});

		
		// A button to display event information.
		JButton eventInfo = new JButton("Event Information ...");
		toolBar.add(eventInfo);
		eventInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				// Create the list view on demand.
				if (eventListView == null) {					
					// An event list view in a separate JFrame.
					eventListView = new EventListView(mapView);
					
					// eventListView.setSize(500, SEQI.this.getHeight());
					mapView.addObserver(eventListView);
				} else {					
					// Show the event list information.
					eventListView.setVisible(true);
				}
			}
		});
		
		
		// A checkbox indicating whether to display stations on the map.
		final JCheckBox hideStations = new JCheckBox("Hide stations");		
		toolBar.add(hideStations);
		hideStations.addActionListener(new ActionListener() {		
			public void actionPerformed(ActionEvent e) {
				mapView.setShowStations(!hideStations.isSelected());
				mapView.repaint();
			}
		});
					
		
		// A checkbox indicating whether to display only selected events.
		final JCheckBox showOnlySelectedEvents = new JCheckBox("Show only selected events");
		toolBar.add(showOnlySelectedEvents);
		showOnlySelectedEvents.addActionListener(new ActionListener() {		
			public void actionPerformed(ActionEvent e) {
				mapView.setShowOnlySelectedEvents(showOnlySelectedEvents.isSelected());
				mapView.repaint();
			}
		});
			
		
		
		// Add a GUI control for mouse mode
		toolBar.addSeparator();
		JLabel zoomModeLabel = new JLabel("Zoom: ", SwingConstants.RIGHT);
		toolBar.add(zoomModeLabel);
		
		
		

		// Button group containing all the mouse modes.
		// ButtonGroup zoomModes = new ButtonGroup();
		
		//JRadioButton zoomClick = new JRadioButton("Zoom by Clicking");
		//zoomClick.setSelected(true);
		
		
		// JRadioButton zoomRegion = new JRadioButton("Zoom into Region");

		//zoomClick.addActionListener(new ActionListener() {
		//	public void actionPerformed(ActionEvent e) {
		//		mapView.enterMouseMode(MouseMode.MOUSEWHEEL_ZOOM);
		//	}
		//});

		
		final JCheckBox zoomRegion = new JCheckBox("Zoom into Region");
		zoomRegion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (zoomRegion.isSelected())
					mapView.enterMouseMode(MouseMode.REGION_ZOOM);
				else 
					mapView.revertMouseMode();
			}
		});

		
		//zoomModes.add(zoomClick);
		//zoomModes.add(zoomRegion);

		//toolBar.add(zoomClick);
		
		// Add a slider for zooming
		ZoomSlider zoomSlider = new ZoomSlider(mapView);
		//zoomSlider.setPreferredSize(new Dimension(300, zoomSlider.getHeight()));
		toolBar.add(zoomSlider);
		
		// TODO: Need layout here.
		
		// Add a button for zoom into region.
		toolBar.add(zoomRegion);

		return toolBar;
	}

	/**
	 * Set a list of selected stations. Display the information of the first
	 * station in the list on the GUI.
	 */
	public void setSelectedStations(StationInfo[] stations) {
		mapView.setSelectedStations(stations);

		// Display information of the most recently selected station.
		if (stations.length >= 1)
			stationInfoDisplay.setStation(stations[stations.length - 1]);
	}

	/**
	 * Set the information displayed as a result of interaction with the map
	 * view.
	 */
	void setMapInteractionInfo(String info) {
		mapInteractionInfoPanel.setInfoText(info);
	}

	/**
	 * 
	 * @return the information of all the stations 
	 * currently loaded into this application. 
	 */
	Set<StationInfo> getAllStations() {
		return stations;
	}
	
	/**
	 * Load a catalog file into the map view.
	 * 
	 * @param f
	 *            A catalog file to load.
	 */
	public void loadCatalogFile(File f) {
		catInfoPanel.displayCatFile(f);

		// Note: cache the event list so that
		// a re-read from disk can be skipped.
		EventList eventList = catalogCache.get(f);

		// if event list has not been cached, read from file
		if (eventList == null) {
			eventList = catalogReader.parseFile(f);
			catalogCache.put(f, eventList); // cache
		}

		// Memorize the whole catalog.
		this.catEventList = eventList;

		// Draw the whole catalog of events in the MapView
		mapView.setEvents(eventList);

		filterControlPanel.setFilteredEvents(eventList);
	}

	/**
	 * TimeWindowFilter used for filtering out catalog events within a specified
	 * number of recent years.
	 */
	private TimeWindowFilter timeFilter = new TimeWindowFilter();

	/**
	 * Load events within a certain number of recent years from a catalog file
	 * into the map view.
	 * 
	 * @param f
	 *            A catalog file to load.
	 * @param numRecentYears
	 *            the number of recent years.
	 */
	public void loadCatalogFile(File f, int numRecentYears) {
		catInfoPanel.displayCatFile(f);

		// Note: cache the event list so that
		// a re-read from disk can be skipped.
		EventList eventList = catalogCache.get(f);

		// if event list has not been cached, read from file
		// and cache the catalog
		if (eventList == null) {
			eventList = catalogReader.parseFile(f);
			catalogCache.put(f, eventList); // cache
		}

		// Memorize the whole catalog.
		this.catEventList = eventList;

		// Filter the event list
		Date currentDate = GregorianCalendar.getInstance().getTime(); // current
																		// date

		Date endDate = new Date(currentDate.getYear(), currentDate.getMonth(),
				currentDate.getDate(), currentDate.getHours(), currentDate
						.getMinutes(), currentDate.getSeconds());
		timeFilter.setEndDate(endDate);

		Date startDate = new Date(currentDate.getYear() - numRecentYears,
				currentDate.getMonth(), currentDate.getDate(), currentDate
						.getHours(), currentDate.getMinutes(), currentDate
						.getSeconds());
		timeFilter.setStartDate(startDate);

		// System.out.println("start" + startDate + "; End" + endDate);

		EventList filteredList = timeFilter.filter(eventList);

		// System.out.println("Number events loaded: " +
		// eventList.getEvents().size());
		// System.out.println("Number events filtered: " +
		// filteredList.getEvents().size());

		// Memorize the dataset.
		this.catEventList = filteredList;

		// Draw the whole catalog of events in the MapView
		mapView.setEvents(filteredList);

		filterControlPanel.setFilteredEvents(filteredList);
	}

	
	/**     added feature--------------------------------------------------------v1.7
	 * Load events within a certain number of recent years from a catalog file
	 * into the map view.
	 * 
	 * @param f
	 *            A catalog file to load.
	 * @param startDate   endDate
	 *            
	 */
	public void loadCatalogFile(File f, Date startDate, Date endDate) {
		catInfoPanel.displayCatFile(f);

		// Note: cache the event list so that
		// a re-read from disk can be skipped.
		EventList eventList = catalogCache.get(f);

		// if event list has not been cached, read from file
		// and cache the catalog
		if (eventList == null) {
			eventList = catalogReader.parseFile(f);
			catalogCache.put(f, eventList); // cache
		}

		// Memorize the whole catalog.
		this.catEventList = eventList;

		// Filter the event list
		
		timeFilter.setEndDate(endDate);

		
		timeFilter.setStartDate(startDate);

		// System.out.println("start" + startDate + "; End" + endDate);

		EventList filteredList = timeFilter.filter(eventList);

		// System.out.println("Number events loaded: " +
		// eventList.getEvents().size());
		// System.out.println("Number events filtered: " +
		// filteredList.getEvents().size());

		// Memorize the dataset.
		this.catEventList = filteredList;

		// Draw the whole catalog of events in the MapView
		mapView.setEvents(filteredList);

		filterControlPanel.setFilteredEvents(filteredList);
	}
	
	
	
	/**
	 * Load a catalog file corresponding to the name given. For example, if
	 * catName is EHB then CATALOG_DIR/EHB-events_1960_2009_sorted_by_depth.cat
	 * is loaded.
	 * 
	 * @param catName
	 *            The catalog name, must be EHB, CMT or NEIC.
	 * @param numRecentYears
	 *            The number of recent years to be loaded.
	 */
	public void loadCatalogByName(String catName, int numRecentYears) {
		String catFileName = null;
		assert (catName.equals("EHB") || catName.equals("CMT") || !catName
				.equals("NEIC")) : "Invalid catalog type " + catName;

		if (catName.equals("EHB"))
			catFileName = CATALOG_DIR
					+ "/EHB-events_1960_2009_sorted_by_depth.cat";
		else if (catName.equals("CMT"))
			catFileName = CATALOG_DIR + "/CMT-jan76-dec05-sorted_by_depth.cat";
		else if (catName.equals("NEIC"))
			catFileName = CATALOG_DIR + "/NEIC_1973_2009_sorted_by_depth.cat";

		File catFile = new File(catFileName);
		loadCatalogFile(catFile, numRecentYears);
	}
	
	
	/**   add new feature-----------------------------------------------------v1.7
	 * Load a catalog file corresponding to the name given. For example, if
	 * catName is EHB then CATALOG_DIR/EHB-events_1960_2009_sorted_by_depth.cat
	 * is loaded.
	 * 
	 * @param catName
	 *            The catalog name, must be EHB, CMT or NEIC.
	 * @param startYear,   endYear
	 *           
	 */
	public void loadCatalogByName(String catName, int startYear,  int endYear) {
		String catFileName = null;
		assert (catName.equals("EHB") || catName.equals("CMT") || !catName
				.equals("NEIC")) : "Invalid catalog type " + catName;

		if (catName.equals("EHB"))
			catFileName = CATALOG_DIR
					+ "/EHB-events_1960_2009_sorted_by_depth.cat";
		else if (catName.equals("CMT"))
			catFileName = CATALOG_DIR + "/CMT-jan76-dec05-sorted_by_depth.cat";
		else if (catName.equals("NEIC"))
			catFileName = CATALOG_DIR + "/NEIC_1973_2009_sorted_by_depth.cat";

		File catFile = new File(catFileName);
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
		String startDateString = "01/01/"+startYear;
		String endDateString = "01/01/"+endYear;
		Date startDate=new Date();
		Date endDate=new Date();
		
		try {
			
			startDate = sdf.parse(startDateString);
			endDate = sdf.parse(endDateString); 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this,
				    "date information is invalid",
				    "Information error",
				    JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} 
		
		
		loadCatalogFile(catFile, startDate, endDate);
	}

	/**
	 * @return The list of all events in the catalog currently loaded into this
	 *         application.
	 */
	public EventList getCatalogEvents() {
		return catEventList;
	}

	/**
	 * @return The MapView which visualizes seismic data for this application.
	 */
	public MapView getMapView() {
		return mapView;
	}

	/**
	 * @return The ControlPanel which transforms user interactions into
	 *         visualisation and data change.
	 */
	public FilterControlPanel getFilterControlPanel() {
		return filterControlPanel;
	}

	/**
	 * Draw the current catalog of events in the MapView.
	 */
	// public void plotCurrentCatalog() {
	// mapView.setEvents(this.catEventList);
	// }

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {				
				SEQI mainWindow = new SEQI("Seismic Event Query Interface");
			}
		});
	}

	/**
	 * CatFileOpenListener listens to open catalog file events.
	 * 
	 * Copyright (C) 2009, Research School of Earth Sciences, The Australian
	 * National University
	 * 
	 * @author huynh
	 * 
	 *         Created on: Jun 25, 2009
	 */
	class CatFileOpenListener implements ActionListener {
		/**
		 * The main application class that handles top level routines.
		 */
		private SEQI mainApp;
		
		/** Category file chooser. */
		private JFileChooser catFileChooser;


		public CatFileOpenListener(SEQI mainApp) {
			this.mainApp = mainApp;
			
			// Add a file chooser for event catalog files.
			catFileChooser = new JFileChooser();
			catFileChooser.addChoosableFileFilter(new CatFileFilter());
		}

		public void actionPerformed(ActionEvent e) {

			// set directory to the home directory of SEQI.
			catFileChooser.setCurrentDirectory(new File(SEQI_HOME));

			// fc.setCurrentDirectory(new File("."));
			int returnVal = catFileChooser.showOpenDialog(SEQI.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				// Open and read the catalogue file
				File file = catFileChooser.getSelectedFile();
				this.mainApp.loadCatalogFile(file);
			}
		}

	}
	
	
	class CatFileFilter extends FileFilter {
		/**
		 * Whether the given file is accepted by this filter.
		 */
		public boolean accept(File f) {
			return f.isDirectory() || f.getName().toLowerCase().endsWith(".cat");
		}

		/**
		 * The description of this filter.
		 */
		public String getDescription() {
			return "Catalog files (*.cat)";
		}
	}

	
	
	/**
	 * LoadMapListener listens to events triggered by map loading actions.
	 * 
	 * Copyright (C) 2009, Research School of Earth Sciences, The Australian
	 * National University
	 * 
	 * @author huynh
	 * 
	 *         Created on: Jun 25, 2009
	 */
	class LoadMapListener implements ActionListener {
		/**
		 * The main application class that handles top level routines.
		 */
		private SEQI mainApp;
		
		/** Image file chooser. */
		private JFileChooser imageFileChooser;
		

		public LoadMapListener(SEQI mainApp) {
			this.mainApp = mainApp;
			
			// Add a file chooser for image files.
			imageFileChooser = new JFileChooser();
			imageFileChooser.addChoosableFileFilter(new ImageFileFilter());
		}

		public void actionPerformed(ActionEvent e) {

			// set directory to the home directory of SEQI.
			imageFileChooser.setCurrentDirectory(new File(SEQI_HOME));

			// fc.setCurrentDirectory(new File("."));
			int returnVal = imageFileChooser.showOpenDialog(SEQI.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				// Load the world map file and redraw the whole map on the screen.
				File file = imageFileChooser.getSelectedFile();
				this.mainApp.getMapView().loadMap(file.getAbsolutePath(), 
						mapView.longMin, mapView.longMax, mapView.latMin, mapView.latMax);
				mapView.repaint();
			}
		}
	}
	
	
	
	class ImageFileFilter extends FileFilter {

		/**
		 * Whether the given file is accepted by this filter.
		 */
		public boolean accept(File f) {
			return f.isDirectory() || f.getName().toLowerCase().endsWith(".gif")
								   || f.getName().toLowerCase().endsWith(".png")
								   || f.getName().toLowerCase().endsWith(".jpg")
								   || f.getName().toLowerCase().endsWith(".jpeg");
		}

		/**
		 * The description of this filter.
		 */
		public String getDescription() {
			return "Image files";
		}
	}
	
}