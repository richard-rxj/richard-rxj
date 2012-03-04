/* -------------------------
 * NetworkGenerator.java
 * -------------------------
 *
 * Original Author:  Zichuan Xu.
 *
 * The node class V need to have four parameters: id, x location, y location, rest energy and current energy.
 *
 * Changes
 * -------
 * 2-Dec-2011 : Initial revision (GB);
 *
 */

package generate.dr.alg.anu.au;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import dr.alg.anu.au.TourDesign;

import network.dr.alg.anu.au.BiNetwork;
import network.dr.alg.anu.au.GateWay;
import network.dr.alg.anu.au.Node;







public class NetworkGenerator   {
	// ~ Instance fields
	// --------------------------------------------------------
	private static Random ran=new Random();
	
	public static BiNetwork generateNetwork(int nSize, int gSize)
	{
		BiNetwork result=new BiNetwork();
		
		for(int i=0;i<nSize;i++)
		{
			Node n=new Node(i);
			n.setX(NetworkGenerator.ran.nextDouble()*TourDesign.xRange);
			n.setY(NetworkGenerator.ran.nextDouble()*TourDesign.yRange);
			n.setcData(TourDesign.gRate*TourDesign.tourTime);
			n.setrData(TourDesign.gRate*TourDesign.tourTime);
			n.sethEnergy(TourDesign.harvestRate[0]+NetworkGenerator.ran.nextDouble()*(TourDesign.harvestRate[1]-TourDesign.harvestRate[0]));
		
			result.getnList().add(n);
		}

		for(int i=nSize;i<nSize+gSize;i++)
		{
			GateWay g=new GateWay(i);
			g.setX(NetworkGenerator.ran.nextDouble()*TourDesign.xRange);
			g.setY(NetworkGenerator.ran.nextDouble()*TourDesign.yRange);
			
			result.getgList().add(g);
		}
		
		
		return result;
	}
	
	
	
	public static BiNetwork firstInitialFromFile(String nFile, String gFile) throws IOException
	{
		BiNetwork result=new BiNetwork();
		String tempString=null;
		
		
		BufferedReader nReader=new BufferedReader(new InputStreamReader(new FileInputStream(nFile)));
	
		int nnn=0;
		
		while((tempString=nReader.readLine())!=null)
		{
			nnn++;
			String[] b=tempString.split(" ");
			Node n=new Node(nnn);
			n.setcData(TourDesign.gRate*TourDesign.tourTime);
			n.setrData(TourDesign.gRate*TourDesign.tourTime);
			n.sethEnergy(TourDesign.harvestRate[0]+NetworkGenerator.ran.nextDouble()*(TourDesign.harvestRate[1]-TourDesign.harvestRate[0]));
			n.setX(Double.parseDouble(b[1]));
			n.setY(Double.parseDouble(b[2]));
			
			result.getnList().add(n);
		}
		
		
		BufferedReader gReader=new BufferedReader(new InputStreamReader(new FileInputStream(gFile)));
		
		int ttt=0;
		while(ttt<TourDesign.gatewayLimit)
		{
			ttt++;
			nnn++;
			tempString=gReader.readLine();
			String[] b=tempString.split(" ");
			GateWay g=new GateWay(nnn);
			g.setX(Double.parseDouble(b[1]));
			g.setY(Double.parseDouble(b[2]));
			
			for(int i=0;i<result.getnList().size();i++)
			{
				Node n=result.getnList().get(i);
				
				double tX=g.getX()-n.getX();
				double tY=g.getY()-n.getY();
				double tD=Math.sqrt(Math.pow(tX, 2)+Math.pow(tY, 2));
				if(tD<=TourDesign.transmissionRange)
				{
					g.addNeighborNode(n);
				}
				
			}
			
			result.getgList().add(g);
		}
		
		nReader.close();
		gReader.close();
		return result;
	}
	
	
	
	
	public static BiNetwork createFromFile(String nFile, String gFile) throws IOException
	{
		BiNetwork result=new BiNetwork();
		String tempString=null;
		
		
		BufferedReader nReader=new BufferedReader(new InputStreamReader(new FileInputStream(nFile)));
	
		while((tempString=nReader.readLine())!=null)
		{
			String[] b=tempString.split(" ");
			Node n=new Node(Integer.parseInt(b[0]));
			//n.setcData(Double.parseDouble(b[1]));
			n.setcData(TourDesign.gRate*TourDesign.tourTime);
			n.setrData(TourDesign.gRate*TourDesign.tourTime);
			//n.setrData(Double.parseDouble(b[2]));
			n.setcEnergy(Double.parseDouble(b[3]));
			n.setrEnergy(Double.parseDouble(b[4]));
			n.sethEnergy(Double.parseDouble(b[5]));
			n.setX(Double.parseDouble(b[6]));
			n.setY(Double.parseDouble(b[7]));
			
			result.getnList().add(n);
		}
		
		
		BufferedReader gReader=new BufferedReader(new InputStreamReader(new FileInputStream(gFile)));
		
		while((tempString=gReader.readLine())!=null)
		{
			String[] b=tempString.split(" ");
			GateWay g=new GateWay(Integer.parseInt(b[0]));
			g.setX(Double.parseDouble(b[1]));
			g.setY(Double.parseDouble(b[2]));
			
			for(int i=0;i<result.getnList().size();i++)
			{
				Node n=result.getnList().get(i);
				
				double tX=g.getX()-n.getX();
				double tY=g.getY()-n.getY();
				double tD=Math.sqrt(Math.pow(tX, 2)+Math.pow(tY, 2));
				if(tD<=TourDesign.transmissionRange)
				{
					g.addNeighborNode(n);
				}
				
			}
			
			result.getgList().add(g);
		}
		
		nReader.close();
		gReader.close();
		return result;
	}
	
	
	
	public static void main(String[] args) throws IOException
	{
		int[] networkSizeSet={100,200,300,400,500,600};
		int[] transRangeSet={23,16,14,12,10,10};
		int cishu=15;
		
		
		String tFileName="test/Topology/";
		File tf=new File(tFileName);
		if(!tf.exists())
		{
			tf.mkdirs();
		}
		
		for(int i=0;i<networkSizeSet.length;i++)
		{
			int networkSize=networkSizeSet[i];
			TourDesign.transmissionRange=transRangeSet[i];
			for(int j=0;j<cishu;j++)
			{
				tFileName="test/Topology/";
				String nFile=tFileName+"node-"+networkSize+"-"+j+".txt";
				String gFile=tFileName+"gateway-"+networkSize+"-"+j+".txt";
				tFileName="test/originTopology/";
				String nOriginFile=tFileName+"node-"+networkSize+"-"+j+".txt";
				String gOriginFile=tFileName+"gateway-"+networkSize+"-"+j+".txt";
				BiNetwork bNet=NetworkGenerator.firstInitialFromFile(nOriginFile, gOriginFile);
				bNet.saveToFile(nFile, gFile);
			}
		}
		
	}
	
	
}
