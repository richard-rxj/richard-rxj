/**
 * 
 */
package coverage.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
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
	private Network network;
	private List<TimeSlot> timeslots;
	

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
		this.network=network;
		this.timeslots=timeslots;
	}
	
	//return sensors in this coverage
	public Set<Sensor> getSensors() {
		Set<Sensor> result=new HashSet<Sensor>();
		
		for(Set<Sensor> set:this.timeslotBased.values()) {
			result.addAll(set);
		}
		
		return result;
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
		Set<Sensor> selectedSensors=new HashSet<Sensor>();
		
		// get relevent sensors through  BFS approach
		Set<Sensor> needToReach = this.connMap.get(timeslot);
		Set<Sensor> visited = new HashSet<Sensor>();
		visited.add(sensor);
		Queue<Sensor> queue=new LinkedList<Sensor>();
		Queue<Set<Sensor>> queueSet=new LinkedList<Set<Sensor>>();
		queue.add(sensor);
		Set<Sensor> tSelectedSensors=new HashSet<Sensor>();
		tSelectedSensors.add(sensor);
		queueSet.add(tSelectedSensors);
		while(!queue.isEmpty()) {
			Sensor tSensor=queue.poll();
			tSelectedSensors=queueSet.poll();
			
			if(needToReach.contains(tSensor)) {
				selectedSensors=tSelectedSensors;
				break;
			}
			
			for(Sensor neighSensor: this.network.getConnMap().get(tSensor)) {
				if(visited.contains(neighSensor)) {
					continue;
				}
				
				Set<Sensor> neighSet = new HashSet<Sensor> (tSelectedSensors);
				neighSet.add(neighSensor);
				visited.add(neighSensor);
				queue.offer(neighSensor);
				queueSet.offer(neighSet);							
			}
			
		}
		
		
		
		for(Sensor tSensor:selectedSensors) {
			this.add(network, tSensor, timeslot);
		}
			
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
			double result1=0;
			double result2=0;
			Map<TimeSlot, Set<Sensor>> tMap=this.targetBased.get(target);
			if(null==tMap.get(timeslot)) {
				result1=this.func.getResult(tMap.keySet().size()+1)-this.func.getResult(tMap.keySet().size());
				result2=this.func.getResult(1)-this.func.getResult(0);
			} else {
				Set<Sensor> tSet=tMap.get(timeslot);
				result2=this.func.getResult(tSet.size()+1)-this.func.getResult(tSet.size());
			}
			
			result+=ExperimentSetting.coverageWeight*result1+(1-ExperimentSetting.coverageWeight)*result2;

		}
		
		return result;
	}
	
	//compute coverage based on predict budget
	public double computeCoverage() {
		double result=0;
		
		for(Target target: this.targetBased.keySet()) {
			Map<TimeSlot, Set<Sensor>> tMap=this.targetBased.get(target);
			double result1 =this.func.getResult(tMap.keySet().size());
			double result2 = 0;
			for(TimeSlot timeslot:tMap.keySet()) {
				result2+=this.func.getResult(tMap.get(timeslot).size());
			}
			result+=ExperimentSetting.coverageWeight*result1+(1-ExperimentSetting.coverageWeight)*result2;

		}
		
		return result;
	}
	
	//compute coverage based on actual budget
	//also update residual energy  !!
	public double computeCoverageWithoutAccuracy() {
		//actual schedule 
		Map<Target, Map<TimeSlot, Set<Sensor>>> tTargetBased=new HashMap<Target, Map<TimeSlot, Set<Sensor>>>();
		
		for(TimeSlot timeslot: this.timeslotBased.keySet()) {
			 Set<Sensor> chosedSensors=this.timeslotBased.get(timeslot);
			 Set<Sensor> connSensors=new HashSet<Sensor>();
			 connSensors.addAll(this.network.getConnMap().get(this.network.getBase()));
			 boolean stop=false;
			 while(!stop) {
				 stop=true;
				 Set<Sensor> tConnSensors=new HashSet<Sensor>();
				 for(Sensor sensor:connSensors) {
					 if(connSensors.contains(sensor)) {
						 if(sensor.getActualBudget()>=ExperimentSetting.energyCost) {
							 stop=false;
							 sensor.setActualBudget(sensor.getActualBudget()-ExperimentSetting.energyCost);
		
							 Set<Target> targets=network.getS2TMap().get(sensor);
								for(Target target:targets) {
									if(tTargetBased.containsKey(target)) {
										Map<TimeSlot, Set<Sensor>> tMap=tTargetBased.get(target);
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
										tTargetBased.put(target, tMap);
									}
								}
							 
						 }
					 }
				 }
			 }
		 }
		
		
		double result=0;
		
		for(Target target: tTargetBased.keySet()) {
			Map<TimeSlot, Set<Sensor>> tMap=tTargetBased.get(target);
			double result1=this.func.getResult(tMap.keySet().size());
			double result2=0;
			for(TimeSlot timeslot:tMap.keySet()) {
				result2+=this.func.getResult(tMap.get(timeslot).size());
			}
			
			result+=ExperimentSetting.coverageWeight*result1+(1-ExperimentSetting.coverageWeight)*result2;
		}
		
		
		return result;
	}
}


