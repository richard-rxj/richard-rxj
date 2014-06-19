/**
 * 
 */
package coverage.alg;

import java.util.List;
import java.util.Set;

import coverage.model.Coverage;
import coverage.model.Network;
import coverage.model.Sensor;
import coverage.model.TimeSlot;
import coverage.util.ExperimentSetting;
import coverage.util.Func;

/**
 * @author user
 *
 */
public class DynamicFrameWork {

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
	
	
	public double schedule() {
		int result=0;
		int initialSize= (int) Math.floor(this.timeslots.size()*ExperimentSetting.intervalInitial);

		
		//initial first interval
		int start = 0;
		int intervalSize=initialSize;
		int end=start+intervalSize-1;

		
		while(end<this.timeslots.size()) {
			List<TimeSlot> interval=this.timeslots.subList(start, end+1);
			//getBudget for each sensor based on residual energy
			for(Sensor sensor: this.network.getSensors()) {
				double residualEnergy=sensor.getResidualEnergy();
				double actualBudget=ExperimentSetting.getActualBudget(sensor.getId(), start, end);
				double predictBudget=ExperimentSetting.getPredictBudget(sensor.getId(), start, end);
				sensor.setResidualEnergy(0);
				sensor.setActualBudget(actualBudget);
				sensor.setPredictBudget(predictBudget);
				sensor.updateAccuracy();
			}
			
			Solution cSolution=new CentralizedSolution();
			cSolution.setNetwork(this.network);
			cSolution.setTimeslots(interval);
			cSolution.setFunc(this.func);
			Coverage coverage=cSolution.schedule();
			result+=coverage.computeCoverageWithoutAccuracy();
			
			//calculate accuracy and adjust next interval
			double accuracy=0;
			Set<Sensor> selectedSensors=coverage.getSensors();
			for(Sensor sensor: selectedSensors) {
				accuracy+=sensor.getAccuracy();
			}
			accuracy=selectedSensors.size()==0 ? 0:accuracy/selectedSensors.size();
			
			if(accuracy<=ExperimentSetting.accuracyThreshold) {
				intervalSize=Math.min(initialSize, Math.min((int) (intervalSize/ExperimentSetting.tuningWeight), this.timeslots.size()-end));
			} else {
				intervalSize=Math.max(1, (int) (intervalSize*ExperimentSetting.tuningWeight));
			}
			start=end+1;
			end=start+intervalSize-1;
		}
		
		return result;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
