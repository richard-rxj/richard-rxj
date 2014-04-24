package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * CMTCatalogConverter is responsible for converting 
 * the CMT catalog (available at 
 * http://www.ldeo.columbia.edu/~gcmt/projects/CMT/catalog/jan76_dec05.ndk)
 * from the NDK format to the format used by the RSES.
 * 
 * A description of the NDK format is available at
 * http://www.ldeo.columbia.edu/~gcmt/projects/CMT/catalog/allorder.ndk_explained
 * 
 * The current format used by the RSES consists of a single line
 * for each earthquake event, each of which include the following fields
 * of the event information:
 * 
 * year month date hour min sec latitude longitude depth mb ms
 * 
 * where depth is in kilometers and mb, ms are the moment 
 * and surface wave magnitudes.
 * 
 * Copyright (C) 2009, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author Cong Phuoc Huynh
 * 
 * Created on: Jun 3, 2009
 */
public class CMTCatalogConverter {
	
	

	/**
	 * Convert the CMT catalog from the NDK format 
	 * to the catalog format for Seismomap.
	 * 
	 * @param filePath the path of the input file, including the file name.
	 * @param outPath the output path name.
	 *  
	 */
	public static void parseCatalogFile(String filePath, String outPath)	{
		int lineNumber = 0;
		try {
			File f = new File(filePath);
			
			FileReader fileReader = new FileReader(f);
			
			BufferedReader buff = new BufferedReader(fileReader);
			
			String line = null;
			
			File outFile = new File(outPath);
			FileWriter fileWriter = new FileWriter(outFile);
			
			ArrayList<String> tokens = new ArrayList<String>(); 


			while ((line = buff.readLine()) != null) {
				lineNumber++;

				// Only read the first of every five lines
				// since each event is stored in five consecutive lines
				// according to the NDK format, and only the first 
				// of them contains enough information to use.
				if (lineNumber % 5 != 1)
					continue;
			
				
				int count = 0;
				tokens.clear();
				
				// Skip the first five characters, which are reserved for 
				// the hypocenter reference catalog.
				StringTokenizer tok = new StringTokenizer(line.substring(5), " ");
				
				while (tok.hasMoreTokens()) {
					count++;
					
					String token = tok.nextToken();
					
					if (count == 1) { // decompose the date												
						String[] YMD = token.split("/");
						tokens.add(YMD[0]);
						tokens.add(YMD[1]);
						tokens.add(YMD[2]);						
					}
						
					if (count == 2) { // decompose the time
						String[] HMS = token.split(":");
						tokens.add(HMS[0]);
						tokens.add(HMS[1]);
						tokens.add(HMS[2]);						
					}
					
					// save the longitude, latitude, depth, mw, ms
					if (count >= 3 && count <= 7) {	
						tokens.add(token);
					}
				}
				
				String outputLine = "";
				for (String string : tokens) {
					outputLine += string + " ";					
				}
				outputLine += "\n";
				
				fileWriter.write(outputLine);
			}
			
			buff.close();
			
			fileWriter.flush();
			fileWriter.close();
		}
		catch (Exception e) {
			System.out.println("Unexpected format at line " + lineNumber + "\n");
			e.printStackTrace();
		} 
	}
	
	public static void main(String[] args) {
		try {
			parseCatalogFile(args[0], args[1]);
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
	}

}
