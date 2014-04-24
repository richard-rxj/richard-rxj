package seqi;

import java.awt.Point;
import java.util.Vector;

	
/**
 * Project: SEQI
 * Package: 
 * Type: TriangleShape
 * 
 * Creator: Phuoc Huynh
 * On: 09/05/2009, @ 10:42:58 PM
 * Copyright 2009, RSES, ANU, Australia.
 */
public class TriangleShape extends DrawShape {	
	/**
	 * Create a triangle shape with three default 
	 * vertex locations.
	 */
	public TriangleShape(int scale) {
		super();		
		xPoints = new float[]{-scale, scale, 0};
		yPoints = new float[]{scale, scale, -scale};		
	}

}
