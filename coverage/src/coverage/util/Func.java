/**
 * 
 */
package coverage.util;

/**
 * @author user
 *
 */
class SQRFunc extends Func {
	public double getResult(int input) {
		return Math.sqrt(input);
	}
}

class LOGFunc extends Func{
	public double getResult(int input) {
		return Math.log10(input+1);
	}
}

class LinearFunc extends Func{
	public double getResult(int input) {
		return input;
	}
}

public abstract class Func {
	public double getResult(int input) {
		return 0;
	}
}


