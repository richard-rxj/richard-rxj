public class Steim2 {
	public static int[] decode(byte[] b, int numSamples, boolean swapBytes,
			int bias) throws SteimException {

		if (b.length % 64 != 0) {
			throw new SteimException(
					" Encoded data length is not multiple of 64 bytes ("
							+ b.length + ")");
		}

		int[] samples = new int[numSamples];
		int[] tempSamples;
		int numFrames = b.length / 64;
		int current = 0;
		int start = 0, end = 0;
		int firstData = 0;
		int lastValue = 0;

		System.out.println("DEBUG: number of samples: " + numSamples
				+ ", number of frames: " + numFrames + ", byte array size: "
				+ b.length);

		for (int i = 0; i < numFrames; i++) {
			System.out.println("DEBUG: start of frame " + i);
			tempSamples = extractSamples(b, i * 64, swapBytes);
			// returns only differences except for frame 0

			firstData = 0; // d(0) is byte 0 by default

			if (i == 0) { // special case for first frame

				lastValue = bias; // assign our X(-1)
				// x0 and xn are in 1 and 2 spots
				start = tempSamples[1]; // X(0) is byte 1 for frame 0
				end = tempSamples[2]; // X(n) is byte 2 for frame 0
				firstData = 3; // d(0) is byte 3 for frame 0

				System.out.println("DEBUG: frame " + i + ", bias = " + bias
						+ ", x(0) = " + start + ", x(n) = " + end);
				// if bias was zero, then we want the first sample to be X(0)
				// constant

				if (bias == 0)
					lastValue = start - tempSamples[3];
				// X(-1) = X(0) - d(0)
			} // end if

			System.out.println("DEBUG: ");
			for (int j = firstData; j < tempSamples.length
					&& current < numSamples; j++) {

				samples[current] = lastValue + tempSamples[j];
				// X(n) = X(n-1) + d(n)
				lastValue = samples[current];

				System.out.println("d(" + (j - firstData) + ")"
						+ tempSamples[j] + ", x(" + current + ")"
						+ samples[current] + ";");

				current++;
			}
			System.out.println("DEBUG: end of frame " + i);
		} // end for each frame...
		return samples;
	} // Steim Exception

	// =======================================================================

	public static int[] decode(byte[] b, int numSamples, boolean swapBytes)
			throws SteimException {
		// zero-bias version of decode
		return decode(b, numSamples, swapBytes, 0);
	}

	public static byte[] encode(int[] samples) {
		byte[] b = new byte[0];
		return b;
	}

	protected static int[] extractSamples(byte[] bytes, int offset,
			boolean swapBytes) {
		/* get nibbles */
		int nibbles = Utility.bytesToInt(bytes[offset], bytes[offset + 1],
				bytes[offset + 2], bytes[offset + 3], swapBytes);

		int currNibble = 0;
		int dnib = 0;
		int[] temp = new int[106]; // 7 samples * 15 long words + 1 nibble int

		int tempInt;
		int currNum = 0;

		for (int i = 0; i < 16; i++) {
			currNibble = (nibbles >> (30 - i * 2)) & 0x03;

			switch (currNibble) {
			case 0:
				// System.out.println("0 means header info");
				// only include header info if offset is 0

				if (offset == 0) {
					temp[currNum++] = Utility.bytesToInt(
							bytes[offset + (i * 4)],
							bytes[offset + (i * 4) + 1], bytes[offset + (i * 4)
									+ 2], bytes[offset + (i * 4) + 3],
							swapBytes);
				}
				break;
			case 1:
				// System.out.println("1 means 4 one byte differences");
				temp[currNum++] = Utility.bytesToInt(bytes[offset + (i * 4)]);
				temp[currNum++] = Utility
						.bytesToInt(bytes[offset + (i * 4) + 1]);
				temp[currNum++] = Utility
						.bytesToInt(bytes[offset + (i * 4) + 2]);
				temp[currNum++] = Utility
						.bytesToInt(bytes[offset + (i * 4) + 3]);

				break;
			case 2:

				tempInt = Utility.bytesToInt(bytes[offset + (i * 4)],
						bytes[offset + (i * 4) + 1],
						bytes[offset + (i * 4) + 2],
						bytes[offset + (i * 4) + 3], swapBytes);
				dnib = (tempInt >> 30) & 0x03;
				switch (dnib) {
				case 1:
					// System.out.println("2,1 means 1 thirty bit difference");
					temp[currNum++] = (tempInt << 2) >> 2;
					break;

				case 2:
					// System.out.println("2,2 means 2 fifteen bit differences");
					temp[currNum++] = (tempInt << 2) >> 17; // d0
					temp[currNum++] = (tempInt << 17) >> 17; // d1
					break;
				case 3:
					// System.out.println("2,3 means 3 ten bit differences");
					temp[currNum++] = (tempInt << 2) >> 22; // d0
					temp[currNum++] = (tempInt << 12) >> 22; // d1
					temp[currNum++] = (tempInt << 22) >> 22; // d2
					break;
				default:
					// System.out.println("default");
				}
				break;
			case 3:
				tempInt = Utility.bytesToInt(bytes[offset + (i * 4)],
						bytes[offset + (i * 4) + 1],
						bytes[offset + (i * 4) + 2],
						bytes[offset + (i * 4) + 3], swapBytes);

				dnib = (tempInt >> 30) & 0x03;

				// for case 3, we are going to use a for-loop formulation that
				// accomplishes the same thing as case 2, just less verbose.
				int diffCount = 0; // number of differences
				int bitSize = 0; // bit size
				int headerSize = 0; // number of header/unused bits at top

				switch (dnib) {
				case 0:
					// System.out.println("3,0 means 5 six bit differences");
					headerSize = 2;
					diffCount = 5;
					bitSize = 6;
					break;
				case 1:
					// System.out.println("3,1 means 6 five bit differences");
					headerSize = 2;
					diffCount = 6;
					bitSize = 5;
					break;
				case 2:
					// System.out.println("3,2 means 7 four bit differences, with 2 unused bits");
					headerSize = 4;
					diffCount = 7;
					bitSize = 4;
					break;
				default:
					// System.out.println("default");
				}

				if (diffCount > 0) {
					for (int d = 0; d < diffCount; d++) { // for-loop
															// formulation
						temp[currNum++] = (tempInt << (headerSize + (d * bitSize))) >> (((diffCount - 1) * bitSize) + headerSize);
					}
				}
			} // currNib
		}// for

		int[] out = new int[currNum];
		System.arraycopy(temp, 0, out, 0, currNum);
		return out;

	}

} // Steim2
