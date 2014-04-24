package seqi;

import java.util.Comparator;

/**
 * DepthComparator
 * 
 * Copyright (C) 2009, Research School of Earth Sciences, The Australian
 * National University
 * 
 * @author huynh
 * 
 *         Created on: May 11, 2009
 */
public class DepthComparator implements Comparator<CatalogEvent> {

	/**
	 * Compare two CatalogEvents by depth.
	 * 
	 * @return -1 if the depth of the first one is less than that of the second
	 *         one. 1 if the depth of the first one is greater than that of the
	 *         second one. 0 otherwise.
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(CatalogEvent e1, CatalogEvent e2) {

		if (e1.depth < e2.depth)
			return -1;

		if (e1.depth > e2.depth)
			return 1;

		return 0;
	}
}