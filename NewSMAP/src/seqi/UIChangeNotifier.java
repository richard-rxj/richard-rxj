package seqi;

import java.util.Observable;

/**
 * UIChangeNotifier 
 *  
 * A generic subclass of Observable, responsible for firing event notification
 * to UI elements upon a change in the UI occurring elsewhere. 
 * 
 * Copyright (C) 2011, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Mar 9, 2011
 */


class UIChangeNotifier extends Observable {
	
	/**
	 * Notify all the observers of the changes in this MapView. 
	 */
	public void notifyObservers() {		
		// Set the change flag so that the 
		// notifyObservers() method can be activated
		this.setChanged();
		super.notifyObservers();
	}
}
