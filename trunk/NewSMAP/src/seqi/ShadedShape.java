package seqi;

import java.awt.Color;
import java.awt.Graphics;


/**
 * ShadedShape draws additional shading effects in addition
 * to a given shape.
 * 
 * The design of this class follows the Decorator Design pattern.
 * 
 * Copyright (C) 2009, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Jun 15, 2009
 */
public class ShadedShape extends DrawShape {
	
	private DrawShape decoratedShape; 
	private Color shadeColor;
	private int shadeX;
	private int shadeY;
	
	/**
	 * Create a ShadedShape for a given one.
	 * 
	 * @param decoratedShape The shape to be decorated with shading.
	 * 
	 * @param shadeX The displacement (in pixels) of the shading in the x axis,
	 * compared to the original decoratedShape. 
	 * x > 0 means shading on the right of the original shape and vice versa.
	 * 
	 * @param shadeY The displacement (in pixels) of the shading in the y axis,
	 * compared to the original decoratedShape. 
	 * y > 0 means shading under the original shape and vice versa.
	 * 
	 * Note that the image origin is located at the top left corner.
	 * 
	 * @param sColor Color of the shading 
	 */
	public ShadedShape(DrawShape decoratedShape, int shadeX, int shadeY, Color sColor) {
		this.decoratedShape = decoratedShape;
		this.shadeX = shadeX;
		this.shadeY = shadeY;
		this.shadeColor = sColor;	
		
	}
	
	/**
	 * Override the parent's drawing functionality.
	 * @param style The drawing style of the original shape part.
	 */
	public void draw(Graphics paramGraphics, double centerx, double centery, ShapeStyle style) {		
		// Draw the shading first.		
		EventShapeStyle shadedStyle = new EventShapeStyle(((EventShapeStyle)style).scaleX, 
				((EventShapeStyle)style).scaleY, shadeColor);
		decoratedShape.draw(paramGraphics, centerx + shadeX, centery + shadeY, shadedStyle);
		
		// Draw the original shape on top.
		decoratedShape.draw(paramGraphics, centerx, centery, style);
	}
	
}
