/**
 * 
 */
package wCharging.alg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import wCharging.model.ChargingRequest;
import wCharging.model.ChargingRequestQueue;
import wCharging.test.SimulationSetting;
import wCharging.util.HtmlLogFormatter;
import wCharging.util.TravelTimeComparator;

/**
 * @author u4964526
 *
 */
public class NearestTourDesignImp extends BaseTourDesign {

	public static final Logger gLog=Logger.getLogger(NearestTourDesignImp.class.getName());  
    static
    {
    	gLog.setLevel(Level.ALL);
    	//gLog.addHandler(new ConsoleHandler());
    	try {
    		FileHandler htmlHandler=new FileHandler(NearestTourDesignImp.class.getName()+".html");
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
		// TODO Auto-generated method stub
		
		ArrayList<ChargingRequest> result=new ArrayList<ChargingRequest>();
		
		double currentX=this.startX;
		double currentY=this.startY;
		
		while(currentTime<=this.timeLimit)
		{
			gLog.info("**********new choice again*********");
			
			ChargingRequest target=this.subDesign(currentX, currentY, this.currentTime, this.requestQueue.getSubQueueByReleaseTime(currentTime));
			if(target==null)   //if no feasible, then stay still
			{
				target=new ChargingRequest();
				target.setId(-1);
				target.setProcessTime(SimulationSetting.stepWaitingConstant);
				gLog.info("current time "+currentTime
						+": stay still wait "+SimulationSetting.stepWaitingConstant+" s");
			}
			else
			{
				target.setProcessTime(target.getTravelTime());
				this.requestQueue.removeById(target.getId());      //update the queue
				currentX=target.getxAxis();
				currentY=target.getyAxis();
				gLog.info("current time "+currentTime
						+"charging "+target.toString());
				
				result.add(target);
			}
			this.currentTime=this.currentTime+target.getProcessTime();       //update time
		}
		
		gLog.warning("the total service served: "+result.size());
		
		return result;
	}

	
	private ChargingRequest subDesign(double currentX, double currentY, double currentTime, ChargingRequestQueue currentQueue)
	{
		ChargingRequest result=null;
		
		for(ChargingRequest c:currentQueue)
		{
			c.ComputeBothTime(currentX, currentY, this.startX, this.startY);
		}
		
		ChargingRequestQueue  feasibleQueue=currentQueue.getSubQueueByTimeLimit(this.timeLimit-currentTime);
		
		Collections.sort(feasibleQueue, new TravelTimeComparator(true));
		
		result=currentQueue.peek();
		
		return result;
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ChargingRequestQueue testQueue=SimulationSetting.generateRequest(100);
		gLog.warning("the total requests are: "+testQueue.size());
		NearestTourDesignImp testSolution=new NearestTourDesignImp();
		testSolution.setRequestQueue(testQueue);
		testSolution.setTimeLimit(SimulationSetting.timeLimit);
		testSolution.setStartX(SimulationSetting.startX);
		testSolution.setStartY(SimulationSetting.startY);
		testSolution.setCurrentTime(0);
		testSolution.design();
	}

}
