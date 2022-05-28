package com.kma.thethuvien;

import javacard.framework.*;

public class Connect extends Applet
{
	public final static byte IDCARD_CLA = (byte)0xB0;
	public final static byte INS_PRINT = (byte)0x11;
	public static byte[] text;
	
	protected Connect(byte[] bArray, short bOffset, byte bLength) {
		byte aIDLen = bArray[bOffset];
		if(aIDLen == 0) {
			register();
		}else {
			register(bArray, (short) (bOffset+1), aIDLen);
			
		}
		bOffset = (short) (bOffset+aIDLen+1);
		byte cLen = bArray[bOffset];
		bOffset = (short) (bOffset+cLen+1);
		byte aLen = bArray[bOffset];
		bOffset = (short) (bOffset + 1);
		
		text = new byte[]{(byte)0x48, (byte)0x45, (byte)0x4c, (byte)0x4f};
		
	}
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		Connect cnt = new Connect(bArray, bOffset, bLength);
	}

	public void process(APDU apdu)
	{
		byte[] buf = apdu.getBuffer();
		if((buf[ISO7816.OFFSET_CLA] == 0x00) && (buf[ISO7816.OFFSET_INS] == (byte)(0xA4)))
			return;
		
		if(buf[ISO7816.OFFSET_CLA] != IDCARD_CLA)
			ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
			
		switch (buf[ISO7816.OFFSET_INS])
		{
			case INS_PRINT:
				//PrintTexst(apdu);
				break;
			default:
				ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}

}
