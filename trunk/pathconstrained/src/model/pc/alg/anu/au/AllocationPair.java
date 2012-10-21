package model.pc.alg.anu.au;

public class AllocationPair {

	private int slotID;
	private double transRate;
	
	
	public int getSlotID() {
		return slotID;
	}


	public void setSlotID(int slotID) {
		this.slotID = slotID;
	}


	public double getTransRate() {
		return transRate;
	}


	public void setTransRate(double transRate) {
		this.transRate = transRate;
	}


	
	
	public AllocationPair(int slotID, double transRate) {
		super();
		this.slotID = slotID;
		this.transRate = transRate;
	}

	
	public AllocationPair() {
		super();
	}
	

	@Override
	public String toString() {
		return "AllocationPair [slotID=" + slotID + ", transRate=" + transRate
				+ "]";
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
