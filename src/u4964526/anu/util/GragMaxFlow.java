package u4964526.anu.util;

import java.util.ArrayList;
import java.util.HashMap;

public class GragMaxFlow {
	/**
	 * @author richard.ren
	 *
	 */
	private double eTx;
	private double eRx;
	private double epsilon;
	private Graph topology;
	private HashMap<Vertex,Flow> fSolution;
	
	
	

	public GragMaxFlow() {
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
	}

	public double getDelta()
	{
		double z=this.getTopology().getEdgeList().size()/(1-this.getEpsilon());
		double x=Math.pow(z,(-1/this.getEpsilon()));
		return x;
	}


	public double getGragScaleFactor() {
		double y=Math.log(1/this.getDelta())/Math.log(1+this.getEpsilon());
		return y;
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
	
	
	public void computeGragFlow()
	/*
	 * update the minpath length every loop
	 */
	{
		topology.transit();
		ArrayList<Edge> edgeList=topology.getEdgeList();
		ArrayList<Vertex> sourceList=topology.getSourceList();
		Vertex sink=topology.getSinkList().get(0);
		
		for(int i=0;i<edgeList.size();i++)
		{
			double mlTemp=this.getDelta()/edgeList.get(i).getCapacity();
			edgeList.get(i).setLength(mlTemp);
		}
		
		for(int i=0;i<sourceList.size();i++)
		{
			Flow f=new Flow();
			Vertex s=sourceList.get(i);
			f.setStart(s);
			f.setEnd(sink);
			f.setMaxRate(s.getMaxRate()*s.getWeight()*this.getGragScaleFactor());
			f.setRate(0);
			fSolution.put(s, f);
		}
		
		boolean w=true;
		int loopSum=0;
		while(w)
		{
			w=false;
			for(int i=0;i<sourceList.size();i++)
			{

				Vertex mSource=sourceList.get(i);
                Flow f=fSolution.get(mSource);
				Path mPath=this.topology.getSingleShortPath(mSource, sink);
				double wd=mPath.getwULength();;
				while((wd<1)&&(f.getMaxRate()>0))
				{
					
					
				
				
					/*
					 * begin of debug info
					 */
					++loopSum;
					System.out.println("********loop--"+loopSum+"*******\n");
					System.out.println("********wp--"+wd+"*******\n");
					//System.out.println(this.getMaxG());
					/*
					 * end of debug info
					 */
					
					if(wd==0)
					{
						break;
					}
					
					
						/*
						 * begin of update rates and length for next loop
						 */
						 
							 f.addPath(mPath);
							 double addRate=mPath.getBottleNeck()/(this.geteRx()+this.geteTx());
							 if(addRate<f.getMaxRate())
							 {
								 w=true;
								 double tFactor=this.getEpsilon()*mPath.getBottleNeck();
								 mPath.updateLength(tFactor);
								 mPath.updateRealCap(mPath.getBottleNeck()/this.getGragScaleFactor());    //only for debug
								 double tRate=f.getRate();
								 double mRate=f.getMaxRate();
								 tRate=tRate+addRate;
								 mRate=mRate-addRate;
								 f.setRate(tRate);
								 f.setMaxRate(mRate);
							 }
							 else
							 {
								 
								 w=true;
								 double tFactor=this.getEpsilon()*f.getMaxRate()*(this.geteRx()+this.geteTx());
								 mPath.updateLength(tFactor);
								 mPath.updateRealCap(f.getMaxRate()*(this.geteRx()+this.geteTx())/this.getGragScaleFactor());   //only for debug
								 double tRate=f.getRate();
								 double mRate=f.getMaxRate();
								 tRate=tRate+mRate;
								 mRate=0;
								 f.setRate(tRate);
								 f.setMaxRate(mRate); 
								 
							 }
						 
						
						/*
						 * end of update rates and length for next loop
						 */
					
					
					mPath=this.topology.getSingleShortPath(mSource, sink);
					wd=mPath.getwULength();
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
		    r=r/this.getGragScaleFactor();
		    f.setRate(r);
		}
		
		/*
		 * end of compute the real rate
		 */
	}
	
	
	public int computeConcurrentFlow()
	/*
	 * concurrent!!!!!!!!   
	 */
	{
		topology.transit();
		ArrayList<Edge> edgeList=topology.getEdgeList();
		ArrayList<Vertex> sourceList=topology.getSourceList();
		Vertex sink=topology.getSinkList().get(0);
		double gD=0;
		
		for(int i=0;i<edgeList.size();i++)
		{
			double mlTemp=this.getDelta()/edgeList.get(i).getCapacity();
			edgeList.get(i).setLength(mlTemp);
			gD=gD+this.getDelta();
		}
		
		for(int i=0;i<sourceList.size();i++)
		{
			Flow f=new Flow();
			Vertex s=sourceList.get(i);
			f.setStart(s);
			f.setEnd(sink);
			f.setMaxRate(s.getMaxRate()*s.getWeight()*this.getGragScaleFactor());
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
			for(int i=0;i<sourceList.size();i++)
			{

				Vertex mSource=sourceList.get(i);
				Path mPath=this.topology.getSingleShortPath(mSource, sink);
				if (mPath.getEdges().size()==0)
				{
					continue;
				}
				//double wd=mPath.getwULength();
				
				
				/*
				 * begin of debug info
				 *
				++loopSum;
				System.out.println("********Gragloop--"+loopSum+"*******\n");
				System.out.println("********GraggD--"+gD+"*******\n");
				//System.out.println(this.getMaxG());
				/*
				 * end of debug info
				 */
				
                Flow f=fSolution.get(mSource);
				if((gD<1)&&(f.getMaxRate()>0))
				{
					/*
					 * begin of update rates and length for next loop
					 */
						 
						 f.addPath(mPath);
						 gD=0;
						 double addRate=mPath.getBottleNeck()/(this.geteRx()+this.geteTx());
						 if(addRate<f.getMaxRate())
						 {
							 w=true;
							 /*
							  * begin of update length and gD
							  */
							 double tFactor=this.getEpsilon()*mPath.getBottleNeck();
							 mPath.updateLength(tFactor);
							 for(int ti=0;ti<edgeList.size();ti++)
							 {
								    Edge te=edgeList.get(ti);									
									gD=gD+te.getLength()*te.getCapacity();
							 }
							 /*
							  * end of update length of gD
							  */
							// mPath.updateRealCap(mPath.getBottleNeck()/this.getGragScaleFactor());    //only for debug
							 double tRate=f.getRate();
							 double mRate=f.getMaxRate();
							 tRate=tRate+addRate;
							 mRate=mRate-addRate;
							 f.setRate(tRate);
							 f.setMaxRate(mRate);
						 }
						 else
						 {
							 if(f.getMaxRate()>0)
							 {
								 w=true;
								 /*
								  * begin of update length and gD
								  */
								 double tFactor=this.getEpsilon()*f.getMaxRate()*(this.geteRx()+this.geteTx());
								 mPath.updateLength(tFactor);
								 for(int ti=0;ti<edgeList.size();ti++)
								 {
									    Edge te=edgeList.get(ti);									
										gD=gD+te.getLength()*te.getCapacity();
								 }
								 /*
								  * end of update length of gD
								  */
								 //mPath.updateRealCap(f.getMaxRate()*(this.geteRx()+this.geteTx())/this.getGragScaleFactor());   //only for debug
								 double tRate=f.getRate();
								 double mRate=f.getMaxRate();
								 tRate=tRate+mRate;
								 mRate=0;
								 f.setRate(tRate);
								 f.setMaxRate(mRate); 
							 }
							 
						 }
					 
					
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
		    r=r/this.getGragScaleFactor();
		    f.setRate(r);
		}
		
		/*
		 * end of compute the real rate
		 */
		return loopSum;
	}
	
	
	
	
	
}
