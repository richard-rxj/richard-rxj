package wCharging.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import wCharging.alg.ClusterTourDesignImp;
import wCharging.alg.NearestTourDesignImp;
import wCharging.model.ChargingRequest;
import wCharging.model.ChargingRequestQueue;
import wCharging.util.HtmlLogFormatter;

public class KvalueTest {

	public static final Logger gLog=Logger.getLogger(SimulationSetting.class.getName());  
    static
    {
    	gLog.setLevel(Level.ALL);
    	//gLog.addHandler(new ConsoleHandler());
    	try {
    		FileHandler htmlHandler=new FileHandler(KvalueTest.class.getName()+".html");
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
	
	public static void test(int tTimeLimit) throws FileNotFoundException
	{
		SimulationSetting.xRange=500;
		SimulationSetting.yRange=500;
		SimulationSetting.timeLimit=tTimeLimit;
		SimulationSetting.ciShu=20;
		SimulationSetting.chargingConstant=2;
		SimulationSetting.travelSpeed=8;
		
		
		
		int[] requestNumSet={100,200,300,400,500,600,700,800,900,1000};
		int[] kValueSet={5,10,15,20,25,30};
		int cishu=SimulationSetting.ciShu;
		String[] algSet={"Nearest","Cluster"};
		
		String tFileName="test/kvalue";
		File tf=new File(tFileName);
		if(!tf.exists())
		{
			tf.mkdirs();
		}
		
		PrintWriter  pwAlg=new PrintWriter(new OutputStreamWriter(new FileOutputStream("kvalue_"+tTimeLimit+".txt",true)));
		
		
		for(int requestNumIndex=0;requestNumIndex<requestNumSet.length;requestNumIndex++)
		{
			int requestNum=requestNumSet[requestNumIndex];
			pwAlg.print(requestNum+" ");
			
			int nearestResult=0;
			int[] clusterResultSet=new int[kValueSet.length];
			for(int i=0;i<cishu;i++)
			{
				gLog.warning("requestNum: "+requestNum+"   Iteration: "+i);
				
				ChargingRequestQueue testQueue=SimulationSetting.generateRequest(requestNum);
				
		
				ChargingRequestQueue testQueue1=(ChargingRequestQueue)testQueue.clone();
				NearestTourDesignImp nearestSolution=new NearestTourDesignImp();
				nearestSolution.setRequestQueue(testQueue1);
				nearestSolution.setTimeLimit(SimulationSetting.timeLimit);
				nearestSolution.setStartX(SimulationSetting.startX);
				nearestSolution.setStartY(SimulationSetting.startY);
				nearestSolution.setCurrentTime(0);
				ArrayList<ChargingRequest> tList=nearestSolution.design();
				nearestResult=nearestResult+tList.size();
				
				gLog.info("nearestSolution: "+tList.size());
				
				ChargingRequestQueue[] testQueueSet=new ChargingRequestQueue[kValueSet.length];
				
				for(int j=0;j<kValueSet.length;j++)
				{
					testQueueSet[j]=(ChargingRequestQueue)testQueue.clone();
					ClusterTourDesignImp clusterSolution=new ClusterTourDesignImp();
					clusterSolution.setRequestQueue(testQueueSet[j]);
					clusterSolution.setTimeLimit(SimulationSetting.timeLimit);
					clusterSolution.setStartX(SimulationSetting.startX);
					clusterSolution.setStartY(SimulationSetting.startY);
					clusterSolution.setCurrentTime(0);
					clusterSolution.setkValue(kValueSet[j]);
					tList=clusterSolution.design();
					clusterResultSet[j]=clusterResultSet[j]+tList.size();
					
					gLog.info("clusterSolution(k="+kValueSet[j]+"): "+tList.size());
				}
			}
			nearestResult=nearestResult/cishu;
			pwAlg.print(nearestResult+" ");
			
			for(int j=0;j<kValueSet.length;j++)
			{
				clusterResultSet[j]=clusterResultSet[j]/cishu;
			pwAlg.print(clusterResultSet[j]+" ");
			}
			pwAlg.println();
			
			gLog.warning("average: <nearestSolution: "+nearestResult+">"
				     +" <clusterSolution: "+clusterResultSet+">");
		}
		pwAlg.close();
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
