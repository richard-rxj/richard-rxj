package seqi;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;



/**
 * MagnitudeRangeFilter retains events whose magnitudes 
 * mb, ms, mw fall inclusively within given ranges.
 * 
 * Copyright (C) 2009, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Dec 20, 2009
 */
public class MagnitudeRangeFilter extends EventFilter {


	// min and max bounds of different kinds of magnitudes.
	private float minMb, maxMb;
	private float minMs, maxMs;
	private float minMw, maxMw;
	
	
	/**
	 * 
	 */
	public MagnitudeRangeFilter() {
		
	}
	
	
	
	public float getMinMb() {
		return minMb;
	}



	public void setMinMb(float minMb) {
		this.minMb = minMb;
	}



	public float getMaxMb() {
		return maxMb;
	}



	public void setMaxMb(float maxMb) {
		this.maxMb = maxMb;
	}



	public float getMinMs() {
		return minMs;
	}



	public void setMinMs(float minMs) {
		this.minMs = minMs;
	}



	public float getMaxMs() {
		return maxMs;
	}



	public void setMaxMs(float maxMs) {
		this.maxMs = maxMs;
	}



	public float getMinMw() {
		return minMw;
	}



	public void setMinMw(float minMw) {
		this.minMw = minMw;
	}



	public float getMaxMw() {
		return maxMw;
	}



	public void setMaxMw(float maxMw) {
		this.maxMw = maxMw;
	}

	/**
	 * Search for events from the given inputList
	 * whose magnitudes fall between the ranges specified
	 * by the minimal and maximal values (end values) of the 
	 * intervals, set to the fields of this class.
	 * 
	 * @see EventFilter#filter(EventList)
	 */
	public EventList filter(EventList inputList) {
				
		
		List<CatalogEvent> events = inputList.getEvents();
		List<CatalogEvent> output = new ArrayList<CatalogEvent>();
		
		for (CatalogEvent e : events) {			
			float eventMb = e.mb;
			float eventMs = e.ms;
			float eventMw = e.mw;
			
			if ((eventMb >= minMb) && (eventMb <= maxMb) && 
				(eventMs >= minMs) && (eventMs <= maxMs) &&
				(eventMw >= minMw) && (eventMw <= maxMw))
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
		return ((maxMb >= minMb) && (minMb >= 0)
			  && (maxMs >= minMs) && (minMs >= 0)
			  && (maxMw >= minMw) && (minMw >= 0));
	}

}
