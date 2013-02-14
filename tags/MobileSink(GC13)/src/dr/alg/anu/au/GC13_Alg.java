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
		/*
		 * initial network topology 
		 */
		//BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
		ArrayList<Node> nodeSet = bNet.getnList(); //
		ArrayList<GateWay> gatewaySet = bNet.getgList(); //
		
		//initial 
		LabResult result=new LabResult();
		double totalUtilityGain=0;
		double totalSojournTime=0;
		double totalMovingTime=0;
		
		
		
		//ArrayList<GateWay> solution=new ArrayList<GateWay>();
		boolean flag=true;
		double tTourTime=ExperimentSetting.tourTime;
		//double tLossWeight=TourDesign.lossWeight;
		double tUsedTime=0;
		double tMovingTime=0;
		double tSojournTime=0;
		
		double tSinkX=ExperimentSetting.initSinkX;
		double tSinkY=ExperimentSetting.initSinkY;
		
		double tSinkSpeed=ExperimentSetting.mSpeed;
		//double tLossPSec=tLossWeight*nodeSet.size()*TourDesign.gRate;
		double lastBackTime=0;
		
		
		
		
		
		
		while(flag)
		{
			ArrayList<GateWay> tGateWaySet=new ArrayList<GateWay>();
			ArrayList<GateWay> tDistanceGateWaySet=new ArrayList<GateWay>();
			ArrayList<Node>    tTimeStampSet=new ArrayList<Node>();

			
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
				
				tGateWay.setTimeStamp(tUsedTime-iniTimeStamp-deltaTimeStamp);
				/*
				 * first select 
				 */
				if((tGateWay.getMovingTime()+tGateWay.getBackTime())>=tTourTime)
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
				
				if(tDistanceGateWaySet.size()>0)
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
			/*
			 * 
			 */
			
						
			if(tDistanceGateWaySet.size()==0)
			{
				flag=false;
				return result;
			}
			
			
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
						tGateWay.calcTraPriorityWeight(tUsedTime);
					}					
				}
				if(tGateWaySet.size()>0)
				{
					timeStampFlag=false;
				}
				tTimeStamp=tTimeStamp-deltaTimeStamp;
			}
			/*
			 * 
			 */
			
			Object[] gSet=tGateWaySet.toArray();
			//GateWayPriorityWeightComparator gCom=new GateWayPriorityWeightComparator(false);
			GateWayPriorityWeightComparator gCom=new GateWayPriorityWeightComparator(true);
			Arrays.sort(gSet,gCom);
			GateWay realGateWay=(GateWay)gSet[0];
			
			realGateWay.calcUnitUtilityGain(lastBackTime, tTourTime, false);
			if(realGateWay.getSojournTime()>(tTourTime-realGateWay.getMovingTime()-realGateWay.getBackTime()))
			{
				realGateWay.calcUnitUtilityGain(lastBackTime,tTourTime, true);   //last sojourn location
			}
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
			totalUtilityGain=totalUtilityGain+chosenGateWay.getUtilityGain();
			
			
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
				double tREnergy=tNode.getrEnergy()+tNode.gethEnergy()*(tMovingTime+tSojournTime);
				if(tREnergy>tNode.getcEnergy())
				{
					tREnergy=tNode.getcEnergy();
				}
				tNode.setrEnergy(tREnergy);
				
				double tRData=tNode.getrData()+tNode.getgRate()*(tMovingTime+tSojournTime);
				tNode.setrData(tRData);
							
				
			}
			
			
			//only update neighboring nodes
			for(int i=0;i<chosenGateWay.getNeighborNodes().size();i++)
			{
				Node tNode=chosenGateWay.getNeighborNodes().get(i);
				double eCom=chosenGateWay.geteConSet().get(i);
				
				tNode.calcUploadTime(tMovingTime, eCom);
                double tUploadTime=tNode.getUploadTime();
                if(tUploadTime>tSojournTime)
                {
                	tUploadTime=tSojournTime;
                }
                
					double tRData=tNode.getrData()-tNode.gettRate()*tUploadTime;
					tNode.setrData(tRData);
					
					double tREnergy=tNode.getrEnergy()-tRData*eCom;
					tNode.setrEnergy(tREnergy);
					
					double tTotalSojournTime=tNode.getTotalSojournTime()+tUploadTime;
					tNode.setTotalSojournTime(tTotalSojournTime);
				
			}
			
			
			if(tTourTime <=0)
			{
				flag=false;
			}
			
		}
		
		
		result.setTourTime(ExperimentSetting.tourTime);
		result.setNetworkSize(bNet.getnList().size());
		result.setTotalMovingTime(totalMovingTime);
		result.setTotalSojournTime(totalSojournTime);
		result.setTotalUtility(totalUtilityGain);
		return result;
	}
	
	
	
	
	public static LabResult randomUtilityGainTourDesign(BiNetwork bNet,ArrayList<GateWay> solution) throws IOException
	{
		/*
		 * initial network topology 
		 */
		//BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
		ArrayList<Node> nodeSet = bNet.getnList(); //
		ArrayList<GateWay> gatewaySet = bNet.getgList(); //
		
		
		//initial 
		LabResult result=new LabResult();
		double totalUtilityGain=0;
		double totalSojournTime=0;
		double totalMovingTime=0;
		
		
		//ArrayList<GateWay> solution=new ArrayList<GateWay>();
		boolean flag=true;
		double tTourTime=ExperimentSetting.tourTime;
		//double tLossWeight=TourDesign.lossWeight;
		
		double tMovingTime=0;
		double tSojournTime=0;
		
		double tSinkX=ExperimentSetting.initSinkX;
		double tSinkY=ExperimentSetting.initSinkY;
		
		double tSinkSpeed=ExperimentSetting.mSpeed;
		//double tLossPSec=tLossWeight*nodeSet.size()*TourDesign.gRate;
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
				return result;
			}
			
			Object[] gSet=tGateWaySet.toArray();
			//GateWayUnitUtilityGainComparator gCom=new GateWayUnitUtilityGainComparator(false);
			//Arrays.sort(gSet,gCom);
			GateWay chosenGateWay=new GateWay((GateWay)gSet[ExperimentSetting.ran.nextInt(gSet.length)]);
			
			solution.add(chosenGateWay);
			tMovingTime=chosenGateWay.getMovingTime();
			tSojournTime=chosenGateWay.getSojournTime();
			tTourTime=tTourTime-tMovingTime-tSojournTime;
			lastBackTime=chosenGateWay.getBackTime();
			
			totalMovingTime=totalMovingTime+tMovingTime;
			totalSojournTime=totalSojournTime+tSojournTime;
			totalUtilityGain=totalUtilityGain+chosenGateWay.getUtilityGain();
			
			
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
				double tREnergy=tNode.getrEnergy()+tNode.gethEnergy()*(tMovingTime+tSojournTime);
				if(tREnergy>tNode.getcEnergy())
				{
					tREnergy=tNode.getcEnergy();
				}
				tNode.setrEnergy(tREnergy);
				
				double tRData=tNode.getrData()+tNode.getgRate()*(tMovingTime+tSojournTime);
				tNode.setrData(tRData);
							
				
			}
			
			
			//only update neighboring nodes
			for(int i=0;i<chosenGateWay.getNeighborNodes().size();i++)
			{
				Node tNode=chosenGateWay.getNeighborNodes().get(i);
				double eCom=chosenGateWay.geteConSet().get(i);
				
				tNode.calcUploadTime(tMovingTime, eCom);
                double tUploadTime=tNode.getUploadTime();
                if(tUploadTime>tSojournTime)
                {
                	tUploadTime=tSojournTime;
                }
                
					double tRData=tNode.getrData()-tNode.gettRate()*tUploadTime;
					tNode.setrData(tRData);
					
					double tREnergy=tNode.getrEnergy()-tRData*eCom;
					tNode.setrEnergy(tREnergy);
					
					double tTotalSojournTime=tNode.getTotalSojournTime()+tUploadTime;
					tNode.setTotalSojournTime(tTotalSojournTime);
				
			}
			
			
			if(tTourTime <=0)
			{
				flag=false;
			}
			
		}
		
		
		result.setTourTime(ExperimentSetting.tourTime);
		result.setNetworkSize(bNet.getnList().size());
		result.setTotalMovingTime(totalMovingTime);
		result.setTotalSojournTime(totalSojournTime);
		result.setTotalUtility(totalUtilityGain);
		return result;
	}
	
	
	
	
	
	
	public static LabResult maxUnitUtilityGainTourDesign(BiNetwork bNet,ArrayList<GateWay> solution) throws IOException
	{
		/*
		 * initial network topology 
		 */
		//BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
		ArrayList<Node> nodeSet = bNet.getnList(); //
		ArrayList<GateWay> gatewaySet = bNet.getgList(); //
		
//		if(gNet!=null)
//		{
//			gNet=bNet;
//		}
		
		//initial 
		LabResult result=new LabResult();
		double totalUtilityGain=0;
		double totalSojournTime=0;
		double totalMovingTime=0;
		
		
		
		
		
		
		//ArrayList<GateWay> solution=new ArrayList<GateWay>();
		boolean flag=true;
		double tTourTime=ExperimentSetting.tourTime;
		//double tLossWeight=TourDesign.lossWeight;
		
		double tMovingTime=0;
		double tSojournTime=0;
		
		double tSinkX=ExperimentSetting.initSinkX;
		double tSinkY=ExperimentSetting.initSinkY;
		
		double tSinkSpeed=ExperimentSetting.mSpeed;
		//double tLossPSec=tLossWeight*nodeSet.size()*TourDesign.gRate;
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
			totalUtilityGain=totalUtilityGain+chosenGateWay.getUtilityGain();
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
				double tREnergy=tNode.getrEnergy()+tNode.gethEnergy()*(tMovingTime+tSojournTime);
				if(tREnergy>tNode.getcEnergy())
				{
					tREnergy=tNode.getcEnergy();
				}
				tNode.setrEnergy(tREnergy);
				
				double tRData=tNode.getrData()+tNode.getgRate()*(tMovingTime+tSojournTime);
				tNode.setrData(tRData);
							
				
			}
			
			
			//only update neighboring nodes
			for(int i=0;i<chosenGateWay.getNeighborNodes().size();i++)
			{
				Node tNode=chosenGateWay.getNeighborNodes().get(i);
				double eCom=chosenGateWay.geteConSet().get(i);
				
				tNode.calcUploadTime(tMovingTime, eCom);
                double tUploadTime=tNode.getUploadTime();
                if(tUploadTime>tSojournTime)
                {
                	tUploadTime=tSojournTime;
                }
                
					double tRData=tNode.getrData()-tNode.gettRate()*tUploadTime;
					tNode.setrData(tRData);
					
					double tREnergy=tNode.getrEnergy()-tRData*eCom;
					tNode.setrEnergy(tREnergy);
					
					double tTotalSojournTime=tNode.getTotalSojournTime()+tUploadTime;
					tNode.setTotalSojournTime(tTotalSojournTime);
				
			}
			
			
			if(tTourTime <=0)
			{
				flag=false;
			}
			
		}
		
		
		
		
		result.setTourTime(ExperimentSetting.tourTime);
		result.setNetworkSize(bNet.getnList().size());
		result.setTotalMovingTime(totalMovingTime);
		result.setTotalSojournTime(totalSojournTime);
		result.setTotalUtility(totalUtilityGain);
		return result;
	}
	
	
	
	
	
	
	public static LabResult getSimInfo(ArrayList<GateWay> solution, BiNetwork bNet, double tourTime)
	{
		
		LabResult result=new LabResult();
		int activeNodes=0;
		double totalUtilityGain=0;
		double totalBenefitGain=0;
		double totalSojournTime=0;
		double totalMovingTime=0;
		ArrayList<Node> tempList=new ArrayList<Node>();
		
		for(int i=0; i<solution.size();i++)
		{
			GateWay g=solution.get(i);
			totalUtilityGain=totalUtilityGain+g.getUtilityGain();
			totalBenefitGain=totalBenefitGain+g.getBenefitGain();
			totalSojournTime=totalSojournTime+g.getSojournTime();
			totalMovingTime=totalMovingTime+g.getMovingTime();
			for(int j=0;j<g.getActiveNodes().size();j++)
			{
				Node n=g.getActiveNodes().get(j);
				if(!tempList.contains(n))
				{
					tempList.add(n);
				}
			}
		}
		activeNodes=tempList.size();
		
		result.setTourTime(tourTime);
		result.setNetworkSize(bNet.getnList().size());
		result.setActiveNodes(activeNodes);
		result.setTotalMovingTime(totalMovingTime);
		result.setTotalSojournTime(totalSojournTime);
		result.setTotalThroughput(totalBenefitGain);
		result.setTotalUtility(totalUtilityGain);
		result.setVariance(ExperimentSetting.calcVariance(bNet.getnList()));
		
		return result;
	}
	
	
	
	
	
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
