import java.io.*;
import java.util.*;

public class NetworkInfo implements Serializable {
	public String network;
	public String station;
	public String lat;
	public String lon;
	public String dep;
	public String startdate;
	public String starttime;
	public String enddate;
	public String endtime;
	public String networkFullName;
	public String networkLocation;

	public NetworkInfo() {
		this("", "", "", "", "", "", "", "", "", "", "");
	}

	public NetworkInfo(String n, String s, String n1, String n2, String n3,
			String sd, String st, String ed, String et, String fullname,
			String location) {
		network = n;
		station = s;
		lat = n1;
		lon = n2;
		dep = n3;
		startdate = sd;
		starttime = st;
		enddate = ed;
		endtime = et;
		networkFullName = fullname;
		networkLocation = location;
	}

	public String getStation() {
		return station;
	}

	public GregorianCalendar getStartDate() {
		GregorianCalendar cal;
		String[] curSD = startdate.split("/");
		if (curSD.length == 3) {
			cal = new GregorianCalendar(Integer.parseInt(curSD[2]), Integer
					.parseInt(curSD[1]) - 1, Integer.parseInt(curSD[0]));
		} else {
			cal = new GregorianCalendar(1900, 1, 1);
		}
		return cal;
	}

	public GregorianCalendar getEndDate() {
		GregorianCalendar cal;
		String[] curED = enddate.split("/");
		if (curED.length == 3) {
			cal = new GregorianCalendar(Integer.parseInt(curED[2]), Integer
					.parseInt(curED[1]) - 1, Integer.parseInt(curED[0]));
		} else {
			cal = new GregorianCalendar(2100, 1, 1);
		}
		return cal;
	}

}
