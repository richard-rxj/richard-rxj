import java.io.*;
import java.util.*;

/*********************************************************
 * Class: SpecialMiniSeedReader Jason Li, August, 2007 jason.li@rsise.anu.edu.au
 * RSES, ANU.
 * 
 * WARNING: This is NOT a generic miniseed reader This class only reads a
 * special type of miniseed file in the archives of RSES at ANU.
 * 
 * Special Type of Miniseed: A miniseed file that has a header followed by a
 * blockette1000, then data.
 * 
 * All other uses are not recommended.
 * 
 * Note: Each file in the archive stores the seismic data recorded within each
 * hour for a specific spatial component (Z, N or E) (Cong Phuoc Huynh)
 ***********************************************************/

public class SpecialMiniSeedReader extends SacFormat {

	/** Data section Starts ****************************/

	/** specifying starting and finishing time of the final sac file */
	public GregorianCalendar start;
	public GregorianCalendar finish;

	/** Station information passed from others */
	public NetworkInfo statInfo;

	/** Event information passed from user */
	public SeismicEvent eventInfo;

	/** Azimuth of Event to Station */
	public Azimuth azi;

	/** Maximum and minimum amplitude */
	public float maxAmp = Float.MIN_VALUE, minAmp = Float.MAX_VALUE;

	/** durations of the final sac file */
	private int millisecsDuration;

	/** internal variables on data */
	private int dataIndex = 0;

	/** internal variable determine current record useful to final file */
	private boolean commenced = false;

	/** internal memory structures to read miniseed data */
	private byte arrayByte1[] = new byte[4032];
	private byte arrayByte2[] = new byte[3968];

	/** variable indicating component */
	public String component = "";

	/** internal variable indicating event name */
	private String eventName = "";

	/** internal string indicating time */
	private String yearStr = "";
	private String monthStr = "";
	private String dayStr = "";
	private String minStr = "";
	private String hourStr = "";

	/** interal variable indicate whether to read header */
	boolean readHeader = false;

	/** Data section Ends *****************************/

	/**
	 * Generic class constructor Starts and ends with default time being the
	 * first hour of year 2000
	 */

	public SpecialMiniSeedReader() {
		start = new GregorianCalendar(1900, 1, 1);
		finish = new GregorianCalendar(2100, 1, 1, 1, 0, 0);
		millisecsDuration = 4 * 3600 * 1000; // max 4 days
		npts = 0;
		statInfo = new NetworkInfo();

		// enter default station and component name:
		printHeaderString(0, "default");
		printHeaderString(160, "default");
	}

	/** Class Constructor: specifying start and finish time of final file */
	public SpecialMiniSeedReader(GregorianCalendar a, GregorianCalendar b,
			NetworkInfo n, SeismicEvent e) {

		start = a;
		finish = b;
		statInfo = n;
		readHeader = true;

		// Calculate the duration in MilliSeconds
		millisecsDuration = (int) (finish.getTimeInMillis() - start
				.getTimeInMillis());
		npts = 0;

		// Write some header info to final sac file
		header_info_i[0] = a.get(Calendar.YEAR);
		header_info_i[1] = a.get(Calendar.DAY_OF_YEAR);
		header_info_i[2] = a.get(Calendar.HOUR_OF_DAY);
		header_info_i[3] = a.get(Calendar.MINUTE);
		header_info_i[4] = a.get(Calendar.SECOND);
		header_info_i[5] = a.get(Calendar.MILLISECOND);

		eventInfo = e;
		String evtName = e.eventName;
		if (evtName.equals("")) {
			yearStr = "" + a.get(Calendar.YEAR);
			monthStr = "" + (a.get(Calendar.MONTH) + 1);
			if (a.get(Calendar.MONTH) + 1 < 10)
				monthStr = "0" + monthStr;
			dayStr = "" + a.get(Calendar.DAY_OF_MONTH);
			if (a.get(Calendar.DAY_OF_MONTH) < 10)
				dayStr = "0" + dayStr;
			hourStr = "" + header_info_i[2];
			if (header_info_i[2] < 10)
				hourStr = "0" + hourStr;
			minStr = "" + header_info_i[3];
			if (header_info_i[3] < 10)
				minStr = "0" + minStr;

			eventName = yearStr + "." + monthStr + "." + dayStr + "." + hourStr
					+ "." + minStr;
		} else {
			eventName = evtName;
		}

		if (e.eventLat != -12345)
			azi = new Azimuth(e.eventLat, e.eventLon, Float
					.parseFloat(statInfo.lat), Float.parseFloat(statInfo.lon));
	}

	public void setEvent(SeismicEvent e) {

		eventInfo = e;
		eventName = e.eventName;

		if (e.eventLat != -12345)
			azi = new Azimuth(e.eventLat, e.eventLon, Float
					.parseFloat(statInfo.lat), Float.parseFloat(statInfo.lon));
	}

	public GregorianCalendar readFileStartTime(String msFile) {
		GregorianCalendar recordStart = null;
		try {
			// Determining component
			if (component == "") {
				component = "" + msFile.charAt(msFile.length() - 1);
			}
			// Determining file machine-type
			// This version works with both SUN_UNIX and PC_INTEL
			// default SUN_UNIX
			BinaryRandomInput input = new BinaryRandomInput(msFile, "SUN_UNIX");
			input.seek(48L);
			Blockette1000 block1000 = new Blockette1000(input);

			int encodingFormat = block1000.encodingFormat;
			int blockettetype = block1000.blocketteType;
			if (blockettetype != 1000) {
				// Fix: 25-05-2011 by Cong Huynh 
				// close the registry entry for the Unix version
				input.close();

				// switch to PC
				input = new BinaryRandomInput(msFile, "PC_INTEL");
				System.out.println("PC_INTEL");
			} else {
				System.out.println("SUN_UNIX");
			}
			input.seek(0);
			Header head = new Header(input);
			recordStart = getCalFromDOY(head.year, head.day, head.hourOfDay,
					head.minOfDay, head.secsOfDay);
			recordStart.add(Calendar.MILLISECOND, (head.milisecsOfDay));
			recordStart.add(Calendar.MILLISECOND, (head.milisecsOfDay));
			

			// Fix: 25-05-2011 by Cong Huynh 
			// close the registry entry for the random access file
			input.close();			
		} catch (Exception e) {
			System.out.println("Exception " + e);
		}
		return recordStart;
	}

	/**
	 * Reading a particular miniseed file.
	 * 
	 * @return The number of seconds passed.
	 */
	public double readFile(String msFile, GregorianCalendar curCalendar,
			boolean displaying) {

		double milliSecondsPassed = 0;
		try {
			Steim1 steim1 = new Steim1();
			Steim2 steim2 = new Steim2();
			int[] arrayOfSamples;

			// Determining component
			if (component == "") {
				component = "" + msFile.charAt(msFile.length() - 1);
			}

			// Determining file machine-type
			// This version works with both SUN_UNIX and PC_INTEL
			// default SUN_UNIX
			BinaryRandomInput input = new BinaryRandomInput(msFile, "SUN_UNIX");
			input.seek(48L);
			Blockette1000 block1000 = new Blockette1000(input);

			int encodingFormat = block1000.encodingFormat;
			int blockettetype = block1000.blocketteType;
			if (blockettetype != 1000) {				
				// Fix: 25-05-2011 by Cong Huynh 
				// close the registry entry for the Unix version
				input.close();

				// switch to PC
				input = new BinaryRandomInput(msFile, "PC_INTEL");
				System.out.println("PC_INTEL");
			} else {
				System.out.println("SUN_UNIX");
			}

			// Determine length and number of records
			long lenfile = input.length();
			long numOfRecords = lenfile / 4096L;

			// Set up variable compare start and end of records
			// We do not read the data of records that are not necessary
			GregorianCalendar recordStart, recordEnd;

			// Benchmark Timer
			Calendar timer;
			long totalTime = 0;
			Calendar sumtime = Calendar.getInstance();

			// Looping through records
			for (int i = 0; i < numOfRecords; i++) {

				// Benchmark Timer
				timer = Calendar.getInstance();

				// Read correct setions of the file
				input.seek(i * 4096L);

				// Read header
				Header head = new Header(input);

				// Benchmark Read time
				totalTime += Calendar.getInstance().getTimeInMillis()
						- timer.getTimeInMillis();

				// Find out what time the record starts
				recordStart = getCalFromDOY(head.year, head.day,
						head.hourOfDay, head.minOfDay, head.secsOfDay);
				recordStart.add(Calendar.MILLISECOND, (head.milisecsOfDay));
				// // note: adding this field makes SDC deem continuous data to
				// be broken.

				// Find out what time the record ends
				recordEnd = getCalFromDOY(head.year, head.day, head.hourOfDay,
						head.minOfDay, head.secsOfDay);

				// Find out the duration of the record in milliseconds
				int recordDuration = head.numberOfSamples
						* (1000 / head.rateFactor);
				/* System.out.println("DURATION " + recordDuration); */

				// If not the first record
				if (npts != 0) {
					// Check whether sample rate is the same
					if (npts != millisecsDuration / (1000 / head.rateFactor)) {
						// if sample rate not the same, give error
						System.out.println("ERROR: rateFactor not the same!");
						System.exit(0);
					}

					// check if data is continuous
					if (i == 0) {
						if (recordStart.after(curCalendar)) {
							// Broken data. Clean up.
							if (!displaying) {
								npts = dataIndex;
								header_info_i[9] = npts;

								// secondsPassed = recordStart.getTimeInMillis()
								// - curCalendar.getTimeInMillis();

								// System.out.println("Out by " + secondsPassed
								// + " milliseconds");
								
								// 26 May 2011: Fixed by Cong Huynh 
								// Release any system resources associated with the input file stream.
								input.close();
								return -1;
							}
							// displaying thus zero-fill
							while (recordStart.after(curCalendar)
									&& dataIndex < npts) {
								curCalendar.add(Calendar.MILLISECOND,
										(int) (delta * 1000));
								data[dataIndex++] = 0;
								milliSecondsPassed += (int) (delta * 1000);
							}
							if (dataIndex == npts) {
								// 26 May 2011: Fixed by Cong Huynh 
								// Release any system resources associated with the input file stream.
								input.close();								
								return 0;
							}
						}
					}
				}
				// if it is the first record
				else {
					// Find more header information for Sac

					// Number of samples
					npts = millisecsDuration / (1000 / head.rateFactor);
					System.out.println("npts is " + npts + ".");

					// Sample Rates
					delta = (float) 1 / (head.rateFactor);
					System.out.println("Delta is set to " + delta
							+ ". RateFactor is " + head.rateFactor);

					// Allocate memory for data
					data = new float[npts];

					// check if we have to write header
					if (readHeader == false)
						continue;
					// write sac header info here:

					System.out.println("Set Header");

					// All Floating point value here
					for (int ii = 0; ii < 70; ii++) {
						header_info_f[ii] = -12345;
					}
					header_info_f[0] = delta;
					header_info_f[3] = 1;
					header_info_f[5] = 0;
					header_info_f[6] = npts * delta;
					header_info_f[9] = 1;
					// station latitude, longitude and elevation
					header_info_f[31] = Float.parseFloat(statInfo.lat);
					header_info_f[32] = Float.parseFloat(statInfo.lon);
					header_info_f[33] = Float.parseFloat(statInfo.dep);

					// event latitude, longitude and depth

					System.out.println("set events");
					if (eventInfo.eventLat != -12345) {

						header_info_f[35] = eventInfo.eventLat;
						header_info_f[36] = eventInfo.eventLon;
						header_info_f[38] = eventInfo.eventDep * 1000;

						// Azimuth of source to station
						/*
						 * header_info_f[50] = azi.rangeKm; header_info_f[51] =
						 * azi.azim; header_info_f[52] = azi.bazim;
						 * header_info_f[53] = azi.rangeDg;
						 */
						// note: Azimuth is not written, because SAC can
						// determine it.
					}
					System.out.println("Set Components");
					if (component.equalsIgnoreCase("E"))
						header_info_f[57] = 90;
					else
						header_info_f[57] = 0;
					if (component.equalsIgnoreCase("Z"))
						header_info_f[58] = 0;
					else
						header_info_f[58] = 90;
					System.out.println("Floats Set");

					// All integer values
					for (int ii = 0; ii < 40; ii++) {
						switch (ii) {
						case 0:
							header_info_i[ii] = start.get(Calendar.YEAR);
							break;
						case 1:
							header_info_i[ii] = start.get(Calendar.DAY_OF_YEAR);
							break;
						case 2:
							header_info_i[ii] = start.get(Calendar.HOUR_OF_DAY);
							break;
						case 3:
							header_info_i[ii] = start.get(Calendar.MINUTE);
							break;
						case 4:
							header_info_i[ii] = start.get(Calendar.SECOND);
							break;
						case 5:
							header_info_i[ii] = start.get(Calendar.MILLISECOND);
							break;
						case 6:
							header_info_i[ii] = 6;
							break;
						case 7:
							header_info_i[ii] = 0;
							break;
						case 8:
							header_info_i[ii] = 0;
							break;
						case 9:
							header_info_i[ii] = npts;
							break;
						case 15:
							header_info_i[ii] = 1;
							break;
						// velocity in nm per sec
						case 16:
							header_info_i[ii] = 7;
							break;
						case 17:
							header_info_i[ii] = 9;
							break;
						case 22:
							header_info_i[ii] = 5;
							break;
						case 35:
							header_info_i[ii] = 1;
							break;
						case 36:
							header_info_i[ii] = 1;
							break;
						case 37:
							header_info_i[ii] = 1;
							break;
						// LCALDA is TRUE
						case 38:
							header_info_i[ii] = 1;
							break;
						case 39:
							header_info_i[ii] = 0;
							break;
						default:
							header_info_i[ii] = -12345;
						}
					}
					System.out.println("Integers Set");
					// All strings
					for (int ii = 0; ii < 192; ii += 8) {
						if (ii == 16)
							continue;
						switch (ii) {
						case 0:
							printHeaderString(ii, head.stationCodeHead);
							break;
						case 8:
							printHeaderString(ii, eventName);
							break;
						// case 8: printHeaderString(ii, "myevent"); break;
						case 160:
							printHeaderString(ii, "BH" + component);
							break;
						case 168:
							printHeaderString(ii, statInfo.networkFullName);
							break;
						default:
							printHeaderString(ii, "-12345");
						}
					}

					System.out.println("Header info recorded");
				}

				recordEnd.add(Calendar.MILLISECOND, recordDuration);

				if (recordEnd.before(start)) {
					continue;
				}
				if (recordStart.after(finish)) {
					continue;
				}

				boolean isFirstRecord = recordStart.before(start);
				boolean isLastRecord = recordEnd.after(finish);

				timer = Calendar.getInstance();
				block1000 = new Blockette1000(input);
				totalTime += Calendar.getInstance().getTimeInMillis()
						- timer.getTimeInMillis();

				encodingFormat = block1000.encodingFormat;
				blockettetype = block1000.blocketteType;

				long dataBegin = (long) head.beginingOfData;
				if (dataBegin == 0)
					dataBegin = 64;

				input.seek(i * 4096L + dataBegin);
				int samples = head.numberOfSamples;

				if (encodingFormat == 1) { // 16 bit integer
					arrayOfSamples = new int[samples];
					timer = Calendar.getInstance();
					/* Reading one int at a time -- inefficient */
					for (int j = 0; j < samples; j++) {
						arrayOfSamples[j] = (int) input.readShortB();
						// if (arrayOfSamples[j] > 1000) j--;
						// System.out.println(j);
					}
					// reading the byte array
					// input.read(arrayByte1);
					// for (int j=0; j<samples*2; j+=2){
					// arrayOfSamples[j/2] = myarr2short(arrayByte1, j);
					// }
					totalTime += Calendar.getInstance().getTimeInMillis()
							- timer.getTimeInMillis();
				}

				else if (encodingFormat == 10) { // steim 1 compression
					timer = Calendar.getInstance();
					input.read(arrayByte1);
					totalTime += Calendar.getInstance().getTimeInMillis()
							- timer.getTimeInMillis();
					arrayOfSamples = steim1
							.decode(arrayByte1, samples, true, 0);
					System.out.println("Steim1 block");
				}

				else if (encodingFormat == 11) { // steim 2 compression
					input.read(arrayByte2);
					arrayOfSamples = steim2.decode(arrayByte2, samples, false,
							0);
					System.out.println("Steim2 block");
				}

				else {
					// System.out.println("reading data");
					arrayOfSamples = new int[samples];
					timer = Calendar.getInstance();

					/* Reading one int at a time -- inefficient */
					for (int j = 0; j < samples; j++) {
						arrayOfSamples[j] = input.readIntB();
					}

					// reading the byte array
					// input.read(arrayByte1);
					// System.out.println("data read into memory");
					// parse each int
					// for (int j=0; j<samples*4; j+=4){
					// arrayOfSamples[j/4] = myarr2int(arrayByte1, j);
					// }
					totalTime += Calendar.getInstance().getTimeInMillis()
							- timer.getTimeInMillis();
				}

				int j;

				// If the first record, determine the start
				if (isFirstRecord) {
					j = (int) ((start.getTimeInMillis() - recordStart
							.getTimeInMillis()) / (1000 / head.rateFactor));
				}
				// else start reading from beginning
				else {
					j = 0;
				}

				// copy to the data array
				maxAmp = Float.MIN_VALUE;
				minAmp = Float.MAX_VALUE;
				float dataPoint;
				while (j < samples && dataIndex < npts) {
					dataPoint = (float) arrayOfSamples[j++];
					data[dataIndex++] = dataPoint;
					milliSecondsPassed += (int) (delta * 1000);

					if (maxAmp < dataPoint)
						maxAmp = dataPoint;
					if (minAmp > dataPoint)
						minAmp = dataPoint;
				}

			}

			// check npts
			if (readHeader == false)
				npts = dataIndex;

			// print performance benchmarks
			System.out
					.println("Total read time " + totalTime + " milliseconds");

			System.out.println("Total Time :"
					+ (Calendar.getInstance().getTimeInMillis() - sumtime
							.getTimeInMillis()) + " milliseconds");
			
			// Fix: 19-04-2011 by Cong Huynh 
			// Finish reading, close the stream to release resources.
			input.close();

			
		} catch (Exception e) {			
			System.out.println("Exception " + e);
		}
		
		System.out.println("returning " + milliSecondsPassed + " milliseconds");
		return milliSecondsPassed / 1000;
	}

	public static int myarr2int(byte[] arr, int start) {

		int f = arr[start] & 0xff;
		int mf = arr[start + 1] & 0xff;
		int mp = arr[start + 2] & 0xff;
		int p = arr[start + 3] & 0xff;
		int retval = ((((f << 8 | mf) << 8 | mp) << 8) | p);
		return retval;
	}

	public static int myarr2short(byte[] arr, int start) {
		int f = arr[start] & 0xff;
		int mf = arr[start + 1] & 0xff;
		int retval = (f << 8 | mf);
		return retval;
	}

	public GregorianCalendar getCalFromDOY(int year, int doy, int hr, int min,
			int sec) {
		int month = 0;
		int day = 0;
		boolean isLeapYear = (year % 4) == 0 && (year % 400) != 0;
		while (doy > 0) {
			month++;
			day = doy;
			switch (month) {
			case 1:
				doy = doy - 31;
				break;
			case 2:
				if (isLeapYear) {
					doy = doy - 29;
					break;
				} else {
					doy = doy - 28;
					break;
				}
			case 3:
				doy = doy - 31;
				break;
			case 4:
				doy = doy - 30;
				break;
			case 5:
				doy = doy - 31;
				break;
			case 6:
				doy = doy - 30;
				break;
			case 7:
				doy = doy - 31;
				break;
			case 8:
				doy = doy - 31;
				break;
			case 9:
				doy = doy - 30;
				break;
			case 10:
				doy = doy - 31;
				break;
			case 11:
				doy = doy - 30;
				break;
			case 12:
				doy = doy - 31;
				break;
			default:
				return null;
			}
		}

		return (new GregorianCalendar(year, month - 1, day, hr, min, sec));
	}

}
