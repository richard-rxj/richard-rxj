package seqi;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

/**
 * TimeWindowFilterPanel
 * 
 * Copyright (C) 2009, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Dec 20, 2009
 */
public class TimeWindowFilterPanel extends FilterPanel {
	
	/**
	 * The associated DistanceRangeFilter.
	 */
	private TimeWindowFilter filter;
	
	
	/** GUI elements */
	// text fields to enter the start and end dates 
	private JFormattedTextField startDateField, endDateField;
	
	// The label displaying error messages when
	// startDate > endDate
	private JLabel checkDateLabel;
	
	
	public TimeWindowFilterPanel() {

		/// Prepare the filter.
		filter = new TimeWindowFilter();
		
		// Set the start and end dates of the time range filter.
		Calendar currentDate = GregorianCalendar.getInstance(); // current date
		filter.setStartDate(currentDate.getTime());
		filter.setEndDate(currentDate.getTime());
		
		
		/// Prepare the GUI.
		// (in degrees) from a point. 
		GridBagLayout gridbag = new GridBagLayout();
		this.setLayout(gridbag);		
		this.setBorder(new TitledBorder("Date Range"));
		
		GridBagConstraints c = new GridBagConstraints();
		this.setToolTipText("Filter events inclusively between the start and end dates");
				
		// Date format label
		JLabel dateFormatLabel = new JLabel("dd/mm/yyyy");		
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0; 
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;		
		c.anchor = GridBagConstraints.LINE_END;
		dateFormatLabel.setFont(new Font("Arial", Font.ITALIC, 12));
		this.add(dateFormatLabel, c);
		
		
		// Start date		
		JLabel startLabel = new JLabel("Start Date");
		c.gridx = 0; 
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;		
		c.weightx = 0; 
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;		
		this.add(startLabel, c);
		
		// listener for the value change in the start and end date fields.
		PropertyChangeListener dateChangeListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				Date startDate = (Date)startDateField.getValue();				
				Date endDate = (Date)endDateField.getValue();
				
				if (endDate.compareTo(startDate) >= 0) {
					filter.setStartDate(startDate);
					filter.setEndDate(endDate);					
					checkDateLabel.setText(" ");
				} else {
					checkDateLabel.setText("Error: start >= end date");
				}
			}
		};
		
		
		// Get a date format
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // date/month/year
		startDateField = new JFormattedTextField(dateFormat);
		startDateField.setValue(filter.getStartDate()); // initialized with the current date
			
		c.gridx = 1; 
		c.gridy = 1;
		c.weightx = 1;	
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(startDateField, c);		
		startDateField.addPropertyChangeListener("value", dateChangeListener);
		
		
		// End date		
		JLabel endLabel = new JLabel("End Date");
		c.gridx = 0; 
		c.gridy = 2;	
		c.weightx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(endLabel, c);

		endDateField = new JFormattedTextField(dateFormat);
		endDateField.setValue(filter.getEndDate()); // initialized with the current date
		
		
		c.gridx = 1; 
		c.gridy = 2;	
		c.weightx = 1;		
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(endDateField, c);		
		endDateField.addPropertyChangeListener("value", dateChangeListener);
		
		// Label which displays an error message when endDate < startDate
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		checkDateLabel = new JLabel(" ");
		checkDateLabel.setFont(new Font("Arial", Font.ITALIC, 12));
		checkDateLabel.setForeground(Color.RED);
		this.add(checkDateLabel, c);
		
	}
	
	
	/**
	 * @return The EventFilter which will take input information 
	 * from this TimeWindowFilterPanel.
	 */
	public EventFilter getFilter() {
		return filter;
	}
}
