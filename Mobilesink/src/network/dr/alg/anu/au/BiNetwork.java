package network.dr.alg.anu.au;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
		PrintWriter pwNode=new PrintWriter(new OutputStreamWriter(new FileOutputStream(nFile)));
		PrintWriter pwGateWay=new PrintWriter(new OutputStreamWriter(new FileOutputStream(gFile)));

		for(int i=0;i<this.nList.size();i++)
		{
			Node n=this.nList.get(i);
			
			pwNode.println(n.getId()+" "+n.getcData()+" "+n.getrData()+" "+n.getcEnergy()+" "+n.getrEnergy()+" "+n.gethEnergy()+" "+n.getX()+" "+n.getY());
			pwNode.flush();
		}
		
		for(int i=0;i<this.gList.size();i++)
		{
			GateWay g=this.gList.get(i);
			
			pwGateWay.println(g.getId()+" "+g.getX()+" "+g.getY());
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
