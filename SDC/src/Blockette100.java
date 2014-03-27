public class Blockette100 {

	short blocketteType;
	short nextBlocketteByteNumber;
	float actualSampleRate;

	Blockette100(BinaryRandomInput input) {

		try {

			blocketteType = input.readShortB();
			nextBlocketteByteNumber = input.readShortB();

			actualSampleRate = input.readFloatB();

		}

		catch (Exception e) {
			System.out.println("Exception " + e);
		}
	}
}
