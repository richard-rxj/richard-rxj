package seqi;

import java.awt.Color;
import java.awt.Graphics2D;


/**
 * EventShapeStyle is the graphical style for a shape 
 * representing an earthquake event.
 * In the current implementation, the style consists 
 * of a shape scale and color. 
 * 
 * Copyright (C) 2009, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Jun 19, 2009
 */
public class EventShapeStyle extends ShapeStyle {
	
	protected double scaleX; // horizontal scale of the event	
	protected double scaleY; // vertical scale of the event	
	protected Color color; // color of the event shape
	
	// previous color of the last graphics 
	// context affected by this style.
	private Color prevColor; 
	
	/**
	 * Create an EventShapeStyle with the given scale and color.
	 * @param scaleX the horizontal scale.
	 * @param scaleY the vertical scale.
	 * @param c The color of the style. 
	 */
	public EventShapeStyle(double scaleX, double scaleY, Color c) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.color = c;
	}
	
	/**
	 * Create an EventShapeStyle with the given scale
	 * as the horizontal and vertical scales, and 
	 * with the given color.
	 * 
	 * @param scale a scale.
	 * @param c The color of the style. 
	 */	
	public EventShapeStyle(double scale, Color c) {
		this(scale, scale, c);
	} 
	
	/**
	 * Apply this style to the given Graphics context.
	 * 
	 * @param g A Graphics context that is changed according to this style.
	 */
	public void applyStyle(Graphics2D g){		
		// Store old color
		prevColor = g.getColor();			
		
		// Set new color and scale
		g.setColor(color);
		g.scale(scaleX, scaleY);
	}
	
	
	/**
	 * Revert the given graphics context to the previous state before 
	 * applying this ShapeStyle.
	 * @param g A Graphics context that was changed according to this style.
	 */
	public void revert(Graphics2D g){
		g.setColor(prevColor);
		g.scale(1/scaleX, 1/scaleY);
	}
	
	
	/**
	 * Compare with another object.
	 */
	public boolean equals(Object style) {
		if (style instanceof EventShapeStyle) {
			EventShapeStyle aStyle = (EventShapeStyle)style;
			return ((aStyle.scaleX == this.scaleX) 
					&& (aStyle.scaleY == this.scaleY)
					&& (aStyle.color.equals(this.color)));
		}
		return false; 
	}
	
	/**
	 * Hashcode to be used for search in a hash table or hash set, hash map.
	 */
	public int hashCode() {
		return (int)Math.floor(scaleX) + (int)Math.floor(scaleY) 
			+ color.hashCode();
	}
}
