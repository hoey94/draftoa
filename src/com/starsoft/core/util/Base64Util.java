package com.starsoft.core.util;

public class Base64Util {

    static private final int  BASELENGTH         = 255;
    static private final int  LOOKUPLENGTH       = 64;
    static private final int  TWENTYFOURBITGROUP = 24;
    static private final int  EIGHTBIT           = 8;
    static private final int  SIXTEENBIT         = 16;
    static private final int  SIXBIT             = 6;
    static private final int  FOURBYTE           = 4;
    static private final int  SIGN               = -128;
    static private final char PAD                = '=';
    static private final boolean fDebug          = false;
    static final private byte [] base64Alphabet        = new byte[BASELENGTH];
    static final private char [] lookUpBase64Alphabet  = new char[LOOKUPLENGTH];

    static {
        for (int i = 0; i<BASELENGTH; i++) {
            base64Alphabet[i] = -1;
        }
        for (int i = 'Z'; i >= 'A'; i--) {
            base64Alphabet[i] = (byte) (i-'A');
        }
        for (int i = 'z'; i>= 'a'; i--) {
            base64Alphabet[i] = (byte) ( i-'a' + 26);
        }
        for (int i = '9'; i >= '0'; i--) {
            base64Alphabet[i] = (byte) (i-'0' + 52);
        }
        base64Alphabet['+']  = 62;
        base64Alphabet['/']  = 63;
        for (int i = 0; i<=25; i++)
            lookUpBase64Alphabet[i] = (char)('A'+i);
        for (int i = 26,  j = 0; i<=51; i++, j++)
            lookUpBase64Alphabet[i] = (char)('a'+ j);
        for (int i = 52,  j = 0; i<=61; i++, j++)
            lookUpBase64Alphabet[i] = (char)('0' + j);
        lookUpBase64Alphabet[62] = (char)'+';
        lookUpBase64Alphabet[63] = (char)'/';
    }

    protected static boolean isWhiteSpace(char octect) {
        return (octect == 0x20 || octect == 0xd || octect == 0xa || octect == 0x9);
    }

    protected static boolean isPad(char octect) {
        return (octect == PAD);
    }

    protected static boolean isData(char octect) {
        return (base64Alphabet[octect] != -1);
    }

    protected static boolean isBase64(char octect) {
        return (isWhiteSpace(octect) || isPad(octect) || isData(octect));
    }

    /**
     * Encodes hex octects into Base64
     *
     * @param binaryData Array containing binaryData
     * @return Encoded Base64 array
     */
    public static String encode(byte[] binaryData) {

        if (binaryData == null)
            return null;

        int      lengthDataBits    = binaryData.length*EIGHTBIT;
        if (lengthDataBits == 0) {
            return "";
        }
        
        int      fewerThan24bits   = lengthDataBits%TWENTYFOURBITGROUP;
        int      numberTriplets    = lengthDataBits/TWENTYFOURBITGROUP;
        int      numberQuartet     = fewerThan24bits != 0 ? numberTriplets+1 : numberTriplets;
        int      numberLines       = (numberQuartet-1)/19+1;
        char     encodedData[]     = null;

        encodedData = new char[numberQuartet*4+numberLines];

        byte k=0, l=0, b1=0,b2=0,b3=0;

        int encodedIndex = 0;
        int dataIndex   = 0;
        int i           = 0;
        if (fDebug) {
            System.out.println("number of triplets = " + numberTriplets );
        }

        for (int line = 0; line < numberLines-1; line++) {
            for (int quartet = 0; quartet < 19; quartet++) {
                b1 = binaryData[dataIndex++];
                b2 = binaryData[dataIndex++];
                b3 = binaryData[dataIndex++];

                if (fDebug) {
                    System.out.println( "b1= " + b1 +", b2= " + b2 + ", b3= " + b3 );
                }

                l  = (byte)(b2 & 0x0f);
                k  = (byte)(b1 & 0x03);

                byte val1 = ((b1 & SIGN)==0)?(byte)(b1>>2):(byte)((b1)>>2^0xc0);

                byte val2 = ((b2 & SIGN)==0)?(byte)(b2>>4):(byte)((b2)>>4^0xf0);
                byte val3 = ((b3 & SIGN)==0)?(byte)(b3>>6):(byte)((b3)>>6^0xfc);

                if (fDebug) {
                    System.out.println( "val2 = " + val2 );
                    System.out.println( "k4   = " + (k<<4));
                    System.out.println( "vak  = " + (val2 | (k<<4)));
                }

                encodedData[encodedIndex++] = lookUpBase64Alphabet[ val1 ];
                encodedData[encodedIndex++] = lookUpBase64Alphabet[ val2 | ( k<<4 )];
                encodedData[encodedIndex++] = lookUpBase64Alphabet[ (l <<2 ) | val3 ];
                encodedData[encodedIndex++] = lookUpBase64Alphabet[ b3 & 0x3f ];

                i++;
            }
            encodedData[encodedIndex++] = 0xa;
        }

        for (; i<numberTriplets; i++) {
            b1 = binaryData[dataIndex++];
            b2 = binaryData[dataIndex++];
            b3 = binaryData[dataIndex++];

            if (fDebug) {
                System.out.println( "b1= " + b1 +", b2= " + b2 + ", b3= " + b3 );
            }

            l  = (byte)(b2 & 0x0f);
            k  = (byte)(b1 & 0x03);

            byte val1 = ((b1 & SIGN)==0)?(byte)(b1>>2):(byte)((b1)>>2^0xc0);

            byte val2 = ((b2 & SIGN)==0)?(byte)(b2>>4):(byte)((b2)>>4^0xf0);
            byte val3 = ((b3 & SIGN)==0)?(byte)(b3>>6):(byte)((b3)>>6^0xfc);

            if (fDebug) {
                System.out.println( "val2 = " + val2 );
                System.out.println( "k4   = " + (k<<4));
                System.out.println( "vak  = " + (val2 | (k<<4)));
            }

            encodedData[encodedIndex++] = lookUpBase64Alphabet[ val1 ];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[ val2 | ( k<<4 )];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[ (l <<2 ) | val3 ];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[ b3 & 0x3f ];
        }

        // form integral number of 6-bit groups
        if (fewerThan24bits == EIGHTBIT) {
            b1 = binaryData[dataIndex];
            k = (byte) ( b1 &0x03 );
            if (fDebug) {
                System.out.println("b1=" + b1);
                System.out.println("b1<<2 = " + (b1>>2) );
            }
            byte val1 = ((b1 & SIGN)==0)?(byte)(b1>>2):(byte)((b1)>>2^0xc0);
            encodedData[encodedIndex++] = lookUpBase64Alphabet[ val1 ];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[ k<<4 ];
            encodedData[encodedIndex++] = PAD;
            encodedData[encodedIndex++] = PAD;
        } else if (fewerThan24bits == SIXTEENBIT) {
            b1 = binaryData[dataIndex];
            b2 = binaryData[dataIndex +1 ];
            l = ( byte ) ( b2 &0x0f );
            k = ( byte ) ( b1 &0x03 );

            byte val1 = ((b1 & SIGN)==0)?(byte)(b1>>2):(byte)((b1)>>2^0xc0);
            byte val2 = ((b2 & SIGN)==0)?(byte)(b2>>4):(byte)((b2)>>4^0xf0);

            encodedData[encodedIndex++] = lookUpBase64Alphabet[ val1 ];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[ val2 | ( k<<4 )];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[ l<<2 ];
            encodedData[encodedIndex++] = PAD;
        }

        encodedData[encodedIndex] = 0xa;
        
        return new String(encodedData);
    }

    /**
     * remove WhiteSpace from MIME containing encoded Base64 data.
     * 
     * @param data  the byte array of base64 data (with WS)
     * @return      the new length
     */
    protected static int removeWhiteSpace(char[] data) {
        if (data == null)
            return 0;

        // count characters that's not whitespace
        int newSize = 0;
        int len = data.length;
        for (int i = 0; i < len; i++) {
            if (!isWhiteSpace(data[i]))
                data[newSize++] = data[i];
        }
        return newSize;
    }
}
