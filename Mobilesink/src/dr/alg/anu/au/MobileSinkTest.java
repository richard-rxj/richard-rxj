/**
 * 
 */
package dr.alg.anu.au;

import generate.dr.alg.anu.au.NetworkGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import network.dr.alg.anu.au.BiNetwork;

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
		FileHandler fh=new FileHandler("m.log");
		fh.setFormatter(new SimpleFormatter());
		fh.setLevel(Level.WARNING);
		logger.addHandler(fh);
		
		
		
		
		String tFileName="test/";
		File tf=new File(tFileName);
		if(!tf.exists())
		{
			tf.mkdirs();
		}
		BiNetwork bNet=NetworkGenerator.generateNetwork(1, 1);
		bNet.saveToFile(tFileName+"n-1.txt", tFileName+"g-1.txt");
		bNet=NetworkGenerator.createFromFile(tFileName+"n-1.txt", tFileName+"g-1.txt");
		bNet.saveToFile(tFileName+"n-2.txt", tFileName+"g-2.txt");
		
	}

}
