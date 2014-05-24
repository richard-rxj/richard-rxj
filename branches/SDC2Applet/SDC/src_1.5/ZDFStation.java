import java.io.*;
import java.util.*;

/**
 * A writer for ZDF station infor.
 */
public class ZDFStation {

	/** Station Header information */
	public int irs; // length of station data, including all the components,
	// but excluding the length of the "irs" field
	public char[] name = new char[4]; // Station code, 4 characters long.
	public int sday; // Station begin: day of year
	public int shr; // Station begin: hour
	public int smin; // Station begin: min
	public float ssec; // Station begin: sec
	public float tcrr; // time correction
	public float delta; // range degrees or km
	public float azim; // Azimuth to source (earthquake)
	public float sampin; // Sample interval for station

	// public int ncomp = 0; // number of components, which can be inferred from
	// the 'componentList' field.

	public char[] cend = { 's', 's', 'z', 'z' }; // end of station marker

	protected ArrayList<ZDFComponent> componentList;

	/** Auxilliary variables */
	GregorianCalendar stationStart, stationFinish;
	public float statLat, statLon, statDep;

	public ZDFStation() {
		// do nothing
	}

	public ZDFStation(SpecialMiniSeedReader m) {

		// irs = 44; // initial station length, only including the length of the
		// header fields, not the end of station field (cend).
		irs = 40;

		// extract station information from SpecialMiniSeedReader.
		// set the header of the trace of data collected by the current station.
		setStationInfo(m);

		// m.start;

		// set time correction
		tcrr = 0;

		// set event and azimuth
		if (m.eventInfo.eventLat != -12345) {
			delta = m.azi.rangeDg;
			azim = m.azi.azim;
		} else {
			delta = 0;
			azim = 0;
		}

		// set sample interval
		sampin = m.delta;

		// initiate number of components
		componentList = new ArrayList<ZDFComponent>();

		// add reader data to component
		addComponent(m);
	}

	public void addComponent(SpecialMiniSeedReader m) {
		ZDFComponent zcomp = new ZDFComponent(m);
		componentList.add(zcomp);
		// irs += (6 * 4 + zcomp.ndat * 4);
		irs += (5 * 4 + zcomp.ndat * 4);
		// ncomp++;
	}

	public void setStationInfo(SpecialMiniSeedReader m) {
		NetworkInfo n = m.statInfo;

		// set station name
		int i;
		for (i = 0; i < 4 && i < n.station.length(); i++) {
			name[i] = n.station.charAt(i);
		}
		while (i < 4) {
			name[i++] = ' ';
		}

		// get starting and ending date
		// Note: Here we should get the start and end dates and time
		// of the data rather than the station's deployment.
		stationStart = m.start; // n.getStartDate();
		stationFinish = m.finish; // n.getEndDate();

		// set header parameters
		// sday = stationStart.get(Calendar.DAY_OF_MONTH);
		sday = stationStart.get(Calendar.DAY_OF_YEAR);
		shr = stationStart.get(Calendar.HOUR_OF_DAY);
		smin = stationStart.get(Calendar.MINUTE);
		ssec = stationStart.get(Calendar.SECOND);

		// set station coordinate
		statLat = Float.parseFloat(n.lat);
		statLon = Float.parseFloat(n.lon);
		statDep = Float.parseFloat(n.dep);
	}

	public int writeZdfStation(DataOutputStream dos) {
		try {

			/** Writing the header */
			dos.writeInt(irs);

			for (int i = 0; i < 4; i++)
				dos.writeByte((byte) name[i]);

			dos.writeInt(sday);
			System.out.println(sday + " sday");

			dos.writeInt(shr);
			System.out.println(shr + " shr");

			dos.writeInt(smin);
			System.out.println(smin + " smin");

			dos.writeFloat(ssec);
			System.out.println(ssec + " ssec");

			dos.writeFloat(tcrr);
			System.out.println(tcrr + " tcrr");

			dos.writeFloat(delta);
			System.out.println(delta + " delta");

			dos.writeFloat(azim);
			System.out.println(azim + " azim");

			dos.writeFloat(sampin);
			System.out.println(sampin + " sampin");

			dos.writeInt(componentList.size());
			System.out.println(componentList.size() + " components");

			/** Now write the data information */
			for (int i = 0; i < componentList.size(); i++) {
				ZDFComponent curComp = componentList.get(i);
				curComp.writeZdfComponent(dos);
			}

			for (int i = 0; i < 4; i++) {
				dos.writeByte((byte) cend[i]);
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
