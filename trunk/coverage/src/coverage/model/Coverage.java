/**
 * 
 */
package coverage.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import coverage.util.ExperimentSetting;
import coverage.util.Func;

/**
 * @author u4964526
 *
 */
public class Coverage {
	private Map<Target, Map<TimeSlot, Set<Sensor>>> targetBased;
	private Map<TimeSlot, Set<Sensor>> timeslotBased;
	private Map<TimeSlot, Set<Sensor>> connMap;
	private Func func;
	
	

	public Coverage() {
		targetBased=new HashMap<Target, Map<TimeSlot, Set<Sensor>>>();
		timeslotBased=new HashMap<TimeSlot, Set<Sensor>>();
		connMap=new HashMap<TimeSlot, Set<Sensor>>();
	}
	
	// initial empty trees and set function
	public void initial(Network network, List<TimeSlot> timeslots, Func criterion) {
		for(TimeSlot timeslot: timeslots) {
			Set<Sensor> tSet=new HashSet<Sensor>();
			tSet.addAll(network.getConnMap().get(network.getBase()));
			this.connMap.put(timeslot, tSet);
		}
		this.func=criterion;
	}
	
	//return sensors in this coverage
	public Set<Sensor> getSensors() {
		
	}
	
	public void add(Network network, Sensor sensor, TimeSlot timeslot) {
		//only update predictBudget
		sensor.setPredictBudget(sensor.getPredictBudget()-ExperimentSetting.energyCost);
		
		Set<Target> targets=network.getS2TMap().get(sensor);
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
		
		if(timeslotBased.containsKey(timeslot)) {
			timeslotBased.get(timeslot).add(sensor);
		} else {
			Set<Sensor> tSet=new HashSet<Sensor>();
			tSet.add(sensor);
			timeslotBased.put(timeslot, tSet);
		}
		
		
		connMap.get(timeslot).addAll(network.getConnMap().get(sensor));
	}
	
	//for sensor and relevant sensors to make it connected
	public void addPath(Network network, Sensor sensor, TimeSlot timeslot) {
		TBD
	}
	
	public double computeCoverageGain(Network network, Sensor sensor, TimeSlot timeslot, boolean connectedReq) {
		double result=0;
		
		if(connectedReq) {
			if(null==this.connMap.get(timeslot)||!this.connMap.get(timeslot).contains(sensor)) {
				return 0;
			}
		}
		
		if(null!=this.timeslotBased.get(timeslot)&&this.timeslotBased.get(timeslot).contains(sensor)) {
			return 0;
		}
		
		for(Target target: network.getS2TMap().get(sensor)) {
			Map<TimeSlot, Set<Sensor>> tMap=this.targetBased.get(target);
			if(null==tMap.get(timeslot)) {
				result+=this.func.getResult(tMap.keySet().size()+1)-this.func.getResult(tMap.keySet().size());
				result+=this.func.getResult(1)-this.func.getResult(0);
			} else {
				Set<Sensor> tSet=tMap.get(timeslot);
				result+=this.func.getResult(tSet.size()+1)-this.func.getResult(tSet.size());
			}
			
		}
		
		return result;
	}
	
	//compute coverage based on predict budget
	public double computeCoverage() {
		double result=0;
		
		for(Target target: this.targetBased.keySet()) {
			Map<TimeSlot, Set<Sensor>> tMap=this.targetBased.get(target);
			result+=this.func.getResult(tMap.keySet().size());
			for(TimeSlot timeslot:tMap.keySet()) {
				result+=this.func.getResult(tMap.get(timeslot).size());
			}
		}
		
		return result;
	}
	
	//compute coverage based on actual budget
	//also update residual energy  !!
	public double computeCoverageWithoutAccuracy() {
		
	}
}


