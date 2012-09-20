package u4964526.anu.util;

import java.util.ArrayList;
import java.util.HashMap;

public class MaxFlow {
	/**
	 * @author richard.ren
	 *
	 */
	private double eTx;
	private double eRx;
	private double apprFactor;
	private double minLength;
	private double scaleFactor;
	private Graph maxG;
	private HashMap<Vertex,Flow> maxFlow;
	
	
	

	public MaxFlow() {
		// TODO Auto-generated constructor stub
		maxG=new Graph();
		maxFlow=new HashMap<Vertex,Flow>();
	}

	public double getMinLength() {
		return minLength;
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

	public void setMinLength(double minLength) {
		//this.minLength = minLength;

	}



	public double getApprFactor() {
		return apprFactor;
	}



	public void setApprFactor(double apprFactor) {
		this.apprFactor = apprFactor;
		double z=(1+this.getApprFactor())*this.getMaxG().getEdgeList().size();
		double x=(this.getApprFactor()+1)/Math.pow(z,(1/this.getApprFactor()));
		double y=Math.log(1/x)/Math.log(1+this.getApprFactor());
		this.minLength=x;
		this.scaleFactor=y;
	}



	public double getScaleFactor() {
		return scaleFactor;
	}



	public HashMap<Vertex, Flow> getMaxFlow() {
		return maxFlow;
	}

	
	public void setMaxG(Graph maxG) {
		this.maxG = maxG;
	}

	public Graph getMaxG() {
		return maxG;
	}



	public void computeFlow2()
	/*
	 * update all shortpath length every time
	 */
	{
		maxG.transit();
		ArrayList<Edge> edgeList=maxG.getEdgeList();
		ArrayList<Vertex> sourceList=maxG.getSourceList();
		Vertex sink=maxG.getSinkList().get(0);
		
		for(int i=0;i<edgeList.size();i++)
		{
			edgeList.get(i).setLength(this.minLength);
		}
		
		for(int i=0;i<sourceList.size();i++)
		{
			Flow f=new Flow();
			Vertex s=sourceList.get(i);
			f.setStart(s);
			f.setEnd(sink);
			f.setMaxRate(s.getMaxRate());
			f.setRate(0);
			maxFlow.put(s, f);
		}
		
		boolean w=true;
		int loopSum=0;
		while(w)
		{
			
			
			HashMap<Vertex,Path> shortpathSet=maxG.getShortPath(sink);
			double wp=Double.POSITIVE_INFINITY;
			
					
			for(int i=0;i<sourceList.size();i++)
			{
				Vertex s=sourceList.get(i);
				
				Path p=shortpathSet.get(s);
				
				if(wp>p.getwLength())
				{
					wp=p.getwLength();
				}
				
			}
			
			/*
			 * begin of debug info
			 */
			++loopSum;
			System.out.println("********loop--"+loopSum+"*******\n");
			System.out.println("********wp--"+wp+"*******\n");
			System.out.println(this.getMaxG());
			/*
			 * end of debug info
			 */
			
			
			if(wp<1)
			{
				/*
				 * begin of update rates and length for next loop
				 */
				 for (int i=0;i<sourceList.size();i++)
				 {
	                 Vertex s=sourceList.get(i);
	                 Flow f=maxFlow.get(s);
	                 Path p=shortpathSet.get(s);
					 f.addPath(p);
					 double addRate=p.getBottleNeck()/(this.geteRx()+this.geteTx());
					 if(addRate<f.getMaxRate())
					 {
						 double tFactor=this.getApprFactor()*p.getBottleNeck();
						 p.updateLength(tFactor);
						 double tRate=f.getRate();
						 double mRate=f.getMaxRate();
						 tRate=tRate+addRate;
						 mRate=mRate-addRate;
						 f.setRate(tRate);
						 f.setMaxRate(mRate);
					 }
					 else
					 {
						 double tFactor=this.getApprFactor()*f.getMaxRate()*(this.geteRx()+this.geteTx());
						 p.updateLength(tFactor);
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
		/*
		 * begin of compute the real rate
		 */
		
		for(int i=0;i<sourceList.size();i++)
		{
			Vertex s=sourceList.get(i);
		    Flow f=maxFlow.get(s);	
		    double r=f.getRate();
		    r=r;//adjust r is needed!!!!!!!!!!!!!!
		    f.setRate(r);
		}
		
		/*
		 * end of compute the real rate
		 */
	}
	
	
	
	public void computeFlow()
	/*
	 * update the minpath length every loop
	 */
	{
		maxG.transit();
		ArrayList<Edge> edgeList=maxG.getEdgeList();
		ArrayList<Vertex> sourceList=maxG.getSourceList();
		Vertex sink=maxG.getSinkList().get(0);
		
		for(int i=0;i<edgeList.size();i++)
		{
			edgeList.get(i).setLength(this.minLength);
		}
		
		for(int i=0;i<sourceList.size();i++)
		{
			Flow f=new Flow();
			Vertex s=sourceList.get(i);
			f.setStart(s);
			f.setEnd(sink);
			f.setMaxRate(s.getMaxRate());
			f.setRate(0);
			maxFlow.put(s, f);
		}
		
		boolean w=true;
		int loopSum=0;
		while(w)
		{
			
			
			HashMap<Vertex,Path> shortpathSet=maxG.getShortPath(sink);
			double wp=Double.POSITIVE_INFINITY;
			
			Vertex mSource=sourceList.get(0);
			Path mPath=shortpathSet.get(mSource);
					
			for(int i=0;i<sourceList.size();i++)
			{
				Vertex s=sourceList.get(i);
				
				Path p=shortpathSet.get(s);
				
				if(wp>p.getwLength())
				{
					mSource=s;
					mPath=p;
					wp=p.getwLength();
				}
				
			}
			
			/*
			 * begin of debug info
			 */
			++loopSum;
			System.out.println("********loop--"+loopSum+"*******\n");
			System.out.println("********wp--"+wp+"*******\n");
			System.out.println(this.getMaxG());
			/*
			 * end of debug info
			 */
			
			
			if(wp<1)
			{
				/*
				 * begin of update rates and length for next loop
				 */
				 
	                 
	                 Flow f=maxFlow.get(mSource);
					 f.addPath(mPath);
					 double addRate=mPath.getBottleNeck()/(this.geteRx()+this.geteTx());
					 if(addRate<f.getMaxRate())
					 {
						 double tFactor=this.getApprFactor()*mPath.getBottleNeck();
						 mPath.updateLength(tFactor);
						 double tRate=f.getRate();
						 double mRate=f.getMaxRate();
						 tRate=tRate+addRate;
						 mRate=mRate-addRate;
						 f.setRate(tRate);
						 f.setMaxRate(mRate);
					 }
					 else
					 {
						 double tFactor=this.getApprFactor()*f.getMaxRate()*(this.geteRx()+this.geteTx());
						 mPath.updateLength(tFactor);
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
			}
			else
			{
				w=false;
			}
		}
		/*
		 * begin of compute the real rate
		 */
		
		for(int i=0;i<sourceList.size();i++)
		{
			Vertex s=sourceList.get(i);
		    Flow f=maxFlow.get(s);	
		    double r=f.getRate();
		    r=r;//adjust r is needed!!!!!!!!!!!!!!
		    f.setRate(r);
		}
		
		/*
		 * end of compute the real rate
		 */
	}
	
	
	public void computeConcurrentFlow()
	/*
	 * concurrent!!!!!!!!   
	 */
	{
		//maxG.transit();
		ArrayList<Edge> edgeList=maxG.getEdgeList();
		ArrayList<Vertex> sourceList=maxG.getSourceList();
		Vertex sink=maxG.getSinkList().get(0);
		
		for(int i=0;i<edgeList.size();i++)
		{
			double mlTemp=this.minLength/edgeList.get(i).getCapacity();
			edgeList.get(i).setLength(mlTemp);
		}
		
		for(int i=0;i<sourceList.size();i++)
		{
			Flow f=new Flow();
			Vertex s=sourceList.get(i);
			f.setStart(s);
			f.setEnd(sink);
			f.setMaxRate(s.getMaxRate()*s.getWeight());
			f.setRate(0);
			maxFlow.put(s, f);
		}
		
		boolean w=true;
		int loopSum=0;
		while(w)
		{
			w=false;
			for(int i=0;i<sourceList.size();i++)
			{

				Vertex mSource=sourceList.get(i);
				Path mPath=this.maxG.getSingleShortPath(mSource, sink);
				double wd=mPath.getwULength();
				
				
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
					continue;
				}
				
				if(wd<1)
				{
					/*
					 * begin of update rates and length for next loop
					 */
					 
		                 Flow f=maxFlow.get(mSource);
						 f.addPath(mPath);
						 double addRate=mPath.getBottleNeck()/(this.geteRx()+this.geteTx());
						 if(addRate<f.getMaxRate())
						 {
							 w=true;
							 double tFactor=this.getApprFactor()*mPath.getBottleNeck();
							 mPath.updateLength(tFactor);
							 //mPath.updateRealCap(mPath.getStart().getWeight()*mPath.getBottleNeck()/this.getScaleFactor());    //only for debug
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
								 double tFactor=this.getApprFactor()*f.getMaxRate()*(this.geteRx()+this.geteTx());
								 mPath.updateLength(tFactor);
								// mPath.updateRealCap(f.getMaxRate()*(this.geteRx()+this.geteTx())/this.getScaleFactor());   //only for debug
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
		 *
		
		for(int i=0;i<sourceList.size();i++)
		{
			Vertex s=sourceList.get(i);
		    Flow f=maxFlow.get(s);	
		    double r=f.getRate();
		    r=r/this.getScaleFactor();
		    f.setRate(r);
		}
		
		*
		 * end of compute the real rate
		 */
		
	}
	
	
	public void computeWFFLow()
	{
		//maxG.transit();
		ArrayList<Edge> edgeList=maxG.getEdgeList();
		ArrayList<Vertex> sourceList=maxG.getSourceList();
		Vertex sink=maxG.getSinkList().get(0);
		
		for(int i=0;i<edgeList.size();i++)
		{
			double mlTemp=this.minLength/edgeList.get(i).getCapacity();
			edgeList.get(i).setLength(mlTemp);
		}
		
		for(int i=0;i<sourceList.size();i++)
		{
			Flow f=new Flow();
			Vertex s=sourceList.get(i);
			f.setStart(s);
			f.setEnd(sink);
			f.setMaxRate(s.getMaxRate()*s.getWeight());
			f.setRate(0);
			maxFlow.put(s, f);
		}
		
		boolean w=true;
		int loopSum=0;
		while(w)
		{
			w=false;
			HashMap<Vertex,Path> tPathSet=this.maxG.getShortPathAndDSNode(sink);
			double wfFactor=1;
			for(int i=0;i<maxG.getEdgeList().size();i++)
			{
				Edge te=maxG.getEdgeList().get(i);
				if(te.isWasTreed())
				{
					double tsumMaxRate=0;
					for(int j=0;j<te.getWfNodeSet().size();j++)
					{
						tsumMaxRate=tsumMaxRate+te.getWfNodeSet().get(j).getMaxRate()*te.getWfNodeSet().get(j).getWeight();
					}
					double twfFactor=te.getCapacity()/((this.geteTx()+this.geteRx())*tsumMaxRate);
					te.setWfFactor(tsumMaxRate);
					if(wfFactor>twfFactor)
					{
						wfFactor=twfFactor;
					}
				}
			}
			
			
			
			
			for(int i=0;i<sourceList.size();i++)
			{

				Vertex mSource=sourceList.get(i);
				Path mPath=tPathSet.get(mSource);
				double wd=mPath.getwULength();
				
				
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
					continue;
				}
				
				if(wd<1)
				{
					/*
					 * begin of update rates and length for next loop
					 */
					 
		                 Flow f=maxFlow.get(mSource);
						 f.addPath(mPath);
						 double addRate=wfFactor*f.getMaxRate();
						 if(f.getMaxRate()>0)
						 {
							 w=true;
							 double tRate=f.getRate();
							 double mRate=f.getMaxRate();
							 tRate=tRate+addRate;
							 mRate=mRate-addRate;
							 f.setRate(tRate);
							 f.setMaxRate(mRate);
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
			
			for(int i=0;i<maxG.getEdgeList().size();i++)
			{
				Edge te=maxG.getEdgeList().get(i);
				if(te.isWasTreed())
				{
					double tLength=te.getLength();
					tLength=tLength*(1+this.getApprFactor()*(this.geteRx()+this.geteTx())*wfFactor*te.getWfFactor()/te.getCapacity());
					te.setLength(tLength);
					te.setWasTreed(false);
					te.getWfNodeSet().clear();
				}
			}
			
		}
		/*
		 * begin of compute the real rate
		 *
		
		for(int i=0;i<sourceList.size();i++)
		{
			Vertex s=sourceList.get(i);
		    Flow f=maxFlow.get(s);	
		    double r=f.getRate();
		    r=r/this.getScaleFactor();
		    f.setRate(r);
		}
		
		*
		 * end of compute the real rate
		 */
	}
	
	
}
