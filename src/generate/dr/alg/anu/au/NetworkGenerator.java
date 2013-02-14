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

import dr.alg.anu.au.ExperimentSetting;



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
			n.setX(NetworkGenerator.ran.nextDouble()*ExperimentSetting.xRange);
			n.setY(NetworkGenerator.ran.nextDouble()*ExperimentSetting.yRange);
			n.setcData(0);
			n.setrData(0);
			n.sethEnergy(ExperimentSetting.harvestRate[0]+NetworkGenerator.ran.nextDouble()*(ExperimentSetting.harvestRate[1]-ExperimentSetting.harvestRate[0]));
		
			result.getnList().add(n);
		}

		for(int i=nSize;i<nSize+gSize;i++)
		{
			GateWay g=new GateWay(i);
			g.setX(NetworkGenerator.ran.nextDouble()*ExperimentSetting.xRange);
			g.setY(NetworkGenerator.ran.nextDouble()*ExperimentSetting.yRange);
			
			result.getgList().add(g);
		}
		
		
		return result;
	}
	
	
	
	public static BiNetwork firstInitialFromFile(String nFile, String gFile, int gatewayLimit, double transRange) throws IOException
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
			n.setcData(0);
			n.setrData(0);
			n.sethEnergy(ExperimentSetting.harvestRate[0]+NetworkGenerator.ran.nextDouble()*(ExperimentSetting.harvestRate[1]-ExperimentSetting.harvestRate[0]));
			n.setX(Double.parseDouble(b[1]));
			n.setY(Double.parseDouble(b[2]));
			
			result.getnList().add(n);
		}
		
		
		BufferedReader gReader=new BufferedReader(new InputStreamReader(new FileInputStream(gFile)));
		
		int ttt=0;
		while(ttt<gatewayLimit)
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
				if(tD<=transRange)
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
	
	
	
	
	public static BiNetwork createFromFile(String nFile, String gFile,int gatewayLimit,double transRange) throws IOException
	{
		BiNetwork result=new BiNetwork();
		String tempString=null;
		
		
		BufferedReader nReader=new BufferedReader(new InputStreamReader(new FileInputStream(nFile)));
	
		while((tempString=nReader.readLine())!=null)
		{
			String[] b=tempString.split(" ");
			Node n=new Node(Integer.parseInt(b[0]));
			n.setcData(Double.parseDouble(b[1]));
			n.setrData(Double.parseDouble(b[2]));
			n.setcEnergy(Double.parseDouble(b[3]));
			n.setrEnergy(Double.parseDouble(b[4]));
			n.sethEnergy(Double.parseDouble(b[5]));
			n.setX(Double.parseDouble(b[6]));
			n.setY(Double.parseDouble(b[7]));
			
			result.getnList().add(n);
		}
		
		
		BufferedReader gReader=new BufferedReader(new InputStreamReader(new FileInputStream(gFile)));
		
		int gNum=0;
		while(gNum<gatewayLimit)
		{
			gNum++;
			tempString=gReader.readLine();
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
				if(tD<=transRange)
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
		int[] networkSizeSet={100,200,300,400,500,600,700,800,900,1000,2000};
		int[] gatewayLimitSet={50,50,50,50,50,50,50,50,50,50,50};  
		int[] transRangeSet={20,20,20,20,20,20,20,20,20,20,20};
		int cishu=20;
		
		
		String tFileName="test/new/topology/";
		File tf=new File(tFileName);
		if(!tf.exists())
		{
			tf.mkdirs();
		}
		
		for(int i=0;i<networkSizeSet.length;i++)
		{
			int tnetworkSize=networkSizeSet[i];
			int tgatewayLimit=tnetworkSize;
			double ttransRange=transRangeSet[i];
			for(int j=0;j<cishu;j++)
			{
				tFileName="test/new/topology/";
				String nFile=tFileName+"node-"+tnetworkSize+"-"+j+".txt";
				String gFile=tFileName+"gateway-"+tnetworkSize+"-"+j+".txt";
				tFileName="test/originTopology/";
				String nOriginFile=tFileName+"node-"+tnetworkSize+"-"+j+".txt";
				String gOriginFile=tFileName+"gateway-"+tnetworkSize+"-"+j+".txt";
				BiNetwork bNet=NetworkGenerator.firstInitialFromFile(nOriginFile, gOriginFile,tgatewayLimit,ttransRange);
				bNet.saveToFile(nFile, gFile);
			}
		}
	}
	
	
}
