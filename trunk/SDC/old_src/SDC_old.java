/*************************************************
/*						*/
/*           Seismic Data Centre		*/
/*		RSES - ANU			*/
/*						*/
/************************************************/

import java.io.*;
import java.lang.*;
import java.util.*;
import javax.swing.*;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DSimple;
import java.awt.*;
import java.awt.event.*;

public class SDC extends JFrame {

	/** Strings indicatings archive path */
	private String archiveRootDir = "";

	/** Network path -- e.g. CAPRA, TASMAL etc. */
	private String networkDir = "";

	/** Path to the instrument coordinate file */
	private String instrCoordFile = "";

	/** Information for Network and Stations */
	public ArrayList ninfo;

	/** Current status information */
	public String curNetwork;
	public String curStation[];
	public String curComponent;
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
	private SacFormat sac;

	/** Network and Station information for GUI */
	private String stations[];
	private String Networks[];
	private String NetworkDuration[] = { "06/2006 - 06/2007" };

	/** Operating Duration Label for GUI */
	private JLabel NDurationLabel = new JLabel(curNetwork + " Operating: "
			+ NetworkDuration[0], SwingConstants.CENTER);

	// Panels
	private JPanel theMainPanel;
	private JPanel botPanel;
	private JPanel theAckPanel;
	private JPanel theStationPanel;

	// Lists and Check Boxes
	private JList StationList;
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
	private Color background = new Color(204, 255, 204);

	/** SDC Constructor */
	public SDC() {

		super("ANU-RSES Seismic Data Centre v1.0");

		/*********************************************/
		/** this would be done through a control file later */
		networkFullname.put("CP", "CAPRA");
		networkLocation.put("CP", "Western Australia");
		archiveRootDir = "/Tdata/CONTINUOUS/";
		instrCoordFile = "/Tdata/public/SDC/instr.coord";
		/*********************************************/

		/** Set frame background */
		getContentPane().setLayout(new BorderLayout());
		getContentPane().setBackground(background);

		/** Load instr.coord file */
		readNetworkInfo();

		/* Step1: Initiate sac (intermediate data format) */
		sac = new SacFormat();

		/** Step2: Set Display */
		/** Main Panel */
		theMainPanel = getMainPanel();
		this.getContentPane().add(theMainPanel, BorderLayout.CENTER);

		/** Lower "Green" styling -- historical reasons */
		theAckPanel = getAcknowledgementPanel();
		this.getContentPane().add(theAckPanel, BorderLayout.SOUTH);
	}

	/** To restart the GUI after "Discard" */
	public void restartGUI() {

		/** Clear current loaded data */
		sac = new SacFormat();
		this.getContentPane().remove(theMainPanel);

		/** Repaint main panel */
		theMainPanel = new JPanel();
		theMainPanel.setLayout(new GridLayout(2, 1));
		theMainPanel.add(sac.DisplayData());
		theMainPanel.add(botPanel);
		this.getContentPane().add(theMainPanel, BorderLayout.CENTER);

		this.setVisible(true);
	}

	/**
	 * To refresh the GUI with changed information (new seismogram, new network
	 * etc.)
	 */
	public void refreshGUI() {

		/** Remove old panel */
		this.getContentPane().remove(theMainPanel);

		/** Install new panel */
		theMainPanel = new JPanel();
		theMainPanel.setLayout(new GridLayout(2, 1));
		theMainPanel.add(sac.DisplayData());
		theMainPanel.add(botPanel);
		this.getContentPane().add(theMainPanel, BorderLayout.CENTER);

		this.setVisible(true);
	}

	/** Write current data into SAC file */
	public int writeSac(File f) {
		return sac.WriteSac(f);
	}

	/** Method initiated when user change networks */
	public void NetworkChanged(JLabel newLabel) {

		/** Layout manager */
		GridBagConstraints g = new GridBagConstraints();
		g.fill = GridBagConstraints.BOTH;
		g.ipadx = 5;
		g.ipady = 5;
		g.gridx = 3;
		g.gridy = 11;
		g.gridwidth = 7;
		g.gridheight = 3;

		/** Remove old label */
		botPanel.remove(NDurationLabel);

		/** Load new label */
		NDurationLabel = newLabel;
		botPanel.add(NDurationLabel, g);

		/**
		 * Remove old station list (each network/deployment has its own list of
		 * stations
		 */
		botPanel.remove(theStationPanel);

		/** Add new station list */
		g.gridx = 0;
		g.gridy = 4;
		g.gridwidth = 1;
		g.gridheight = 2;
		theStationPanel = getStationPanel(false);
		botPanel.add(theStationPanel, g);

		this.setVisible(true);
	}

	/** Paint the main panel (entire interface) */
	public JPanel getMainPanel() {

		/** Initiations and layout manager */
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(2, 1));

		/** Top half: seismogram */
		mainPanel.add(sac.DisplayData());

		/** Bottom half: Main-Bottom Panel (controls) */
		botPanel = getMainBotPanel();
		mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		mainPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		mainPanel.add(botPanel);

		/** Setting color */
		mainPanel.setBackground(background);

		return mainPanel;
	}

	/** Paint the main-bottom panel (controls) */
	public JPanel getMainBotPanel() {

		/** Main bottom panel - what this function returns */
		JPanel mainBotPanel = new JPanel();

		/** Sub-panels of the main bottom panel */
		JPanel buttonPanel = new JPanel();
		JPanel controlPanel = new JPanel();
		JPanel networkStation = new JPanel();
		JPanel networkInfoPanel = new JPanel();
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

		return mainBotPanel;
	}

	/** Function return the painted panel for selecting network/deployments */
	public JPanel getNetworkPanel() {

		/** Initiation */
		JPanel NetworkPanel = new JPanel();

		/** Layout Manager + Title */
		NetworkPanel.setLayout(new BorderLayout(1, 1));
		NetworkPanel.setBorder(BorderFactory
				.createTitledBorder("RSES Deployments"));

		/** Box for selecting the network */
		final JComboBox NetworkBox = new JComboBox(Networks);
		NetworkBox.setMaximumRowCount(3);

		/** Its own actionListener */
		NetworkBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				/** Whenever something changed */
				if (event.getStateChange() == ItemEvent.SELECTED) {
					/** Change current network */
					curNetwork = Networks[NetworkBox.getSelectedIndex()];
					networkDir = (String) networkFullname.get(curNetwork) + "/";

					JLabel newLabel = new JLabel(curNetwork + " Operating: "
							+ NetworkDuration[NetworkBox.getSelectedIndex()],
							SwingConstants.CENTER);

					/** Change station/deployment info */
					NetworkChanged(newLabel);
				}
			}
		});

		/** Paint it on the panel */
		NetworkPanel.add(NetworkBox);
		NetworkPanel.setBackground(background);

		return NetworkPanel;
	}

	/** Return panel containing station list */
	public JPanel getStationPanel(boolean getAll) {

		/** Initiation */
		JPanel StationPanel = new JPanel();

		/** Layout manager */
		StationPanel.setLayout(new BorderLayout(1, 1));
		StationPanel.setBorder(BorderFactory
				.createTitledBorder("Available Stations"));

		/** Initiate list of stations */
		StationList = new JList(stations);
		curStation = new String[stations.length];

		/** properties of the list */
		StationList
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		StationList.setVisibleRowCount(5);

		/** default: select first element in the list */
		StationList.setSelectedIndex(0);

		/** Add a scroll */
		StationPanel.add(new JScrollPane(StationList), BorderLayout.CENTER);

		/** Color */
		StationPanel.setBackground(background);

		return StationPanel;
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
		nsBox = new JCheckBox("NS");
		nsBox.setBackground(background);
		ewBox = new JCheckBox("EW");
		ewBox.setBackground(background);
		zBox.setSelected(true);

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
	public JPanel getStartTimePanel(){

	/** Initiation */
	JPanel StartTimePanel = new JPanel();\

	/** Layout Manager */
	StartTimePanel.setLayout(new GridLayout(4,1));
	StartTimePanel.setBorder(BorderFactory.createTitledBorder("Starting Date"));

	/** Date label - tell user what to input in the textfields */
	JLabel dateLabel = new JLabel("  dd / mm / yyyy ", SwingConstants.LEFT);
	StartTimePanel.add(dateLabel,0);	
	
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
	StartTimePanel.add(datePanel,1);
       
	/** Time label - tell user what to input in the textfields */
	JLabel timeLabel = new JLabel("  hh : mm : ss . mls ", SwingConstants.LEFT);
	StartTimePanel.add(timeLabel,2);

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
	StartTimePanel.add(timePanel,3);
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
		JButton ExtractButton = new JButton("Extract");
		JButton AboutButton = new JButton("SDC Manual");
		JButton quitButton = new JButton("Quit");

		/** Add buttons to the panel */
		opPanel.add(DisplayButton);
		opPanel.add(disButton);
		opPanel.add(ExtractButton);
		opPanel.add(AboutButton);
		opPanel.add(quitButton);

		/** Add action listener to buttons (a private sub-class of SDC) */
		DisplayButton.addActionListener(new MainButtonListener());
		disButton.addActionListener(new MainButtonListener());
		ExtractButton.addActionListener(new MainButtonListener());
		AboutButton.addActionListener(new MainButtonListener());
		quitButton.addActionListener(new MainButtonListener());

		/** Paint color */
		opPanel.setBackground(background);

		return opPanel;
	}

	/** Function return painted panel for network/deployment informations */
	JPanel getNetworkInfoPanel() {

		/** Initiation */
		JPanel nPanel = new JPanel();

		/** Layout Manager */
		nPanel.setLayout(new GridLayout(4, 1, 15, 15));
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
		int month = startD.get(Calendar.MONTH);
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
		month = finishDD.get(Calendar.MONTH);
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

		/** Paint color */
		nPanel.setBackground(background);

		return nPanel;
	}

	/** Bottom panel -obsolete, but still used. */
	JPanel getAcknowledgementPanel() {

		JPanel AknPanel = new JPanel();
		AknPanel.setBackground(Color.green);
		JLabel nameLabel = new JLabel(
				"readSac: Jason Li, RSES - Australian National University, June 2007",
				SwingConstants.CENTER);
		/** Decision made not to display credits at the bottom */
		// AknPanel.add(nameLabel);
		return AknPanel;
	}

	/** Choose directory for data extraction */
	private String chooseDirectory() {

		/** Initiation */
		JFileChooser fc = new JFileChooser();

		/** Select only directories */
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		/** Show GUI for this */
		int returnVal = fc.showSaveDialog(this);
		if (returnVal == JFileChooser.CANCEL_OPTION)
			return null;

		/** Test selected directory for Write permission */
		File dir = fc.getSelectedFile();
		if (dir.canWrite() == false) {
			JOptionPane.showMessageDialog(null, "ERROR: NO ACCESS TO "
					+ dir.getName(), "Extract", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		/** Return directory full path */
		String dirName = dir.getPath();

		return dirName;
	}

	// Private Class to handle buttons and extractions
	private class MainButtonListener implements ActionListener {
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

		networkInfo statInfo;

		// reading in user entry
		public int readData() {

			// Getting Station
			if (StationList.getSelectedIndex() == 0) {
				StationList.clearSelection();
				// Select all the items
				int listStart = 1;
				int listEnd = StationList.getModel().getSize() - 1;
				if (listEnd >= 0) {
					StationList.setSelectionInterval(listStart, listEnd);
				}
			}

			Object[] objs = StationList.getSelectedValues();
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

			// reading components;
			curComponent = "";
			if (zBox.isSelected())
				curComponent = curComponent + "Z";
			if (nsBox.isSelected())
				curComponent = curComponent + "N";
			if (ewBox.isSelected())
				curComponent = curComponent + "E";
			if (curComponent.length() == 0) {
				JOptionPane
						.showMessageDialog(null,
								"ERROR: No component selected. Please selected a component");
				return 0;
			}

			startTime[0] = yearField.getText();
			startTime[1] = monthField.getText();
			startTime[2] = dayField.getText();
			startTime[3] = hourField.getText();
			startTime[4] = minField.getText();
			startTime[5] = secField.getText();
			startTime[6] = msecField.getText();

			endTime[0] = endYearField.getText();
			endTime[1] = endMonthField.getText();
			endTime[2] = endDayField.getText();
			endTime[3] = endHourField.getText();
			endTime[4] = endMinField.getText();
			endTime[5] = endSecField.getText();
			endTime[6] = endMsecField.getText();

			startYear = Integer.parseInt(startTime[0]);
			startMonth = Integer.parseInt(startTime[1]);
			startDay = Integer.parseInt(startTime[2]);
			startHour = Integer.parseInt(startTime[3]);
			startMin = Integer.parseInt(startTime[4]);
			startSec = Integer.parseInt(startTime[5]);
			startMSec = Integer.parseInt(startTime[6]);

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

			// Check duration limit
			long thisDuration = endCalendar.getTimeInMillis()
					- startCalendar.getTimeInMillis();
			long durationLimit = 24 * 3600 * 1000; // currently set to 24 hours
			if (thisDuration < 0 || thisDuration > durationLimit) {
				JOptionPane.showMessageDialog(null,
						"Time exceed current limit of " + (int) durationLimit
								/ (3600 * 1000) + " hours.", "Duration Error",
						JOptionPane.ERROR_MESSAGE);
				return 0;
			}

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
			return 1;
		}

		public ArrayList getLoadFileList(int stationIndex, char componentIndex) {

			ArrayList<String> loadFileList = new ArrayList<String>();

			String loadFile = "";

			curCalendar = new GregorianCalendar(startYear, startMonth - 1,
					startDay, startHour, 0, 0);

			while (curCalendar.before(endCalendar)) {

				// Year
				int curYear = curCalendar.get(Calendar.YEAR);
				String curyearString = "" + curYear;
				String shortYearString = curyearString.charAt(2) + ""
						+ curyearString.charAt(3);

				// Month
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

				// Load File
				loadFile = archiveRootDir + networkDir + curyearString + "/"
						+ curjDayString + "/" + curStation[stationIndex]
						+ shortYearString + curmonthString + curdayString
						+ curhourString + "0000" + ".D" + componentIndex;
				System.out.println(loadFile);

				// check file exists
				try {
					File f = new File(loadFile);
					if (f.exists() == false) {
						// JOptionPane.showMessageDialog(null,
						// "There has been files missing for " +
						// curStation[stationIndex]);
						return null;
					}
				} catch (Exception ioe) {
					System.out.println("ms2sac ERROR: " + ioe);
					return null;
				}

				loadFileList.add(loadFile);

				// increment by hour
				curCalendar.add(Calendar.HOUR, 1);
			}

			// reading station info
			for (int i = 0; i < ninfo.size(); i++) {
				statInfo = (networkInfo) ninfo.get(i);
				if (statInfo.station.equals(curStation[stationIndex]))
					break;
			}

			return loadFileList;

		}

		SpecialMiniSeedReader readFromList(ArrayList loadFileList,
				String eventName) {

			SpecialMiniSeedReader msReader = new SpecialMiniSeedReader(
					startCalendar, endCalendar, statInfo, eventName);

			for (int i = 0; i < loadFileList.size(); i++) {
				String msFileName = (String) loadFileList.get(i);
				msReader.readFile(msFileName);
			}

			return msReader;
		}

		public void actionPerformed(ActionEvent e) {
			// Display Button

			if (e.getActionCommand().equals("Display")) {

				ArrayList loadFileList;

				if (readData() == 0)
					return;

				if (curStation.length > 1 || curComponent.length() > 1) {
					JOptionPane.showMessageDialog(null,
							"More than one station / component is selected. PDC will display station "
									+ curStation[0] + ", component "
									+ curComponent.charAt(0));
				}

				loadFileList = getLoadFileList(0, curComponent.charAt(0));

				if (loadFileList == null) {
					JOptionPane.showMessageDialog(null,
							"Files cannot be loaded.", "ERROR",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				// Start Converting
				SpecialMiniSeedReader msReader = readFromList(loadFileList, "");

				if (msReader == null) {
					JOptionPane.showMessageDialog(null,
							"Files cannot be loaded.", "ERROR",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				sac = (SacFormat) msReader;

				refreshGUI();

				JOptionPane.showMessageDialog(null, "File Loaded");
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
			if (e.getActionCommand().equals("Extract")) {

				if (zdfBox.isSelected()) {
					String notApplied = "Extraction of ZDF format has not been implemented";
					JOptionPane.showMessageDialog(null, notApplied, "Extract",
							JOptionPane.ERROR_MESSAGE);
				}

				if (sacBox.isSelected() == false)
					return;

				if (readData() == 0)
					return;
				String pathname = chooseDirectory();

				if (pathname == null)
					return;
				int counter = curStation.length * curComponent.length();

				int confirm = JOptionPane.showConfirmDialog(null,
						"Extract " + counter + " files to " + pathname
								+ ", are you sure?", "Extract",
						JOptionPane.OK_CANCEL_OPTION);
				if (confirm != JOptionPane.OK_OPTION)
					return;

				String eventName = "";
				confirm = JOptionPane.showConfirmDialog(null,
						"Use default event name? (yyyy.mm.dd.hh.mm)",
						"Extract", JOptionPane.YES_NO_OPTION);

				if (confirm != JOptionPane.YES_OPTION) {
					eventName = JOptionPane.showInputDialog(null,
							"Please enter event name (max 16 characters)",
							"Extract", JOptionPane.OK_CANCEL_OPTION);
				} else {
					eventName = startTime[0] + "." + startTime[1] + "."
							+ dayString + "." + hourString + "." + minString;
				}

				ArrayList loadFileList;
				SpecialMiniSeedReader msReader;
				int success = 0;

				String failedFiles = "";

				for (int i = 0; i < curStation.length; i++) {
					for (int j = 0; j < curComponent.length(); j++) {
						loadFileList = getLoadFileList(i, curComponent
								.charAt(j));
						if (loadFileList == null) {
							failedFiles = failedFiles + curStation[i]
									+ ", component BH" + curComponent.charAt(j)
									+ "\n";
							continue;
						}
						msReader = readFromList(loadFileList, eventName);
						if (msReader == null) {
							failedFiles = failedFiles + curStation[i]
									+ ", component BH" + curComponent.charAt(j)
									+ "\n";
							continue;
						}
						sac = (SacFormat) msReader;
						sac.printHeaderString(8, eventName); // writing event
																// name
						File f = new File(pathname + "/" + curStation[i] + "_"
								+ eventName + ".BH" + curComponent.charAt(j)
								+ ".SAC");
						writeSac(f);
						success++;
					}
				}
				if (success != curStation.length * curComponent.length()) {
					JOptionPane.showMessageDialog(null,
							"ERROR: Data not extracted for\n" + failedFiles,
							"Extract", JOptionPane.ERROR_MESSAGE);
				}

				JOptionPane.showMessageDialog(null, success + " out of "
						+ counter + " sac files were extracted successfully.",
						"Extract", JOptionPane.PLAIN_MESSAGE);
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
					dispose();
					System.exit(0);
				}
			}
		}
	}

	// Read network info file
	public void readNetworkInfo() {

		ArrayList<networkInfo> nlist = new ArrayList<networkInfo>();
		// ArrayList nlist = new ArrayList(); // previous java versions
		ArrayList<String> nnames = new ArrayList<String>();
		// ArrayList nnames = new ArrayList();

		try {
			BufferedReader in = new BufferedReader(new FileReader(
					instrCoordFile));
			networkInfo n;

			while (true) {
				String tmps = in.readLine();
				if (tmps == null)
					break;
				if (tmps.equals("") || tmps.equals(" "))
					continue;
				// System.out.println(tmps);
				String tmpstr[] = tmps.split(" ");
				if (tmpstr.length != 9) {
					System.out.println("ERROR IN NETWORK "
							+ "INFO FILE: elements " + tmpstr.length);
					System.exit(0);
				}
				Object nfname = networkFullname.get(tmpstr[0]);
				String thisname = "Unnamed";
				if (nfname != null)
					thisname = (String) nfname;
				n = new networkInfo(tmpstr[0], tmpstr[1], tmpstr[2], tmpstr[3],
						tmpstr[4], tmpstr[5], tmpstr[6], tmpstr[7], tmpstr[8],
						thisname);
				nlist.add(n);
				String network = tmpstr[0];
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

				if (nnames.contains(tmpstr[0])) {
					continue;
				} else {
					nnames.add(tmpstr[0]);
				}

			}

			if (nnames.size() == 0) {
				System.err.println("No network found!");
				System.exit(0);
			}
			String netlist[] = new String[nnames.size()];
			for (int i = 0; i < nnames.size(); i++) {
				netlist[i] = (String) nnames.get(i);
			}
			Networks = netlist;
			curNetwork = netlist[0];
			networkDir = (String) networkFullname.get(curNetwork) + "/";
			ninfo = nlist;
			updateStationList();
			NDurationLabel = new JLabel(curNetwork + " Operating: "
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

	public void updateStationList() {
		ArrayList<String> stationList = new ArrayList<String>();
		int stationCount = 0;
		for (int i = 0; i < ninfo.size(); i++) {
			networkInfo n = (networkInfo) ninfo.get(i);
			if (n.network.equals(curNetwork)) {
				stationList.add(n.station);
				stationCount++;
			}
		}
		String[] newStations = new String[stationCount + 1];
		newStations[0] = "ALL STATIONS";
		for (int i = 1; i <= stationCount; i++) {
			newStations[i] = (String) stationList.get(i - 1);
		}
		stations = newStations;
		// curStation[0] = stations[0];
	}

	private class ReadmeFrame extends JFrame {
		String ReadmeFilePath = "/Tdata/public/SDC/SDC_MANUAL.txt";

		public ReadmeFrame() {
			getContentPane().setLayout(new BorderLayout());
			getContentPane().setBackground(background);

			// read the README file
			String readmeContent = getContents(new File(ReadmeFilePath));

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

			getContentPane().add(areaScrollPane, BorderLayout.CENTER);
			getContentPane().add(readmeButtons, BorderLayout.SOUTH);

		}

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

		private class ReadmeButtonListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {

				if (e.getActionCommand().equals("OK")) {
					dispose();
				}
				if (e.getActionCommand().equals("About SDC")) {
					String about = "Seismic Data Centre verson 1.0\n";
					about = about + "Research School of Earth Sciences\n";
					about = about + "Australian National University\n\n";
					about = about
							+ "Jason Li, Armando Arcidiaco, Cristo Tarlowski and Hrvoje Tkalcic\n";
					JOptionPane.showMessageDialog(null, about, "About SDC",
							JOptionPane.PLAIN_MESSAGE);
				}
			}
		}
	}

	// Main Method
	public static void main(String args[]) {
		SDC myGUI = new SDC();
		myGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myGUI.pack();
		myGUI.setSize(800, 750);
		myGUI.setVisible(true);
	}

}
