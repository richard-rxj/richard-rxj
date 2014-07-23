/*************************************************
/*						*/
/*      Seismic Data Centre			*/
/*	RSES - ANU				*/
/*	Jason Li, October 2007			*/
/*						*/
/*	Requires: jchart2d-2.1.1.jar		*/
/*						*/
/************************************************/

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SDCApplet extends JApplet {

	/** Strings indicatings archive path */
	private String archiveRootDir = "";

	/** Network path -- e.g. CAPRA, TASMAL etc. */
	private String networkDir = "";

	/** Path to the instrument coordinate file */
	private String instrCoordFile = "";

	/** Information for Network and Stations */
	public ArrayList<NetworkInfo> ninfo;

	/** Current status information */
	public String curNetwork;
	public String curStation[];
	
	
	//merge  by Richard Ren
	private Process SMAP=null;

	// public String curComponent;
	/** The components currently selected for displaying the seismograms. */
	public ArrayList<String> curComponents = new ArrayList<String>();

	public String[] eventOptions = { "Load and extract a single event",
			"Load and extract multiple events from a file" };
	public String curEventOption;
	public String[] startTime = { "2007", "01", "01", "00", "00", "00", "000" };
	public String[] endTime = { "2007", "01", "01", "01", "00", "00", "000" };

	/** Hashtables for looking up network information */
	public Hashtable<String, GregorianCalendar> networkStart = new Hashtable<String, GregorianCalendar>();
	public Hashtable<String, GregorianCalendar> networkFinish = new Hashtable<String, GregorianCalendar>();
	public Hashtable<String, String> networkFullname = new Hashtable<String, String>();
	public Hashtable<String, String> networkLocation = new Hashtable<String, String>();

	/** Duration of the selected data */
	public int duration;

	/** IMPORTANT: intermediate data format */
	// private ArrayList<SacFormat> sacData; // A list of SAC formats, each for
	// each station and component.

	/** Network and Station information for GUI */
	private String stations[];
	private String networks[];
	private String NetworkDuration[] = { "06/2006 - 06/2007" };

	private int networkNum = 0;

	/** Operating Duration Label for GUI */
	private JLabel nDurationLabel;

	// Panels
	private JSplitPane theMainPanel;
	private JPanel chartPanel;
	private JPanel botPanel;
	private JPanel theAckPanel;
	private JPanel theStationPanel;
	private JPanel networkStation;
	private JPanel networkInfoPanel;

	// Lists and Check Boxes
	private JList stationList;
	private JCheckBox nsBox;
	private JCheckBox ewBox;
	private JCheckBox zBox;
	private JCheckBox sacBox;
	private JCheckBox zdfBox;

	// TextFields
	private JTextField yearField = new JTextField("2007", 4);
	private JTextField monthField = new JTextField("01", 2);
	private JTextField dayField = new JTextField("01", 2);
	private JTextField hourField = new JTextField("00", 2);
	private JTextField minField = new JTextField("00", 2);
	private JTextField secField = new JTextField("00", 2);
	private JTextField msecField = new JTextField("000", 3);

	private JTextField endYearField = new JTextField("2007", 4);
	private JTextField endMonthField = new JTextField("01", 2);
	private JTextField endDayField = new JTextField("01", 2);
	private JTextField endHourField = new JTextField("01", 2);
	private JTextField endMinField = new JTextField("00", 2);
	private JTextField endSecField = new JTextField("00", 2);
	private JTextField endMsecField = new JTextField("000", 3);

	// Colors
	static Color background = new Color(204, 255, 204);
	static Color chartBackground = new Color(50, 150, 50);

	private boolean displaying = false;
	private ExtractionManager em = new ExtractionManager();
	
	


	
		
	
	/** SDC Constructor */
	public void guiInit() {

		
		System.out.println("first step!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		
		System.out.println(this.getCodeBase()+"/n/t"+this.getDocumentBase());
		
		
		
		/*********************************************/
		/** this would be done through a control file later */
		archiveRootDir = this.getCodeBase()+"Tlink/lacie1/data/";  //"file:///Tdata/lacie1/data/";
		System.out.println(archiveRootDir);
		
		
		//this file is included in SDC.jar---------------Richard Ren
		instrCoordFile = "instr.coord";  //"/Tdata/public/SDC/instr.coord";
		/*********************************************/
		
	
		/** set size */
		setSize(900, 1000);
		

		/** Set frame background */
		getContentPane().setLayout(new BorderLayout());
		getContentPane().setBackground(background);

		/** Load instr.coord file */
		readNetworkInfo();

		/** Choose event option */
		curEventOption = eventOptions[0];

		/* Step1: Initiate sac (intermediate data format) */
		// sacData = new ArrayList<SacFormat>();
		/** Step2: Set Display */
		/** Main Panel */
		theMainPanel = initMainPanel();
		this.getContentPane().add(theMainPanel, BorderLayout.CENTER);

		/** Lower "Green" styling -- historical reasons */
		theAckPanel = getAcknowledgementPanel();
		this.getContentPane().add(theAckPanel, BorderLayout.SOUTH);

		/** Set display */
		setVisible(true);

		/**
		 * Set the occupation area of the charts to be 2/3 of the main window.
		 * Note: this is only effective after the split pane has been displayed.
		 */
		theMainPanel.setDividerLocation(620);
		
	}

	/** To restart the GUI after "Discard" */
	public void restartGUI() {

		/** Clear current loaded data */
		// sacData.clear();
		/** Repaint main panel */
		theMainPanel.removeAll();
		theMainPanel.setLayout(new GridLayout(2, 1));

		/** This is the panel containing the charts */
		chartPanel.removeAll();

		theMainPanel.add(chartPanel);
		theMainPanel.add(botPanel);
		theMainPanel.repaint();
	}

	/** Method initiated when user change networks */
	public void networkChanged() {
		updateStationList();
		refreshStationList();
	}

	public void stationChanged() {
		networkInfoPanel.setVisible(false);
		networkInfoPanel.removeAll();
		networkInfoPanel.add(getNetworkInfoPanel());
		networkInfoPanel.setVisible(true);
		this.setVisible(true);
	}

	/** Display seismic time-series data in charts */
	public void displayCharts() {

		// Re-start the chart panel.
		chartPanel.removeAll();
		chartPanel.setLayout(new GridLayout(curComponents.size(), 1));
		chartPanel.setBackground(SDCApplet.background);

		/** Initate the file list */
		ArrayList<String> loadFileList = new ArrayList<String>();

		displaying = true;

		if (em.readPanel() == 0) {
			displaying = false;
			return;
		}

		displaying = false;

		// Clear the current set of intermediate SAC data
		// sacData.clear();
		for (String component : curComponents) {
			loadFileList = em.getLoadFileList(0, component);

			if (loadFileList.isEmpty()) {
				JOptionPane.showMessageDialog(SDCApplet.this,
						"No archived files matching the selected criteria can be found.\n"
								+ "Please modify your selection.", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Retrieve a list of miniseed blocks for each component.
			ArrayList<SpecialMiniSeedReader> msReaderList = em.readFromList(
					loadFileList, "", true);
			if (msReaderList.isEmpty()) {
				JOptionPane.showMessageDialog(null,
						"Files cannot be loaded here.", "ERROR",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Concatenate all the miniseed blocks (as if they are continuous)
			ArrayList<SacFormat> sacData = new ArrayList<SacFormat>();
			sacData.addAll(msReaderList);

			// Add the display of the SAC data (for all selected components) to
			// the panel.
			JPanel componentChart = SacFormat.displayData(sacData);
			chartPanel.add(componentChart);
		}

		SDCApplet.this.setVisible(true);
	}

	/**
	 * Initialize the main panel (entire interface). Only call this at
	 * initialization time.
	 */
	private JSplitPane initMainPanel() {
		/** Initiations and layout manager */
		JSplitPane mainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false);
		mainPanel.setOneTouchExpandable(true);

		/** Top half: Seismograms */
		chartPanel = new JPanel(new BorderLayout());
		mainPanel.add(chartPanel, 1);

		/** Bottom half: Main-Bottom Panel (controls) */
		botPanel = initMainBotPanel();
		mainPanel.add(botPanel, 2);

		/** Setting color */
		mainPanel.setBackground(background);

		/**
		 * By default, display all 3 components of the initially chosen station,
		 * between the default start and end time.
		 */
		displayCharts();
		return mainPanel;
	}

	/**
	 * Initialize the main-bottom panel (controls). Only call this at
	 * initialization time.
	 */
	public JPanel initMainBotPanel() {

		/** Main bottom panel - what this function returns */
		JPanel mainBotPanel = new JPanel();

		/** Sub-panels of the main bottom panel */
		JPanel buttonPanel = new JPanel();
		JPanel controlPanel = new JPanel();
		networkStation = new JPanel();
		networkInfoPanel = new JPanel();
		JPanel formatPanel = new JPanel();
		JPanel datePanel = new JPanel();

		/** Setout of main bottom panel */
		mainBotPanel.setLayout(new BorderLayout(5, 5));

		/** Controls (user inputs) in the middle */
		mainBotPanel.add(controlPanel, BorderLayout.CENTER);

		/** Buttons at SOUTH side */
		mainBotPanel.add(buttonPanel, BorderLayout.SOUTH);

		/** ControlPanel */
		controlPanel.setLayout(new GridLayout(1, 3, 5, 5));
		controlPanel.add(networkStation, 0);
		controlPanel.add(formatPanel, 1);
		controlPanel.add(datePanel, 2);

		controlPanel.setBackground(background);
		/** Background color */

		/**
		 * networkStation -- on the left Panel displaying Networks, Stations,
		 * Components
		 */
		networkStation.setLayout(new BorderLayout(10, 10));
		/** Network */
		networkStation.add(getNetworkPanel(), BorderLayout.NORTH);
		/** Station */
		theStationPanel = getStationPanel(false);
		networkStation.add(theStationPanel, BorderLayout.CENTER);
		/** Components */
		networkStation.add(getComponentPanel(), BorderLayout.SOUTH);
		networkStation.setBackground(background);

		/**
		 * formatPanel -- in the middle Panel displaying network information and
		 * data format selection
		 */
		formatPanel.setLayout(new BorderLayout(10, 10));
		/** Deployment Information */
		networkInfoPanel.add(getNetworkInfoPanel());
		networkInfoPanel.setBackground(background);
		formatPanel.add(networkInfoPanel, BorderLayout.NORTH);
		/** Data Format */
		formatPanel.add(getFormatPanel(), BorderLayout.CENTER);
		formatPanel.setBackground(background);

		formatPanel.add(getEventOptionPanel(), BorderLayout.SOUTH);
		formatPanel.setBackground(background);

		/**
		 * datePanel -- on the right Panel displaying the input for starting and
		 * finishing dates
		 */
		datePanel.setLayout(new BorderLayout());
		JPanel middleDatePanel = new JPanel();
		middleDatePanel.setLayout(new GridLayout(2, 1));
		middleDatePanel.add(getStartTimePanel(), 0);
		middleDatePanel.add(getEndTimePanel(), 1);
		datePanel.add(middleDatePanel, BorderLayout.CENTER);
		datePanel.setBackground(background);

		/**
		 * buttonPanel -- on the bottom Panel displaying buttons
		 */
		buttonPanel.add(getButtonPanel());
		buttonPanel.setBackground(background);

		/** Set up background color */
		mainBotPanel.setBackground(background);
		
		/** Set size */
		mainBotPanel.setSize(500, 600);


		return mainBotPanel;
	}

	/** Function return the painted panel for selecting network/deployments */
	private JPanel getNetworkPanel() {

		/** Initiation */
		JPanel networkPanel = new JPanel();

		/** Layout Manager + Title */
		networkPanel.setLayout(new BorderLayout(1, 1));
		networkPanel.setBorder(BorderFactory
				.createTitledBorder("RSES Deployments"));

		/** Box for selecting the network */
		final JComboBox networkBox = new JComboBox(networks);
		curNetwork = "CAPRA"; // init to this network by default.   v1.6
		//curNetwork="ANTARTIC";    //   v1.7
		
		networkBox.setSelectedItem(curNetwork);

		networkBox.setMaximumRowCount(3);

		/** Its own actionListener */
		networkBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				/** Whenever something changed */
				if (event.getStateChange() == ItemEvent.SELECTED) {
					/** Change current network */
					curNetwork = networks[networkBox.getSelectedIndex()];
					networkDir = (String) networkFullname.get(curNetwork) + "/";

					/** Change station/deployment info */
					networkChanged();
				}
			}
		});

		/** Paint it on the panel */
		networkPanel.add(networkBox);
		networkPanel.setBackground(background);
		return networkPanel;
	}

	/** Return panel containing station list */
	public JPanel getStationPanel(boolean getAll) {

		/** Initiation */
		JPanel stationPanel = new JPanel();

		/** Layout manager */
		stationPanel.setLayout(new BorderLayout(1, 1));
		stationPanel.setBorder(BorderFactory
				.createTitledBorder("Available Stations"));

		/** Color */
		stationPanel.setBackground(background);

		/** Initiate list of stations */
		stationList = new JList(stations);
		curStation = new String[stations.length];

		/** properties of the list */
		stationList
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		stationList.setVisibleRowCount(5);

		/** default: set the initial station to CP01 for CAPRA network. */   // v1.6
		   stationList.setSelectedValue("CP01", true);         
		//stationList.setSelectedIndex(0);                //    v1.7

		/** Add listener for the station list */
		stationList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent listSelectionEvent) {
				/** Change station/deployment info */
				stationChanged();
			}
		});

		/** Add a scroll */
		stationPanel.add(new JScrollPane(stationList), BorderLayout.CENTER);

		return stationPanel;
	}

	// Update the GUI with the current station list
	private void refreshStationList() {
		stationList.setListData(stations);
		this.setVisible(true);
	}

	/** Function return the panel painted with component choices */
	public JPanel getComponentPanel() {

		/** Initiation */
		JPanel ComponentPanel = new JPanel();

		/** Layout manager */
		ComponentPanel.setLayout(new BorderLayout(1, 1));
		ComponentPanel.setBorder(BorderFactory.createTitledBorder("Component"));

		/** Three boxes for three components */
		zBox = new JCheckBox("Z");
		zBox.setBackground(background);
		zBox.setSelected(true);

		nsBox = new JCheckBox("NS");
		nsBox.setBackground(background);
		nsBox.setSelected(true);

		ewBox = new JCheckBox("EW");
		ewBox.setBackground(background);
		ewBox.setSelected(true);

		/** Middle component to contain the boxes */
		JPanel middleComponent = new JPanel();
		middleComponent.setBackground(background);
		middleComponent.setLayout(new GridLayout(3, 1));
		middleComponent.add(zBox, 0);
		middleComponent.add(nsBox, 1);
		middleComponent.add(ewBox, 2);

		/** Paint the final panel */
		ComponentPanel.add(middleComponent, BorderLayout.CENTER);
		ComponentPanel.setBackground(background);

		return ComponentPanel;
	}

	/**
	 * Function returns the painted panel containing extracted data format
	 * deployment information
	 */
	public JPanel getFormatPanel() {

		/** Initiation */
		JPanel FormatPanel = new JPanel();

		/** Layout manager */
		FormatPanel.setLayout(new BorderLayout(1, 1));
		FormatPanel.setBorder(BorderFactory
				.createTitledBorder("Extraction Format"));

		/** Two alternative formats */
		sacBox = new JCheckBox("SAC");
		sacBox.setBackground(background);
		zdfBox = new JCheckBox("ZDF");
		zdfBox.setBackground(background);

		// TODO: Further checking of the saving in ZDF format is required.

		/** Default selection is SAC */
		sacBox.setSelected(true);

		/** Middle panel to contain the boxes */
		JPanel middleComponent = new JPanel();
		middleComponent.setBackground(background);
		middleComponent.setLayout(new GridLayout(2, 1));
		middleComponent.add(sacBox, 0);
		middleComponent.add(zdfBox, 1);

		/** Paint the final returning panel */
		FormatPanel.add(middleComponent, BorderLayout.CENTER);
		FormatPanel.setBackground(background);

		return FormatPanel;
	}

	/** Function return painted panel for user input on start time */
	public JPanel getStartTimePanel() {

		/** Initiation */
		JPanel StartTimePanel = new JPanel();

		/** Layout Manager */
		StartTimePanel.setLayout(new GridLayout(4, 1));
		StartTimePanel.setBorder(BorderFactory
				.createTitledBorder("Starting Date"));

		/** Date label - tell user what to input in the textfields */
		JLabel dateLabel = new JLabel("  dd / mm / yyyy ", SwingConstants.LEFT);
		StartTimePanel.add(dateLabel, 0);

		/** Dates input textfields */
		JPanel datePanel = new JPanel();
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		datePanel.setLayout(fl);
		datePanel.add(dayField);
		datePanel.add(new JLabel("/"));
		datePanel.add(monthField);
		datePanel.add(new JLabel("/"));
		datePanel.add(yearField);
		datePanel.setBackground(background);
		StartTimePanel.add(datePanel, 1);

		/** Time label - tell user what to input in the textfields */
		JLabel timeLabel = new JLabel("  hh : mm : ss . mls ",
				SwingConstants.LEFT);
		StartTimePanel.add(timeLabel, 2);

		/** Time input textfields */
		JPanel timePanel = new JPanel();
		FlowLayout fl2 = new FlowLayout();
		fl2.setAlignment(FlowLayout.LEFT);
		timePanel.setLayout(fl2);
		timePanel.add(hourField);
		timePanel.add(new JLabel(":"));
		timePanel.add(minField);
		timePanel.add(new JLabel(":"));
		timePanel.add(secField);
		timePanel.add(new JLabel("."));
		timePanel.add(msecField);
		timePanel.setBackground(background);

		/** Paint final panel */
		StartTimePanel.add(timePanel, 3);
		StartTimePanel.setBackground(background);

		return StartTimePanel;
	}

	/** Function return painted panel for user input on start time */
	public JPanel getEndTimePanel() {

		/** Initiation */
		JPanel EndTimePanel = new JPanel();

		/** Layout Manager */
		EndTimePanel.setLayout(new GridLayout(4, 1));
		EndTimePanel.setBorder(BorderFactory.createTitledBorder("Ending Date"));

		/** Date label - tell user what to input in the textfields */
		JLabel dateLabel = new JLabel("  dd / mm / yyyy ", SwingConstants.LEFT);
		EndTimePanel.add(dateLabel, 0);

		/** Dates input textfields */
		JPanel datePanel = new JPanel();
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		datePanel.setLayout(fl);
		datePanel.add(endDayField);
		datePanel.add(new JLabel("/"));
		datePanel.add(endMonthField);
		datePanel.add(new JLabel("/"));
		datePanel.add(endYearField);
		datePanel.setBackground(background);
		EndTimePanel.add(datePanel, 1);

		/** Time label - tell user what to input in the textfields */
		JLabel timeLabel = new JLabel("  hh : mm : ss . mls ",
				SwingConstants.LEFT);
		EndTimePanel.add(timeLabel, 2);

		/** Time input textfields */
		JPanel timePanel = new JPanel();
		FlowLayout fl2 = new FlowLayout();
		fl2.setAlignment(FlowLayout.LEFT);
		timePanel.setLayout(fl2);
		timePanel.add(endHourField);
		timePanel.add(new JLabel(":"));
		timePanel.add(endMinField);
		timePanel.add(new JLabel(":"));
		timePanel.add(endSecField);
		timePanel.add(new JLabel("."));
		timePanel.add(endMsecField);
		timePanel.setBackground(background);

		/** Paint final panel */
		EndTimePanel.add(timePanel, 3);
		EndTimePanel.setBackground(background);

		return EndTimePanel;
	}

	/** Function return painted panel for buttons */
	JPanel getButtonPanel() {

		/** Initiation */
		JPanel opPanel = new JPanel();

		/** Layout Manager */
		opPanel.setLayout(new FlowLayout());

		/** Buttons */
		JButton DisplayButton = new JButton("Display");
		JButton disButton = new JButton("Discard");
		JButton ExtractButton = new JButton("Load and Extract");
		JButton AboutButton = new JButton("SDC Manual");
		//JButton quitButton = new JButton("Quit");

		
		/** Add buttons to the panel */
		opPanel.add(DisplayButton);
		opPanel.add(disButton);
		opPanel.add(ExtractButton);
		opPanel.add(AboutButton);
		//opPanel.add(quitButton);

		/** Add action listener to buttons (a private sub-class of SDC) */
		DisplayButton.addActionListener(new MainButtonListener());
		disButton.addActionListener(new MainButtonListener());
		ExtractButton.addActionListener(new MainButtonListener());
		AboutButton.addActionListener(new MainButtonListener());
		//quitButton.addActionListener(new MainButtonListener());
		

		/** Paint color */
		opPanel.setBackground(background);

		return opPanel;
	}

	/** Function return painted panel for network/deployment informations */
	JPanel getNetworkInfoPanel() {

		/** Initiation */
		JPanel nPanel = new JPanel();

		/** Layout Manager */
		nPanel.setLayout(new GridLayout(8, 1));
		nPanel.setBorder(BorderFactory
				.createTitledBorder("Deployment Information"));

		/** Loading network full name - if not exist, then default "Unnamed" */
		String nfname = "Unnamed";
		Object nfname1 = networkFullname.get(curNetwork);
		if (nfname1 != null)
			nfname = (String) nfname1;

		/** Display deployment full name */
		JLabel nname = new JLabel("Name: " + nfname);
		nPanel.add(nname, 0);
		String monthString = "";

		/** Load deployment commencement date */
		GregorianCalendar startD = (GregorianCalendar) networkStart
				.get(curNetwork);
		int month = startD.get(Calendar.MONTH) + 1;
		if (month < 10)
			monthString = "0" + month;
		else
			monthString = "" + month;
		JLabel nstart = new JLabel("First commenced in: " + monthString + " / "
				+ startD.get(Calendar.YEAR) + "    ");
		nPanel.add(nstart, 1);

		/** Load deployment ending date */
		GregorianCalendar finishD = (GregorianCalendar) networkFinish
				.get(curNetwork);
		month = finishD.get(Calendar.MONTH) + 1;
		if (month < 10)
			monthString = "0" + month;
		else
			monthString = "" + month;
		JLabel nfinish = new JLabel("In operation until: " + monthString
				+ " / " + finishD.get(Calendar.YEAR) + "    ");
		nPanel.add(nfinish, 2);

		/** Load and display deployment location */
		String loc = "";
		Object loc1 = networkLocation.get(curNetwork);
		if (loc1 != null)
			loc = "Location: " + (String) loc1;
		JLabel nLocation = new JLabel(loc);
		nPanel.add(nLocation, 3);

		/** Information about stations selected */
		GregorianCalendar StationStartDate, StationFinishDate;

		JLabel StationSelected = new JLabel("Selected Stations:");
		nPanel.add(new JLabel(""), 4);
		nPanel.add(StationSelected, 5);

		if (stationList.getSelectedIndex() == 0) {
			StationStartDate = startD;
			StationFinishDate = finishD;
		} else {
			StationStartDate = new GregorianCalendar(3000, 12, 31);
			StationFinishDate = new GregorianCalendar(1900, 1, 1);

			/** Collect selected stations */
			Object[] objs = stationList.getSelectedValues();
			for (int i = 0; i < objs.length; i++) {
				for (int j = 0; j < ninfo.size(); j++) {
					NetworkInfo n = (NetworkInfo) ninfo.get(j);
					if ((n.getStation()).equals(objs[i])) {
						GregorianCalendar s = n.getStartDate();
						GregorianCalendar e = n.getEndDate();
						if (s.before(StationStartDate)) {
							StationStartDate = s;
						}
						if (e.after(StationFinishDate)) {
							StationFinishDate = e;
						}
					}
				}
			}

		}

		/** Display start and finish date of selected stations */
		month = StationStartDate.get(Calendar.MONTH) + 1;
		if (month < 10)
			monthString = "0" + month;
		else
			monthString = "" + month;
		JLabel nnstart = new JLabel("Earliest Station Begun: "
				+ StationStartDate.get(Calendar.DAY_OF_MONTH) + " / "
				+ monthString + " / " + StationStartDate.get(Calendar.YEAR)
				+ "    ");
		nPanel.add(nnstart, 6);

		month = StationFinishDate.get(Calendar.MONTH) + 1;
		if (month < 10)
			monthString = "0" + month;
		else
			monthString = "" + month;
		JLabel nnfinish = new JLabel("Latest Station Ends: "
				+ StationFinishDate.get(Calendar.DAY_OF_MONTH) + " / "
				+ monthString + " / " + StationFinishDate.get(Calendar.YEAR)
				+ "    ");
		nPanel.add(nnfinish, 7);

		/** Paint color */
		nPanel.setBackground(background);

		return nPanel;
	}

	/** Panel for loading events from files intead */
	public JPanel getEventOptionPanel() {

		/** Initiation */
		JPanel EventOptionPanel = new JPanel();

		/** Layout Manager + Title */
		EventOptionPanel.setLayout(new BorderLayout(1, 1));
		EventOptionPanel.setBorder(BorderFactory
				.createTitledBorder("Multiple Event Option"));

		/** Box for selecting the network */
		final JComboBox eventOptionBox = new JComboBox(eventOptions);

		eventOptionBox.setSelectedItem(curEventOption);
		eventOptionBox.setMaximumRowCount(3);

		/** Its own actionListener */
		eventOptionBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				/** Whenever something changed */
				if (event.getStateChange() == ItemEvent.SELECTED) {
					/** Change current network */
					curEventOption = eventOptions[eventOptionBox
							.getSelectedIndex()];
				}
			}
		});

		/** Paint it on the panel */
		EventOptionPanel.add(eventOptionBox);
		EventOptionPanel.setBackground(background);
		return EventOptionPanel;
	}

	/** Bottom panel -obsolete, but still used. */
	JPanel getAcknowledgementPanel() {

		JPanel ackPanel = new JPanel();
		ackPanel.setBackground(Color.green);
		// JLabel nameLabel = new JLabel(
		// "readSac: Jason Li, RSES - Australian National University, June 2007",
		// SwingConstants.CENTER);
		/** Decision made not to display credits at the bottom */
		// ackPanel.add(nameLabel);
		return ackPanel;
	}

	
	
	// A file chooser for the destination file for extraction. 
	private JFileChooser extractionFileChooser = new JFileChooser(".");
	
	/** @return a directory for data extraction */
	private File chooseDirectory() {
		
		/** Select only directories */
		extractionFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		extractionFileChooser.setDialogTitle("Extract data to a directory");

		/** Show GUI for this */
		int returnVal = extractionFileChooser.showSaveDialog(this);
		if (returnVal == JFileChooser.CANCEL_OPTION)
			return null;

		/** Test selected directory for Write permission */
		File dir = extractionFileChooser.getSelectedFile();
		if (dir.canWrite() == false) {
			JOptionPane.showMessageDialog(null, "ERROR: NO ACCESS TO "
					+ dir.getName(), "Load and Extract", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return dir;
	}
	

	/**
	 * The event list file chooser used for specifying a file consisting of a
	 * list of seismic periods.
	 */
	private JFileChooser eventListFileChooser = new JFileChooser(".");
	private String chooseEventListFile() {
		eventListFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		ExampleFileFilter filter = new ExampleFileFilter();

		filter.addExtension("txt");
		filter.setDescription("txt files only");
		eventListFileChooser.setFileFilter(filter);

		/** Show GUI for this */
		eventListFileChooser.setDialogTitle("Select event list file");
		int returnVal = eventListFileChooser.showOpenDialog(this);

		if (returnVal == JFileChooser.CANCEL_OPTION)
			return null;

		/** Test selected directory for Read permission */
		File f = eventListFileChooser.getSelectedFile();
		if (f.canRead() == false) {
			JOptionPane.showMessageDialog(null, "ERROR: NO ACCESS TO READING "
					+ f.getName(), "Load Multipe File",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

		String filePath = f.getPath();

		System.out.println(filePath);

		return filePath;
	}

	// Read network info file
	public void readNetworkInfo() {

		/** Initiation */
		ArrayList<NetworkInfo> nlist = new ArrayList<NetworkInfo>();
		ArrayList<String> nnames = new ArrayList<String>();

		try {
			//change to getResourse reading file from server    by Richard Ren
			BufferedReader in = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(
					instrCoordFile)));
			
			
			NetworkInfo n;

			/** Reading instrument coordinate information */
			while (true) {

				/** Read instr.coord file */
				String tmps = in.readLine();
				if (tmps == null)
					break;
				if (tmps.equals("") || tmps.equals(" "))
					continue;
				String tmpstr[] = tmps.split(" ");

				/** File must contain 11 columns */
				if (tmpstr.length != 11) {
					System.out.println("ERROR IN NETWORK " + "INFO FILE: "
							+ instrCoordFile);
					System.out.println("Expected 11 columns, found "
							+ tmpstr.length);
					System.exit(0);
				}
//				Object nfname = networkFullname.get(tmpstr[9]);
//				String thisname;
//				if (nfname != null)
//					thisname = (String) nfname;

				/** Load information into memory */
				n = new NetworkInfo(tmpstr[0], tmpstr[1], tmpstr[2], tmpstr[3],
						tmpstr[4], tmpstr[5], tmpstr[6], tmpstr[7], tmpstr[8],
						tmpstr[9], tmpstr[10]);
				nlist.add(n);

				/** Parse Network Information */
				String network = tmpstr[9];

				/** Store network fullname for lookup */
				if (networkFullname.containsKey(network) == false) {
					networkFullname.put(network, tmpstr[9]);
				}

				/** Store network location for lookup */
				if (networkLocation.containsKey(network) == false) {
					String[] nlocation = tmpstr[10].split("_");
					String thislocation = nlocation[0];
					for (int ii = 1; ii < nlocation.length; ii++) {
						thislocation = thislocation + " " + nlocation[ii];
					}
					networkLocation.put(network, thislocation);
				}

				/** Store earliest deployment date */
				String[] curSD = tmpstr[5].split("/");
				if (curSD.length == 3) {
					GregorianCalendar cal = new GregorianCalendar(Integer
							.parseInt(curSD[2]),
							Integer.parseInt(curSD[1]) - 1, Integer
									.parseInt(curSD[0]));

					if (networkStart.containsKey(network) == false) {
						networkStart.put(network, cal);
					}

					else {
						Object d = networkStart.get(network);
						GregorianCalendar curStart = (GregorianCalendar) d;
						if (curStart.after(cal)) {
							networkStart.remove(network);
							networkStart.put(network, cal);
						}
					}
				}

				/** Store last deployment date */
				String[] curED = tmpstr[7].split("/");
				if (curED.length == 3) {
					GregorianCalendar cal = new GregorianCalendar(Integer
							.parseInt(curED[2]),
							Integer.parseInt(curED[1]) - 1, Integer
									.parseInt(curED[0]));

					if (networkFinish.containsKey(network) == false) {
						networkFinish.put(network, cal);
					} else {
						GregorianCalendar curFinish = (GregorianCalendar) networkFinish
								.get(network);
						if (curFinish.before(cal)) {
							networkFinish.remove(network);
							networkFinish.put(network, cal);
						}
					}
				}

				/** Add to network list */
				if (nnames.contains(tmpstr[9])) {
					continue;
				} else {
					nnames.add(tmpstr[9]);
				}

			}

			/** Checking if any network loaded */
			if (nnames.size() == 0) {
				System.err.println("No network found!");
				System.exit(0);
			}

			/** Prepare list for GUI */
			networkNum = nnames.size();
			

			
			String netlist[] = new String[networkNum];
			NetworkDuration = new String[networkNum];
			for (int i = 0; i < nnames.size(); i++) {
				netlist[i] = (String) nnames.get(i);
				GregorianCalendar st = networkStart.get(netlist[i]);
				GregorianCalendar fn = networkFinish.get(netlist[i]);
				int smonth = st.get(Calendar.MONTH) + 1;
				int fmonth = fn.get(Calendar.MONTH) + 1;

				String smonthStr = "" + smonth;
				if (smonth < 10)
					smonthStr = "0" + smonth;
				String fmonthStr = "" + fmonth;
				if (fmonth < 10)
					fmonthStr = "0" + fmonth;

				NetworkDuration[i] = smonthStr + "/" + st.get(Calendar.YEAR)
						+ " - " + fmonthStr + "/" + fn.get(Calendar.YEAR);

				System.out.println(netlist[i] + ": " + NetworkDuration[i]);
			}
			
			Arrays.sort(netlist);     //order alphabetically    v1.7
			
			
			networks = netlist;
			curNetwork = netlist[6];    //reorder---default network  CAPRA   v1.7

			/** Network directory is just the fullname of the network */
			networkDir = (String) networkFullname.get(curNetwork) + "/";
			ninfo = nlist;

			/** Update station list */
			updateStationList();
			nDurationLabel = new JLabel(curNetwork + " Operating: "
					+ NetworkDuration[0], SwingConstants.CENTER);
		} catch (EOFException endOfFileException) {
			return;
		} catch (SecurityException securityException) {
			System.err.println("NO ACCESS");
			System.exit(1);
		} catch (FileNotFoundException filesNotFoundException) {
			System.err.println("NO SUCH FILE");   
			System.exit(1);
		} catch (IOException e) {
			System.err.println("SAC IO ERROR: " + e);
			System.exit(1);
		}
	}

	/** Update the list of stations for a given network */
	public void updateStationList() {

		/** Initiation */
		ArrayList<String> stationList = new ArrayList<String>();
		int stationCount = 0;

		/** Read all stations for the network */
		for (int i = 0; i < ninfo.size(); i++) {
			NetworkInfo n = (NetworkInfo) ninfo.get(i);
			if (n.networkFullName.equals(curNetwork)) {
				stationList.add(n.station);
				stationCount++;
			}
		}

		/** Prepare list for GUI */
		String[] newStations = new String[stationCount + 1];
		newStations[0] = "ALL STATIONS";
		for (int i = 1; i <= stationCount; i++) {
			newStations[i] = (String) stationList.get(i - 1);
		}
		stations = newStations;
		// curStation[0] = stations[0];
	}

	/** Private Class to handle buttons and extractions */
	public class ExtractionManager {
		int startYear;
		int startMonth;
		int startDay;
		int startJDay;
		int endYear;
		int endMonth;
		int endDay;
		int endJDay;
		int startHour;
		int startMin;
		int startSec;
		int startMSec;
		int endHour;
		int endMin;
		int endSec;
		int endMSec;

		// checking progress
		int mycount = 0;

		ArrayList<GregorianCalendar> startTimeList;
		ArrayList<GregorianCalendar> endTimeList;

		ArrayList<SeismicEvent> eventList;

		SeismicEvent curEventInfo;

		GregorianCalendar startCalendar;
		GregorianCalendar endCalendar;
		GregorianCalendar curCalendar;

		String yearString = "";
		String monthString = "";
		String dayString = "";
		String hourString = "";
		String minString = "";
		String secString = "";
		String msecString = "";
		String jDayString = "";

		String failedFiles = "";

		String eventListFile = "";

		NetworkInfo statInfo;
		int progress = 0;

		ProgressMonitor pm;

		// Constructor
		public ExtractionManager() {

		}

		/** check give duration */
		public int checkDuration() {
			// Check duration limit
			long thisDuration = endCalendar.getTimeInMillis()
					- startCalendar.getTimeInMillis();

			if (thisDuration < 0)
				return 0;

			// long durationLimit = 24 * 3600 * 1000; // currently set to 24
			// hours
			// if (thisDuration < 0 || thisDuration > durationLimit) {
			// JOptionPane.showMessageDialog(null,
			// "Time exceed current limit of " + (int) durationLimit
			// / (3600 * 1000) + " hours.", "Duration Error",
			// JOptionPane.ERROR_MESSAGE);
			// return 0;
			// }

			return 1;
		}

		/** Load information about current event */
		public void getCurEventData(int i) {

			curEventInfo = (SeismicEvent) eventList.get(i);

			// TODO: Update event catalog
			// curEventInfo

			startCalendar = curEventInfo.eventStart;
			endCalendar = curEventInfo.eventFinish;
			// startCalendar = (GregorianCalendar)startTimeList.get(i);
			// endCalendar = (GregorianCalendar)endTimeList.get(i);

			startYear = startCalendar.get(Calendar.YEAR);
			startMonth = startCalendar.get(Calendar.MONTH) + 1;
			startDay = startCalendar.get(Calendar.DAY_OF_MONTH);
			startJDay = startCalendar.get(Calendar.DAY_OF_YEAR);
			startHour = startCalendar.get(Calendar.HOUR_OF_DAY);
			startMin = startCalendar.get(Calendar.MINUTE);
			startSec = startCalendar.get(Calendar.SECOND);

			endYear = endCalendar.get(Calendar.YEAR);
			endMonth = endCalendar.get(Calendar.MONTH) + 1;
			endDay = endCalendar.get(Calendar.DAY_OF_MONTH);
			endJDay = endCalendar.get(Calendar.DAY_OF_YEAR);
			endHour = endCalendar.get(Calendar.HOUR_OF_DAY);
			endMin = endCalendar.get(Calendar.MINUTE);
			endSec = endCalendar.get(Calendar.SECOND);

			// get date strings
			// Year
			yearString = "" + startYear;

			// Month
			if (startMonth < 10)
				monthString = "0" + startMonth;
			else
				monthString = "" + startMonth;

			// Day
			if (startDay < 10)
				dayString = "0" + startDay;
			else
				dayString = "" + startDay;

			// Julian Day
			if (startJDay < 10)
				jDayString = "00" + startJDay;
			else if (startJDay < 100)
				jDayString = "0" + startJDay;
			else
				jDayString = "" + startJDay;

			// Hour
			if (startHour < 10)
				hourString = "0" + startHour;
			else
				hourString = "" + startHour;

			// Minute
			if (startMin < 10)
				minString = "0" + startMin;
			else
				minString = "" + startMin;

		}

		/** Get start and end time from Panel, check it and store in ArrayList */
		public int getDateFromPanel() {
			/** Reading start time */
			startTime[0] = yearField.getText();
			startTime[1] = monthField.getText();
			startTime[2] = dayField.getText();
			startTime[3] = hourField.getText();
			startTime[4] = minField.getText();
			startTime[5] = secField.getText();
			startTime[6] = msecField.getText();

			/** Reading end time */
			endTime[0] = endYearField.getText();
			endTime[1] = endMonthField.getText();
			endTime[2] = endDayField.getText();
			endTime[3] = endHourField.getText();
			endTime[4] = endMinField.getText();
			endTime[5] = endSecField.getText();
			endTime[6] = endMsecField.getText();

			/** Parse the read text into data */
			startYear = Integer.parseInt(startTime[0]);
			startMonth = Integer.parseInt(startTime[1]);
			startDay = Integer.parseInt(startTime[2]);
			startHour = Integer.parseInt(startTime[3]);
			startMin = Integer.parseInt(startTime[4]);
			startSec = Integer.parseInt(startTime[5]);
			startMSec = Integer.parseInt(startTime[6]);

			/** Store date in GregorianCalendar */
			startCalendar = new GregorianCalendar(startYear, startMonth - 1,
					startDay, startHour, startMin, startSec);
			startCalendar.add(Calendar.MILLISECOND, startMSec);
			startJDay = startCalendar.get(Calendar.DAY_OF_YEAR);

			endYear = Integer.parseInt(endTime[0]);
			endMonth = Integer.parseInt(endTime[1]);
			endDay = Integer.parseInt(endTime[2]);
			endHour = Integer.parseInt(endTime[3]);
			endMin = Integer.parseInt(endTime[4]);
			endSec = Integer.parseInt(endTime[5]);
			endMSec = Integer.parseInt(endTime[6]);

			endCalendar = new GregorianCalendar(endYear, endMonth - 1, endDay,
					endHour, endMin, endSec);
			endCalendar.add(Calendar.MILLISECOND, endMSec);
			endJDay = endCalendar.get(Calendar.DAY_OF_YEAR);

			if (checkDuration() == 0)
				return 0;

			startTimeList.add(startCalendar);
			endTimeList.add(endCalendar);
			curEventInfo = new SeismicEvent(startCalendar, endCalendar);
			eventList.add(curEventInfo);
			return 1;
		}

		/** reading in user entry */
		public int readPanel() {

			eventList = new ArrayList<SeismicEvent>();
			startTimeList = new ArrayList<GregorianCalendar>();
			endTimeList = new ArrayList<GregorianCalendar>();

			// Getting Station
			if (stationList.getSelectedIndex() == 0) {
				stationList.clearSelection();
				// Select all the items
				int listStart = 1;
				int listEnd = stationList.getModel().getSize() - 1;
				if (listEnd >= 0) {
					stationList.setSelectionInterval(listStart, listEnd);
				}
			}

			/** Collect selected stations */
			Object[] objs = stationList.getSelectedValues();
			curStation = new String[objs.length];
			for (int i = 0; i < objs.length; i++) {
				curStation[i] = (String) objs[i];
			}

			// if no station selected, then error.
			if (curStation.length == 0) {
				JOptionPane
						.showMessageDialog(null,
								"ERROR: No station selected. Please selected a station");
				return 0;
			}

			// reading components
			curComponents.clear();
			if (zBox.isSelected())
				curComponents.add("Z");

			if (nsBox.isSelected())
				curComponents.add("N");

			if (ewBox.isSelected())
				curComponents.add("E");

			if (curComponents.size() == 0) {
				JOptionPane
						.showMessageDialog(null,
								"ERROR: No component selected. Please selected a component");
				return 0;
			}

			if (curEventOption.equals(eventOptions[0]) || displaying) {
				if (getDateFromPanel() == 0)
					return 0;
			} else {
				eventListFile = chooseEventListFile();
				if (eventListFile == null)
					return 0;
				if (readMultipleEventFile(eventListFile) == 0)
					return 0;

				EventFrame ef = new EventFrame(eventListFile);
//---------------begin of v1.6-------------------------------------------------------------
//				ef.setSize(400, 200);
//				ef.setLocation(SDC.this.getLocation().x + (SDC.this.getWidth() - ef.getWidth())/2, 
//						SDC.this.getLocation().y + (SDC.this.getHeight() - ef.getHeight())/2);
//---------------end of v1.6-------------------------------------------------------------				
				
//---------------begin of v1.7-------------------------------------------------------------
				ef.setSize(SDCApplet.this.getWidth()/5, SDCApplet.this.getHeight()/5);
				ef.setLocation(SDCApplet.this.getLocation().x + (SDCApplet.this.getWidth() - ef.getWidth())/2, 
						SDCApplet.this.getLocation().y + (SDCApplet.this.getHeight() - ef.getHeight())/2);
//---------------end of v1.7-------------------------------------------------------------	
				
				ef.setVisible(true);
				return 1;
			}
			return 1;
		}

		/**
		 * Given the index of a selected station and a component name, retrieve
		 * from the archive directory all the file names matching the currently
		 * selected date range and station info.
		 * 
		 * @param stationIndex
		 * @param componentIndex
		 * @return A list of matching file names.
		 * @throws URISyntaxException 
		 * @throws MalformedURLException 
		 * 
		 * 
		 */
		public ArrayList<String> getLoadFileList(int stationIndex,
				String componentIndex)  {
            
			
			
			// v1.7
			
			
			ArrayList<String> loadfileList = new ArrayList<String>();

			curCalendar = new GregorianCalendar(startYear, startMonth - 1,
					startDay, startHour, 0, 0);
			curCalendar.add(Calendar.MILLISECOND, startMSec);

			// reading station info
			for (int i = 0; i < ninfo.size(); i++) {
				statInfo = (NetworkInfo) ninfo.get(i);
				if (statInfo.station.equals(curStation[stationIndex]))
					break;
			}

			while (curCalendar.before(endCalendar)) {
				/* First, we parse text. */
				// Year
				int curYear = curCalendar.get(Calendar.YEAR);
				String curyearString = "" + curYear;
				String shortYearString = curyearString.charAt(2) + ""
						+ curyearString.charAt(3);

				// MonthyearString
				String curmonthString = "";
				int curMonth = curCalendar.get(Calendar.MONTH) + 1;
				if (curMonth < 10)
					curmonthString = "0" + curMonth;
				else
					curmonthString = "" + curMonth;

				// Day
				String curdayString = "";
				int curDay = curCalendar.get(Calendar.DAY_OF_MONTH);
				if (curDay < 10)
					curdayString = "0" + curDay;
				else
					curdayString = "" + curDay;

				// Julian Day
				String curjDayString = "";
				int curJDay = curCalendar.get(Calendar.DAY_OF_YEAR);
				if (curJDay < 10)
					curjDayString = "00" + curJDay;
				else if (curJDay < 100)
					curjDayString = "0" + curJDay;
				else
					curjDayString = "" + curJDay;

				// Hour
				String curhourString = "";
				int curHour = curCalendar.get(Calendar.HOUR_OF_DAY);
				if (curHour < 10)
					curhourString = "0" + curHour;
				else
					curhourString = "" + curHour;

				{
					System.out.println("Debug: 1494 ----"+this.getClass().getName());
				}
				
				
				//change to URL  reading----------------------Richard Ren
				File archiveDir=null; 
				try {
					archiveDir = new File(new URL(archiveRootDir + networkDir
						+ curyearString + "/" + curjDayString + "/").toURI());
				}catch (Exception e) {
					System.out.println(e);
				}
				
				
				// File archiveDir = new File("/");				
				String hourFileHead = curStation[stationIndex]
						+ shortYearString + curmonthString + curdayString
						+ curhourString;

				if (!archiveDir.isDirectory()) {
					curCalendar.add(Calendar.HOUR, 1);
					// continue;
				}
				
				ArchiveFilter filter =  new ArchiveFilter(hourFileHead, componentIndex);				
				String[] archiveFiles = archiveDir.list(filter);
				
				// String[] archiveFiles = archiveDir.list();                
				
			    //File f = new File("/Tdata/lacie1/data/CAPRA/2007/001/CP12070101190000.BHE");
		        //String[] files = f.list();
				
//				System.out.println("Filter results: " + filter.accept(archiveDir, hourFileHead + "0000.BH" +  componentIndex));
//				
//				System.out.println("Hour file head: " + hourFileHead + ", component index " + componentIndex);
//				
//				System.out.println("Archived dir: " + archiveDir.toString());
//				System.out.println("Number of archived files: " + archiveFiles.length);				
				
				
				//begin of bug fixing----------------------v1.7
				if(archiveFiles==null){
					continue;
				}
				//end of bug fixing----------------------v1.7
				
				for (int i = 0; i < archiveFiles.length; i++) {
					System.out.println("Archived file: " + archiveFiles[i]);
					
					loadfileList.add(archiveRootDir + networkDir
							+ curyearString + "/" + curjDayString + "/"
							+ archiveFiles[i]);
					System.out.println(archiveRootDir + networkDir
							+ curyearString + "/" + curjDayString + "/"
							+ archiveFiles[i]);
				}
				curCalendar.add(Calendar.HOUR, 1);
			}

			return loadfileList;
		}

		/**
		 * Having loaded the file list, now extract the data (maybe from several
		 * files).
		 * 
		 * Concatenate all seismograms from input files that are continuous in
		 * time into one output Miniseed data block.
		 * 
		 * If there is a time gap between two input files, then store the data
		 * in the latter file in a new block.
		 * 
		 * 
		 * @return A list of continuous miniseed data blocks (which have time
		 *         gaps between them), which have been extracted from the given
		 *         file list.
		 * 
		 *         If the given file list is null or empty, return null.
		 */
		private ArrayList<SpecialMiniSeedReader> readFromList(
				ArrayList<String> loadFileList, String eventName,
				boolean displaying) {

			if (loadFileList == null || loadFileList.size() == 0)
				return null;

			
			System.out.println("go to MiniSeedReader!!!!");
			
			// Initiate miniseed reader
			SpecialMiniSeedReader msReader = new SpecialMiniSeedReader(
					startCalendar, endCalendar, statInfo, curEventInfo);

			// The time position where we are currently reading data from
			// (within the for loop)
			GregorianCalendar curCalendar = new GregorianCalendar(startYear,
					startMonth - 1, startDay, startHour, startMin, startSec);
			curCalendar.add(Calendar.MILLISECOND, startMSec);

			System.out.println("success pass 1588");
			
			// The list of miniseed structures to be returned.
			ArrayList<SpecialMiniSeedReader> msReaderList = new ArrayList<SpecialMiniSeedReader>();

			double rd = 0;

			// Read data from the input file list
			for (String msFileName : loadFileList) {
				System.out.println("msFileName:---------"+msFileName);   //Debug Richard Ren

				// Append the current file to the previous one
				// if they are continuous or return -1 if there is a time gap.
				rd = msReader.readServerFile(msFileName, curCalendar, displaying
						|| zdfBox.isSelected());

				// if there is a time gap between the current file
				// and the previous file.
				if (rd == -1) {
					// Store the miniseed data for the previous continuous
					// sequence of files.
					msReaderList.add(msReader);

					// Create a new miniseed data block for the current file.
					msReader = new SpecialMiniSeedReader(curCalendar,
							endCalendar, statInfo, curEventInfo);

					// re-set the current time to read data from
					curCalendar = msReader.readServerFileStartTime(msFileName);

					// re-read the file
					rd = msReader.readServerFile(msFileName, curCalendar, displaying
							|| zdfBox.isSelected());
				}
				// update the current time after processing a continuous file.
				curCalendar.add(Calendar.MILLISECOND, (int) (rd * 1000));

				System.out.println("Miniseed file loaded, delta "
						+ msReader.delta + " npts "
						+ (int) (rd / msReader.delta));
			}

			// add the last miniseed data block
			msReaderList.add(msReader);

			return msReaderList;
		}

		// Read multiple event file
		public int readMultipleEventFile(String multipleEventFile) {
			try {
				BufferedReader in = new BufferedReader(new FileReader(
						multipleEventFile));
				int line_count = 0;
				
				boolean showErrorDialog=true;     //v1.7   showErrorDialog only once 
				
				while (true) {

					/** Read input event list file */
					String tmps = in.readLine();
					
					line_count++;
					
					if (tmps == null)
						break;
					if (tmps.equals("") || tmps.equals(" "))
						continue;

					//String tmpstr[] = tmps.split(" "); 
					String tmpstr[] = tmps.split("\\s+");      //v1.7
					

					/**
					 * File must contain 14 columns (simple) or 19 columns
					 * (complex)
					 */
					
					
					if (tmpstr.length != 14 && tmpstr.length != 19) {
						String ErrorMessage = "Cannot understand event file: "
								+ multipleEventFile + "\n";
						ErrorMessage = ErrorMessage
								+ "Please see SDC MANUAL for event file format details";
						
						//JOptionPane.showMessageDialog(null, ErrorMessage, "Load and Extract", JOptionPane.ERROR_MESSAGE);
						
						//begin of bug fix   v1.7
						if(showErrorDialog) {                                  
							JOptionPane.showMessageDialog(null, ErrorMessage,
								"Load and Extract", JOptionPane.ERROR_MESSAGE);
							showErrorDialog=false;
						} else {
							System.out.println(ErrorMessage);
						}
						//end of bug fix   v1.7
						
						// Ignore the current line if it does not conform 
						// to the prescribed format
						continue;
					}

					/** Parse the read text into data */
					startYear = Integer.parseInt(tmpstr[0]);
					startMonth = Integer.parseInt(tmpstr[1]);
					startDay = Integer.parseInt(tmpstr[2]);
					startHour = Integer.parseInt(tmpstr[3]);
					startMin = Integer.parseInt(tmpstr[4]);
					startSec = Integer.parseInt(tmpstr[5]);
					startMSec = Integer.parseInt(tmpstr[6]);

					endYear = Integer.parseInt(tmpstr[7]);
					endMonth = Integer.parseInt(tmpstr[8]);
					endDay = Integer.parseInt(tmpstr[9]);
					endHour = Integer.parseInt(tmpstr[10]);
					endMin = Integer.parseInt(tmpstr[11]);
					endSec = Integer.parseInt(tmpstr[12]);
					endMSec = Integer.parseInt(tmpstr[13]);

					/** Store date in GregorianCalendar */
					startCalendar = new GregorianCalendar(startYear,
							startMonth - 1, startDay, startHour, startMin,
							startSec);
					startCalendar.add(Calendar.MILLISECOND, startMSec);
					startJDay = startCalendar.get(Calendar.DAY_OF_YEAR);

					endCalendar = new GregorianCalendar(endYear, endMonth - 1,
							endDay, endHour, endMin, endSec);
					endCalendar.add(Calendar.MILLISECOND, endMSec);
					endJDay = endCalendar.get(Calendar.DAY_OF_YEAR);

					// If start date/time > end date/time
					// give a warning and ignore the line.
					if (checkDuration() == 0) {
						System.out.println("Warning: Line " + line_count + " has a start date after the end date.");
						continue;
					}
					
					//if (endCalendar.before(startCalendar))
					//	return 0;

					SeismicEvent curEvent;

					/** Parsing other data, if available */
					if (tmpstr.length == 19) {
						float lat = Float.parseFloat(tmpstr[14]);
						float lon = Float.parseFloat(tmpstr[15]);
						float dep = Float.parseFloat(tmpstr[16]);
						float mag = Float.parseFloat(tmpstr[17]);
						String catalog = tmpstr[18];
						curEvent = new SeismicEvent("", catalog, lat, lon, dep,
								mag, startCalendar, endCalendar);
					} else {
						startTimeList.add(startCalendar);
						endTimeList.add(endCalendar);
						curEvent = new SeismicEvent(startCalendar, endCalendar);
					}

					eventList.add(curEvent);

					// System.out.println("Event added with " + tmpstr.length +
					// " columns");
				}
			} catch (EOFException endOfFileException) {
				return 1;
			} catch (SecurityException securityException) {
				System.err.println("NO ACCESS");
				return 0;
			} catch (FileNotFoundException filesNotFoundException) {
				System.err.println("NO SUCH FILE");
				return 0;
			} catch (IOException e) {
				System.err.println("SAC IO ERROR: " + e);
				return 0;
			}
			return 1;
		}

		/**
		 * Write out the data of the currently displayed seismic event
		 * (including all the selected channels) to the given directory name.
		 * 
		 * @param eventName
		 * @param dirName
		 *            the parent directory of the output file.
		 * @return The number of files written successfully.
		 */
		public int extractSingleEvent(String eventName, String dirName) {
			int nCreatedFiles = 0;

			// Create the destination directory if it doesn't exist
			if (!new File(dirName).exists()) {
				boolean getDir = (new File(dirName)).mkdir();
				if (!getDir) {
					// Directory creation failed
					JOptionPane.showMessageDialog(null,
							"Cannot create new event directory " + dirName,
							"Load and Extract", JOptionPane.ERROR_MESSAGE);
					return 0;
				}
			}

			// Write in SAC format
			if (sacBox.isSelected()) {
				for (int i = 0; i < curStation.length; i++) {

					/** Each component */
					for (String curComponent : curComponents) {

						// The list of filenames to be loaded from the archive.
						ArrayList<String> loadFileList = getLoadFileList(i,
								curComponent);

						// Miniseed files from the archive into memory.
						ArrayList<SpecialMiniSeedReader> msList = readFromList(
								loadFileList, eventName, false);

						if (msList == null) {
							// No data for the current station and component.
							// Perhaps the dates selected is out of the
							// operating period
							// of the current station.
							System.out.println("No data for station "
									+ curStation[i] + " and component "
									+ curComponent);
							continue;
						}

						int k = 0; // count the number of files for the current
									// components
						for (SpecialMiniSeedReader msReader : msList) {

							/** Write file name */
							String fname;
							if (k == 0)
								fname = dirName + "/" + eventName + "."
										+ curStation[i] + ".BH"
										+ msReader.component + ".SAC";
							else
								fname = dirName + "/" + eventName + "_" + k
										+ "." + curStation[i] + ".BH"
										+ msReader.component + ".SAC";

							File f = new File(fname);
							/** Write to file */
							msReader.writeSac(f);

							/** Increment progress counter */
							nCreatedFiles++;
							k++;
						}
					}
				}
			}

			// Write in ZDF format
			if (zdfBox.isSelected()) {
				// The ZDF structure to be written.
				ZDF myZdf = null;

				// For each station
				for (int i = 0; i < curStation.length; i++) {

					/** Combine all the files for the current station */
					ArrayList<SpecialMiniSeedReader> msList = new ArrayList<SpecialMiniSeedReader>();

					// For each component to be read
					for (String curComponent : curComponents) {

						// The list of filenames to be loaded from the archive.
						ArrayList<String> loadFileList = getLoadFileList(i,
								curComponent);

						// Miniseed files from the archive into memory.
						ArrayList<SpecialMiniSeedReader> compList = readFromList(
								loadFileList, eventName, false);

						if (compList == null) {
							// No data for the current station and component.
							// Perhaps the dates selected is out of the
							// operating period
							// of the current station.
							System.out.println("No data for station "
									+ curStation[i] + " and component "
									+ curComponent);
							continue;
						} else {
							msList.addAll(compList);
						}
					}

					if (msList.isEmpty()) {
						// If there is no data stored in the current station
						// for the queried period of time,
						// then we can go ahead to other stations.
						continue;
					}

					Iterator<SpecialMiniSeedReader> iter = msList.iterator();
					SpecialMiniSeedReader firstMsReader = iter.next();

					if (myZdf == null) {
						myZdf = new ZDF(firstMsReader);
					} else {
						myZdf.addStation(firstMsReader);
					}

					ZDFStation zdfStation = myZdf.getLastStation();

					/**
					 * The first component has already been added to the last
					 * station. This loop skip the first station and add from
					 * the second one.
					 */
					for (; iter.hasNext();) {
						SpecialMiniSeedReader msReader = iter.next();
						zdfStation.addComponent(msReader);
					}
				}

				/** Write file name */
				String fname = dirName + "/" + eventName + "." + "zb";
				File f = new File(fname);
				if (myZdf != null) {
					if (myZdf.writeZDF(f) == 1)
						nCreatedFiles++;
				} else {
					System.out.println("No data to write");
				}

			}

			return nCreatedFiles;
		}

		/** Extracting data */
		public void extractSac() {
			String eventName = "";

			/** Ask user for extraction directory */
			File dir = chooseDirectory();
			if (dir == null)
				return;

			String dirName = dir.getPath();		
			
			/** Prompt user to confirm extraction */
			String showStation = "";
			for (int i = 0; i < curStation.length; i++) {
				showStation = showStation + curStation[i] + "\n";
			}

			String showComponent = "Components: " + curComponents.toString()
					+ "\n";

			String showEventDate;

			int confirm;

			if (curEventOption.equals(eventOptions[0])) {
				// Save data of the event currently selected on the GUI
				confirm = JOptionPane.showConfirmDialog(null,
						"Use default event name? (YYYY.DDD.hh.mm)",
						"Load and Extract", JOptionPane.YES_NO_OPTION);
				getCurEventData(0);
				/** Not default event name: user input */
				if (confirm != JOptionPane.YES_OPTION) {
					eventName = JOptionPane.showInputDialog(null,
							"Please enter event name (max 16 characters)",
							"Load and Extract", JOptionPane.OK_CANCEL_OPTION);
				}
				/** Default event name: yyyy.ddd.hh.mm */
				else {
					eventName = yearString + "." + jDayString + "."
							+ hourString + "." + minString;
				}
				showEventDate = "Event commencing " + yearString + "/"
						+ monthString + "/" + dayString + " " + hourString
						+ ":" + minString + "\n";

			} else {
				// Save data of a number of events which have been
				// extracted and stored in a file.
				showEventDate = "Event from file: " + eventListFile + "\n";
			}

			// The expected total number of files if there is only SAC file
			// created
			// per combination of station, component and seismic period,
			// and there is only one file created for all stations and
			// components
			// per each seismic period.
			int expectedTotal = 0;

			if (zdfBox.isSelected())
				expectedTotal += eventList.size();

			if (sacBox.isSelected())
				expectedTotal += curStation.length * curComponents.size()
						* eventList.size();

			String showResultDirectory = "Extract files to " + dirName
					+ ", are you sure?";

			String confirmMessage = "Final Confirmation:\n Stations: \n"
					+ showStation + showComponent + showEventDate
					+ showResultDirectory;

			confirm = JOptionPane.showConfirmDialog(null, confirmMessage,
					"Load and Extract", JOptionPane.OK_CANCEL_OPTION);

			if (confirm != JOptionPane.OK_OPTION)
				return;

			/** Initiate data structures for extractions */
			int passed = 0;

			// progress = 0;
			// String monitorMessage = String.format("Completed %d%%.\n",
			// progress);
			// pm.setNote(monitorMessage);
			// System.out.println("System Monitor Initiated");

			// For each event to be saved,
			// write the selected components of theirs to a separate file.
			for (int i = 0; i < eventList.size(); i++) {

				/** Prompt user each event name */
				getCurEventData(i);

				String newPathName = dirName;

				if (curEventOption.equals(eventOptions[1])) {
					eventName = yearString + "." + jDayString + "."
							+ hourString + "." + minString;
					newPathName = dirName + "/" + yearString + monthString
							+ dayString + hourString + minString;
				}

				System.out.println("Extracting " + mycount + " event "
						+ eventName);

				int numCreatedFiles = extractSingleEvent(eventName,
						newPathName);
				passed += numCreatedFiles;
			}

			/** Confirm extraction success */
			if (passed > expectedTotal) {
				JOptionPane.showMessageDialog(null, passed
						+ " files were extracted successfully, in which \n"
						+ (passed - expectedTotal)
						+ " were created due to time gap in data",
						"Load and Extract", JOptionPane.PLAIN_MESSAGE);
			} else if (passed < expectedTotal) {
				/** Check if program missed files */
				JOptionPane.showMessageDialog(null,
						"No data available for some stations and components",
						"Load and Extract", JOptionPane.WARNING_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "All " + passed
						+ " files were extracted successfully.",
						"Load and Extract", JOptionPane.PLAIN_MESSAGE);
			}
		}

		/** Class ExtractionManger Ends */

		class ArchiveFilter implements FilenameFilter {
			private String archiveHead, archiveEnd;

			public ArchiveFilter(String s1, String s2) {
				archiveHead = s1;
				archiveEnd = s2;
			}

			public boolean accept(File dir, String name) {
				return (name.startsWith(archiveHead) && name
						.endsWith(archiveEnd));
			}
		}

		/** Private subclass. Create the frame for the SDC Manual */
		private class EventFrame extends JFrame {
			String eventFilePath;

			/** Constructor */
			public EventFrame(String eventFile) {
				super("Event File Details");				
				
				eventFilePath = eventFile;

				getContentPane().setLayout(new BorderLayout());
				getContentPane().setBackground(background);
				
				

				// read the README file
				String eventContent = getContents(new File(eventFilePath));

				// Create an instance of JTextArea
				JTextArea textArea = new JTextArea(eventContent);
				textArea.setEditable(false);
				textArea.setLineWrap(true);
				textArea.setWrapStyleWord(true);


				// Add to a scroll pane
				JScrollPane areaScrollPane = new JScrollPane(textArea);


				// Buttons
				JPanel eventButtons = new JPanel();
				eventButtons.setLayout(new FlowLayout());

				JButton okButton = new JButton("OK");
				JButton cancelButton = new JButton("Cancel");

				okButton.addActionListener(new EventButtonListener());
				cancelButton.addActionListener(new EventButtonListener());

				eventButtons.add(okButton);
				eventButtons.add(cancelButton);

				// Add scrolls
				getContentPane().add(
						new JLabel("Load events specified in this file? "),
						BorderLayout.NORTH);
				getContentPane().add(areaScrollPane, BorderLayout.CENTER);
				getContentPane().add(eventButtons, BorderLayout.SOUTH);

			}

			/** Load contents of the manual */
			public String getContents(File aFile) {
				// ...checks on aFile are elided
				StringBuffer contents = new StringBuffer();

				// declared here only to make visible to finally clause
				BufferedReader input = null;
				try {
					// use buffering, reading one line at a time
					// FileReader always assumes default encoding is OK!
					input = new BufferedReader(new FileReader(aFile));
					String line = null; // not declared within while loop
					/*
					 * readLine is a bit quirky : it returns the content of a
					 * line MINUS the newline. it returns null only for the END
					 * of the stream. it returns an empty String if two newlines
					 * appear in a row.
					 */
					while ((line = input.readLine()) != null) {
						contents.append(line);
						contents.append(System.getProperty("line.separator"));
					}
				} catch (FileNotFoundException ex) {
					ex.printStackTrace();
				} catch (IOException ex) {
					ex.printStackTrace();
				} finally {
					try {
						if (input != null) {
							// flush and close both "input" and its underlying
							// FileReader
							input.close();
						}
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
				return contents.toString();
			}

			/** ActionListener for buttons in the manual */
			private class EventButtonListener implements ActionListener {

				public void actionPerformed(ActionEvent e) {
					// user pressed "OK"
					if (e.getActionCommand().equals("OK")) {
						// closes the frame
						dispose();
						extractSac();
					}
					// user pressed "Cancel"
					if (e.getActionCommand().equals("Cancel")) {

						/** Closes the frame and exits */
						dispose();
					}
				}
			}
		}

	}

	/** Private sub-class to interpret the buttons */
	private class MainButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// Display Button
			if (e.getActionCommand().equals("Display")) {
				SDCApplet.this.displayCharts();
			}

			// Discard Button
			if (e.getActionCommand().equals("Discard")) {
				int i = JOptionPane.showConfirmDialog(null,
						"Discard what you have done?", "Discard",
						JOptionPane.OK_CANCEL_OPTION);
				if (i == JOptionPane.OK_OPTION) {
					// JOptionPane.showMessageDialog(null, "removing...");
					restartGUI();
				}
			}

			// Extract
			if (e.getActionCommand().equals("Load and Extract")) {

				/*
				 * if (zdfBox.isSelected()){ String notApplied =
				 * "Extraction of ZDF format has not been implemented";
				 * JOptionPane.showMessageDialog(null, notApplied,
				 * "Load and Extract", JOptionPane.ERROR_MESSAGE); }
				 * 
				 * if (sacBox.isSelected() == false) return;
				 */

				if (zdfBox.isSelected() == false
						&& sacBox.isSelected() == false) {
					String notApplied = "Please specify which format to extract";
					JOptionPane.showMessageDialog(null, notApplied,
							"Load and Extract", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (zdfBox.isSelected() && sacBox.isSelected()) {
					String notApplied = "SDC currently can only extract one format at a time";
					JOptionPane.showMessageDialog(null, notApplied,
							"Load and Extract", JOptionPane.ERROR_MESSAGE);
					return;
				}

				ExtractionManager em = new ExtractionManager();
				if (em.readPanel() == 0)
					return;
				if (curEventOption.equals(eventOptions[0])
						&& !SDCApplet.this.displaying)
					em.extractSac();
			}

			// About Button
			if (e.getActionCommand().equals("SDC Manual")) {
				ReadmeFrame rf = new ReadmeFrame();
				rf.setSize(600, 800);
				rf.setVisible(true);
			}

			// Quit Button
			if (e.getActionCommand().equals("Quit")) {
				int i = JOptionPane.showConfirmDialog(null, "Exit Now?",
						"Exit", JOptionPane.OK_CANCEL_OPTION);
				if (i == JOptionPane.OK_OPTION) {
					if(SMAP!=null) SMAP.destroy();    //merge by Richard Ren
					System.exit(0);
				}
			}
		}
	}

	/** Private subclass. Create the frame for the SDC Manual */
	private class ReadmeFrame extends JFrame {
		//String ReadmeFilePath = "/Tdata/public/SDC/SDC_MANUAL.txt";    //v1.6
		
		//for SDCApplet Richard Ren
		String ReadmeFilePath = "SDC_MANUAL.txt";   
				//"/home/seis2/richardr/workspace/projects/SDC/SDC_MANUAL.txt";    //v1.7
		
		

		/** Constructor 
		 * @throws  */
		public ReadmeFrame() {

			getContentPane().setLayout(new BorderLayout());
			getContentPane().setBackground(background);

			// read the README file
			String readmeContent =getContents(this.getClass().getResourceAsStream(ReadmeFilePath));
			

			// Create an instance of JTextArea
			JTextArea textArea = new JTextArea(readmeContent);
			textArea.setEditable(false);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);

			// Add to a scroll pane
			JScrollPane areaScrollPane = new JScrollPane(textArea);

			// Buttons
			JPanel readmeButtons = new JPanel();
			readmeButtons.setLayout(new FlowLayout());

			JButton okButton = new JButton("OK");
			JButton aboutButton = new JButton("About SDC");

			okButton.addActionListener(new ReadmeButtonListener());
			aboutButton.addActionListener(new ReadmeButtonListener());

			readmeButtons.add(okButton);
			readmeButtons.add(aboutButton);

			// Add scrolls
			getContentPane().add(areaScrollPane, BorderLayout.CENTER);
			getContentPane().add(readmeButtons, BorderLayout.SOUTH);

		}

		/** Load contents of the manual */
		public String getContents(InputStream aFile) {
			// ...checks on aFile are elided
			StringBuffer contents = new StringBuffer();

			// declared here only to make visible to finally clause
			BufferedReader input = null;
			try {
				// use buffering, reading one line at a time
				// FileReader always assumes default encoding is OK!
				input = new BufferedReader(new InputStreamReader(aFile));
				String line = null; // not declared within while loop
				/*
				 * readLine is a bit quirky : it returns the content of a line
				 * MINUS the newline. it returns null only for the END of the
				 * stream. it returns an empty String if two newlines appear in
				 * a row.
				 */
				while ((line = input.readLine()) != null) {
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				try {
					if (input != null) {
						// flush and close both "input" and its underlying
						// FileReader
						input.close();
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			return contents.toString();
		}

		/** ActionListener for buttons in the manual */
		private class ReadmeButtonListener implements ActionListener {

			public void actionPerformed(ActionEvent e) {
				// user pressed "OK"
				if (e.getActionCommand().equals("OK")) {
					// closes the frame
					dispose();
				}
				// user pressed "ABOUT SDC"
				if (e.getActionCommand().equals("About SDC")) {

					/** Display author list */
					String about = "Seismic Data Centre version 1.7\n";
					about = about + "Research School of Earth Sciences\n";
					about = about + "Australian National University\n\n";
					about = about
							+ "Jason Li, Armando Arcidiaco, Cristo Tarlowski, Hrvoje Tkalcic and Richard Ren\n";
					about = about
							+ "Email: [Jason.Li | Hrvoje.Tkalcic | Richard.rxj]@anu.edu.au\n";
					JOptionPane.showMessageDialog(null, about, "About SDC",
							JOptionPane.PLAIN_MESSAGE);
				}
			}
		}
	}

	// Main Method
	public void init() {
		// Run the GUI on an event dispatching thread,
		// because Swing components are not all thread-safe.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
//				if (!login()) {
//				     try {
//				         getAppletContext().showDocument
//				           (new URL(getCodeBase()+"accessdenied.html"),"_top");
//				         }
//				     catch (Exception e) {e.printStackTrace(); }
//				} 
					guiInit();				
			}
		});
	}
	
	
//	/*
//	 *  begin: test for login------------------------Richard Ren
//	 */
//	
//	private String username;
//	private String password;
//	
//	public boolean login() {
//		  boolean userValid = false;
//		  MyLogin login = new MyLogin (new Frame(""));
//		  requestFocus();
//		  if (login.id) {
//		    username = login.username.getText();
//		    password = login.password.getText();
//		    userValid = validateUser(username , password);
//		    System.out.println
//		      ("The password for " + username 
//		        +  " is " + (userValid?"valid":"invalid"));
//		    }
//		  else
//		    System.out.println
//		      ("Cancel was pressed.");
//		    
//		  login.dispose();
//		  return userValid;
//		  
//		  }
//		     
//		 private boolean validateUser(String usr, String pwd) {
//		   // here you will code some logic to validate the username
//		   // password... for testing purpose :
//		   //                 username = test  password = test
//		   return (usr.equals("test") && pwd.equals("test"));
//		   }
//	
//	
//	   /*
//		* End: test for login
//		*/

}
