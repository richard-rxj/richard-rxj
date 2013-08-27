/**
 * 
 */
package wCharging.test;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import wCharging.model.ChargingRequest;
import wCharging.model.ChargingRequestQueue;
import wCharging.util.HtmlLogFormatter;

/**
 * @author user
 *
 */
public class SimulationSetting {
	
	//k-mean
	public static  int kValue=3;
	public static  double kMeanThreshold=0.05;     //used to stop k-mean
	
	//charger
	public static  double timeLimit=60;             //s   1800
    public static  double stepWaitingConstant=1;   //if no feasible then keep still and wait
	public static  double startX=0;                //depot of the charger
	public static  double startY=0;
	
	//network topology
	public static  double xRange=50;              //500
	public static  double yRange=50;              //500
	public static  double travelSpeed=5;          //m/s    5
    public static  double chargingConstant=3;    //second  10

	
    //test
    public static  int ciShu=20;
    
    
    public static final Logger gLog=Logger.getLogger(SimulationSetting.class.getName());  
    static
    {
    	gLog.setLevel(Level.ALL);
    	//gLog.addHandler(new ConsoleHandler());
    	try {
    		FileHandler htmlHandler=new FileHandler(SimulationSetting.class.getName()+".html");
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
    
    public static ChargingRequestQueue generateRequest(int totalRequest)
    {
    	gLog.warning("request generating details:");
    	ChargingRequestQueue resultQueue=new ChargingRequestQueue();
    	for(int i=1;i<=totalRequest;i++)
    	{
    		ChargingRequest c=new ChargingRequest();
    		c.setId(i);
    		c.setxAxis(Math.random()*SimulationSetting.xRange);
    		c.setyAxis(Math.random()*SimulationSetting.yRange);
    		c.setReleaseTime(Math.random()*SimulationSetting.timeLimit);   	
    		resultQueue.add(c);
    		gLog.info(c.toString());
    	}
    	gLog.warning("the total requests are: "+resultQueue.size());
    	return resultQueue;
    }
    
    /**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SimulationSetting.generateRequest(100);
	}

}
