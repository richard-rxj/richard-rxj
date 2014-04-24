package seqi;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;


/**
 * BorderedShape
 * 
 * Copyright (C) 2009, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Jun 15, 2009
 */
public class BorderedShape extends DrawShape {
	
	private DrawShape decoratedShape; 
	private ShapeStyle borderStyle;
	
	/**
	 * Create a BorderedShape for a given DrawShape
	 * with a no border style.
	 * 
	 * @param decoratedShape The shape to be decorated with shading. 
	 */	
	public BorderedShape(DrawShape decoratedShape) {
		this.decoratedShape = decoratedShape;
	}
	
	
	/**
	 * Create a BorderedShape for a given DrawShape
	 * with the given border style.
	 * 
	 * @param decoratedShape The shape to be decorated with shading. 
	 * 
	 * @param bColor Color of the border
	 */	
	public BorderedShape(DrawShape decoratedShape, ShapeStyle borderStyle) {
		this.decoratedShape = decoratedShape;
		this.borderStyle = borderStyle;
	}
	
	/** Set the border style for this bordered shape. */
	public void setBorderStyle(ShapeStyle borderStyle) {
		this.borderStyle = borderStyle;
	}
	
	/**
	 * Override the parent's drawing functionality. 
	 * @param style The drawing style of the original shape 
	 * (to be decorated) 
	 */
	public void draw(Graphics paramGraphics, double centerx, double centery,
			ShapeStyle style) {
		
		float[] xPoints = decoratedShape.xPoints;
		float[] yPoints = decoratedShape.yPoints;
		
		int nPoints = xPoints.length;
		
		Graphics2D g2d = (Graphics2D)paramGraphics;
      	AffineTransform oldTransform = g2d.getTransform();
      	
    	// create a star from a series of points
        GeneralPath drawPath = new GeneralPath();
      
        // set the initial coordinate of the General Path        
        drawPath.moveTo(xPoints[0], yPoints[0]);
      
        // create the star--this does not draw the star
        for ( int k = 1; k < nPoints; k++ )
        	drawPath.lineTo(xPoints[k], yPoints[k]);

        // close the shape
        drawPath.closePath();		
		
		// translate the origin to centerx, centery
        g2d.translate(centerx, centery);
        
        //// Fill the original shape with the given style.
        style.applyStyle(g2d); // apply the style.        
        g2d.fill(drawPath); // draw
        
		// revert back to the previous transform and color.
        style.revert(g2d);
        
        //// Draw the border using the same style, 
        /// but a different color for the border.
        borderStyle.applyStyle(g2d); // apply the style.        
        g2d.draw(drawPath); // draw
        
		// revert back to the previous transform and color.        
        borderStyle.revert(g2d);
        
        // revert g2d to the previous transformation 
        // before drawing this shape.
		g2d.setTransform(oldTransform);
	}
}
