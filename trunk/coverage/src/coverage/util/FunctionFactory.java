/**
 * 
 */
package coverage.util;

/**
 * @author user
 *
 */
public class FunctionFactory {

	public static Func getFunc(String name) {
		if("SQR".equals(name)) {
			return new SQRFunc();
		} else if ("LOG".equals(name)) {
			return new LOGFunc();
		} else {
			return new LinearFunc();
		}
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}


