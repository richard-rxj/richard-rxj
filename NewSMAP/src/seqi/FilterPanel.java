package seqi;

import javax.swing.JPanel;


/**
 * FilterPanel is a panel providing the user interface for 
 * customizing filter's parameters. Subclasses of this class
 * implement specific UI components for users 
 * to interact with specific filters. 
 * 
 * Copyright (C) 2009, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Dec 17, 2009
 */
public abstract class FilterPanel extends JPanel {
	
	/**
	 * @return The EventFilter which will take user input 
	 * information from this FilterPanel.
	 */
	public abstract EventFilter getFilter();
}
