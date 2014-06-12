/**
 * 
 */
package coverage.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author u4964526
 *
 */
public class Coverage {
	Map<Target, Map<TimeSlot, Set<Sensor>>> matrix;
	
	
	public Coverage() {
		matrix=new HashMap<Target, Map<TimeSlot, Set<Sensor>>>();
	}
}
