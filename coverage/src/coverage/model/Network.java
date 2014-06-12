package coverage.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Network {

	BaseStation base;
	Set<Sensor> sensors;
	Set<Target> targets;
	
	Map<Sensor, Set<Target>>  s2TMap;
	Map<Target, Set<Sensor>>  t2SMap;
	
	public Network() {
		sensors=new HashSet<Sensor>();
		targets=new HashSet<Target>();
		s2TMap=new HashMap<Sensor, Set<Target>>();
		t2SMap=new HashMap<Target, Set<Sensor>>();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
