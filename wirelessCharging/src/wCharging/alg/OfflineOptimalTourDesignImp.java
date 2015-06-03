package wCharging.alg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import wCharging.model.ChargingRequest;
import wCharging.model.ChargingRequestQueue;
import wCharging.model.ChargingTour;
import wCharging.util.HtmlLogFormatter;

public class OfflineOptimalTourDesignImp extends BaseTourDesign {

	public static final Logger gLog = Logger
			.getLogger(OfflineOptimalTourDesignImp.class.getName());
	static {
		gLog.setLevel(Level.ALL);
		// gLog.addHandler(new ConsoleHandler());
		try {
			FileHandler htmlHandler = new FileHandler(
					OfflineOptimalTourDesignImp.class.getName() + ".html");
			htmlHandler.setFormatter(new HtmlLogFormatter());
			gLog.addHandler(htmlHandler);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<ChargingRequest> design() {
		// TODO Auto-generated method stub
		ArrayList<ChargingTour> tAllTours = this
				.getCombinationRecursive(this.requestQueue);
		ArrayList<ChargingRequest> tMax = null;
		for (int i = 0; i < tAllTours.size(); i++) {
			ChargingTour tCurrent = tAllTours.get(i);
			if (tMax == null || tMax.size() < tCurrent.getSequence().size()) {
				tMax = tCurrent.getSequence();
			}
		}
		return tMax;
	}


	private ArrayList<ChargingTour> getCombinationRecursive(
			ChargingRequestQueue q) {
		ArrayList<ChargingTour> tTour = new ArrayList<ChargingTour>();

		if (q == null || q.size() == 0) {
			tTour.add(new ChargingTour());
			return tTour;
		}

		for (int i = 0; i < q.size(); i++) {
			ChargingRequest current = q.get(i);
			ChargingRequestQueue subQueue = (ChargingRequestQueue) q.clone();
			subQueue.remove(current);
			ArrayList<ChargingTour> subTours = getCombinationRecursive(subQueue);
			for (int j = 0; j < subTours.size(); j++) {
				ChargingTour subTour = new ChargingTour();
				subTour.addChargingRequest(current);
				subTour.addCharingTour(subTours.get(j));
				if (subTour.getTotalTime(startX, startY) <= this.timeLimit) {
					tTour.add(subTour);
				}
			}
		}

		return tTour;
	}

}
