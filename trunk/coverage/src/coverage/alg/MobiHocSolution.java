package coverage.alg;

import java.util.Collections;
import java.util.PriorityQueue;

import coverage.model.ChoicePair;
import coverage.model.Coverage;
import coverage.model.Sensor;
import coverage.model.TimeSlot;
import coverage.util.ExperimentSetting;

public class MobiHocSolution extends Solution {

	@Override
	public Coverage schedule() {
		// TODO Auto-generated method stub
		Coverage coverage=new Coverage();
		coverage.initial(this.network, this.timeslots, this.func);
		
		double currentWeight=ExperimentSetting.coverageWeight;
		ExperimentSetting.coverageWeight=1;
		
		int loop=0;
		double debugGain=0;
		
		for(TimeSlot timeslot:this.timeslots) {
			PriorityQueue<ChoicePair> queue=new PriorityQueue<ChoicePair>(this.network.getSensors().size()*this.timeslots.size(),Collections.reverseOrder());		
			while(true) {
				queue.clear();
				
				loop++;
				ExperimentSetting.gLog.severe(String.format("Centralized-Iteration<%d>----begin", loop));
				
				for(Sensor sensor:this.network.getSensors()) {
					
					if(sensor.getPredictBudget()<ExperimentSetting.energyCost) continue;
					
				
					double gain=coverage.computeCoverageGain(this.network, sensor, timeslot, true);
					if(gain>0) {
						ChoicePair pair=new ChoicePair();
						pair.setSensor(sensor);
						pair.setTimeslot(timeslot);
						pair.setCoverageGain(gain);
						queue.offer(pair);
					}
					
				}
				
				if(null!=queue.peek()) {
					ChoicePair selected=queue.peek();
					ExperimentSetting.gLog.info(String.format("sensor<%d,%.2f>---->timeslot<%d>----Gain<%.4f>", 
							selected.getSensor().getId(), selected.getSensor().getPredictBudget(), 
							selected.getTimeslot().getId(), selected.getCoverageGain()));
					debugGain+=selected.getCoverageGain();
					coverage.add(this.network, selected.getSensor(), selected.getTimeslot());
				} else {
					break;
				}
			}
		}
		
		ExperimentSetting.coverageWeight=currentWeight;
		
		ExperimentSetting.gLog.severe(String.format("Debug Gain is %.2f", debugGain));
		return coverage;
	}

}
