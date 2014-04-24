package seqi;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * DepthRangeFilter retains events whose depth fall 
 * inclusively within a range.
 * 
 * Copyright (C) 2009, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Dec 20, 2009
 */
public class DepthRangeFilter extends EventFilter {

	// min and max depth of the range
	private float minDepth, maxDepth;
	
	/**
	 * 
	 */
	public DepthRangeFilter() {
		
	}
	
	public float getMinDepth() {
		return minDepth;
	}

	public void setMinDepth(float minDepth) {
		this.minDepth = minDepth;
	}

	public float getMaxDepth() {
		return maxDepth;
	}

	public void setMaxDepth(float maxDepth) {
		this.maxDepth = maxDepth;
	}

	/**
	 * @see EventFilter#filter(EventList)
	 */
	public EventList filter(EventList inputList) {		
		List<CatalogEvent> events = inputList.getEvents();
		List<CatalogEvent> output = new ArrayList<CatalogEvent>();
		
		for (CatalogEvent e : events) {			
			float eventDepth = e.depth;
			if ((eventDepth >= minDepth) && (eventDepth <= maxDepth))
				output.add(e);
		}
		
		return new EventList(output);
	}

	@Override
	protected void drawFilterInfo(Graphics2D g2d, MapView map) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Check if the filter has received valid input to perform filtering.
	 * 
	 * @return true if sufficient, false otherwise.
	 * 
	 */
	public boolean hasValidInput() {
		return (maxDepth > minDepth	&& minDepth >= 0);
	}
		
		
}
