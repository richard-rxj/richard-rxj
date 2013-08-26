/**
 * 
 */
package wCharging.alg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import wCharging.model.ChargingRequest;
import wCharging.model.ChargingRequestQueue;
import wCharging.test.SimulationSetting;
import wCharging.util.HtmlLogFormatter;

/**
 * @author u4964526
 *
 */
public class OfflineApproTourDesignImp extends BaseTourDesign {

	public static final Logger gLog=Logger.getLogger(OfflineApproTourDesignImp.class.getName());  
    static
    {
    	gLog.setLevel(Level.ALL);
    	//gLog.addHandler(new ConsoleHandler());
    	try {
    		FileHandler htmlHandler=new FileHandler(OfflineApproTourDesignImp.class.getName()+".html");
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
	
	
	/* (non-Javadoc)
	 * @see wCharging.alg.BaseTourDesign#design()
	 */
	@Override
	public ArrayList<ChargingRequest> design() {
		ChargingRequest start=new ChargingRequest();
		start.setId(-1);
		start.setReleaseTime(0);
		start.setxAxis(this.startX);
		start.setyAxis(this.startY);
		
		ChargingRequest end=new ChargingRequest();
		end.setId(-1);
		end.setReleaseTime(0);
		end.setxAxis(this.startX);
		end.setyAxis(this.startY);
		
		ArrayList<ChargingRequest> result=this.recursiveDesign(start, end, this.currentTime, this.timeLimit, this.requestQueue, this.requestQueue.size());
		
		gLog.info("***********The result is***************");
		for(ChargingRequest c: result)
		{
			gLog.info(c.toString());
		}
		return result;
	}

	
	@SuppressWarnings("unchecked")
	private ArrayList<ChargingRequest> recursiveDesign(ChargingRequest start, ChargingRequest end, double currentTime, double relativeTimeLimit, ChargingRequestQueue subQueue, double recursiveLimit)
	{
		gLog.info("current time: "+currentTime+"  relativeTimeLimit: "+relativeTimeLimit+" recursiveLimit: "+recursiveLimit);
		
		ArrayList<ChargingRequest> result=new ArrayList<ChargingRequest>();
		double tTime=Math.sqrt(Math.pow(start.getxAxis()-end.getxAxis(), 2)+Math.pow(start.getyAxis()-end.getyAxis(), 2))/SimulationSetting.travelSpeed;
		if((tTime>relativeTimeLimit)||(currentTime+tTime<end.getReleaseTime())) 
		{
			return null;
		}
		//result.add(start);
		result.add(end);
		if(recursiveLimit==0)
		{
			return result;
		}
		int tmax=result.size();		
		for(ChargingRequest c:subQueue)
		{
			for(double firstHalfLimit=1; firstHalfLimit<=relativeTimeLimit; firstHalfLimit=firstHalfLimit+1)
			{
				ArrayList<ChargingRequest> p1=this.recursiveDesign(start, c, currentTime, firstHalfLimit, subQueue, recursiveLimit-1);
				ArrayList<ChargingRequest> p2=this.recursiveDesign(c, end, firstHalfLimit, relativeTimeLimit-firstHalfLimit, subQueue.getSubQueueByRequestList(p1), recursiveLimit-1);
				if((p1!=null)&&(p2!=null)&&(p1.size()+p2.size()>tmax))
				{
					result=(ArrayList<ChargingRequest>) p1.clone();
					result.addAll(p2);
					tmax=p1.size()+p2.size();
				}
			}			
		}
		return result;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
