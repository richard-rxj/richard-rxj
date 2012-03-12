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
import network.dr.alg.anu.au.GateWayUnitBenefitGainComparator;
import network.dr.alg.anu.au.GateWayUnitUtilityGainComparator;
import network.dr.alg.anu.au.GateWayUtilityGainComparator;
import network.dr.alg.anu.au.LabResult;
import network.dr.alg.anu.au.Node;

public class NewTourDesign {

	
	
	
	
	
	
	public static ArrayList<GateWay> randomBenefitGainTourDesign(String nFile, String gFile) throws IOException
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
				
				tGateWay.calcBenefitGain(lastBackTime,tTourTime,false);
				if(tGateWay.getFeasible()>0)
					tGateWaySet.add(tGateWay);
			}
			
			if(tGateWaySet.size()==0)
			{
				flag=false;
				for(int i=0;i<gatewaySet.size();i++)
				{
					GateWay tGateWay=gatewaySet.get(i);
					tGateWay.calcBenefitGain(lastBackTime,tTourTime,true);     //last  sojourn location
					if(tGateWay.getFeasible()>0)
						tGateWaySet.add(tGateWay);
				}
			}
			
			if(tGateWaySet.size()==0)
			{
				flag=false;
				return solution;
			}
			
			Object[] gSet=tGateWaySet.toArray();
			//GateWayBenefitGainComparator gCom=new GateWayBenefitGainComparator(false);
			//Arrays.sort(gSet,gCom);
			GateWay chosenGateWay=new GateWay((GateWay)gSet[ExperimentSetting.ran.nextInt(gSet.length)]);
			
			solution.add(chosenGateWay);
			tMovingTime=chosenGateWay.getMovingTime();
			tSojournTime=chosenGateWay.getSojournTime();
			tTourTime=tTourTime-tMovingTime-tSojournTime;
			lastBackTime=chosenGateWay.getBackTime();
			
			
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
					
					double tREnergy=tNode.getrEnergy()-tDData*eCom;
					tNode.setrEnergy(tREnergy);
					
					double tTotalSojournTime=tNode.getTotalSojournTime()+tSojournTime;
					tNode.setTotalSojournTime(tTotalSojournTime);
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
				
				
				
			}
			
			
			if(tTourTime <=0)
			{
				flag=false;
			}
			
		}
		
		
		return solution;
	}
	
	
	
	
	
	
	
	
	
	public static ArrayList<GateWay> maxBenefitGainTourDesign(String nFile, String gFile) throws IOException
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
				
				tGateWay.calcBenefitGain(lastBackTime,tTourTime,false);
				if(tGateWay.getFeasible()>0)
					tGateWaySet.add(tGateWay);
			}
			
			if(tGateWaySet.size()==0)
			{
				flag=false;
				for(int i=0;i<gatewaySet.size();i++)
				{
					GateWay tGateWay=gatewaySet.get(i);
					tGateWay.calcBenefitGain(lastBackTime,tTourTime,true);     //last  sojourn location
					if(tGateWay.getFeasible()>0)
						tGateWaySet.add(tGateWay);
				}
			}
			
			if(tGateWaySet.size()==0)
			{
				flag=false;
				return solution;
			}
			
			Object[] gSet=tGateWaySet.toArray();
			GateWayBenefitGainComparator gCom=new GateWayBenefitGainComparator(false);
			Arrays.sort(gSet,gCom);
			GateWay chosenGateWay=new GateWay((GateWay)gSet[0]);
			
			solution.add(chosenGateWay);
			tMovingTime=chosenGateWay.getMovingTime();
			tSojournTime=chosenGateWay.getSojournTime();
			tTourTime=tTourTime-tMovingTime-tSojournTime;
			lastBackTime=chosenGateWay.getBackTime();
			
			
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
					
					double tREnergy=tNode.getrEnergy()-tDData*eCom;
					tNode.setrEnergy(tREnergy);
					
					double tTotalSojournTime=tNode.getTotalSojournTime()+tSojournTime;
					tNode.setTotalSojournTime(tTotalSojournTime);
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
				
				
				
			}
			
			
			if(tTourTime <=0)
			{
				flag=false;
			}
			
		}
		
		
		return solution;
	}
	
	
	
	public static ArrayList<GateWay> maxUnitBenefitGainTourDesign(String nFile, String gFile, BiNetwork gNet) throws IOException
	{
		/*
		 * initial network topology 
		 */
		BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
		ArrayList<Node> nodeSet = bNet.getnList(); //
		ArrayList<GateWay> gatewaySet = bNet.getgList(); //
		
		
		if(gNet!=null)
		{
			gNet=bNet;
		}
		
		
		
		
		ArrayList<GateWay> solution=new ArrayList<GateWay>();
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
				
				tGateWay.calcUnitBenefitGain(lastBackTime,tTourTime,false);
				if(tGateWay.getFeasible()>0)
					tGateWaySet.add(tGateWay);
			}
			
			if(tGateWaySet.size()==0)
			{
				flag=false;
				for(int i=0;i<gatewaySet.size();i++)
				{
					GateWay tGateWay=gatewaySet.get(i);
					tGateWay.calcUnitBenefitGain(lastBackTime,tTourTime,true);     //last  sojourn location
					if(tGateWay.getFeasible()>0)
						tGateWaySet.add(tGateWay);
				}
			}
			
			
			if(tGateWaySet.size()==0)
			{
				flag=false;
				return solution;
			}
			
			Object[] gSet=tGateWaySet.toArray();
			GateWayUnitBenefitGainComparator gCom=new GateWayUnitBenefitGainComparator(false);
			Arrays.sort(gSet,gCom);
			GateWay chosenGateWay=new GateWay((GateWay)gSet[0]);
			
			solution.add(chosenGateWay);
			tMovingTime=chosenGateWay.getMovingTime();
			tSojournTime=chosenGateWay.getSojournTime();
			tTourTime=tTourTime-tMovingTime-tSojournTime;
			lastBackTime=chosenGateWay.getBackTime();
			
			
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
					
					double tREnergy=tNode.getrEnergy()-tDData*eCom;
					tNode.setrEnergy(tREnergy);
					
					double tTotalSojournTime=tNode.getTotalSojournTime()+tSojournTime;
					tNode.setTotalSojournTime(tTotalSojournTime);
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
				
				
				
			}
			
			
			if(tTourTime <=0)
			{
				flag=false;
			}
			
		}
		
		
		return solution;
	}
	
	
	
	public static ArrayList<Node> maxUnitBenefitGainTourDesignNode(String nFile, String gFile) throws IOException
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
				
				tGateWay.calcUnitBenefitGain(lastBackTime,tTourTime,false);
				if(tGateWay.getFeasible()>0)
					tGateWaySet.add(tGateWay);
			}
			
			if(tGateWaySet.size()==0)
			{
				flag=false;
				for(int i=0;i<gatewaySet.size();i++)
				{
					GateWay tGateWay=gatewaySet.get(i);
					tGateWay.calcUnitBenefitGain(lastBackTime,tTourTime,true);     //last  sojourn location
					if(tGateWay.getFeasible()>0)
						tGateWaySet.add(tGateWay);
				}
			}
			
			
			if(tGateWaySet.size()==0)
			{
				flag=false;
				return nodeSet;
			}
			
			Object[] gSet=tGateWaySet.toArray();
			GateWayUnitBenefitGainComparator gCom=new GateWayUnitBenefitGainComparator(false);
			Arrays.sort(gSet,gCom);
			GateWay chosenGateWay=new GateWay((GateWay)gSet[0]);
			
			solution.add(chosenGateWay);
			tMovingTime=chosenGateWay.getMovingTime();
			tSojournTime=chosenGateWay.getSojournTime();
			tTourTime=tTourTime-tMovingTime-tSojournTime;
			lastBackTime=chosenGateWay.getBackTime();
			
			
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
					
					double tREnergy=tNode.getrEnergy()-tDData*eCom;
					tNode.setrEnergy(tREnergy);
					
					double tTotalSojournTime=tNode.getTotalSojournTime()+tSojournTime;
					tNode.setTotalSojournTime(tTotalSojournTime);
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
				
				
				
			}
			
			
			if(tTourTime <=0)
			{
				flag=false;
			}
			
		}
		
		
		return nodeSet;
	}
	
	
	
	
	public static ArrayList<GateWay> randomUtilityGainTourDesign(String nFile, String gFile) throws IOException
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
				return solution;
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
					
					double tREnergy=tNode.getrEnergy()-tDData*eCom;
					tNode.setrEnergy(tREnergy);
					
					double tTotalSojournTime=tNode.getTotalSojournTime()+tSojournTime;
					tNode.setTotalSojournTime(tTotalSojournTime);
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
				
				
				
			}
			
			
			if(tTourTime <=0)
			{
				flag=false;
			}
			
		}
		
		
		return solution;
	}
	
	
	
	public static ArrayList<GateWay> maxUtilityGainTourDesign(String nFile, String gFile) throws IOException
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
				return solution;
			}
			
			Object[] gSet=tGateWaySet.toArray();
			GateWayUtilityGainComparator gCom=new GateWayUtilityGainComparator(false);
			Arrays.sort(gSet,gCom);
			GateWay chosenGateWay=new GateWay((GateWay)gSet[0]);
			
			solution.add(chosenGateWay);
			tMovingTime=chosenGateWay.getMovingTime();
			tSojournTime=chosenGateWay.getSojournTime();
			tTourTime=tTourTime-tMovingTime-tSojournTime;
			lastBackTime=chosenGateWay.getBackTime();
			
			
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
					
					double tREnergy=tNode.getrEnergy()-tDData*eCom;
					tNode.setrEnergy(tREnergy);
					
					double tTotalSojournTime=tNode.getTotalSojournTime()+tSojournTime;
					tNode.setTotalSojournTime(tTotalSojournTime);
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
				
				
				
			}
			
			
			if(tTourTime <=0)
			{
				flag=false;
			}
			
		}
		
		
		return solution;
	}
	
	
	public static ArrayList<GateWay> maxUnitUtilityGainTourDesign(String nFile, String gFile, BiNetwork gNet) throws IOException
	{
		/*
		 * initial network topology 
		 */
		BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
		ArrayList<Node> nodeSet = bNet.getnList(); //
		ArrayList<GateWay> gatewaySet = bNet.getgList(); //
		
		if(gNet!=null)
		{
			gNet=bNet;
		}
		
		
		
		ArrayList<GateWay> solution=new ArrayList<GateWay>();
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
				return solution;
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
					
					double tREnergy=tNode.getrEnergy()-tDData*eCom;
					tNode.setrEnergy(tREnergy);
					
					double tTotalSojournTime=tNode.getTotalSojournTime()+tSojournTime;
					tNode.setTotalSojournTime(tTotalSojournTime);
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
				
				
				
			}
			
			
			if(tTourTime <=0)
			{
				flag=false;
			}
			
		}
		
		
		return solution;
	}
	
	
	public static ArrayList<Node> maxUnitUtilityGainTourDesignNode(String nFile, String gFile) throws IOException
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
				return nodeSet;
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
					
					double tREnergy=tNode.getrEnergy()-tDData*eCom;
					tNode.setrEnergy(tREnergy);
					
					double tTotalSojournTime=tNode.getTotalSojournTime()+tSojournTime;
					tNode.setTotalSojournTime(tTotalSojournTime);
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
				
				
				
			}
			
			
			if(tTourTime <=0)
			{
				flag=false;
			}
			
		}
		
		
		return nodeSet;
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
		
		result.setActiveNodes(activeNodes);
		result.setTotalMovingTime(totalMovingTime);
		result.setTotalSojournTime(totalSojournTime);
		result.setTotalThroughput(totalBenefitGain);
		result.setTotalUtility(totalUtilityGain);
		
		return result;
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
