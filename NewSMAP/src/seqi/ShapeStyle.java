package seqi;

import java.awt.Graphics2D;


/**
 * ShapeStyle sets the style to draw a shape. A style 
 * currently consist of a scale (size) and a color.
 * 
 * The design of this class hierarchy follows the 
 * Strategy design pattern. 
 * 
 * Copyright (C) 2009, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Jun 19, 2009
 */
public abstract class ShapeStyle {
	
	/**
	 * Apply this style to the given Graphics context.
	 * 
	 * @param g A Graphics context that is changed according to this style.
	 */
	public abstract void applyStyle(Graphics2D g);
	
	/**
	 * Revert the given graphics context to the previous state before applying this 
	 * ShapeStyle.
	 * @param g A Graphics context that was changed according to this style.
	 */
	public abstract void revert(Graphics2D g);
}
