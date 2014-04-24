/*
  The TauP Toolkit: Flexible Seismic Travel-Time and Raypath Utilities.
  Copyright (C) 1998-2000 University of South Carolina

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

  The current version can be found at 
  <A HREF="www.seis.sc.edu">http://www.seis.sc.edu</A>

  Bug reports and comments should be directed to 
  H. Philip Crotwell, crotwell@seis.sc.edu or
  Tom Owens, owens@seis.sc.edu

 */

package util;

/**
 * Utility class for spherical coordinate (lat-lon) transformations. Given lat,
 * lon, lat, lon you can find the distance or azimuth and given lat, lon,
 * distance, azimuth you can find the lat lon of the resultant point. Just uses
 * spherical relations, no ellpticity correction is applied.
 * 
 * See Appendix A of "Seismology and Plate Tectonics" by David Gubbins Cambridge
 * University Press, 1990
 * 
 * and Chapter 3 of "Plate Tectonics: How it Works" by Allan Cox and Robert
 * Brian Hart Blackwell Scientific Publications, 1986
 * 
 * @author H. Philip Crotwell
 * @version 1.1.3 Wed Jul 18 15:00:35 GMT 2001
 * 
 * 
 * 14/10/2010.
 * Modified by Cong Phuoc Huynh 
 * Seismology Research Group, Research School of Earth Science,
 * The Australian National University.
 *   
 * Copyright (C) 2010, Research School of Earth Sciences,
 * The Australian National University
 * 
 * 
 */
public class SphericalCoords_v1 {

	// Constants for conversion between radians and degrees.
	public static final float deg2Rad = (float)(Math.PI / 180.0);
	public static final float rad2Deg = (float)(1/deg2Rad);

	/**
	 * Use Law of Cosines to compute the great-circle distance 
	 * between the two points given their longitudes and latitudes,   
	 * in numeric degrees.
	 * 
	 * @param lonA, latA 
	 * 		The longitude and latitude of the first point, in degrees.
	 * 
	 * @param lonB, latB 
	 * 		The longitude and latitude of the second point, in degrees.
	 * 
	 * @return The great circle distance in degrees, which is the 
	 * 			angle subtended by the arc between the given points
	 * 			on the great circle, with respect to the center of the sphere. 
	 */	
	public static double greatCirleDist(double lonA, double latA, double lonB, double latB) {
		double d = Math.acos(Math.sin(latA * deg2Rad) *Math.sin(latB * deg2Rad) +				
				Math.cos(latA * deg2Rad) * Math.cos(latB * deg2Rad) * Math.cos((lonB-lonA)* deg2Rad));
		return d * rad2Deg;
	}
	

	/** Calculates azimuth between two lat lon pairs. */
	public static double azimuth(double lonA, double latA, double lonB, double latB) {
//		double cosAzimuth = (Math.cos(latA * deg2Rad) * Math.sin(latB * deg2Rad) - Math
//				.sin(latA * deg2Rad)
//				* Math.cos(latB * deg2Rad) * Math.cos((lonB - lonA) * deg2Rad))
//				/ Math.sin(greatCirleDist(lonA, latA, lonB, latB) * deg2Rad);
//
//		double sinAzimuth = Math.cos(latB * deg2Rad)
//				* Math.sin((lonB - lonA) * deg2Rad)
//				/ Math.sin(greatCirleDist(lonA, latA, lonB, latB) * deg2Rad);
		
		// difference in longitude and latitude between the initial and terminal points.
		double dLon = lonB - lonA; 
			
		double cosAzimuth = (Math.cos(latA * deg2Rad) * Math.sin(latB * deg2Rad) - 
				Math.sin(latA * deg2Rad) * Math.cos(latB * deg2Rad) * Math.cos(dLon * deg2Rad));

		double sinAzimuth =  Math.sin(dLon * deg2Rad) * Math.cos(latB * deg2Rad);
		
		return rad2Deg * Math.atan2(sinAzimuth, cosAzimuth);
	}

	
	/**
	 * Find the rotation pole required to rotate the first lat lon pair to the
	 * second. Just does a cross product.
	 * 
	 * @returns a 3 element double array with the X, Y and Z components of the
	 *          pole.
	 */
	public static double[] rotationPole(double lonA, double latA, double lonB, 
			double latB) {
		double[] pointA = new double[3];
		double[] pointB = new double[3];
		double[] pole = new double[3];

		double dToR = Math.PI / 180.0;

		pointA[0] = Math.cos(latA * dToR) * Math.cos(lonA * dToR);
		pointA[1] = Math.cos(latA * dToR) * Math.sin(lonA * dToR);
		pointA[2] = Math.sin(latA * dToR);

		pointB[0] = Math.cos(latB * dToR) * Math.cos(lonB * dToR);
		pointB[1] = Math.cos(latB * dToR) * Math.sin(lonB * dToR);
		pointB[2] = Math.sin(latB * dToR);

		pole[0] = pointA[1] * pointB[2] - pointA[2] * pointB[1];
		pole[1] = pointA[2] * pointB[0] - pointA[0] * pointB[2];
		pole[2] = pointA[0] * pointB[1] - pointA[1] * pointB[0];

		return pole;
	}

	/**
	 * rotates a point about a pole by an angle.
	 * 
	 * @param pole
	 *            is a 3 element double array with X, Y and Z components of the
	 *            pole.
	 * @returns [lon, lat] in array.
	 */
	public static double[] rotate(double lonA, double latA, double[] pole,
			double angleDeg) {
		double[][] R = new double[3][3]; /* rotation matrix. */
		double[] point = new double[3];
		double[] newPoint = new double[3];

		double rToDeg = 180.0 / Math.PI;
		double angle = angleDeg / rToDeg;

		R[0][0] = pole[0] * pole[0] * (1 - Math.cos(angle)) + Math.cos(angle);
		R[0][1] = pole[0] * pole[1] * (1 - Math.cos(angle)) - pole[2]
				* Math.sin(angle);
		R[0][2] = pole[0] * pole[2] * (1 - Math.cos(angle)) + pole[1]
				* Math.sin(angle);

		R[1][0] = pole[1] * pole[0] * (1 - Math.cos(angle)) + pole[2]
				* Math.sin(angle);
		R[1][1] = pole[1] * pole[1] * (1 - Math.cos(angle)) + Math.cos(angle);
		R[1][2] = pole[1] * pole[2] * (1 - Math.cos(angle)) - pole[0]
				* Math.sin(angle);

		R[2][0] = pole[2] * pole[0] * (1 - Math.cos(angle)) - pole[1]
				* Math.sin(angle);
		R[2][1] = pole[2] * pole[1] * (1 - Math.cos(angle)) + pole[0]
				* Math.sin(angle);
		R[2][2] = pole[2] * pole[2] * (1 - Math.cos(angle)) + Math.cos(angle);

		point[0] = Math.cos(latA / rToDeg) * Math.cos(lonA / rToDeg);
		point[1] = Math.cos(latA / rToDeg) * Math.sin(lonA / rToDeg);
		point[2] = Math.sin(latA / rToDeg);

		newPoint[0] = R[0][0] * point[0] + R[0][1] * point[1] + R[0][2]
				* point[2];

		newPoint[1] = R[1][0] * point[0] + R[1][1] * point[1] + R[1][2]
				* point[2];

		newPoint[2] = R[2][0] * point[0] + R[2][1] * point[1] + R[2][2]
				* point[2];

		double newLat = Math.asin(newPoint[2]) * 180.0 / Math.PI;
		double newLon = Math.atan2(newPoint[1], newPoint[0]) * 180.0 / Math.PI;

		newPoint = new double[2];
		newPoint[0] = newLon;
		newPoint[1] = newLat;

		return newPoint;
	}

	/**
	 * Calculates the longitude and latitude of a point given its distance along 
	 * a given azimuth from a starting lon lat.
	 * 
	 * @param lonA
	 * @param latA the longitude and latitude (in degrees) of a given point on the unit sphere.
	 *  
	 * @return The longitude and latitude of the destination point stored in the first 
	 * 			and second element of the returned 2-element array.  
	 */
	public static double[] lonLatFor(double lonA, double latA, double distance,
			double azimuth) {
		double latB = rad2Deg
				* Math.asin(Math.sin(latA * deg2Rad) * Math.cos(distance * deg2Rad) +
				Math.cos(latA * deg2Rad  * Math.sin(distance * deg2Rad) * Math.cos(azimuth * deg2Rad)));
		
		double sinLonB = Math.sin(azimuth * deg2Rad) * Math.sin(distance * deg2Rad)		
				* Math.cos(latA * deg2Rad);
		double cosLonB = Math.cos(distance * deg2Rad) - Math.sin(latA * deg2Rad)
				* Math.sin(latB * deg2Rad);
		
		// make sure answer (lon) is between -180 and 180
		double lonB = lonA + rad2Deg * Math.atan2(sinLonB, cosLonB);
		if (lonB <= -180.0)
			lonB += 360.0;
		if (lonB > 180.0)
			lonB -= 360.0;
		
		double[] lonLatCoords = new double[]{lonB, latB};
		return lonLatCoords;
	}
	
	public static double latFor(double lonA, double latA, double distance,
			double azimuth) {
		double latB = rad2Deg
				* Math.asin(Math.sin(latA * deg2Rad) * Math.cos(distance * deg2Rad) +
				Math.cos(latA * deg2Rad  * Math.sin(distance * deg2Rad) * Math.cos(azimuth * deg2Rad)));
		
		return latB;
	}
	
	/**
	 * Calculates the longitude of a point a given distance along a given
	 * azimuth from a starting lon lat.
	 */
	public static double lonFor(double lonA, double latA, double distance,
			double azimuth) {
		double tempLat = latFor(latA, lonA, distance, azimuth);

//		double sinLon = Math.sin(azimuth * deg2Rad) * Math.sin(distance * deg2Rad)		
//				/ Math.cos(tempLat * deg2Rad);
//		double cosLon = (Math.cos(distance * deg2Rad) - Math.sin(latA * deg2Rad)
//				* Math.sin(tempLat * deg2Rad))
//				/ (Math.cos(latA * deg2Rad) * Math.cos(tempLat * deg2Rad));
		
		double sinLon = Math.sin(azimuth * deg2Rad) * Math.sin(distance * deg2Rad)		
				* Math.cos(latA * deg2Rad);
		double cosLon = Math.cos(distance * deg2Rad) - Math.sin(latA * deg2Rad)
				* Math.sin(tempLat * deg2Rad);

		// make sure answer (lon) is between -180 and 180
		double lon = lonA + rad2Deg * Math.atan2(sinLon, cosLon);
		if (lon <= -180.0)
			lon += 360.0;
		if (lon > 180.0)
			lon -= 360.0;
		return lon;
	}

	public static void main(String args[]) {

		System.out.println(greatCirleDist(0, 0, 45, 0) + "  " + azimuth(0, 0, 0, 45)
				+ "   " + azimuth(0, 45, 0, 0));

//		System.out.println(latFor(0, 0, 45, 90) + "   " + lonFor(0, 0, 45, 90));

		System.out.println("(35,42,36,43)  " + greatCirleDist(42, 35, 43, 35) + "  "
				+ azimuth(35, 42, 36, 43) + "   " + azimuth(36, 43, 35, 42));

//		System.out.println(latFor(35, 42, greatCirleDist(35, 42, 36, 43), azimuth(35,
//				42, 36, 43))
//				+ "   "
//				+ lonFor(35, 42, greatCirleDist(35, 42, 36, 43), azimuth(35, 42, 36,
//						43)));
	}
}
