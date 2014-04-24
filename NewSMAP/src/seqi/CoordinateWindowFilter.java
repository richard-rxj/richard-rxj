package seqi;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;


/**
 * CoordinateWindowFilter
 * 
 * Copyright (C) 2010, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Jan 27, 2010
 */
public class CoordinateWindowFilter extends EventFilter {

	// The x and y coordinates of the top-left (minLon, minLat) 
	// and bottom-right (maxLon, maxLat) corners 
	// of the selected coordinate window.	
	private float topLeftLon, topLeftLat, bottomRightLon, bottomRightLat;
	
	
	
	/**
	 * 
	 */
	public CoordinateWindowFilter() {
		
	}
	
	
	/**
	 * Search for events from the given inputList
	 * whose spatial coordinates fall between the ranges 
	 * specified by the minimal and maximal x and y 
	 * coordinates as stored in the fields of this class.
	 * 
	 * @see EventFilter#filter(EventList)
	 */
	@Override
	public EventList filter(EventList inputList) {
		List<CatalogEvent> events = inputList.getEvents();
		List<CatalogEvent> output = new ArrayList<CatalogEvent>();
		
		for (CatalogEvent e : events) {			
			float eventLat = e.latitude;
			float eventLon = e.longitude;
			

			// Note that the longitude can wrap around the interval [-180, 180]
			// so if topLeftLon >= topLeftLat, we need to use a different 
			// condition for the range check.
			// This check assumes that all the longitudes are in the range [-180, 180]
			if ((eventLat <= topLeftLat) && (eventLat >= bottomRightLat) &&
				(((topLeftLon <= bottomRightLon) && (eventLon >= topLeftLon) && (eventLon <= bottomRightLon))				
				|| ((topLeftLon > bottomRightLon) && !((eventLon >= bottomRightLon) && (eventLon <= topLeftLon))))) 
				output.add(e);
		}
		
		return new EventList(output);
	}


	public float getTopLeftLon() {
		return topLeftLon;
	}


	public void setTopLeftLon(float topLeftLon) {
		this.topLeftLon = topLeftLon;
	}


	public float getTopLeftLat() {
		return topLeftLat;
	}


	public void setTopLeftLat(float topLeftLat) {
		this.topLeftLat = topLeftLat;
	}


	public float getBottomRightLon() {
		return bottomRightLon;
	}


	public void setBottomRightLon(float bottomRightLon) {
		this.bottomRightLon = bottomRightLon;
	}


	public float getBottomRightLat() {
		return bottomRightLat;
	}


	public void setBottomRightLat(float bottomRightLat) {
		this.bottomRightLat = bottomRightLat;
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
		final int viewWidth = map.getWidth();
		
		// Draw visual feedback on the map, showing user interaction
		// in the Zoom by Region Selection and Region Selection mouse mode.
		
		
		// Convert the polar coordinates of the top left and bottom right corners 
		// into image coordinates.
		float[] topLeftImageCoords = map.geoToImageCoords(topLeftLon, topLeftLat);			
		float[] bottomRightImageCoords = map.geoToImageCoords(bottomRightLon, bottomRightLat);

		// Set a color for drawing
		Color curColor = g2d.getColor();
		g2d.setColor(Color.YELLOW);
		
		if ((int)topLeftImageCoords[0] < (int)bottomRightImageCoords[0]) {
			g2d.drawRect((int)topLeftImageCoords[0], (int)topLeftImageCoords[1], 
				(int)(bottomRightImageCoords[0] - topLeftImageCoords[0]) + 1, 
				(int)(bottomRightImageCoords[1] - topLeftImageCoords[1]) + 1);
		} else {
			// Draw two half rectangles
			g2d.drawRect((int)topLeftImageCoords[0], (int)topLeftImageCoords[1], 
					viewWidth - (int)(bottomRightImageCoords[0]) + 1, 
					(int)(bottomRightImageCoords[1] - topLeftImageCoords[1]) + 1);
			g2d.drawRect(-1, (int)topLeftImageCoords[1], 
					(int)bottomRightImageCoords[0] + 2, 
					(int)(bottomRightImageCoords[1] - topLeftImageCoords[1]) + 1);
		}
		
		g2d.setColor(curColor);
		
	}
	
	
	
	/**
	 * Check if the filter has received valid input to perform filtering.
	 * 
	 * @return true if sufficient, false otherwise.
	 * 
	 */
	public boolean hasValidInput() {
		return (topLeftLat > bottomRightLat && topLeftLon != bottomRightLon);
	}

}
