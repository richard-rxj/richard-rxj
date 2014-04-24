package seqi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;

/**
 * EventList is an ordered list of CatalogEvents that
 * is either loaded from files or filtered out, sorted
 * as a results of an operation carried out by this application. 
 * 
 * @author huynh
 * 
 * Date: Apr 8, 2009
 */
public class EventList extends Observable {
	
	/** All the events in the catalog */
	private List<CatalogEvent> events;
	
	/**
	 * Create an empty event list.
	 */
	public EventList() {
		this.events = new ArrayList<CatalogEvent>();
	}
	
	
	public EventList(List<CatalogEvent> events) {
		this.events = events;
	}
	
	/** Get all events as a set */
	public List<CatalogEvent> getEvents() {
		
		Collections.sort(events, new Comparator<CatalogEvent>(){
			public int compare(CatalogEvent c1,CatalogEvent c2) {
				return c1.startDate.compareTo(c2.startDate);
			}
		});      //v1.7----------sort events according to start date
		
		return Collections.unmodifiableList(events);
	}	
	
	/**
	 * Add all events in another EventList to 
	 * this EventList, excluding the events already
	 * in this EventList. 
	 * 
	 * @param another Another EventList.
	 */
	public void addEvents(EventList another) {
		if (another == null)
			return;
		
		List<CatalogEvent> otherEvents = another.getEvents();
		for (CatalogEvent e : otherEvents) {
			if (!events.contains(e))
				events.add(e);
		}
	}
	
	/** 
	 * Select/deselect events in this list to be displayed
	 * @param eventIndices indices of the events to be displayed.
	 */
	public void setSelectedEvents(int[] eventIndices) {
		// First de-select all the events in this list
		int nEvents = events.size();		
		for (int i = 0; i < nEvents; i++) {					
			CatalogEvent event = events.get(i);
			event.setSelected(false);
		}
		
		// Select the given events
		int nSelected = eventIndices.length;
		for (int i = 0; i < nSelected; i++) {					
			CatalogEvent event = events.get(eventIndices[i]);
			event.setSelected(true);
		}
		
		// Flag the changes
		setChanged();		
		
		// Notify observers to update their displays.  
		notifyObservers();
	}
	
	
	/** 
	 * De-select all the events in this EventList, i.e. set to the default 'unselected' status.
	 */
	public void deselectAllEvents() {
		// First de-select all the events in this list
		int nEvents = events.size();		
		for (int i = 0; i < nEvents; i++) {					
			CatalogEvent event = events.get(i);
			event.setSelected(false);
		}
		
		// Flag the changes
		setChanged();		
		
		// Notify observers to update their displays.  
		notifyObservers();
	}
	
	
	/**
	 * Update all the views (Observer) of this EventList. 
	 */
	public void updateEventsDisplay() {
		// Flag the changes
		setChanged();		
		
		// Notify observers to update their displays.  
		notifyObservers();
	}
	
	
	/**
	 * @return a List of selected events. 
	 */
	public EventList getSelectedEvents() {
		List<CatalogEvent> selectedEvents = new ArrayList<CatalogEvent>();
		int nEvents = events.size();		
		for (CatalogEvent catalogEvent : events) {
			if (catalogEvent.isSelected) {
				selectedEvents.add(catalogEvent);
			}
		}		
		return new EventList(selectedEvents);
	}
	
	
//	/**
//	 * Does this EventList have any hidden events (events that are not displayed) 
//	 */
//	public boolean hasHiddenEvents() {
//		for (CatalogEvent catalogEvent : events) {
//			if (!catalogEvent.isSelected) {
//				return true;
//			}
//		}
//		return false;
//	}
}
