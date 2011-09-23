package u4964526.anu.util;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TestAll {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try
		{
			Logger logger=Logger.getLogger("MaxFlow");
			logger.setLevel(Level.WARNING);
			
			//TestRealData.realTest();
			
			logger.warning("TestMaxFlow.runningTask(1,1)");
			TestMaxFlow.runningTask(1,1);
			
			logger.warning("TestMaxFlow.runningTask(1,3)");
			TestMaxFlow.runningTask(1,3);
			
			logger.warning("TestMaxFlow.runningTask(1,6)");
			TestMaxFlow.runningTask(1,6);
			
			logger.warning("TestMaxFlow.runningTask(1,12)");
			TestMaxFlow.runningTask(1,12);
			
			logger.warning("TestMaxFlow.runningTask(3,1)");
			TestMaxFlow.runningTask(3,1);
						
			logger.warning("TestMaxFlow.runningTask(3,3)");
			TestMaxFlow.runningTask(3,3);
			
			logger.warning("TestMaxFlow.runningTask(3,6)");
			TestMaxFlow.runningTask(3,6);
			
			logger.warning("TestMaxFlow.runningTask(3,12)");
			TestMaxFlow.runningTask(3,12);
			
			logger.warning("TestMaxFlow.runningTask(0,3)");
			TestMaxFlow.runningTask(0,3);
			
			logger.warning("TestMaxFlow.runningTask(0,6)");
			TestMaxFlow.runningTask(0,6);
			
			logger.warning("TestMaxFlow.runningTask(0,12)");
			TestMaxFlow.runningTask(0,12);
			
			TestMaxFlow.includeMatlabTask(1, 1);
			TestMaxFlow.includeMatlabTask(1, 3);
			TestMaxFlow.includeMatlabTask(1, 6);
			TestMaxFlow.includeMatlabTask(1, 12);
			TestMaxFlow.includeMatlabTask(3, 3);
            
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
