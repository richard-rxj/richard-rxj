/**
 * 
 */
package dr.alg.anu.au;

import generate.dr.alg.anu.au.NetworkGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import network.dr.alg.anu.au.BiNetwork;
import network.dr.alg.anu.au.GateWay;

/**
 * @author user
 *
 */
public class MobileSinkTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Logger logger=Logger.getLogger("MobileSink");
		//FileHandler fh=new FileHandler("m.log");
		//fh.setFormatter(new SimpleFormatter());
		//fh.setLevel(Level.WARNING);
		//logger.addHandler(fh);
		
		
		
		
		String tFileName="test/";
		File tf=new File(tFileName);
		if(!tf.exists())
		{
			tf.mkdirs();
		}
		
		String nFile=tFileName+"n-100.txt";
		String gFile=tFileName+"g-100.txt";
		
		//BiNetwork bNet=NetworkGenerator.generateNetwork(100, 50);
		//bNet.saveToFile(nFile, gFile);
		BiNetwork bNet=NetworkGenerator.createFromFile(nFile, gFile);
		//bNet.saveToFile(tFileName+"n-100-2.txt", tFileName+"g-100-2.txt");
		
		//ArrayList<GateWay> solution=TourDesign.linearTourDesign(nFile, gFile);
		ArrayList<GateWay> solution=TourDesign.fairTourDesign(nFile, gFile);
		for(int i=0;i<solution.size();i++)
		{
			System.out.println(solution.get(i));
		}
		
		
	}

}
