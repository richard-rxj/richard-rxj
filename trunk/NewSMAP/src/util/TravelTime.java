package util;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import seqi.CatalogEvent;
import seqi.SEQI;
import seqi.StationInfo;

import edu.sc.seis.TauP.*;




/**
 * TravelTime is a class responsible for providing a facade to compute the
 * travel time of an event from a source with a particular depth to a
 * destination with a given azimuth angle distance from the source.
 * 
 * Other parameters to be provided are the phase and (travel) velocity model.
 * 
 * Copyright (C) 2010, Research School of Earth Sciences, The Australian
 * National University
 * 
 * @author huynh
 * 
 *         Created on: Oct 14, 2010
 */
public class TravelTime {

	private static final String TAUP_MODEL_PATH = SEQI.SEQI_HOME  + "/data/velocity_models";

	/**
	 * Compute the travel time from a source location, with a depth, to a
	 * destination with an angle distance from the source (measured along the
	 * greatest circle on the earth surface).
	 * 
	 * The travel time is computed for a particular phase of the seismic wave,
	 * according to a velocity model.
	 * 
	 * 
	 * @param srcDepth
	 *            The depth of the source in kms
	 * @param angularDist
	 *            The great-circle distance on the surface of the earth, between
	 *            the source and the destination, measured in degrees.
	 * @param phase
	 *            The phase for which the travel time is computed.
	 * @param model
	 *            The TauModel of propogation time.
	 * 
	 * @return the travel time (in seconds)
	 * 			-1 if the phase cannot reach the destination.
	 */
	public static double travelTime(double srcDepth, double angularDist,
			PhaseName phase, TauModel model) {
		try {
			TauP_Time tauPTime = new TauP_Time(model);

			// Reset the seismic phases
			tauPTime.clearPhaseNames();
			tauPTime.appendPhaseName(phase);

			// Correct the TauModel for the given source depth.
			tauPTime.depthCorrect(srcDepth);

			// Compute travel time
			tauPTime.calculate(angularDist);

			// Get arrival information
			Arrival[] arrivals = tauPTime.getArrivals();

			// There's only an Arrival instance as the output
			if (arrivals.length > 0)
				return arrivals[0].getTime();
			else 
				// phase not reaching the destination.
				return -1;
		} catch (TauModelException e) {
			throw new RuntimeException("Model file " + model.getModelName()
					+ " is not compatible with the current version."
					+ "Recreate using taup_create.", e);
		}
	}
	
	
	
	/**
	 * Compute the travel time from a source seismic event, to a destination
	 * location with the given longitude and latitude.
	 * 
	 * The travel time is computed for a particular phase of the seismic wave,
	 * according to a velocity model.
	 * 
	 * 
	 * @param event
	 *            The source event.
	 *            
	 * @param lon
	 *            The longitude of the destination.
	 *            
	 * @param lat
	 *            The latitude of the destination.
 	 * 
	 * @param phase
	 *            The phase for which the travel time is computed.
	 * @param model
	 *            The TauModel of propagation time.
	 * 
	 * @return the travel time (in seconds)
	 * 			-1 if the phase cannot reach the destination.
	 */
	public static double travelTime(CatalogEvent event, 
			float lon, float lat, PhaseName phase, TauModel model) {
		
		// Compute the great circle angular (azimuth) distance between the event
		// location and the destination.
		double angularDist = SphericalCoords.greatCirleDist(event.longitude,
				event.latitude, lon, lat);

		return travelTime(event.depth, angularDist, phase, model);	
	}
	

	/**
	 * Compute the travel time from a source seismic event, to a destination
	 * station.
	 * 
	 * The travel time is computed for a particular phase of the seismic wave,
	 * according to a velocity model.
	 * 
	 * 
	 * @param event
	 *            The source event.
	 * @param station
	 *            The destination station.
	 * @param phase
	 *            The phase for which the travel time is computed.
	 * @param model
	 *            The TauModel of propagation time.
	 * 
	 * @return the travel time (in seconds)
	 * 			-1 if the phase cannot reach the destination.
	 */
	public static double travelTime(CatalogEvent event, StationInfo station,
			PhaseName phase, TauModel model) {
		return travelTime(event, station.lon, station.lat, phase, model);
	}

	
	/**
	 * Compute the travel time from a source seismic event, to a destination
	 * station.
	 * 
	 * The travel time is computed for a particular phase of the seismic wave,
	 * according to a velocity model.
	 * 
	 * 
	 * @param event
	 *            The source event.
	 * @param station
	 *            The destination station.
	 * @param phaseName
	 *            The phase for which the travel time is computed.
	 * @param modelName
	 *            The String name of the TauModel of propagation time.
	 * 
	 * @return the travel time (in seconds)
	 * 				-1 if the phase cannot reach the destination.
	 */
	public static double travelTime(CatalogEvent event, StationInfo station,
			String phaseName, String modelName) {
		return travelTime(event, station.lon, station.lat, phaseName, modelName);
	}
	
	
	/**
	 * Compute the travel time from a source seismic event, to a destination
	 * station.
	 * 
	 * The travel time is computed for a particular phase of the seismic wave,
	 * according to a velocity model.
	 * 
	 * @param event
	 *            The source event.
	 *            
	 * @param lon
	 *            The longitude of the destination.
	 *            
	 * @param lat
	 *            The latitude of the destination.
	 *
	 * @param phaseName
	 *            The phase for which the travel time is computed.
	 * 
	 * @param modelName
	 *            The String name of the TauModel of propagation time.
	 * 
	 * @return the travel time (in seconds).
	 * 			-1 if the phase cannot reach the destination.
	 * 
	 */
	public static double travelTime(CatalogEvent event, float lon, float lat, 
			String phaseName, String modelName) {
		
		PhaseName phase = new PhaseName(phaseName);
		TauModel model = loadVelocityModel(modelName, TAUP_MODEL_PATH);		
		
		return travelTime(event, lon, lat, phase, model);
	}
	
	
	/**
	 * Compute the travel time from a list of source seismic events, 
 	 * to a destination station.
	 * 
	 * The travel time is computed for a particular phase of the seismic wave,
	 * according to a velocity model.
	 * 
	 * 
	 * @param event
	 *            The source event.
	 * @param station
	 *            The destination station.
	 * @param phaseName
	 *            The phase name for which the travel time is computed.
	 * @param modelName
	 *            The name of the model of wave propagation.
	 * 
	 * @return a list of travel times (in seconds) corresponding 
	 * 			to the source events. 		   
	 * 			-1 if the phase cannot reach the destination.
	 */
	public static List<Double> travelTimes(List<CatalogEvent> events, StationInfo station,
			String phaseName, String modelName) {
		
		PhaseName phase = new PhaseName(phaseName);

		TauModel model = loadVelocityModel(modelName, TAUP_MODEL_PATH);
		
		
		List<Double> tTimes = new ArrayList<Double>(); 
		
		for (CatalogEvent catalogEvent : events) {
			double time = travelTime(catalogEvent, station, phase, model);
			tTimes.add(time);
		}

		return tTimes;
	}
	
	
	/**
	 * Compute the travel time from a list of source seismic events, 
 	 * to a destination location with the given longitude and latitude.
	 * 
	 * The travel time is computed for a particular phase of the seismic wave,
	 * according to a velocity model.
	 * 
	 * 
	 * @param event
	 *            The source event.
	 *            
	 * @param lon
	 *            The longitude of the destination.
	 *            
	 * @param lat
	 *            The latitude of the destination.
	 *                        
	 * @param phaseName
	 *            The phase name for which the travel time is computed.
	 *            
	 * @param modelName
	 *            The name of the model of wave propagation.
	 * 
	 * @return a list of travel times (in seconds) corresponding 
	 * 			to the source events.
	 * 			-1 if the phase cannot reach the destination.
	 */
	public static List<Double> travelTimes(List<CatalogEvent> events, float lon,
			float lat, String phaseName, String modelName) {
		
		PhaseName phase = new PhaseName(phaseName);

		TauModel model = loadVelocityModel(modelName, TAUP_MODEL_PATH);
		
		
		List<Double> tTimes = new ArrayList<Double>(); 
		
		for (CatalogEvent catalogEvent : events) {
			double time = travelTime(catalogEvent, lon, lat, phase, model);
			tTimes.add(time);
		}

		return tTimes;
	}
	

	/**
	 * Load a TauP velocity model given the location of the file storing the
	 * model.
	 * 
	 * @param modelName
	 *            The name of the model (with or without the extension), e.g.
	 *            iasp91 or iasp91.taup
	 * @param modelDirPath
	 *            The full path name of the parent directory containing the
	 *            model file.
	 * 
	 * @return The TauModel containing information of velocity model.
	 */
	public static TauModel loadVelocityModel(String modelName,
			String modelDirPath) {

		try {
			TauModel tModel = TauModelLoader.load(modelName, modelDirPath);
			return tModel;
		} catch (ClassNotFoundException e) {
			Alert
					.error(
							"Caught ClassNotFoundException",
							e.getMessage()
									+ "\nThere must be something wrong with your installation of TauP.");
			throw new RuntimeException(
					"Caught ClassNotFoundException"
							+ e.getMessage()
							+ "\nThere must be something wrong with your installation of TauP.",
					e);
		} catch (InvalidClassException e) {
			Alert.error("Model file " + modelName
					+ " is not compatible with the current version.",
					"Recreate using taup_create.");
			throw new RuntimeException("Model file " + modelName
					+ " is not compatible with the current version."
					+ "Recreate using taup_create.", e);
		} catch (OptionalDataException e) {
			Alert.error("Failure of an object read operation",
					"Check the model file path");
			throw new RuntimeException("Model file " + modelName
					+ " may not exist. " + "Check its path.", e);
		} catch (StreamCorruptedException e) {
			Alert.error("Failure to read the model file",
					"Check the model file path");
			throw new RuntimeException("Model file " + modelName
					+ " may not exist. " + "Check its path.", e);
		} catch (IOException e) {
			Alert.error("An IO failure has occurred", "");
			throw new RuntimeException("Model file " + modelName
					+ " may not exist. " + "Check its path.", e);
		}
	}

	/**
	 * Create a TauP velocity model and store the model file in a given
	 * location.
	 * 
	 * @param modelFileName
	 *            The original source name of the model with the extension .tvel
	 *            or .nd.
	 * 
	 * @param directory
	 *            The full path name of the parent directory containing the
	 *            model file.
	 * 
	 * @return The TauModel created.
	 */
	private static TauModel createVelocityModel(String modelFileName,
			String directory) {
		TauP_Create tauPCreate = new TauP_Create();
		tauPCreate.setModelFilename(modelFileName);
		tauPCreate.setDirectory(directory);

		String file_sep = System.getProperty("file.separator");
		VelocityModel vMod = new VelocityModel();

		// Set the type of velocity source file. 
		String velFileType = "";
		if (modelFileName.startsWith("GB.")) {
			velFileType = "nd";
		} else if (modelFileName.endsWith(".nd")) {
			velFileType = "nd";

		} else if (modelFileName.endsWith(".tvel")) {
			velFileType = "tvel";
		}
		vMod.setFileType(velFileType);

		// Read the velocity model file.
		try {
			vMod.readVelocityFile(directory + file_sep + modelFileName);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (VelocityModelException e1) {
			e1.printStackTrace();
		}

		SphericalSModel sMod;
		if (vMod.getSpherical()) {
			sMod = new SphericalSModel();
		} else {
			System.out.println("Flat slowness model not yet implemented."
					+ "\n   Using spherical slowness model.");
			sMod = new SphericalSModel();
		}

		// Load properties of sMod if stored in a file somewhere.
		Properties toolProps;
		try {
			toolProps = PropertyLoader.load();
		} catch (Exception e) {
			Alert.warning("Unable to load properties, using defaults.", e
					.getMessage());
			toolProps = new Properties();
		}
		
		// set sMod defaults from properties
		sMod.setMinDeltaP(Double.valueOf(
				toolProps.getProperty("taup.create.minDeltaP", "0.1"))
				.doubleValue());
		sMod.setMaxDeltaP(Double.valueOf(
				toolProps.getProperty("taup.create.maxDeltaP", "11.0"))
				.doubleValue());
		sMod.setMaxDepthInterval(Double.valueOf(
				toolProps.getProperty("taup.create.maxDepthInterval", "115.0"))
				.doubleValue());
		sMod.setMaxRangeInterval(Double.valueOf(
				toolProps.getProperty("taup.create.maxRangeInterval", "1.75"))
				.doubleValue());
		sMod.setMaxInterpError(Double.valueOf(
				toolProps.getProperty("taup.create.maxInterpError", "0.05"))
				.doubleValue());
		sMod.setAllowInnerCoreS(Boolean.valueOf(
				toolProps.getProperty("taup.create.allowInnerCoreS", "true"))
				.booleanValue());

		try {
			sMod.createSample(vMod);
		} catch (NoSuchMatPropException e) {
			e.printStackTrace();
		} catch (NoSuchLayerException e) {
			e.printStackTrace();
		} catch (SlownessModelException e) {
			e.printStackTrace();
		} // Create slowness sampling.

		TauModel tMod = new TauModel();

		// Creates tau model from slownesses
		try {
			tMod.calcTauIncFrom(sMod);
		} catch (NoSuchLayerException e) {
			e.printStackTrace();
		} catch (NoSuchMatPropException e) {
			e.printStackTrace();
		} catch (SlownessModelException e) {
			e.printStackTrace();
		} catch (TauModelException e) {
			e.printStackTrace();
		}
		
		// Write the model file.
		String outFile;
		if (!directory.equals(".")) {
			outFile = directory + file_sep + vMod.getModelName() + ".taup";
		} else {
			outFile = vMod.getModelName() + ".taup";
		}

		try {
			tMod.writeModel(outFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return tMod;
	}

	public static void main(String[] args) {
		
		// Test the tTime routine.
		TauModel model = loadVelocityModel("1066b", TAUP_MODEL_PATH);
		double secs = travelTime(20, 40, new PhaseName("P"), model);
		System.out.println("Time travelled " + secs + " seconds");
	}
}
