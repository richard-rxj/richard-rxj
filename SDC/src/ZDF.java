import java.io.*;
import java.util.*;

/** A writer which converts miniseed data to the ZDF format. */

public class ZDF {

	/** zdf header information */
	int irc; /* Header length (180) */
	char[] projct = new char[4]; /* Project name */
	char[] pyr = new char[4]; /* Project year */
	char[] dtype = new char[4]; /* Data type */
	char[] cevent = new char[64]; /* Event identifier, i.e. source of information. */
	char[] chyp = new char[4]; /*
								 * Source of hypocentre, e.g. EHB or NEIC
								 * catalog
								 */

	// Origin information of the earthquake
	int oyr; /* Origin time: year */
	int omon; /* Origin time: month */
	int oday; /* Origin time: day */
	int ohr; /* Origin time: hour */
	int omin; /* Origin time: minute */
	float osec; /* Origin time: second */
	float lat; /* Hypocentre: latitude */
	float lon; /* Hypocentre: longitude */
	float dep; /* Hypocentre: depth */
	float mag; /* Hypocentre: magnitude */

	// Start date/time of the first data segment recorded.
	int bday; /* Begin rec.: day of year */
	int bhr; /* Begin rec.: hour */
	int bmin; /* Begin rec.: minute */
	float bsec; /* Begin rec.: second */

	// End date/time of the last data segment recorded.
	int eday; /* End record: day of year */
	int ehr; /* End record: hour */
	int emin; /* End record: minute */
	float esec; /* End record: second */
	// int nsta; /* Number of stations */
	/* unnecessary field since it can be inferred from the station list */

	int ndgkm; /* range deg(0), km(1) */
	int nrft; /* time rel to origin(0) or event start(1) */
	int nrot; /* ZNE(0),ZRT(1),other(2) */
	int nsmpl; /* Nominal sampling rate (per sec) */
	char[] cend = new char[4]; /* end of header marker */

	ArrayList<ZDFStation> stations;

	public ZDF() {
		/** Specify header length */
		irc = 180;
	}

	/**
	 * 
	 * @param m
	 *            The miniseed data structure to be converted into ZDF.
	 */
	public ZDF(SpecialMiniSeedReader m) {

		/** Specify header length */
		irc = 180;

		// Get the event
		SeismicEvent e = m.eventInfo;
		NetworkInfo n = m.statInfo;

		int i;

		// set project name
		String networkName = n.networkFullName;
		for (i = 0; i < 4 && i < networkName.length(); i++) {
			projct[i] = networkName.charAt(i);
		}
		while (i < 4) {
			projct[i] = ' ';
			i++;
		}

		// set project year
		String projectYear = "" + n.getStartDate().get(Calendar.YEAR);
		for (i = 0; i < 4 && i < projectYear.length(); i++) {
			pyr[i] = projectYear.charAt(i);
		}
		while (i < 4) {
			pyr[i] = ' ';
			i++;
		}

		// set data type
		for (i = 0; i < 4; i++)
			dtype[i] = '-';

		// set event name
		String eventName = e.eventName;
		for (i = 0; i < 64 && i < eventName.length(); i++) {
			cevent[i] = eventName.charAt(i);
		}
		while (i < 64) {
			cevent[i] = ' ';
			i++;
		}

		// source of hypocentre information
		String eventSource = e.eventCatalog;
		for (i = 0; i < 4 && i < eventSource.length(); i++) {
			chyp[i] = eventSource.charAt(i);
		}
		while (i < 4) {
			chyp[i] = ' ';
			i++;
		}

		// Set Event Time
		oyr = e.eventStart.get(Calendar.YEAR);
		omon = e.eventStart.get(Calendar.MONTH) + 1;
		oday = e.eventStart.get(Calendar.DAY_OF_MONTH);
		ohr = e.eventStart.get(Calendar.HOUR_OF_DAY);
		omin = e.eventStart.get(Calendar.MINUTE);
		osec = e.eventStart.get(Calendar.SECOND);

		// Other event information
		lat = e.eventLat;
		lon = e.eventLon;
		dep = e.eventDep;
		mag = e.eventMag;

		// record start and finish
		// Assume event starts at record start
		// bday = e.eventStart.get(Calendar.DAY_OF_MONTH);
		bday = e.eventStart.get(Calendar.DAY_OF_YEAR);
		bhr = e.eventStart.get(Calendar.HOUR_OF_DAY);
		bmin = e.eventStart.get(Calendar.MINUTE);
		bsec = e.eventStart.get(Calendar.SECOND);

		// eday = e.eventFinish.get(Calendar.DAY_OF_MONTH);
		eday = e.eventFinish.get(Calendar.DAY_OF_YEAR);
		ehr = e.eventFinish.get(Calendar.HOUR_OF_DAY);
		emin = e.eventFinish.get(Calendar.MINUTE);
		esec = e.eventFinish.get(Calendar.SECOND);

		// stations set a zero
		// nsta = 0;

		// range is in degrees
		ndgkm = 0;

		// Time rel to origin
		nrft = 0;

		// ZNE
		nrot = 0;

		// set sample rate per second
		nsmpl = (int) (1 / m.delta);

		// end of header marker
		for (i = 0; i < 4; i++)
			cend[i] = 'z';

		// Initiate list of stations
		stations = new ArrayList<ZDFStation>();

		// add data of m to station
		addStation(m);
	}

	/**
	 * Add a writer for ZDF station infor available from the given
	 * SpecialMiniSeedReader
	 */
	public void addStation(SpecialMiniSeedReader m) {
		stations.add(new ZDFStation(m));
	}

	/**
	 * Get last station added to this ZDF structure.
	 */
	public ZDFStation getLastStation() {
		ZDFStation stat = stations.get(stations.size() - 1);
		return stat;
	}

	/** Write zdf to a file */
	public int writeZDF(File f) {

		try {
			/** Initiate file writer */
			FileOutputStream fos = new FileOutputStream(f);
			DataOutputStream dos = new DataOutputStream(fos);

			/** Writing the header */

			// header length
			dos.writeInt(irc);

			int i;
			// project name
			for (i = 0; i < 4; i++)
				dos.writeByte((byte) projct[i]);

			// project year
			for (i = 0; i < 4; i++)
				dos.writeByte((byte) pyr[i]);

			// data type
			for (i = 0; i < 4; i++)
				dos.writeByte((byte) dtype[i]);

			// event identifier
			for (i = 0; i < 64; i++)
				dos.writeByte((byte) cevent[i]);

			// source to hypocentre
			for (i = 0; i < 4; i++)
				dos.writeByte((byte) chyp[i]);

			// origin time of earthquake events
			dos.writeInt(oyr);
			dos.writeInt(omon);
			dos.writeInt(oday);
			dos.writeInt(ohr);
			dos.writeInt(omin);
			dos.writeFloat(osec);

			// earthquake event location and magnitude
			dos.writeFloat(lat);
			dos.writeFloat(lon);
			dos.writeFloat(dep);
			dos.writeFloat(mag);

			// record start and end
			dos.writeInt(bday);
			dos.writeInt(bhr);
			dos.writeInt(bmin);
			dos.writeFloat(bsec);
			dos.writeInt(eday);
			dos.writeInt(ehr);
			dos.writeInt(emin);
			dos.writeFloat(esec);

			// other info
			dos.writeInt(stations.size()); // number of stations
			dos.writeInt(ndgkm);
			dos.writeInt(nrft);
			dos.writeInt(nrot);

			dos.writeInt(nsmpl); // write nominal sampling rate
			// dos.writeFloat(nsmpl); // write nominal sampling rate

			// write end marker
			for (i = 0; i < 4; i++)
				dos.writeByte((byte) cend[i]);

			/** Now write the station information */
			for (i = 0; i < stations.size(); i++) {
				ZDFStation curStat = stations.get(i);
				if (curStat.writeZdfStation(dos) != 1) {
					System.err.println("Cannot write station!");
					
			
					/** Close file writer - clean up*/
					if (dos != null) {
						dos.flush();
						dos.close();
					}
					
					if (fos != null) {			
						fos.flush();
						fos.close();
					}
					return 0;
				}
			}

			/** Close file writer anyway - clean up*/
			if (dos != null) {
				dos.flush();
				dos.close();
			}
			
			if (fos != null) {			
				fos.flush();
				fos.close();
			}
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

		return 1;
	}

}
