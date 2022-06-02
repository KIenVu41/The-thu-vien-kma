package com.kma.thethuvien;

import javacard.framework.*;
import javacard.framework.OwnerPIN;

public class Connect extends Applet
{
	public final static byte IDCARD_CLA = (byte)0x80;
	public final static byte INS_PRINT = (byte)0x11;
	public final static byte VERIFY = (byte)0x20;
	public static final byte INS_SET_OWNER_PIN = (byte) 0x05;
	public static byte[] text;

	// maximum number of incorrect tries before the PIN is blocked
    final static byte PIN_TRY_LIMIT =(byte)0x03;
    // maximum size PIN
    final static byte MAX_PIN_SIZE =(byte)0x04;
    
     // signal that the PIN verification failed
    final static short SW_VERIFICATION_FAILED = 0x6300;
	OwnerPIN pin;
	byte[] tmp;
	protected Connect(byte[] bArray, short bOffset, byte bLength) {
		//  dài ca AID
		// byte aIDLen = bArray[bOffset];
		// if(aIDLen == 0) {
			// pin = new OwnerPIN(PIN_TRY_LIMIT, MAX_PIN_SIZE);

			// register();
		// }else {
			// register(bArray, (short) (bOffset+1), aIDLen);
			
		// }
		// bOffset = (short) (bOffset+aIDLen+1); 
		// byte cLen = bArray[bOffset]; //  dài thông tin iu khin
		// bOffset = (short) (bOffset+cLen+1);
		// byte aLen = bArray[bOffset]; //  dài d liu applet
		// bOffset = (short) (bOffset + 1);
		
		// text = new byte[]{(byte)0x48, (byte)0x45, (byte)0x4c, (byte)0x4f};
		 pin = new OwnerPIN(PIN_TRY_LIMIT, MAX_PIN_SIZE);
		 byte iLen = bArray[bOffset]; // aid length
         bOffset = (short) (bOffset + iLen + 1);
         byte cLen = bArray[bOffset]; // info length
         bOffset = (short) (bOffset + cLen + 1);
         byte aLen = bArray[bOffset]; // applet data length

        // // The installation parameters contain the PIN
        // // initialization value
        tmp = JCSystem.makeTransientByteArray((short) 256, JCSystem.CLEAR_ON_DESELECT);
        register();
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
			case VERIFY:        
				verify(apdu);
				break;
			 case INS_SET_OWNER_PIN:
                 insSetOwnerPin(apdu);
                 break;
			default:
				ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}
	
	private void insSetOwnerPin(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        JCSystem.beginTransaction();
        pin.update(buffer, ISO7816.OFFSET_CDATA, (byte) (buffer[ISO7816.OFFSET_LC] & 0x00FF));
        JCSystem.commitTransaction();
    }
   
	 private void verify(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        short numBytes = (short) (buffer[ISO7816.OFFSET_LC] & 0x00FF);
        // retrieve the PIN data for validation.
        //byte byteRead = (byte)(apdu.setIncomingAndReceive());
        // check pin the PIN data is read into the APDU buffer at the offset ISO7816.OFFSET_CDATA
        // the PIN data length = byteRead
        // if ( pin.check(buffer, ISO7816.OFFSET_CDATA, byteRead) == false )
				// ISOException.throwIt(SW_VERIFICATION_FAILED);

    }
}
