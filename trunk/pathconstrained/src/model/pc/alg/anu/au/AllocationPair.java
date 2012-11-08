package model.pc.alg.anu.au;

public class AllocationPair {

	private int slotID;
	private double slotData;
	private double energyCost=1;
	private double perCost;
	
	
	public int getSlotID() {
		return slotID;
	}


	public void setSlotID(int slotID) {
		this.slotID = slotID;
	}




	
	
	public double getSlotData() {
		return slotData;
	}


	public void setSlotData(double slotData) {
		this.slotData = slotData;
		this.perCost=this.slotData/this.energyCost;
	}


	public void setEnergyCost(double energyCost) {
		this.energyCost = energyCost;
		this.perCost=this.slotData/this.energyCost;
	}


	public double getEnergyCost() {
		return energyCost;
	}


	public double getPerCost() {
		return perCost;
	}


	public AllocationPair(int slotID, double slotData, double energyCost) {
		super();
		this.slotID = slotID;
		this.slotData = slotData;
		this.energyCost=energyCost;
	}

	
	public AllocationPair() {
		super();
	}
	

	@Override
	public String toString() {
		return "AllocationPair [slotID=" + slotID + ", transRate=" + slotData
				+ "]";
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
