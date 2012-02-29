package network.dr.alg.anu.au;

import java.util.ArrayList;

public class Node implements Comparable<Node> {

	private int id;
	private int name;  //0-------node      1-------gateway
	private int active; //0-------non-select 1--------select
	private double rEnergy =0;
	private double cEnergy =100;  //battery capacity 100 Jules
	private double hEnergy=0;
	private double rData=0;
	private double cData=0;
	private double gRate = 500;
	private double tRate = 10000;
	private double xLabel = 0;
	private double yLabel = 0;
	private double X=0;
	private double Y=0;
	private double tTime = 0;
	private double uploadTime =0;
	private double lWeight= 1;
	private double fWeight= 1;
	private double lBenefit = 0;
	private double fBenefit = 0;

	
	


	public Node(int id) {
		this.id = id;
		this.name = id;
	}

	public Node(NodeInitialParameters ni) {
		this.id = ni.id;
		this.X = ni.x;
		this.Y = ni.y;
		this.rEnergy = ni.re;
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
		this.rEnergy = v.getrEnergy();
	}
	
	public Node(Node another){
		this.id = another.id;
		this.cEnergy = another.cEnergy;
		this.name = another.name;
		this.X = another.X;
		this.Y = another.Y;
		this.rEnergy = another.rEnergy;
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


	public double getrEnergy() {
		return rEnergy;
	}

	public void setrEnergy(double rEnergy) {
		this.rEnergy = rEnergy;
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

	public void setgRate(double gRate) {
		this.gRate = gRate;
	}

	public double gettRate() {
		return tRate;
	}

	public void settRate(double tRate) {
		this.tRate = tRate;
	}

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

	public double getlWeight() {
		return lWeight;
	}

	public void setlWeight(double lWeight) {
		this.lWeight = lWeight;
	}

	public double getfWeight() {
		return fWeight;
	}

	public void setfWeight(double fWeight) {
		this.fWeight = fWeight;
	}

	public double getlBenefit() {
		return lBenefit;
	}

	public void setlBenefit(double lBenefit) {
		this.lBenefit = lBenefit;
	}

	public double getfBenefit() {
		return fBenefit;
	}

	public void setfBenefit(double fBenefit) {
		this.fBenefit = fBenefit;
	}

	public int compareTo(Node vertex2) {
		// TODO Auto-generated method stub
		if (rEnergy > vertex2.getrEnergy()) {
			return 1;
		} else if (rEnergy < vertex2.getrEnergy()) {
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
		return X;
	}

	public void setX(double x) {
		X = x;
	}

	public double getY() {
		return Y;
	}

	public void setY(double y) {
		X = y;
	}
	
	
	public double calcUploadTime(double movingTime, double eConsumption)
	{
		double result=0;
		double result1=0;
		result = (this.rEnergy+this.hEnergy*movingTime)/(eConsumption*this.tRate-this.hEnergy);
		result1 = this.rData / this.tRate;
		if(result>result1)
		{
			result=result1;
		}
		this.uploadTime=result;
		return result;
	}
	
	public double calcLBenefit(double tSojournTime)
	{
		double result=0;
		
		result=this.lWeight*this.tRate*tSojournTime;
		
		this.lBenefit=result;
		return result;
		
	}
	
	public double calcFBenefit(double tSojournTime)
	{
		double result=0;
		
		result=this.fWeight*this.tRate*tSojournTime;
		
		this.fBenefit=result;
		return result;
		
	}
	
	
	public double updateFweight()
	{
		double result=0;
		result = 1-(this.rData/this.cData)*(this.rData/this.cData);
		this.fWeight = result;
		return result;
	}
	
	
	public double updateLweight()
	{
		
		return this.lWeight;
	}
	
	
	@Override
	public boolean equals(Object another) {
		if(this.id == ((Node)another).getId())
			return true;
		else
			return false;
	}

}