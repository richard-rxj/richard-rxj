/*****************************************************************************
 * Azimuth.java
 * 
 * RSES-ANU
 * 
 * Jason Li, Nov 2008 jason.li@anu.edu.au
 * 
 * Calculates the azimuth, back-azimuth, range (in degrees) for give coordinates
 * 
 * Modified from the code in C by Prof. Brian Kennett
 ******************************************************************************/

public class Azimuth {

	float rangeDg;
	float rangeKm;
	float azim;
	float bazim;

	public Azimuth() {
		// no input, do nothing
	}

	public Azimuth(float originLat, float originLon, float stationLat,
			float stationLon) {

		double azima, delta, bazima, olat, olong, slat, slong;

		double dlats, dlons, dlatr, dlonr;
		double ecc, ec1, re, pi, pib2, degr, aa, bb, glats, glatr, sps, cps;
		double spr, cpr, rs, rr, trs, prs, trr, prr, AS, BS, CS, DS, ES, GS, HS, KS;
		double AR, BR, CR, DR, ER, GR, HR, KR, cosdr, deltar, deltak, szr;
		double czr, cazim, czs, szs, junk;

		// include the origin and station data.
		olat = (double) stationLat;
		olong = (double) stationLon;
		slat = (double) originLat;
		slong = (double) originLon;

		bazima = 0;
		if (olat > 90.0F || olong > 180.0F) {
			delta = 0.0F;
			rangeDg = 0;
			rangeKm = 0;
			azima = 0.0F;
			azim = 0;
			return;
		}
		ecc = 0.003367F;
		ec1 = (1.0F - ecc) * (1.0F - ecc);
		re = 6378.388F;
		pi = Math.PI;
		pib2 = pi / 2.0F;
		degr = pi / 180.0F;
		dlats = olat * degr;
		dlons = olong * degr;
		dlatr = slat * degr;
		dlonr = slong * degr;
		/******** geocentric coor **********/
		aa = ec1 * Math.sin(dlats);
		bb = Math.cos(dlats);
		glats = Math.atan2(aa, bb);
		glatr = Math.atan2(ec1 * Math.sin(dlatr), Math.cos(dlatr));
		sps = Math.sin(glats) * Math.sin(glats);
		cps = Math.cos(glats) * Math.cos(glats);
		spr = Math.sin(glatr) * Math.sin(glatr);
		cpr = Math.cos(glatr) * Math.cos(glatr);
		/********* radii at source,receiver ******/
		rs = re * sacgra(sps, cps, ecc);
		rr = re * sacgra(spr, cpr, ecc);
		trs = pib2 - glats;
		prs = dlons;
		trr = pib2 - glatr;
		prr = dlonr;
		/*********** direction cosines of source *********/
		AS = Math.sin(trs) * Math.cos(prs);
		BS = Math.sin(trs) * Math.sin(prs);
		CS = Math.cos(trs);
		DS = Math.sin(prs);
		ES = 0.0F - Math.cos(prs);
		GS = Math.cos(trs) * Math.cos(prs);
		HS = Math.cos(trs) * Math.sin(prs);
		KS = 0.0F - Math.sin(trs);
		/********** direction cosines of reciever **********/
		AR = Math.sin(trr) * Math.cos(prr);
		BR = Math.sin(trr) * Math.sin(prr);
		CR = Math.cos(trr);
		DR = Math.sin(prr);
		ER = 0.0F - Math.cos(prr);
		GR = Math.cos(trr) * Math.cos(prr);
		HR = Math.cos(trr) * Math.sin(prr);
		KR = 0.0F - Math.sin(trr);
		/********************** distance ***************/
		cosdr = (AS * AR) + (BS * BR) + (CS * CR);
		/*** cal distance *****/
		deltar = Math.acos(cosdr);
		deltak = deltar * 0.5F * (rr + rs);
		delta = deltar / degr;
		/*********************** azimith ************************/
		szr = DR * AS + ER * BS;
		czr = GR * AS + HR * BS + KR * CS;
		szs = DS * AR + ES * BR;
		czs = GS * AR + HS * BR + KS * CR;
		if (szr == 0.0F)
			azima = 180.0F;
		else {
			azima = Math.atan2(0.0F - szr, 0.0F - czr) / degr;
			junk = Math.atan2(0.0F - szs, 0.0F - czs) / degr;
			bazima = junk;
		}
		if (azima < 0.0F)
			azima = azima + 360.0F;
		if (bazima < 0.0F)
			bazima = bazima + 360.0F;
		cazim = azima + 180.0F;
		if (cazim < 0.0F)
			cazim = cazim - 360.0F;

		// finalizing the numbers
		rangeDg = (float) delta;
		rangeKm = (float) deltak;

		azim = (float) azima;
		bazim = (float) bazima;

		return;
	}

	double sacgra(double xvar, double yvar, double evar) {
		double value, cal1, cal2, cal3;
		cal1 = (1.0F - evar) * (1.0F - evar);
		cal2 = (1.0F - evar * yvar) * (1.0F - evar * yvar);
		cal3 = (evar * evar * xvar * yvar);
		value = cal1 / (cal2 + cal3);
		return (value);
	}

}
