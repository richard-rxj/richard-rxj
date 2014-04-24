package seqi;

import java.awt.Graphics2D;


/**
 * EventFilter is responsible for filtering events from 
 * a catalog based on certain criteria.
 * 
 * Copyright (C) 2009, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Nov 27, 2009
 */
public abstract class EventFilter { // extends Observable {
	
	/**
	 * 
	 * Produce an output EventList from the provided input list.
	 * 
	 * @param inputList
	 * 
	 * @return output list
	 */
	public abstract EventList filter(EventList inputList);
	
	
	/**
	 * For some filter, it is necessary to visualize its information
	 * in a MapView. Subclass implementation is optional, 
	 * i.e. can be empty.
	 */
	protected abstract void drawFilterInfo(Graphics2D g2d, MapView map);
	
	
	
	/**
	 * Check if the filter has received valid input to perform filtering.
	 * 
	 * @return true if sufficient, false otherwise.
	 * 
	 */
	public abstract boolean hasValidInput();
}
