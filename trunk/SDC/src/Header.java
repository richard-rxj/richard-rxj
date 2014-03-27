public class Header {

	String sequenceNumberHead;
	String dataHeaderHead;
	String reservedByteHead;
	String stationCodeHead;
	String locationIdentifierHead;
	String channelIdentifierHead;
	String networkCodeHead;

	short year;
	short day;
	short hourOfDay;
	short minOfDay;
	short secsOfDay;
	short milisecsOfDay; // milli-seconds

	short numberOfSamples;
	short rateFactor;
	short rateMultiplier;

	short activityFlag;
	short ioFlag;
	short dataQualityFlag;
	short numberFollowingBlockettes;

	int timeCorrection;

	short beginingOfData;
	short startOfFirstBlockette;

	Header(BinaryRandomInput input) {

		try {
			byte sequenceNumber[] = new byte[6];
			byte dataHeader[] = new byte[1];
			byte reservedByte[] = new byte[1];
			byte stationCode[] = new byte[5];
			byte locationIdentifier[] = new byte[2];
			byte channelIdentifier[] = new byte[3];
			byte networkCode[] = new byte[2];

			input.readFully(sequenceNumber);
			sequenceNumberHead = new String(sequenceNumber);

			input.readFully(dataHeader);
			dataHeaderHead = new String(dataHeader);

			input.readFully(reservedByte);
			reservedByteHead = new String(reservedByte);

			input.readFully(stationCode);
			stationCodeHead = new String(stationCode);

			input.readFully(locationIdentifier);
			locationIdentifierHead = new String(locationIdentifier);

			input.readFully(channelIdentifier);
			channelIdentifierHead = new String(channelIdentifier);

			input.readFully(networkCode);
			networkCodeHead = new String(networkCode);
			//
			// Read time
			//

			year = input.readShortB();
			day = input.readShortB();

			hourOfDay = (short) input.read();
			minOfDay = (short) input.read();
			secsOfDay = (short) input.read();

			int unused = input.read();

			// NOTE:
			// input.readShortB() gives the number
			// of one-ten thousandths of a second.
			milisecsOfDay = (short) (input.readShortB() / 10);

			// End of reading time

			numberOfSamples = input.readShortB();
			rateFactor = input.readShortB();
			rateMultiplier = input.readShortB();

			activityFlag = (short) input.read();
			ioFlag = (short) input.read();
			dataQualityFlag = (short) input.read();
			numberFollowingBlockettes = (short) input.read();

			timeCorrection = input.readIntB();

			beginingOfData = input.readShortB();
			startOfFirstBlockette = input.readShortB();

		}

		catch (Exception e) {
			System.out.println("Exception " + e);
		}
	}
}
