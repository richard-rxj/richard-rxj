/* Program started on May 16, 2007
 
ZDF to SAC Converter
Created By Kunnal Khiatani as a Research Assistant for Dr.Hrvoje Tkalcic at the RSES, Australian National University
 
Last Edited: June 18, 2007
Days worked on: May 16, 18, 22, June 12, 13, 15, 18
 
Status: Functional. Correctly reads zdf files and creates sac output files.
Records Station information from a separate station input file.
 
Notes:
Functional for both Big-Endian Machines and Little-Endian Machines.

Usage:
zdf2sac_new <zdfFile> <stationFile>

(1) zdf2sac_new is the executable
(2) the zdf file to be converted to sac files
(3) the station file that holds information about the latitudes, longitudes and elevations of the stations [Optional]

*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define LITTLE_ENDIAN 	0
#define BIG_ENDIAN 	1

/*Definition Block as taken from ZDF's Format Definition*/

#define      NPTS   30000
#define      NSTA   100

int      irc;             /* Header length (180)     */
char     bla[80];         /* Text                    */
int      oyr;             /* Origin time: year       */
int      omon;            /* Origin time: month      */
int      oday;            /* Origin time: day        */
int      ohr;             /* Origin time: hour       */
int      omin;            /* Origin time: minute     */
float    osec;            /* Origin time: second     */
float    lat;             /* Hypocentre: latitude    */
float    lon;             /* Hypocentre: longitude   */
float    dep;             /* Hypocentre: depth       */
float    mag;             /* Hypocentre: magnitude   */
int      bday;            /* Begin rec.: day         */
int      bhr;             /* Begin rec.: hour        */
int      bmin;            /* Begin rec.: minute      */
float    bsec;            /* Begin rec.: second      */
int      eday;            /* End record: day         */
int      ehr;             /* End record: hour        */
int      emin;            /* End record: minute      */
float    esec;            /* End record: second      */
int      nsta;            /* Number of stations      */
int      ndgkm;           /* range deg(0), km(1)     */ 
int      nrft;            /* time rel to origin(0) or event start(1) */
int      nrot;            /* ZNE(0),ZRT(1),other(2)  */
int      nsmpl;           /* Nominal sampling rate (per sec) */

		/*Station Header: */
		//struct sthead {
		int      irs;             /* Length of station data  */
                char     name[5];         /* Name of station         */
                int      sday;            /* Station begin: day      */
                int      shr;             /* Station begin: hour     */
                int      smin;            /* Station begin: minute   */
                float    ssec;            /* Station begin: second   */
                float    tcrr;            /* Time correction         */
                float    delta;           /* Range (degrees or km)   */
                float    azim;            /* Azimuth to source       */
                float    sampin;          /* Sample interval         */
                int	 ncomp;           /* no of components (le 5) */
                //};//} //sthd[NSTA];
		/*End of Station Header */

//#define    SSIZE  44
		
		/* Component Header: */
		//struct cmphed {
		char     compid[4];       /* Component identifier    */
                int      ndat;            /* Number of data points   */
                float    pred;            /* Reduction slowness      */
                float    tsrt;            /* Starting reduced time   */
                float    amp;             /* Maximum amplitude  */
                //}; //cmphed[NSTA][5];
		/*End of Component Header */

//#define    CSIZE  20

int             junk;
float           rdat; /*Raw Data goes here*/
float           fsabs();

/*End of ZDF Definition Block*/


/*Extra ZDF Defintions...Not mentioned above.*/

char projct[4];		/* Project Name	                */	
char pyr[4];		/* Project Year 		*/
char dtype[4];		/* Data Type			*/
char cevent[64];	/* Event Identifier		*/
char chyp[4];		/* Source of Hypocenter Info	*/
char cend[5];		/* End of Header marker 	*/

/*End of Extra Definitions Block*/


/*SAC Definition Block - Defined in the Byte-order of the SAC Format*/
/*NOTE: NPTS Defined in SAC is now called SACNPTS*/
//0
float	DELTA;
float	DEPMIN;
float	DEPMAX;
float	SCALE;
float 	ODELTA;

//5
float	B;
float	E;
float	O;
float 	A;
float	INTERNALF;

//10
float	T0;
float	T1;
float	T2;
float	T3;
float	T4;

//15
float	T5;
float	T6;
float	T7;
float	T8;
float	T9;

//20
float	F;
float	RESP0;
float	RESP1;
float	RESP2;
float	RESP3;

//25
float	RESP4;
float	RESP5;
float	RESP6;
float	RESP7;
float	RESP8;

//30
float	RESP9;
float	STLA;
float	STLO;
float	STEL;
float	STDP;

//35
float	EVLA;
float	EVLO;
float	EVEL;
float	EVDP;
float	MAG;

//40
float	USER0;
float	USER1;
float	USER2;
float	USER3;
float	USER4;

//45
float	USER5;
float	USER6;
float	USER7;
float	USER8;
float	USER9;

//50
float	DIST;
float	AZ;
float	BAZ;
float	GCARC;
//	INTERNAL; //Already Defined in 0

//55
//	INTERNAL; //Already Defined in 0
float	DEPMEN;
float	CMPAZ;
float	CMPINC;
float	XMINIMUM;	

//60
float	XMAXIMUM;
float	YMINIMUM;
float	YMAXIMUM;
float	UNUSEDF; 
//	UNUSED; //Already Defined

//65
//	UNUSED
//	UNUSED
//	UNUSED
//	UNUSED
//	UNUSED

//70
int	NZYEAR;
int	NZJDAY;
int	NZHOUR;
int	NZMIN;
int	NZSEC;

//75
int	NZMSEC;
int	NVHDR;
int	NORID;
int	NEVID;
int	SACNPTS; //NOTE: NPTS Variable ALready defined for ZDF. This variable has thus been renamed to show the difference between the two.

//80
int	INTERNALI; //ALREADY DEFINED AS FLOAT.
int	NWFID;
int	NXSIZE;
int	NYSIZE;
int	UNUSEDI;

//85
int	IFTYPE;
int	IDEP;
int	IZTYPE;
//	UNUSED
int 	IINST;

//90
int	ISTREG;
int	IEVREG;
int	IEVTYP;
int	IQUAL;
int	ISYNTH;

//95
int	IMAGTYP;
int	IMAGSRC;
//	UNUSED
//	UNUSED
//	UNUSED

//100
//	UNUSED
//	UNUSED
//	UNUSED
//	UNUSED
//	UNUSED

//105
int	LEVEN;
int 	LPSPOL;
int	LOVROK;
int	LCALDA;
//	UNUSED

//110
char	KSTNM[8];
char	KEVNM[16];

//116
char	KHOLE[8];
char	KO[8];
char	KA[8];

//122
char	KT0[8];
char	KT1[8];
char	KT2[8];

//128
char	KT3[8];
char	KT4[8];
char	KT5[8];

//134
char	KT6[8];
char	KT7[8];
char	KT8[8];

//140
char	KT9[8];
char	KF[8];
char	KUSER0[8];

//146
char	KUSER1[8];
char	KUSER2[8];
char	KCMPNM[8];

//152
char	KNETWK[8];
char	KDATRD[8];
char	KINST[8];


// variable undefine class
#define  UNDEFINE_F      -12345.0
#define  UNDEFINE_N      -12345
#define  UNDEFINE_I      -12345
#define  UNDEFINE_L       0       // logical false
#define  UNDEFINE_K      "-12345"

/*End of SAC Definition Block*/

/*Variable Definitions*/
int a=0, j=0, k=0;
int exitFlag = 0; /* Representation of Boolean Variable for Exiting the Program */
int byteSwapFlag = 0; /* Representation of Boolean Variable for Byte Swapping */
//char machineFamilyType;/* Used for the appropriate Machine type */
int endian = 1; /* Set to Big Endian by default */
char sacFileName[100];
char asciiFileName[100];
int temp=0; 

/*End of Variable Definitions*/

/*Function Definitions*/
int endian_check();
void swap_bytes(char *buffer,int samples,int size);
void initialize_sac_header();
void set_char_undefine(char *stringVar);

/*End of Function Defintions*/


/*Start of Main Program*/
int main(int argc, char* argv[])
{
  char buffer[100];
  int  *ptint;
  float *ptfloat;
  
  /*Checking for Correct Input Parameters*/
  if (argc<2)
    {
      printf("Insufficient Parameters. Exiting...\n");
      exitFlag = 1;
    }
  else if (argc>3)
    {
      printf("Too Many Parameters. Exiting...\n");
      exitFlag = 1;
    }
  
  while(exitFlag!=1)
    {
      /*Main body*/
      /*File Opening*/
      FILE *zdfInput, *sacOutput, *stationFile, *asciiOutput;
      zdfInput = fopen(argv[1], "rb"); //Contains the ZDF File
      stationFile = fopen(argv[2],"r"); //Contains the Station File with Station Latitudes and Longitudes
      
      if(zdfInput == NULL)
	{
	  printf("Empty ZDF File Found.\n");
	}
      
      else
	{	
	  /*Determines whether the machine is little endian or big endian and sets the byteSwapFlag accordingly*/
	  if(stationFile == NULL)
	    {
	      printf("Empty/No Station File Found. Sac files will be generated without station information. \n");
	    }
	  
	  fclose(stationFile);
	  endian = endian_check();
	  if (endian == 0)
	    {
	      //printf("Little endian architecture found..\n");
	      byteSwapFlag = 1;
	    }
	  else if (endian == 1)
	    {
	      //printf("Big endian architecture found..\n");
	      byteSwapFlag = 0;
	    }
	  
	  
	  
	  /*Reading ZDF Header Information*/
	  //printf("Reading ZDF Header...\n");
	  //fread(&irc, sizeof(int),1,zdfInput);
	  fread(buffer,sizeof(char),4,zdfInput);
	  if(byteSwapFlag == 1)
	    {
	      swap_bytes(buffer,1,4);
	    }
	  ptint = (int *)buffer;
	  //printf("%d\n",ptint[0]);
	  fread(projct, sizeof(char),4,zdfInput);
	  char ProjectName[100];
	  sscanf(projct, "%s", ProjectName);
	  //printf("%s\n",ProjectName);
	  fread(pyr, sizeof(char),4,zdfInput);
	  //printf("%s\n",pyr);
	  fread(dtype, sizeof(char),4,zdfInput);
	  //printf("%s\n",dtype);
	  fread(cevent, sizeof(char),64,zdfInput);
	  //printf("%s\n",cevent);
	  fread(chyp, sizeof(char),4,zdfInput);
	  //printf("%s\n",chyp);
	  
	  /*Origin Year, Month, Day, Hour, Min, Sec follows*/		
	  //printf("Origin Year, Month, Day, Hour, Min, Sec\n");
	  
	  //fread(&oyr, sizeof(int),1,zdfInput);
	  fread(buffer, sizeof(char),4,zdfInput);
	  if(byteSwapFlag == 1)
	    {
	      swap_bytes(buffer,1,4);
	    }
	  ptint =(int*)buffer;
	  oyr = ptint[0];
	  //printf("%d\n",oyr);
	  
	  //fread(&omon, sizeof(int),1,zdfInput);
	  fread(buffer, sizeof(char),4,zdfInput);
	  if(byteSwapFlag == 1)
	    {
	      swap_bytes(buffer,1,4);
	    }
	  ptint =(int*)buffer;
	  omon = ptint[0];
	  //printf("%d\n",omon);
	  
	  //fread(&oday, sizeof(int),1,zdfInput);
	  fread(buffer, sizeof(char),4,zdfInput);
	  if(byteSwapFlag == 1)
	    {
	      swap_bytes(buffer,1,4);
	    }
	  ptint =(int*)buffer;
	  oday = ptint[0];
	  //printf("%d\n",oday);
	  
	  //fread(&ohr, sizeof(int),1,zdfInput);
	  fread(buffer, sizeof(char),4,zdfInput);
	  if(byteSwapFlag == 1)
	    {
	      swap_bytes(buffer,1,4);
	    }
	  ptint =(int*)buffer;
	  ohr = ptint[0];	
	  //printf("%d\n",ohr);
	  
	  //fread(&omin, sizeof(int),1,zdfInput);
	  fread(buffer, sizeof(char),4,zdfInput);
	  if(byteSwapFlag == 1)
	    {
	      swap_bytes(buffer,1,4);
	    }
	  ptint =(int*)buffer;
	  omin = ptint[0];
	  //printf("%d\n",omin);
	  
	  //fread(&osec, sizeof(float),1,zdfInput);
	  fread(buffer, sizeof(char),4,zdfInput);
	  if(byteSwapFlag == 1)
	    {
	      swap_bytes(buffer,1,4);
	    }
	  ptfloat =(float*)buffer;
	  osec = ptfloat[0];	
	  //printf("%f\n",osec);
	  
	  /*Hypocenter Latitude, Longtitude, Depth, Magnitude follows*/
	  //printf("Hypocenter Latitude, Longtitude, Depth, Magnitude\n");		
	  
	  //fread(&lat, sizeof(float),1,zdfInput);
	  fread(buffer, sizeof(char),4,zdfInput);
	  if(byteSwapFlag == 1)
	    {
	      swap_bytes(buffer,1,4);
	    }
	  ptfloat =(float*)buffer;
	  lat = ptfloat[0];		
	  //printf("%f\n",lat);
	  
	  //fread(&lon, sizeof(float),1,zdfInput);
	  fread(buffer, sizeof(char),4,zdfInput);
	  if(byteSwapFlag == 1)
	    {
	      swap_bytes(buffer,1,4);
	    }
	  ptfloat =(float*)buffer;
	  lon = ptfloat[0];
	  //printf("%f\n",lon);
	  
	  //fread(&dep, sizeof(float),1,zdfInput);
	  fread(buffer, sizeof(char),4,zdfInput);
	  if(byteSwapFlag == 1)
	    {
	      swap_bytes(buffer,1,4);
	    }
	  ptfloat =(float*)buffer;
	  dep = ptfloat[0];
	  //printf("%f\n",dep);
	  
	  //fread(&mag, sizeof(float),1,zdfInput);
	  fread(buffer, sizeof(char),4,zdfInput);
	  if(byteSwapFlag == 1)
	    {
	      swap_bytes(buffer,1,4);
	    }
	  ptfloat =(float*)buffer;
	  mag = ptfloat[0];
	  //printf("%f\n",mag);
	  
	  
	  /*Begin Day, Hour, Min, Sec follows*/
	  //printf("Event Start Day, Hour, Min, Sec \n");
	  
	  //fread(&bday, sizeof(int),1,zdfInput);			
	  fread(buffer, sizeof(char),4,zdfInput);
	  if(byteSwapFlag == 1)
	    {
	      swap_bytes(buffer,1,4);
	    }
	  ptint =(int*)buffer;
	  bday = ptint[0];
	  //printf("%d\n",bday);
	  
	  //fread(&bhr, sizeof(int),1,zdfInput);
	  fread(buffer, sizeof(char),4,zdfInput);
	  if(byteSwapFlag == 1)
	    {
	      swap_bytes(buffer,1,4);
	    }
	  ptint =(int*)buffer;
	  bhr = ptint[0];
	  //printf("%d\n",bhr);
	  
	  //fread(&bmin, sizeof(int),1,zdfInput);
	  fread(buffer, sizeof(char),4,zdfInput);
	  if(byteSwapFlag == 1)
	    {
	      swap_bytes(buffer,1,4);
	    }
	  ptint =(int*)buffer;
	  bmin = ptint[0];
	  //printf("%d\n",bmin);
	  
	  //fread(&bsec, sizeof(float),1,zdfInput);
	  fread(buffer, sizeof(char),4,zdfInput);
	  if(byteSwapFlag == 1)
	    {
	      swap_bytes(buffer,1,4);
	    }
	  ptfloat =(float*)buffer;
	  bsec = ptfloat[0];
	  //printf("%f\n",bsec);
	  
	  /*End Day, Hour, Min, Sec follows*/
	  //printf("Event Stop Day, Hour, Min, Sec \n");
	  
	  //fread(&eday, sizeof(int),1,zdfInput);
	  fread(buffer, sizeof(char),4,zdfInput);
	  if(byteSwapFlag == 1)
	    {
	      swap_bytes(buffer,1,4);
	    }
	  ptint =(int*)buffer;
	  eday = ptint[0];
	  //printf("%d\n",eday);
	  
	  //fread(&ehr, sizeof(int),1,zdfInput);
	  fread(buffer, sizeof(char),4,zdfInput);
	  if(byteSwapFlag == 1)
	    {
	      swap_bytes(buffer,1,4);
	    }
	  ptint =(int*)buffer;
	  ehr = ptint[0];
	  //printf("%d\n",ehr);
	  
	  //fread(&emin, sizeof(int),1,zdfInput);
	  fread(buffer, sizeof(char),4,zdfInput);
	  if(byteSwapFlag == 1)
	    {
	      swap_bytes(buffer,1,4);
	    }
	  ptint =(int*)buffer;
	  bmin = ptint[0];
	  //printf("%d\n",emin);
	  
	  //fread(&esec, sizeof(float),1,zdfInput);
	  fread(buffer, sizeof(char),4,zdfInput);
	  if(byteSwapFlag == 1)
	    {
	      swap_bytes(buffer,1,4);
	    }
	  ptfloat =(float*)buffer;
	  esec = ptfloat[0];
	  //printf("%f\n",esec);
	  
	  /*Number of stations, range, time rel to origin or event start, ZNE/ZRT/Other, Nominal sample rate (per sec) */
	  
	  //fread(&nsta, sizeof(int),1,zdfInput);
	  fread(buffer, sizeof(char),4,zdfInput);
	  if(byteSwapFlag == 1)
	    {
	      swap_bytes(buffer,1,4);
	    }
	  ptint =(int*)buffer;
	  nsta = ptint[0];
	  //printf("%d\n",nsta);
	  
	  //fread(&ndgkm, sizeof(int),1,zdfInput);
	  fread(buffer, sizeof(char),4,zdfInput);
	  if(byteSwapFlag == 1)
	    {
	      swap_bytes(buffer,1,4);
	    }
	  ptint =(int*)buffer;
	  ndgkm = ptint[0];
	  //printf("%d\n",ndgkm);
	  
	  //fread(&nrft, sizeof(int),1,zdfInput);
	  fread(buffer, sizeof(char),4,zdfInput);
	  if(byteSwapFlag == 1)
	    {
	      swap_bytes(buffer,1,4);
	    }
	  ptint =(int*)buffer;
	  nrft = ptint[0];
	  //printf("%d\n",nrft);
	  
	  //fread(&nrot, sizeof(int),1,zdfInput);
	  fread(buffer, sizeof(char),4,zdfInput);
	  if(byteSwapFlag == 1)
	    {
	      swap_bytes(buffer,1,4);
	    }
	  ptint =(int*)buffer;
	  nrot = ptint[0];
	  //printf("%d\n",nrot);
	  
	  //fread(&nsmpl, sizeof(int),1,zdfInput);
	  fread(buffer, sizeof(char),4,zdfInput);
	  if(byteSwapFlag == 1)
	    {
	      swap_bytes(buffer,1,4);
	    }
	  ptint =(int*)buffer;
	  nsmpl = ptint[0];
	  //printf("%d\n",nsmpl);
	  
	  /*Reading the end of header marker*/
	  fread(cend, sizeof(char),4,zdfInput);
	  //printf("%s\n",cend);
	  
	  //printf("Reading of ZDF Header Complete...\n");
	  /*Reading of ZDF Header Information Complete.*/
	  
	  /*Begin Reading Station and Components*/
	  
	  /*Station Header Information: */
	  
	  for(j=1; j <=nsta; j++)
	    {
	      //printf("Station Number %d\n", j);
	      //fread(&irs, sizeof(int),1,zdfInput);
	      fread(buffer, sizeof(char),4,zdfInput);
	      if(byteSwapFlag == 1)
		{
		  swap_bytes(buffer,1,4);
		}
	      ptint =(int*)buffer;
	      irs = ptint[0];
	      //printf("%d\n",irs);
	      
	      char statName[50];
	      fread(name, sizeof(char),4,zdfInput);
	      sscanf(name,"%s",statName);
	      //printf("%s\n",statName);
	      
	      //fread(&sday, sizeof(int),1,zdfInput);
	      fread(buffer, sizeof(char),4,zdfInput);
	      if(byteSwapFlag == 1)
		{
		  swap_bytes(buffer,1,4);
		}
	      ptint =(int*)buffer;
	      sday = ptint[0];
	      //printf("%d\n",sday);
	      
	      //fread(&shr, sizeof(int),1,zdfInput);
	      fread(buffer, sizeof(char),4,zdfInput);
	      if(byteSwapFlag == 1)
		{
		  swap_bytes(buffer,1,4);
		}
	      ptint =(int*)buffer;
	      shr = ptint[0];
	      //printf("%d\n",shr);
	      
	      //fread(&smin, sizeof(int),1,zdfInput);
	      fread(buffer, sizeof(char),4,zdfInput);
	      if(byteSwapFlag == 1)
		{
		  swap_bytes(buffer,1,4);
		}
	      ptint =(int*)buffer;
	      smin= ptint[0];
	      //printf("%d\n",smin);
	      
	      // fread(&ssec, sizeof(float),1,zdfInput);
	      fread(buffer, sizeof(char),4,zdfInput);
	      if(byteSwapFlag == 1)
		{
		  swap_bytes(buffer,1,4);
		}
	      ptfloat =(float*)buffer;
	      ssec = ptfloat[0];
	      //printf("%f\n",ssec);
	      
	      //fread(&tcrr, sizeof(float),1,zdfInput);
	      fread(buffer, sizeof(char),4,zdfInput);
	      if(byteSwapFlag == 1)
		{
		  swap_bytes(buffer,1,4);
		}
	      ptfloat =(float*)buffer;
	      tcrr = ptfloat[0];
	      //printf("%f\n",tcrr);
	      
	      //fread(&delta, sizeof(float),1,zdfInput);
	      fread(buffer, sizeof(char),4,zdfInput);
	      if(byteSwapFlag == 1)
		{
		  swap_bytes(buffer,1,4);
		}
	      ptfloat =(float*)buffer;
	      delta = ptfloat[0];
	      //printf("%f\n",delta);
	      
	      //fread(&azim, sizeof(float),1,zdfInput);
	      fread(buffer, sizeof(char),4,zdfInput);
	      if(byteSwapFlag == 1)
		{
		  swap_bytes(buffer,1,4);
		}
	      ptfloat =(float*)buffer;
	      azim = ptfloat[0];
	      //printf("%f\n",azim);
	      
	      //fread(&sampin, sizeof(float),1,zdfInput);
	      fread(buffer, sizeof(char),4,zdfInput);
	      if(byteSwapFlag == 1)
		{
		  swap_bytes(buffer,1,4);
		}
	      ptfloat =(float*)buffer;
	      sampin = ptfloat[0];
	      //printf("%f\n",sampin);
	      
	      //fread(&ncomp, sizeof(int),1,zdfInput);
	      fread(buffer, sizeof(char),4,zdfInput);
	      if(byteSwapFlag == 1)
		{
		  swap_bytes(buffer,1,4);
		}
	      ptint =(int*)buffer;
	      ncomp = ptint[0];
	      //printf("%d\n",ncomp);
	      
	      
	      /*Component Header Information: */
	      for(k=1;k<=ncomp;k++)
		{
		  /*Initialize SAC Header Variables*/
		  initialize_sac_header();
		  
		  
		  char compName[50];
		  //printf("Component Number %d\n", k);
		  fread(compid, sizeof(char),4, zdfInput);
		  sscanf(compid, "%s", compName);
		  //printf("%s\n",compName);
		  
		  //fread(&ndat, sizeof(int),1,zdfInput);
		  fread(buffer, sizeof(char),4,zdfInput);
		  if(byteSwapFlag == 1)
		    {
		      swap_bytes(buffer,1,4);
		    }
		  ptint =(int*)buffer;
		  ndat = ptint[0];
		  //printf("%d\n",ndat);
		  
		  //fread(&pred, sizeof(float),1,zdfInput);
		  fread(buffer, sizeof(char),4,zdfInput);
		  if(byteSwapFlag == 1)
		    {
		      swap_bytes(buffer,1,4);
		    }
		  ptfloat =(float*)buffer;
		  pred = ptfloat[0];
		  //printf("%f\n",pred);
		  
		  //fread(&tsrt, sizeof(float),1,zdfInput);
		  fread(buffer, sizeof(char),4,zdfInput);
		  if(byteSwapFlag == 1)
		    {
		      swap_bytes(buffer,1,4);
		    }
		  ptfloat =(float*)buffer;
		  tsrt = ptfloat[0];
		  //printf("%f\n",tsrt);
		  
		  // fread(&amp, sizeof(float),1,zdfInput);
		  fread(buffer, sizeof(char),4,zdfInput);
		  if(byteSwapFlag == 1)
		    {
		      swap_bytes(buffer,1,4);
		    }
		  ptfloat =(float*)buffer;
		  amp = ptfloat[0];
		  //printf("%f\n",amp);
		  
		  //Creating sacFileName, asciiFileName
		  strcpy(sacFileName,"");
		  //strcpy(asciiFileName, "");
		  strcat(sacFileName, argv[1]);
		  strcat(sacFileName, ".");
		  strcat(sacFileName,name);
		  strcat(sacFileName, ".");
		  strcat(sacFileName,compName);
		  //strcat(asciiFileName, sacFileName);
		  strcat(sacFileName,".sac");
		  //strcat(asciiFileName,".ascii");
		  //printf("Sac Filename: %s\n", sacFileName);
		  
		  //Creating Sac File:
		  sacOutput=fopen(sacFileName, "wb");
		  //sacOutput = fopen("TempSac.sac", "wb");
		  
		  //Creating Ascii File:
		  asciiOutput=fopen(asciiFileName, "w");
		  
		  //ZDF to SAC conversions
		  SACNPTS = ndat;
		  NVHDR = 6;
		  B = tsrt;
		  E = tsrt + (ndat-1)*sampin;
		  IFTYPE = 01; // "ITIME"
		  LEVEN = 1; //TRUE
		  LPSPOL = 1; //TRUE Left Hand Rule
		  LOVROK = 1; //TRUE
		  LCALDA = 1; //TRUE
		  DELTA = sampin;
		  NZYEAR = oyr;
		  NZJDAY = sday;
		  NZHOUR = shr;
		  NZMIN = smin;
		  NZSEC = (int)ssec;
		  NZMSEC = (int)( (ssec - (float)NZSEC)*1000. + .5);
		  strcpy(KSTNM, statName);
		  strcpy(KNETWK,ProjectName);
		  strcpy(KCMPNM,compName);
		  EVLA = lat;
		  EVLO = lon;
		  EVDP = dep*1000; //KMs to Meters
		  MAG = mag;


		  if(EVLA == UNDEFINE_F)
		    {
		      IZTYPE = 9;
		    }
		  else
		    {
		      IZTYPE = 11;
		    }

		  //Reading Station information from input station file
		  stationFile = fopen(argv[2],"r");
		  if(stationFile!=NULL)
		    {
		      float junk;
		      char stationCode[10];
		      char junkString[15];
		      while(feof(stationFile)==0){		  
			fscanf(stationFile, "%s", junkString);
			sscanf(junkString, "%s", stationCode);
			if(strcmp(stationCode, name)==0)
			  {
			    //printf("Station Name: %s\n", stationCode);
			    fscanf(stationFile, "%f", &STLA);
			    //printf("Latitude: %f\n", STLA);
			    fscanf(stationFile, "%f", &STLO);
			    //printf("Longitude: %f\n", STLO);
			    fscanf(stationFile, "%f", &STEL);
			    //printf("Elevatin: %f\n", STEL);
			    fscanf(stationFile, "%s", junkString);
			    //printf("Junk: %s\n", junkString);
			    fscanf(stationFile, "%s", junkString);
			    //printf("Junk: %s\n", junkString);
			    fscanf(stationFile, "%s", junkString);
			    //printf("Junk: %s\n", junkString);
			    fscanf(stationFile, "%s", junkString);
			    //printf("Junk: %s\n", junkString);
			    fscanf(stationFile, "%s", junkString);
			    //printf("Junk: %s\n", junkString);
			    fscanf(stationFile, "%s", junkString);
			    //printf("Junk: %s\n", junkString);
			  }//end of if
			else
			  {
			    //printf("Station Name: %s\n", stationCode);
			    fscanf(stationFile, "%f", &junk);
			    //printf("Latitude: %f\n", junk);
			    fscanf(stationFile, "%f", &junk);
			    //printf("Longitude: %f\n", junk);
			    fscanf(stationFile, "%f", &junk);
			    //printf("Elevatin: %f\n", STEL);
			    fscanf(stationFile, "%s", junkString);
			    //printf("Junk: %s\n", junkString);
			    fscanf(stationFile, "%s", junkString);
			    //printf("Junk: %s\n", junkString);
			    fscanf(stationFile, "%s", junkString);
			    //printf("Junk: %s\n", junkString);
			    fscanf(stationFile, "%s", junkString);
			    //printf("Junk: %s\n", junkString);
			    fscanf(stationFile, "%s", junkString);
			    //printf("Junk: %s\n", junkString);
			    fscanf(stationFile, "%s", junkString);
			    //printf("Junk: %s\n", junkString);
			  }//end of else
			
		      }//end of while
		      fclose(stationFile);
		    }//end if
		  
		  //Start of writing to sacOutput
		  //05
		  fwrite(&DELTA, sizeof(float),1,sacOutput);		
		  fwrite(&DEPMIN, sizeof(float),1,sacOutput);
		  fwrite(&DEPMAX, sizeof(float),1,sacOutput);
		  fwrite(&SCALE, sizeof(float),1,sacOutput);
		  fwrite(&ODELTA, sizeof(float),1,sacOutput);
		  //10
		  fwrite(&B, sizeof(float),1,sacOutput);
		  fwrite(&E, sizeof(float),1,sacOutput);
		  fwrite(&O, sizeof(float),1,sacOutput);
		  fwrite(&A, sizeof(float),1,sacOutput);
		  fwrite(&INTERNALF, sizeof(float),1,sacOutput);
		  //15
		  fwrite(&T0, sizeof(float),1,sacOutput);
		  fwrite(&T1, sizeof(float),1,sacOutput);
		  fwrite(&T2, sizeof(float),1,sacOutput);
		  fwrite(&T3, sizeof(float),1,sacOutput);
		  fwrite(&T4, sizeof(float),1,sacOutput);
		  //20
		  fwrite(&T5, sizeof(float),1,sacOutput);
		  fwrite(&T6, sizeof(float),1,sacOutput);
		  fwrite(&T7, sizeof(float),1,sacOutput);
		  fwrite(&T8, sizeof(float),1,sacOutput);
		  fwrite(&T9, sizeof(float),1,sacOutput);
		  //25
		  fwrite(&F, sizeof(float),1,sacOutput);
		  fwrite(&RESP0, sizeof(float),1,sacOutput);
		  fwrite(&RESP1, sizeof(float),1,sacOutput);
		  fwrite(&RESP2, sizeof(float),1,sacOutput);
		  fwrite(&RESP3, sizeof(float),1,sacOutput);
		  //30
		  fwrite(&RESP4, sizeof(float),1,sacOutput);
		  fwrite(&RESP5, sizeof(float),1,sacOutput);
		  fwrite(&RESP6, sizeof(float),1,sacOutput);
		  fwrite(&RESP7, sizeof(float),1,sacOutput);
		  fwrite(&RESP8, sizeof(float),1,sacOutput);
		  //35
		  fwrite(&RESP9, sizeof(float),1,sacOutput);
		  fwrite(&STLA, sizeof(float),1,sacOutput);
		  fwrite(&STLO, sizeof(float),1,sacOutput);
		  fwrite(&STEL, sizeof(float),1,sacOutput);
		  fwrite(&STDP, sizeof(float),1,sacOutput);
		  //40
		  fwrite(&EVLA, sizeof(float),1,sacOutput);
		  fwrite(&EVLO, sizeof(float),1,sacOutput);
		  fwrite(&EVEL, sizeof(float),1,sacOutput);
		  fwrite(&EVDP, sizeof(float),1,sacOutput);
		  fwrite(&MAG, sizeof(float),1,sacOutput);
		  //45
		  fwrite(&USER0, sizeof(float),1,sacOutput);
		  fwrite(&USER1, sizeof(float),1,sacOutput);
		  fwrite(&USER2, sizeof(float),1,sacOutput);
		  fwrite(&USER3, sizeof(float),1,sacOutput);
		  fwrite(&USER4, sizeof(float),1,sacOutput);
		  //50
		  fwrite(&USER5, sizeof(float),1,sacOutput);
		  fwrite(&USER6, sizeof(float),1,sacOutput);
		  fwrite(&USER7, sizeof(float),1,sacOutput);
		  fwrite(&USER8, sizeof(float),1,sacOutput);
		  fwrite(&USER9, sizeof(float),1,sacOutput);
		  //55
		  fwrite(&DIST, sizeof(float),1,sacOutput);
		  fwrite(&AZ, sizeof(float),1,sacOutput);
		  fwrite(&BAZ, sizeof(float),1,sacOutput);
		  fwrite(&GCARC, sizeof(float),1,sacOutput);
		  fwrite(&INTERNALF, sizeof(float),1,sacOutput);
		  //60
		  fwrite(&INTERNALF, sizeof(float),1,sacOutput);
		  fwrite(&DEPMEN, sizeof(float),1,sacOutput);
		  fwrite(&CMPAZ, sizeof(float),1,sacOutput);
		  fwrite(&CMPINC, sizeof(float),1,sacOutput);
		  fwrite(&XMINIMUM, sizeof(float),1,sacOutput);
		  //65
		  fwrite(&XMAXIMUM, sizeof(float),1,sacOutput);
		  fwrite(&YMINIMUM, sizeof(float),1,sacOutput);
		  fwrite(&YMAXIMUM, sizeof(float),1,sacOutput);
		  fwrite(&UNUSEDF, sizeof(float),1,sacOutput);
		  fwrite(&UNUSEDF, sizeof(float),1,sacOutput);
		  //70
		  fwrite(&UNUSEDF, sizeof(float),1,sacOutput);
		  fwrite(&UNUSEDF, sizeof(float),1,sacOutput);
		  fwrite(&UNUSEDF, sizeof(float),1,sacOutput);
		  fwrite(&UNUSEDF, sizeof(float),1,sacOutput);
		  fwrite(&UNUSEDF, sizeof(float),1,sacOutput);
		  //75
		  fwrite(&NZYEAR, sizeof(int),1,sacOutput);
		  fwrite(&NZJDAY, sizeof(int),1,sacOutput);
		  fwrite(&NZHOUR, sizeof(int),1,sacOutput);
		  fwrite(&NZMIN, sizeof(int),1,sacOutput);
		  fwrite(&NZSEC, sizeof(int),1,sacOutput);
		  //80
		  fwrite(&NZMSEC, sizeof(int),1,sacOutput);
		  fwrite(&NVHDR, sizeof(int),1,sacOutput);
		  fwrite(&NORID, sizeof(int),1,sacOutput);
		  fwrite(&NEVID, sizeof(int),1,sacOutput);
		  fwrite(&SACNPTS, sizeof(int),1,sacOutput);
		  //85
		  fwrite(&INTERNALI, sizeof(int),1,sacOutput);
		  fwrite(&NWFID, sizeof(int),1,sacOutput);
		  fwrite(&NXSIZE, sizeof(int),1,sacOutput);
		  fwrite(&NYSIZE, sizeof(int),1,sacOutput);
		  fwrite(&UNUSEDI, sizeof(int),1,sacOutput);
		  //90
		  fwrite(&IFTYPE, sizeof(int),1,sacOutput);
		  fwrite(&IDEP, sizeof(int),1,sacOutput);
		  fwrite(&IZTYPE, sizeof(int),1,sacOutput);
		  fwrite(&UNUSEDI, sizeof(int),1,sacOutput);
		  fwrite(&IINST, sizeof(int),1,sacOutput);
		  //95
		  fwrite(&ISTREG, sizeof(int),1,sacOutput);
		  fwrite(&IEVREG, sizeof(int),1,sacOutput);
		  fwrite(&IEVTYP, sizeof(int),1,sacOutput);
		  fwrite(&IQUAL, sizeof(int),1,sacOutput);
		  fwrite(&ISYNTH, sizeof(int),1,sacOutput);
		  //100
		  fwrite(&IMAGTYP, sizeof(int),1,sacOutput);
		  fwrite(&IMAGSRC, sizeof(int),1,sacOutput);
		  fwrite(&UNUSEDI, sizeof(int),1,sacOutput);
		  fwrite(&UNUSEDI, sizeof(int),1,sacOutput);
		  fwrite(&UNUSEDI, sizeof(int),1,sacOutput);
		  //105
		  fwrite(&UNUSEDI, sizeof(int),1,sacOutput);
		  fwrite(&UNUSEDI, sizeof(int),1,sacOutput);
		  fwrite(&UNUSEDI, sizeof(int),1,sacOutput);
		  fwrite(&UNUSEDI, sizeof(int),1,sacOutput);
		  fwrite(&UNUSEDI, sizeof(int),1,sacOutput);
		  //110
		  fwrite(&LEVEN, sizeof(int),1,sacOutput);
		  fwrite(&LPSPOL, sizeof(int),1,sacOutput);
		  fwrite(&LOVROK, sizeof(int),1,sacOutput);
		  fwrite(&LCALDA, sizeof(int),1,sacOutput);
		  fwrite(&UNUSEDI, sizeof(int),1,sacOutput);
		  //116
		  fwrite(KSTNM, sizeof(char),8,sacOutput);
		  fwrite(KEVNM, sizeof(char),16,sacOutput);
		  //122
		  fwrite(KHOLE, sizeof(char),8,sacOutput);
		  fwrite(KO, sizeof(char),8,sacOutput);
		  fwrite(KA, sizeof(char),8,sacOutput);
		  //128
		  fwrite(KT0, sizeof(char),8,sacOutput);
		  fwrite(KT1, sizeof(char),8,sacOutput);
		  fwrite(KT2, sizeof(char),8,sacOutput);
		  //134
		  fwrite(KT3, sizeof(char),8,sacOutput);
		  fwrite(KT4, sizeof(char),8,sacOutput);
		  fwrite(KT5, sizeof(char),8,sacOutput);
		  //140
		  fwrite(KT6, sizeof(char),8,sacOutput);
		  fwrite(KT7, sizeof(char),8,sacOutput);
		  fwrite(KT8, sizeof(char),8,sacOutput);
		  //146
		  fwrite(KT9, sizeof(char),8,sacOutput);
		  fwrite(KF, sizeof(char),8,sacOutput);
		  fwrite(KUSER0, sizeof(char),8,sacOutput);
		  //152
		  fwrite(KUSER1, sizeof(char),8,sacOutput);
		  fwrite(KUSER2, sizeof(char),8,sacOutput);
		  fwrite(KCMPNM, sizeof(char),8,sacOutput);
		  //END
		  fwrite(KNETWK, sizeof(char),8,sacOutput);
		  fwrite(KDATRD, sizeof(char),8,sacOutput);
		  fwrite(KINST, sizeof(char),8,sacOutput);
		  //End of Writes to sacOutput
		  
		  
		  /*Actual Data Read Here: */
		  for(a=1;a<=ndat;a++)
		    {
		      // fread(&rdat, sizeof(float),1,zdfInput);
		      fread(buffer, sizeof(char),4,zdfInput);
		      if(byteSwapFlag == 1)
			{
			  swap_bytes(buffer,1,4);
			}
		      ptfloat =(float*)buffer;
		      rdat = ptfloat[0];
		      fwrite(&rdat, sizeof(float),1,sacOutput);
		      //fprintf(asciiOutput, "%d\t%f\n", a%(nsmpl+1) ,rdat);
		    }/*End of A Loop*/
		  
		  fclose(sacOutput);
		}/*End of K Loop*/
	      
	      /*Reading the end of station marker*/
	      fread(cend, sizeof(char),4,zdfInput);
	      //printf("%s\n",cend);
	      
	    }/*End of J Loop*/
	  fclose(zdfInput);
	}//End of Else
      
      exitFlag = 1; /*Exiting the while loop*/
    }/*End of While*/
  
  return;
}/*End of Main*/









/*Required Functions*/

/*Endian Checker*/
int endian_check() 
{
    int i = 1;
    char *p = (char *)&i;

    if (p[0] == 1)
        return LITTLE_ENDIAN;
    else
        return BIG_ENDIAN;
}


/*Byte Swapper*/
void swap_bytes(char *buffer,int samples,int size)
{
  char hold;
  int  i;

  for(i=0;i<samples*size;i=i+size)
  {
    if( size == 4 )
    {
      hold = buffer[i];
      buffer[i] = buffer[i+3];
      buffer[i+3] = hold;
      hold = buffer[i+1];
      buffer[i+1] = buffer[i+2];
      buffer[i+2] = hold;
    }
    else if( size == 2 )
    {
      hold = buffer[i];
      buffer[i] = buffer[i+1];
      buffer[i+1] = hold;
    }
  }
}

void initialize_sac_header()
{
//0
DELTA = UNDEFINE_F;
DEPMIN = UNDEFINE_F;
DEPMAX = UNDEFINE_F;
SCALE = UNDEFINE_F;
ODELTA = UNDEFINE_F;

//5
B = UNDEFINE_F;
E = UNDEFINE_F;
O = UNDEFINE_F;
A = UNDEFINE_F;
INTERNALF = UNDEFINE_F;

//10
T0 = UNDEFINE_F;
T1 = UNDEFINE_F;
T2 = UNDEFINE_F;
T3 = UNDEFINE_F;
T4 = UNDEFINE_F;

//15
T5 = UNDEFINE_F;
T6 = UNDEFINE_F;
T7 = UNDEFINE_F;
T8 = UNDEFINE_F;
T9 = UNDEFINE_F;

//20
F = UNDEFINE_F;
RESP0 = UNDEFINE_F;
RESP1 = UNDEFINE_F;
RESP2 = UNDEFINE_F;
RESP3 = UNDEFINE_F;

//25
RESP4 = UNDEFINE_F;
RESP5 = UNDEFINE_F;
RESP6 = UNDEFINE_F;
RESP7 = UNDEFINE_F;
RESP8 = UNDEFINE_F;

//30
RESP9 = UNDEFINE_F;
STLA = UNDEFINE_F;
STLO = UNDEFINE_F;
STEL = UNDEFINE_F;
STDP = UNDEFINE_F;

//35
EVLA = UNDEFINE_F;
EVLO = UNDEFINE_F;
EVEL = UNDEFINE_F;
EVDP = UNDEFINE_F;
MAG = UNDEFINE_F;

//40
USER0 = UNDEFINE_F;
USER1 = UNDEFINE_F;
USER2 = UNDEFINE_F;
USER3 = UNDEFINE_F;
USER4 = UNDEFINE_F;

//45
USER5 = UNDEFINE_F;
USER6 = UNDEFINE_F;
USER7 = UNDEFINE_F;
USER8 = UNDEFINE_F;
USER9 = UNDEFINE_F;

//50
DIST = UNDEFINE_F;
AZ = UNDEFINE_F;
BAZ = UNDEFINE_F;
GCARC = UNDEFINE_F;
//	INTERNAL; //Already Defined in 0

//55
//	INTERNAL; //Already Defined in 0
DEPMEN = UNDEFINE_F;
CMPAZ = UNDEFINE_F;
CMPINC = UNDEFINE_F;
XMINIMUM = UNDEFINE_F;	

//60
XMAXIMUM = UNDEFINE_F;
YMINIMUM = UNDEFINE_F;
YMAXIMUM = UNDEFINE_F;
UNUSEDF = UNDEFINE_F; 
//	UNUSED; //Already Defined

//65	ALL UNUSED

//70
NZYEAR = UNDEFINE_N;
NZJDAY = UNDEFINE_N;
NZHOUR = UNDEFINE_N;
NZMIN = UNDEFINE_N;
NZSEC = UNDEFINE_N;

//75
NZMSEC = UNDEFINE_N;
NVHDR = UNDEFINE_N;
NORID = UNDEFINE_N;
NEVID = UNDEFINE_N;
SACNPTS = UNDEFINE_N; //NOTE: NPTS Variable ALready defined for ZDF. This variable has thus been renamed to show the difference between the two.

//80
INTERNALI = UNDEFINE_N; //ALREADY DEFINED AS FLOAT.
NWFID = UNDEFINE_N;
NXSIZE = UNDEFINE_N;
NYSIZE = UNDEFINE_N;
UNUSEDI = UNDEFINE_N;

//85
IFTYPE = UNDEFINE_I;
IDEP = UNDEFINE_I;
IZTYPE = UNDEFINE_I;
//	UNUSED
IINST = UNDEFINE_I;

//90
ISTREG = UNDEFINE_I;
IEVREG = UNDEFINE_I;
IEVTYP = UNDEFINE_I;
IQUAL = UNDEFINE_I;
ISYNTH = UNDEFINE_I;

//95
IMAGTYP = UNDEFINE_I;
IMAGSRC = UNDEFINE_I;
//	UNUSED
//	UNUSED
//	UNUSED

//100	ALL UNUSED

//105
LEVEN = UNDEFINE_L;
LPSPOL = UNDEFINE_L;
LOVROK = UNDEFINE_L;
LCALDA = UNDEFINE_L;
//	UNUSED

//110

//char 	KSTNM[] = UNDEFINE_K;
//char	KEVNM[16] = UNDEFINE_K;
set_char_undefine(KSTNM);
set_char_undefine(KEVNM);
//116
//char	KHOLE[8] = UNDEFINE_K;
//char	KO[8] = UNDEFINE_K;
//char	KA[8] = UNDEFINE_K;
set_char_undefine(KHOLE);
set_char_undefine(KO);
set_char_undefine(KA);

//122
//char	KT0[8] = UNDEFINE_K;
//char	KT1[8] = UNDEFINE_K;
//char	KT2[8] = UNDEFINE_K;
set_char_undefine(KT0);
set_char_undefine(KT1);
set_char_undefine(KT2);

//128
//char	KT3[8] = UNDEFINE_K;
//char	KT4[8] = UNDEFINE_K;
//char	KT5[8] = UNDEFINE_K;
set_char_undefine(KT3);
set_char_undefine(KT4);
set_char_undefine(KT5);

//134
//char	KT6[8] = UNDEFINE_K;
//char	KT7[8] = UNDEFINE_K;
//char	KT8[8] = UNDEFINE_K;
set_char_undefine(KT6);
set_char_undefine(KT7);
set_char_undefine(KT8);
//140
//char	KT9[8] = UNDEFINE_K;
//char	KF[8] = UNDEFINE_K;
//char	KUSER0[8] = UNDEFINE_K;
set_char_undefine(KT9);
set_char_undefine(KF);
set_char_undefine(KUSER0);

//146
//char	KUSER1[8] = UNDEFINE_K;
//char	KUSER2[8] = UNDEFINE_K;
//char	KCMPNM[8] = UNDEFINE_K;
set_char_undefine(KUSER1);
set_char_undefine(KUSER2);
set_char_undefine(KCMPNM);

//152
//char	KNETWK[8] = UNDEFINE_K;
//char	KDATRD[8] = UNDEFINE_K;
//char	KINST[8] = UNDEFINE_K;
set_char_undefine(KNETWK);
set_char_undefine(KDATRD);
set_char_undefine(KINST);

}


void set_char_undefine(char *stringVar)
{
   strcpy(stringVar,"-12345");
}


