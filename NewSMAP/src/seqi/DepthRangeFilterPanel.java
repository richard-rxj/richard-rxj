package seqi;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

/**
 * MagnitudeFilterPanel
 * 
 * Copyright (C) 2009, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Dec 20, 2009
 */
public class DepthRangeFilterPanel extends FilterPanel {
	
	/**
	 * The associated DistanceRangeFilter.
	 */
	private DepthRangeFilter filter;
	
	/** GUI elements */
	// text fields to enter the start and end dates 
	private JFormattedTextField minDepthField, maxDepthField;
	
	// The label displaying error messages when
	// startDate > endDate
	private JLabel checkDepthLabel;
	
	public DepthRangeFilterPanel() {

		/// Prepare the filter.
		filter = new DepthRangeFilter();
		
		// Set the depth range filter parameters.
		filter.setMinDepth(0);
		filter.setMaxDepth(100);
		
		
		/// Prepare the GUI.
		// (in degrees) from a point. 
		GridBagLayout gridbag = new GridBagLayout();
		this.setLayout(gridbag);
		this.setBorder(new TitledBorder("Depth Range"));
		
		GridBagConstraints c = new GridBagConstraints();
		this.setToolTipText("Filter events whose depth falls inclusively within a range.");
		
		// Min depth		
		JLabel startLabel = new JLabel("Min (kms)");
		c.gridx = 0; 
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;		
		c.weightx = 0; 
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;		
		this.add(startLabel, c);
		
		// listener for the value change in the min and max depth fields.
		PropertyChangeListener depthChangeListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				float minDepth = ((Number)minDepthField.getValue()).floatValue();				
				float maxDepth = ((Number)maxDepthField.getValue()).floatValue();
				
				if (minDepth <= maxDepth) {
					filter.setMinDepth(minDepth);
					filter.setMaxDepth(maxDepth);					
					checkDepthLabel.setText(" ");
				} else {
					checkDepthLabel.setText("Error: min > max depth");
				}
			}
		};
		
		
		// Set up a number format
		NumberFormat numberFormat = NumberFormat.getInstance();
		minDepthField = new JFormattedTextField(numberFormat);
		minDepthField.setValue(filter.getMinDepth()); // initialized		
		c.gridx = 1; 
		c.gridy = 0;
		c.weightx = 1;	
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(minDepthField, c);		
		minDepthField.addPropertyChangeListener("value", depthChangeListener);
		
		
		// End date		
		JLabel endLabel = new JLabel("Max (kms)");
		c.gridx = 0; 
		c.gridy = 1;	
		c.weightx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(endLabel, c);

		maxDepthField = new JFormattedTextField(numberFormat);
		maxDepthField.setValue(filter.getMaxDepth()); // initialized 		
		c.gridx = 1; 
		c.gridy = 1;	
		c.weightx = 1;		
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(maxDepthField, c);		
		maxDepthField.addPropertyChangeListener("value", depthChangeListener);
		
		// Label which displays an error message when endDate < startDate
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		checkDepthLabel = new JLabel(" ");
		checkDepthLabel.setFont(new Font("Arial", Font.ITALIC, 12));
		checkDepthLabel.setForeground(Color.RED);
		this.add(checkDepthLabel, c);
		
	}
	

	
	/**
	 * @return The EventFilter which will take input information 
	 * from this DepthFilterPanel.
	 */
	public EventFilter getFilter() {
		return filter;
	}
}
