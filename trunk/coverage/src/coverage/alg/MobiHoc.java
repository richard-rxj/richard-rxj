package coverage.alg;

import coverage.model.Coverage;
import coverage.util.ExperimentSetting;

public class MobiHoc extends Solution {

	@Override
	public Coverage schedule() {
		// TODO Auto-generated method stub
		Coverage coverage=new Coverage();
		coverage.initial(this.network, this.timeslots, this.func);
		
		
		
		
		ExperimentSetting.gLog.severe(String.format("Debug Gain is %.2f", debugGain));
		return coverage;
	}

}
