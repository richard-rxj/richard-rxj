/**
 * 
 */
package coverage.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import coverage.util.Func;

/**
 * @author u4964526
 *
 */
public class Coverage {
	Map<Target, Map<TimeSlot, Set<Sensor>>> targetBased;
	Map<TimeSlot, Set<Sensor>> timeslotBased;
	Func func;
	
	/**
	 * @return the func
	 */
	public Func getFunc() {
		return func;
	}

	/**
	 * @param func the func to set
	 */
	public void setFunc(Func func) {
		this.func = func;
	}

	public Coverage() {
		targetBased=new HashMap<Target, Map<TimeSlot, Set<Sensor>>>();
		timeslotBased=new HashMap<TimeSlot, Set<Sensor>>();
	}
	
	public void add(Network network, Sensor sensor, TimeSlot timeslot) {
		sensor.update();
		Set<Target> targets=network.s2TMap.get(sensor);
		for(Target target:targets) {
			if(targetBased.containsKey(target)) {
				Map<TimeSlot, Set<Sensor>> tMap=targetBased.get(target);
				if(tMap.containsKey(timeslot)) {
					tMap.get(timeslot).add(sensor);
				} else {
					Set<Sensor> tSet=new HashSet<Sensor>();
					tSet.add(sensor);
					tMap.put(timeslot, tSet);
				}
			} else {
				Map<TimeSlot, Set<Sensor>> tMap=new HashMap<TimeSlot, Set<Sensor>>();
				Set<Sensor> tSet=new HashSet<Sensor>();
				tSet.add(sensor);
				tMap.put(timeslot, tSet);
				targetBased.put(target, tMap);
			}
		}
		
		timeslotBased.get(timeslot).add(sensor);
	}
	
	
	public double computeCoverageGain(Sensor sensor, TimeSlot timeslot) {
		//TBD
	}
	
	public double computeCoverage() {
		//TBD
	}
}


