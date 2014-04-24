package seqi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.Vector;

/**
 * StarShape.java
 * Created by: Phuoc Huynh
 * On: 09/05/2009 @ 11:02:11 PM
 */

/**
 * Project: SEQI
 * Package: 
 * Type: StarShape
 * 
 * Creator: Phuoc Huynh
 * On: 09/05/2009, @ 11:02:11 PM
 * Copyright 2009, RSES, ANU, Australia.
 */
public class StarShape extends DrawShape {

	
	/**
	 * Create a star shape with the default 
	 * vertex locations. 
	 */
	public StarShape(int scale) {
		super();
		xPoints = new float[]{0, -2, -8, -4, -6,  0, 6, 4, 8, 2 };
		yPoints = new float[]{-8, -2, -2, 2,  8, 4, 8, 2, -2, -2};
		
	    // xPoints = new int[]{ 55, 67, 109, 73, 83, 55, 27, 37, 1, 43 };
        // yPoints = new int[]{ 0, 36, 36, 54, 96, 72, 96, 54, 36, 36 };
			
//		// radius of the circle going through the outer vertices
//		this.outerRadius = outRadius;				
//		
//		double pi = Math.PI;
//		double ratio = Math.tan(72 * pi/180) /  Math.tan(56 * pi/180);		
//		// radius of the circle going through the inner vertices.
//		// this is proportional to r1.
//		double innerRadius = outerRadius / (ratio + 1) * Math.sin(56 * pi/180);
//		
//		Vector<Point> points = new Vector<Point>();
//		points.setSize(10);
//		
//		// store the outer vertices
//		double angle = pi/2; // first vertex corresponds to the one with 
//							 // x = 0 and y > 0		
//		for (int i = 0; i < 10; i = i + 2) {
//			points.set(i, new Point((int)(outerRadius * Math.cos(angle)), -(int)(outerRadius * Math.sin(angle))));			
//			angle += 72*pi/180;
//		}
//
//		// store the inner vertices
//		angle = pi/2 +  34 * pi / 180; // second vertex is next to the first one 
//										      // in the counter clockwise direction.	
//		for (int i = 1; i < 10; i = i + 2) {
//			points.set(i, new Point((int)(innerRadius * Math.cos(angle)), -(int)(innerRadius * Math.sin(angle))));
//			angle += 72*pi/180;
//		}
//		
//		setPoints(points);
//		
//		nPoints = xPoints.length; 
//        
//        Vector<Point> points = new Vector<Point>();
//        for (int i = 0; i < nPoints; i++) {
//        	xPoints[i]= xPoints[i] * scale;
//        	yPoints[i]= yPoints[i] * scale;
//        	
//        	points.add(new Point(xPoints[i], yPoints[i]));
//		}
//        setPoints(points);		
	}
	
	
	
//	/**
//     * Draw this StarShape in the given graphics context paramGraphics, 
//     * with the center of the shape being located at centerx, centery, 
//     * with respect the the left top origin of the image coordinate system,
//     * and the given color.
//     *
//	 * @param paramGraphics
//	 * @param centerx
//	 * @param centery
//	 * @param color
//	 */
//	public void draw(Graphics paramGraphics, int centerx, int centery, Color color) {
//		int nPoints = xPoints.length;
//
//		Color oldColor = paramGraphics.getColor();
//		
//      	Graphics2D g2d = (Graphics2D)paramGraphics;
//      	
//      	
//    	// create a star from a series of points
//        GeneralPath drawPath = new GeneralPath();
//      
//        // set the initial coordinate of the General Path        
//        drawPath.moveTo(xPoints[0], yPoints[0]);
//      
//        // create the star--this does not draw the star
//        for ( int k = 1; k < nPoints; k++ )
//        	drawPath.lineTo( xPoints[k], yPoints[k] );
//
//        // close the shape
//        drawPath.closePath();
//
//		g2d.setColor(color);
//		
//		AffineTransform oldTransform = g2d.getTransform();
//		
//		// translate the origin to centerx, centery 
//        g2d.translate(centerx, centery);
//        g2d.fill(drawPath);
//        g2d.setColor(Color.BLACK);
//        
//        g2d.draw(drawPath);
//                
//		
//		// revert back to the previous transform and color.
//        g2d.setColor(oldColor);
//		g2d.setTransform(oldTransform);
//	}

}
