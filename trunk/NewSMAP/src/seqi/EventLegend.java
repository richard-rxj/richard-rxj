package seqi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JPanel;


/**
 * Project: SEQI
 * Package: 
 * Type: EventLegend draws the legend to relate 
 * events' properties to their appearance.
 * 
 * Creator: Cong Phuoc Huynh
 * On: 20/06/2009, @ 12:25:31 PM
 * Copyright 2009.
 */
public class EventLegend extends JPanel {
	/** Lower bound of each (mb) magnitude interval, 
	 * must be sorted in increasing order */
	private final float[] magLowerBounds;
	
	/** Shape scales corresponding to the magnitude intervals above */
	private final float[] scales;
	
	/** Lower bound of each depth interval, in kms, 
	 * must be sorted in increasing order */
	private final float[] depthLowerBounds;
	
	/** Colors corresponding to the depth intervals above */
	private final Color[] colors;
	
	/**
	 * Create an EventLegend with the following properties.
	 * @param mbLb Lower bound of each (mb) magnitude interval, 
	 * 			must be sorted in increasing order.
	 * @param scales Shape scales corresponding to the magnitude intervals above. 
	 * @param depthLb Lower bound of each depth interval, in kms, 
	 * 			must be sorted in increasing order.
	 * @param colors Colors corresponding to the depth intervals above.
	 */
	public EventLegend(float[] mbLb, float[] scales, float[] depthLb, Color[] colors){		
		this.magLowerBounds = mbLb;
		this.scales = scales;
		this.depthLowerBounds = depthLb;
		this.colors = colors;		
		setBorder(BorderFactory.createTitledBorder("Event Legend"));	
		
		setPreferredSize(new Dimension(300, 100));
	}
	
	/**
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {		
		super.paintComponent(g);

		final int nScales = scales.length;
		final int nColors = colors.length;
			
		// Draw the world topology map, with scaling to fit in
		// its in the boundary of its view area.
		final Rectangle clipBound = g.getClipBounds();
		final int width = clipBound.width;
		final int height = clipBound.height;
		
		final int strWidth = 90;
		final int stringHeight = 15;
		
		final int borderWidth = 5;
		final int borderHeight = 10;
		
		final int magIntWidth = (int)((width - borderWidth - strWidth)/nScales);		
		final int magIntHeight = 10;
		
		
		// Draw magnitude scales
		g.drawString("Mb magnitude", borderWidth,  height/4 + stringHeight);
		g.setColor(Color.WHITE);
		DrawShape star = new StarShape(1);
		BorderedShape borderedStar = new BorderedShape(star);
		
		final int starCenterY = height/4 + stringHeight/2;
		
		for (int i = 0; i < nScales; i++) {
			// draw star shape at each scale.
			int starCenterX = strWidth + i * magIntWidth + magIntWidth/2;
			
			EventShapeStyle starShapeStyle = new EventShapeStyle(scales[i], Color.WHITE);			
			EventShapeStyle borderStarShapeStyle = new EventShapeStyle(scales[i], Color.BLACK);			
			borderedStar.setBorderStyle(borderStarShapeStyle);
			
			borderedStar.draw(g, starCenterX, starCenterY, starShapeStyle);
			
			// draw the caption
			g.setColor(Color.BLACK);
			g.drawString(String.valueOf(magLowerBounds[i]), 
					strWidth + i * magIntWidth + magIntWidth/2-10, height/2 + magIntHeight);
		}		
		
		
		// Draw depth scales.
		final int depthIntWidth = (int)((width - borderWidth - strWidth)/nColors);		
		final int depthIntHeight = 5;
		
		g.setColor(Color.BLACK);
		g.drawString("Depth (kms)", borderWidth, 3*height/4);		
		for (int i = 0; i < nColors; i++) {
			// draw the horizontal rule.
			g.setColor(colors[i]);
			g.fillRect(strWidth + i * depthIntWidth, 3*height/4-5, depthIntWidth, depthIntHeight);
			
			// draw the caption
			g.setColor(Color.BLACK);
			g.drawString(String.valueOf(depthLowerBounds[i]), 
					strWidth + i * depthIntWidth, 3*height/4 + depthIntHeight + 10);
		}
		
	}
}
