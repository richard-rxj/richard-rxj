package seqi;

import java.util.Calendar;


/**
 * CatalogEvent
 * 
 * @author huynh
 * 
 * Date: Apr 8, 2009
 */
public class CatalogEvent {
	
	public final Calendar startDate; // start date and time of the event
	
	
	// the position of the event (polar coordinates, in degrees)
	public final float longitude; // between (-180, 180]	
	public final float latitude;  // between (-90, 90]
 	
	public final float depth; // depth of event in kms
	
	public final float mb, ms, mw; // body wave, surface wave and moment magnitudes

	// whether this CatalogEvent is currently displayed in the user interface or not.
	public boolean isSelected;
	
	public CatalogEvent(Calendar startDate, float longitude, float latitude,
			float depth, float mb, float ms, float mw) {
		super();
		this.startDate = startDate;
		this.longitude = longitude;
		this.latitude = latitude;
		this.depth = depth;
		this.mb = mb;
		this.ms = ms;
		this.mw = mw;
		this.isSelected = false;
	}
	
	/**
	 * Set the selection status of this CatalogEvent.  
	 * 
	 * @param isSelected The selection status.
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	/**
	 * Compare a given object to this CatalogEvent
	 * @return true only if the given object is a CatalogEvent
	 * and its properties are the same as this CatalogEvent. 
	 */
	public boolean equals(Object obj) {
		if (CatalogEvent.class.isInstance(obj)) {
			CatalogEvent e = (CatalogEvent)obj;
			return (e.depth == depth && e.latitude == latitude 
					&& e.longitude == longitude && e.mb == mb 
					&& e.ms == ms && e.mw == mw 
					&& e.startDate.equals(startDate));
		}
        return false;
    }
	
}


