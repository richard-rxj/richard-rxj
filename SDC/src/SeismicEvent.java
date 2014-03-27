import java.util.*;

public class SeismicEvent {

	public String eventName, eventCatalog;
	public float eventLat, eventLon, eventDep, eventMag;
	public GregorianCalendar eventStart, eventFinish;

	public SeismicEvent() {
		/** Do nothing */
	}

	public SeismicEvent(GregorianCalendar start, GregorianCalendar finish) {
		eventName = "";
		eventCatalog = "";
		eventLat = -12345;
		eventLon = -12345;
		eventDep = -12345;
		eventMag = -12345;
		eventStart = start;
		eventFinish = finish;
	}

	public SeismicEvent(String n, String catalog, float lat, float lon,
			float dep, float mag, GregorianCalendar start,
			GregorianCalendar finish) {
		eventName = n;
		eventCatalog = catalog;
		eventLat = lat;
		eventLon = lon;
		eventDep = dep;
		eventMag = mag;
		eventStart = start;
		eventFinish = finish;
	}

	public void setName(String name) {
		eventName = name;
	}
}
