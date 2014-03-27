void cal_dist_azim_sac( int type, double *azima, double *delta, double *bazima,int *ndgkm,
					   double olat,double olong,double slat,double slong)
 {
/*** Calculates the djstances between the station coor and event coor **/
     

    double dlats,dlons,dlatr,dlonr;
    double ecc,ec1,re,pi,pib2,degr,aa,bb,glats,glatr,sps,cps;
    double spr,cpr,rs,rr,trs,prs,trr,prr,AS,BS,CS,DS,ES,GS,HS,KS;
    double AR,BR,CR,DR,ER,GR,HR,KR,cosdr,deltar,deltak,szr;
    double czr,cazim,czs,szs,junk;

	*bazima = 0;
    if( olat > 90.0F || olong > 180.0F)
     {
	   *delta = 0.0F;
       *ndgkm = 0L;
       *azima =   0.0F;
       return;
     }	 
    ecc = 0.003367F;
    ec1 =(1.0F-ecc)*(1.0F-ecc);
    re = 6378.388F;
    pi = 3.141592653589793F;
    pib2 = pi/2.0F;
    degr = pi/180.0F;
    dlats = olat * degr;
    dlons = olong * degr;
    dlatr = slat * degr;
    dlonr = slong * degr;
  /********  geocentric coor **********/
    aa=ec1*sin(dlats);
    bb=cos(dlats);
    glats = atan2(aa,bb);    
    glatr = atan2(ec1*sin(dlatr),cos(dlatr));	 
    sps = sin(glats)*sin(glats);
    cps = cos(glats)*cos(glats);
    spr = sin(glatr)*sin(glatr);
    cpr = cos(glatr)*cos(glatr);
 /*********	radii at source,receiver ******/
    rs = re * sacgra(sps,cps,ecc);
    rr = re * sacgra(spr,cpr,ecc);
    trs = pib2 - glats;
    prs = dlons;
    trr = pib2 - glatr;
    prr = dlonr;
 /***********  direction cosines of source *********/
    AS = sin(trs)*cos(prs);
    BS = sin(trs)*sin(prs);
    CS = cos(trs);
    DS = sin(prs);
    ES = 0.0F - cos(prs);
    GS = cos(trs)*cos(prs);
    HS = cos(trs)*sin(prs);
    KS = 0.0F - sin(trs);
 /**********   direction cosines of reciever **********/
    AR = sin(trr)*cos(prr);
    BR = sin(trr)*sin(prr);
    CR = cos(trr);
    DR = sin(prr);
    ER = 0.0F - cos(prr);
    GR = cos(trr) *cos(prr);
    HR = cos(trr) *sin(prr);
    KR = 0.0F - sin(trr);
  /**********************  djstance ***************/
    cosdr = (AS*AR)+(BS*BR)+(CS*CR);
				      /*** cal distance *****/
    deltar = acos(cosdr);
    deltak = deltar*0.5F*(rr+rs);
    *delta = deltar/degr;
 /***********************   azimith ************************/
    szr = DR*AS + ER*BS;
    czr = GR*AS + HR*BS + KR*CS;
    szs = DS*AR + ES*BR;
    czs = GS*AR + HS*BR + KS*CR;
    if( szr == 0.0F )
      *azima = 180.0F;
    else
     {
       *azima = atan2( 0.0F - szr, 0.0F - czr )/ degr;
       junk = atan2( 0.0F - szs, 0.0F - czs) / degr;
	   *bazima = junk;
     }
    if( *azima < 0.0F )
      *azima = *azima + 360.0F;
    if( *bazima < 0.0F )
      *bazima = *bazima + 360.0F;
    cazim = *azima + 180.0F;
    if( cazim < 0.0F )
      cazim = cazim - 360.0F;
    if( type == 0 )    /*** dealing in degrees ***/
      *ndgkm = 0L;
    else
    {
      *delta = deltak;
      *ndgkm = 1L;
    }
  }
    
double sacgra( double xvar,double yvar,double evar)
 {
    double value,cal1,cal2,cal3;
    
    cal1 = (1.0F - evar)*(1.0F - evar);
    cal2 = (1.0F - evar*yvar) * (1.0F - evar*yvar);
    cal3 = ( evar*evar*xvar*yvar);
    value = cal1/(cal2+cal3);
    return(value);
 }
