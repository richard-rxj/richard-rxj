
/**
 * 
 */
package dr.alg.anu.au;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import network.dr.alg.anu.au.BiNetwork;
import network.dr.alg.anu.au.GateWay;
import network.dr.alg.anu.au.GateWayPriorityWeightComparator;
import network.dr.alg.anu.au.GateWayUnitUtilityGainComparator;
import network.dr.alg.anu.au.LabResult;
import network.dr.alg.anu.au.Node;

/**
 * @author u4964526
 *
 */
public class GC13_Alg {

	public static LabResult disTraMaxUtilityGainTourDesign(BiNetwork bNet, double iniMinRange, double iniMaxRange, double deltaRange, double iniTimeStamp, double deltaTimeStamp, ArrayList<GateWay> solution) throws IOException
	{

		ArrayList<Node> nodeSet = bNet.getnList(); //
		ArrayList<GateWay> gatewaySet = bNet.getgList(); //
		
		//initial 
		LabResult result=new LabResult();
		double totalUtility=0;
		double totalSojournTime=0;
		double totalMovingTime=0;
		
		
		
		boolean flag=true;
		double tTourTime=ExperimentSetting.tourTime;
		double tUsedTime=0;
		double tMovingTime=0;
		double tSojournTime=0;
		
		double tSinkX=ExperimentSetting.initSinkX;
		double tSinkY=ExperimentSetting.initSinkY;
		
		double tSinkSpeed=ExperimentSetting.mSpeed;
		double lastBackTime=0;
		
		
		for(int i=0;i<gatewaySet.size();i++)
		{
			GateWay tGateWay=gatewaySet.get(i);
			tGateWay.setTimeStamp(tUsedTime-iniTimeStamp-deltaTimeStamp);
		}
		
		
		while(flag)
		{
			ArrayList<GateWay> tGateWaySet=new ArrayList<GateWay>();
			ArrayList<GateWay> tDistanceGateWaySet=new ArrayList<GateWay>();

			
			for(int i=0;i<gatewaySet.size();i++)
			{
				GateWay tGateWay=gatewaySet.get(i);
				double tX=tSinkX-tGateWay.getX();
				double tY=tSinkY-tGateWay.getY();
				double tD=Math.sqrt(tX*tX+tY*tY);
				tGateWay.setDistance(tD);
				tGateWay.setMovingTime(tD/tSinkSpeed);  //calculate moving time
				
				tX=ExperimentSetting.initSinkX-tGateWay.getX();
				tY=ExperimentSetting.initSinkY-tGateWay.getY();
				tD=Math.sqrt(tX*tX+tY*tY);
				tGateWay.setBackTime(tD/tSinkSpeed);
				
				
				/*
				 * first select 
				 */
				if((tGateWay.getMovingTime()+tGateWay.getBackTime())>tTourTime)
				{
					tGateWay.setFeasible(0);
				}
				else
				{
					tGateWay.setFeasible(1);
				}
																												
			}
			
			/*
			 * Step1----distance
			 */
			double min=iniMinRange;                    //1.5R
			double max=iniMaxRange;                      //3R
			boolean distanceFlag=true;
			while(distanceFlag)
			{	
				for(int i=0;i<gatewaySet.size();i++)
				{
					GateWay tGateWay=gatewaySet.get(i);
					if(tGateWay.getFeasible()>0)
					{
						if((tGateWay.getDistance()>=min) && (tGateWay.getDistance()<=max))
						{
							tDistanceGateWaySet.add(tGateWay);

						}
					}
				}
				
				if(tDistanceGateWaySet.size()>2)
				{
					distanceFlag=false;
				}
			
				
				min=min-deltaRange;
				if(min <0)
				{
					min=0;
				}
				max=max+deltaRange;
				double maxRange=Math.sqrt(Math.pow(ExperimentSetting.xRange, 2)+Math.pow(ExperimentSetting.yRange, 2));
				if(max >maxRange)
				{
					max=maxRange;
				}
				if((min==0) && (max==maxRange))
				{
					distanceFlag=false;
				}
				
			}
			
			if(tDistanceGateWaySet.size()==0)
			{
				flag=false;
				return result;
			}
			
			/*
			 * 
			 */
			
						
			
			
			/*
			 * Step 2---timestamp
			 */
			boolean timeStampFlag=true;
			double tTimeStamp=iniTimeStamp;
			while(timeStampFlag)
			{

				for(int i=0;i<tDistanceGateWaySet.size();i++)
				{
					GateWay tGateWay=tDistanceGateWaySet.get(i);
					if(tGateWay.getTimeStamp()<tUsedTime-tTimeStamp)
					{
						tGateWaySet.add(tGateWay);
						tGateWay.calcPriorityWeight(tUsedTime);
					}					
				}
				if(tGateWaySet.size()>0)
				{
					timeStampFlag=false;
				}
				tTimeStamp=tTimeStamp-deltaTimeStamp;
				if(tTimeStamp<=1.5)
				{
					timeStampFlag=false;
				}
				
			}
			
			if(tGateWaySet.size()==0)
			{
				flag=false;
				
				
				for(int i=0;i<nodeSet.size();i++)
				{
					totalUtility=totalUtility+nodeSet.get(i).getTotalUtility();
				}
				
				result.setTourTime(ExperimentSetting.tourTime);
				result.setNetworkSize(bNet.getnList().size());
				result.setTotalMovingTime(totalMovingTime);
				result.setTotalSojournTime(totalSojournTime);
				result.setTotalUtility(totalUtility);
				return result;
			}
			/*
			 * 
			 */
			
			Object[] gSet=tGateWaySet.toArray();
			GateWayPriorityWeightComparator gCom=new GateWayPriorityWeightComparator(true);
			Arrays.sort(gSet,gCom);
			GateWay realGateWay=(GateWay)gSet[0];
			
			realGateWay.calcUtilityGain(lastBackTime, tTourTime, false);
			if(realGateWay.getSojournTime()>(tTourTime-realGateWay.getMovingTime()-realGateWay.getBackTime()))
			{
				realGateWay.calcUtilityGain(lastBackTime,tTourTime, true);   //last sojourn location
				flag=false;
			}
			
//			/*
//			 * begin of debug
//			 */
//			System.out.println(realGateWay);
//			
//			/*
//			 * end of debug
//			 */
			
			realGateWay.setTimeStamp(tUsedTime+realGateWay.getMovingTime()+realGateWay.getSojournTime());
			
			GateWay chosenGateWay=new GateWay(realGateWay);
			
			solution.add(chosenGateWay);
			tMovingTime=chosenGateWay.getMovingTime();
			tSojournTime=chosenGateWay.getSojournTime();
			tTourTime=tTourTime-tMovingTime-tSojournTime;
			tUsedTime=tUsedTime+tMovingTime+tSojournTime;
			lastBackTime=chosenGateWay.getBackTime();
			
			
			totalMovingTime=totalMovingTime+tMovingTime;
			totalSojournTime=totalSojournTime+tSojournTime;
			
			
			/*
			 *  move to chosen sojourn location to collect data
			 *  then update both mobilesink, and node status
			 *  including:
			 *  mobilesink(X,Y)
			 *  
			 *  node(rEnergy, rData, hEnergy, and fWeight)  
			 */
			tSinkX=chosenGateWay.getX();
			tSinkY=chosenGateWay.getY();
			
			//update all nodes
			for(int i=0;i<nodeSet.size();i++)
			{
				Node tNode=nodeSet.get(i);
				double tREnergy=tNode.getaEnergy()+tNode.gethEnergy()*(tMovingTime+tSojournTime);
				if(tREnergy>tNode.getcEnergy())
				{
					tREnergy=tNode.getcEnergy();
				}
				tNode.setaEnergy(tREnergy);
				
//				//begin of debug
//				if(tREnergy<0)
//				{
//					System.out.println(tREnergy);
//				}
//				//end of debug
				
				double tRData=tNode.getrData()+tNode.getgRate()*(tMovingTime+tSojournTime);
				tNode.setrData(tRData);
				
//				//begin of debug
//				if(tRData<0)
//				{
//					System.out.println(tRData);
//				}
//				//end of debug

				
			}
			
			
			//only update neighboring nodes
			for(int i=0;i<chosenGateWay.getNeighborNodes().size();i++)
			{
				Node tNode=chosenGateWay.getNeighborNodes().get(i);
				double eCom=chosenGateWay.geteConSet().get(i);
				
				
				//compare uploadtime and sojourtime
                double tUploadTime=tNode.getaEnergy()/(tNode.gettRate()*eCom);
                double tDataTime=tNode.getrData()/tNode.gettRate();
                if(tUploadTime>tDataTime)
                {
                	tUploadTime=tDataTime;
                }
                
                
                //
                
                
                if(tUploadTime>tSojournTime)
                {
                	tUploadTime=tSojournTime;
                }
                
					double tRData=tNode.getrData()-tNode.gettRate()*tUploadTime;
					
					//begin of debug
					if(tRData<0)
					{
						//System.out.println(tRData);
						tRData=0;
					}
					//end of debug
					
					tNode.setrData(tRData);
					
					

					
					
					double tREnergy=tNode.getaEnergy()-tNode.gettRate()*tUploadTime*eCom;
					
					//begin of debug
					if(tREnergy<0)
					{
						//System.out.println(tREnergy);
						tREnergy=0;
					}
					//end of debug
					
					tNode.setaEnergy(tREnergy);
					

					
					double tTotalSojournTime=tNode.getTotalSojournTime()+tUploadTime;
					tNode.setTotalSojournTime(tTotalSojournTime);
				
			}
			
			
			if(tTourTime <=0)
			{
				flag=false;
			}
			
		}
		
		for(int i=0;i<nodeSet.size();i++)
		{
			totalUtility=totalUtility+nodeSet.get(i).getTotalUtility();
		}
		
		result.setTourTime(ExperimentSetting.tourTime);
		result.setNetworkSize(bNet.getnList().size());
		result.setTotalMovingTime(totalMovingTime);
		result.setTotalSojournTime(totalSojournTime);
		result.setTotalUtility(totalUtility);
		return result;
	}
	
	
	/*
	 *  go to the longest location first
	 *  unfinished!!!
	 */
	public static LabResult dis2TraMaxUtilityGainTourDesign(BiNetwork bNet, double iniMinRange, double iniMaxRange, double deltaRange, double iniTimeStamp, double deltaTimeStamp, ArrayList<GateWay> solution) throws IOException
	{

		ArrayList<Node> nodeSet = bNet.getnList(); //
		ArrayList<GateWay> gatewaySet = bNet.getgList(); //
		
		//initial 
		LabResult result=new LabResult();
		double totalUtility=0;
		double totalSojournTime=0;
		double totalMovingTime=0;
		
		
		
		boolean flag=true;
		double tTourTime=ExperimentSetting.tourTime;
		double tUsedTime=0;
		double tMovingTime=0;
		double tSojournTime=0;
		
		double tSinkX=ExperimentSetting.initSinkX;
		double tSinkY=ExperimentSetting.initSinkY;
		
		double tSinkSpeed=ExperimentSetting.mSpeed;
		double lastBackTime=0;
		
		
		for(int i=0;i<gatewaySet.size();i++)
		{
			GateWay tGateWay=gatewaySet.get(i);
			tGateWay.setTimeStamp(tUsedTime-iniTimeStamp-deltaTimeStamp);
		}
		
		
		/*
		 * begin of go to longest location
		 */
		
		ArrayList<GateWay> longGateWaySet=new ArrayList<GateWay>();
		for(int i=0;i<gatewaySet.size();i++)
		{
			GateWay tGateWay=gatewaySet.get(i);
			double tX=tSinkX-tGateWay.getX();
			double tY=tSinkY-tGateWay.getY();
			double tD=Math.sqrt(tX*tX+tY*tY);
			tGateWay.setDistance(tD);
			tGateWay.setMovingTime(tD/tSinkSpeed);  //calculate moving time
			
			tX=ExperimentSetting.initSinkX-tGateWay.getX();
			tY=ExperimentSetting.initSinkY-tGateWay.getY();
			tD=Math.sqrt(tX*tX+tY*tY);
			tGateWay.setBackTime(tD/tSinkSpeed);
			
			
			if((tGateWay.getMovingTime()+tGateWay.getBackTime())<tTourTime)
			{
				longGateWaySet.add(tGateWay);
				tGateWay.calcPriorityWeight(tUsedTime);
			}
			
	      }
		
		Object[] longSet=longGateWaySet.toArray();
		GateWayPriorityWeightComparator longCom=new GateWayPriorityWeightComparator(false);
		Arrays.sort(longSet,longCom);
		GateWay longGateWay=(GateWay)longSet[0];
		
		longGateWay.calcUtilityGain(lastBackTime, tTourTime, false);
		if(longGateWay.getSojournTime()>(tTourTime-longGateWay.getMovingTime()-longGateWay.getBackTime()))
		{
			longGateWay.calcUtilityGain(lastBackTime,tTourTime, true);   //last sojourn location
			flag=false;
		}
		
//		/*
//		 * begin of debug
//		 */
//		System.out.println(realGateWay);
//		
//		/*
//		 * end of debug
//		 */
		
		longGateWay.setTimeStamp(tUsedTime+longGateWay.getMovingTime()+longGateWay.getSojournTime());
		
		GateWay chosenLongGateWay=new GateWay(longGateWay);
		
		solution.add(chosenLongGateWay);
		tMovingTime=chosenLongGateWay.getMovingTime();
		tSojournTime=chosenLongGateWay.getSojournTime();
		tTourTime=tTourTime-tMovingTime-tSojournTime;
		tUsedTime=tUsedTime+tMovingTime+tSojournTime;
		lastBackTime=chosenLongGateWay.getBackTime();
		
		
		totalMovingTime=totalMovingTime+tMovingTime;
		totalSojournTime=totalSojournTime+tSojournTime;
		
		
		/*
		 *  move to chosen sojourn location to collect data
		 *  then update both mobilesink, and node status
		 *  including:
		 *  mobilesink(X,Y)
		 *  
		 *  node(rEnergy, rData, hEnergy, and fWeight)  
		 */
		tSinkX=chosenLongGateWay.getX();
		tSinkY=chosenLongGateWay.getY();
		
		//update all nodes
		for(int i=0;i<nodeSet.size();i++)
		{
			Node tNode=nodeSet.get(i);
			double tREnergy=tNode.getaEnergy()+tNode.gethEnergy()*(tMovingTime+tSojournTime);
			if(tREnergy>tNode.getcEnergy())
			{
				tREnergy=tNode.getcEnergy();
			}
			tNode.setaEnergy(tREnergy);
			
//			//begin of debug
//			if(tREnergy<0)
//			{
//				System.out.println(tREnergy);
//			}
//			//end of debug
			
			double tRData=tNode.getrData()+tNode.getgRate()*(tMovingTime+tSojournTime);
			tNode.setrData(tRData);
			
//			//begin of debug
//			if(tRData<0)
//			{
//				System.out.println(tRData);
//			}
//			//end of debug

			
		}
		
		
		//only update neighboring nodes
		for(int i=0;i<chosenLongGateWay.getNeighborNodes().size();i++)
		{
			Node tNode=chosenLongGateWay.getNeighborNodes().get(i);
			double eCom=chosenLongGateWay.geteConSet().get(i);
			
			
			//compare uploadtime and sojourtime
            double tUploadTime=tNode.getaEnergy()/(tNode.gettRate()*eCom);
            double tDataTime=tNode.getrData()/tNode.gettRate();
            if(tUploadTime>tDataTime)
            {
            	tUploadTime=tDataTime;
            }
            
            
            //
            
            
            if(tUploadTime>tSojournTime)
            {
            	tUploadTime=tSojournTime;
            }
            
				double tRData=tNode.getrData()-tNode.gettRate()*tUploadTime;
				
				//begin of debug
				if(tRData<0)
				{
					//System.out.println(tRData);
					tRData=0;
				}
				//end of debug
				
				tNode.setrData(tRData);
				
				

				
				
				double tREnergy=tNode.getaEnergy()-tNode.gettRate()*tUploadTime*eCom;
				
				//begin of debug
				if(tREnergy<0)
				{
					//System.out.println(tREnergy);
					tREnergy=0;
				}
				//end of debug
				
				tNode.setaEnergy(tREnergy);
				

				
				double tTotalSojournTime=tNode.getTotalSojournTime()+tUploadTime;
				tNode.setTotalSojournTime(tTotalSojournTime);
			
		}
		
		
		if(tTourTime <=0)
		{
			flag=false;
		}
		
		/*
		 * end of go to longest location
		 */
		
		while(flag)
		{
			ArrayList<GateWay> tGateWaySet=new ArrayList<GateWay>();
			ArrayList<GateWay> tDistanceGateWaySet=new ArrayList<GateWay>();

			
			for(int i=0;i<gatewaySet.size();i++)
			{
				GateWay tGateWay=gatewaySet.get(i);
				double tX=tSinkX-tGateWay.getX();
				double tY=tSinkY-tGateWay.getY();
				double tD=Math.sqrt(tX*tX+tY*tY);
				tGateWay.setDistance(tD);
				tGateWay.setMovingTime(tD/tSinkSpeed);  //calculate moving time
				
				tX=ExperimentSetting.initSinkX-tGateWay.getX();
				tY=ExperimentSetting.initSinkY-tGateWay.getY();
				tD=Math.sqrt(tX*tX+tY*tY);
				tGateWay.setBackTime(tD/tSinkSpeed);
				
				
				/*
				 * first select 
				 */
				if((tGateWay.getMovingTime()+tGateWay.getBackTime())>tTourTime)
				{
					tGateWay.setFeasible(0);
				}
				else
				{
					tGateWay.setFeasible(1);
				}
																												
			}
			
			/*
			 * Step1----distance
			 */
			double min=iniMinRange;                    //1.5R
			double max=iniMaxRange;                      //3R
			boolean distanceFlag=true;
			while(distanceFlag)
			{	
				for(int i=0;i<gatewaySet.size();i++)
				{
					GateWay tGateWay=gatewaySet.get(i);
					if(tGateWay.getFeasible()>0)
					{
						if((tGateWay.getDistance()>=min) && (tGateWay.getDistance()<=max))
						{
							tDistanceGateWaySet.add(tGateWay);

						}
					}
				}
				
				if(tDistanceGateWaySet.size()>2)
				{
					distanceFlag=false;
				}
			
				
				min=min-deltaRange;
				if(min <0)
				{
					min=0;
				}
				max=max+deltaRange;
				double maxRange=Math.sqrt(Math.pow(ExperimentSetting.xRange, 2)+Math.pow(ExperimentSetting.yRange, 2));
				if(max >maxRange)
				{
					max=maxRange;
				}
				if((min==0) && (max==maxRange))
				{
					distanceFlag=false;
				}
				
			}
			
			if(tDistanceGateWaySet.size()==0)
			{
				flag=false;
				return result;
			}
			
			/*
			 * 
			 */
			
						
			
			
			/*
			 * Step 2---timestamp
			 */
			boolean timeStampFlag=true;
			double tTimeStamp=iniTimeStamp;
			while(timeStampFlag)
			{

				for(int i=0;i<tDistanceGateWaySet.size();i++)
				{
					GateWay tGateWay=tDistanceGateWaySet.get(i);
					if(tGateWay.getTimeStamp()<tUsedTime-tTimeStamp)
					{
						tGateWaySet.add(tGateWay);
						tGateWay.calcPriorityWeight(tUsedTime);
					}					
				}
				if(tGateWaySet.size()>0)
				{
					timeStampFlag=false;
				}
				tTimeStamp=tTimeStamp-deltaTimeStamp;
				if(tTimeStamp<=1.5)
				{
					timeStampFlag=false;
				}
				
			}
			
			if(tGateWaySet.size()==0)
			{
				flag=false;
				
				
				for(int i=0;i<nodeSet.size();i++)
				{
					totalUtility=totalUtility+nodeSet.get(i).getTotalUtility();
				}
				
				result.setTourTime(ExperimentSetting.tourTime);
				result.setNetworkSize(bNet.getnList().size());
				result.setTotalMovingTime(totalMovingTime);
				result.setTotalSojournTime(totalSojournTime);
				result.setTotalUtility(totalUtility);
				return result;
			}
			/*
			 * 
			 */
			
			Object[] gSet=tGateWaySet.toArray();
			GateWayPriorityWeightComparator gCom=new GateWayPriorityWeightComparator(true);
			Arrays.sort(gSet,gCom);
			GateWay realGateWay=(GateWay)gSet[0];
			
			realGateWay.calcUtilityGain(lastBackTime, tTourTime, false);
			if(realGateWay.getSojournTime()>(tTourTime-realGateWay.getMovingTime()-realGateWay.getBackTime()))
			{
				realGateWay.calcUtilityGain(lastBackTime,tTourTime, true);   //last sojourn location
				flag=false;
			}
			
//			/*
//			 * begin of debug
//			 */
//			System.out.println(realGateWay);
//			
//			/*
//			 * end of debug
//			 */
			
			realGateWay.setTimeStamp(tUsedTime+realGateWay.getMovingTime()+realGateWay.getSojournTime());
			
			GateWay chosenGateWay=new GateWay(realGateWay);
			
			solution.add(chosenGateWay);
			tMovingTime=chosenGateWay.getMovingTime();
			tSojournTime=chosenGateWay.getSojournTime();
			tTourTime=tTourTime-tMovingTime-tSojournTime;
			tUsedTime=tUsedTime+tMovingTime+tSojournTime;
			lastBackTime=chosenGateWay.getBackTime();
			
			
			totalMovingTime=totalMovingTime+tMovingTime;
			totalSojournTime=totalSojournTime+tSojournTime;
			
			
			/*
			 *  move to chosen sojourn location to collect data
			 *  then update both mobilesink, and node status
			 *  including:
			 *  mobilesink(X,Y)
			 *  
			 *  node(rEnergy, rData, hEnergy, and fWeight)  
			 */
			tSinkX=chosenGateWay.getX();
			tSinkY=chosenGateWay.getY();
			
			//update all nodes
			for(int i=0;i<nodeSet.size();i++)
			{
				Node tNode=nodeSet.get(i);
				double tREnergy=tNode.getaEnergy()+tNode.gethEnergy()*(tMovingTime+tSojournTime);
				if(tREnergy>tNode.getcEnergy())
				{
					tREnergy=tNode.getcEnergy();
				}
				tNode.setaEnergy(tREnergy);
				
//				//begin of debug
//				if(tREnergy<0)
//				{
//					System.out.println(tREnergy);
//				}
//				//end of debug
				
				double tRData=tNode.getrData()+tNode.getgRate()*(tMovingTime+tSojournTime);
				tNode.setrData(tRData);
				
//				//begin of debug
//				if(tRData<0)
//				{
//					System.out.println(tRData);
//				}
//				//end of debug

				
			}
			
			
			//only update neighboring nodes
			for(int i=0;i<chosenGateWay.getNeighborNodes().size();i++)
			{
				Node tNode=chosenGateWay.getNeighborNodes().get(i);
				double eCom=chosenGateWay.geteConSet().get(i);
				
				
				//compare uploadtime and sojourtime
                double tUploadTime=tNode.getaEnergy()/(tNode.gettRate()*eCom);
                double tDataTime=tNode.getrData()/tNode.gettRate();
                if(tUploadTime>tDataTime)
                {
                	tUploadTime=tDataTime;
                }
                
                
                //
                
                
                if(tUploadTime>tSojournTime)
                {
                	tUploadTime=tSojournTime;
                }
                
					double tRData=tNode.getrData()-tNode.gettRate()*tUploadTime;
					
					//begin of debug
					if(tRData<0)
					{
						//System.out.println(tRData);
						tRData=0;
					}
					//end of debug
					
					tNode.setrData(tRData);
					
					

					
					
					double tREnergy=tNode.getaEnergy()-tNode.gettRate()*tUploadTime*eCom;
					
					//begin of debug
					if(tREnergy<0)
					{
						//System.out.println(tREnergy);
						tREnergy=0;
					}
					//end of debug
					
					tNode.setaEnergy(tREnergy);
					

					
					double tTotalSojournTime=tNode.getTotalSojournTime()+tUploadTime;
					tNode.setTotalSojournTime(tTotalSojournTime);
				
			}
			
			
			if(tTourTime <=0)
			{
				flag=false;
			}
			
		}
		
		for(int i=0;i<nodeSet.size();i++)
		{
			totalUtility=totalUtility+nodeSet.get(i).getTotalUtility();
		}
		
		result.setTourTime(ExperimentSetting.tourTime);
		result.setNetworkSize(bNet.getnList().size());
		result.setTotalMovingTime(totalMovingTime);
		result.setTotalSojournTime(totalSojournTime);
		result.setTotalUtility(totalUtility);
		return result;
	}
	
	
	public static LabResult randomUnitUtilityGainTourDesign(BiNetwork bNet,ArrayList<GateWay> solution) throws IOException
	{

		ArrayList<Node> nodeSet = bNet.getnList(); //
		ArrayList<GateWay> gatewaySet = bNet.getgList(); //
		
		
		//initial 
		LabResult result=new LabResult();
		double totalUtility=0;
		double totalSojournTime=0;
		double totalMovingTime=0;
		
		
		boolean flag=true;
		double tTourTime=ExperimentSetting.tourTime;

		
		double tMovingTime=0;
		double tSojournTime=0;
		
		double tSinkX=ExperimentSetting.initSinkX;
		double tSinkY=ExperimentSetting.initSinkY;
		
		double tSinkSpeed=ExperimentSetting.mSpeed;
		double lastBackTime=0;
		
		
		while(flag)
		{
			ArrayList<GateWay> tGateWaySet=new ArrayList<GateWay>();
			for(int i=0;i<gatewaySet.size();i++)
			{
				GateWay tGateWay=gatewaySet.get(i);
				double tX=tSinkX-tGateWay.getX();
				double tY=tSinkY-tGateWay.getY();
				double tD=Math.sqrt(tX*tX+tY*tY);
				tGateWay.setMovingTime(tD/tSinkSpeed);  //calculate moving time
				
				tX=ExperimentSetting.initSinkX-tGateWay.getX();
				tY=ExperimentSetting.initSinkY-tGateWay.getY();
				tD=Math.sqrt(tX*tX+tY*tY);
				tGateWay.setBackTime(tD/tSinkSpeed);
				
				if((tTourTime-tGateWay.getMovingTime()-tGateWay.getBackTime())>0)
					tGateWaySet.add(tGateWay);
			}
			

			
			
			if(tGateWaySet.size()==0)
			{
				flag=false;
				
				for(int i=0;i<nodeSet.size();i++)
				{
					totalUtility=totalUtility+nodeSet.get(i).getTotalUtility();
				}
				
				result.setTourTime(ExperimentSetting.tourTime);
				result.setNetworkSize(bNet.getnList().size());
				result.setTotalMovingTime(totalMovingTime);
				result.setTotalSojournTime(totalSojournTime);
				result.setTotalUtility(totalUtility);
				return result;
			}
			
			if(tGateWaySet.size()==1)
			{
				flag=false;
			}
			Object[] gSet=tGateWaySet.toArray();

			GateWay chosenGateWay=new GateWay((GateWay)gSet[ExperimentSetting.ran.nextInt(gSet.length)]);
			chosenGateWay.calcUnitUtilityGain(lastBackTime, tTourTime, false);
			if(chosenGateWay.getSojournTime()>(tTourTime-chosenGateWay.getMovingTime()-chosenGateWay.getBackTime()))
			{
				flag=false;
				chosenGateWay.calcUnitUtilityGain(lastBackTime, tTourTime, true);
			}
			solution.add(chosenGateWay);
			
			tMovingTime=chosenGateWay.getMovingTime();
			tSojournTime=chosenGateWay.getSojournTime();
			tTourTime=tTourTime-tMovingTime-tSojournTime;
			lastBackTime=chosenGateWay.getBackTime();
			
			totalMovingTime=totalMovingTime+tMovingTime;
			totalSojournTime=totalSojournTime+tSojournTime;
			
			
			/*
			 *  move to chosen sojourn location to collect data
			 *  then update both mobilesink, and node status
			 *  including:
			 *  mobilesink(X,Y)
			 *  
			 *  node(rEnergy, rData, hEnergy, and fWeight)  
			 */
			tSinkX=chosenGateWay.getX();
			tSinkY=chosenGateWay.getY();
			
			//update all nodes
			for(int i=0;i<nodeSet.size();i++)
			{
				Node tNode=nodeSet.get(i);
				double tREnergy=tNode.getaEnergy()+tNode.gethEnergy()*(tMovingTime+tSojournTime);
				if(tREnergy>tNode.getcEnergy())
				{
					tREnergy=tNode.getcEnergy();
				}
				tNode.setaEnergy(tREnergy);
				
//				//begin of debug
//				if(tREnergy<0)
//				{
//					System.out.println(tREnergy);
//				}
//				//end of debug
				
				double tRData=tNode.getrData()+tNode.getgRate()*(tMovingTime+tSojournTime);
				tNode.setrData(tRData);
				
//				//begin of debug
//				if(tRData<0)
//				{
//					System.out.println(tRData);
//				}
//				//end of debug

				
			}
			
			
			//only update neighboring nodes
			for(int i=0;i<chosenGateWay.getNeighborNodes().size();i++)
			{
				Node tNode=chosenGateWay.getNeighborNodes().get(i);
				double eCom=chosenGateWay.geteConSet().get(i);
				
				
				//compare uploadtime and sojourtime
                double tUploadTime=tNode.getaEnergy()/(tNode.gettRate()*eCom);
                double tDataTime=tNode.getrData()/tNode.gettRate();
                if(tUploadTime>tDataTime)
                {
                	tUploadTime=tDataTime;
                }
                
                
                //
                
                
                if(tUploadTime>tSojournTime)
                {
                	tUploadTime=tSojournTime;
                }
                
					double tRData=tNode.getrData()-tNode.gettRate()*tUploadTime;
					
					//begin of debug
					if(tRData<0)
					{
						//System.out.println(tRData);
						tRData=0;
					}
					//end of debug
					
					tNode.setrData(tRData);
					
					

					
					
					double tREnergy=tNode.getaEnergy()-tNode.gettRate()*tUploadTime*eCom;
					
					//begin of debug
					if(tREnergy<0)
					{
						//System.out.println(tREnergy);
						tREnergy=0;
					}
					//end of debug
					
					tNode.setaEnergy(tREnergy);
					

					
					double tTotalSojournTime=tNode.getTotalSojournTime()+tUploadTime;
					tNode.setTotalSojournTime(tTotalSojournTime);
				
			}
			
			
			if(tTourTime <ExperimentSetting.minSojournTime)
			{
				flag=false;
			}
			
		}
		
		
		for(int i=0;i<nodeSet.size();i++)
		{
			totalUtility=totalUtility+nodeSet.get(i).getTotalUtility();
		}
		
		result.setTourTime(ExperimentSetting.tourTime);
		result.setNetworkSize(bNet.getnList().size());
		result.setTotalMovingTime(totalMovingTime);
		result.setTotalSojournTime(totalSojournTime);
		result.setTotalUtility(totalUtility);
		return result;
	}
	
	
	
	
	public static LabResult randomUtilityGainTourDesign(BiNetwork bNet,ArrayList<GateWay> solution) throws IOException
	{

		ArrayList<Node> nodeSet = bNet.getnList(); //
		ArrayList<GateWay> gatewaySet = bNet.getgList(); //
		
		
		//initial 
		LabResult result=new LabResult();
		double totalUtility=0;
		double totalSojournTime=0;
		double totalMovingTime=0;
		
		
		boolean flag=true;
		double tTourTime=ExperimentSetting.tourTime;

		
		double tMovingTime=0;
		double tSojournTime=0;
		
		double tSinkX=ExperimentSetting.initSinkX;
		double tSinkY=ExperimentSetting.initSinkY;
		
		double tSinkSpeed=ExperimentSetting.mSpeed;
		double lastBackTime=0;
		
		
		while(flag)
		{
			ArrayList<GateWay> tGateWaySet=new ArrayList<GateWay>();
			for(int i=0;i<gatewaySet.size();i++)
			{
				GateWay tGateWay=gatewaySet.get(i);
				double tX=tSinkX-tGateWay.getX();
				double tY=tSinkY-tGateWay.getY();
				double tD=Math.sqrt(tX*tX+tY*tY);
				tGateWay.setMovingTime(tD/tSinkSpeed);  //calculate moving time
				
				tX=ExperimentSetting.initSinkX-tGateWay.getX();
				tY=ExperimentSetting.initSinkY-tGateWay.getY();
				tD=Math.sqrt(tX*tX+tY*tY);
				tGateWay.setBackTime(tD/tSinkSpeed);
				
				if((tTourTime-tGateWay.getMovingTime()-tGateWay.getBackTime())>0)
					tGateWaySet.add(tGateWay);
			}
			

			
			
			if(tGateWaySet.size()==0)
			{
				flag=false;
				
				for(int i=0;i<nodeSet.size();i++)
				{
					totalUtility=totalUtility+nodeSet.get(i).getTotalUtility();
				}
				
				result.setTourTime(ExperimentSetting.tourTime);
				result.setNetworkSize(bNet.getnList().size());
				result.setTotalMovingTime(totalMovingTime);
				result.setTotalSojournTime(totalSojournTime);
				result.setTotalUtility(totalUtility);
				return result;
			}
			
			if(tGateWaySet.size()==1)
			{
				flag=false;
			}
			Object[] gSet=tGateWaySet.toArray();

			GateWay chosenGateWay=new GateWay((GateWay)gSet[ExperimentSetting.ran.nextInt(gSet.length)]);
			chosenGateWay.calcUtilityGain(lastBackTime, tTourTime, false);
			if(chosenGateWay.getSojournTime()>(tTourTime-chosenGateWay.getMovingTime()-chosenGateWay.getBackTime()))
			{
				flag=false;
				chosenGateWay.calcUtilityGain(lastBackTime, tTourTime, true);
			}
			solution.add(chosenGateWay);
			
			tMovingTime=chosenGateWay.getMovingTime();
			tSojournTime=chosenGateWay.getSojournTime();
			tTourTime=tTourTime-tMovingTime-tSojournTime;
			lastBackTime=chosenGateWay.getBackTime();
			
			totalMovingTime=totalMovingTime+tMovingTime;
			totalSojournTime=totalSojournTime+tSojournTime;
			
			
			/*
			 *  move to chosen sojourn location to collect data
			 *  then update both mobilesink, and node status
			 *  including:
			 *  mobilesink(X,Y)
			 *  
			 *  node(rEnergy, rData, hEnergy, and fWeight)  
			 */
			tSinkX=chosenGateWay.getX();
			tSinkY=chosenGateWay.getY();
			
			//update all nodes
			for(int i=0;i<nodeSet.size();i++)
			{
				Node tNode=nodeSet.get(i);
				double tREnergy=tNode.getaEnergy()+tNode.gethEnergy()*(tMovingTime+tSojournTime);
				if(tREnergy>tNode.getcEnergy())
				{
					tREnergy=tNode.getcEnergy();
				}
				tNode.setaEnergy(tREnergy);
				
//				//begin of debug
//				if(tREnergy<0)
//				{
//					System.out.println(tREnergy);
//				}
//				//end of debug
				
				double tRData=tNode.getrData()+tNode.getgRate()*(tMovingTime+tSojournTime);
				tNode.setrData(tRData);
				
//				//begin of debug
//				if(tRData<0)
//				{
//					System.out.println(tRData);
//				}
//				//end of debug

				
			}
			
			
			//only update neighboring nodes
			for(int i=0;i<chosenGateWay.getNeighborNodes().size();i++)
			{
				Node tNode=chosenGateWay.getNeighborNodes().get(i);
				double eCom=chosenGateWay.geteConSet().get(i);
				
				
				//compare uploadtime and sojourtime
                double tUploadTime=tNode.getaEnergy()/(tNode.gettRate()*eCom);
                double tDataTime=tNode.getrData()/tNode.gettRate();
                if(tUploadTime>tDataTime)
                {
                	tUploadTime=tDataTime;
                }
                
                
                //
                
                
                if(tUploadTime>tSojournTime)
                {
                	tUploadTime=tSojournTime;
                }
                
					double tRData=tNode.getrData()-tNode.gettRate()*tUploadTime;
					
					//begin of debug
					if(tRData<0)
					{
						//System.out.println(tRData);
						tRData=0;
					}
					//end of debug
					
					tNode.setrData(tRData);
					
					

					
					
					double tREnergy=tNode.getaEnergy()-tNode.gettRate()*tUploadTime*eCom;
					
					//begin of debug
					if(tREnergy<0)
					{
						//System.out.println(tREnergy);
						tREnergy=0;
					}
					//end of debug
					
					tNode.setaEnergy(tREnergy);
					

					
					double tTotalSojournTime=tNode.getTotalSojournTime()+tUploadTime;
					tNode.setTotalSojournTime(tTotalSojournTime);
				
			}
			
			
			if(tTourTime <ExperimentSetting.minSojournTime)
			{
				flag=false;
			}
			
		}
		
		
		for(int i=0;i<nodeSet.size();i++)
		{
			totalUtility=totalUtility+nodeSet.get(i).getTotalUtility();
		}
		
		result.setTourTime(ExperimentSetting.tourTime);
		result.setNetworkSize(bNet.getnList().size());
		result.setTotalMovingTime(totalMovingTime);
		result.setTotalSojournTime(totalSojournTime);
		result.setTotalUtility(totalUtility);
		return result;
	}
	
	
	
	
	
	/*
	 * max utilitygain  for each gateway !!!!not unitutilitygain
	 */
	public static LabResult maxUtilityGainTourDesign(BiNetwork bNet,ArrayList<GateWay> solution) throws IOException
	{
		
		ArrayList<Node> nodeSet = bNet.getnList(); //
		ArrayList<GateWay> gatewaySet = bNet.getgList(); //
		
//		//begin debug
//		for(int i=0;i<nodeSet.size();i++)
//		{
//			Node t=nodeSet.get(i);
//			System.out.println("node:"+t.getId()+"--rData("+t.getrData()+")--rEnergy("+t.getaEnergy()+")--hEnergy("+t.gethEnergy());
//		}
//		//end debug
//		if(gNet!=null)
//		{
//			gNet=bNet;
//		}
		
		//initial 
		LabResult result=new LabResult();
		double totalUtility=0;
		double totalSojournTime=0;
		double totalMovingTime=0;
		
		
		
		
		
		

		boolean flag=true;
		double tTourTime=ExperimentSetting.tourTime;

		
		double tMovingTime=0;
		double tSojournTime=0;
		
		double tSinkX=ExperimentSetting.initSinkX;
		double tSinkY=ExperimentSetting.initSinkY;
		
		double tSinkSpeed=ExperimentSetting.mSpeed;
		double lastBackTime=0;
		
		
		
		
		
		while(flag)
		{
			ArrayList<GateWay> tGateWaySet=new ArrayList<GateWay>();
			for(int i=0;i<gatewaySet.size();i++)
			{
				GateWay tGateWay=gatewaySet.get(i);
				double tX=tSinkX-tGateWay.getX();
				double tY=tSinkY-tGateWay.getY();
				double tD=Math.sqrt(tX*tX+tY*tY);
				tGateWay.setMovingTime(tD/tSinkSpeed);  //calculate moving time
				
				tX=ExperimentSetting.initSinkX-tGateWay.getX();
				tY=ExperimentSetting.initSinkY-tGateWay.getY();
				tD=Math.sqrt(tX*tX+tY*tY);
				tGateWay.setBackTime(tD/tSinkSpeed);
				
				tGateWay.calcUtilityGain(lastBackTime,tTourTime,false);
				if(tGateWay.getFeasible()>0)
					tGateWaySet.add(tGateWay);
			}
			
			if(tGateWaySet.size()==0)
			{
				flag=false;
				for(int i=0;i<gatewaySet.size();i++)
				{
					GateWay tGateWay=gatewaySet.get(i);
					tGateWay.calcUtilityGain(lastBackTime,tTourTime,true);     //last  sojourn location
					if(tGateWay.getFeasible()>0)
						tGateWaySet.add(tGateWay);
				}
			}
			
			
			if(tGateWaySet.size()==0)
			{
				flag=false;
				
				for(int i=0;i<nodeSet.size();i++)
				{
					Node tNode=nodeSet.get(i);
					totalUtility=totalUtility+tNode.getTotalUtility();
				}
				
				result.setTourTime(ExperimentSetting.tourTime);
				result.setNetworkSize(bNet.getnList().size());
				result.setTotalMovingTime(totalMovingTime);
				result.setTotalSojournTime(totalSojournTime);
				result.setTotalUtility(totalUtility);
				return result;
			}
			
			Object[] gSet=tGateWaySet.toArray();
			GateWayUnitUtilityGainComparator gCom=new GateWayUnitUtilityGainComparator(false);
			Arrays.sort(gSet,gCom);
			GateWay chosenGateWay=new GateWay((GateWay)gSet[0]);
			
			solution.add(chosenGateWay);
			tMovingTime=chosenGateWay.getMovingTime();
			tSojournTime=chosenGateWay.getSojournTime();
			tTourTime=tTourTime-tMovingTime-tSojournTime;
			lastBackTime=chosenGateWay.getBackTime();
			
			
			totalMovingTime=totalMovingTime+tMovingTime;
			totalSojournTime=totalSojournTime+tSojournTime;
			/*
			 *  move to chosen sojourn location to collect data
			 *  then update both mobilesink, and node status
			 *  including:
			 *  mobilesink(X,Y)
			 *  
			 *  node(rEnergy, rData, hEnergy, and fWeight)  
			 */
			tSinkX=chosenGateWay.getX();
			tSinkY=chosenGateWay.getY();
			
			
			
			//update all nodes
			for(int i=0;i<nodeSet.size();i++)
			{
				Node tNode=nodeSet.get(i);
				double tREnergy=tNode.getaEnergy()+tNode.gethEnergy()*(tMovingTime+tSojournTime);
				if(tREnergy>tNode.getcEnergy())
				{
					tREnergy=tNode.getcEnergy();
				}
				tNode.setaEnergy(tREnergy);
				
//				//begin of debug
//				if(tREnergy<0)
//				{
//					System.out.println(tREnergy);
//				}
//				//end of debug
				
				double tRData=tNode.getrData()+tNode.getgRate()*(tMovingTime+tSojournTime);
				tNode.setrData(tRData);
				
//				//begin of debug
//				if(tRData<0)
//				{
//					System.out.println(tRData);
//				}
//				//end of debug

				
			}
			
			
			//only update neighboring nodes
			for(int i=0;i<chosenGateWay.getNeighborNodes().size();i++)
			{
				Node tNode=chosenGateWay.getNeighborNodes().get(i);
				double eCom=chosenGateWay.geteConSet().get(i);
				
				
				//compare uploadtime and sojourtime
                double tUploadTime=tNode.getaEnergy()/(tNode.gettRate()*eCom);
                double tDataTime=tNode.getrData()/tNode.gettRate();
                if(tUploadTime>tDataTime)
                {
                	tUploadTime=tDataTime;
                }
                
                
                //
                
                
                if(tUploadTime>tSojournTime)
                {
                	tUploadTime=tSojournTime;
                }
                
					double tRData=tNode.getrData()-tNode.gettRate()*tUploadTime;
					
					//begin of debug
					if(tRData<0)
					{
						//System.out.println(tRData);
						tRData=0;
					}
					//end of debug
					
					tNode.setrData(tRData);
					
					

					
					
					double tREnergy=tNode.getaEnergy()-tNode.gettRate()*tUploadTime*eCom;
					
					//begin of debug
					if(tREnergy<0)
					{
						//System.out.println(tREnergy);
						tREnergy=0;
					}
					//end of debug
					
					tNode.setaEnergy(tREnergy);
					

					
					double tTotalSojournTime=tNode.getTotalSojournTime()+tUploadTime;
					tNode.setTotalSojournTime(tTotalSojournTime);
				
			}
			
			
			
			
			
			if(tTourTime <=0)
			{
				flag=false;
			}
			
		}
		
		
		for(int i=0;i<nodeSet.size();i++)
		{
			Node tNode=nodeSet.get(i);
			totalUtility=totalUtility+tNode.getTotalUtility();
		}
		
		result.setTourTime(ExperimentSetting.tourTime);
		result.setNetworkSize(bNet.getnList().size());
		result.setTotalMovingTime(totalMovingTime);
		result.setTotalSojournTime(totalSojournTime);
		result.setTotalUtility(totalUtility);
		return result;
	}
	
	
	/*
	 * max unitutilitygain for each gateway
	 */
	public static LabResult maxUnitUtilityGainTourDesign(BiNetwork bNet,ArrayList<GateWay> solution) throws IOException
	{
		
		ArrayList<Node> nodeSet = bNet.getnList(); //
		ArrayList<GateWay> gatewaySet = bNet.getgList(); //
		
//		//begin debug
//		for(int i=0;i<nodeSet.size();i++)
//		{
//			Node t=nodeSet.get(i);
//			System.out.println("node:"+t.getId()+"--rData("+t.getrData()+")--rEnergy("+t.getaEnergy()+")--hEnergy("+t.gethEnergy());
//		}
//		//end debug
//		if(gNet!=null)
//		{
//			gNet=bNet;
//		}
		
		//initial 
		LabResult result=new LabResult();
		double totalUtility=0;
		double totalSojournTime=0;
		double totalMovingTime=0;
		
		
		
		
		
		

		boolean flag=true;
		double tTourTime=ExperimentSetting.tourTime;

		
		double tMovingTime=0;
		double tSojournTime=0;
		
		double tSinkX=ExperimentSetting.initSinkX;
		double tSinkY=ExperimentSetting.initSinkY;
		
		double tSinkSpeed=ExperimentSetting.mSpeed;
		double lastBackTime=0;
		
		
		
		
		
		while(flag)
		{
			ArrayList<GateWay> tGateWaySet=new ArrayList<GateWay>();
			for(int i=0;i<gatewaySet.size();i++)
			{
				GateWay tGateWay=gatewaySet.get(i);
				double tX=tSinkX-tGateWay.getX();
				double tY=tSinkY-tGateWay.getY();
				double tD=Math.sqrt(tX*tX+tY*tY);
				tGateWay.setMovingTime(tD/tSinkSpeed);  //calculate moving time
				
				tX=ExperimentSetting.initSinkX-tGateWay.getX();
				tY=ExperimentSetting.initSinkY-tGateWay.getY();
				tD=Math.sqrt(tX*tX+tY*tY);
				tGateWay.setBackTime(tD/tSinkSpeed);
				
				tGateWay.calcUnitUtilityGain(lastBackTime,tTourTime,false);
				if(tGateWay.getFeasible()>0)
					tGateWaySet.add(tGateWay);
			}
			
			if(tGateWaySet.size()==0)
			{
				flag=false;
				for(int i=0;i<gatewaySet.size();i++)
				{
					GateWay tGateWay=gatewaySet.get(i);
					tGateWay.calcUnitUtilityGain(lastBackTime,tTourTime,true);     //last  sojourn location
					if(tGateWay.getFeasible()>0)
						tGateWaySet.add(tGateWay);
				}
			}
			
			
			if(tGateWaySet.size()==0)
			{
				flag=false;
				
				for(int i=0;i<nodeSet.size();i++)
				{
					Node tNode=nodeSet.get(i);
					totalUtility=totalUtility+tNode.getTotalUtility();
				}
				
				result.setTourTime(ExperimentSetting.tourTime);
				result.setNetworkSize(bNet.getnList().size());
				result.setTotalMovingTime(totalMovingTime);
				result.setTotalSojournTime(totalSojournTime);
				result.setTotalUtility(totalUtility);
				return result;
			}
			
			Object[] gSet=tGateWaySet.toArray();
			GateWayUnitUtilityGainComparator gCom=new GateWayUnitUtilityGainComparator(false);
			Arrays.sort(gSet,gCom);
			GateWay chosenGateWay=new GateWay((GateWay)gSet[0]);
			
			solution.add(chosenGateWay);
			tMovingTime=chosenGateWay.getMovingTime();
			tSojournTime=chosenGateWay.getSojournTime();
			tTourTime=tTourTime-tMovingTime-tSojournTime;
			lastBackTime=chosenGateWay.getBackTime();
			
			
			totalMovingTime=totalMovingTime+tMovingTime;
			totalSojournTime=totalSojournTime+tSojournTime;
			/*
			 *  move to chosen sojourn location to collect data
			 *  then update both mobilesink, and node status
			 *  including:
			 *  mobilesink(X,Y)
			 *  
			 *  node(rEnergy, rData, hEnergy, and fWeight)  
			 */
			tSinkX=chosenGateWay.getX();
			tSinkY=chosenGateWay.getY();
			
			
			
			//update all nodes
			for(int i=0;i<nodeSet.size();i++)
			{
				Node tNode=nodeSet.get(i);
				double tREnergy=tNode.getaEnergy()+tNode.gethEnergy()*(tMovingTime+tSojournTime);
				if(tREnergy>tNode.getcEnergy())
				{
					tREnergy=tNode.getcEnergy();
				}
				tNode.setaEnergy(tREnergy);
				
//				//begin of debug
//				if(tREnergy<0)
//				{
//					System.out.println(tREnergy);
//				}
//				//end of debug
				
				double tRData=tNode.getrData()+tNode.getgRate()*(tMovingTime+tSojournTime);
				tNode.setrData(tRData);
				
//				//begin of debug
//				if(tRData<0)
//				{
//					System.out.println(tRData);
//				}
//				//end of debug

				
			}
			
			
			//only update neighboring nodes
			for(int i=0;i<chosenGateWay.getNeighborNodes().size();i++)
			{
				Node tNode=chosenGateWay.getNeighborNodes().get(i);
				double eCom=chosenGateWay.geteConSet().get(i);
				
				
				//compare uploadtime and sojourtime
                double tUploadTime=tNode.getaEnergy()/(tNode.gettRate()*eCom);
                double tDataTime=tNode.getrData()/tNode.gettRate();
                if(tUploadTime>tDataTime)
                {
                	tUploadTime=tDataTime;
                }
                
                
                //
                
                
                if(tUploadTime>tSojournTime)
                {
                	tUploadTime=tSojournTime;
                }
                
					double tRData=tNode.getrData()-tNode.gettRate()*tUploadTime;
					
					//begin of debug
					if(tRData<0)
					{
						//System.out.println(tRData);
						tRData=0;
					}
					//end of debug
					
					tNode.setrData(tRData);
					
					

					
					
					double tREnergy=tNode.getaEnergy()-tNode.gettRate()*tUploadTime*eCom;
					
					//begin of debug
					if(tREnergy<0)
					{
						//System.out.println(tREnergy);
						tREnergy=0;
					}
					//end of debug
					
					tNode.setaEnergy(tREnergy);
					

					
					double tTotalSojournTime=tNode.getTotalSojournTime()+tUploadTime;
					tNode.setTotalSojournTime(tTotalSojournTime);
				
			}
			
			
			
			
			
			if(tTourTime <=0)
			{
				flag=false;
			}
			
		}
		
		
		for(int i=0;i<nodeSet.size();i++)
		{
			Node tNode=nodeSet.get(i);
			totalUtility=totalUtility+tNode.getTotalUtility();
		}
		
		result.setTourTime(ExperimentSetting.tourTime);
		result.setNetworkSize(bNet.getnList().size());
		result.setTotalMovingTime(totalMovingTime);
		result.setTotalSojournTime(totalSojournTime);
		result.setTotalUtility(totalUtility);
		return result;
	}
	
	
	
	
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
