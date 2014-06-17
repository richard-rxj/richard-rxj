/**
 * 
 */
package coverage.alg;

import java.util.HashSet;
import java.util.Set;

import coverage.model.Coverage;
import coverage.model.Sensor;

/**
 * @author user
 *
 */
public class CentralizedSolution extends Solution {

	/* (non-Javadoc)
	 * @see coverage.alg.Solution#schedule()
	 */
	@Override
	public double schedule() {
		// TODO Auto-generated method stub
		Coverage coverage=new Coverage();
		coverage.initial(this.network, this.timeslots, this.func);
		
		Set<Sensor> visitSet=new HashSet<Sensor>();
		
		
		
		return 0;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
