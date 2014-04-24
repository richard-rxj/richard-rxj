package seqi;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Project: SEQI
 * Package: 
 * Type: TimeWindowFilter is responsible for retrieving 
 * events within a time window specified by a start and 
 * end date from a collection of earthquake events.
 * 
 * Creator: Cong Phuoc Huynh
 * On: 20/12/2009, @ 12:11:31 PM
 * Copyright 2009.
 */
public class TimeWindowFilter extends EventFilter {
	
	// start and end dates for filtering
	private Date startDate, endDate;
	
	/**
	 * 
	 */
	public TimeWindowFilter() {
		
	}
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {		
		// set start time to be the start of the startDate
		this.startDate = new Date(startDate.getYear(), startDate.getMonth(), startDate.getDate(), 0, 0, 0);
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		// set end time to be the end of the endDate
		this.endDate = new Date(endDate.getYear(), endDate.getMonth(), endDate.getDate(), 23, 59, 59);
	}
	
	


	/** 
	 * Retain any events happening between the start date 
	 * and end date specified for this filter.
	 * 
	 * @see EventFilter#filter(EventList)
	 */
	public EventList filter(EventList inputList) {		
		List<CatalogEvent> events = inputList.getEvents();
		List<CatalogEvent> output = new ArrayList<CatalogEvent>();
		
		for (CatalogEvent e : events) {			
			Date eventDate = e.startDate.getTime();					
			if ((eventDate.compareTo(startDate) >=0) && (eventDate.compareTo(endDate) <=0))
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
		return (startDate.before(endDate) || startDate.equals(endDate));
	}
}
