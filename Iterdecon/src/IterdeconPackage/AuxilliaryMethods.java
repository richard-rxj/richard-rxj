package IterdeconPackage;

//
// File: AuxilliaryMethods.java
//


import java.io.*;

public class AuxilliaryMethods {

//=====================================================================
//=====================================================================
// 
// Implementation of Fourier Transform from NR:
//
//=====================================================================
//=====================================================================

    void realFour( double data[], int nn, int isign) {
  
        double wr,wi,wpr,wpi,wtemp,theta,tempr,tempi;

        int  i,j,m, mmax,iSTEP;
    
        int N = 2*nn;
        
        j = 1;

       
        for ( i = 1; i <= N; i = i+2) {

	
	    if (j > i) {

       	          
	        tempr=data[j];
                tempi=data[j+1];

                data[j]=data[i];
	        data[j+1]=data[i+1];

	        data[i]=tempr;
	        data[i+1]=tempi;

	    } // end if


             m=N/2;

	     //===========================================	

point1:  
 
              while ((m >= 2) && (j > m)) {
	         j=j-m;
	         m=m/2;
	         continue point1;
              }

              j=j+m;

	}// for (int i = 1;


      mmax=2;

point2:    

     while (N > mmax) {
	 iSTEP=2*mmax;
         theta= 2.0*Math.PI/(isign*mmax);
	 wpr=-2.0*Math.sin(0.5*theta)*Math.sin(0.5*theta); 
	 wpi= Math.sin(theta);
	 wr=1.0;
	 wi=0.0;
         
	 //DO 13 m=1,mmax,2

	 for ( m = 1; m <= mmax; m= m+2) {

	    for (  i = m; i <= N; i = i+iSTEP ) {

		//          DO 12 i=m,N,iSTEP
		j=i+mmax;
	       	tempr=(wr)*data[j]- (wi)*data[j+1];
		tempi= (wr)*data[j+1]+ (wi)*data[j];
		data[j]=data[i]-tempr;
		data[j+1]=data[i+1]-tempi;
		data[i]=data[i]+tempr;
		data[i+1]=data[i+1]+tempi;
		
		//12        CONTiNUE
	      }

	    wtemp=wr;
            wr=wr*wpr-wi*wpi+wr;
	    wi=wi*wpr+wtemp*wpi+wi;
	 }

	//13    CONTiNUE

	 mmax=iSTEP;
	 continue point2;

     } // while ( N > mMAX
	
  		 
    } // void FOUR
    
    //============================================================

  
    //============================================================
    // ===========================================================
    //
    // Implementation of void realft( ) from Numerical Receipes in C
    //
    // p513 correct version see TestFFT_B.java
    //
    //
    //============================================================
    //============================================================


    void realFT(double data[], int n, int isign) { 

	double WR,WI,WPR,WPI,WTEMP,theta;
        double C1,C2;
        double H1R,H2R,H1I,H2I;
        double WRS,WIS;
        int I1,I2,I3,I4;
  
	theta=Math.PI/(double)(n/2);

	  C1=0.5;
          
          if (isign == 1) {
	      C2=-0.5;

	       realFour(data,n/2,1);

	      // realFour(data,n,1);

	  } else {

	      C2=0.5;
              theta=-theta;
	  }

          WTEMP = Math.sin(0.5*theta);

	  WPR=-2.0*WTEMP*WTEMP;
  
          WPI=Math.sin(theta);
          WR=1.0 + WPR;
          WI=WPI;
          int N2P3=n+3;
          
     	  //     DO 11 I=2,N/2+1

          for( int i = 2; i <= n/4; i++ ) {

	      I1=2*i - 1;

	      I2=I1+1;
	      I3=N2P3-I2;
	      I4=I3+1;
 
	      WRS = WR;
              WIS = WI;

              H1R=C1*(data[I1]+data[I3]);

              H1I=C1*(data[I2]-data[I4]);

	      H2R=-C2*(data[I2]+data[I4]);

	      H2I=C2*(data[I1]-data[I3]);

	      data[I1]=H1R+WRS*H2R-WIS*H2I;


              data[I2]=H1I+WRS*H2I+WIS*H2R;

	      data[I3]=H1R-WRS*H2R+WIS*H2I;

	      data[I4]=-H1I+WRS*H2I+WIS*H2R;

	      WTEMP=WR;

	      WR=WR*WPR-WI*WPI+WR;

	      WI=WI*WPR+WTEMP*WPI+WI;
	     

	  } //for

	  //11    CONTINUE

          
	  if(isign == 1) {

	      H1R=data[1];
	      data[1]=H1R+data[2];
	      data[2]=H1R-data[2];

	  } else {
     
	  H1R=data[1];
	  data[1]=C1*(H1R+data[2]);
          data[2]=C1*(H1R-data[2]);

	  //   CALL FOUR1(DATA,N,-1);
              
	   realFour( data,n/2, -1);
           
	  //realFour( data,n, -1);
	  }

      
	  } // end realFT


    //==========================================================
    //==========================================================
    //
    //   twoFFT
    //
    //==========================================================
    //==========================================================

    void realFFT2( double data1[], double data2[], 
                 double fft1[],  double fft2[], int n ) {

	int nn3, nn2;

	// int jj, j;
     
        double rep, rem, aip,aim;

        //=========================================

	nn2 = 2 + n + n;

        nn3 = nn2 + 1;


        int odd, even;

        for( int i = 1; i <= n; i++ ) {

	    odd = 2*i - 1;
            even = 2*i;

            fft1[odd] = data1[i];
            fft1[even] = data2[i];

	   
	}

       
        realFour(fft1,n,1);

        fft2[1] = fft1[2];
         
        fft2[2] = 0.0;

        fft1[2] = 0.0;

        for (int j = 3; j<=n+1; j+=2) {

            rep = 0.5*(fft1[j] + fft1[nn2-j]);
            rem = 0.5*(fft1[j] - fft1[nn2-j]);
	
	    aip = 0.5*(fft1[j+1] + fft1[nn3-j]);
	    aim = 0.5*(fft1[j+1] - fft1[nn3-j]);
           
            fft1[j] = rep;
            fft1[j+1] = aim;

            fft1[nn2-j] = rep;
            fft1[nn3-j] = -aim;

            fft2[j] = aip;
            fft2[j+1] = -rem;

            fft2[nn2-j] = aip;
            fft2[nn3-j] = rem;

	}// end for

    }// end of realFFT2 



    //====================================================
    //====================================================
    //
    //  This method gets as input complex array f[0:n]
    //  and returns double array data[0:2*n + 1] as output.
    //
    //====================================================
    //====================================================

    void  complexArray2Real( Complex f[], double data[], int n) {

      
        int sizeComplex = f.length;
        int sizeDouble  = data.length;

  
        for (int i = 1; i <= n; i++) {
          

	    int index1 = 2*i - 1;
            int index2 = 2*i;

	     data[index1] = f[i-1].re();
	     data[index2] = f[i-1].im();

      	}
       

    }// end of ComplexToReal

    //=============================================================
    //=============================================================
    //
    //  Method: realArray2Complex(
    //
    //=============================================================
    //=============================================================

    void realArray2Complex( double data[], Complex f[], int n) {

        for ( int i = 1; i <=  n; i++ ) {
	  
	    f[i-1] = new Complex(data[2*i-1], data[2*i]);
	}  
	    
    }// end realArray2Complex


//====================================================================
//=====================================================================
// 
//     Method:  realFour  with vomplex argument 
//=====================================================================
//=====================================================================

    void complexFour( Complex f[], int nn, int isign) {
  
        double wr,wi,wpr,wpi,wtemp,theta,tempr,tempi;

        int  i,j,m, mmax,iSTEP;
    
        int N = 2*nn;

        AuxilliaryMethods func = new AuxilliaryMethods();

	//
        // Transfer f to data
	//

        double data[] = new double[2*nn+1];

	//=====================================
	//
	// f[0:127] data[1:256] i.e data[0] = 0 unused
	// data[1] = f[0].re
	// data[2] = f[0].im

	//
	//  data[255] = f[127].re
        //  data[256] = f[127].im
   
	//========================================
	 func.complexArray2Real(f,data, N/2);

	 
           
	//	func.complexArray2Real(f,data, N);
           
        
        j = 1;

       
        for ( i = 1; i <= N; i = i+2) {

	
	    if (j > i) {

       	          
	        tempr=data[j];
                tempi=data[j+1];

                data[j]=data[i];
	        data[j+1]=data[i+1];

	        data[i]=tempr;
	        data[i+1]=tempi;

	    } // end if


             m=N/2;
	     //===========================================	

point1:  
 
              while ((m >= 2) && (j > m)) {
	         j=j-m;
	         m=m/2;
	         continue point1;
              }

              j=j+m;

	}// for (int i = 1;


      mmax=2;

point2:    

     while (N > mmax) {
	 iSTEP=2*mmax;
         theta= 2.0*Math.PI/(isign*mmax);
	 wpr=-2.0*Math.sin(0.5*theta)*Math.sin(0.5*theta); 
	 wpi= Math.sin(theta);
	 wr=1.0;
	 wi=0.0;
         
	 //DO 13 m=1,mmax,2

	 for ( m = 1; m <= mmax; m= m+2) {

	    for (  i = m; i <= N; i = i+iSTEP ) {

		//          DO 12 i=m,N,iSTEP
		j=i+mmax;
	       	tempr=(wr)*data[j]- (wi)*data[j+1];
		tempi= (wr)*data[j+1]+ (wi)*data[j];
		data[j]=data[i]-tempr;
		data[j+1]=data[i+1]-tempi;
		data[i]=data[i]+tempr;
		data[i+1]=data[i+1]+tempi;
		
		//12        CONTiNUE
	      }

	    wtemp=wr;
            wr=wr*wpr-wi*wpi+wr;
	    wi=wi*wpr+wtemp*wpi+wi;
	 }

	//13    CONTiNUE

	 mmax=iSTEP;
	 continue point2;

     } // while ( N > mMAX

     func.realArray2Complex(data,f,nn);
     
	
  		 
    } // void FOUR Complex

    //=======================================================================
    //
    //  void complexFFT2( double data1[], double data2[],
    //                  Complex fft1[], Complex fft2[], int n )
    //
    //  It calls:
    //            void complexFour( Complex f[], int nn, int isign) 
    //   
    //  Input data1[0:n] and data2[0:n]
    //
    //  It returns FFT of fft1[0:n] of data1 & FFT of fft2[0:n] of data1
    // 
    //=====================================================================       


    void complexFFT2( double data1[], double data2[],
                      Complex fft1[], Complex fft2[], int n ) {

        int maxSize = 1024;

	Complex h1 = new Complex(0.0,0.0);
        Complex h2 = new Complex(0.0,0.0);
       
	Complex c1 = new Complex(0.5, 0.0);
        Complex c2 = new Complex(0.0, -0.5);

        AuxilliaryMethods func = new AuxilliaryMethods();

        FFT transform = new FFT();

	for (int i = 0; i < n; i++ ) {

            fft1[i] = new Complex(data1[i+1], data2[i+1]);
            fft2[i] = new Complex( 0.0, 0.0);
	}
	//=====================================================
	//
	// Call complexFour
	//
	//=====================================================


	func.complexFour( fft1,n,1);


        fft2[0] = new Complex( fft1[0].im(), 0.0); // 0 or 1 ?
        fft1[0] = new Complex( fft1[0].re(), 0.0);

    
        int n2 = n + 1;

        for ( int j = 1; j <= n/2; j++) {

	    // conjugate of fft1[n2-j-1]

	    int index = n2 - j - 1;

            Complex A = new Complex(fft1[index].re(), -fft1[index].im());

            Complex sum = fft1[j].plus(A);
            Complex diff = fft1[j].minus(A);

            h1 = c1.times(sum);
	    h2 = c2.times(diff);

            //-------------------------------------------------------------


            //fft1[j].re() = h1.re();
            //fft1[j].im() = h1.im();

            fft1[j] = new Complex( h1.re(), h1.im());
	    //
            //  fft1[index].re() = h1.re();
            //fft1[index].im() = -h1.im();

            fft1[index] = new Complex( h1.re(), -h1.im());

	    //------------------------------------------------------------

	    //fft2[j].re() = h2.re();
	    // fft2[j].im() = h2.im();

            fft2[j] = new Complex( h2.re(), h2.im());

	    // fft2[index].re() = h2.re();
	    // fft2[index].im() = -h2.im();
          
            fft2[index] = new Complex( h2.re(), -h2.im());

	    //-----------------------------------------------------------
     	}
	     
    } // end of complexFFT2(

    //=====================================================================



    //=====================================================================
    //
    //  Implementation of correl subroutine from NR.
    //
    //  This method requires: complexFFT2() and
    //                        complexFour()
    //
    //=====================================================================                     

    void correlComplex( double data1[], double data2[], int n, Complex ans[]) {
	
	int maxSize = 1024;
	//
	// Temporary complex array[maxSize]
	//
        Complex fftArray[] = new Complex[maxSize];
        FFT transform = new FFT();
        AuxilliaryMethods aux = new AuxilliaryMethods();

     
        //==================================================================
      
        complexFFT2( data1, data2, fftArray,ans,n);

        //==================================================================

  

        double no2 = (double)n/2.0;

        double inverseOfno2 = 1.0/no2;


	for ( int i = 0; i <= n/2; i++ ) {
            
            Complex a = new Complex( ans[i].re(), -ans[i].im());
            ans[i] = fftArray[i].times( a );
            ans[i] = ans[i].times( inverseOfno2 );

	}

        //
	// Transfer complex ans[] to real data[]
	//

        double data[] = new double[2*n+1];

        complexArray2Real(ans,data,n);

        aux.realFT(data,n,-1);

        realArray2Complex( data,ans,n);

      
            
    }// end correlComplex(

    //=======================================================================
    // 
    //  void crossCorreltionF(
    //
    //=======================================================================

    void crossCorrelationFFT(double f[], double g[], int n,int size,double dt){

 
        Complex ans[] = new Complex[size];
	double   c[] = new double[size];
     
	double sum0 = 0.0;
	double temp = 0.0;

	int n2 = 0;
	int n2over2 = 0;

	// Compute the zero lag autocorrelation of g[]\

        for (int i = 0; i <= n; i++) {
	    sum0 = sum0 + g[i]*g[i];
	}

        sum0 = sum0 * dt;

	// System.out.println(" sum0 = " + sum0 );
        

	//
	// Compute the next power of 2 > n
	//

	n2 = 1;
	while ( n2 < n ) {
	    n2 = n2*2;
	}

       
	//=========================================================
          
        correlComplex(f, g, n2,ans);

	//=========================================================
        // transfer ans to real array
	//    This is offending    complexArray2Real( ans, c, n2);
	//

        //========================================================


        for( int i = 1; i <= n2/2; i++) {

	    int i1 = 2*i - 1;
            int i2 = 2*i;
          

	     c[i1] = ans[i-1].re();
             c[i2] = ans[i-1].im();

	}

        temp = dt/sum0;



	for ( int i = 0; i <= n2; i++) {

	    g[i] = c[i]*temp;
	}

      
    } // crossCorrelationFFT
        

    //================================================================
    //
    // void gFilter(
    //
    //================================================================

    void gFilter( double x[], double gWidthIn, int n, double dt ) {

	
	double gauss, dOmega, omega, gwidth,df;
	double sum0 = 0.0;
        
        double Pi = Math.PI;
	double twoPi = 2.0*Pi;
      
          
        int n2,halfpts;

        int forward = 1;
	int inverse = -1;

        AuxilliaryMethods aux = new AuxilliaryMethods();

        n2 = 1;

        while ( n2 < n) {
	    n2 = n2*2;
	}

	halfpts = n2/2;

        df = 1.0/( (double)n2 *dt );

        double scale = dt*(2.0*df);


        // call FFT

	//=============================================================
	//
	// In this method the realFT() has to have n2 points no halfpts
	//
	//      HOW STUPID IS FORTRAN
	//
	//==============================================================
	//
	//==============================================================

	aux.realFT( x, n2, forward);

	//==============================================================

        
	dOmega = twoPi*df;

	gwidth = 4.0*gWidthIn*gWidthIn;

	omega = twoPi /(2.0*dt) ;
        
        gauss = Math.exp(-omega*omega/gwidth);

        x[2] = x[2]*gauss;

        //======================================================

        int j = 0;

        for ( int i = 2; i <= halfpts; i++) {
	    
	    j = i * 2;

	    omega = (i-1)*dOmega;
    	    
	    gauss = Math.exp(-omega*omega/gwidth);
 	    
	    x[j-1] = x[j-1]*gauss;
	    x[j  ] = x[j  ]*gauss;

	}

	//
	// call inverse FFT
	//

        aux.realFT( x, n2, inverse );
	
	//
 	// Scale
	//

	for ( int i = 1; i <= n; i++) {

	    x[i] = x[i]*scale;
	}

    } //end gFilter(

    //=======================================================================
    //
    // void zero( x,n)
    //
    //=======================================================================

    void zero(double x[], int n) {

	for ( int i = 0; i < n; i++ ) {
	    x[i] = 0.0;
	}
    } // end zero(

    //=======================================================================
    //
    // void getMax(
    //
    //======================================================================

    void getMax(double x[], int n, double maxValue, int maxIndex ) {

	maxValue = x[1];
	maxIndex = 1;

	for (int i =2; i <=n; i++ ){
	    
	    if ( x[i] > maxValue ) {
		maxValue = x[i];
		maxIndex = i;
	    }

	}

    } //end getMax

    //======================================================
    //======================================================

     double getMaxValue(double x[], int n ) {

	double maxValue = x[1];
 	int maxIndex = 1;

	for (int i =2; i <=n; i++ ){
	    
	    if ( x[i] > maxValue ) {
		maxValue = x[i];
		maxIndex = i;
	    }

	}
      
        return maxValue;
    
    } //end getMaxValue

    //=====================================================
    //====================================================

     int getMaxIndex(double x[], int n ) {

	double maxValue = x[1];
 	int maxIndex = 1;

	for (int i =2; i <=n; i++ ){
	    
	    if ( x[i] > maxValue ) {
		maxValue = x[i];
		maxIndex = i;
	    }

	}
      
        return maxIndex;
    
    } //end getMaxIndex

    //======================================================================
    //
    // void getAbsMax( 
    //
    //======================================================================

    void getAbsMax( double x[], int n, double theValue, int maxIndex ) {

     
	double maxValue = Math.abs(x[1]);
	maxIndex = 1;

	theValue = x[1];

	for ( int i = 2; i <=n; i++ ) {

	    if ( Math.abs(x[i]) > maxValue ) {
		maxValue = Math.abs(x[i]);
		theValue = x[i];
		maxIndex = i;
	    }

	}
  

    }// end getAbsMax(....

    double getAbsMaxValue( double x[], int n ) {

   
	double maxValue = Math.abs(x[1]);
   
	int maxIndex = 1;

	double theValue = x[1];

	for ( int i = 2; i <=n; i++ ) {

	    if ( Math.abs(x[i]) > maxValue ) {
		maxValue = Math.abs(x[i]);
		theValue = x[i];
		maxIndex = i;
	    }

	}
          
        return theValue; 

    }// end getAbsMax(....

    //*******************************************************************

        int getAbsMaxIndex( double x[], int n ) {

       

	double maxValue = Math.abs(x[1]);

	int maxIndex = 1;

	double theValue = x[1];

	for ( int i = 2; i <=n; i++ ) {

	    if ( Math.abs(x[i]) > maxValue ) {
		maxValue = Math.abs(x[i]);
		theValue = x[i];
		maxIndex = i;
	    }

	}
        
        return maxIndex; 

    }// end getAbsMaxIndex(....

    

    //=====================================================================
    //
    //   void getRes( ....
    //
    //=====================================================================

    void getRes( double x[], double y[], double r[], int n, double sumsQ ) {

	sumsQ = 0.0;
	
	for ( int i = 1; i <= n; i++ ) {

	    r[i] = x[i] - y[i];
	    sumsQ = sumsQ + r[i]*r[i];
	}

    }// end getRes





//**************************************************************************


    void complexFT(Complex array[], int n, int isign) { 

	double WR,WI,WPR,WPI,WTEMP,theta;
        double C1,C2;
        double H1R,H2R,H1I,H2I;
        double WRS,WIS;
        int I1,I2,I3,I4;
  
	theta=Math.PI/(double)(n/2);

        
	//  Transfer complex array[] to double data[]



        double data[] = new double[2*n+1];        

	complexArray2Real(array, data,n);

       


	  C1=0.5;
          
          if (isign == 1) {
	      C2=-0.5;

	      //  realFour(data,2*n,1); does not work

	      realFour( data,n,1);

	  } else {

	      C2=0.5;
              theta=-theta;
	  }

          WTEMP = Math.sin(0.5*theta);

	  WPR=-2.0*WTEMP*WTEMP;
  
          WPI=Math.sin(theta);
          WR=1.0 + WPR;
          WI=WPI;
          int N2P3=n+3;

    
	  //     DO 11 I=2,N/2+1

	   for( int i = 2; i <= n/4; i++ ) {
          //    for( int i = 2; i <= n/2+1; i++ ) {

	      I1=2*i - 1;

	      I2=I1+1;
	      I3=N2P3-I2;
	      I4=I3+1;
        
	      //     WRS=SNGL(WR);
	      //     WIS=SNGL(WI)

	      WRS = WR;
              WIS = WI;

	     
              H1R=C1*(data[I1]+data[I3]);

              H1I=C1*(data[I2]-data[I4]);

	      H2R=-C2*(data[I2]+data[I4]);

	      H2I=C2*(data[I1]-data[I3]);

	      data[I1]=H1R+WRS*H2R-WIS*H2I;


              data[I2]=H1I+WRS*H2I+WIS*H2R;

	      data[I3]=H1R-WRS*H2R+WIS*H2I;

	      data[I4]=-H1I+WRS*H2I+WIS*H2R;

	      WTEMP=WR;

	      WR=WR*WPR-WI*WPI+WR;

	      WI=WI*WPR+WTEMP*WPI+WI;
	     

	  } //for

	  //11    CONTINUE

	     
  
	  if(isign == 1) {

	      H1R=data[1];
	      data[1]=H1R+data[2];
	      data[2]=H1R-data[2];

	  } else {
     
	  H1R=data[1];
	  data[1]=C1*(H1R+data[2]);
          data[2]=C1*(H1R-data[2]);

	  //
	  // Start here
	  //
	  
              
	  //realFour( data,n/2, -1);
           
	  realFour( data,n, -1);

	  }
      
	  //
	  // Transfer real data[] to complex array[]
	  //

          realArray2Complex(data,array, n);

   } // end complexFT

    //===========================================================
    //===========================================================
    //===========================================================

    void buildDeconvolution(double amps[], int shifts[], double p[],
                            int nshifts, int n, double gwidth, double dt )
    {

        zero(p,n);
        
        for ( int i = 1; i <= nshifts; i++) {

	    p[shifts[i]] = p[shifts[i]] + amps[i];
        }			    

       gFilter(p,gwidth,n,dt );

    }

    //===========================================================


    void convolveReal( double x[], double y[], int n, double dt ) {

	double scale0 = 0.0;
	double scale1 = 0.0;

        double a = 0.0;
        double b = 0.0;
        double c = 0.0;
        double d = 0.0;

        int forward = 1;
	int inverse = -1;

        double df = 0.0;

        int j  = 0;
        int n2 = 0;
        int halfpts = 0;
	//
	// Check if n is even
	//
        if ( (n % 2) != 0 ) {
	    System.out.println(" Number of points n in convolveReal is odd");
	}

        n2 = n;
        halfpts = n2/2;

	//   realFT( x, halfpts/2, forward );
	//   realFT( y, halfpts/2, forward );

        // realFT( x, halfpts, forward );
        // realFT( y, halfpts, forward ); it didnt work


        realFT( x,n2, forward );
        realFT( y,n2, forward );

        df = 1.0/((double)n2 * dt);
      
 
        // Handle the zero & Nyquist frequency

	x[1] = x[1]*y[1];
        x[2] = x[2]*y[2];

        for ( int i = 2; i <= halfpts; i++ ) {

	    j = i*2;
         
            a = x[j-1];
            b = x[j];

            c = y[j-1];
            d = y[j];

            x[j-1] = a*c - b*d;
            x[j]   = a*d - b*c;

            //x[j]   = a*d - b*c;
	}

 
	 // realFT( x, halfpts, inverse );
	 // realFT( y, halfpts, inverse );

        realFT( x, n2, inverse );
        realFT( y, n2, inverse );

	scale0 = dt*(2.0*df);
        scale1 = dt*scale0;

        for ( int i = 1; i <= n; i++ ) {

	    y[i] = y[i]*scale0;
            x[i] = x[i]*scale1;
	}
      
  

    }// end of convolveReal

    double getResiduum( double x[], double y[],double r[], int n) {

	double sumsq = 0.0;

        for ( int i = 1; i <= n; i++) {

	    r[i] = x[i] - y[i];

	    sumsq = sumsq + r[i]*r[i];
	}

	return sumsq;
    
    } // end of getResiduum

    
    void convolveRealLoop( double x[], double y[], int n, double dt ) {

	double scale0 = 0.0;
	double scale1 = 0.0;

        double a = 0.0;
        double b = 0.0;
        double c = 0.0;
        double d = 0.0;

        int forward = 1;
	int inverse = -1;

        double df = 0.0;

        int j  = 0;
        int n2 = 0;
        int halfpts = 0;
	//
	// Check if n is even
	//
        if ( (n % 2) != 0 ) {
	    System.out.println(" Number of points n in convolveReal is odd");
	}

        n2 = n;
        halfpts = n2/2;

	//   realFT( x, halfpts/2, forward );
	//   realFT( y, halfpts/2, forward );

        // realFT( x, halfpts, forward );
        // realFT( y, halfpts, forward ); it didnt work


        realFT( x,n2, forward );
        realFT( y,n2, forward );



        df = 1.0/((double)n2 * dt);

        
        // Handle the zero & Nyquist frequency

	x[1] = x[1]*y[1];
        x[2] = x[2]*y[2];

        for ( int i = 2; i <= halfpts; i++ ) {

	    j = i*2;
         
            a = x[j-1];
            b = x[j];

            c = y[j-1];
            d = y[j];

            x[j-1] = a*c - b*d;
            x[j]   = a*d + b*c;
	}

 
	 // realFT( x, halfpts, inverse );
	 // realFT( y, halfpts, inverse );

        realFT( x, n2, inverse );
        realFT( y, n2, inverse );


	scale0 = dt*(2.0*df);
        scale1 = dt*scale0;

        for ( int i = 1; i <= n; i++ ) {

	    y[i] = y[i]*scale0;
            x[i] = x[i]*scale1;
	}
      
  

    }// end of convolveRealLoop

    void phaseShift(double x[], double theShift, int n, double dt ) {

        double PI = Math.PI;
        double twoPI = 2.0*PI;

        double dOmega;
        double omega;
        double df;
        double a,b,c,d;

        int j, n2, halfpts;

        int forward =  1;
        int inverse = -1;
       
        //------------------------------------------
       
        n2 = 1;

        while ( n2 < n) {
	    n2 = n2*2;
	}

	halfpts = n2/2;
           
        //realFT( x, halfpts, forward );

	realFT( x, n2 ,forward );

      
        df = 1.0/((double)n2 * dt );
    
        dOmega = twoPI*df;

	//
	// Nyquist
	//
	
	omega = twoPI/(2.0*dt);
     
        x[2] = x[2]*Math.cos(omega*theShift);
     
        for (int i = 2; i <= halfpts; i++ ) {

	    j = i*2;
	    omega = (i-1) * dOmega;
	    a = x[j-1];
	    b = x[j];
	    c = Math.cos(omega*theShift);
            d = Math.sin(omega*theShift);

            x[j-1] = a*c - b*d;
            x[j] =   a*d + b*c;
	}

	//===============================================


        realFT( x, n2, inverse);

        double scalefactor = dt*(2.0*df);


        for (int i = 1; i <= n; i++ ) {
	    x[i] = x[i]*scalefactor;
	}

    } // end of phaseShift
    //
    //
    //=====================================================================
    //
    //
    //
     
    void readSacFile( String fileName, int numberPtsRead, float array[] ) {


	SacTimeSeriesX sac = new SacTimeSeriesX();


        try {

	    sac.read(fileName);
            
	             
	    for ( int i = 0; i < numberPtsRead; i++) {

		array[i] = sac.y[i];

	    }

	} catch ( FileNotFoundException e ) {

	    System.out.println("File: " + fileName + " does not exists!");
    
            System.exit(0);

	} catch (IOException e ) {

	    System.out.println("IOException: " + e.getMessage());
	    e.printStackTrace();
	}

    } // readSacFile()

    int getNumberOfPointsFromSACFile( String fileName ) {

       	SacTimeSeriesX sac = new SacTimeSeriesX();
  
        int numberPtsRead  = 0;

        try {

	    sac.read(fileName);
            
	    numberPtsRead = sac.y.length;
       	    

	} catch ( FileNotFoundException e ) {

	    System.out.println("File: " + fileName + " does not exists!");

            System.exit(0);

	} catch (IOException e ) {

	    System.out.println("IOException: " + e.getMessage());
	    e.printStackTrace();
	}
	
	
	return numberPtsRead;

    } // getNumberPts


    //===================================================

     
    float getBTimeFromSACFile( String fileName ) {

       	SacTimeSeriesX sac = new SacTimeSeriesX();
  
        float bTime  = 0.0f;

        try {

	    sac.read(fileName);
            
       	    bTime = sac.b;

	} catch ( FileNotFoundException e ) {

	    System.out.println("File: " + fileName + " does not exists!");
            System.exit(0);

	} catch (IOException e ) {

	    System.out.println("IOException: " + e.getMessage());
	    e.printStackTrace();
	}
	
	
	return bTime;

    } 


   float getETimeFromSACFile( String fileName ) {

       	SacTimeSeriesX sac = new SacTimeSeriesX();
  
        float eTime  = 0.0f;

        try {

	    sac.read(fileName);
            
       	    eTime = sac.e;

	} catch ( FileNotFoundException e ) {

	    System.out.println("File: " + fileName + " does not exists!");
            System.exit(0);

	} catch (IOException e ) {

	    System.out.println("IOException: " + e.getMessage());
	    e.printStackTrace();
	}
	
	
	return eTime;

    } // getETime

 float getSpacingFromSACFile( String fileName ) {

       	SacTimeSeriesX sac = new SacTimeSeriesX();
  
        float spacing  = 0.0f;

        try {

	    sac.read(fileName);
            
       	    spacing = sac.delta;

	} catch ( FileNotFoundException e ) {

	    System.out.println("File: " + fileName + " does not exists!");
            System.exit(0);

	} catch (IOException e ) {

	    System.out.println("IOException: " + e.getMessage());
	    e.printStackTrace();
	}
	
	
	return spacing;

    } // spacing

    // 
    // Write to SAC File
    //

     void writeSacFile( String fileName, int numberOfPoints, double array[],
		        float spacing, float begining, float ending ) {

	SacTimeSeriesX sac = new SacTimeSeriesX();

          try {

            sac.npts = numberOfPoints;
            sac.delta = spacing;
	    sac.b = begining;
            sac.e = ending;

            sac.y = new float[numberOfPoints];

	    for ( int i = 1; i <=  numberOfPoints; i++) {

	       	sac.y[i-1] =  (float)array[i]; // Notice: (i)

		//sac.y[i] = array[i];
		
	     }

	    sac.write( fileName);

        } catch ( FileNotFoundException e ) {
	    System.out.println("File: " + fileName + " does not exists!");
	} catch ( IOException e ) {
	    System.out.println(" IOException: " + e.getMessage());
	    e.printStackTrace();
	}

	
	} //writeSacFile

    // ******************************************************************
 void writeSacFile( String fileName, int numberOfPoints, double array[],
		    float spacing, float begining, float ending, float user0,
		    int itype, int leven, int lpspol, int lovrok, int lcalda,
		    int unused27) {

	SacTimeSeriesX sac = new SacTimeSeriesX();

          try {

            sac.npts = numberOfPoints;
            sac.delta = spacing;
	    sac.b = begining;
            sac.e = ending;
            
            sac.iztype   = itype;
	    sac.leven    = leven;
	    sac.lpspol   = lpspol;
	    sac.lcalda   = lcalda;
	    sac.unused27 = unused27;
  

            sac.y = new float[numberOfPoints];

	    for ( int i = 1; i <=  numberOfPoints; i++) {

	       	sac.y[i-1] =  (float)array[i]; // Notice: (i)

		//sac.y[i] = array[i];
		
	     }

	    sac.write( fileName);

        } catch ( FileNotFoundException e ) {
	    System.out.println("File: " + fileName + " does not exists!");
	} catch ( IOException e ) {
	    System.out.println(" IOException: " + e.getMessage());
	    e.printStackTrace();
	}

	
	} //writeSacFile
       

    //==================================================

   double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    long factor = (long) Math.pow(10, places);
    value = value * factor;
    long tmp = Math.round(value);
    return (double) tmp / factor;
}

 }// end of AuxilliaryMethods class

