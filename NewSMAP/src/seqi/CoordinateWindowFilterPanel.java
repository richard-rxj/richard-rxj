package seqi;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;


/**
 * CoordinateWindowFilterPanel
 * 
 * Copyright (C) 2010, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Jan 27, 2010
 */
public class CoordinateWindowFilterPanel extends FilterPanel {
	
	/**
	 * The associated CoordinateWindowFilter.
	 */
	private CoordinateWindowFilter filter;
	
	
	/**
	 * The associated MapView
	 */
	private MapView map;
	

	/**
	 * 
	 */
	public CoordinateWindowFilterPanel(MapView mapView) {
		
		/// Prepare the filter.
		filter = new CoordinateWindowFilter();
		
		this.map = mapView;		
		
		/// Prepare the GUI.
		// (in degrees) from a point. 
		GridBagLayout gridbag = new GridBagLayout();
		this.setLayout(gridbag);
		this.setBorder(new TitledBorder("Latitude & Longitude Range"));
		
		GridBagConstraints c = new GridBagConstraints();
		this.setToolTipText("Filter events whose latitude and longitude fall inclusively within a range.");

		
		// A button to change the mouse mode between the current one 
		// and the spatial coord selection mode.   
		final JButton mouseModeButton = new JButton("Select Window"); 
		c.gridx = 0; 
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1; 
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		this.add(mouseModeButton, c);

		// An area which displays the top-left and bottom-right 
		// longitudes and latitudes of the selected window.
		final JLabel topLeftLabel = new JLabel("Top-left:");
		c.gridx = 0; 
		c.gridy = 1;		
		this.add(topLeftLabel, c);
		
		
		final JLabel topLeftCoordsLabel = new JLabel(" "); 
		c.gridx = 0; 
		c.gridy = 2;
		this.add(topLeftCoordsLabel, c);
		

		final JLabel bottomRightLabel = new JLabel("Bottom-right:");
		c.gridx = 0; 
		c.gridy = 3;
		this.add(bottomRightLabel, c);
		
		final JLabel bottomRightCoordsLabel = new JLabel(" ");
		c.gridx = 0; 
		c.gridy = 4;
		this.add(bottomRightCoordsLabel, c);
		

		// Add listeners
		mouseModeButton.addActionListener(new ActionListener(){
			/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				MouseMode curMode = map.getMouseMode();
				
				if (!curMode.equals(MouseMode.REGION_SELECT)) {
					// Set mouse mode for station or point selection. 				
					map.enterMouseMode(MouseMode.REGION_SELECT);					
					mouseModeButton.setText("Confirm Selection");
				} else {
					map.revertMouseMode();					
					mouseModeButton.setText("Select Window");
					
					float[] tlCoords = map.getWindowTopLeftGeoCoords();
					float[] brCoords = map.getWindowBottomRightGeoCoords();
					
									
					// Update the selected coordinates
					if (tlCoords != null)
						topLeftCoordsLabel.setText("(" + 
							NumberFormat.getInstance().format(tlCoords[0]) + "," + 
							NumberFormat.getInstance().format(tlCoords[1]) + ")");
					
					if (brCoords != null)
						bottomRightCoordsLabel.setText("(" +
							NumberFormat.getInstance().format(brCoords[0]) + "," + 
							NumberFormat.getInstance().format(brCoords[1]) + ")");
					

					// Set the Coordinate Range of the CoordinateWindowFilter
					if (tlCoords != null && brCoords != null) {
						filter.setTopLeftLon(tlCoords[0]);
						filter.setTopLeftLat(tlCoords[1]);
						
						filter.setBottomRightLon(brCoords[0]);
						filter.setBottomRightLat(brCoords[1]);
					}
				}	
			}
		});		
	}


	
	/* (non-Javadoc)
	 * @see FilterPanel#getFilter()
	 */
	public EventFilter getFilter() {
		return filter;
	}
}
