package seqi;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;


/**
 * StationInfoPanel
 * 
 * Copyright (C) 2009, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Aug 28, 2009
 */
public class StationInfoDisplay extends JPanel {
	
	private JTextArea textArea;
	private JTable infoTable;
	
	public StationInfoDisplay() {
		this.setLayout(new GridBagLayout());		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; 
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH;		
		c.weightx = 1; 
		c.weighty = 1;
		
		// textArea = new JTextArea();
		// JScrollPane scroll = new JScrollPane(textArea);		
		infoTable = new JTable();
		JScrollPane scroll = new JScrollPane(infoTable);
		this.add(scroll, c);
		
		// Border 
		setBorder(BorderFactory.createTitledBorder("Station information"));
		this.setPreferredSize(new Dimension(180, 100));
	}
	
	/** 
	 * Set the station whose information will be displayed
	 * in this panel. 
	 * */
	public void setStation(final StationInfo station) {			
//		textArea.setText(
//				"Network  : " + station.network + "\n" +
//				"Name     : " + station.station + "\n" +
//				"Start    : " + station.startDate.getTime().toString() + "\n" +
//				"End      : " + station.endDate.getTime().toString() + "\n" +
//				"Longitude: " + station.lon + "\n" +
//				"Latitude : " + station.lat + "\n" +
//				"Depth    : " + station.depth + "\n" +	
//				"Location : " + station.networkLocation); 
		
	    AbstractTableModel  stationTableModel = new AbstractTableModel() {	    	
	    	private String[] columnNames = {"Attribute", "Value"};
	        private Object[][] data = {
					{"Network", station.network},
					{"Name", station.station},
					{"Start Date", station.startDate.getTime().toGMTString()}, 
					{"End Date", station.endDate.getTime().toGMTString()}, 
					{"Longitude", station.lon}, 
					{"Latitude", station.lat}, 
					{"Depth", station.depth}, 	
					{"Location", station.networkLocation}};

	        public String getColumnName(int col) {
	        	return columnNames[col].toString();
	        }
	        
	        public int getRowCount() {
	            return data.length;
	        }

	        public int getColumnCount() { 
	        	return columnNames.length; 
        	}
	        
	        public Object getValueAt(int row, int col) {
	            return data[row][col];
	        }
	        public boolean isCellEditable(int row, int col)	            
	        { 
	        	return false; 
	        }
	        public void setValueAt(Object value, int row, int col) {
	            data[row][col] = value;
	            fireTableCellUpdated(row, col);
	        }
	    };

	    infoTable.setModel(stationTableModel);
	    TableColumn attributeColumn = infoTable.getColumnModel().getColumn(0);
	    attributeColumn.setPreferredWidth(20);
	    
	    TableColumn valueColumn = infoTable.getColumnModel().getColumn(1);
	    valueColumn.setPreferredWidth(120);
	}
	
}
