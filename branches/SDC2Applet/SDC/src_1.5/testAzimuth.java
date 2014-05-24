import java.io.*;
import java.util.*;

public class testAzimuth {

	public static void main(String[] args) {
		if (args.length != 4) {
			System.out
					.println("Please give four parameters: origin lat, origin long, station lat, station lon");
		}

		Azimuth a = new Azimuth(Float.parseFloat(args[0]), Float
				.parseFloat(args[1]), Float.parseFloat(args[2]), Float
				.parseFloat(args[3]));

		System.out.println("Origin " + args[0] + " latitude");
		System.out.println("Origin " + args[1] + " longitude");
		System.out.println("Station " + args[2] + " latitude");
		System.out.println("Station " + args[3] + " longitude");

		System.out.println("delta " + a.rangeDg + " degrees");
		System.out.println("delta " + a.rangeKm + " kms");
		System.out.println("azimuth " + a.azim + " degrees");
		System.out.println("back azimuth " + a.bazim + " degrees");
	}
}
