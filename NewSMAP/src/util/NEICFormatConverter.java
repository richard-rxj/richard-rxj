package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * NEICFormatConverter is responsible for the conversion 
 * from the NEIC format to the catalog format for Seismomap. 
 *   
 * Copyright (C) 2009, Research School of Earth Sciences,
 * The Australian National University
 * 
 * @author Cong Phuoc Huynh
 * 
 * Date: May 6, 2009
 */
public class NEICFormatConverter {
	
	/**
	 * Convert from NEIC format to the catalog format for Seismomap,
	 * which contains two magnitude fields (the first two)
	 * 
	 * @param filePath the path of the input file, including the file name.
	 * @param outPath the output path name.
	 *  
	 */
	public static void parseCatalogFile(String filePath, String outPath) {		
		
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
				StringTokenizer tok = new StringTokenizer(line, " ");
				
				lineNumber++;
				
				int count = 0;
				tokens.clear();
				
				while (tok.hasMoreTokens()) {
					count++;
					
					if (count >= 9) // skip to the next line, read enough data
						break;
					
					String token = tok.nextToken();
					
					if (count == 4) { // separate the time string and convert to number of seconds. 
						String hour = token.substring(0, 2);
						String min;
						String sec;

						// pad zeros to align the minute field
						if (token.length() <= 2) { 
							min = "00";
						} else {
							min = token.substring(2, 4);
						}
						
						// pad zeros to align the second field
						if (token.length() <= 4) {
							sec = "00.00";
						} else if (token.length() <= 6) {
							sec = token.substring(4) + ".00";
						}else { 
							sec = token.substring(4);
						}
						
						tokens.add(hour);
						tokens.add(min);
						tokens.add(sec);
						
//						float timeInSeconds;						
//						// if the second field is missing, it the number of seconds is assumed to be 0.
//						if (sec.equals("")) {
//							// timeInSeconds = 3600 * Integer.parseInt(hour) + 60 * Integer.parseInt(min);
//						} else {
//							timeInSeconds = 3600 * Integer.parseInt(hour) 
//							+ 60 * Integer.parseInt(min) + Float.parseFloat(sec);
//						}
//						
//						// only take maximum 2 digits
//						tokens.add(String.valueOf(timeInSeconds));
						
					} else {
						tokens.add(token);
					}
				}
				
				String outputLine = "";
				for (String string : tokens) {
					outputLine += string + " ";					
				}
				outputLine += "0.0\n";
				
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
