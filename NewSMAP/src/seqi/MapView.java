package seqi;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Stack;
import javax.swing.JComponent;

import util.SphericalCoords;


/**
 * MapView
 * 
 * Copyright (C) 2010, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Mar 26, 2010
 */
/**
 * MapView
 * 
 * Copyright (C) 2010, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Jul 16, 2010
 */
public class MapView extends JComponent implements Observer {


	/** Colours for events */
	private static final Color selectedEventColor = Color.DARK_GRAY;
	private static final Color unselectedEventColor = Color.YELLOW;

	private static final Color selectedStationColor = Color.YELLOW;
	private static final Color unselectedStationColor = new Color(128, 0, 128);

	// the default size of the station shape.
	private static final int STATION_SHAPE_SIZE = 4;

	// minimum allowable zoom scale
	// (scale = 1 corresponds to the whole world map fully displayed
	// in this MapView)
	private static final double MIN_ZOOM_SCALE = 1;
	
	
	// maximum allowable zoom scale     ----------------add new feature v1.7
	private static final double MAX_ZOOM_SCALE = 20;
	
	
	// the zoom level at which and beyond which the station's name 
	// will be displayed.
	private static final double STAT_NAME_DISPLAY_ZOOM_SCALE = 15; 

	// the String storing the path to the map file location.
	private static final String MAP_IMAGE_LOC = SEQI.SEQI_HOME + "/images/GLOBALeb3litcolshade_ver2.jpg";	
	// private static final String MAP_IMAGE_LOC = "/images/Earth-whole-planet-2D-map.gif";
	
	
	// The main application class of the Seismic Event Query Interface,
	// that contains this map view.
	protected SEQI mainApp;

	private Toolkit tk;
	private Image mapImg; // the topology map of the world

	// The (minimum and maximum)
	// longitudes and latitudes (in degrees) of the bottom left
	// and top right corners of the map.
	// Note that longMin must be <= longMax
	// and latMin must be <= latMax
	protected final float longMin = -180, latMin = -90, longMax = 180, latMax = 90;
	private final float longExtent = longMax - longMin, latExtent = latMax - latMin;

	private EventList eventList; // the events currently drawn in the map.
	protected Set<StationInfo> stations; // the stations currently drawn in the
										// map.

	private float zoomScale = 1; // the zoom scale of this MapView.

	// The (image) pixel coordinates of the top left corner of
	// the map image with respect to the origin of the viewing area.
	private float xOrigin = 0, yOrigin = 0;
	
	// Listeners which listen to zooming and selection 
	// commands by clicking the left and right mouse buttons.
	private MouseDragTranslateListener mouseDragTranslateListener;
	
	private MouseWheelZoomListener mouseWheelZoomListener;
	
	private RegionZoomListener regionZoomListener;
	
	private ClickSelectionListener clickSelectionListener;
	
	private RegionSelectionListener regionSelectionListener;
	
	
	// Drawing mode on the view. The use of this variable is to avoid
	// unnecessarily duplicated rendering. 
	protected DrawMode drawMode;
	
	// The current mode of interaction by mouse with the map view
	protected MouseMode mouseMode;
	
	
    // The longitude and latitude of an epi-center point selected as center for
    // event filtering.
    // This is either a station location or a user-selected location.
    private float[] distRangeFilterCenterCoords = null;
    
    // Epi-center type, either station or user-selected point.
    enum EpiCenterType {STATION, POINT};
    private EpiCenterType epiCenterType = EpiCenterType.POINT; // initially
    
    
	// The radii of the inner and outer circles centered at a selected point.
    // The circles are drawn whenever the points or stations are selected. 
    private float innerRadius, outerRadius;
    
    // An object whose functionality is to notify all the Observers of 
    // this MapView of its changes.
    private final UIChangeNotifier mapChangeNotifier = new UIChangeNotifier();
    
    // The filter currently used (if any) to filter events displayed in the map.
    private EventFilter filter;
    
    // A flag indicating whether the stations should be shown.
    private boolean showStations = true;
    
    // A flag indicating that only selected events should be shown.
    private boolean showOnlySelectedEvents;
    
    // A flag to switch the great circle path plots on/off
    private boolean plotGCPathEnabled = false;
    
	public MapView(SEQI mainApp) {
		this.mainApp = mainApp;		

		this.tk = Toolkit.getDefaultToolkit();
		
		loadMap(MAP_IMAGE_LOC, longMin, longMax, latMin, latMax);
		
		// Initialize mouse listeners
		mouseDragTranslateListener = new MouseDragTranslateListener(this);
		
		mouseWheelZoomListener = new MouseWheelZoomListener(this);
		clickSelectionListener = new ClickSelectionListener(this);
		regionZoomListener = new RegionZoomListener(this);
		regionSelectionListener = new RegionSelectionListener(this);
		
		// Optional drawing elements
		// selectedRegion = new Rectangle();
		
		// Set initial mouse mode
		enterMouseMode(MouseMode.MOUSEWHEEL_ZOOM);
		

		// Add mouse motion listener
		addMouseMotionListener(new MapViewMouseMotionListener(this));
		
		
		// Miscellaneous settings
		setFocusable(true);
		
		mapChanged = true;
	}

    
    /**
     * @return The MapChangeNotifier of this MapView.
     */
	protected UIChangeNotifier getMapChangeNotifier() {
		return mapChangeNotifier;
	}


	/**
	 * Add the given observer to the list of observers of this MapView.
	 * 
	 * @param observer an Observer.
	 */
	public void addObserver(java.util.Observer observer) {		
		mapChangeNotifier.addObserver(observer);
	}
	
	/**
	 * Notify all the observers of the changes in this MapView. 
	 */
	public void notifyObservers() {
		mapChangeNotifier.notifyObservers();
	}
	
	

	/**
	 * Load a map image with the specified longitude and latitude (in degrees)
	 * of the top left and bottom right corners of the map.
	 */
	protected void loadMap(String mapFilePath, double longMin, double longMax,
			double latMin, double latMax) {
		mapImg = tk.getImage(mapFilePath);
	}

	/**
	 * Set the stations to be drawn.
	 */
	public void setStations(Set<StationInfo> stations) {
		this.stations = stations;
	}

	/**
	 * Set the catalog events to be loaded and render them.
	 */
	public void setEvents(EventList eventList) {
		this.eventList = eventList;
		
		this.eventList.addObserver(this);
		
		// redraw the map
		mapChanged = true;
		notifyObservers();
		repaint();
	}
	
	/** 
     * Set the filter currently used (if any) to filter events displayed in the map.
     */
	public void setFilter(EventFilter filter) {
		this.filter = filter;
		
		mapChanged = true;
		repaint();
		mapChanged = false;
	}
	
	
	/**
	 * Get the event list currently being loaded into this MapView. 
	 */
	public EventList getEvents() {
		return eventList;		
	}
	
    
    /**
     * Set a list of selected stations.
     * Display the information of the first station in the list
     * on the GUI.
     * 
     * @param selectedStations The list of selected stations.  
     */
    public void setSelectedStations(StationInfo[] selectedStations) {
    	// Deselect all the currently selected stations
    	// in the mapView
		for (StationInfo station : this.stations) {
			station.setSelected(false);
		}

		// Highlight the selected stations.
		for (Object station : selectedStations) {											
			((StationInfo)station).setSelected(true);
		}		
		
		// If only one station is selected, it is regarded as the 
		// reference point for the distance range filter.
		if (selectedStations.length == 1) {
			float[] stationCoords = new float[]{selectedStations[0].lon, selectedStations[0].lat};
			setEpiCenterCoords(stationCoords);
		}
		
    	// repaint the map.
		repaint();
    }
    
    
    //// Methods to stores the data resulted from //// 
    //// user interactions with the map 		  ////

    
    /**
     * Set the longitude and latitude of the epi-center 
     * of the inner and outer circles to be plotted.
     * 
     * @param A 2-element array, where the first element is the longitude
     * and the second one is the latitude of the circle center. 
     */
    public void setEpiCenterCoords(float[] epiCenterCoords) {
    	this.distRangeFilterCenterCoords = epiCenterCoords;
    	
    	// Notify the GUIs to update the epicenter coordinates. 
    	notifyObservers();    	
    }
    
    /**
     * @return The epi-center coordinates.
     */
    public float[] getEpiCenterCoords() {
		return distRangeFilterCenterCoords;
	}

	/**
     * Set the type of epi-center point used as the center for 
     * distance range filtering and plotting. 
     */
    public void setEpiCenterType(EpiCenterType epiCenterType) {
		this.epiCenterType = epiCenterType;
		notifyObservers();
	}

    /**
     * @return  the type of epi-point used as the center for distance range filtering
     * and plotting. 
     */
    public EpiCenterType getEpiCenterType() {
		return this.epiCenterType;
	}

    
    /** Set the radius of the inner circle to be drawn around the selected epi-center. */
	public void setInnerRadius(float innerRadius) {
		this.innerRadius = innerRadius;
		
		// Notify the GUIs to update the epicenter coordinates. 
    	notifyObservers();
    	
    	//repaint();
	}

	/** Set the radius of the outer circle to be drawn around the selected epi-center. */
	public void setOuterRadius(float outerRadius) {
		this.outerRadius = outerRadius;
		
		// Notify the GUIs to update the epicenter coordinates. 
    	notifyObservers();
    	
    	//repaint();
	}    
	
	
	/**
	 * @return The minimal (top-left) longitude and latitude 
	 * of the rectangular window currently selected on this MapView, 
	 * respectively, in the first and second element of the returned array.
	 */
	public float[] getWindowTopLeftGeoCoords() {
		float[] windowTopLeftGeoCoords  
			= imageToGeoCoords(regionSelectionListener.startPoint.x, regionSelectionListener.startPoint.y);
		// System.out.println(" selection listener " + regionSelectionListener);
		
		return windowTopLeftGeoCoords;
	}

	/**
	 * @return The bottom-right longitude and latitude 
	 * of the rectangular window currently selected on this MapView, 
	 * respectively, in the first and second element of the returned array.
	 */
	public float[] getWindowBottomRightGeoCoords() {
		float[] windowBottomRightGeoCoords  
			= imageToGeoCoords(regionSelectionListener.endPoint.x, regionSelectionListener.endPoint.y);
		return windowBottomRightGeoCoords;
	}
	

	/**
     * Set the flag indicating whether stations are shown.
	 * @param showStations	true if we want to show the stations on the map,
     * 						false otherwise.
	 */
    public void setShowStations(boolean showStations) {
		this.showStations = showStations;
	}

    
	/**
     * Set the flag indicating whether only selected events are shown
     * @param showOnlySelectedEvents true if we want to show only selected events.
     * 								 false if we want to show all the events. 
     */
    public void setShowOnlySelectedEvents(boolean showOnlySelectedEvents) {
		this.showOnlySelectedEvents = showOnlySelectedEvents;
	}
    
    
    /**
     * Set a flag to indicate whether to draw great circle paths from the reference 
     * center of the distance range filter (if available) to Catalog Events. 
     */
	public void setPlotGCPathEnabled(boolean plotGCPathEnabled) {
		this.plotGCPathEnabled = plotGCPathEnabled;
	}


	//// Drawing functions ////
    enum DrawMode {drawStations,  drawEvents, drawSelectedRegion;}
	
    /**
     * A BufferedImage which stores a temporary rendering of the map 
     * in memory. This buffer currently includes stations, events, 
     * latitude and longitude markers, and no filter's information 
     * or user's interaction indication.
     */
    protected BufferedImage bufferWithNoFilterInfo;
    
    /**
     * The final buffer map to be output to the display, with 
     * markers for user's selection and filter's properties.
     * This is also the buffer to be saved on disk on user's request.
     */
    protected BufferedImage outputBuffer;
    
    /** 
     * A flag indicating whether the map has to be re-drawn 
     * in the next call to paintComponent(Graphics g).
     * 
     * Setting flag == false before calling paintComponent(Graphics g)
     * will bypass the re-rendering of stations, 
     * events and the latitudes and longitudes, thus the rendering will
     * be much faster. This setting can be used only when there is no changes 
     * in the event lists and map navigation, and the contents added to the 
     * map are due to additional markers about filter information or user's interaction.
     * 
     * On all other occasions, this flag has to be set to true before every call 
     * to paintComponent(Graphics g), resulting the map to be re-drawn.
     *  
     */
    protected boolean mapChanged = false;
    

    /**
     * Repaint the map once any change of its content is notified.
     */
	public void update(Observable o, Object arg) {
		repaint();
	}
    
	
	/**
	 * Paint the MapView component.
	 */
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D outputGraphics;
		if (mapChanged) {		
			// Render everything we want to display in the bufferedImage first.
			final int viewWidth = this.getWidth();
			final int viewHeight = this.getHeight();
					
			// Draw the world topology map, with scaling to fit
			// it in its own viewing area.
			bufferWithNoFilterInfo = new BufferedImage(viewWidth, viewHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = bufferWithNoFilterInfo.createGraphics();
			
			AffineTransform oldTransform = g2d.getTransform();
	
			// translate the origin of the map and scale it.
			g2d.translate(xOrigin, yOrigin);
			g2d.scale(zoomScale, zoomScale);
	
			g2d.drawImage(mapImg, 0, 0, viewWidth, viewHeight, this);
			
			// draw a second map on the right hand side of the first one
			// to create an effect that the map wraps around.		
			g2d.drawImage(mapImg, viewWidth, 0, viewWidth, viewHeight, this);
			
			// revert the scaling and transformation before drawing 
			// the stations and events.
			g2d.setTransform(oldTransform);
	
			// Draw stations
			if (showStations) drawStations(g2d);
			
			
			// Draw events
			drawEvents(g2d);
			
			// Draw the longitudes and latitudes
			drawLatsLons(g2d);
			
			// If an epi-center has been selected on the map, draw it
			if (distRangeFilterCenterCoords != null && distRangeFilterCenterCoords.length == 2) {
				drawEpicenter(g2d, distRangeFilterCenterCoords[0], distRangeFilterCenterCoords[1]);
			}
			
			// Draw the great circle paths from the epi-center (reference center of 
			// the distance range filter) to the filtered events on the map. 
			if (plotGCPathEnabled) {
				drawGCPaths(g2d);
			}			
			
			// Now create the final output buffer.
			outputBuffer = new BufferedImage(viewWidth, viewHeight, BufferedImage.TYPE_INT_RGB);
			outputGraphics = outputBuffer.createGraphics();
			
			// Copy the buffer with no filter and user's interaction information
			// into the output buffer.
			outputGraphics.drawImage(bufferWithNoFilterInfo, 0, 0, null);
			
			// Draw filter information on this MapView.
			if (filter != null)
				filter.drawFilterInfo((Graphics2D)outputBuffer.getGraphics(), this);			
			
		} else {
			// outputGraphics = (Graphics2D)outputBuffer.getGraphics();			
		}
		
		// Now draw the outputBuffer on the screen.
        g.drawImage(outputBuffer, 0, 0, null);
	}
	
    
    /**
     * Draw a rectangle in the filter marker buffer. 
     * @param topLeftGeoCoords: an array storing the x and y coordinates 
     * 			of the top-left corner of the window to be drawn in this MapView.
     * @param bottomRightGeoCoords: an array storing the x and y coordinates 
     * 			of the bottom-right corner of the window to be drawn in this MapView.
     */
    protected void drawCoordWindow(int[] topLeftImageCoords, int[] bottomRightImageCoords) {
		// Render everything we want to display in the bufferedImage first.
		final int viewWidth = this.getWidth();
		
		// Copy the content of the buffered map without markers.
		Graphics g = outputBuffer.getGraphics();
		g.drawImage(bufferWithNoFilterInfo, 0, 0, null);
		

		// Set a color for drawing
		Color curColor = g.getColor();
		g.setColor(Color.YELLOW);
	
		if ((int)topLeftImageCoords[0] < (int)bottomRightImageCoords[0]) {
			g.drawRect((int)topLeftImageCoords[0], (int)topLeftImageCoords[1], 
				(int)(bottomRightImageCoords[0] - topLeftImageCoords[0]) + 1, 
				(int)(bottomRightImageCoords[1] - topLeftImageCoords[1]) + 1);
		} else {
			// Draw two half rectangles
			g.drawRect((int)topLeftImageCoords[0], (int)topLeftImageCoords[1], 
					viewWidth - (int)(bottomRightImageCoords[0]) + 1, 
					(int)(bottomRightImageCoords[1] - topLeftImageCoords[1]) + 1);
			g.drawRect(-1, (int)topLeftImageCoords[1], 
					(int)bottomRightImageCoords[0] + 2, 
					(int)(bottomRightImageCoords[1] - topLeftImageCoords[1]) + 1);
		}
		
		
		g.setColor(curColor);
    }
	
	
	/** 
	 * Get the latest rendering output of the seismic map that has been saved in memory. 
	 */
	protected BufferedImage getBufferedMap() {
		return outputBuffer;
	}

	/**
	 * Draw all the stations set to be drawn in this map.
	 */
	private void drawStations(Graphics g) {
		// Don't need to draw an empty set
		if (stations == null)
			return;

		// final int viewWidth = this.getWidth();
		// final int viewHeight = this.getHeight();

		Graphics2D g2d = (Graphics2D)g;

		// Draw all the unselected stations first.
		for (StationInfo stat : stations) {

			if (stat.isSelected) continue;
			
			// draw a polygon at the event location for each event
			// Center of the shape is (lon, lat)
			DrawShape triangle;

			// Set the station color based on selection status
			EventShapeStyle stationStyle;
			ShapeStyle stationBorderStyle;

			stationStyle = new EventShapeStyle(1, unselectedStationColor);
			stationBorderStyle = new EventShapeStyle(1, Color.WHITE);
			triangle = new TriangleShape(STATION_SHAPE_SIZE);

			
			BorderedShape borderedTriangle = new BorderedShape(triangle, stationBorderStyle);
			
//			float longitude = stat.lon;
//			float latitude = stat.lat;
//
//			// Compute the image coordinates of the current station.			
//			float x1 = (float)(longitude - this.longMin)/this.longExtent;	
//			
//			// invert the y direction because the coordinate origin of the Graphics context  
//			float y1 = 1 - (float)(latitude -this.latMin)/ this.latExtent;
//
//			float x2 = (xOrigin + x1 * zoomScale * viewWidth);
//			float y2 = (yOrigin + y1 * zoomScale * viewHeight);
//			
//			float x3 = x2 + zoomScale * viewWidth;
//			if (x3 <= viewWidth) { // wrap the location around
//				x2 = x3;
//			}	
//			borderedTriangle.draw(g2d, x2, y2, stationStyle);

			
			float[] imageCoords = geoToImageCoords(stat.lon, stat.lat); 
			borderedTriangle.draw(g2d, imageCoords[0], imageCoords[1], stationStyle);
			
			// Draw the station's name
			if (zoomScale > STAT_NAME_DISPLAY_ZOOM_SCALE) {
				// g2d.drawString(stat.station, x2 - 15, y2 + 20);
				g2d.drawString(stat.station, imageCoords[0]- 15, imageCoords[1] + 20);
			}
		}
		
		
		// Draw the selected stations on top (in front).
		// center of the shape is (lon, lat)
		// This is a lot of code redundancy, but is the easiest way 
		// of plotting stations.
		for (StationInfo stat : stations) {

			if (!stat.isSelected) continue;
			
			// draw a polygon at the event location for each event
			DrawShape triangle;

			// Set the station color based on selection status
			EventShapeStyle stationStyle;
			ShapeStyle stationBorderStyle;
			stationStyle = new EventShapeStyle(1, selectedStationColor);
			stationBorderStyle = new EventShapeStyle(1, Color.BLACK);
			triangle = new TriangleShape(STATION_SHAPE_SIZE + 2); // bigger or easier localization
			
			BorderedShape borderedTriangle = new BorderedShape(triangle, stationBorderStyle);
			
//			float longitude = stat.lon;
//			float latitude = stat.lat;
//
//			// Compute the image coordinates of the current station.			
//			float x1 = (float)(longitude - this.longMin)/this.longExtent;	
//			
//			// invert the y direction because the coordinate origin of the Graphics context  
//			float y1 = 1 - (float)(latitude -this.latMin)/ this.latExtent;
//
//			float x2 = (xOrigin + x1 * zoomScale * viewWidth);
//			float y2 = (yOrigin + y1 * zoomScale * viewHeight);
//			
//			float x3 = x2 + zoomScale * viewWidth;
//			if (x3 <= viewWidth) { // wrap the location around
//				x2 = x3;
//			}
//			
//			borderedTriangle.draw(g2d, x2, y2, stationStyle);
			
			float[] imageCoords = geoToImageCoords(stat.lon, stat.lat); 
			borderedTriangle.draw(g2d, imageCoords[0], imageCoords[1], stationStyle);
			
			
			// Draw the station's name
			if (zoomScale > STAT_NAME_DISPLAY_ZOOM_SCALE) {
				// g2d.drawString(stat.station, x2 - 15, y2 + 20);
				g2d.drawString(stat.station, imageCoords[0]- 15, imageCoords[1] + 20);
			}
		}
	}
	
	
	
	
	/**
	 * Draw all the events selected to be displayed in this map.
	 */
	private void drawEvents(Graphics g) {
		// Don't need to draw an empty set
		if (eventList == null)
			return;

		final int viewWidth = this.getWidth();
		final int viewHeight = this.getHeight();

		// draw a polygon at the event location for each event
		DrawShape star = new StarShape(1);

		BorderedShape borderedStar = new BorderedShape(star);

		DrawStyleManager styleManager = DrawStyleManager.getStyleManager();

		// center of the shape is (lon, lat)
		final List<CatalogEvent> events = eventList.getEvents();
		
		// Draw unselected events first.
		if (!showOnlySelectedEvents) { 
			for (CatalogEvent e : events) {
				if (e.isSelected) continue;
				
				// If the event has not been selected for drawing
				// if (!e.isDisplayed) continue;
				float longitude = e.longitude;
				float latitude = e.latitude;
	
				ShapeStyle eventStyle = styleManager.getStyle(e);
				ShapeStyle eventBorderStyle = styleManager.getBorderStyle(e,
						Color.BLACK);
				borderedStar.setBorderStyle(eventBorderStyle);
	
				float x1 = (float)(longitude - this.longMin)/this.longExtent;			
				// invert the y direction because the coordinate origin of the Graphics context  
				float y1 = 1 - (float)(latitude -this.latMin)/ this.latExtent;
	
				float x2 = (xOrigin + x1 * zoomScale * viewWidth);
				float y2 = (yOrigin + y1 * zoomScale * viewHeight);
				
				float x3 = x2 + zoomScale * viewWidth;
				if (x3 <= viewWidth) { // wrap the location around
					x2 = x3;
				}			 
				
				borderedStar.draw(g, x2, y2, eventStyle);
				
				// Rather inline the method lonLatToImageCoords(e.longitude, e.latitude) 
				// as above to reduce the number of function calls.
				// float[] imageCoords = lonLatToImageCoords(e.longitude, e.latitude);
				// borderedStar.draw(g, imageCoords[0], imageCoords[1], eventStyle);
			}
		}
		
			
		// Draw selected events in front of the unselected ones. 
		for (CatalogEvent e : events) {
			
			if (e.isSelected) {			
				// If the event has not been selected for drawing
				// if (!e.isDisplayed) continue;
				float longitude = e.longitude;
				float latitude = e.latitude;
	
				ShapeStyle eventStyle = styleManager.getStyle(e);
				ShapeStyle eventBorderStyle = styleManager.getBorderStyle(e,
						Color.BLACK);
				borderedStar.setBorderStyle(eventBorderStyle);
	
				float x1 = (float)(longitude - this.longMin)/this.longExtent;			
				// invert the y direction because the coordinate origin of the Graphics context  
				float y1 = 1 - (float)(latitude -this.latMin)/ this.latExtent;
	
				float x2 = (xOrigin + x1 * zoomScale * viewWidth);
				float y2 = (yOrigin + y1 * zoomScale * viewHeight);
				
				float x3 = x2 + zoomScale * viewWidth;
				if (x3 <= viewWidth) { // wrap the location around
					x2 = x3;
				}			 
				
				borderedStar.draw(g, x2, y2, eventStyle);
				
				// If the current event has been selected by mouse, 
				// mark their selection with a cross.
			
				// Convert the polar coordinates into image coordinates.
				float[] imageCoords = geoToImageCoords(longitude, latitude);
				
				Color curColor = g.getColor();
				g.setColor(Color.BLUE);

				g.drawLine((int)imageCoords[0]-10, (int)imageCoords[1], (int)imageCoords[0]+10, (int)imageCoords[1]);
				g.drawLine((int)imageCoords[0], (int)imageCoords[1]-10, (int)imageCoords[0], (int)imageCoords[1]+10);
				
				// Revert to the current colour
				g.setColor(curColor);				
			}
		}
	}
	
	/**
	 * Draw all the major longitudes and latitudes (depending on the map extent)
	 */
	private void drawLatsLons(Graphics g) {
		final int viewWidth = this.getWidth();
		final int viewHeight = this.getHeight();

		// draw longitudes
		int i;
		
		// The spacing j between longitudinal
		// and latitudinal lines 
		// is changed adaptive according to the zoom level
		int j = (int)Math.ceil(45/zoomScale);		
		// find the smallest divisor of 90 that is larger than j
		while (90 % j != 0) {
			j++;
		}
		
		g.setColor(Color.black);
		for (i = (int) Math.round(this.longMin); 
			i < (int) Math.round(this.longMax); ++i) {
			if (i % j == 0) {
				drawLon(g, i, viewWidth, viewHeight);
			}
		}
		
		for (i = (int) Math.round(this.latMin); 
			i <= (int) Math.round(this.latMax); ++i) {
			if (i % j == 0) {
				drawLat(g, i, viewWidth, viewHeight);
			}
		}
	}

	/**
	 * Draw a longitudinal line with the longitude given by the
	 *            .
	 * @param longitude
	 *            The longitude to be drawn.
	 * @param viewWidth
	 *            the width of the view area of the map, in pixels.
	 * @param viewHeight
	 *            the height of the view area of the map, in pixels.
	 */
	private void drawLon(Graphics g, int longitude, int viewWidth,
			int viewHeight) {

		if ((longitude > longMax) || (longitude < longMin)) {
			System.err.println("Longitude " + longitude + " out of bound.");
			return;
		}
		
		// normalised x-coordinates (between 0 and 1).
		double i1 = (double) (longitude - this.longMin)/this.longExtent;

		// convert the longitude to the image coordinate 
		int i2 = (int)(xOrigin + i1 * zoomScale * viewWidth);
		
		// wrap around the x-coordinate of the longitude
		int i3 = i2 + (int) (zoomScale * viewWidth);
		
		if (i3 <= viewWidth) { // wrap the location around
			i2 = i3;
		}
		
		String str = String.valueOf(longitude);		

		int j = g.getFontMetrics().stringWidth(str);
		
		Color curColor = g.getColor();

		g.setColor(Color.gray);

		g.drawLine(i2, 15, i2, viewHeight - 15);

		g.setColor(Color.lightGray);

		g.drawString(str, i2 - j / 2, 14);
		g.drawString(str, i2 - j / 2, viewHeight);

		g.setColor(curColor);
	}

	/**
	 * Draw a latitude line with the latitude given by the
	 * 
	 * @param latitude
	 *            .
	 * @param latitude
	 *            The latitude to be drawn.
	 * @param viewWidth
	 *            the width of the view area of the map, in pixels.
	 * @param viewHeight
	 *            the height of the view area of the map, in pixels.
	 */
	private void drawLat(Graphics g, int latitude, int viewWidth, int viewHeight) {
		if ((latitude > latMax) || (latitude < latMin)) {
				System.err.println("Longitude " + latitude + " out of bound.");
				return;
		}
		
		// compute the normalised y-coordinate (between 0 and 1)
		// note that the lowest latitude is at the bottom of the map,
		// while the graphics coordinate origin is located at the top left
		// corner.
		double i1 = 1 - (double)(latitude - this.latMin)/ this.latExtent;

		int i2 = (int)(yOrigin + i1 * zoomScale * viewHeight);

		String str = String.valueOf(latitude);

		Rectangle2D localRectangle2D = g.getFontMetrics().getStringBounds(str, g);
		int j = (int) Math.round(localRectangle2D.getHeight() / 2.0D);

		Color curColor = g.getColor();

		g.setColor(Color.gray);

		g.drawLine(20, i2, viewWidth - 15, i2);

		g.setColor(Color.lightGray);

		g.drawString(str, 1, i2 + j / 2);
		g.drawString(str, viewWidth - 14, i2 + j / 2);

		g.setColor(curColor);
	}

	/**	 
	 * Draw a cross at the epi-center of selection.
	 *  
	 * @param g 
	 * 			The current graphics context.
	 * 
 	 * @param lon1, lat1 
	 * 			The longitude and latitude of the reference point (in degrees).
	 * 
	 */
	protected void drawEpicenter(Graphics g, float lon1, float lat1) {		
		// Convert the polar coordinates into image coordinates.
		float[] imageCoords = geoToImageCoords(lon1, lat1);
		
		Color curColor = g.getColor();
		g.setColor(Color.RED);

		g.drawLine((int)imageCoords[0]-10, (int)imageCoords[1], (int)imageCoords[0]+10, (int)imageCoords[1]);
		g.drawLine((int)imageCoords[0], (int)imageCoords[1]-10, (int)imageCoords[0], (int)imageCoords[1]+10);
		
		g.setColor(curColor);		
	}
	
	
	/** 
	 * Draw iso-distance curves corresponding to the given radial distance (in degrees)
	 * from a selected center with coordinates centerx, centery.
	 *  
	 * @param g 
	 * 			The current graphics context.
	 * 
	 * @param lon1, lat1 
	 * 			The longitude and latitude of the reference point (in degrees). 
	 * 
	 * @param dist 
	 * 			The angular distance between the reference point and the end points. 
	 *  		along the great circles going through the reference point and each end point.
	 *  		Dist is given in radians. 
	 *   
	 */
	protected void drawIsoDistCurve(Graphics g, float lon1, float lat1, float dist) {
		
		// number of points to be plotted on the curve.
		final int nPoints = 180;		
		float[] xPoints = new float[nPoints], yPoints = new float[nPoints];
		
		lon1 = lon1 * SphericalCoords.deg2Rad;
		lat1 = lat1 * SphericalCoords.deg2Rad;
		
		
		for (int k = 0; k < nPoints; k++) {
			// the azimuth angle of the destination point from the reference point.
			// (also called bearing or course in navigation terms).
			double azimuth = k * 360/nPoints;

			// convert the azimuth angle to radians. 
			azimuth = azimuth * SphericalCoords.deg2Rad;

			// The two following formulae are taken from 
			// http://www.movable-type.co.uk/scripts/latlong.html
			float lat2 = (float)Math.asin( Math.sin(lat1) * Math.cos(dist) +					
			                        Math.cos(lat1) * Math.sin(dist) * Math.cos(azimuth));
			
			float lon2 = lon1 + (float)Math.atan2(Math.sin(azimuth) * Math.sin(dist) * Math.cos(lat1), 
			                               Math.cos(dist) - Math.sin(lat1) * Math.sin(lat2));
			
			lon2 = (float)((lon2 + Math.PI)%(2*Math.PI) - Math.PI);  // normalised to -180...+180

			// if (lat2 == Double.NaN || lon2 == Double.NaN) 						
			
			// Convert the polar coordinates into image coordinates.
			float[] imageCoords = geoToImageCoords(lon2 * SphericalCoords.rad2Deg, lat2 * SphericalCoords.rad2Deg);
			
			// Store the new point as a vertex of the polygon to be drawn.
			xPoints[k] = imageCoords[0]; 
			yPoints[k] = imageCoords[1];
		} 

		// create a draw path from a series of points
        //GeneralPath drawPath = new GeneralPath();
      
        // set the initial coordinate of the General Path        
        //drawPath.moveTo(xPoints[0], yPoints[0]);
      
		Graphics2D g2d = (Graphics2D) g;
        Color currentColor = g2d.getColor();
        
        // Color of the curve
        g2d.setColor(Color.YELLOW);
        
		// create the path
		final int viewWidth = this.getWidth();
		
		final int x_dist_threshold = viewWidth/10; 
        for ( int k = 1; k < nPoints; k++ ) {        	
        	// don't draw the wrap-around lines (when the 
        	//  adjacent points on the curve
        	// lie on different side (left and right) of the image 
        	// coordinate window.
        	if (Math.abs(xPoints[k] - xPoints[k-1]) < x_dist_threshold)
        	//	drawPath.lineTo(xPoints[k], yPoints[k]);
        		g2d.drawLine((int)xPoints[k-1], (int)yPoints[k-1], (int)xPoints[k], (int)yPoints[k]);
        }

        // close the path.
        //drawPath.closePath();
    	if (Math.abs(xPoints[nPoints - 1] - xPoints[0]) < x_dist_threshold)
        	//	drawPath.lineTo(xPoints[k], yPoints[k]);
    		g2d.drawLine((int)xPoints[nPoints -1], (int)yPoints[nPoints-1], (int)xPoints[0], (int)yPoints[0]);
        
        //g2d.draw(drawPath); 
        
        
        g2d.setColor(currentColor);
	}
	
	
	/** 
	 * Draw the great circle paths from the epi-center (reference center of
	 * the distance range filter) to the filtered events on the map.
	 * 
	 * @param g 
	 * 			The current graphics context.
	 */ 
	private void drawGCPaths(Graphics g) {
		// number of points to be plotted on each great circle path.
		final int nPoints = 180;
				
		if (distRangeFilterCenterCoords != null && distRangeFilterCenterCoords.length == 2 
				&& eventList != null) {
			float refLon = distRangeFilterCenterCoords[0];
			float refLat = distRangeFilterCenterCoords[1];
			
			float[] refImageCoords = geoToImageCoords(refLon, refLat);
			
			// Convert the polar coordinates into Cartesian coordinates
			double[] refCoords = new double[]{Math.cos(refLon * SphericalCoords.deg2Rad) * Math.cos(refLat * SphericalCoords.deg2Rad), 
					 Math.sin(refLon * SphericalCoords.deg2Rad) * Math.cos(refLat * SphericalCoords.deg2Rad),
					 Math.sin(refLat * SphericalCoords.deg2Rad)};
			
			
			// If the epi-center has been defined and the event list is not null
			List<CatalogEvent> events = eventList.getEvents();
			for (CatalogEvent e : events) {
				float eLon = e.longitude;
				float eLat = e.latitude;
				
				double[] eCoords = 
					new double[]{Math.cos(eLon * SphericalCoords.deg2Rad) * Math.cos(eLat * SphericalCoords.deg2Rad), 
								 Math.sin(eLon * SphericalCoords.deg2Rad) * Math.cos(eLat * SphericalCoords.deg2Rad),
								 Math.sin(eLat * SphericalCoords.deg2Rad)};	
				
				
				// Assuming that A is the center of reference, B is the event location
				// O is the center of the unit sphere.
				// dot product between the vectors OA and OB.
				double dotProd = refCoords[0] * eCoords[0] + refCoords[1] * eCoords[1] + refCoords[2] * eCoords[2];
				
				
				// The angle subtended by each arc connecting two successive points on the great circle path.  
				double arcAngle = Math.acos(dotProd)/(nPoints - 1);
				
				// Image coordinates of the points along the great circle path between the ref location
				// and the event location.
				int[] xPoints = new int[nPoints], yPoints = new int[nPoints];
				xPoints[0] = Math.round(refImageCoords[0]); 
				yPoints[0] = Math.round(refImageCoords[1]);
				
				// Create the path
				for (int k = 1; k < nPoints; k++) {
					// Interpolate between the center and the event location
					double t = (Math.cos(k * arcAngle) - dotProd) / (1 - dotProd); 
					
					// Cartesian coordinates of the intermediate points 
					// on the segment between A and B.
					double[] interCoords = new double[3];
					double normSq = 0;
					for (int i = 0; i <=2; i++) {
						interCoords[i] = t * refCoords[i] + (1 - t) * eCoords[i];
						normSq = normSq + interCoords[i] * interCoords[i]; 
					}
					double norm = Math.sqrt(normSq);
					
					// Normalise (to project the point on the segment AB to a point on the sphere) 
					// and convert Cartesian to polar coordinates.
					for (int i = 0; i <= 2; i++) {
						interCoords[i] = interCoords[i]/norm; 
					}
					
					// Convert the polar coordinates into image coordinates.					
					float interLat = (float)Math.asin(interCoords[2]);					
					float interLon = (float)Math.atan2(interCoords[1], interCoords[0]);					
					float[] interImageCoords = geoToImageCoords(interLon * SphericalCoords.rad2Deg, interLat * SphericalCoords.rad2Deg);

					
					// Store the new point as a vertex of the polygon to be drawn.
					xPoints[k] = Math.round(interImageCoords[0]); 
					yPoints[k] = Math.round(interImageCoords[1]);
				}
				
//				System.out.println("\n\nxPoints:");
//				for (int k = 0; k < nPoints; k++) {
//					System.out.print(xPoints[k] + " ");
//				}
//				
//				System.out.println("\nyPoints:");
//				for (int k = 0; k < nPoints; k++) {
//					System.out.print(yPoints[k] + " ");
//				}
				
				
				Graphics2D g2d = (Graphics2D) g;
		        Color currentColor = g2d.getColor();		        
		        // Color of the curve
		        g2d.setColor(Color.YELLOW);
		        
				final int viewWidth = this.getWidth();				
				final int x_dist_threshold = (int)(viewWidth * 9/10);
				
		        for ( int k = 1; k < nPoints; k++ ) {        	
		        	// don't draw the wrap-around lines (when the 
		        	// adjacent points on the curve
		        	// lie on different sides (left and right) of the image 
		        	// coordinate window.
		        	if (Math.abs(xPoints[k] - xPoints[k-1]) <= x_dist_threshold)		        		
		        		g2d.drawLine((int)xPoints[k-1], (int)yPoints[k-1], (int)xPoints[k], (int)yPoints[k]);
		        }
		        
		        // Connect the last segment along the path.
		        float[] eventImageCoords = geoToImageCoords(eLon, eLat);
				g2d.drawLine((int)xPoints[nPoints - 1], (int)yPoints[nPoints - 1], 
						(int)Math.round(eventImageCoords[0]), (int)Math.round(eventImageCoords[1]));

				// revert to the previous colour
		    	g2d.setColor(currentColor);	
			}
		}	
	}
	
	/** Clear out all the markers of event filters on the MapView. */
	protected void clearFilterMarkers() {
		// Window coordinate marker 		
		
		// Distance range marker		
		repaint();
	}
	
	
	//// Observer functionalities ////
	/**
	 * Update the interface upon the change in the given Observable object.
	 * This object maybe an instance of the EventFilter class.
	 */
//	public void update(Observable o, Object arg) {
//		
//		// Update the label displaying the epi-center coordinates.
//		// and set the center of reference for the filter.
//		if (o instanceof DistanceRangeFilter) {
//			DistanceRangeFilter distFilter = (DistanceRangeFilter)o;
//			setInnerRadius(distFilter.getMinAngle());
//			setOuterRadius(distFilter.getMaxAngle());
//			setEpiCenterCoords(new float[]{distFilter.getRefLon(), distFilter.getRefLat()});
//		}
//	}
	
	
	//// Navigation functionalities ////
	/**
	 * Translate the whole map view by a vector with coordinates of (dx, dy) in pixels. 
	 * The map area will wrap around horizontally,
	 * i.e. the map area that is pushed out of the view area will appear at the
	 * other end of the view area.
	 * 
	 * @param dx
	 *            The number of pixels to translate horizontally, where dx > 0
	 *            means a translation to the right, and dx < 0 means one to the
	 *            left.
	 * 
	 * @param dy
	 *            The number of pixels to translate vertically, where dy > 0
	 *            means a translation to the right, and dy < 0 means one to the
	 *            left.
	 */
	public void translate(int dx, int dy) {
		final int viewWidth = this.getWidth();

		// System.out.println("Old origin (x, y): " + xOrigin + " " + yOrigin + "Scale: " + zoomScale );
		xOrigin += dx;
		yOrigin += dy;

		// System.out.println("Origin (x, y) after displacement: " + xOrigin + " " + yOrigin+ " Scale: " + zoomScale );
		
		// Wrap around horizontally
		final float xMax = xOrigin + zoomScale * viewWidth;
		if (xMax < 0) {
			xOrigin += zoomScale * viewWidth;  
		}
		
		if (xOrigin > 0) {
			xOrigin -= zoomScale * viewWidth;
		}

		// System.out.println("Origin (x, y) after scaling: " + xOrigin + " " + yOrigin+ " Scale: " + zoomScale );
		
		// redraw the map
		mapChangeNotifier.notifyObservers();
		repaint();
	}
	
	
	
	
	/**
	 * @return The current zoom scale of this MapView.
	 */
	protected float getZoomScale() {
		return zoomScale;
	}

	private void setZoomScale(float newZoomScale) {
		zoomScale = newZoomScale;
		mapChangeNotifier.notifyObservers();
	}
	
	/**
	 * Scale the current map about the center point with the given 
	 * 2D image coordinates centerx, centery and with the given scale.
	 * Note that centerx and centery are the 2D coordinates of a pixel
	 * in the image.
	 * 
	 * @param centerx
	 *            x pixel coordinates of the center of the scaling.
	 * @param centery
	 *            y pixel coordinates of the center of the scaling.
	 * @param zoomFactor
	 *            the scaling factor of the zoom.
	 */
	public void zoomAboutPoint(int centerx, int centery, float zoomFactor) {
		
		if (zoomScale * zoomFactor >= MIN_ZOOM_SCALE && zoomScale*zoomFactor<=MAX_ZOOM_SCALE) {			
			xOrigin = centerx + zoomFactor * (xOrigin - centerx);
			yOrigin = centery + zoomFactor * (yOrigin - centery);
			
			setZoomScale(zoomScale * zoomFactor);
			
			repaint();
		}
	}
	
	
	/**
	 * Zoom the current map so that the selected rectangular region 
	 * best fits the entire mapView.
	 * 
	 * @param minx, miny: the x, y image coordinates of the top-left 
	 * 		corner of the selected region.
	 * 
	 * @param maxx, maxy: the x, y image coordinates of the bottom-right 
	 * 		corner of the selected region.            
	 */
	public void zoomIntoRegion(int minx, int miny, int maxx, int maxy) {
		final int viewWidth = this.getWidth();
		final int viewHeight = this.getHeight();		
		float zoomFactor = Math.min((float)viewWidth/(float)(maxx-minx),  (float)viewHeight/(float)(maxy - miny));
		
		// System.out.println("View width " + viewWidth + " View height " + viewHeight + " zoom factor = " + zoomFactor);		
		// System.out.println("minx = " + minx + " maxx = " + maxx + " miny = " + miny +  " maxy = " + maxy);
		
		int x_move = viewWidth/2 - (minx + maxx)/2; 
		int y_move = viewHeight/2 - (miny + maxy)/2;
		
		// System.out.println("Displacement: " + x_move + ", " + y_move);
		
		// This transformation is a combination of a 
		// and a scaling about the center of the selected region, 
		// followed by a translation from the center 
		// of the selected region to the center of the mapView area
		zoomAboutPoint((minx + maxx)/2, (miny + maxy)/2, (float)Math.sqrt(zoomFactor));
		
		// Move the center to the middle of the screen.		
		translate((int)((float)x_move/zoomFactor), (int)((float)y_move/zoomFactor));
		
		// Change the zoom region.
		mapChanged = true;
		repaint();
	}
	
	
	
	/**
	 * Reset to the default view to
	 * center the map around the Asia-Pacific region (by default), 
	 * i.e. the 0 degree longitude is mapped to the left margin 
	 * of the world map, and the 360 degree longitude is mapped 
	 * to the right margin. Zoom level is set to the initial one (1).
	 */
	public void resetView(){
		xOrigin = -this.getWidth()/2;
		yOrigin = 0;		
		setZoomScale(1);
		repaint();
	} 
	
	
	////  Utilities  //// 
	/**
	 * Convert the given (x, y) coordinates 
	 * of a point in the the current image coordinate
	 * system into the original longitude and magnitude 
	 * of the world location corresponding to it.
	 * 
	 * @param x
	 * @param y the x and y image coordinates.
	 * 
	 * @return The longitude and latitude in the first and second 
	 * element of the returned array.  
	 */
	protected float[] imageToGeoCoords(float x, float y) {
		final int viewWidth = this.getWidth();
		final int viewHeight = this.getHeight();
		
		float x1 = x - zoomScale * viewWidth;
		
		if (x1 >= xOrigin && x1 <= xOrigin + zoomScale * viewWidth)
			x = x1;
		
		float x2 = (x - xOrigin)/(zoomScale * viewWidth); 
		float y2 = (y - yOrigin)/(zoomScale * viewHeight);
		
		float[] geoCoords = new float[2];
		// longitude
		geoCoords[0] = x2 * this.longExtent + this.longMin;
		
		// latitude
		geoCoords[1] = (1 - y2) * this.latExtent + this.latMin;
		
		return geoCoords;
	}
	
	/**
	 * Convert the given (lon, lat) coordinates
	 * of a point on the 3D sphere into to the image coordinate
	 * assuming the current image coordinate system
	 * of this MapView.
	 * 
	 * NOTE: lon and lat MUST be given in degrees.
	 * 
	 * @param lon
	 * @param lat the longitude and latitude of a given 3D point.
	 * 
	 * 
	 * @return The x and y image coordinates stored in the first 
	 * and second element of the returned 2-element array.  
	 */
	
	protected float[] geoToImageCoords(float lon, float lat) {
		final int viewWidth = this.getWidth();
		final int viewHeight = this.getHeight();
		
		float x1 = (float)(lon - longMin)/longExtent;			
		// invert the y direction because the coordinate origin of the Graphics context  
		float y1 = 1 - (float)(lat -latMin)/latExtent;

		float x2 = (xOrigin + x1 * zoomScale * viewWidth);
		float y2 = (yOrigin + y1 * zoomScale * viewHeight);
		
		float x3 = x2 + zoomScale * viewWidth;
		if (x3 <= viewWidth) { // wrap the location around
			x2 = x3;
		}
		
		float[] imageCoords = new float[]{x2, y2};
		return imageCoords;
	}
	
	
	
	//// Interaction controls ////
	/**
	 * A stack storing the history of mouse modes prior to the current
	 * mouse mode of this MapView.
	 * When a mouse mode is set it is added to the history, 
	 * when we revert to the previous mouse mode, the current mouse 
	 * mode will be removed from the history.
	 */
	private Stack<MouseMode> mouseModeStack = new Stack<MouseMode>();
	

	/**
	 * Change the mouse mode of this MapView to the previously set 
	 * mouse mode, and update the history of mouse mode at the same time.
	 *  
	 */
	public void revertMouseMode() {
		if (mouseModeStack.size() > 1) {
			MouseMode curMode = mouseModeStack.pop();		
			MouseMode prevMode = mouseModeStack.peek();
			setMouseMode(prevMode);			
		} 
//		else {
//			System.out.println("No mouse mode history to go back");
//		}		
//			System.out.println(mouseModeStack.toString());
		
	}
	
	
	/**
	 * Enter a new mouse mode of this MapView and add the current mouse
	 * mode to the history. 
	 */
	public void enterMouseMode(MouseMode mode) {				
		if (mode != null) {
			mouseModeStack.push(mode);
			setMouseMode(mode);
		}
//		else {
//			System.out.println("No mouse mode history to add");
//		}
//		System.out.println(mouseModeStack.toString());
	}
	
	
	/**
	 * Set the mouse mode for interacting with the map view,
	 * without adding the mode specified in the parameter 
	 * to the mouse mode history.
	 * 
	 * NOTE: This method is designed to have private access, 
	 * only meant to be
	 * called by revertMouseMode and enterMouseMode methods 
	 * and the initialization in the constructor only.
	 * 
	 * @param mouseMode the mode for mouse interaction.
	 * 	This can assume one of the following values.
	 * 	CLICK_ZOOM: Zoom by clicking left and right mouse buttons.
	 * 	REGION_ZOOM: Zoom to a selected (rectangular) region. 
	 * 	CLICK_SELECT: Click on the world map to select 
	 * 		a station (or an event)
	 */
	private void setMouseMode(MouseMode mode){
		if (mode == MouseMode.MOUSEWHEEL_ZOOM) {
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			
			this.addMouseListener(mouseDragTranslateListener);
			this.addMouseWheelListener(mouseWheelZoomListener);
			
			
			this.removeMouseListener(regionZoomListener);
			this.removeMouseMotionListener(regionZoomListener);
		
			this.addMouseListener(clickSelectionListener);
			//this.removeMouseListener(clickSelectionListener);
			
			this.removeMouseListener(regionSelectionListener);			
			this.removeMouseMotionListener(regionSelectionListener);
		
			this.mouseMode = mode;
		}			
		else if (mode == MouseMode.REGION_ZOOM) {
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			this.removeMouseListener(mouseDragTranslateListener);
			this.removeMouseWheelListener(mouseWheelZoomListener);
			
			
			this.addMouseListener(regionZoomListener);
			this.addMouseMotionListener(regionZoomListener);			
			
			this.removeMouseListener(clickSelectionListener);
			
			this.removeMouseListener(regionSelectionListener);			
			this.removeMouseMotionListener(regionSelectionListener);		
			
			this.mouseMode = mode;
		}
//		else if (mode == MouseMode.CLICK_SELECT) {
//			this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));			
//			
//			this.removeMouseListener(mouseDragTranslateListener);
//			this.removeMouseWheelListener(mouseWheelZoomListener);
//			
//			this.removeMouseListener(regionZoomListener);
//			this.removeMouseMotionListener(regionZoomListener);
//			
//			this.addMouseListener(clickSelectionListener);
//			
//			this.removeMouseListener(regionSelectionListener);			
//			this.removeMouseMotionListener(regionSelectionListener);
//		
//			this.mouseMode = mode;
//		}
		else if (mode == MouseMode.REGION_SELECT) {
			this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
						
			this.removeMouseListener(mouseDragTranslateListener);
			this.removeMouseWheelListener(mouseWheelZoomListener);
			
			this.removeMouseListener(regionZoomListener);			
			this.removeMouseMotionListener(regionZoomListener);
			
			this.removeMouseListener(clickSelectionListener);
			
			this.addMouseListener(regionSelectionListener);
			this.addMouseMotionListener(regionSelectionListener);			
			
			this.mouseMode = mode;			
		}
		else {
			System.err.println("Invalid mouse mode for MapView.");
		}	
	}
	
	
	/**
	 * 
	 * @return The current mode of interaction by mouse with the map view. 
	 */
	public MouseMode getMouseMode() {
		return mouseMode;
	}
}


enum MouseMode {
	MOUSEWHEEL_ZOOM, REGION_ZOOM, CLICK_SELECT, REGION_SELECT;
	public String toString() {
		if (this == MOUSEWHEEL_ZOOM)
			return "Zoom by Mouse-Wheel Scrolling";
		
		if (this == REGION_ZOOM)
			return "Zoom Into a Window";
		
		if (this == CLICK_SELECT)
			return "Select by Clicking";
				
		if (this == REGION_SELECT)
			return "Select a Window";
		
		return "";
	}
}



/**
 * A class responsible for listening mouse drag event.  
 * Upon a mouse drag, the map will be moved/translated a certain amount.
 */
class MouseDragTranslateListener extends MouseAdapter {
	// The MapView associated with this MapViewMouseListener
	private MapView view;
	
	// The image coordinates where the mouse is pressed and released.
	private Point mousePressedPoint, mouseReleasedPoint;

	/**
	 * Create a mouse listener for the given MapView
	 * 
	 * @param view
	 */
	public MouseDragTranslateListener(MapView view) {
		this.view = view;
	}

	
	public void mouseClicked(MouseEvent e) {
		
		// Zoom in the map if press left button
//		if (e.getButton() == MouseEvent.BUTTON1) {
//			view.zoomAboutPoint(e.getPoint().x, e.getPoint().y, ZOOM_FACTOR);
//		}
//		// Zoom out the map if press right button
//		else if (e.getButton() == MouseEvent.BUTTON3) {
//			view.zoomAboutPoint(e.getPoint().x, e.getPoint().y, (float)1/ZOOM_FACTOR);
//		}
	}

	public void mousePressed(MouseEvent e) {
		mousePressedPoint = e.getPoint();
	}

	public void mouseReleased(MouseEvent e) {
		mouseReleasedPoint = e.getPoint();
		int dx = mouseReleasedPoint.x - mousePressedPoint.x;
		int dy = mouseReleasedPoint.y - mousePressedPoint.y;
		view.translate(dx, dy);
	}
}

/** 
 * A class responsible for listening any MouseWheel event. 
 * Upon a mouse wheel scroll, the map will be zoomed in/out. 
 */
class MouseWheelZoomListener implements MouseWheelListener {

	// The MapView associated with this MapViewMouseListener
	private MapView view;
	
	
	// The zoom in/out factor between successive zoom levels.
	private float ZOOM_FACTOR = 1.2f;

	/**
	 * Create a MouseWheelListener for the given MapView
	 * 
	 * @param view
	 */
	public MouseWheelZoomListener(MapView view) {
		this.view = view;
	}


	public void mouseWheelMoved(MouseWheelEvent e) {
		int notches = e.getWheelRotation();
		
		// System.out.println("Rotated by " + notches + " notches");
		
		// Zoom in/out depending on the rotation direction
		// (i.e. the positivity of the number of notches).
		// Zoom in == rotating the wheel upward/away
		// Zoom out == rotating the wheel downward/toward the user
		view.zoomAboutPoint(e.getPoint().x, e.getPoint().y, (float)Math.pow(ZOOM_FACTOR, -notches));	
	}
}



class MapViewMouseMotionListener extends MouseMotionAdapter {
	private MapView view;
	
	// Threshold on the distance between stations and 
	// the geographical mapping of the mouse location.
	// The nearest station with distance below this threshold 
	// will have its name displayed.
	private static final float mouseDistThresh = 1; // 1 degree.  

	public MapViewMouseMotionListener(MapView view) {
		this.view = view;
	}


	public void mouseMoved(MouseEvent e) {
		// The current mouse location.
		Point p = e.getPoint();
		float[] geoCoords = view.imageToGeoCoords((float)p.x, (float)p.y);
		String info = "(Longitude, Latitude) = (" + geoCoords[0] + ", " + geoCoords[1]+ ")";
		
		// Find the nearest station to the mouse location and display its name.
		StationInfo nearestStation = null;
		float shortestDistSqr = Float.MAX_VALUE;
		for (StationInfo station : view.stations) {				
			float distSqr = (station.lon - geoCoords[0]) * (station.lon - geoCoords[0]) 
				+ (station.lat - geoCoords[1]) * (station.lat - geoCoords[1]);
			
			if (distSqr < shortestDistSqr) {
				shortestDistSqr = distSqr;
				nearestStation = station;
			}
		}
		
		// Only display the station name if the mouse is close enough to it.
		if (shortestDistSqr < mouseDistThresh) {			
			view.setToolTipText(nearestStation.station);
			info += ". Station: " + nearestStation.station;
		} else {
			view.setToolTipText("");
		}
		
		// Display the geographical coordinate corresponding to the mouse location.
		view.mainApp.setMapInteractionInfo(info);
	}
}


class ClickSelectionListener extends MouseAdapter {

	
	/** 
	 * The precision of selection by clicking
	 * as an upper limit on how far the clicked point is 
	 * from the selection target. 
	 * */
	private static final double CLICK_PRECISION_THRESH = 30;
	
	private MapView view;

	public ClickSelectionListener(MapView view) {
		this.view = view;
	}


	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			// Geo-coordinates of the mouse selection point.			
			float[] geoCoords = view.imageToGeoCoords(e.getPoint().x, e.getPoint().y);
			
			// Select the event closest to the mouse selection point.
			EventList mapEvents = view.getEvents();			
			if (mapEvents != null) {
				
				List<CatalogEvent> events = view.getEvents().getEvents();				
				if (events != null) {
				
					CatalogEvent nearestEvent = null;
					double shortestEventDistSqr = Double.MAX_VALUE;
					
					for (CatalogEvent event: events) {
						float[] eventImageCoords = view.geoToImageCoords(event.longitude, event.latitude);
						double distSqr = Math.pow(eventImageCoords[0] - e.getPoint().x, 2) 
							+ Math.pow(eventImageCoords[1] - e.getPoint().y, 2);
					
						// The event selection only happens if the user's click is within a vicinity 
						// (with radius CLICK_PRECISION_THRESH) of the selection target.
						if (distSqr < shortestEventDistSqr && distSqr <= CLICK_PRECISION_THRESH) {
							shortestEventDistSqr = distSqr;
							nearestEvent = event;
						}
					}
					
					if (nearestEvent != null) {
						// Toggle the selection of the nearest event.						
						nearestEvent.setSelected(!nearestEvent.isSelected);
						
						mapEvents.updateEventsDisplay();
					}
				}
			}
			
			
			// Also store the polar coordinates mapped to the mouse location.
			// view.setLastGeoLocation(geoCoords);			
			if (view.getEpiCenterType() == MapView.EpiCenterType.STATION) {
				
				// Select the nearest station upon a left click
				StationInfo nearestStation = null;
				double shortestStationDistSqr = Double.MAX_VALUE;
				
				for (StationInfo station : view.stations) {				
//					float distSqr = (station.lon - geoCoords[0]) * (station.lon - geoCoords[0]) 
//						+ (station.lat - geoCoords[1]) * (station.lat - geoCoords[1]);
//					
//					if (distSqr < shortestDistSqr) {
//						shortestDistSqr = distSqr;
//						nearestStation = station;
//					}
					
					float[] stationImageCoords = view.geoToImageCoords(station.lon, station.lat);
					double distSqr = Math.pow(stationImageCoords[0] - e.getPoint().x, 2) 
						+ Math.pow(stationImageCoords[1] - e.getPoint().y, 2);
				
					// The selection only happens if the user's click is within a vicinity 
					// (with radius CLICK_PRECISION_THRESH) of the selection target.
					if (distSqr < shortestStationDistSqr && distSqr <= CLICK_PRECISION_THRESH) {
						shortestStationDistSqr = distSqr;
						nearestStation = station;
					}
				}
				
				if (nearestStation != null) {
					view.setEpiCenterCoords(new float[]{nearestStation.lon, nearestStation.lat});
					
					// Highlight the selected station.
					view.mainApp.setSelectedStations(new StationInfo[]{nearestStation});
				}
				
			} else if (view.getEpiCenterType() == MapView.EpiCenterType.POINT) {				
				view.setEpiCenterCoords(geoCoords);				
				//view.mapChanged = true;
				view.repaint();			
				//view.mapChanged = false;
			} else {
				System.err.println("Invalid Reference Center Type");
			}	
			
		}
	}
	

	public void mouseReleased(MouseEvent e) {		
		// Set the map to be re-drawn next time.
		view.mapChanged = true;
	}
}




class RegionZoomListener implements MouseMotionListener, MouseListener {

	private MapView view;
	

	// The top-left corner of the rectangular region selected.
	// Region selection starts from the top left corner.
	private Point startPoint, endPoint;  

	public RegionZoomListener(MapView view) {
		this.view = view;
	}

	
	public void mousePressed(MouseEvent e) {
		startPoint = e.getPoint();
		// System.out.println("Start point " + startPoint.x + " " + startPoint.y);
	}

	public void mouseReleased(MouseEvent e) {		
		// The point where the mouse is release
		// is the bottom right corner of the rectangular region.
		// Zoom the map into the area selected.
		// System.out.println("Start point " + startPoint.x + " " + startPoint.y);
		
		endPoint = e.getPoint();
		// System.out.println("End point " + endPoint.x + " " + endPoint.y);
		
		if (startPoint.x < endPoint.x && startPoint.y < endPoint.y) {			
			// draw the zoom region
			view.zoomIntoRegion(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
		}
		view.mapChanged = true;
	}

	
	
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
		
	}
	
	
	public void mouseDragged(MouseEvent e) {
		Point p = e.getPoint();
		
		if (p.x > startPoint.x && p.y > startPoint.y) {			
			// System.out.println("Start point " + startPoint.x + " " + startPoint.y);
			// System.out.println("end : x = " + p.x + " y = " + p.y);
				
			// draw a rectangle.
			int[] topLeftImageCoords = new int[]{startPoint.x, startPoint.y};
			int[] bottomRightImageCoords = new int[]{p.x, p.y};			
			view.drawCoordWindow(topLeftImageCoords, bottomRightImageCoords);
			view.mapChanged = false;
			view.repaint();		
		}
	}

	
    /**
     * Invoked when the mouse button has been moved on a component
     * (with no buttons no down).
     */
    public void mouseMoved(MouseEvent e) {}
}



/** 
 * A MouseListener which listens to the drag action of users
 * in order to sense the selected coordinate window.
 *   
 */
class RegionSelectionListener implements MouseMotionListener, MouseListener {

	private MapView view;
	

	// The top-left corner of the rectangular region selected.
	// Region selection starts from the top left corner.
	protected Point startPoint, endPoint;  

	public RegionSelectionListener(MapView view) {
		this.view = view;
	}
	
	public void mousePressed(MouseEvent e) {
		startPoint = e.getPoint();
		// System.out.println("Start point " + startPoint.x + " " + startPoint.y);	
	}

	public void mouseReleased(MouseEvent e) {		
		// The point where the mouse is release
		// is the bottom right corner of the rectangular region.
		// Zoom the map into the area selected.
		// System.out.println("Start point " + startPoint.x + " " + startPoint.y);
		
		endPoint = e.getPoint();
		// System.out.println("End point " + endPoint.x + " " + endPoint.y);

		// Set the mapView to be re-drawn the next time.
		view.mapChanged = true;
	}

	
	
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
		
	}
	
	
	public void mouseDragged(MouseEvent e) {
		Point p = e.getPoint();
		
		if (p.x > startPoint.x && p.y > startPoint.y) {
			// System.out.println("Start point " + startPoint.x + " " + startPoint.y);
			// System.out.println("end : x = " + p.x + " y = " + p.y);

			// draw a rectangle.
			int[] topLeftImageCoords = new int[]{startPoint.x, startPoint.y};
			int[] bottomRightImageCoords = new int[]{p.x, p.y};			
			view.drawCoordWindow(topLeftImageCoords, bottomRightImageCoords);
			view.mapChanged = false;
			view.repaint();			
		}
	}

	
    /**
     * Invoked when the mouse button has been moved on a component
     * (with no buttons no down).
     */
    public void mouseMoved(MouseEvent e) {}
}



