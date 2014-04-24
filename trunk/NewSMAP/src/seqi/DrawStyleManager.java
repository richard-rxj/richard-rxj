package seqi;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;



/**
 * DrawStyleManager manages styles of the shapes drawn 
 * in an image.
 * 
 * Use Singleton to enforce a single manager.
 * 
 * Copyright (C) 2009, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Jun 19, 2009
 */
public class DrawStyleManager {
	
	/** Lower bound of each (mb) magnitude interval, 
	 * must be sorted in increasing order */
	static final float[] magLowerBounds = new float[]{0f, 5f, 6f, 7f};
	
	/** Shape scales corresponding to the magnitude intervals above */
	static final float[] scales = new float[]{0.5f, 1.0f, 1.5f, 2.2f}; 
	
	/** Lower bound of each depth interval, in kms, 
	 * must be sorted in increasing order */
	static final float[] depthLowerBounds = new float[]{0, 33f, 100f, 300f};
	
	/** Colors corresponding to the depth intervals above */
	static final Color[] colors = new Color[]{Color.WHITE, Color.YELLOW, Color.ORANGE, Color.RED};
	
	private static DrawStyleManager styleManager;
	
	//Old code: private static final EventShapeStyle[][] eventStyles;
	
	
	private static final Hashtable<Integer, Set<EventShapeStyle>> eventStyles 
		= new Hashtable<Integer, Set<EventShapeStyle>>();
		

// Old code:
//	static {
		// pre-cache all possible styles.
//		int nScales = scales.length;
//		int nColors = colors.length;
//		eventStyles = new EventShapeStyle[nScales][nColors];
//		for (int i = 0; i < nScales; i++) {
//			for (int j = 0; j < nColors; j++) {
//				eventStyles[i][j] = new EventShapeStyle(scales[i], colors[j]);
//			}
//		} 
//	}
	
	
	private DrawStyleManager() {		
		
	}
	
	
	
	
//	Old code: 
//	public EventShapeStyle getStyle(CatalogEvent e) {
//		float depth = e.depth;
//		float mb = e.mb; // mb magnitude.
//		
//		// Find the index of the lower bound 
//		// of the interval containing the magnitude and depth of the 
//		// current event (use binary search).
//		int magIndex = Arrays.binarySearch(magLowerBounds, mb);
//		int depthIndex = Arrays.binarySearch(depthLowerBounds, depth);
//		
//		// if the given mb is not one of the lower bounds
//		// turn the index into a positive number.
//		if (magIndex < 0)
//			magIndex = -(magIndex + 2); // see Arrays.binarySearch doc
//		else if (magIndex == magLowerBounds.length) // if found as a lower bound
//			magIndex--;
//
//		// if the given mb is not one of the lower bounds
//		// turn the index into a positive number.
//		if (depthIndex < 0)
//			depthIndex = -(depthIndex + 2);// see Arrays.binarySearch doc
//		else if (depthIndex == depthLowerBounds.length) // if found as a lower bound
//			depthIndex--;
//		
//		return eventStyles[magIndex][depthIndex];
//	}
	
	
	/**
	 * Map an earthquake event to a drawing style corresponding to it.
	 */
	public EventShapeStyle getStyle(CatalogEvent e) {
		float depth = e.depth;
		float mb = e.mb; // mb magnitude.
		
		// Find the index of the pre-defined interval  
		// into which the magnitude of the current event fall.
		int magIndex = Arrays.binarySearch(magLowerBounds, mb);
		
		// if the given mb is not one of the lower bounds
		// turn the index into a positive number.
		if (magIndex < 0)
			magIndex = -(magIndex + 2); // see Arrays.binarySearch doc
		else if (magIndex == magLowerBounds.length) // if found as a lower bound
			magIndex--;

		// convert magnitude into a continuous shape scale by 
		// interpolating the magnitude
		// within the pre-defined range it falls into.
		float shapeScale = 1;
		if (magIndex < magLowerBounds.length - 1) {
			shapeScale = (mb - magLowerBounds[magIndex])/
				(magLowerBounds[magIndex+1] - magLowerBounds[magIndex])
				* (scales[magIndex+1] - scales[magIndex]) + scales[magIndex];  
		} else if (magIndex == magLowerBounds.length - 1) { 
			// if this event is bigger than any pre-defined lower bounds,			
			// interpolate to the greatest lower interval.
			shapeScale = (mb - magLowerBounds[magIndex-1])/
			(magLowerBounds[magIndex] - magLowerBounds[magIndex-1])
			* (scales[magIndex] - scales[magIndex-1]) + scales[magIndex-1];
		}

		// Find the index of the lower bound 
		// of the interval containing the depth of the 
		// current event (use binary search).
		int depthIndex = Arrays.binarySearch(depthLowerBounds, depth);
		
		// if the given mb is not one of the lower bounds
		// turn the index into a positive number.
		if (depthIndex < 0)
			depthIndex = -(depthIndex + 2);// see Arrays.binarySearch doc
		else if (depthIndex == depthLowerBounds.length) // if found as a lower bound
			depthIndex--;
		
		return getCachedShapeStyle(shapeScale, shapeScale, colors[depthIndex]);
	}
	
	// Obtain an event style with the given scales and color if available
	// from the style pool. Otherwise create a new style and add it into
	// the pool. This method saves memory from being allocated to new objects. 
	private EventShapeStyle getCachedShapeStyle(float scaleX, float scaleY, Color color) {		
		int hashCode = (int)Math.floor(scaleX) + (int)Math.floor(scaleY) 
			+ color.hashCode(); 
		
		Set<EventShapeStyle> styleSet = eventStyles.get(Integer.valueOf(hashCode));
		if (styleSet == null) { 
			styleSet = new HashSet<EventShapeStyle>();
			eventStyles.put(Integer.valueOf(hashCode), styleSet);
		}
		
		for (EventShapeStyle eventShapeStyle : styleSet) {
			if ((eventShapeStyle.scaleX == scaleX) && (eventShapeStyle.scaleY == scaleY) 
					&& (eventShapeStyle.color.equals(color)))
				return eventShapeStyle;
		}
		
		// If style is not yet added, create one and add it to the pool.
		final EventShapeStyle newStyle = new EventShapeStyle(scaleX, scaleY, color);		
		styleSet.add(newStyle);
		
		return newStyle;
	}
	
	
	/**
	 * Based on the current map from earthquake event properties
	 * to ShapeStyle, retrieve a border style for the current event,
	 * that is the same as its fill style, but with the given border color.
	 * If the required border style has not existed, create it 
	 * and update the border style pool with the new border style. 
	 * 
	 * @param
	 * 
	 * @return a border style required for the given event. 
	 */
	public EventShapeStyle getBorderStyle(CatalogEvent e, Color borderColor) {
		EventShapeStyle fillStyle = getStyle(e);
		EventShapeStyle borderStyle =  
			new EventShapeStyle(fillStyle.scaleX, fillStyle.scaleY, borderColor);
		return borderStyle;
	}
	
	public static DrawStyleManager getStyleManager(){
		if (styleManager == null)
			styleManager = new DrawStyleManager();
		return styleManager; 
	}
	
	
}
