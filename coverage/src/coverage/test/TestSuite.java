package coverage.test;

import java.io.IOException;

public class TestSuite {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//TestCentralized.test();
		TestFrameworkByTuningWeight.test();
		TestFrameworkByThreshold.test();
		TestFrameworkByBudgetFactor.test();
	}

}
