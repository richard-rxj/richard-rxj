package coverage.test;

import java.io.File;

import coverage.util.ExperimentSetting;

public class TestCentralized {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] targetSizes={25,50};
		int[] networkSizes={100, 200, 300, 400, 500};
		String[] algs={"Centralized", "ICDCS"};
		String[] funs={"SQR","LOG"};
		int cishu=ExperimentSetting.cishu;
		
		String outputBase="data"+File.separator+"result";
		File tf=new File(outputBase);
		if(!tf.exists()) {
			tf.mkdirs();
		}
		
		for(int )
		
	}

}
