package seqi;

import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * MapViewInfoPanel display the information of 
 * user's interactions with the MapView.
 * 
 * Copyright (C) 2009, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: Jun 25, 2009
 */
public class MapInteractionInfoPanel extends JPanel {
	private JLabel infoLabel; 
	
	public MapInteractionInfoPanel(){
		infoLabel = new JLabel("(X, Y)");
		add(infoLabel, FlowLayout.LEFT);		
	}
	
	
	/**
	 * Set and display the name of the catalog file 
	 * being loaded.
	 * @param catFileName
	 */
	public void setInfoText(String info) {
		infoLabel.setText(info);
		repaint();
	}

}
