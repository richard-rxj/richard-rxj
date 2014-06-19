/**
 * 
 */
package coverage.alg;

import java.util.List;

import coverage.model.Coverage;
import coverage.model.Network;
import coverage.model.TimeSlot;
import coverage.util.Func;

/**
 * @author user
 *
 */
public abstract class Solution {
	Network network;
	List<TimeSlot> timeslots;
	Func func;
	
	/**
	 * @param func the func to set
	 */
	public void setFunc(Func func) {
		this.func = func;
	}


	/**
	 * @param network the network to set
	 */
	public void setNetwork(Network network) {
		this.network = network;
	}


	/**
	 * @param timeslots the timeslots to set
	 */
	public void setTimeslots(List<TimeSlot> timeslots) {
		this.timeslots = timeslots;
	}

	public abstract Coverage schedule();
}
