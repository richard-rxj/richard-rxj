package util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * FileTypeFilter extends FileFilter for filtering files 
 * with a certain extension. 
 * 
 * Copyright (C) 2010, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author huynh
 * 
 * Created on: 17/03/2010
 */


public class FileTypeFilter extends FileFilter implements Comparable<FileTypeFilter>{
	
	/** A short string showing the extension of the filename, e.g. png, bmp */
	private String extension;
	
	/** 
	 * Create a File filter with the given extension.
	 * @param ext An extension with no '.' character, e.g.  png, bmp 
	 */
	public FileTypeFilter(String ext) {
		extension = ext;
	}
	
	@Override
	public String getDescription() {
		return extension + " Files";
	}

	@Override
	public boolean accept(File f) {
		if (f.isDirectory())
			return true;

		String fName = f.getName();
		int dotLoc = fName.lastIndexOf('.');
		String ext = fName.substring(dotLoc + 1);

		if (extension.equalsIgnoreCase(ext))
			return true;
		return false;
	}

	/**
	 * @return The file type (jpeg, jpg, png ...) in lower case.
	 */
	public String getExtension() {
		return extension.toLowerCase();
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(FileTypeFilter o) {
		return extension.toLowerCase().compareTo(o.getExtension().toLowerCase());
	}
}