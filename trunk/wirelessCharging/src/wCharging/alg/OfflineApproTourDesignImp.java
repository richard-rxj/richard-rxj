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
		end.setId(-11);
		end.setReleaseTime(0);
		end.setxAxis(this.startX);
		end.setyAxis(this.startY);
		
		int recursiveLimit=(int)(Math.log(this.requestQueue.size())/Math.log(2));
		
		gLog.warning("----currentTime<"+this.currentTime+">---timeLimit<"+this.timeLimit+">---recursiveLimit<"+recursiveLimit
                +">----Start(id="+start.getId()+", releasetime="+start.getReleaseTime()
                +")----End(id="+end.getId()+", releasetime="+end.getReleaseTime()+")");
		
		ArrayList<ChargingRequest> result=this.recursiveDesign(start, end, this.currentTime, this.timeLimit, this.requestQueue, recursiveLimit);
		
		
		
		
		gLog.warning("***********The final result is***************");
		for(ChargingRequest c: result)
		{
			gLog.info(c.toString());
		}
		gLog.warning("the total service served: "+result.size());
		return result;
	}

	
	@SuppressWarnings("unchecked")
	private ArrayList<ChargingRequest> recursiveDesign(ChargingRequest start, ChargingRequest end, double currentTime, double relativeTimeLimit, ChargingRequestQueue subQueue, double recursiveLimit)
	{
		//gLog.info("current time: "+currentTime+"  relativeTimeLimit: "+relativeTimeLimit+" recursiveLimit: "+recursiveLimit);
		
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
				//gLog.warning("P1----currentTime<"+currentTime+">---timeLimit<"+firstHalfLimit+">---recursiveLimit<"+(recursiveLimit-1)
		        //        +">----Start(id="+start.getId()+", releasetime="+start.getReleaseTime()
		        //        +")----End(id="+c.getId()+", releasetime="+c.getReleaseTime()+")");
				//gLog.warning("P2----recursiveLimit<"+(recursiveLimit-1)
		        //       +">----Start(id="+c.getId()+", releasetime="+c.getReleaseTime()
		        //        +")----End(id="+end.getId()+", releasetime="+end.getReleaseTime()+")");
				ChargingRequestQueue subQueue1=(ChargingRequestQueue)subQueue.clone();
				subQueue1.removeById(c.getId());
				
				ArrayList<ChargingRequest> p1=this.recursiveDesign(start, c, currentTime, firstHalfLimit, subQueue1, recursiveLimit-1);
				ArrayList<ChargingRequest> p2=this.recursiveDesign(c, end, currentTime+firstHalfLimit, relativeTimeLimit-firstHalfLimit, subQueue1.getSubQueueByRequestList(p1), recursiveLimit-1);
				
				
				if((p1!=null)&&(p2!=null)&&(p1.size()+p2.size()>tmax))
				{
					//begin of debug
					if((p1.size()+p2.size())>2)
					{
						gLog.info("----P1--currentTime<"+currentTime+">---firstBudget<"+firstHalfLimit
								                             +">----Start(id="+start.getId()+", releasetime="+start.getReleaseTime()
								                             +")----End(id="+c.getId()+", releasetime="+c.getReleaseTime()+")");
						for(ChargingRequest tC:p1)
						{
							gLog.info(tC.toString());
						}
						gLog.info("----P2--leftBudget<"+(relativeTimeLimit-firstHalfLimit)
								                             +">----Start(id="+c.getId()+", releasetime="+c.getReleaseTime()
								                             +")----End(id="+end.getId()+", releasetime="+end.getReleaseTime()+")");
						for(ChargingRequest tC:p2)
						{
							gLog.info(tC.toString());
						}
					}
					//end of debug
					result=(ArrayList<ChargingRequest>) p1.clone();
					result.addAll(p2);
					tmax=p1.size()+p2.size();
				}
				
			}			
		}
		
		if(result.size()>3)
		{
			gLog.warning("--------------one solution is--------------");
			for(ChargingRequest c:result)
			{
				gLog.info(c.toString());
			}
		}
		return result;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ChargingRequestQueue testQueue=SimulationSetting.generateRequest(10);
		gLog.warning("the total requests are: "+testQueue.size());
		OfflineApproTourDesignImp testSolution=new OfflineApproTourDesignImp();
		testSolution.setRequestQueue(testQueue);
		testSolution.setTimeLimit(SimulationSetting.timeLimit);
		testSolution.setStartX(SimulationSetting.startX);
		testSolution.setStartY(SimulationSetting.startY);
		testSolution.setCurrentTime(0);
		testSolution.design();
	}

}
