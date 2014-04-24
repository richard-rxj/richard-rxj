package util;

import java.awt.Component;
import java.awt.Container;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.imageio.ImageIO;

/**
 * GUIUtils provide a set of utility routines for handling GUIs.
 * 
 * Copyright (C) 2010, Research School of Earth Sciences, The Australian
 * National University
 * 
 * @author huynh
 * 
 *         Created on: Feb 11, 2010
 */
public class GUIUtils {

	/**
	 * Set the enable/disable flag to all the components of the specified
	 * container.
	 * 
	 * @param flag
	 *            true if the components are enabled, false otherwise.
	 */
	public static void setEnabledComponents(Container container, boolean flag) {
		Component[] comps = container.getComponents();

		// Search for all GUI components and set the flag.
		if (comps != null) {
			for (Component component : comps) {
				component.setEnabled(flag);

				if (component instanceof java.awt.Container)
					setEnabledComponents((Container) component, flag);
			}
		}
	}

	/**
	 * @return A list of filters for all the image formats
	 * 	recognizable by the host JVM. 
	 */
	public static SortedSet<FileTypeFilter> getImageFileFilters() {
		
		// Get the formats sorted alphabetically and in lower case 
		final String[] formats = ImageIO.getWriterFormatNames();
		final TreeSet<String> formatSet = new TreeSet<String>();
		for (String s : formats) {
			String format = s.toLowerCase();
			formatSet.add(format);
		}

		SortedSet<FileTypeFilter> fileFilters = new TreeSet<FileTypeFilter>();
		for (final String format : formatSet) {
			FileTypeFilter fileFilter = new FileTypeFilter(format);
			fileFilters.add(fileFilter);
		}

		return fileFilters;
	}
}
