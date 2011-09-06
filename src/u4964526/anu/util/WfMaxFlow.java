package u4964526.anu.util;

import java.util.ArrayList;
import java.util.HashMap;

public class WfMaxFlow {
	/**
	 * @author richard.ren
	 *
	 */
	private double eTx;
	private double eRx;
	private double epsilon;
	private double wfScaleFactor;
	private double delta;
	private double tau=1;
	private Graph topology;
	private HashMap<Vertex,Flow> fSolution;
	
	
	

	public WfMaxFlow() {
		// TODO Auto-generated constructor stub
		topology=new Graph();
		fSolution=new HashMap<Vertex,Flow>();
	}

	public double geteTx() {
		return eTx;
	}

	public void seteTx(double eTx) {
		this.eTx = eTx;
	}

	public double geteRx() {
		return eRx;
	}

	public void seteRx(double eRx) {
		this.eRx = eRx;
	}


	public double getEpsilon() {
		return epsilon;
	}



	public void setEpsilon(double apprFactor) {
		this.epsilon = apprFactor;
		double z=(1+apprFactor)*topology.getEdgeList().size();
		double x=(apprFactor+1)/Math.pow(z,(1/apprFactor));
		double y=Math.log((1+apprFactor)/x)/Math.log(1+apprFactor);
        wfScaleFactor=y;
		delta=x;
	}

	public double getWfMinlength()
	{	
		return delta;
	}


	public double getWfScaleFactor() {
		return wfScaleFactor;
	}


	public double getTau() {
		return tau;
	}

	public void setTau(double slotNum) {
		this.tau = slotNum;
	}

	public HashMap<Vertex, Flow> getFSolution() {
		return fSolution;
	}

	
	public void setTopology(Graph maxG) {
		this.topology = maxG;
	}

	public Graph getTopology() {
		return topology;
	}
	
	
	
	public void computeWFFLow()
	{
		//topology.transit();
		ArrayList<Edge> edgeList=topology.getEdgeList();
		ArrayList<Vertex> sourceList=topology.getSourceList();
		Vertex sink=topology.getSinkList().get(0);
		double gD=0;
		
		for(int i=0;i<edgeList.size();i++)
		{
			double mlTemp=this.getWfMinlength()/edgeList.get(i).getCapacity();
			edgeList.get(i).setLength(mlTemp);
			gD=gD+this.getWfMinlength();
		}
		
		for(int i=0;i<sourceList.size();i++)
		{
			Flow f=new Flow();
			Vertex s=sourceList.get(i);
			f.setStart(s);
			f.setEnd(sink);
			f.setMaxRate(s.getMaxRate()*s.getWeight()/this.getTau());  //??????????????????????
			f.setRate(0);
			fSolution.put(s, f);
		}
		
		boolean w=false;
		if(gD<1)
		{
			w=true;
		}
		int loopSum=0;
		while(w)
		{
			w=false;
			HashMap<Vertex,Path> tPathSet=this.topology.getShortPathAndDSNode(sink);
			double gLamda=1;
			for(int i=0;i<topology.getEdgeList().size();i++)
			{
				Edge te=topology.getEdgeList().get(i);
				if(te.isWasTreed())
				{
					double tsumMaxRate=0;
					for(int j=0;j<te.getWfNodeSet().size();j++)
					{
						/*
						 *  for debug 
						 *
						if(te.getWfNodeSet().size()>1&&te.isWasFaked())
						{
							System.out.println(te.getWfNodeSet().size());
						}
						/*
						 *  for debug
						 */
						tsumMaxRate=tsumMaxRate+te.getWfNodeSet().get(j).getMaxRate()*te.getWfNodeSet().get(j).getWeight();
					}
					double twfFactor=te.getCapacity()/((this.geteTx()+this.geteRx())*tsumMaxRate);
					te.setWfFactor(tsumMaxRate);
					if(te.isWasFaked())
					{
						if(gLamda>twfFactor)
						{
							gLamda=twfFactor;
						}
					}
				}
			}
			
			/*
			 * begin of debug info
			 */
			++loopSum;
			System.out.println("********Wfloop--"+loopSum+"*******\n");
			System.out.println("********WfgD--"+gD+"*******\n");
			System.out.println("********WfFactor--"+gLamda+"*******\n");
			//System.out.println(this.getMaxG());
			/*
			 * end of debug info
			 */
			double gD1=gD;
			gD=0;
			for(int i=0;i<topology.getEdgeList().size();i++)
			{
				Edge te=topology.getEdgeList().get(i);
				if(te.isWasTreed())
				{
					double tLength=te.getLength();
					tLength=tLength*(1+this.getEpsilon()*(this.geteRx()+this.geteTx())*gLamda*te.getWfFactor()/te.getCapacity());
					te.setLength(tLength);
					te.setWasTreed(false);
					te.getWfNodeSet().clear();
					
				}
				gD=gD+te.getCapacity()*te.getLength();
			}
			
			for(int i=0;i<sourceList.size();i++)
			{

				Vertex mSource=sourceList.get(i);
				Path mPath=tPathSet.get(mSource);
				//double wd=mPath.getwULength();
				
				
				

				Flow f=fSolution.get(mSource);
				if((gD1<1)&&(f.getMaxRate()>0))
				{
					/*
					 * begin of update rates and length for next loop
					 */
					 
						 f.addPath(mPath);
						 double addRate=gLamda*f.getMaxRate();
						 w=true;
						 double tRate=f.getRate();
						 double mRate=f.getMaxRate();
						 tRate=tRate+addRate;
						 mRate=mRate-addRate;
						 mPath.updateRealCap(addRate*(this.geteRx()+this.geteTx()));    //only for debug
						 f.setRate(tRate);
						 //f.setMaxRate(mRate);
						 //mSource.setMaxRate(mRate);
					/*
					 * end of update rates and length for next loop
					 */
				}
				else
				{
					w=false;
				}
			}
			
			
		}
		/*
		 * begin of compute the real rate
		 */
		
		for(int i=0;i<sourceList.size();i++)
		{
			Vertex s=sourceList.get(i);
		    Flow f=fSolution.get(s);	
		    double r=f.getRate();
		    r=r*this.getTau()/this.getWfScaleFactor();
		    f.setRate(r);
		}
		
		/*
		 * end of compute the real rate
		 */
	}
	
	
	public int computeDWFFLow()
	{
		topology.transit();
		ArrayList<Edge> edgeList=topology.getEdgeList();
		ArrayList<Vertex> sourceList=topology.getVertexList();
		Vertex sink=topology.getSinkList().get(0);
		double gD=0;
		
		for(int i=0;i<edgeList.size();i++)
		{
			double mlTemp=this.getWfMinlength()/edgeList.get(i).getCapacity();
			edgeList.get(i).setLength(mlTemp);
			gD=gD+this.getWfMinlength();
		}
		
		
		for(int i=0;i<sourceList.size();i++)
		{
			Vertex s=sourceList.get(i);
			double tRate=s.getMaxRate()*s.getWeight()*this.getWfScaleFactor()/this.getTau();
			s.setMaxRate(tRate);
		}
		
		boolean w=false;
		if(gD<1)
		{
			w=true;
		}
		int loopSum=0;
		while(w)
		{
			w=false;
			HashMap<Vertex,Path> tPathSet=this.topology.getShortPathAndDSNode(sink);
			double wfFactor=1;
			for(int i=0;i<topology.getEdgeList().size();i++)
			{
				Edge te=topology.getEdgeList().get(i);
				if(te.isWasTreed())
				{
					double tsumMaxRate=te.getWfFactor();
					
					double twfFactor=te.getCapacity()/((this.geteTx()+this.geteRx())*tsumMaxRate);
					
					
					if(wfFactor>twfFactor)
					{
						wfFactor=twfFactor;
					}
					
					
					/*
					if(te.isWasFaked())
					{
						if(wfFactor>twfFactor)
						{
							wfFactor=twfFactor;
						}
					}
					*/
				}
			}
			
			/*
			 * begin of debug info
			 */
			++loopSum;
			System.out.println("********Wfloop--"+loopSum+"*******\n");
			System.out.println("********WfgD--"+gD+"*******\n");
			System.out.println("********WfFactor--"+wfFactor+"*******\n");
			//System.out.println(this.getMaxG());
			/*
			 * end of debug info
			 */
			double gD1=gD;
			gD=0;
			for(int i=0;i<topology.getEdgeList().size();i++)
			{
				Edge te=topology.getEdgeList().get(i);
				if(te.isWasTreed())
				{
					double tLength=te.getLength();
					tLength=tLength*(1+this.getEpsilon()*(this.geteRx()+this.geteTx())*wfFactor*te.getWfFactor()/te.getCapacity());
					te.setLength(tLength);
					te.setWasTreed(false);
					te.getWfNodeSet().clear();
					te.setWfFactor(0);
					
				}
				gD=gD+te.getCapacity()*te.getLength();
			}
			
			for(int i=0;i<sourceList.size();i++)
			{

				Vertex mSource=sourceList.get(i);
				
				if((gD1<1)&&(mSource.getMaxRate()>0))
				{
					/*
					 * begin of update rates and length for next loop
					 */
					 
						 double addRate=wfFactor*mSource.getMaxRate();
						 w=true;
						 double tRate=mSource.getRate();
						 double mRate=mSource.getMaxRate();
						 tRate=tRate+addRate;
						 mRate=mRate-addRate;
						 mSource.setRate(tRate);
						 mSource.setMaxRate(mRate);
					/*
					 * end of update rates and length for next loop
					 */
				}
				else
				{
					w=false;
				}
			}
			
			
		}
		/*
		 * begin of compute the real rate
		 */
		
		for(int i=0;i<sourceList.size();i++)
		{
			Vertex s=sourceList.get(i);
		    double r=s.getRate();
		    r=r*this.getTau()/this.getWfScaleFactor();
		    s.setRate(r);
		}
		return loopSum;
		/*
		 * end of compute the real rate
		 */
	}
}
