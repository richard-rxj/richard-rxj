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
		double z=(1-this.epsilon)/this.getTopology().getEdgeList().size();           //!!!
		double x=Math.pow(z,(1/this.getEpsilon()));                                  //!!!
		return x;
	}


	public double getGragScaleFactor() {
		double ty=Math.log((1+this.epsilon)/this.getDelta())/Math.log(1+this.epsilon);            //!!!
		double y=1;
        for(int i=0;i<this.topology.getEdgeList().size();i++)
        {
        	Edge e=this.topology.getEdgeList().get(i);
        	double y1=e.getRealCap()/e.getCapacity();
        	if(y1>y)
        	{
        		y=y1;
        	}
        }
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
	
	
	
	
	public double computeConcurrentFlow()
	/*
	 * concurrent!!!!!!!!   
	 */
	{
		topology.transit();   //now!!!
		ArrayList<Edge> edgeList=topology.getEdgeList();
		ArrayList<Vertex> sourceList=topology.getSourceList();
		Vertex sink=topology.getSinkList().get(0);
		double gD=0;
		
		for(int i=0;i<edgeList.size();i++)
		{
			double mlTemp=this.getDelta()/edgeList.get(i).getCapacity();
			edgeList.get(i).setLength(mlTemp);
			gD=gD+edgeList.get(i).getLength()*edgeList.get(i).getCapacity();
		}
		
		for(int i=0;i<sourceList.size();i++)
		{
			Flow f=new Flow();
			Vertex s=sourceList.get(i);
			f.setStart(s);
			f.setEnd(sink);
			//f.setMaxRate(s.getMaxRate()*s.getWeight()*this.getGragScaleFactor());         //!!!!!!!!!!!!!!!1027
			//f.setMaxRate(s.getMaxRate()*this.getGragScaleFactor());                     //now
			f.setMaxRate(s.getMaxRate()*s.getWeight());                                     //!!!
			f.setRate(0);
			fSolution.put(s, f);
		}
		boolean w=false;
		if(gD<1)
		{
			w=true;
		}
		int loopSum=0;
		double startTime=System.currentTimeMillis();
		while(w)
		{
			w=false;
			
			for(int i=0;i<sourceList.size();i++)
			{

				Vertex mSource=sourceList.get(i);
				Path mPath=this.topology.getSingleShortPath(mSource, sink);
				sink.setMinKey(1);
				if (mPath.getEdges().size()==0)
				{
					continue;
				}
				
				if(mSource.getWeight()<0.0001)
				{
					continue;
				}
				//double wd=mPath.getwULength();
				
				++loopSum;
				/*
				 * begin of debug info
				 *
				
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
						 double addRate=mPath.getBottleNeck()/(this.geteRx()+this.geteTx());  //!!!!!!!!!!!!!!!!!
						 //double addRate=mSource.getWeight()*mPath.getBottleNeck()/(this.geteRx()+this.geteTx());   //!!!now
						 if(addRate<f.getMaxRate())
						 {
							 w=true;
							 /*
							  * begin of update length and gD
							  */
							 double tFactor=this.getEpsilon()*mPath.getBottleNeck();
							 mPath.updateLength(tFactor);    //!!!!!!!
							 //mPath.updateLength(tFactor*mSource.getWeight());   //!!!now
							 for(int ti=0;ti<edgeList.size();ti++)
							 {
								    Edge te=edgeList.get(ti);	
								    te.getWfNodeSet().clear();
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
							 mPath.updateRealCap(addRate*(this.geteRx()+this.geteTx()));
						 }
						 else
						 {
							 if(f.getMaxRate()>0.0001)
							 {
								 w=true;
								 /*
								  * begin of update length and gD
								  */
								 double tFactor=this.getEpsilon()*f.getMaxRate()*(this.geteRx()+this.geteTx());
								 mPath.updateLength(tFactor);  //!!!
								 for(int ti=0;ti<edgeList.size();ti++)
								 {
									    Edge te=edgeList.get(ti);	
									    te.getWfNodeSet().clear();
										gD=gD+te.getLength()*te.getCapacity();
								 }
								 /*
								  * end of update length of gD
								  */
								 //mPath.updateRealCap(f.getMaxRate()*(this.geteRx()+this.geteTx())/this.getGragScaleFactor());   //only for debug
								 double tRate=f.getRate();
								 double mRate=f.getMaxRate();
								 mPath.updateRealCap(mRate*(this.geteRx()+this.geteTx()));
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
				
			}
		}
		double endTime=System.currentTimeMillis();
		/*
		 * begin of compute the real rate
		 */
		sink.setMaxRate(loopSum);
		
		double tScaleFactor=this.getGragScaleFactor();
		for(int i=0;i<sourceList.size();i++)
		{
			Vertex s=sourceList.get(i);
		    Flow f=fSolution.get(s);	
		    double r=f.getRate();
		    r=r/tScaleFactor;    //now!!!
		    f.setRate(r);
		}
		
		/*
		 * end of compute the real rate
		 */
		return endTime-startTime;
	}
	
	
	
	
	
}
