/*************************************************
/*						*/
	/*      SacFormat.java				*/
/*	RSES - ANU				*/
/*	Jason Li, October 2007			*/
/*						*/
/*	Requires: jchart2d-2.1.1.jar		*/
/*						*/
/************************************************/

/* Public class to store SAC, the intermediate
 format for SDC
 */

/************************************************/

import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DSimple;
import java.awt.*;
import java.awt.event.*;

public class SacFormat {

	/** SAC header information */
	public float[] header_info_f = new float[70];
	public int[] header_info_i = new int[40];
	public char[] header_info_c = new char[192];
	public String[] header_info_s = new String[23];

	/** SAC data */
	public float[] data;

	/**
	 * internal variables for easy access NOTE: delta = header_info_f[0] npts =
	 * header_info_i[9]
	 */
	public int npts = 0;
	public float delta = 1; // the sampling interval (in seconds)

	private static Color RED = new Color(255, 0, 0);

	/** Empty Constructor */
	public SacFormat() {
		/** do nothing, just allocate the memories */
	}

	/** Constructor: parsed in a sac file */
	public SacFormat(String inputFile) {
		/** read the sac file */
		readSacFormat(inputFile);
	}

	/** Function that read an input SAC file */
	public void readSacFormat(String inputFile) {

		try {
			/** Initiate file reader */
			File f = new File(inputFile);
			FileInputStream fis = new FileInputStream(f);
			DataInputStream dis = new DataInputStream(fis);

			/** Reading the SAC header */
			int byteCount = 0;

			/** header elements as floats */
			for (int i = 0; i < 70; i++) {
				header_info_f[i] = dis.readFloat();
				byteCount += 4;
			}

			/** ints */
			for (int i = 0; i < 40; i++) {
				header_info_i[i] = dis.readInt();
				byteCount += 4;
			}

			/** chars */
			int k = 0;
			for (int i = 0; i < 23; i++) {
				header_info_s[i] = "";

				if (i == 1) {
					/** The second string field has 16 characters */
					for (int j = 0; j < 16; j++) {
						byte b = dis.readByte();
						char c = (char) b;
						header_info_s[i] = header_info_s[i] + c;
						header_info_c[k] = c;
						k++;
						byteCount++;
					}
				} else {
					/** Default string field has 8 characters */
					for (int j = 0; j < 8; j++) {
						byte b = dis.readByte();
						char c = (char) b;
						header_info_s[i] = header_info_s[i] + c;
						header_info_c[k] = c;
						k++;
						byteCount++;
					}
				}
			}

			/** assign internal variables */
			npts = header_info_i[9];
			delta = header_info_f[0];

			/** Check if there is a frequency section */
			if (delta == 0) {
				/** frequency data not implemented yet */
				System.out.println("WARNING: DELTA = 0");
				System.out.println("Frequency section not implemented yet");
				System.exit(0);
			}

			/** allocate memory for data */
			data = new float[npts];

			/** reading data into memory */
			for (int i = 0; i < npts; i++) {
				data[i] = dis.readFloat();
				byteCount += 4;
			}

			/** frequency data not implemented yet */

		}
		/** Catching errors */
		catch (SecurityException securityException) {
			/** No access to data */
			System.err.println("NO ACCESS");
			System.exit(1);
		} catch (FileNotFoundException filesNotFoundException) {
			/** File not found */
			System.err.println("NO SUCH FILE");
			System.exit(1);
		} catch (IOException e) {
			/** IO exception */
			System.err.println("SAC IO ERROR: " + e);
			System.exit(1);
		}
	}

	/**
	 * Read two sac files at once NOTE: Obselete by addConsecutiveSac
	 */
	public void readTwoSacs(String sac1, String sac2) {
		this.readSacFormat(sac1);
		SacFormat tmpSac = new SacFormat();
		tmpSac.readSacFormat(sac2);
		if (tmpSac.delta != this.delta) {
			System.out.println("ERROR DELTA NOT SAME");
			return;
		}
		float[] newData = new float[this.npts + tmpSac.npts];
		int i;
		for (i = 0; i < this.npts; i++) {
			newData[i] = this.data[i];
		}
		int j = i;
		for (i = 0; i < tmpSac.npts; i++) {
			newData[j + i] = tmpSac.data[i];
		}
		this.data = newData;
		this.npts = this.npts + tmpSac.npts;
		this.header_info_i[9] = this.npts;
	}

	/** Add another sac file data to memory */
	public void addConsecutiveSac(String sac2) {

		/** Read the new sac file */
		SacFormat tmpSac = new SacFormat();
		tmpSac.readSacFormat(sac2);
		if (tmpSac.delta != this.delta && this.npts != 0) {
			System.out.println("ERROR DELTA NOT SAME");
			return;
		}

		/** Allocate temporary memory for the new file */
		float[] newData = new float[this.npts + tmpSac.npts];
		int i;

		/** Copy data across to the new memory */
		for (i = 0; i < this.npts; i++) {
			newData[i] = this.data[i];
		}
		int j = i;
		for (i = 0; i < tmpSac.npts; i++) {
			newData[j + i] = tmpSac.data[i];
		}

		/** settle header information */
		this.data = newData;
		this.delta = tmpSac.delta;
		this.npts = this.npts + tmpSac.npts;
		this.header_info_i[9] = this.npts;
	}

	/** Write data into sac file. Only even spaced sac is implemented */
	public int writeSac(File f) {
		try {
			/** Initiate file writer */
			FileOutputStream fos = new FileOutputStream(f);
			DataOutputStream dos = new DataOutputStream(fos);

			/** Writing the header */
			for (int i = 0; i < 70; i++) {
				dos.writeFloat(header_info_f[i]);
			}
			for (int i = 0; i < 40; i++) {
				dos.writeInt(header_info_i[i]);
				if (i >= 0 && i <= 5) {
					System.out.println(header_info_i[i]);
				}
			}
			for (int i = 0; i < 192; i++) {
				byte b = (byte) header_info_c[i];
				dos.writeByte(b);
			}

			/** Writing the data into file */
			for (int i = 0; i < this.npts; i++) {
				dos.writeFloat(this.data[i]);
			}

			/** Close file writer */
			dos.flush();
			fos.flush();
			fos.close();
			dos.close();			
			return 1;
		}
		/** Catch exceptions */
		catch (SecurityException securityException) {
			/** Security exception */
			System.err.println("NO ACCESS");
			return -1;
		} catch (IOException e) {
			/** IO exception */
			System.err.println("IO ERROR: " + e);
			return -2;
		}
	}

	/** Calculate number of seconds in difference between two dates */
	public int diffTimeSecs(int y1, int d1, int h1, int m1, int s1, int y2,
			int d2, int h2, int m2, int s2) {
		int retval = 0;
		int val1 = y1 * 365 * 24 * 3600 + d1 * 24 * 3600 + h1 * 3600 + m1 * 60
				+ s1;
		int val2 = y2 * 365 * 24 * 3600 + d2 * 24 * 3600 + h2 * 3600 + m2 * 60
				+ s2;
		if ((y1 % 4) == 0 && (y1 % 400) != 0
				&& !((y2 % 4) == 0 && (y2 % 400) != 0)) {
			// leap year
			// System.out.println("LEAP " + y1);
			val2 = val2 + 3600 * 24;
		} else {
			// System.out.println(y1);
		}
		retval = val2 - val1;
		System.out.println("diff time: " + retval);
		if (retval < 0) {
			System.out.println("OUT! " + y1 + " " + d1 + " " + h1 + " " + m1
					+ " " + s1);
			System.out.println("OUT! " + y2 + " " + d2 + " " + h2 + " " + m2
					+ " " + s2);
		}
		return retval;
	}

	/** Test method: print out start date in the header */
	public void showStartDate() {
		for (int i = 0; i < 5; i++) {
			System.out.println(this.header_info_i[i]);
		}
	}

	/** Given an index, print a string to the header */
	public void printHeaderString(int index, String newString) {

		/** If the string are too long, then just print the default */
		if (newString.length() > 8 && index != 8) {
			/** Normal field can take up to 8 characthers */
			printHeaderString(index, "-12345");
		}

		/** The second string field can tak up to 16 characters */
		if (newString.length() > 16)
			printHeaderString(index, "-12345");

		/** Setting string limit */
		int k;
		if (index == 8)
			k = 15;
		else
			k = 7;

		/** Print the string into header */
		int j = index;
		for (int i = 0; i < newString.length(); i++) {
			header_info_c[j] = newString.charAt(i);
			j++;
		}

		/** Fill up the empty space */
		while (j <= k) {
			header_info_c[j] = ' ';
			j++;
		}

		return;
	}

	/**
	 * Given a list of SAC data blocks from the same station and component,
	 * concatenate all of the seismograms stored in them and return a panel
	 * painted with the seismogram.
	 * 
	 * Precondition: the list must of non-empty.
	 */
	public static JPanel displayData(List<SacFormat> sacBlocks) {

		/** Initiate chart */
		Chart2D chart = new Chart2D();
		chart.setBackground(SDC.background);
		ITrace2D trace = new Trace2DSimple();

		/** Set up units for the x and y axis */
		trace.setPhysicalUnits("seconds", "amplitude");
		chart.setPaintLabels(true);

		/** Add data information to trace */
		float xf = 0;

		for (SacFormat sac : sacBlocks) {
			int cur_npts = sac.npts;
			float[] curData = sac.data;
			float curDelta = sac.delta;
			for (int j = 0; j < cur_npts; j++) {
				trace.addPoint(xf, (double) curData[j]);
				xf = xf + curDelta;
			}
		}
		/** Add trace (with data) to chart */
		chart.addTrace(trace);

		/** Prepare supplementary information */
		/** Station Name */
		SacFormat firstBlock = sacBlocks.get(0);
		String stationName = "";

		if (firstBlock.npts != 0)
			for (int i = 0; i < 8; i++)
				stationName = stationName + firstBlock.header_info_c[i];

		/** Component Name */
		String componentName = "";
		if (firstBlock.npts != 0)
			for (int i = 160; i < 163; i++)
				componentName = componentName + firstBlock.header_info_c[i];

		/** Supplementary information label */
		String seis_info = new String("  Information: ");
		if (firstBlock.npts != 0) {

			/** String for seismogram commencement time */
			String hourString = "" + firstBlock.header_info_i[2];
			if (firstBlock.header_info_i[2] < 10) {
				hourString = "0" + firstBlock.header_info_i[2];
			}

			String minuteString = "" + firstBlock.header_info_i[3];
			if (firstBlock.header_info_i[3] < 10) {
				minuteString = "0" + firstBlock.header_info_i[3];
			}

			String secString = "" + firstBlock.header_info_i[4];
			if (firstBlock.header_info_i[4] < 10) {
				secString = "0" + firstBlock.header_info_i[4];
			}

			String milliString = "" + firstBlock.header_info_i[5];
			if (firstBlock.header_info_i[5] < 10) {
				milliString = "00" + firstBlock.header_info_i[5];
			} else if (firstBlock.header_info_i[5] < 100) {
				milliString = "0" + firstBlock.header_info_i[5];
			}

			/** Put the string together */
			seis_info = "  Begins: " + "year " + firstBlock.header_info_i[0]
					+ ", day " + firstBlock.header_info_i[1];
			seis_info = seis_info + ". Time: " + hourString;
			seis_info = seis_info + ":" + minuteString + ":" + secString + ":"
					+ milliString + ". Duration: " + (int) firstBlock.npts
					* firstBlock.delta + " seconds.    Station: " + stationName
					+ "  Component: " + componentName;
		}

		/** Initiate GUI Panel */
		JPanel displayPanel = new JPanel();
		displayPanel.setLayout(new BorderLayout(10, 10));
		displayPanel.setBackground(SDC.background);

		displayPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createMatteBorder(5, 5, 5, 5, SDC.chartBackground),
				BorderFactory.createEtchedBorder()));

		/** Create panel for supplementary information */
		JLabel seisLabel = new JLabel(seis_info, SwingConstants.LEFT);
		JPanel lPanel = new JPanel();
		lPanel.setLayout(new BorderLayout());
		lPanel.setBackground(SDC.background);
		lPanel.add(seisLabel, BorderLayout.CENTER);

		/** Paint the final panel */
		displayPanel.add(chart, BorderLayout.CENTER);
		displayPanel.add(lPanel, BorderLayout.SOUTH);

		return displayPanel;
	}

	// provide its own GUI -- OBSOLETE: no longer used
	public void CreateAndShowGUI() {

		Chart2D chart = new Chart2D();
		ITrace2D trace = new Trace2DSimple();
		// trace.setColor(Color(100,0,0));

		for (int i = 0; i < npts; i++) {
			trace.addPoint(i, (double) data[i]);
		}

		chart.addTrace(trace);
		JFrame frame = new JFrame("SAC FILE");
		// add the chart to the frame:
		frame.getContentPane().setLayout(new BorderLayout(5, 5));

		// frame.getContentPane().add(chart, BorderLayout.CENTER);

		// Main Panel: Consist of Top and Bot Panel.
		// Divided by two evenly
		JPanel main_panel = new JPanel();
		main_panel.setLayout(new GridLayout(2, 1));

		// Top Panel - Seismologram and essential info
		JPanel top_panel = new JPanel();
		top_panel.setLayout(new BorderLayout(10, 10));
		String seis_info = new String("");
		seis_info = "Begins: " + header_info_i[0] + " " + header_info_i[1];
		seis_info = seis_info + " day. " + header_info_i[2];
		seis_info = seis_info + ":" + header_info_i[3] + ":" + header_info_i[4]
				+ ":" + header_info_i[5] + ", duration: " + (int) npts * delta
				+ " seconds.";
		JLabel seisLabel = new JLabel(seis_info, SwingConstants.LEFT);
		top_panel.add(seisLabel, BorderLayout.SOUTH);
		top_panel.add(chart, BorderLayout.CENTER);

		// top_panel.BorderLayout.setHgap(5);
		// top_panel.BorderLayout.setVgap(5);

		main_panel.add(top_panel);

		// Bottom Panel: controls and textfields
		JPanel bot_panel = new JPanel();
		bot_panel.setLayout(new GridLayout(3, 1));

		JPanel bot_left = new JPanel();
		bot_left.setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 0;
		g.ipadx = 5;
		g.ipady = 5;
		g.weightx = 5;
		g.weighty = 5;

		String[] network = { "CP", "KA", "KB", "LP", "QR", "TL" };
		JComboBox networkList = new JComboBox(network);
		networkList.setMaximumRowCount(3);

		networkList.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				;
			}
		});

		bot_left.add(networkList, g);
		bot_panel.add(bot_left);

		main_panel.add(bot_panel);

		frame.getContentPane().add(main_panel, BorderLayout.CENTER);

		// South Panel - Author Information
		JLabel nameLabel = new JLabel(
				"readSac: Jason Li, RSES - Australian National University, June 2007",
				SwingConstants.CENTER);

		// JPanel south_p = new JPanel();
		// south_p.setLayout(new FlowLayout());

		frame.getContentPane().add(nameLabel, BorderLayout.SOUTH);

		// East Panel: Buttons
		JPanel r_p = new JPanel();
		r_p.setLayout(new GridLayout(10, 1));
		r_p.add(new JButton("Cut"));
		r_p.add(new JButton("Save"));
		r_p.add(new JButton("Discard"));
		r_p.add(new JButton("Exit"));
		Icon anu = new ImageIcon(getClass().getResource("ANU-logo.gif"));
		JLabel logo = new JLabel(anu);
		// r_p.add(logo);

		frame.getContentPane().add(r_p, BorderLayout.EAST);
		frame.setSize(800, 600);
		// Enable the termination button
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		// frame.pack();
		frame.setVisible(true);
	}
}
