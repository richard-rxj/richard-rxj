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
		} else {
			return new LOGFunc();
		}
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}


