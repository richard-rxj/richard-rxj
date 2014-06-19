    /**
 * 
 */
package coverage.util;

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
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
