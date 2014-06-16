/**
 * 
 */
package coverage.alg;

import java.util.List;

import coverage.model.Coverage;
import coverage.model.Network;
import coverage.model.TimeSlot;

/**
 * @author user
 *
 */
public abstract class Solution {
	Network network;
	Coverage coverage;
	List<TimeSlot> timeslots;
	
	/**
	 * @param network the network to set
	 */
	public void setNetwork(Network network) {
		this.network = network;
	}

	/**
	 * @param coverage the coverage to set
	 */
	public void setCoverage(Coverage coverage) {
		this.coverage = coverage;
	}

	/**
	 * @param timeslots the timeslots to set
	 */
	public void setTimeslots(List<TimeSlot> timeslots) {
		this.timeslots = timeslots;
	}

	public abstract double schedule();
}
