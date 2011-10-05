package u4964526.anu.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

public class RealDataHandler {
	
	int nodeSum=50;
	int dataSum=100;

	public int getNodeSum() {
		return nodeSum;
	}

	public void setNodeSum(int nodeSum) {
		this.nodeSum = nodeSum;
	}

	public int getDataSum() {
		return dataSum;
	}

	public void setDataSum(int dataSum) {
		this.dataSum = dataSum;
	}

	private double computeDataCT(double[] u, double[] v, double threshold)
	{
		int result=0;
		for(int i=0;i<u.length;i++)
		{
			double temp=Math.abs(u[i]-v[i])/Math.max(u[i], v[i]);
			if(temp<threshold)
			{
				result++;
			}
		}
		return 1.0*result/u.length;
	}
	
	public void getData(String dateFilter, String sourceFile,String dataFile)
	{
		try
		{
			double[][] result=new double[nodeSum][dataSum];
			BufferedReader bf=new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile)));
		    String tempString;
		    
			int nodeNum=1;
			int nodeId=nodeSum;
			int dataNum=0;
			while(((tempString=bf.readLine())!=null)&&(nodeNum<=nodeId))
			{

				
				String[] temp=tempString.split(" ");
				if(temp[0].equals(dateFilter))
				{
					if((!temp[4].equals(""))&&(Double.parseDouble(temp[4])<100))
					{
						if(Integer.parseInt(temp[3])==nodeNum)
						{
							if(dataNum<dataSum)
							{
								System.out.println(temp[4]+" * "+nodeNum+"-"+dataNum);
								if(nodeNum==51)
								{
									result[4][dataNum]=Double.parseDouble(temp[4]);
								}
								else if(nodeNum==52)
								{
									result[14][dataNum]=Double.parseDouble(temp[4]);
								}
								else
								{
									result[nodeNum-1][dataNum]=Double.parseDouble(temp[4]);
								}
								dataNum++;
							}
							else
							{
								nodeNum++;
								if(nodeNum==5)
								{
									nodeNum++;
									nodeId++;
								}
								if(nodeNum==15)
								{
									nodeNum++;
									nodeId++;
								}
								
								
								
								dataNum=0;
							}
						}
						
					}
				}
			}
			
			
			
			
			PrintWriter pw1=new PrintWriter(new OutputStreamWriter(new FileOutputStream(dataFile+"-1.txt")));
			PrintWriter pw2=new PrintWriter(new OutputStreamWriter(new FileOutputStream(dataFile+"-2.txt")));
			PrintWriter pw3=new PrintWriter(new OutputStreamWriter(new FileOutputStream(dataFile+"-3.txt")));
			PrintWriter pw4=new PrintWriter(new OutputStreamWriter(new FileOutputStream(dataFile+"-4.txt")));
			PrintWriter pw5=new PrintWriter(new OutputStreamWriter(new FileOutputStream(dataFile+"-5.txt")));
			PrintWriter pw6=new PrintWriter(new OutputStreamWriter(new FileOutputStream(dataFile+"-6.txt")));

			
			for(int i=0;i<nodeSum;i++)
			{
				for(int j=0;j<dataSum;j++)
				{
					System.out.println(i+"-"+j+":"+result[i][j]);
					if(j<100)
					{
						pw1.print(result[i][j]+" ");
					}
					else if(j<200)
					{
						pw2.print(result[i][j]+" ");
					}
					else if(j<300)
					{
						pw3.print(result[i][j]+" ");
					}
					else if(j<400)
					{
						pw4.print(result[i][j]+" ");
					}
					else if(j<500)
					{
						pw5.print(result[i][j]+" ");
					}
					else if(j<600)
					{
						pw6.print(result[i][j]+" ");
					}
				}
				pw1.println();
				pw2.println();
			}
			pw1.flush();
			pw1.close();
			pw2.flush();
			pw2.close();
			pw3.flush();
			pw3.close();
			pw4.flush();
			pw4.close();
			pw5.flush();
			pw5.close();
			pw6.flush();
			pw6.close();
			bf.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	
	
	private double[][] loadData(String dataFile)
	{
		double[][] result=new double[nodeSum][dataSum];
		try
		{
			BufferedReader bf=new BufferedReader(new InputStreamReader(new FileInputStream(dataFile)));
			String tempString;
			int lineNum=0;
			while((tempString=bf.readLine())!=null)
			{
				String[] tData=tempString.split(" ");
				for(int i=0;i<100;i++)
				{
					result[lineNum][i]=Double.parseDouble(tData[i]);
				}
				lineNum++;
			}
			bf.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	public void outputCTFile(String dataFile,String ctFile)
	{
		try
		{
			double[][] result=new double[nodeSum][nodeSum];
			double[][] dataSet=this.loadData(dataFile);
			int pairSum=0;
			
			for(int i=0;i<nodeSum;i++)
			{
				for(int j=0;j<nodeSum;j++)
				{
					if(i!=j)
					{
						result[i][j]=this.computeDataCT(dataSet[i], dataSet[j], 0.005);
					}
						if(result[i][j]>0.8)
					{
						System.out.println((i+1)+"-"+(j+1)+":"+result[i][j]);
						pairSum++;
					}
				}
			}
			System.out.println(pairSum);
			
			
			
			PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(ctFile)));
			for(int i=0;i<nodeSum;i++)
			{
				for(int j=0;j<nodeSum;j++)
				{
					pw.print(result[i][j]+" ");
				}
				pw.println();
			}
			pw.flush();
			pw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public void outputDataFile(String sourceFile,String matchFile, String dataFile)
	{
		try
		{
			double[][] source=this.loadData(sourceFile);
			double[][] result=new double[nodeSum][dataSum];
			BufferedReader bf=new BufferedReader(new InputStreamReader(new FileInputStream(matchFile)));
			String tempString;
			while((tempString=bf.readLine())!=null)
			{
				String[] temp=tempString.split(" ");
				int ri=Integer.parseInt(temp[0]);
				int si=Integer.parseInt(temp[3]);
				result[ri-1]=source[si-1];
			}
			
			PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(dataFile)));
			for(int i=0;i<nodeSum;i++)
			{
				for(int j=0;j<dataSum;j++)
				{
					pw.print(result[i][j]+" ");
				}
				pw.println();
			}
			pw.flush();
			pw.close();
			bf.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public Graph getGraph(String sVertex,String fVertex,String fEdge) throws NumberFormatException, IOException
	{
		Graph g=new Graph();
		BufferedReader bf=new BufferedReader(new InputStreamReader(new FileInputStream(sVertex)));
		double gRadius=9;
		int i=0;
		String tempString;
		while((tempString=bf.readLine())!=null)
		{
			String[] temp=tempString.split(" ");
			if(temp.length<2)
			{
				continue;
			}
			int tN=Integer.parseInt(temp[0])+1;
			Vertex s=new Vertex(String.valueOf(tN));
			s.setxLabel(Double.parseDouble(temp[1]));
			s.setyLabel(Double.parseDouble(temp[2]));
			g.addVertex(s);
			if(i==0)
			{
				g.addSink(s);
				s.setWasSink(true);
			}
			else
			{
				g.addSource(s);
				s.setWasSource(true);
			}
			i=i+1;	
		}
		
		
		
		
		
		
		ArrayList<Vertex> vSet=g.getVertexList();
		Iterator<Vertex> slowI=vSet.iterator();
		Iterator<Vertex> fastI;
		while(slowI.hasNext())
		{
			Vertex v1=slowI.next();
			fastI=vSet.iterator();
			
			while(fastI.next()!=v1)
			{
				
			}
			
			Vertex v2;
			while(fastI.hasNext())
			{
				v2=fastI.next();
				double tRange=this.validTransRangePlusEdgeCapacity(v1, v2,gRadius);
				if(tRange>0)
				{
					//double tV1=this.getgFactorRecv()*v1.getMaxRate()+this.getgFactorSend()*v1.getMaxRate()*Math.pow((tRange), 2);
					Edge e1=new Edge(v1,v2,v1.getBudgetEnergy());
					g.addEdge(e1);
					//double tV2=this.getgFactorRecv()*v2.getMaxRate()+this.getgFactorSend()*v2.getMaxRate()*Math.pow((tRange), 2);
					Edge e2=new Edge(v2,v1,v2.getBudgetEnergy());
					g.addEdge(e2);
				}
				
				
				
				
			}
						
		}
		
		boolean w=true;
		double tRadius=gRadius;
		while(w)
		{
			tRadius=tRadius+1;
			w=false;
			g.getShortPathAndDSNode(g.getSinkList().get(0));
			for(int m=0;m<g.getSourceList().size();m++)
			{
				Vertex v1=g.getSourceList().get(m);
				if(!v1.isWasConnected())
				{
					
					v1.setWasConnected(true);
					w=true;
					slowI=vSet.iterator();
					while(slowI.hasNext())
					{
						Vertex v2=slowI.next();
						double tRange=this.validTransRangePlusEdgeCapacity(v1, v2,tRadius);
						if(tRange>0)
						{
							//double tV1=this.getgFactorRecv()*v1.getMaxRate()+this.getgFactorSend()*v1.getMaxRate()*Math.pow((tRange), 2);
							Edge e1=new Edge(v1,v2,v1.getBudgetEnergy());
							g.addEdge(e1);
							//double tV2=this.getgFactorRecv()*v2.getMaxRate()+this.getgFactorSend()*v2.getMaxRate()*Math.pow((tRange), 2);
							Edge e2=new Edge(v2,v1,v2.getBudgetEnergy());
							g.addEdge(e2);
						}
					}
				}
			}
		}

		bf.close();
		g.outputFile(fVertex, fEdge);
		return g;
	}
	
	private double validTransRangePlusEdgeCapacity(Vertex v1, Vertex v2, double tRadius)
	{
		double result=0;
		double c1=Math.pow((v1.getxLabel()-v2.getxLabel()),2);
		double c2=Math.pow((v1.getyLabel()-v2.getyLabel()),2);
		double o=Math.pow(tRadius,2);
		if(c1+c2<=o)
		{
			result=c1+c2;
		}
		
		return result;
	}
	
	
	public int outputWeightFile(Graph g,String dataFile,String weightFile,double ratio,double dThreshold)
	{
		int rI=0;
		try
		{
			double[][] result=new double[nodeSum][3];
			double[][] dataSet=this.loadData(dataFile);

			
			for(int i=0;i<g.getSourceList().size();i++)
			{
				int ti=g.getSourceList().get(i).getVerValue();
				result[ti-1][0]=ti;
				result[ti-1][1]=1;
				result[ti-1][2]=0;
			}
			
			ArrayList<Vertex> vSet=new ArrayList<Vertex>();
			Vertex sink=g.getSinkList().get(0);
			vSet.add(sink);
			for(int i=0;i<g.getEdgeList().size();i++)
			{
				Edge tE=g.getEdgeList().get(i);
				Vertex tS=tE.getSource();
				Vertex tT=tE.getTarget();
				if((!vSet.contains(tS))&&(!vSet.contains(tT)))
				{
					double tD=this.computeDataCT(dataSet[tS.getVerValue()-1], dataSet[tT.getVerValue()-1], dThreshold);
					if(tD>=ratio)
					{
						vSet.add(tS);
						rI++;
						vSet.add(tT);
						rI++;
						result[tS.getVerValue()-1][1]=1-tD;
						result[tS.getVerValue()-1][2]=tT.getVerValue();
					}
				}
			}
			
			
			
			PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(weightFile)));
			for(int i=0;i<nodeSum;i++)
			{
				for(int j=0;j<3;j++)
				{
					pw.print(result[i][j]+" ");
				}
				pw.println();
			}
			pw.flush();
			pw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return rI;
	}
	
	
	/*
	 * »¹ÐèÐÞÕý£¡£¡£¡£¡£¡£¡
	 */
	public int outputStarWeightFile(Graph g,String dataFile,String weightFile,double ratio,double dThreshold)
	{
		int rI=0;
		try
		{
			double[][] result=new double[nodeSum][3];
			double[][] dataSet=this.loadData(dataFile);

			
			for(int i=0;i<g.getSourceList().size();i++)
			{
				int ti=g.getSourceList().get(i).getVerValue();
				result[ti-1][0]=ti;
				result[ti-1][1]=1;
				result[ti-1][2]=0;
			}
			
			ArrayList<Vertex> vSet=new ArrayList<Vertex>();
			Vertex sink=g.getSinkList().get(0);
			vSet.add(sink);
			for(int i=0;i<g.getEdgeList().size();i++)
			{
				Edge tE=g.getEdgeList().get(i);
				Vertex tS=tE.getSource();
				Vertex tT=tE.getTarget();
				if((!vSet.contains(tS))&&(!vSet.contains(tT)))
				{
					double tD=this.computeDataCT(dataSet[tS.getVerValue()-1], dataSet[tT.getVerValue()-1], dThreshold);
					if(tD>=ratio)
					{
						vSet.add(tS);
						rI++;
						vSet.add(tT);
						rI++;
						result[tS.getVerValue()-1][1]=1-tD;
						result[tS.getVerValue()-1][2]=tT.getVerValue();
					}
				}
			}
			
			
			
			PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(weightFile)));
			for(int i=0;i<nodeSum;i++)
			{
				for(int j=0;j<3;j++)
				{
					pw.print(result[i][j]+" ");
				}
				pw.println();
			}
			pw.flush();
			pw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return rI;
	}
	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		RealDataHandler rdh=new RealDataHandler();
		rdh.setDataSum(600);
		rdh.setNodeSum(50);
		for(int i=1;i<10;i++)
		{
			rdh.getData("2004-03-0"+i, "test/real/data.txt", "test/real/data-"+(i-1));
		}
		//rdh.outputCTFile("test/real/Ndata.txt","test/real/ct.txt");
		//rdh.outputDataFile("test/real/Ndata.txt", "test/real/labData/50/9/weight/weight-0.txt", "test/real/labData/50/9/data/data-0.txt");
		//rdh.getGraph("test/real/Vertex.txt", "test/real/fVertex.txt", "test/real/fEdge.txt");
	}

}
