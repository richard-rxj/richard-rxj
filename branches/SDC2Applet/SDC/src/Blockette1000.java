public class Blockette1000 {

	short blocketteType;
	short nextBlocketteByteNumber;
	int encodingFormat;
	int wordOrder;
	int dataRecordLength;
	int reservedByte;

	Blockette1000(BinaryRandomInput input) {

		try {

			blocketteType = input.readShortB();
			nextBlocketteByteNumber = input.readShortB();

			encodingFormat = input.readUnsignedByte();
			wordOrder = input.readUnsignedByte();
			dataRecordLength = input.readUnsignedByte();
			reservedByte = input.readUnsignedByte();

		}

		catch (Exception e) {
			System.out.println("Exception " + e);
		}
	}
}
