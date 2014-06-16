package coverage.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import coverage.util.ExperimentSetting;

public class Network {

	BaseStation base;
	Set<Sensor> sensors;
	Set<Target> targets;
	
	Map<Sensor, Set<Target>>  s2TMap;
	Map<Target, Set<Sensor>>  t2SMap;
	Map<Node, Set<Sensor>> connMap;
	
	public Network() {
		sensors=new HashSet<Sensor>();
		targets=new HashSet<Target>();
		s2TMap=new HashMap<Sensor, Set<Target>>();
		t2SMap=new HashMap<Target, Set<Sensor>>();
		connMap=new HashMap<Node, Set<Sensor>>();
	}
	
	/*
	 * fill s2TMap and t2SMap
	 */
	private void combine() {
		//construct s2TMap
		for(Sensor sensor:sensors) {
			Set<Target> tSet=new HashSet<Target>();
			s2TMap.put(sensor, tSet);
			for(Target target:targets) {
				double dis=sensor.getDistance(target);
				if(dis<=ExperimentSetting.senseRange) {
					tSet.add(target);
				}
			}
		}
		
		//construct t2SMap
		for(Target target: targets) {
			Set<Sensor> tSet=new HashSet<Sensor>();
			t2SMap.put(target, tSet);
			for(Sensor sensor:sensors) {
				double dis=sensor.getDistance(target);
				if(dis<=ExperimentSetting.senseRange) {
					tSet.add(sensor);
				}
			}
		}
		
		//construct connMap
		Set<Sensor> bSet=new HashSet<Sensor>();
		connMap.put(this.base, bSet);
		for(Sensor sensor:sensors) {
			double dis=this.base.getDistance(sensor);
			if(dis<=ExperimentSetting.transRange) {
				bSet.add(sensor);
			}
		}
		
		for(Sensor sensor:sensors) {
			Set<Sensor> tSet=new HashSet<Sensor>();
			connMap.put(sensor, tSet);
			for(Sensor tSensor:sensors) {
				if(tSensor==sensor) continue;
				double dis=this.base.getDistance(sensor);
				if(dis<=ExperimentSetting.transRange) {
					tSet.add(sensor);
				}
			}
		}
		
	}
	
	
	public void saveToFile(String fileName) throws IOException {
		PrintWriter bw=new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
		try  {
			bw.println(this.base);
			bw.println(this.sensors.size());
			for(Sensor sensor:sensors) {
				bw.println(sensor);
			}
			bw.println(this.targets.size());
			for(Target target:targets) {
				bw.println(target);
			}
			
		} finally {
			if(bw!=null) {
				bw.close();
			}
		}
	}
	
	public void getFromFile (String fileName) throws IOException {
		BufferedReader br=new BufferedReader(new FileReader(fileName));
		try {
			//construct basestation
			String line=br.readLine();
			String[] paras=line.split("\\s+");
			this.base=new BaseStation(Integer.parseInt(paras[0]), 
					Double.parseDouble(paras[1]), 
					Double.parseDouble(paras[2]), 
					Double.parseDouble(paras[3]));
			
			//construct sensors
			line=br.readLine();
			int sensorCount=Integer.parseInt(line);
			for(int i=0; i<sensorCount; i++) {
				line=br.readLine();
				paras=line.split("\\s+");
				Sensor tmp=new Sensor(Integer.parseInt(paras[0]), 
						Double.parseDouble(paras[1]), 
						Double.parseDouble(paras[2]));
				tmp.setTransRange(Double.parseDouble(paras[3]));
				tmp.setBatteryCapacity(Double.parseDouble(paras[4]));
				tmp.setEnergyBudget(Double.parseDouble(paras[5]));
				tmp.setResidualEnergy(Double.parseDouble(paras[6]));
				this.sensors.add(tmp);
			}
			
			//construct targets
			line=br.readLine();
			int targetCount=Integer.parseInt(line);
			for(int i=0; i<targetCount; i++) {
				line=br.readLine();
				paras=line.split("\\s+");
				Target tmp=new Target(Integer.parseInt(paras[0]), 
						Double.parseDouble(paras[1]), 
						Double.parseDouble(paras[2]));				
				this.targets.add(tmp);
			}
			
			
			//finish
			this.combine();
			
		} finally {
			if(br!=null) {
				br.close();
			}
		}
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	
	

}