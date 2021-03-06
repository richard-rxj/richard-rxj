package network.dr.alg.anu.au;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class BiNetwork {

	
	
	
	private ArrayList<Node> nList;
	private ArrayList<GateWay> gList;
	
	
	public BiNetwork() {
		nList=new ArrayList<Node>();
		gList=new ArrayList<GateWay>();
		// TODO Auto-generated constructor stub
	}
	
	
	public ArrayList<Node> getnList() {
		return nList;
	}


	public ArrayList<GateWay> getgList() {
		return gList;
	}


	public void saveToFile(String nFile,String gFile) throws FileNotFoundException
	{
		DecimalFormat df=new DecimalFormat("#.0000");
		DecimalFormat df1=new DecimalFormat("#.000000");
		PrintWriter pwNode=new PrintWriter(new OutputStreamWriter(new FileOutputStream(nFile)));
		PrintWriter pwGateWay=new PrintWriter(new OutputStreamWriter(new FileOutputStream(gFile)));

		for(int i=0;i<this.nList.size();i++)
		{
			Node n=this.nList.get(i);
			
			pwNode.println(n.getId()+" "+n.getcData()+" "+n.getrData()+" "+df.format(n.getcEnergy())+" "+df.format(n.getrEnergy())+" "+df1.format(n.gethEnergy())+" "+df.format(n.getX())+" "+df.format(n.getY()));
			pwNode.flush();
		}
		
		for(int i=0;i<this.gList.size();i++)
		{
			GateWay g=this.gList.get(i);
			
			pwGateWay.println(g.getId()+" "+df.format(g.getX())+" "+df.format(g.getY()));
			pwGateWay.flush();
		}
		
		pwNode.close();
		pwGateWay.close();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
