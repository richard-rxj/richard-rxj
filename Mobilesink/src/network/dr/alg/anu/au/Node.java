package network.dr.alg.anu.au;

import java.util.ArrayList;

import dr.alg.anu.au.ExperimentSetting;
import dr.alg.anu.au.NewTourDesign;

public class Node implements Comparable<Node> {

	private int id;
	private int name;  //0-------node      1-------gateway
	private int active; //0-------non-select 1--------select
	private double rEnergy =0;
	private double cEnergy =100;  //battery capacity 100 Jules
	private double hEnergy=0;
	private double rData=0;
	private double cData=0;
	private double gRate = 1000;
	private double tRate = 1000;   // transmission rate = generation rate
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
	private double survivalTime=0; // new
	private double totalSojournTime=0; // new
	private double benefitGain=0;     //new
	private double utilityGain=0;      //new
	
	

	
	


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
	
	
	public double calcUploadTime(double movingTime, double eConsumption)
	{
		double result=0;
		double result1=0;
		result1 = this.rData / this.tRate;
		if(eConsumption*this.tRate>this.hEnergy)
		{
			result = (this.rEnergy+this.hEnergy*movingTime)/(eConsumption*this.tRate-this.hEnergy);
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
		result = 1-((this.cData-this.rData)/this.cData)*((this.cData-this.rData)/this.cData);
		this.fWeight = result;
		return result;
	}
	
	
	public double updateLweight()
	{
		
		return this.lWeight;
	}
	
	
	public double getSurvivalTime() {
		return survivalTime;
	}

	public void setSurvivalTime(double survivalTime) {
		this.survivalTime = survivalTime;
	}

	public double getTotalSojournTime() {
		return totalSojournTime;
	}

	public void setTotalSojournTime(double totalSojournTime) {
		this.totalSojournTime = totalSojournTime;
		this.benefitGain=totalSojournTime*this.tRate;
		double tA=ExperimentSetting.utilityA;
		double tTourTime=ExperimentSetting.tourTime;
		double tPrevious=1-Math.pow((1-this.totalSojournTime/tTourTime),tA);
		this.utilityGain=tPrevious;

	}

	public double getBenefitGain() {
		return benefitGain;
	}

	public void setBenefitGain(double benefitGain) {
		this.benefitGain = benefitGain;
	}

	public double getUtilityGain() {
		return utilityGain;
	}

	public void setUtilityGain(double utilityGain) {
		this.utilityGain = utilityGain;
	}

	
	public double calcSurvivalTime(double movingTime, double eConsumption)
	{
		double result=0;
		double tConsumption=eConsumption*this.tRate;
		if(tConsumption<ExperimentSetting.minEConsumption)
			tConsumption=ExperimentSetting.minEConsumption;
		result = (this.rEnergy+this.hEnergy*movingTime)/(eConsumption*this.tRate-this.hEnergy);
		this.setSurvivalTime(result);
		return result;
	}
	
	
	public double calcUtilityGain(double sojournTime)
	{
		double result=0;
		double tA=ExperimentSetting.utilityA;
		double tTourTime=ExperimentSetting.tourTime;
		double tPrevious=1-Math.pow((1-this.totalSojournTime/tTourTime),tA);
		double tNew=1-Math.pow((1-(this.totalSojournTime+sojournTime)/tTourTime),tA);
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

}