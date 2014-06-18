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
	public double schedule() {
		// TODO Auto-generated method stub
		Coverage coverage=new Coverage();
		coverage.initial(this.network, this.timeslots, this.func);
		

		
		PriorityQueue<ChoicePair> queue=new PriorityQueue<ChoicePair>(this.network.getSensors().size()*this.timeslots.size(),Collections.reverseOrder());

		while(true) {
			queue.clear();
			
			for(Sensor sensor:this.network.getSensors()) {
				
				if(sensor.getActualBudget()<ExperimentSetting.energyCost) continue;
				
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
				coverage.add(this.network, selected.getSensor(), selected.getTimeslot());
			} else {
				break;
			}
		}
		
		
		
		return coverage.computeCoverage();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
