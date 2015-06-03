package wCharging.model;

import java.util.ArrayList;

import wCharging.test.SimulationSetting;

public class ChargingTour {
	private ArrayList<ChargingRequest> sequence = new ArrayList<ChargingRequest>();
	private double interTime;

	public ArrayList<ChargingRequest> getSequence() {
		return sequence;
	}

	public double getInterTime() {
		return interTime;
	}

	public void setInterTime(double interTime) {
		this.interTime = interTime;
	}

	public void addChargingRequest(ChargingRequest a) {
		if (sequence.size() == 0) {
			this.sequence.add(a);
			this.interTime = 0;
		} else {
			ChargingRequest current = this.sequence
					.get(this.sequence.size() - 1);
			this.sequence.add(a);
			this.interTime += Math.sqrt(Math.pow(
					current.getxAxis() - a.getxAxis(), 2)
					+ Math.pow(current.getyAxis() - a.getyAxis(), 2))
					/ SimulationSetting.travelSpeed;
		}
	}

	public void addCharingTour(ChargingTour a) {
		if (a.sequence.size() == 0)
			return;
		if (sequence.size() == 0) {
			this.sequence.addAll(a.getSequence());
			this.interTime = a.getInterTime();
		} else {
			ChargingRequest current = this.sequence
					.get(this.sequence.size() - 1);
			ChargingRequest next = a.getSequence().get(0);
			this.sequence.addAll(a.getSequence());
			this.interTime += a.getInterTime();
			this.interTime += Math.sqrt(Math.pow(
					current.getxAxis() - next.getxAxis(), 2)
					+ Math.pow(current.getyAxis() - next.getyAxis(), 2))
					/ SimulationSetting.travelSpeed;

		}
	}

	public double getTotalTime(double startX, double startY) {
		if (this.sequence.size() == 0)
			return 0;
		ChargingRequest first = this.sequence.get(0);
		ChargingRequest last = this.sequence.get(this.sequence.size() - 1);
		double result = this.getInterTime()
				+ Math.sqrt(Math.pow(startX - first.getxAxis(), 2)
						+ Math.pow(startY - first.getyAxis(), 2))
				/ SimulationSetting.travelSpeed
				+ Math.sqrt(Math.pow(startX - last.getxAxis(), 2)
						+ Math.pow(startY - last.getyAxis(), 2))
				/ SimulationSetting.travelSpeed;
		return result;
	}

	public String getUniqueId() {
		if (this.sequence.size() == 0)
			return "#";
		StringBuffer sf = new StringBuffer();
		for (int i = 0; i < this.sequence.size(); i++) {
			sf.append(this.sequence.get(i).getId() + "#");
		}
		return sf.toString().intern();
	}
}
