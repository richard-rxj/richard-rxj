package seqi;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;


/**
 * DrawShape.java
 * Created by: Phuoc Huynh
 * On: 09/05/2009 @ 9:57:50 PM
 */

/**
 * Project: SEQI Package: Type: DrawShape
 * 
 * DrawShape is a class of Polygon shapes that provide utilities to draw
 * themselves.
 * The vertices of a DrawShape have coordinates relative to a common 
 * local origin. The size (the scale) and coordinates of the vertices in 
 * a containing image should be specified by the Graphics context.   
 * 
 * Creator: Cong Phuoc Huynh On: 09/05/2009, @ 9:57:50 PM Copyright 2009, RSES, ANU,
 * Australia.
 */
public class DrawShape implements Cloneable {
	
	/** The x and y coordinates of the vertices 
	 * (in the counter clockwise direction) */
	protected float xPoints[], yPoints[]; 
	
	
	private boolean selectionState;

	
	public DrawShape() {
		this.selectionState = false;
	}
			
	
    @Override
    /** Use prototype pattern to create the shape cheaply */
    public DrawShape clone() {
        DrawShape copy;
	    try {
	        copy = (DrawShape) super.clone();
	    } catch (CloneNotSupportedException unexpected) {
	        throw new AssertionError(unexpected);
	    }
	    return copy;
    }
    
    /**
     * Move the center of the shape to the specified location
     * with respect to the given coordinate system of the given 
     * Graphics context.
     * 
     * @param g 
     * @param centerx The x coordinate of the new center with respect to g.
     * @param centery The y coordinate of the new center with respect to g.
     */
    public void moveCenter(Graphics g, int centerx, int centery) {
    	// do nothing at the moment.
    }
    
    /**
     * Draw this DrawShape in the given graphics context paramGraphics, 
     * with the center of the shape being located at centerx, centery, 
     * with respect the the left top origin of the image coordinate system,
     * (the coordinate system of the viewing window)
     * and the given color.
     * 
     * Precondition: Before drawing this shape it is assumed that 
     * the current transformation matrix of the parameter Graphics context 
     * is the identity matrix (no transformations yet).
     * 
     * In this implementation, 
     * the polygon is filled with the color given by the style.
     *
	 * @param paramGraphics
	 * @param centerx the x coordinate in the image coordinate system.
	 * @param centery the y coordinate in the image coordinate system.
	 * @param style The drawing style to be used. 
	 */
	public void draw(Graphics paramGraphics, double centerx, double centery,
			ShapeStyle style) {
		int nPoints = xPoints.length;		
		
		Graphics2D g2d = (Graphics2D)paramGraphics;
      	AffineTransform oldTransform = g2d.getTransform();
      	
    	// create a star from a series of points
        GeneralPath drawPath = new GeneralPath();
      
        // set the initial coordinate of the General Path        
        drawPath.moveTo(xPoints[0], yPoints[0]);
      
        // create the star--this does not draw the star
        for ( int k = 1; k < nPoints; k++ )
        	drawPath.lineTo(xPoints[k], yPoints[k] );

        // close the shape
        drawPath.closePath();		
		
		// translate the origin to centerx, centery
        g2d.translate(centerx, centery);
        
                
        style.applyStyle(g2d); // apply the style.        
        g2d.fill(drawPath); // draw
        
		// revert back to the previous transform and color.
        style.revert(g2d);
        
        // revert g2d to the previous transformation 
        // before drawing this shape.
		g2d.setTransform(oldTransform);
		
	}

	public boolean isSelected() {
		return this.selectionState;
	}

	public void setSelected(boolean paramBoolean) {
		this.selectionState = paramBoolean;
	}
}
