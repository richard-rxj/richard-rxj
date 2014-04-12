//=============================================================================
//
                                                                                                                 // File: IterativeDeconvolution.java 
//
// Dir: /home/mars/cristo/IterativeDeconvolutionJava
//
// Usage: java IterativeDeconvolution 
//
//=============================================================================

package IterdeconPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.*;

public class IterativeDeconvolution {
    
    public static void main( String args[] ) throws IOException {

    	for(String arg:args) {
    		System.out.println(arg);
    	}
    	
    	

        
        System.out.println("\n       Program: IterativeDeconvolution.java");

        System.out.print(" ===========================================");
        System.out.println("===================\n");

        System.out.print(" Program IterativeDeconvolution.java is ");	
	System.out.print(" based on the program \n");
        System.out.print(" iterdeconfd.f - Version 1.04 written by ");
     
          
        
	System.out.println(" Chuck Ammon from");
        System.out.println( " Saint Louis University. The program implements Kikuchi and ");
        System.out.println(" algorithm (Kikuchi, M. and Kanamori H.,1982, Bull. Seismol.");
        System.out.println(" Soc. Am., 72: 491-596.)");


        System.out.print("\n This version of Java program produces minimal output only,\n");
   
	System.out.println(" it means that the results from each iteration are not stored");
        System.out.println(" in the SAC files. The final prediction is written to SAC file:");
        System.out.println(" deconvolution.out\n");
        System.out.print(" ===========================================");
        System.out.println("===================");



        FileWriter fstream = new FileWriter("output.txt");
        BufferedWriter out = new BufferedWriter( fstream );
      
    	//Global variables

	int forward = 1;
	int inverse = -1;

	int maxSize = 8192;
	int maxAllowedBumps = 200;

	int npts = 0;

	double dt = 0.0;

        boolean lpositive = false;
	boolean verbose =  false;

        int numberPointsRead = 0;
        
        float spacing = 0.0f;
	float bTime = 0.0f;
	float eTime = 0.0f;

        double power = 0.0;

	// Global arrays

	int shifts[] = new int[maxAllowedBumps];
        double amps[] = new double[maxAllowedBumps];

	double f[] = new double[maxSize];
          
        double g[] = new double[maxSize];

        double p[] = new double[maxSize];
        double r[] = new double[maxSize];

  
      	String resFiles[] = new String[maxAllowedBumps];
	
	//========================================================
	//
	// Read parameters in:
	//
	//=======================================================

        String input = null;
     
        BufferedReader br =
              new BufferedReader(new InputStreamReader(System.in));

        System.out.print("\n");

        System.out.print(" What is the numerator file?:");

       // String numeratorIn  = br.readLine();
        String numeratorIn=args[0];
        
        SacTimeSeriesX sacNum = new SacTimeSeriesX();

        try {

            sacNum.read(numeratorIn);
	} catch (FileNotFoundException e) {

            System.out.println(" ===========================================");
	    System.out.println(" File : [ " + numeratorIn + " ] does not exist!");

            System.out.println(" Exiting...");
       
            System.out.println(" ===========================================");

            System.exit(0);
	}

        System.out.print("\n");

        System.out.print( " What is the denominator file?:");

        //String denominatorIn = br.readLine();
        String denominatorIn=args[1];
        
        SacTimeSeriesX sacDen= new SacTimeSeriesX();

        try {

            sacDen.read(denominatorIn);

	} catch (FileNotFoundException e) {

            System.out.println(" ===========================================");
	    System.out.println(" File : [ " + denominatorIn + " ] does not exist!");

            System.out.println(" Exiting...");
       
            System.out.println(" ===========================================");

            System.exit(0);
	}

        System.out.print("\n");

        System.out.print(" What is the max number of iterations?:");

        //input = br.readLine();
        input=args[2];

        System.out.print("\n");

        int nbumps = Integer.parseInt( input );

        System.out.print(" What is the phase shift(secs) for output?:");

	    //input = br.readLine();
        input=args[3];

        System.out.print("\n");

        double theShift = Double.parseDouble( input );

        System.out.print(" What is the minimum percent error increase to accept?:");

        //input = br.readLine();
   		input=args[4];

        double tol = Double.parseDouble( input );
  
        System.out.print("\n");
         
        System.out.print( " What is the Gaussian filter width?:");

        //input = br.readLine();
        input=args[5];

        double gwidth = Double.parseDouble(input );

        System.out.print("\n");

	    System.out.print(" Allow negative pulses (1-->yes, 0-->no)?:");

        //input = br.readLine();
	    input=args[6];

        System.out.print("\n");

        int idum = Integer.parseInt(input);


	//===========================================================
       

        if ( idum == 0 )  {
         
	    lpositive = true;
	}

     
	//============================================================
        // Open file for text output: 
        //===================================================
	//
	// Create resFiles[]
	//
	//===================================================

	for (int i = 1; i < maxAllowedBumps; i++ ) {

	    if ( i < 10 ) {
		resFiles[i] = "r00" + String.valueOf(i);
	    } 

            if ( i>=10 & i<=99) {
		resFiles[i] = "r0" + String.valueOf(i);
             }

            if( i >= 100 ) {
 		resFiles[i] = "r" + String.valueOf(i) ;
	    }
           
	}


	//===========================================================
        // Objects
	//===========================================================

	SacTimeSeriesX sac = new SacTimeSeriesX();

	FFT fft = new FFT();
 
        AuxilliaryMethods aux = new AuxilliaryMethods();

        Complex complex = new Complex(0.0, 0.0);

	//============================================================
	//	
	// Read numerator from args list i.e "num1.r"
	//
	//===========================================================

        numberPointsRead = aux.getNumberOfPointsFromSACFile( numeratorIn);

        spacing   = aux.getSpacingFromSACFile( numeratorIn ); 
       
        bTime = aux.getBTimeFromSACFile( numeratorIn );

        eTime = aux.getETimeFromSACFile (numeratorIn );

        float arrayNumerator[] = new float[numberPointsRead];
	
        aux.readSacFile( numeratorIn, numberPointsRead, arrayNumerator);

        //============================================================
	// Put read arrayNumerator into f[]
	//============================================================
     
        for (int i = 0; i < numberPointsRead; i++ ) {
             
	    f[i+1] = arrayNumerator[i];
	}
    
	// System.out.println(" Numerator: " + numeratorIn + " read.\n");

	//===========================================================
	// Read denominator from args list
	//===========================================================

        numberPointsRead = aux.getNumberOfPointsFromSACFile( denominatorIn);

        spacing   = aux.getSpacingFromSACFile( denominatorIn ); 
       
        bTime = aux.getBTimeFromSACFile( denominatorIn );

        eTime = aux.getETimeFromSACFile (denominatorIn );

        float arrayDenominator[] = new float[numberPointsRead];
	
        aux.readSacFile( denominatorIn, numberPointsRead, arrayDenominator);
      

        //==========================================================
	// Put read arrayNumerator into f[]
	//==========================================================
     
        for (int i = 0; i < numberPointsRead; i++ ) {
             
	    g[i+1] = arrayDenominator[i];
	}
 
	//==========================================================

        npts = numberPointsRead;

	int n = 1;
	while ( n <= npts) {
	    n = n*2;
	}
        
        npts = n;
        
        dt = spacing;       

	//==========================================================
	//  Filter f and g signals
	//==========================================================

        aux.gFilter(f, gwidth, npts, dt);
        aux.gFilter(g, gwidth, npts, dt);

	//=======================================================
	// Write filtered signal f[] to SAC File: numerator
	//=======================================================

        spacing = (float)dt;
        
        eTime = (npts - 1)*spacing;

        String numerator = "numerator";
	
        aux.writeSacFile(numerator, npts, f, spacing, bTime, eTime );
 
	 //=======================================================
	 // Write filtered signal f[] to SAC File: observed
	 //=======================================================

	 String observed = "observed";

         aux.writeSacFile(observed, npts, f, spacing, bTime, eTime );

        //=======================================================
	// Write filtered signal g[] to SAC File: denominator
	//=======================================================
      
       	 String denominator = "denominator";

         aux.writeSacFile(denominator, npts, g, spacing, bTime, eTime );

        //=========================================================
	// Calculate power of numerator for error scaling
	//==========================================================

         power=0.0;

         for (int i = 0; i <= npts; i++) {

	    power = power + f[i]*f[i];
	 }

	
        double power1 = 0.0;
        
        for (int i = 0; i <= npts; i++) {

	    power1 = power1 + g[i]*g[i];
	}
  
	//===========================================================
	//  Correlate f & g signals
	//===========================================================

	aux.crossCorrelationFFT( f, g, npts, maxSize,dt );

	//===========================================================
	//  Find the peak in correlation
	//===========================================================
        
        int maxLag = npts/2;
        double spikeDelay = maxLag*dt;

        System.out.println(" ================================================");

        System.out.println("The maximum spike delay is:  " + spikeDelay );
          
        System.out.println();

        if ( lpositive ) {

	    amps[1] = aux.getMaxValue(g, maxLag);
            shifts[1] = aux.getMaxIndex( g, maxLag);
	
 
	} else {

            amps[1] = aux.getAbsMaxValue( g, maxLag );
            shifts[1] = aux.getAbsMaxIndex( g, maxLag );
	}

        
        amps[1]= amps[1]/dt;
      
        int nshifts = 1; // Starts nshifts

	//==========================================================
	//   Zeroes f & g
	//==========================================================
       
        aux.zero(f,maxSize);
        aux.zero(g,maxSize);
   
        //==========================================================
	//   Reading f and g again from SAC files:
	//==========================================================

	// Numerator

	int numberRead = aux.getNumberOfPointsFromSACFile( numeratorIn);

        float arrayNN[] = new float[numberRead];

        aux.readSacFile( numeratorIn, numberRead, arrayNN );

        dt = aux.getSpacingFromSACFile( numeratorIn );

        for (int i =  0; i < numberRead; i++) {

	    f[i+1] = arrayNN[i];
	}

	//  Denominator	

      
        numberRead = aux.getNumberOfPointsFromSACFile( denominatorIn);

        float arrayDD[] = new float[numberRead];

        aux.readSacFile( denominatorIn, numberRead, arrayDD );

        dt = aux.getSpacingFromSACFile( denominatorIn );

        for (int i =  0; i < numberRead; i++) {

	    g[i+1] = arrayDD[i];
	}

        //=========================================================
	//  Zero vector p[]
	//=========================================================

        aux.zero(p,maxSize);

 	//============================================================
	// Compute predicted deconvolution result - build deconvolution
	//=============================================================

	aux.buildDeconvolution(amps, shifts, p, nshifts,npts, gwidth, dt );

        //==============================================================
	// Convolve the prediction with the denominator g
	//==============================================================
        
        aux.convolveRealLoop(p,g, npts, dt );

	//==============================================================
	//  Filter  f & g again!
	//==============================================================
    
        aux.gFilter(f, gwidth, npts, dt);
        aux.gFilter(g, gwidth, npts, dt );


	// System.out.println("=====================================\n");

        double sumsq_i = 1.0;

        //========================================================
	//  Compute the residual ( initial error is: 1.0 )
      	//========================================================

        double sumsq_ip1 = aux.getResiduum( f,p,r, npts );

        sumsq_ip1 = sumsq_ip1/power;

        double d_error = 100.0*(sumsq_i - sumsq_ip1);

	// System.out.println(" Error = " + d_error  );

     

  	System.out.println("File         Spike amplitude    Spike Delay   Misfit   Improvement");

        

	  System.out.print( resFiles[1] + "       ");
          System.out.format("%1$17.9e       %2$8.3f  %3$6.2f%%    %4$9.4f%%%n",
                            ( dt*amps[1]),  
			    ((shifts[1] - 1)*dt ), 
  			    (100.0*sumsq_ip1),
			     d_error );
	  // Step 1

          //============================================================
	  //============================================================

	      
	    String s1 = "File         Spike amplitude    Spike Delay   Misfit   Improvement" + "\n";

	  String s2 = resFiles[1] + "     " +
	      Double.toString( aux.round( dt*amps[1], 9));
          
	  String s3 = "  " + Double.toString(aux.round((shifts[1] - 1)*dt, 3)) + "  " +
	      Double.toString(aux.round(100.0*sumsq_ip1,2)) + " " +
              Double.toString(aux.round( d_error,4)) + "\n";

          out.write(s1);
	  out.write(s2);
          out.write(s3);
      
	  //=============================================================
	  //============================================================

	  // SacTimeSeriesX sacDG = new SacTimeSeriesX();
	  // SacTimeSeriesX sacFG = new SacTimeSeriesX();

        String fileInD = denominatorIn;

        String fileInF  = numeratorIn;

        //=============================================================
	//    Start looping
	//=============================================================

	// for( int loopIndex = 1; loopIndex < nbumps; loopIndex++ ) {

        while( (d_error > tol) && (nshifts < nbumps)) {

	    nshifts = nshifts + 1;
       
	    sumsq_i = sumsq_ip1;

	    aux.zero( g, maxSize);

	    //=========================================================
            // Read from SAC file: denominator( den1.z) signal g[]
            //=========================================================

            int numberD = aux.getNumberOfPointsFromSACFile( fileInD);

            spacing   = aux.getSpacingFromSACFile( fileInD ); 
       
            bTime = aux.getBTimeFromSACFile( fileInD );

            eTime = aux.getETimeFromSACFile ( fileInD );

	    float arrayD[] = new float[numberD];
	
	    aux.readSacFile( fileInD, numberD, arrayD );

	    //=========================================================
	    // Put read arrayD into g[]
	    //=========================================================
     
             for (int i = 0; i < numberPointsRead; i++ ) {
             
		 g[i+1] = arrayD[i];
	     }

	     //=======================================================
	     // Filter g[]
	     //=======================================================

	      aux.gFilter( g, gwidth, npts, dt );

	      //=====================================================
	      // Correlate r[] & g[]
	      //=====================================================

	      aux.crossCorrelationFFT( r, g, npts, maxSize,dt );

	      if ( lpositive ) {
		  amps[nshifts]   = aux.getMaxValue(g, maxLag);
		  shifts[nshifts] = aux.getMaxIndex( g, maxLag);
	      } else {
		  amps[nshifts] = aux.getAbsMaxValue( g, maxLag );
		  shifts[nshifts] = aux.getAbsMaxIndex( g, maxLag );
	      }
        
	      amps[nshifts] = amps[nshifts]/dt;

	      //=====================================================
	      // Zero p[]
	      //=====================================================

	      aux.zero( p, maxSize );

	      // Step 2

              //=====================================================
	      // Buid Deconvolution
	      //=====================================================
       
       	      aux.buildDeconvolution(amps, shifts, p, nshifts,npts, gwidth,dt);
	     
	      //====================================================
	      // Zero g[]
	      //====================================================

	      aux.zero( g, maxSize);
        
	      // Step 3

	      //=========================================================
	      // Read from SAC file: denominator( den1.z) signal g[]
	      //=========================================================

	      numberD = aux.getNumberOfPointsFromSACFile( fileInD);

	      spacing   = aux.getSpacingFromSACFile( fileInD ); 
       
	      bTime = aux.getBTimeFromSACFile( fileInD );

	      eTime = aux.getETimeFromSACFile ( fileInD );

	      float arrayDDD[] = new float[numberD];
	
	      aux.readSacFile( fileInD, numberD, arrayDDD );

	      //=========================================================
	      // Put read arrayDDD into g[]
	      //=========================================================
     
	      for (int i = 0; i < numberD; i++ ) {
             
		  g[i+1] = arrayDDD[i];
	      }

	      // Step 4
	      
	      //==========================================================
	      // Convolve p[] & g[]
	      //==========================================================
  
	      aux.convolveRealLoop(p,g, npts, dt );

	      //==========================================================
	      // Zero f[]
	      //==========================================================

	      aux.zero(f,maxSize);


	      // Read signal f from numerator: num1.r


	      //=========================================================
	      // Read from SAC file: numerator ( num1.r) signal f[]
	      //=========================================================

	      int numberF = aux.getNumberOfPointsFromSACFile( fileInF);

	      spacing   = aux.getSpacingFromSACFile( fileInF ); 
       
	      bTime = aux.getBTimeFromSACFile( fileInF );

	      eTime = aux.getETimeFromSACFile ( fileInF );

	      float arrayF[] = new float[numberF];
	
	      aux.readSacFile( fileInF, numberF, arrayF );

	      //=========================================================
	      // Put read arrayF into f[]
	      //=========================================================
     
	      for (int i = 0; i < numberF; i++ ) {
             
		  f[i+1] = arrayF[i];
	      }

	      dt = spacing;

	      //==========================================================
	      // Filter f[]
	      //==========================================================

	      aux.gFilter( f, gwidth, npts,dt );

	      // Step 5

	      //==========================================================
	      // Get residuum
	      //==========================================================

	      sumsq_ip1 = aux.getResiduum( f,p,r, npts );

              sumsq_ip1 = sumsq_ip1/power;

	      d_error = 100.0*(sumsq_i - sumsq_ip1);

	  System.out.print( resFiles[nshifts] + "       ");
          System.out.format("%1$17.9e       %2$8.3f  %3$6.2f%%    %4$9.4f%%%n",
                            ( dt*amps[nshifts]),  
			    ((shifts[nshifts] - 1)*dt ), 
  			    (100.0*sumsq_ip1),
			     d_error );

      
	
	  String ss2 = resFiles[nshifts] +
            "     " + Double.toString(aux.round( dt*amps[nshifts],9));
          
	  String ss3 = "  " +
	      Double.toString(aux.round( (shifts[nshifts] - 1)*dt, 3) )
            + "  " +								    	  Double.toString(aux.round((100.0*sumsq_ip1),2)) + 
           " " + Double.toString(aux.round(d_error,4)) +
           "\n";

  
	  out.write(ss2);
          out.write(ss3);
      
	  // out.close();
 
	

	     //===================================================
	     //===================================================

        } // end loopIndex 

        
        out.close();
 

	System.out.println();

        System.out.format(" Last Error Change = %1$9.4f%%%n", d_error );

        System.out.println();

        double fit = 100.0 - 100.0*sumsq_ip1;
  
   	//============================================================
	// Check d_error
	//============================================================

        if ( d_error <= tol ) {

	    nshifts = nshifts - 1;
         
	    fit = 100.0 - 100.0*sumsq_i;

       System.out.println("\n Reached min improvement tolerance - stopping!");

	}
  
	//============================================================
	// Check number of bumps
	//============================================================

	if ( nbumps >= maxAllowedBumps ) {

   System.out.println("\n Exceeded allowed number of iteratons - stopping!");

	}
      
      //
      //==============================================================
      //

      System.out.println(" Number of bumps/iterations in final result:   " +
        
			 nshifts );

      System.out.print(" The final deconvolution reproduces");
      System.out.format(" %1$8.1f%% of the signal%n", fit );

      //===============================================================
      // Zero p[]
      //===============================================================
 
      aux.zero(p,maxSize);
      
      //======================================================
      // Build Deconvolution
      //=======================================================

      aux.buildDeconvolution(amps,shifts,p,nshifts,npts,gwidth,dt );

      //========================================================
      // Zero g[]
      //========================================================

      aux.zero( g,maxSize);
      
      //=========================================================
      // Read from SAC file: denominator( den1.z) signal g[]
      //=========================================================

       int numberDX = aux.getNumberOfPointsFromSACFile( fileInD);

       spacing   = aux.getSpacingFromSACFile( fileInD ); 
       
        bTime = aux.getBTimeFromSACFile( fileInD );

        eTime = aux.getETimeFromSACFile ( fileInD );

        float arrayDX[] = new float[numberDX];
	
        aux.readSacFile( fileInD, numberDX, arrayDX );

        //=========================================================
	// Put read arrayDX into g[]
        //=========================================================
     
        for (int i = 0; i < numberDX; i++ ) {
             
	    g[i+1] = arrayDX[i];
         }

	//========================================================
	// Convolve p[] & g[]
	//========================================================

	aux.convolveRealLoop(p,g,npts,dt);
     
        //========================================================
	// Write final prediction p[] to  SAC File: predictedX
	//========================================================

	String predicted = "predicted";

        spacing = (float)dt;
        
        eTime = (npts - 1)*spacing;
           	
        aux.writeSacFile(predicted, npts, p, spacing, bTime, eTime );
     	
	//========================================================
	// Zero g[] & p[]
	//========================================================

	aux.zero(g, maxSize);
        aux.zero(p, maxSize);

	//=========================================================
	// Build Deconvolution
	//=========================================================

        aux.buildDeconvolution(amps, shifts, p, nshifts,npts, gwidth, dt );

	//=========================================================
	//  Phase Shift
	//=========================================================

	aux.phaseShift(p, theShift, npts, dt );
	//
	//=====================================================
	//
       
        System.out.println("\n Final deconvolution has been written to: deconvolution.out\n");

        String finalResult = "deconvolution.out";

	int numberPtsOut  = npts;
        float beginingOut = -(float)theShift;
	float endOut      = -(float)(theShift + ( npts - 1) * dt);
        float user0       = (float)gwidth;

        int itypeOut      = 1;
	int levenOut      = 1;
	int lpspolOut     = 0;
        int lovrokOut     = 1;
	int lcaldaOut     = 1;
        int unused27Out   = 0;

	aux.writeSacFile( finalResult, numberPtsOut, p, spacing, beginingOut,
			  endOut, user0, itypeOut,levenOut,lpspolOut,lovrokOut,
                          lcaldaOut, unused27Out );


    } // end of main()

} // end IterativeDeconvolution class