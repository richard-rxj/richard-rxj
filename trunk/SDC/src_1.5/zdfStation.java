import java.io.*;
import java.util.*;


public class zdfStation{

	/** Station Header information */
	public int irs;		// length of station data
	public char[] name 
		= new char[4];		// Name of station
	public int sday;		// Station begin: day
	public int shr;		// Station begin: hour
	public int smin;		// Station begin: min
	public float ssec;		// Station begin: sec
	public float tcrr;		// time correction
	public float delta;		// range degrees or km
	public float azim;		// Azimuth to source
	public float sampin;		// Sample interval
	public int ncomp;		// number of components
	public char[] cend 
		= new char[4];		// end of station marker


	protected ArrayList<zdfComponent> componentList;

	/** Auxilliary variables */
	GregorianCalendar stationStart, stationFinish;
	public float statLat, statLon, statDep;

	public zdfStation(){
		// do nothing
	}

	public zdfStation(SpecialMiniSeedReader m){

	  irs = 44; // initial station length

	  // extract station information from SpecialMiniSeedReader
	  NetworkInfo n = m.statInfo;	

	  // set the station information	  
	  setStationInfo(n);

	  // set time correction
	  tcrr = 0;

	  // set sample interval
          sampin = 1 / m.delta;

	  // set event and azimuth
	  if (m.eventInfo.eventLat != -12345){
	    delta = m.azi.rangeDg;
	    azim  = m.azi.azim;
	  }	
	  else{
	    delta = 0; 
	    azim = 0;
	  }

	  cend[0] = 's';
	  cend[1] = 's';
 	  cend[2] = 'z';
	  cend[3] = 'z';

	  // initiate number of components
	  ncomp = 0;
	  componentList = new ArrayList<zdfComponent>();

	  // add reader data to component
	  addComponent(m);
	}


	public void addComponent(SpecialMiniSeedReader m){
	  zdfComponent zcomp = new zdfComponent(m);
	  componentList.add(zcomp);
	  irs += (6*4 + zcomp.ndat*4); 
	  ncomp++;   
	}


	public void setStationInfo(NetworkInfo n){

	    // set station name
	    int i;
	    for (i=0; i<4 && i<n.station.length(); i++){
		name[i] = n.station.charAt(i);
	    }
	    while (i<4){
		name[i++] = ' ';		
	    }	

	    // get starting and ending date
	    stationStart = n.getStartDate();
	    stationFinish = n.getEndDate();

	    // set header parameters	    
	    sday = stationStart.get(Calendar.DAY_OF_MONTH);
	    shr = stationStart.get(Calendar.HOUR_OF_DAY);
	    smin = stationStart.get(Calendar.MINUTE);
	    ssec = stationStart.get(Calendar.SECOND);

	    // set station coordinate
	    statLat = Float.parseFloat(n.lat);
	    statLon = Float.parseFloat(n.lon);
	    statDep = Float.parseFloat(n.dep);
	
	}

	public int writeZdfStation(DataOutputStream dos){
	    try{

	        /** Writing the header */
		dos.writeInt(irs);

		for (int i=0; i<4; i++)
		    dos.writeByte((byte)name[i]);
		
		dos.writeInt(sday);
		System.out.println(sday + " sday");

		dos.writeInt(shr);
		System.out.println(shr + " shr");

		dos.writeInt(smin);
		System.out.println(smin + " smin");

		dos.writeFloat(ssec);
		System.out.println(ssec + " ssec");

		dos.writeFloat(tcrr);
		System.out.println(tcrr + " tcrr");

		dos.writeFloat(delta);
		System.out.println(delta + " delta");

		dos.writeFloat(azim);
		System.out.println(azim + " azim");

		dos.writeFloat(sampin);
		System.out.println(sampin + " sampin");

		dos.writeInt(ncomp);
		System.out.println(ncomp + " components");

		/** Now write the data information */	
	    	for (int i=0; i<componentList.size(); i++){
	   	    zdfComponent curComp = componentList.get(i);
	   	    curComp.writeZdfComponent(dos);
	    	}

		for (int i=0; i<4; i++){
		    dos.writeByte((byte)cend[i]);
		}

	    }
	    /** Catch exceptions */	
	    catch ( SecurityException securityException )
	    {
	    	/** Security exception */
	    	System.err.println( "NO ACCESS" );
	    	return -1;
	    }
	    catch ( IOException e){
	    	/** IO exception */
	    	System.err.println("IO ERROR: " + e);
	    	return -2;
	    }

	    
	    return 1;   
	}

}
