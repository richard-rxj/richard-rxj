package model.pc.alg.anu.au;

public class AllocationPair {

	private int slotID;
	private double slotData;
	
	
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
	}


	public AllocationPair(int slotID, double slotData) {
		super();
		this.slotID = slotID;
		this.slotData = slotData;
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
