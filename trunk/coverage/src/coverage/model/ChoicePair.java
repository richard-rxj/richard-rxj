/**
 * 
 */
package coverage.model;

/**
 * @author u4964526
 *
 */
public class ChoicePair implements Comparable<ChoicePair> {

	Sensor sensor;
	TimeSlot timeslot;
	double coverageGain;
	
	
	
	public Sensor getSensor() {
		return sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	public TimeSlot getTimeslot() {
		return timeslot;
	}

	public void setTimeslot(TimeSlot timeslot) {
		this.timeslot = timeslot;
	}

	public double getCoverageGain() {
		return coverageGain;
	}

	public void setCoverageGain(double cverageGain) {
		this.coverageGain = cverageGain;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public int compareTo(ChoicePair o) {
		// TODO Auto-generated method stub
		if(this.coverageGain<o.coverageGain) {
			return -1;
		} else if (this.coverageGain>o.coverageGain) {
			return 1;
		} else {
			return 0;
		}
	}

}
