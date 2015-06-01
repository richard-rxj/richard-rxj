package wCharging.alg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import wCharging.model.ChargingRequest;
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
		return null;
	}

}
