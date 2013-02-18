package network.dr.alg.anu.au;

import java.util.ArrayList;

import dr.alg.anu.au.ExperimentSetting;


public class Node implements Comparable<Node> {

	private int id;
	private int name;  //0-------node      1-------gateway
	private int active; //0-------non-select 1--------select
	private double aEnergy =0;
	private double cEnergy =10000;  //battery capacity 10000 Jules
	private double hEnergy=0;
	private double rData=ExperimentSetting.gRate*ExperimentSetting.tourTime;
	private double cData=0;
	private double gRate = ExperimentSetting.gRate;
	private double tRate = ExperimentSetting.tRate;   
	private double xLabel = 0;
	private double yLabel = 0;
	private double X=0;
	private double Y=0;
	private double tTime = 0;
	private double uploadTime =0;
	private double totalSojournTime=0; // new
	private double totalUtility=0;      //new
	
	

	
	


	public Node(int id) {
		this.id = id;
		this.name = id;
	}

	public Node(NodeInitialParameters ni) {
		this.id = ni.id;
		this.X = ni.x;
		this.Y = ni.y;
		this.aEnergy = ni.re;
		this.cEnergy = ni.ce;
	}

	public Node(Node v, int id) {
		this.id = id;
		this.name = v.getId();
		this.xLabel = v.getxLabel();
		this.yLabel = v.getyLabel();
		this.X = v.getX();
		this.Y = v.getY();
		this.cEnergy = v.getcEnergy();
		this.aEnergy = v.getaEnergy();
	}
	
	public Node(Node another){
		this.id = another.id;
		this.cEnergy = another.cEnergy;
		this.name = another.name;
		this.X = another.X;
		this.Y = another.Y;
		this.aEnergy = another.aEnergy;
	}

	
	
	
	
	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}


	public double getaEnergy() {
		return aEnergy;
	}

	public void setaEnergy(double aEnergy) {
		this.aEnergy = aEnergy;
	}

	public double getcEnergy() {
		return cEnergy;
	}

	public void setcEnergy(double cEnergy) {
		this.cEnergy = cEnergy;
	}

	public double gethEnergy() {
		return hEnergy;
	}

	public void sethEnergy(double hEnergy) {
		this.hEnergy = hEnergy;
	}

	public double getrData() {
		return rData;
	}

	public void setrData(double rData) {
		this.rData = rData;
	}

	public double getcData() {
		return cData;
	}

	public void setcData(double cData) {
		this.cData = cData;
	}

	public double getgRate() {
		return gRate;
	}

//	public void setgRate(double gRate) {
//		this.gRate = gRate;
//	}

	public double gettRate() {
		return tRate;
	}

//	public void settRate(double tRate) {
//		this.tRate = tRate;
//	}

	public double getxLabel() {
		return xLabel;
	}

	public void setxLabel(double xLabel) {
		this.xLabel = xLabel;
	}

	public double getyLabel() {
		return yLabel;
	}

	public void setyLabel(double yLabel) {
		this.yLabel = yLabel;
	}

	

	public double gettTime() {
		return tTime;
	}

	public void settTime(double tTime) {
		this.tTime = tTime;
	}




	public double getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(double uploadTime) {
		this.uploadTime = uploadTime;
	}

	

	public int compareTo(Node vertex2) {
		// TODO Auto-generated method stub
		if (aEnergy > vertex2.getaEnergy()) {
			return 1;
		} else if (aEnergy < vertex2.getaEnergy()) {
			return -1;
		} else
			return 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String toString() {
		return "node " + id;
	}

	public double getX() {
		return this.X;
	}

	public void setX(double x) {
		this.X = x;
	}

	public double getY() {
		return this.Y;
	}

	public void setY(double y) {
		this.Y = y;
	}
	
	
	public double calcUploadTime(double movingTime, double eConsumption)   //for GLOBECOM 2013
	{
		double result=0;
		double result1=0;
		//result1 = (this.rData+this.gRate*movingTime) / (this.tRate-this.gRate);
		result1=this.rData/this.tRate;
		double tEnergy=eConsumption*this.tRate;
		if(tEnergy>this.hEnergy)
		{
			result = (this.aEnergy+this.hEnergy*movingTime)/(tEnergy-this.hEnergy);
		
			//begin of debug
			if(result<0)
			{
				System.out.println("nodeid:"+this.id+"--hVate("+this.hEnergy+")--rData("+this.rData+")--uploadtime("+result1+")--rEnergy("+this.aEnergy+")--survivalTime("+result+")");
			}
			//end of debug
			
			if(result>result1)
			{
				result=result1;
			}
		
		}
		else
			result=result1;
		
		
		this.uploadTime=result;
		
		
		
		return result;
	}
	



	public double getTotalSojournTime() {
		return totalSojournTime;
	}

	public void setTotalSojournTime(double totalSojournTime) {
		this.totalSojournTime = totalSojournTime;
		double tA=ExperimentSetting.utilityA;
		double tTourTime=ExperimentSetting.tourTime;
		double tPrevious=Math.sqrt(this.totalSojournTime*this.tRate/(tTourTime*this.gRate));
		this.totalUtility=tPrevious;

	}



	public double getTotalUtility() {
		return this.totalUtility;
	}


	
	
	public double calcUtilityGain(double sojournTime)   //modify for GLOBECOM 2013
	{
		double result=0;
		double tA=ExperimentSetting.utilityA;
		double tTourTime=ExperimentSetting.tourTime;
		double tPrevious=Math.sqrt(this.totalSojournTime*this.tRate/(tTourTime*this.gRate));
		
		double tUploadTime=this.uploadTime;
		if(tUploadTime>sojournTime)
		{
			tUploadTime=sojournTime;
		}
		
		double tNew=Math.sqrt((this.totalSojournTime+tUploadTime)*this.tRate/(tTourTime*this.gRate));
		
		
		result=tNew-tPrevious;
		return result;
	}
	
	
	@Override
	public boolean equals(Object another) {
		if(this.id == ((Node)another).getId())
			return true;
		else
			return false;
	}

	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Node> l1=new ArrayList<Node>();
		ArrayList<Node> l2=new ArrayList<Node>();
		ArrayList<Node> l3=new ArrayList<Node>();
		
		Node n1=new Node(1);
		Node n2=new Node(2);
		Node n3=new Node(3);
		Node n4=new Node(4);
		Node n5=new Node(5);
		Node n6=new Node(6);
		
		l1.add(n1);
		l1.add(n2);
		l1.add(n3);
		
		l2.add(n3);
		l2.add(n4);
		l2.add(n5);
		//l2.retainAll(l1);
	
		System.out.println(l2);
		//l2.removeAll(l2);
		l3.addAll(l2);
		System.out.println(l3);
		//l3.removeAll(l2);
		//l3.addAll(l2);
		l2.clear();
		System.out.println(l2);
		System.out.println(l3);
	}
}