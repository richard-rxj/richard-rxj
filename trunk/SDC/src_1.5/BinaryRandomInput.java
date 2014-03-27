
import java.io.*;

/**
 * Reads binary data
 */

public class BinaryRandomInput extends RandomAccessFile {

	public int binaryType;

	/** Constructor */

	public BinaryRandomInput(String fileName, String binaryTypeStr)
			throws IOException {

		super(fileName, "r");

		binaryType = BinaryType.stringToBinaryType(binaryTypeStr);

	}

	public BinaryRandomInput(String fileName, int iBinaryType)
			throws IOException {

		super(fileName, "r");

		binaryType = BinaryType.intToBinaryType(iBinaryType);

	}

	public final short readShortB() throws IOException {
		switch (binaryType) {
		case BinaryType.PC_INTEL: {
			int byte0 = readUnsignedByte();
			int byte1 = readUnsignedByte();
			short ival = (short) ((byte1 << 8) | byte0);
			return ival;
		}
		case BinaryType.SUN_UNIX:
		default:
			return readShort();
		}
	}

	public final int readUnsignedShortB() throws IOException {
		switch (binaryType) {
		case BinaryType.PC_INTEL: {
			int byte0 = readUnsignedByte();
			int byte1 = readUnsignedByte();
			int ival = (int) ((byte1 << 8) | byte0);
			return ival;
		}
		case BinaryType.SUN_UNIX:
		default:
			return readUnsignedShort();
		}
	}

	public final int readIntB() throws IOException {
		switch (binaryType) {
		case BinaryType.PC_INTEL: {
			int byte0 = readUnsignedByte();
			int byte1 = readUnsignedByte();
			int byte2 = readUnsignedByte();
			int byte3 = readUnsignedByte();
			int ival = (((byte3 << 8 | byte2) << 8 | byte1) << 8) | byte0;
			return ival;
		}
		case BinaryType.SUN_UNIX:
		default:
			return readInt();
		}
	}

	public final float readFloatB() throws IOException {
		switch (binaryType) {
		case BinaryType.PC_INTEL:
			int byte0 = readUnsignedByte();
			int byte1 = readUnsignedByte();
			int byte2 = readUnsignedByte();
			int byte3 = readUnsignedByte();
			int ival = (((byte3 << 8 | byte2) << 8 | byte1) << 8) | byte0;
			float fval = Float.intBitsToFloat(ival);
			return fval;
		case BinaryType.SUN_UNIX:
		default:
			return readFloat();
		}
	}

	public final String readStringB(int length) throws IOException {
		byte bytes[] = new byte[length];
		read(bytes, 0, length);
		return (new String(bytes)).trim();
	}

}
