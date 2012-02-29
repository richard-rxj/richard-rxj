package dr.alg.anu.au;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import network.dr.alg.anu.au.GateWay;
import network.dr.alg.anu.au.GateWayFBenefitComparator;
import network.dr.alg.anu.au.GateWayLBenefitComparator;
import network.dr.alg.anu.au.Node;

public class TourDesign {

	public static double transmissionRange=25;
	public static double initSinkX=50;
	public static double initSinkY=50;
	public static double gRate= 500; //  bps
	public static double tRate= 10000; //  bps
	public static double tourTime=300;  //  s    -----------------------varible
	public static double[] harvestRate={0.00653,0.01365}; // J/s
	public static double mSpeed=1;   // m/s   
	public static double lossWeight=0.1;  //--------------------------------varible
	public static double beta=0.006; //J/b/m^3     £¡£¡£¡£¡£¡need to reset
	public static Random ran=new Random();
	
	
	
	public ArrayList<Node> getNodeFromFile(String nFile)
	{
		ArrayList<Node> result=new ArrayList<Node>();
	
		return result;
	}
	
	public ArrayList<GateWay> getGateWayFromFile(String gFile,ArrayList<Node> nList)
	{
		ArrayList<GateWay> result=new ArrayList<GateWay>();
		
		return result;
	}
	
	
	public ArrayList<GateWay> linearTourDesign(String nFile, String gFile)
	{
		/*
		 * initial network topology 
		 */
		ArrayList<Node> nodeSet = this.getNodeFromFile(nFile) ; //
		ArrayList<GateWay> gatewaySet = this.getGateWayFromFile(gFile, nodeSet); //
		
		
		
		
		
		ArrayList<GateWay> solution=new ArrayList<GateWay>();
		boolean flag=true;
		double tTourTime=TourDesign.tourTime;
		double tLossWeight=TourDesign.lossWeight;
		
		double tMovingTime=0;
		double tSojournTime=0;
		
		double tSinkX=TourDesign.initSinkX;
		double tSinkY=TourDesign.initSinkY;
		
		double tSinkSpeed=TourDesign.mSpeed;
		double tLossPSec=tLossWeight*nodeSet.size()*TourDesign.gRate;
		
		
		
		while(flag)
		{
			for(int i=0;i<gatewaySet.size();i++)
			{
				GateWay tGateWay=gatewaySet.get(i);
				double tX=tSinkX-tGateWay.getX();
				double tY=tSinkY-tGateWay.getY();
				double tD=Math.sqrt(tX*tX+tY*tY);
				tGateWay.settMovingTime(tD/tSinkSpeed);  //calculate moving time
				tGateWay.calcLBenefit(tLossPSec,tTourTime);
			}
			Object[] gSet=gatewaySet.toArray();
			GateWayLBenefitComparator gCom=new GateWayLBenefitComparator(false);
			Arrays.sort(gSet,gCom);
			GateWay chosenGateWay=new GateWay((GateWay)gSet[0]);
			
			solution.add(chosenGateWay);
			tMovingTime=chosenGateWay.gettMovingTime();
			tSojournTime=chosenGateWay.getlSojournTime();
			tTourTime=tTourTime-tMovingTime-tSojournTime;
			
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
				Node tNode=nodeSet.get(i);
				double eCom=chosenGateWay.geteConSet().get(i);
				if(chosenGateWay.getlActiveNodes().contains(tNode))
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
				tNode.sethEnergy(TourDesign.harvestRate[0]+TourDesign.ran.nextDouble()*(TourDesign.harvestRate[1]-TourDesign.harvestRate[0]));
			
				
				
				tNode.updateLweight();
				
			}
			
			
			
			
			if(tTourTime <=0)
			{
				flag=false;
			}
			
		}
		
		
		
		
		return solution;
	}
	
	
	public ArrayList<GateWay> fairTourDesign(String nFile, String gFile)
	{
		/*
		 * initial network topology 
		 */
		ArrayList<Node> nodeSet = this.getNodeFromFile(nFile) ; //
		ArrayList<GateWay> gatewaySet = this.getGateWayFromFile(gFile, nodeSet); //
		
		
		
		
		
		ArrayList<GateWay> solution=new ArrayList<GateWay>();
		boolean flag=true;
		double tTourTime=TourDesign.tourTime;
		double tLossWeight=TourDesign.lossWeight;
		
		double tMovingTime=0;
		double tSojournTime=0;
		
		double tSinkX=TourDesign.initSinkX;
		double tSinkY=TourDesign.initSinkY;
		
		double tSinkSpeed=TourDesign.mSpeed;
		double tLossPSec=tLossWeight*nodeSet.size()*TourDesign.gRate;
		
		
		
		while(flag)
		{
			for(int i=0;i<gatewaySet.size();i++)
			{
				GateWay tGateWay=gatewaySet.get(i);
				double tX=tSinkX-tGateWay.getX();
				double tY=tSinkY-tGateWay.getY();
				double tD=Math.sqrt(tX*tX+tY*tY);
				tGateWay.settMovingTime(tD/tSinkSpeed);  //calculate moving time
				tGateWay.calcFBenefit(tLossPSec,tTourTime);
			}
			Object[] gSet=gatewaySet.toArray();
			GateWayFBenefitComparator gCom=new GateWayFBenefitComparator(false);
			Arrays.sort(gSet,gCom);
			GateWay chosenGateWay=new GateWay((GateWay)gSet[0]);
			
			solution.add(chosenGateWay);
			tMovingTime=chosenGateWay.gettMovingTime();
			tSojournTime=chosenGateWay.getlSojournTime();
			tTourTime=tTourTime-tMovingTime-tSojournTime;
			
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
				Node tNode=nodeSet.get(i);
				double eCom=chosenGateWay.geteConSet().get(i);
				if(chosenGateWay.getlActiveNodes().contains(tNode))
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
				tNode.sethEnergy(TourDesign.harvestRate[0]+TourDesign.ran.nextDouble()*(TourDesign.harvestRate[1]-TourDesign.harvestRate[0]));
			
				
				
				tNode.updateFweight();
				
			}
			
			
			
			
			if(tTourTime <=0)
			{
				flag=false;
			}
			
		}
		
		
		
		
		return solution;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
