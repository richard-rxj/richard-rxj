package alg.pc.alg.anu.au;

import java.io.IOException;

import util.pc.alg.anu.au.CommonFacility;
import model.pc.alg.anu.au.Network;

public abstract class Allocate {

	private Network gNet;
	
		
	
	public Network getgNet() {
		return gNet;
	}


	public Allocate(String sensorTxt, double speed) throws RuntimeException, IOException {
		super();
		this.gNet=CommonFacility.getNetwork(sensorTxt, speed);
	}

	
	public abstract void schedule();


	
	public double getNetworkUtility()
	{
		double result=0;
		
		for(int i=0;i<gNet.getSensorSet().size();i++)
		{
			result=result+gNet.getSensorSet().get(i).getUtility();
		}
		
		
		return result;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
