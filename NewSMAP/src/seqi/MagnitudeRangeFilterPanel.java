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
 * MagnitudeRangeFilterPanel provides a UI for users 
 * to interact with the MagnitudeRangeFilter. 
 * 
 * Copyright (C) 2009, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Dec 23, 2009
 */
public class MagnitudeRangeFilterPanel extends FilterPanel {

	/**
	 * The associated DistanceRangeFilter.
	 */
	private MagnitudeRangeFilter filter;
	
	private JFormattedTextField minMbField, maxMbField,
								minMsField, maxMsField,
								minMwField, maxMwField;
								
    private JLabel checkMbLabel, checkMsLabel, checkMwLabel;
	
	public MagnitudeRangeFilterPanel() {
		/// Prepare the filter.
		filter = new MagnitudeRangeFilter();
		filter.setMinMb(0);
		filter.setMaxMb(10);
		filter.setMinMs(0);
		filter.setMaxMs(10);
		filter.setMinMw(0);
		filter.setMaxMw(10);
		
		/// Prepare the GUI.
		// (in degrees) from a point. 
		GridBagLayout gridbag = new GridBagLayout();
		this.setLayout(gridbag);
		this.setBorder(new TitledBorder("Magnitude Range"));
		
		GridBagConstraints c = new GridBagConstraints();
		this.setToolTipText("Filter events whose Mb, Ms, Mw magnitudes fall inclusively within input ranges.");
		
		// First row - header row. 	
		JLabel minLabel = new JLabel("Min");
		c.gridx = 1; 
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;		
		c.weightx = 0; 
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;		
		this.add(minLabel, c);
		
		
		JLabel maxLabel = new JLabel("Max");
		c.gridx = 2; 
		this.add(maxLabel, c);
		
		
		// Each next 2 rows are for each type of magnitude (one for the input 
		// of the range ends and one for the error message if any) 

		/// Mb range
		// listener for the value change in the min Mb and max Mb fields.
		PropertyChangeListener mbChangeListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				float minMb = ((Number)minMbField.getValue()).floatValue();				
				float maxMb = ((Number)maxMbField.getValue()).floatValue();
				
				if (minMb <= maxMb) {
					filter.setMinMb(minMb);
					filter.setMaxMb(maxMb);					
					checkMbLabel.setText(" ");
				} else {
					checkMbLabel.setText("Error: min Mb > max Mb");
				}
			}
		};
		
		// Mb label
		JLabel mbLabel = new JLabel("Mb");
		c.gridx = 0; 
		c.gridy = 1;
		c.weightx = 0;
		this.add(mbLabel, c);
		
		
		// Min Mb field
		NumberFormat numberFormat = NumberFormat.getInstance();
		minMbField = new JFormattedTextField(numberFormat);
		minMbField.setValue(filter.getMinMb()); // initialized		
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1;	
		this.add(minMbField, c);
		minMbField.addPropertyChangeListener("value", mbChangeListener);
		
		
		// Max Mb field 		
		maxMbField = new JFormattedTextField(numberFormat);
		maxMbField.setValue(filter.getMaxMb());  		
		c.gridx = 2;
		c.gridy = 1;	
		this.add(maxMbField, c);		
		maxMbField.addPropertyChangeListener("value", mbChangeListener);
		
		// Label which displays an error message when maxMb < minMb
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 3;
		checkMbLabel = new JLabel(" ");
		checkMbLabel.setFont(new Font("Arial", Font.ITALIC, 12));
		checkMbLabel.setForeground(Color.RED);
		this.add(checkMbLabel, c);
		
		
		
		/// Ms range
		// listener for the value change in the min Ms and max Ms fields.
		PropertyChangeListener msChangeListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				float minMs = ((Number)minMsField.getValue()).floatValue();				
				float maxMs = ((Number)maxMsField.getValue()).floatValue();
				
				if (minMs <= maxMs) {
					filter.setMinMs(minMs);
					filter.setMaxMs(maxMs);					
					checkMsLabel.setText(" ");
				} else {
					checkMsLabel.setText("Error: min Ms > max Ms");
				}
			}
		};
		

		// Ms label
		JLabel msLabel = new JLabel("Ms");
		c.gridx = 0; 
		c.gridy = 3;
		c.weightx = 0;
		c.gridwidth = 1;
		this.add(msLabel, c);
		
		
		// Min Ms field
		minMsField = new JFormattedTextField(numberFormat);
		minMsField.setValue(filter.getMinMs()); // initialized		
		c.gridx = 1;
		c.gridy = 3;
		c.weightx = 1;	
		this.add(minMsField, c);
		minMsField.addPropertyChangeListener("value", msChangeListener);
		
		
		// Max Ms field 		
		maxMsField = new JFormattedTextField(numberFormat);
		maxMsField.setValue(filter.getMaxMs());
		c.gridx = 2;
		c.gridy = 3;	
		this.add(maxMsField, c);		
		maxMsField.addPropertyChangeListener("value", msChangeListener);
		
		// Label which displays an error message when maxMs < minMs
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 3;
		checkMsLabel = new JLabel(" ");
		checkMsLabel.setFont(new Font("Arial", Font.ITALIC, 12));
		checkMsLabel.setForeground(Color.RED);
		this.add(checkMsLabel, c);
		

		/// Mw range
		// listener for the value change in the min Mw and max Mw fields.
		PropertyChangeListener mwChangeListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				float minMw = ((Number)minMwField.getValue()).floatValue();				
				float maxMw = ((Number)maxMwField.getValue()).floatValue();
				
				if (minMw <= maxMw) {
					filter.setMinMw(minMw);
					filter.setMaxMw(maxMw);					
					checkMwLabel.setText(" ");
				} else {
					checkMwLabel.setText("Error: min Mw > max Mw");
				}
			}
		};
		

		// Mw label
		JLabel mwLabel = new JLabel("Mw");
		c.gridx = 0; 
		c.gridy = 5;
		c.weightx = 0;
		c.gridwidth = 1;
		this.add(mwLabel, c);
		
		
		// Min Mw field
		minMwField = new JFormattedTextField(numberFormat);
		minMwField.setValue(filter.getMinMw()); // initialized		
		c.gridx = 1;
		c.gridy = 5;
		c.weightx = 1;	
		this.add(minMwField, c);
		minMwField.addPropertyChangeListener("value", mwChangeListener);
		
		
		// Max Mw field 		
		maxMwField = new JFormattedTextField(numberFormat);
		maxMwField.setValue(filter.getMaxMw());  		
		c.gridx = 2;
		c.gridy = 5;	
		this.add(maxMwField, c);		
		maxMwField.addPropertyChangeListener("value", mwChangeListener);
		
		// Label which displays an error message when maxMw < minMw
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 3;
		checkMwLabel = new JLabel(" ");
		checkMwLabel.setFont(new Font("Arial", Font.ITALIC, 12));
		checkMwLabel.setForeground(Color.RED);
		this.add(checkMwLabel, c);
	}

	
	public EventFilter getFilter() {
		return filter;
	}

}
