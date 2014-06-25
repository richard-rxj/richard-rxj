/**
 * 
 */
package coverage.model;

import java.util.Collections;
import java.util.PriorityQueue;

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
	
	@Override
	public String toString() {
		return String.format("Sensor<%d>-TimeSlot<%d>-Gain<%.2f>", this.sensor.id, this.timeslot.getId(), this.coverageGain);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PriorityQueue<ChoicePair> queue=new PriorityQueue<ChoicePair>(5,Collections.reverseOrder());

		Sensor s1=new Sensor(1,1,1);
		TimeSlot t1=new TimeSlot(1);
		ChoicePair c1=new ChoicePair();
		c1.setSensor(s1);
		c1.setTimeslot(t1);
		c1.setCoverageGain(8);
		queue.offer(c1);
		
		
		Sensor s2=new Sensor(2,1,1);
		TimeSlot t2=new TimeSlot(2);
		ChoicePair c2=new ChoicePair();
		c2.setSensor(s2);
		c2.setTimeslot(t2);
		c2.setCoverageGain(10);
		queue.offer(c2);
		
		
		Sensor s3=new Sensor(3,1,1);
		TimeSlot t3=new TimeSlot(1);
		ChoicePair c3=new ChoicePair();
		c3.setSensor(s3);
		c3.setTimeslot(t3);
		c3.setCoverageGain(5);
		queue.offer(c3);
		
		System.out.println(queue.peek());
		
	}

}
