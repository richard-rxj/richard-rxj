/**
 * 
 */
package coverage.alg;

import java.util.Collections;
import java.util.PriorityQueue;

import coverage.model.ChoicePair;
import coverage.model.Coverage;
import coverage.model.Sensor;
import coverage.model.TimeSlot;
import coverage.util.ExperimentSetting;

/**
 * @author user
 *
 */
public class ICDCSSolution extends Solution {

	/* (non-Javadoc)
	 * @see coverage.alg.Solution#schedule()
	 */
	@Override
	public Coverage schedule() {
		// TODO Auto-generated method stub
		Coverage coverage=new Coverage();
		coverage.initial(this.network, this.timeslots, this.func);
		
		ExperimentSetting.gLog.info(String.format("*****ICDCS-%s-network<%d>-target<%d> begin", this.func.getClass().getName(), this.network.getSensors().size(), this.network.getTargets().size()));

		
		PriorityQueue<ChoicePair> queue=new PriorityQueue<ChoicePair>(this.network.getSensors().size()*this.timeslots.size(),Collections.reverseOrder());

		int loop=0;
		double debugGain=0;
		
		while(true) {
			queue.clear();
			
			loop++;
			ExperimentSetting.gLog.severe(String.format("ICDCS-Iteration<%d>----begin", loop));
			
			for(Sensor sensor:this.network.getSensors()) {
				
				if(sensor.getPredictBudget()<ExperimentSetting.energyCost) continue;
				if(!sensor.isConnectedToBaseStation()) continue;
				
				for (TimeSlot timeslot:this.timeslots) {
					double gain=coverage.computeCoverageGain(this.network, sensor, timeslot, false);
					if(gain>0) {
						ChoicePair pair=new ChoicePair();
						pair.setSensor(sensor);
						pair.setTimeslot(timeslot);
						pair.setCoverageGain(gain);
						queue.offer(pair);
					}
				}
			}
			
			if(null!=queue.peek()) {
				ChoicePair selected=queue.peek();
				ExperimentSetting.gLog.info(String.format("Add Path: sensor<%d,%.2f>---->timeslot<%d>----Gain<%.4f>", 
						selected.getSensor().getId(), selected.getSensor().getPredictBudget(), 
						selected.getTimeslot().getId(), selected.getCoverageGain()));
				debugGain+=selected.getCoverageGain();
				coverage.addPath(this.network, selected.getSensor(), selected.getTimeslot());
			} else {
				break;
			}
		}
		
		
		ExperimentSetting.gLog.severe(String.format("Debug Gain is %.2f", debugGain));
		return coverage;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
