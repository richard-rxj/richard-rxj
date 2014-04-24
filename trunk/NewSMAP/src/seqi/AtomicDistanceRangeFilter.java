package seqi;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import util.SphericalCoords;


/**
 * AtomicDistanceRangeFilter retains events within a distance range
 * (specified by angles) from a SINGLE point of reference.
 * 
 * 
 * Copyright (C) 2009, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Dec 1, 2009
 */
public class AtomicDistanceRangeFilter extends AbstractDistanceRangeFilter {

	/**
	 * The longitude and latitude of the point of reference.
	 */
	private float refLon = Float.MAX_VALUE, refLat = Float.MAX_VALUE; // initialised to invalid ones.
	
	
	/**
	 * Default constructor for a distance range filter.
	 */
	public AtomicDistanceRangeFilter() {
		super();
	}
	
	
	/**
	 * Initialize a DistanceRangeFilter with the given values 
	 * for the min and max distance and reference point's polar coordinates.
	 * All the input values should be provided in degrees. 
	 * @param minAngle in degrees
	 * @param maxAngle in degrees
	 * @param refLon in degrees
	 * @param refLat in degrees
	 */
	public AtomicDistanceRangeFilter(float minAngle, float maxAngle, float refLon,
			float refLat) {
		super();
		this.minAngle = minAngle;
		this.maxAngle = maxAngle;
		this.refLon = refLon;
		this.refLat = refLat;
	}


	public float getRefLon() {
		return refLon;
	}


	public void setRefLon(float refLon) {
		this.refLon = refLon;
		//this.setChanged();
		//super.notifyObservers();
	}


	public float getRefLat() {
		return refLat;		
	}


	public void setRefLat(float refLat) {
		this.refLat = refLat;
		//this.setChanged();
		//super.notifyObservers();
	}


	/** 
	 * 
	 * @return An EventList containing events within the specified 
	 * range from the reference point. 
	 * @see EventFilter#filter(EventList)
	 * 
	 */
	public EventList filter(EventList inputList) {
		List<CatalogEvent> events = inputList.getEvents();
		List<CatalogEvent> output = new ArrayList<CatalogEvent>();
		
		for (CatalogEvent e : events) {
			// Distance between the current event and the reference point.
			double dist = SphericalCoords.greatCirleDist(e.longitude, e.latitude, refLon, refLat);
			
			if (dist <= maxAngle && dist >= minAngle)
				output.add(e);
		}
		
		return new EventList(output);
	}
	
	/**
	 * Draw the visualization of the filter information, rectangular boundary
	 * for coordinate window filter, or distance range ovals for distance range filter. 
	 * 
	 * @param g The Graphics context used to draw the filter information in the map.
	 * 
	 * @param map The MapView in which the filter information is drawn. 
	 */
	protected void drawFilterInfo(Graphics2D g2d, MapView map) {
		// Draw a cross at the center of selection.
		map.drawEpicenter(g2d, refLon, refLat);
		
		// Draw the inner and outer iso-distance curves centered at the selected point.			
		map.drawIsoDistCurve(g2d, refLon, refLat, minAngle * SphericalCoords.deg2Rad);
		map.drawIsoDistCurve(g2d, refLon, refLat, maxAngle * SphericalCoords.deg2Rad);
	}

	
	/**
	 * Check if the filter has received valid input to perform filtering.
	 * 
	 * @return true if sufficient, false otherwise.
	 * 
	 */
	public boolean hasValidInput() {
		return (maxAngle > minAngle && minAngle >= 0 && maxAngle <= 180 && 
				(refLat >= -90) && (refLat <= 90) &&
				(refLon >= -180) && (refLon <= 180));
	}
	
}
