package seqi;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import util.TableSorter;


/**
 * EventListView provides a view for a list of CatalogEvents. Currently, this is
 * manifested as a table view.
 * 
 * Copyright (C) 2010, Research School of Earth Sciences, The Australian
 * National University
 * 
 * @author huynh
 * 
 *         Created on: May 14, 2010
 */
public class EventListView extends JFrame implements Observer {

	/** 
	 * A model of the event list, to be observed by 
	 * a JTable displayed on an interface.
	 */
	private EventTableModel tableModel;
	
	private JTable eventsInfoTable;
	
	/** Table sorter */
	private TableSorter tableSorter;
	
	/**
	 * The list currently loaded into this EventListView
	 */
	private EventList currentEvents;
	
	/** The list of events displayed in this View. */
	private MapView map;
	
	/**
	 * The checkbox to whether to select events or clear events.
	 */
	private JCheckBox displaySelectedOption;
	
	/**
	 * 
	 * @param map The MapView whose events is displayed 
	 * in the created EventListView.
	 */
	public EventListView(MapView map) {
		this.map = map;
		
		loadEventsFromMap();

		
		// This version of the code for table sorting 
		// applies to Java version 1.6 and beyond.

		// A Table of events in the center of the JFrame.
//		eventsInfoTable  = new JTable(tableModel);
//		eventsInfoTable.setFillsViewportHeight(true);
//		// eventsInfoTable.setAutoCreateRowSorter(true);
//
//		// Create a table sorter 
//		tableSorter = new TableRowSorter<EventTableModel>(tableModel);
//		eventsInfoTable.setRowSorter(tableSorter);
//
//		// sort table by the dates of the events.
//		tableSorter.setComparator(0, new Comparator<GregorianCalendar>() {
//			public int compare(Calendar o1, Calendar o2) {
//				return o1.compareTo(o2);
//			}
//		});
//		if (tableModel.getRowCount() > 0) {
//			tableSorter.sort();
//		}
		
		// Create sort keys for the sorter
		//List <RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
		//sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
		//tableSorter.setSortKeys(sortKeys);
		// end of Java 1.6 TableSorter
		
		
		// This version of the code for table sorting 
		// applies to Java version 1.5 and earlier.
		TableSorter tableSorter = new TableSorter(tableModel); 
		eventsInfoTable  = new JTable(tableSorter);
		tableSorter.setSortingStatus(0, TableSorter.ASCENDING);
		// end of Java 1.5 TableSorter
		

		// Create a table renderer for Gregorian Calendar objects
		// eventsInfoTable.getColumnModel().getColumn(0).setCellRenderer(new DateRenderer());
		eventsInfoTable.setDefaultRenderer(GregorianCalendar.class, new DateRenderer());
	    
        eventsInfoTable.setPreferredScrollableViewportSize(new Dimension(500, map.getHeight()));                
        
        
        
        // Set the preferred column widths.
        for (int i = 0; i < 7; i++) {
        	TableColumn column = eventsInfoTable.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(160);
            } else if ((i >= 1) && (i <= 3)) {
            	column.setPreferredWidth(40);
            } else {
                column.setPreferredWidth(20);
            }
        }

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(eventsInfoTable);

        //Add the scroll pane to this panel.
	    getContentPane().add(scrollPane, BorderLayout.CENTER);
	    
	    
	    /// A panel containing display options at the top of the JFrame. 
	    JPanel optionPanel = new JPanel();
	    displaySelectedOption = new JCheckBox("Show only selected events");
	    optionPanel.add(displaySelectedOption, false);	    
	    getContentPane().add(optionPanel, BorderLayout.NORTH);
	    
	    
	    // A listener for event selection.
	    // eventsInfoTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//	    ListSelectionModel eventListSelectionModel = eventsInfoTable.getSelectionModel();	    
//        eventListSelectionModel.addListSelectionListener(new ListSelectionListener() {
//			public void valueChanged(ListSelectionEvent e) {
//				int[] selectedRows = eventsInfoTable.getSelectedRows();
//				if (!EventListView.this.displaySelectedOption.isSelected()){
//					if (currentEvents != null)
//						currentEvents.setSelectedEvents(selectedRows);
//				} else {
//					
//				}
//			}
//		});

        
	    
	    
	    // Add an event listener for checkbox selection events	    
	    displaySelectedOption.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {		
				if (e.getSource() == EventListView.this.displaySelectedOption) {
					EventListView.this.updateEventListView();
				}
			}
		});
	    
    
		// Display the window
	    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	    setTitle("Filtered Event Information");
	    pack();
	    setVisible(true);
	}

	
	/**
	 * Load and display the current EventList in the MapView associated with this EventListView 
	 */
	private void loadEventsFromMap() {
		currentEvents = map.getEvents();
		if (currentEvents != null)
			currentEvents.addObserver(this); // notify this EventListView of the status changes of the events.

		if (tableModel == null)
			if (currentEvents != null) {
				// Initialise a TableModel from the event information.
				tableModel = new EventTableModel(currentEvents);
			}
			else
				// Initialise an empty TableModel
				tableModel = new EventTableModel();
		else
			// Update the table model.
			tableModel.setEventList(currentEvents);		
	}
	
	
	/**
	 * When the list of events displayed in the MapView changes,
	 * we also need to synchronize the event list to this EventListView.  
	 */
	public void update(Observable o, Object arg) {
		
		// If the MapView has changed its events.
		if (o instanceof UIChangeNotifier && o == map.getMapChangeNotifier()) {
			// Load the list of events currently displayed in the map.
			loadEventsFromMap();
		} 
			
		updateEventListView();
	}
	
	
	/** Update the list view of the events. */
	private void updateEventListView() {		
		if (currentEvents == null || currentEvents.getEvents().isEmpty()) {			
			// clear all rows in the event table.
			tableModel.setEventList(null);			
			return;
		}
		
		if (displaySelectedOption.isSelected()) {
			// Update the list view of the events.
			EventList selectedEvents = currentEvents.getSelectedEvents();
			tableModel.setEventList(selectedEvents);

		} else {			
	
			// Display the whole list of events
			tableModel.setEventList(currentEvents);

			// Highlight the selected events.
			eventsInfoTable.clearSelection();
			List<CatalogEvent> events = currentEvents.getEvents();
			int nEvents = events.size();
			for (int i = 0; i < nEvents; i++) {
				CatalogEvent event = events.get(i);
				if (event.isSelected) {
					eventsInfoTable.addRowSelectionInterval(i, i);
				}
			}
		}

		
// This sorting routine is only available for Java 1.6 and beyond.		
//		// Sort the table (by ascending Date by default)
//		if (tableModel.getRowCount() > 0) {
//			tableSorter.sort();
//		}
	}
}

class DateRenderer extends DefaultTableCellRenderer {
	SimpleDateFormat formatter; 
	
    public DateRenderer() { 
    	super(); 
    }

    public void setValue(Object value) {    	
    	formatter = new SimpleDateFormat("dd/MM/yyyy, hh:mm:ss a");
        setText((value == null) ? "" : formatter.format(((Calendar)value).getTime()));
    }
}




/**
 * EventTableModel stores the table model of an EventList.
 * Data in this model is reflected/updated in a JTable.
 */
class EventTableModel extends AbstractTableModel {
    private String[] columnNames =  { "Date/Time", "Longitude", "Latitude", "Depth", "mb", "ms", "mw" };
    private EventList eventList = new EventList();
    
    /**
     * Construct an EventTableModel.
     */
    public EventTableModel() {
    }
    
    
    /**
     * Construct an EventTableModel.
     * Pre-condition: The eventList must not be null.
     */
    public EventTableModel(EventList eventList) {	    	
    	setEventList(eventList);
    }
    
    /**
     * 
     * Set the event list to be rendered by this EventListView
     * @param eventList The event list to be displayed.
     * 	If eventList == null, then the table model is empty.
     *  
     */
    public void setEventList(EventList eventList) {
    	this.eventList = eventList;    	
    	if (eventList == null) {
    		fireTableDataChanged();
    		return;
    	}
    	
    	List<CatalogEvent> events = eventList.getEvents();    	
    	if (events == null) {
    		fireTableDataChanged();
    		return;
    	}

		int numEvents = eventList.getEvents().size();
		for (int i = 0; i < numEvents; i++) {
			CatalogEvent e = events.get(i);
			setValueAt(e.startDate, i, 0);
			setValueAt(e.longitude, i, 1);
			setValueAt(e.latitude, i, 2);
			setValueAt(e.depth, i, 3);
			setValueAt(e.mb, i, 4);
			setValueAt(e.ms, i, 5);
			setValueAt(e.mw, i, 6);					
		}
		fireTableDataChanged();
    }
    
    /**
     * @return The list of events currently displayed in this table model. 
     */
    public EventList getEventList() {
    	return eventList;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
    	if (this.eventList == null) 
    		return 0;
        return this.eventList.getEvents().size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
    	CatalogEvent e = eventList.getEvents().get(row);
    	
    	switch (col) {
			case 0:		
				// return e.startDate.getTime().toString();
				return e.startDate;
			case 1:
				return e.longitude;
			case 2:					
				return e.latitude;
			case 3:
				return e.depth;
			case 4:
				return e.mb;
			case 5:
				return e.ms;
			case 6:
				return e.mw;
			default:
				return -1; // Erroneous column number.
		} 
    }
    
    /**
     * @return A {@link CatalogEvent} in the given row.
     */
    public CatalogEvent getEvent(int row) {
    	if (eventList == null)
    		return null;
    	return eventList.getEvents().get(row);
    }

    public Class getColumnClass(int col) {
        return getValueAt(0, col).getClass();
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
    	return false;
    }
}