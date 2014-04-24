package seqi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * ZoomSlider
 * 
 * Copyright (C) 2010, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Jul 14, 2010
 */
public class ZoomSlider extends JPanel implements Observer {
	
	private JButton zoomOut, zoomIn; // Zoom in and out buttons
	private JSlider zoomScaleSlider; // Zoom scale slider
	private MapView map;
	
	/** 
	 * The logarithmic base of the zoom scale of the MapView.
	 * This is used to convert the zoom scale of the MapView 
	 * to the scale on the slider (which is logarithmic).
	 */
	private static final float ZOOM_LOG_BASE = 1.2f; 
	
	public ZoomSlider(MapView mapView) {
		this.map = mapView;
		mapView.addObserver(this);
		
		zoomOut = new JButton("-");
		zoomIn = new JButton("+"); 
		zoomScaleSlider = new JSlider(0, 15, 0);
				
		add(zoomOut);
		add(zoomScaleSlider);
		add(zoomIn);
		
		// Add listeners
		zoomScaleSlider.addChangeListener(new ChangeListener(){
			/* (non-Javadoc)
		 	* @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
			*/
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				if (source == zoomScaleSlider && source.getValueIsAdjusting()) { 
					// Need to add this condition in case the map is zoomed by another interaction method.
					// Since the knob on this slider is adjusted with the zoom scale of the map, 
					// we don't want the change in the slider's value to stop the actual zooming of the map. 
					
					int currentSliderVal = source.getValue(); // slider's value
					float currentMapZoomScale  = map.getZoomScale();   // zoom scale if the Map.
					map.zoomAboutPoint(map.getWidth()/2, map.getHeight()/2, (float)Math.pow(ZOOM_LOG_BASE, currentSliderVal)/currentMapZoomScale);
					//map.zoomAboutPoint(map.getWidth()/2, map.getHeight()/2, currentSliderVal);    //v1.7  
				}
			}
		});
		
		// Zoom in action
		zoomIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				map.zoomAboutPoint(map.getWidth()/2, map.getHeight()/2, ZOOM_LOG_BASE);				
			}
		});
		
		// Zoom out action
		zoomOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				map.zoomAboutPoint(map.getWidth()/2, map.getHeight()/2, 1/ZOOM_LOG_BASE);								
			}
		});
	
		// setPreferredSize(new Dimension(300, 20));
	}
	
	public void update(Observable o, Object arg) {
		if (o instanceof UIChangeNotifier && o  == map.getMapChangeNotifier()) {
			// Update the zoom scale on the slider	
			float mapZoomScale = map.getZoomScale();			
			// System.out.println("Slider scale " + (int)(Math.log(mapZoomScale)/Math.log(ZOOM_LOG_BASE)));			
			zoomScaleSlider.setValue((int)(Math.log(mapZoomScale)/Math.log(ZOOM_LOG_BASE)));	
			//zoomScaleSlider.setValue((int)(mapZoomScale));   //v1.7
		}
	}
}
