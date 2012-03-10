package dr.alg.anu.au;

import generate.dr.alg.anu.au.NetworkGenerator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;


import network.dr.alg.anu.au.BiNetwork;
import network.dr.alg.anu.au.GateWay;
import network.dr.alg.anu.au.GateWayBenefitGainComparator;
import network.dr.alg.anu.au.GateWayFBenefitComparator;
import network.dr.alg.anu.au.GateWayLBenefitComparator;
import network.dr.alg.anu.au.GateWayMovingTimeComparator;
import network.dr.alg.anu.au.LabResult;
import network.dr.alg.anu.au.Node;

public class TourDesign {

	
	
	
	
	public static ArrayList<GateWay> nearestLinearTourDesign(String nFile, String gFile) throws IOException
	{
		/*
		 * initial network topology 
		 */
		BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
		ArrayList<Node> nodeSet = bNet.getnList(); //
		ArrayList<GateWay> gatewaySet = bNet.getgList(); //
		
		
		
		
		
		ArrayList<GateWay> solution=new ArrayList<GateWay>();
		boolean flag=true;
		double tTourTime=ExperimentSetting.tourTime;
		double tLossWeight=ExperimentSetting.lossWeight;
		
		double tMovingTime=0;
		double tSojournTime=0;
		
		double tSinkX=ExperimentSetting.initSinkX;
		double tSinkY=ExperimentSetting.initSinkY;
		
		double tSinkSpeed=ExperimentSetting.mSpeed;
		double tLossPSec=tLossWeight*nodeSet.size()*ExperimentSetting.gRate;
		
		
		
		while(flag)
		{
			for(int i=0;i<gatewaySet.size();i++)
			{
				GateWay tGateWay=gatewaySet.get(i);
				double tX=tSinkX-tGateWay.getX();
				double tY=tSinkY-tGateWay.getY();
				double tD=Math.sqrt(tX*tX+tY*tY);
				tGateWay.setMovingTime(tD/tSinkSpeed);  //calculate moving time
				tGateWay.calcLBenefit(tLossPSec,tTourTime);   //utility function
				if(tGateWay.getSojournTime()<ExperimentSetting.minSojournTime)
				{
					tGateWay.setMovingTime(Double.POSITIVE_INFINITY);
				}
			}
			Object[] gSet=gatewaySet.toArray();
			GateWayMovingTimeComparator gCom=new GateWayMovingTimeComparator(true);
			Arrays.sort(gSet,gCom);
			GateWay chosenGateWay=new GateWay((GateWay)gSet[0]);
			


			
			solution.add(chosenGateWay);
			tMovingTime=chosenGateWay.getMovingTime();
			tSojournTime=chosenGateWay.getSojournTime();
			tTourTime=tTourTime-tMovingTime-tSojournTime;
			
			if(tSojournTime<=0)
			{
				flag=false;
			}
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
			
			//only update active nodes
			for(int i=0;i<chosenGateWay.getNeighborNodes().size();i++)
			{
				Node tNode=chosenGateWay.getNeighborNodes().get(i);
				double eCom=chosenGateWay.geteConSet().get(i);
				if(chosenGateWay.getActiveNodes().contains(tNode))
				{
					double tDData=tNode.gettRate()*tSojournTime;
					double tRData=tNode.getrData();
					tNode.setrData(tRData-tDData);
					double tREnergy=tNode.getrEnergy()-tDData*eCom;
					tNode.setrEnergy(tREnergy);
					
				}
			}
			
			//update nodes
			for(int i=0;i<nodeSet.size();i++)
			{
				Node tNode=nodeSet.get(i);
				double tREnergy=tNode.getrEnergy()+tNode.gethEnergy()*(tMovingTime+tSojournTime);
				if(tREnergy>tNode.getcEnergy())
				{
					tREnergy=tNode.getcEnergy();
				}
				tNode.setrEnergy(tREnergy);
				
				//reallocate harvesting rate
				//tNode.sethEnergy(TourDesign.harvestRate[0]+TourDesign.ran.nextDouble()*(TourDesign.harvestRate[1]-TourDesign.harvestRate[0]));
			
				
				
				tNode.updateLweight();       //utility function
				
			}
			

			
			
			if(tTourTime <=0)
			{
				flag=false;
			}
			
		}
		
		
		
		
		return solution;
	}
	
	
	
	
	
	
	
	public static ArrayList<GateWay> randomLinearTourDesign(String nFile, String gFile) throws IOException
	{
		/*
		 * initial network topology 
		 */
		BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
		ArrayList<Node> nodeSet = bNet.getnList(); //
		ArrayList<GateWay> gatewaySet = bNet.getgList(); //
		
		
		
		
		
		ArrayList<GateWay> solution=new ArrayList<GateWay>();
		boolean flag=true;
		double tTourTime=ExperimentSetting.tourTime;
		double tLossWeight=ExperimentSetting.lossWeight;
		
		double tMovingTime=0;
		double tSojournTime=0;
		
		double tSinkX=ExperimentSetting.initSinkX;
		double tSinkY=ExperimentSetting.initSinkY;
		
		double tSinkSpeed=ExperimentSetting.mSpeed;
		double tLossPSec=tLossWeight*nodeSet.size()*ExperimentSetting.gRate;
		
		
		
		while(flag)
		{
//			for(int i=0;i<gatewaySet.size();i++)
//			{
//				GateWay tGateWay=gatewaySet.get(i);
//				double tX=tSinkX-tGateWay.getX();
//				double tY=tSinkY-tGateWay.getY();
//				double tD=Math.sqrt(tX*tX+tY*tY);
//				tGateWay.setMovingTime(tD/tSinkSpeed);  //calculate moving time
//				tGateWay.calcLBenefit(tLossPSec,tTourTime);
//			}
//			Object[] gSet=gatewaySet.toArray();
//			GateWayLBenefitComparator gCom=new GateWayLBenefitComparator(false);
//			Arrays.sort(gSet,gCom);
//			GateWay chosenGateWay=new GateWay((GateWay)gSet[0]);
			
			GateWay chosenGateWay=gatewaySet.get(new Random().nextInt(gatewaySet.size()));
			double tX=tSinkX-chosenGateWay.getX();
			double tY=tSinkY-chosenGateWay.getY();
			double tD=Math.sqrt(tX*tX+tY*tY);
			chosenGateWay.setMovingTime(tD/tSinkSpeed);  //calculate moving time
			chosenGateWay.calcLBenefit(tLossPSec,tTourTime);  //utility function

			
			solution.add(chosenGateWay);
			tMovingTime=chosenGateWay.getMovingTime();
			tSojournTime=chosenGateWay.getSojournTime();
			tTourTime=tTourTime-tMovingTime-tSojournTime;
			
			if(tSojournTime<=0)
			{
				flag=false;
			}
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
			
			//only update active nodes
			for(int i=0;i<chosenGateWay.getNeighborNodes().size();i++)
			{
				Node tNode=chosenGateWay.getNeighborNodes().get(i);
				double eCom=chosenGateWay.geteConSet().get(i);
				if(chosenGateWay.getActiveNodes().contains(tNode))
				{
					double tDData=tNode.gettRate()*tSojournTime;
					double tRData=tNode.getrData();
					tNode.setrData(tRData-tDData);
					double tREnergy=tNode.getrEnergy()-tDData*eCom;
					tNode.setrEnergy(tREnergy);
					
				}
			}
			
			//update nodes
			for(int i=0;i<nodeSet.size();i++)
			{
				Node tNode=nodeSet.get(i);
				double tREnergy=tNode.getrEnergy()+tNode.gethEnergy()*(tMovingTime+tSojournTime);
				if(tREnergy>tNode.getcEnergy())
				{
					tREnergy=tNode.getcEnergy();
				}
				tNode.setrEnergy(tREnergy);
				
				//reallocate harvesting rate
				//tNode.sethEnergy(TourDesign.harvestRate[0]+TourDesign.ran.nextDouble()*(TourDesign.harvestRate[1]-TourDesign.harvestRate[0]));
			
				
				
				tNode.updateLweight();       //utility function
				
			}
			

			
			
			if(tTourTime <=0)
			{
				flag=false;
			}
			
		}
		
		
		
		
		return solution;
	}
	
	
	
	
	
	
	public static ArrayList<GateWay> linearTourDesign(String nFile, String gFile) throws IOException
	{
		/*
		 * initial network topology 
		 */
		BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
		ArrayList<Node> nodeSet = bNet.getnList(); //
		ArrayList<GateWay> gatewaySet = bNet.getgList(); //
		
		
		
		
		
		ArrayList<GateWay> solution=new ArrayList<GateWay>();
		boolean flag=true;
		double tTourTime=ExperimentSetting.tourTime;
		double tLossWeight=ExperimentSetting.lossWeight;
		
		double tMovingTime=0;
		double tSojournTime=0;
		
		double tSinkX=ExperimentSetting.initSinkX;
		double tSinkY=ExperimentSetting.initSinkY;
		
		double tSinkSpeed=ExperimentSetting.mSpeed;
		double tLossPSec=tLossWeight*nodeSet.size()*ExperimentSetting.gRate;
		
		
		
		while(flag)
		{
			for(int i=0;i<gatewaySet.size();i++)
			{
				GateWay tGateWay=gatewaySet.get(i);
				double tX=tSinkX-tGateWay.getX();
				double tY=tSinkY-tGateWay.getY();
				double tD=Math.sqrt(tX*tX+tY*tY);
				tGateWay.setMovingTime(tD/tSinkSpeed);  //calculate moving time
				tGateWay.calcLBenefit(tLossPSec,tTourTime);
			}
			Object[] gSet=gatewaySet.toArray();
			GateWayLBenefitComparator gCom=new GateWayLBenefitComparator(false);
			Arrays.sort(gSet,gCom);
			GateWay chosenGateWay=new GateWay((GateWay)gSet[0]);
			
			solution.add(chosenGateWay);
			tMovingTime=chosenGateWay.getMovingTime();
			tSojournTime=chosenGateWay.getSojournTime();
			tTourTime=tTourTime-tMovingTime-tSojournTime;
			
			if(tSojournTime<=0)
			{
				flag=false;
			}
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
			
			//only update active nodes
			for(int i=0;i<chosenGateWay.getNeighborNodes().size();i++)
			{
				Node tNode=chosenGateWay.getNeighborNodes().get(i);
				double eCom=chosenGateWay.geteConSet().get(i);
				if(chosenGateWay.getActiveNodes().contains(tNode))
				{
					double tDData=tNode.gettRate()*tSojournTime;
					double tRData=tNode.getrData();
					tNode.setrData(tRData-tDData);
					double tREnergy=tNode.getrEnergy()-tDData*eCom;
					tNode.setrEnergy(tREnergy);
					
				}
			}
			
			//update nodes
			for(int i=0;i<nodeSet.size();i++)
			{
				Node tNode=nodeSet.get(i);
				double tREnergy=tNode.getrEnergy()+tNode.gethEnergy()*(tMovingTime+tSojournTime);
				if(tREnergy>tNode.getcEnergy())
				{
					tREnergy=tNode.getcEnergy();
				}
				tNode.setrEnergy(tREnergy);
				
				//reallocate harvesting rate
				//tNode.sethEnergy(TourDesign.harvestRate[0]+TourDesign.ran.nextDouble()*(TourDesign.harvestRate[1]-TourDesign.harvestRate[0]));
			
				
				
				tNode.updateLweight();
				
			}
			

			
			
			if(tTourTime <=0)
			{
				flag=false;
			}
			
		}
		
		
		
		
		return solution;
	}
	
	
	public static ArrayList<GateWay> fairTourDesign(String nFile, String gFile) throws IOException
	{
		/*
		 * initial network topology 
		 */
		BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
		ArrayList<Node> nodeSet = bNet.getnList(); //
		ArrayList<GateWay> gatewaySet = bNet.getgList(); //
		
		
		
		
		
		ArrayList<GateWay> solution=new ArrayList<GateWay>();
		boolean flag=true;
		double tTourTime=ExperimentSetting.tourTime;
		double tLossWeight=ExperimentSetting.lossWeight;
		
		double tMovingTime=0;
		double tSojournTime=0;
		
		double tSinkX=ExperimentSetting.initSinkX;
		double tSinkY=ExperimentSetting.initSinkY;
		
		double tSinkSpeed=ExperimentSetting.mSpeed;
		double tLossPSec=tLossWeight*nodeSet.size()*ExperimentSetting.gRate;
		
		
		
		while(flag)
		{
			for(int i=0;i<gatewaySet.size();i++)
			{
				GateWay tGateWay=gatewaySet.get(i);
				double tX=tSinkX-tGateWay.getX();
				double tY=tSinkY-tGateWay.getY();
				double tD=Math.sqrt(tX*tX+tY*tY);
				tGateWay.setMovingTime(tD/tSinkSpeed);  //calculate moving time
				tGateWay.calcFBenefit(tLossPSec,tTourTime);
			}
			Object[] gSet=gatewaySet.toArray();
			GateWayFBenefitComparator gCom=new GateWayFBenefitComparator(false);
			Arrays.sort(gSet,gCom);
			GateWay chosenGateWay=new GateWay((GateWay)gSet[0]);
			
			solution.add(chosenGateWay);
			tMovingTime=chosenGateWay.getMovingTime();
			tSojournTime=chosenGateWay.getSojournTime();
			tTourTime=tTourTime-tMovingTime-tSojournTime;
			
			if(tSojournTime<=0)
			{
				flag=false;
			}
			
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
			
			//only update active nodes
			for(int i=0;i<chosenGateWay.getNeighborNodes().size();i++)
			{
				Node tNode=chosenGateWay.getNeighborNodes().get(i);
				double eCom=chosenGateWay.geteConSet().get(i);
				if(chosenGateWay.getActiveNodes().contains(tNode))
				{
					double tDData=tNode.gettRate()*tSojournTime;
					double tRData=tNode.getrData();
					tNode.setrData(tRData-tDData);
					double tREnergy=tNode.getrEnergy()-tDData*eCom;
					tNode.setrEnergy(tREnergy);
					
				}
			}
			
			//update nodes
			for(int i=0;i<nodeSet.size();i++)
			{
				Node tNode=nodeSet.get(i);
				double tREnergy=tNode.getrEnergy()+tNode.gethEnergy()*(tMovingTime+tSojournTime);
				if(tREnergy>tNode.getcEnergy())
				{
					tREnergy=tNode.getcEnergy();
				}
				tNode.setrEnergy(tREnergy);
				
				//reallocate harvesting rate
				//tNode.sethEnergy(TourDesign.harvestRate[0]+TourDesign.ran.nextDouble()*(TourDesign.harvestRate[1]-TourDesign.harvestRate[0]));
			
				
				
				tNode.updateFweight();
				
			}
			
			
			
			
			if(tTourTime <=0)
			{
				flag=false;
			}
			
		}
		
		
		
		
		return solution;
	}
	
	
	
	
	
	
	public static LabResult getSimInfo(ArrayList<GateWay> solution, BiNetwork bNet, double tourTime)
	{
		
		LabResult result=new LabResult();
		int activeNodes=0;
		double totalUtility=0;
		double totalThroughput=0;
		double totalSojournTime=0;
		double totalMovingTime=0;
		ArrayList<Node> tempList=new ArrayList<Node>();
		
		for(int i=0; i<solution.size();i++)
		{
			GateWay g=solution.get(i);
			totalUtility=totalUtility+g.getUtility();
			totalThroughput=totalThroughput+g.getThroughput();
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
		
		result.setActiveNodes(activeNodes);
		result.setTotalMovingTime(totalMovingTime);
		result.setTotalSojournTime(totalSojournTime);
		result.setTotalThroughput(totalThroughput);
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
