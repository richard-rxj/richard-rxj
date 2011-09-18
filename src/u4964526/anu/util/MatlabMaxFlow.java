package u4964526.anu.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

public class MatlabMaxFlow {

	private double eTx=1;
	private double eRx=1;
	private Graph maxG;
	
	public MatlabMaxFlow() {
		maxG=new Graph();
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

	public Graph getMaxG() {
		return maxG;
	}

	public void setMaxG(Graph maxG) {
		this.maxG = maxG;
	}

	
	public void computeLPMatlab(String dir) throws FileNotFoundException
	{
		Logger logger=Logger.getLogger("MaxFlow");
		
		File mFile=new File(dir);
		if(!mFile.exists())
		{
			mFile.mkdirs();
		}
		
		PrintWriter pwA=new PrintWriter(new OutputStreamWriter(new FileOutputStream(dir+"/a.txt")));
		PrintWriter pwF=new PrintWriter(new OutputStreamWriter(new FileOutputStream(dir+"/f.txt")));
		PrintWriter pwB=new PrintWriter(new OutputStreamWriter(new FileOutputStream(dir+"/b.txt")));
		PrintWriter pwLB=new PrintWriter(new OutputStreamWriter(new FileOutputStream(dir+"/lb.txt")));

		ArrayList<Path> pSet=maxG.getAllPath(maxG.getSinkList().get(0));
		/*
		 *  begin of output f and lb Matrix
		 */
		for(int i=0;i<pSet.size();i++)
		{
			pwF.println(0);
			pwLB.println(0);
			logger.info(i+"--"+pSet.get(i));
		}
		pwF.println(-1);
		pwF.flush();
		pwLB.println(0);
		pwLB.flush();
		/*
		 *  end of output f and lb Matrix
		 */
		
		/*
		 *  begin of output A and b Matrix
		 */
		Iterator<Vertex> iv=maxG.getSourceList().iterator();
		while(iv.hasNext())
		{
			Vertex tv=iv.next();
			for(int i=0;i<pSet.size();i++)
			{
				Path tp=pSet.get(i);
				if(tp.getStart()==tv)
				{
					pwA.print("-1 ");
				}
				else
				{
					pwA.print("0 ");
				}
			}
			pwA.println(tv.getMaxRate());
			pwA.flush();
			pwB.println(0);
			pwB.flush();
		}
		
		Iterator<Edge> ie=maxG.getEdgeList().iterator();
		while(ie.hasNext())
		{
			Edge te=ie.next();
			for(int i=0;i<pSet.size();i++)
			{
				Path tp=pSet.get(i);
				if(tp.getEdges().contains(te))
				{
					pwA.print("1 ");
				}
				else
				{
					pwA.print("0 ");
				}
			}
			pwA.println(0);
			pwA.flush();
			pwB.println(te.getCapacity()/(eTx+eRx));
			pwB.flush();
		}
		/*
		 *  begin of output A and b Matrix
		 */		
	}
}
