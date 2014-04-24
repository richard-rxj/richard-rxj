package seqi;

import java.awt.Graphics2D;

/**
 * AbstractDistanceRangeFilter is the general abstract superclass 
 * of any concrete filter that filters event based on a distance range
 * from one or more reference locations.
 * 
 * Copyright (C) 2010, Research School of Earth Sciences,
 * The Australian National University.
 * 
 * @author huynh
 * 
 * Created on: Dec 3, 2010
 */
public class AbstractDistanceRangeFilter extends EventFilter {
	
	/**
	 * The minimum and maximum great-circle distances, in degrees.
	 */
	protected float minAngle, maxAngle;
	
	
	public float getMinAngle() {
		return minAngle;
	}


	public void setMinAngle(float minAngle) {
		this.minAngle = minAngle;		
		//this.setChanged();
		//super.notifyObservers();
	}


	public float getMaxAngle() {
		return maxAngle;
	}


	public void setMaxAngle(float maxAngle) {
		this.maxAngle = maxAngle;
		//this.setChanged();
		//super.notifyObservers();
	}
	

	/* (non-Javadoc)
	 * @see seqi.EventFilter#drawFilterInfo(java.awt.Graphics2D, seqi.MapView)
	 */
	protected void drawFilterInfo(Graphics2D g2d, MapView map) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see seqi.EventFilter#filter(seqi.EventList)
	 */
	public EventList filter(EventList inputList) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see seqi.EventFilter#hasValidInput()
	 */
	public boolean hasValidInput() {
		// TODO Auto-generated method stub
		return false;
	}

}
