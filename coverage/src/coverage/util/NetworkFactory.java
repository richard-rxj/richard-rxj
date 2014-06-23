    /**
 * 
 */
package coverage.util;

import java.io.File;
import java.io.IOException;

import coverage.model.BaseStation;
import coverage.model.Network;
import coverage.model.Sensor;
import coverage.model.Target;

/**
 * @author u4964526
 *
 */
public class NetworkFactory {

    private int sensorSize;
    private int targetSize;
    
	
	


	public void setSensorSize(int sensorSize) {
		this.sensorSize = sensorSize;
	}


	


	public void setTargetSize(int targetSize) {
		this.targetSize = targetSize;
	}


	public Network getNetwork() {
		Network result=new Network();
		
		int id=0;
		double x=ExperimentSetting.getX();
		double y=ExperimentSetting.getY();
		BaseStation tBase=new BaseStation(id, x, y);
		tBase.setTransRange(ExperimentSetting.transRange);
		result.setBase(tBase);
		
		for(int i=0; i<this.sensorSize; i++) {
			id++;
			x=ExperimentSetting.getX();
			y=ExperimentSetting.getY();
			
			Sensor tSensor=new Sensor(id, x, y);
			tSensor.setTransRange(ExperimentSetting.transRange);
			tSensor.setSenseRange(ExperimentSetting.senseRange);
			tSensor.setBatteryCapacity(ExperimentSetting.batteryCapacity);
			tSensor.setActualBudget(ExperimentSetting.getActualBudget(id));
			tSensor.setPredictBudget(ExperimentSetting.getPredictBudget(id));
			tSensor.setResidualEnergy(0);
			
			result.getSensors().add(tSensor);
		}
		
		
		for(int i=0; i<this.targetSize; i++) {
			id++;
			x=ExperimentSetting.getX();
			y=ExperimentSetting.getY();
			
			Target tTarget=new Target(id, x, y);
			
			result.getTargets().add(tTarget);
		}
		
		result.combine();
		
		return result;
	}
	
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int[] targetSizes={25,50};
		int[] networkSizes={100, 200, 300, 400, 500};
		int cishu=ExperimentSetting.cishu;  
		
		String outputBase="data"+File.separator+"topology";
		File tf=new File(outputBase);
		if(!tf.exists()) {
			tf.mkdirs();
		}
		
		NetworkFactory generator=new NetworkFactory();
		for(int nI=0; nI<networkSizes.length; nI++) {
			int networkSize=networkSizes[nI];
			for(int tI=0; tI<targetSizes.length; tI++) {
				int targetSize=targetSizes[tI];
				for(int cI=0; cI<cishu; cI++) {
					String outputFile=outputBase+File.separator+"topology_"
				                     +networkSize+"_"+targetSize+"_"+cI+".txt";
					generator.setSensorSize(networkSize);
					generator.setTargetSize(targetSize);
					Network tNetwork=generator.getNetwork();
					tNetwork.saveToFile(outputFile);
					System.out.println("topology_"
		                     +networkSize+"_"+targetSize+"_"+cI+" generated!");
				}
			}
		}
		
	}

}
