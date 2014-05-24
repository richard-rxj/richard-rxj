import java.io.*;
import java.util.*;

public class ZDFComponent {

	/** Component header information */
	public char[] compid = new char[4]; // component identifier
	public int ndat; // number of data points
	public float pred; // reduction slowness
	public float tsrt; // starting reduced time
	public float amp; // max amplitude

	/** Component Data */
	SpecialMiniSeedReader data;

	public ZDFComponent() {
		// do nothing
	}

	public ZDFComponent(SpecialMiniSeedReader m) {
		// copy over data
		data = m;
		compid[0] = 'B';
		compid[1] = 'H';
		compid[2] = m.component.charAt(0);
		compid[3] = ' ';

		// number of data points
		ndat = m.npts;

		// reduction slowness
		pred = 0;

		// start reduced time
		tsrt = 0;

		// maximum amplitude
		amp = m.maxAmp;

	}

	public int writeZdfComponent(DataOutputStream dos) {
		try {

			/** Writing the header */
			if (ndat != data.npts) {
				System.err.println("WRONG NUMBER OF POINTS IN ZDF COMPONENT");
				System.exit(0);
			}

			for (int i = 0; i < 4; i++)
				dos.writeByte((byte) compid[i]);

			// write number of data points
			dos.writeInt(ndat);

			// write reduction slowness
			dos.writeFloat(pred);

			// write start reduced time
			dos.writeFloat(tsrt);

			// write max amplitude
			dos.writeFloat(amp);

			// write data
			for (int i = 0; i < ndat; i++) {
				dos.writeFloat(data.data[i]);
			}
		}
		/** Catch exceptions */
		catch (SecurityException securityException) {
			/** Security exception */
			System.err.println("NO ACCESS");
			return -1;
		} catch (IOException e) {
			/** IO exception */
			System.err.println("IO ERROR: " + e);
			return -2;
		}

		return 1;
	}

}
