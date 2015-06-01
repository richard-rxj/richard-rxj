package wCharging.util;

import java.util.Comparator;

import wCharging.model.ChargingRequest;

public class ResidualLifetimeComparator implements Comparator<ChargingRequest> {

	private boolean ascendingOrder;

	public ResidualLifetimeComparator(boolean b) {
		this.ascendingOrder = b;
	}

	@Override
	public int compare(ChargingRequest arg0, ChargingRequest arg1) {
		// TODO Auto-generated method stub
		if ((arg0.getResidualLifetime() > arg1.getResidualLifetime() && this.ascendingOrder)
				|| (arg0.getResidualLifetime() < arg1.getResidualLifetime() && !this.ascendingOrder)) {
			return 1;
		} else if ((arg0.getResidualLifetime() < arg1.getResidualLifetime() && this.ascendingOrder)
				|| (arg0.getResidualLifetime() > arg1.getResidualLifetime() && !this.ascendingOrder)) {
			return -1;
		} else {
			return 0;
		}
	}

}
