
import java.io.*;

import java.nio.charset.StandardCharsets;

public class Main {

    private static int FRAMES = 0;
    private static final int HEADER = 10;
    private static final int ID_LENGTH = 4;


    public static void main(String[] args) throws IOException {

        File file = new File("D:/mp3tag/src/Death Grips - Lil Boy.mp3"); // Death Grips - Lil Boy, ACsoli, aa
        int fileLength = (int) file.length();

        System.out.println(file);

        // open file in "rw" read-write access mode
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");


        // with try(randomAccesFille) will work only "Death Grips - Lil Boy.mp3" file
        try (randomAccessFile) {

            // Will create a byte array of raf.length size that will hold our file
            byte[] bytes = new byte[(int) randomAccessFile.length()]; // for whole file

            // read bytes file, from position, up to fileLength
            //int bytesRead = randomAccessFile.read(bytes, 0, HEADER); /* array, offset, amount of bytes */


            // TAG HEADER -> 10 bytes
            System.out.println("------------------------");
            System.out.println("    TAG HEADER");
            System.out.println("------------------------");

            bytes = new byte[3];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "identifier: ", "identifier");

            bytes = new byte[2];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "version: ");

            bytes = new byte[1];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "flags: ");

            bytes = new byte[4];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "size: ");

            /*
             * An easy way of calculating the tag size is A*2^21+B*2^14+C*2^7+D,
             * where A is the first byte, B the second, C the third and D the fourth byte.
             */

            int tagHeaderSize = unpackSynchsafeInteger(bytes[0], bytes[1], bytes[2], bytes[3]);
            System.out.println("tag header size: " + tagHeaderSize + " bytes");

    /*
        The 'Size of padding' is simply the TOTAL TAG SIZE excluding the frames and the headers

        TOTAL TAG SIZE = tag header size // 610
        PADDING SIZE = TOTAL TAG SIZE - FRAMES - frameHEADERS // 610 - 264(all frames added) - 90(all header frames added) = 256 bytes
    */

            // FRAME HEADER
            System.out.println("------------------------");
            System.out.println("    FRAME HEADER");
            System.out.println("------------------------");

            bytes = new byte[4];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "ID: ", "frame_id");

            bytes = new byte[4];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "frame header size: "); // 8c - 140
            /* A*2^21+B*2^14+C*2^7+D */
            int tagSize = unpackInteger(bytes[0], bytes[1], bytes[2], bytes[3]);
            System.out.println("tag  size: " + tagSize + " bytes");

            bytes = new byte[2];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "flags: ");

            // FRAME BODY
            System.out.println("------------------------");
            System.out.println("    FRAME BODY");
            System.out.println("------------------------");

            bytes = new byte[1];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "encoding bytes: ");
            System.out.print("encoding: UTF 16\n");

            bytes = new byte[3];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "language: ", "language");

            bytes = new byte[18];
            randomAccessFile.readFully(bytes);
            // created a new string and store all bytes removing 0 bytes and negative bytes
            String hex = "";
            for (int k = 0; k < bytes.length; k++) {
                if (bytes[k] > 0) {
                    hex += UnicodeFormatter.byteToHex(bytes[k]); /* convert Byte -> Hex and store in a String */
                }
            }
            StringBuilder output = new StringBuilder();
            for (int i = 0; i < hex.length(); i += 2) {
                String str = hex.substring(i, i + 2);
                output.append((char) Integer.parseInt(str, 16));
            }
            printBytes(bytes, "description bytes: ");
            System.out.println("description: " + output);


            bytes = new byte[118];
            randomAccessFile.readFully(bytes);
            String comments = new String(bytes, StandardCharsets.UTF_16);
            printBytes(bytes, "comments hex: ", "to text");
            System.out.println("comments utf16: " + comments);

            // FRAME HEADER
            System.out.println("------------------------");
            System.out.println("    FRAME HEADER tpe1");
            System.out.println("------------------------");

            bytes = new byte[4];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "frame identifier: ", "TPE1");
            System.out.println("TPE1 - LEAD PERFORMER");

            bytes = new byte[4];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "frame header size: "); // [19] - 25
            bytes = new byte[2];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "frame flags: ");

            // FRAME BODY
            System.out.println("------------------------");
            System.out.println("    FRAME BODY");
            System.out.println("------------------------");

            bytes = new byte[1];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "encoding bytes: ");
            System.out.print("encoding: UTF 16\n");

            bytes = new byte[24];
            randomAccessFile.readFully(bytes);

            String hexS = "";
            for (int k = 0; k < bytes.length; k++) {
                if (bytes[k] > 0) {
                    hexS += UnicodeFormatter.byteToHex(bytes[k]); /* convert Byte -> Hex and store in a String */
                }
            }
            StringBuilder outputS = new StringBuilder();
            for (int i = 0; i < hexS.length(); i += 2) {
                String str = hexS.substring(i, i + 2);
                outputS.append((char) Integer.parseInt(str, 16)); /* convert String -> UTF-16*/
            }
            printBytes(bytes, "description bytes: ");
            System.out.println("description: " + outputS);

            // FRAME HEADER
            System.out.println("------------------------");
            System.out.println("    FRAME HEADER");
            System.out.println("------------------------");

            bytes = new byte[4];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "frame identifier: ", "TALB");
            System.out.println("TALB - ALBUM TITLE");

            bytes = new byte[4];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "frame header size: "); // 31


            bytes = new byte[2];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "flags: ");

            // FRAME BODY
            System.out.println("------------------------");
            System.out.println("    FRAME BODY");
            System.out.println("------------------------");

            bytes = new byte[1];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "encoding: ");

            bytes = new byte[30];
            randomAccessFile.readFully(bytes);
            String hexZ = "";
            for (int k = 0; k < bytes.length; k++) {
                if (bytes[k] > 0) {
                    hexZ += UnicodeFormatter.byteToHex(bytes[k]); /* convert Byte -> Hex and store in a String */
                }
            }
            StringBuilder outputZ = new StringBuilder();
            for (int i = 0; i < hexZ.length(); i += 2) {
                String str = hexZ.substring(i, i + 2);
                outputZ.append((char) Integer.parseInt(str, 16)); /* convert String -> UTF-16*/
            }
            printBytes(bytes, "description bytes: ");
            System.out.println("description: " + outputZ);

            // FRAME HEADER
            System.out.println("------------------------");
            System.out.println("    FRAME HEADER");
            System.out.println("------------------------");

            bytes = new byte[4];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "frame identifier: ", "TIT2");
            System.out.println("TIT2 - SONG TITLE");

            bytes = new byte[4];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "frame header size: "); // 1b = 27

            bytes = new byte[2];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "flags: ");

            // FRAME BODY
            System.out.println("------------------------");
            System.out.println("    FRAME BODY");
            System.out.println("------------------------");

            bytes = new byte[1];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "encoding: ");

            bytes = new byte[26];
            randomAccessFile.readFully(bytes);
            String hexX = "";
            for (int k = 0; k < bytes.length; k++) {
                if (bytes[k] > 0) {
                    hexX += UnicodeFormatter.byteToHex(bytes[k]); /* convert Byte -> Hex and store in a String */
                }
            }
            StringBuilder outputX = new StringBuilder();
            for (int i = 0; i < hexX.length(); i += 2) {
                String str = hexX.substring(i, i + 2);
                outputX.append((char) Integer.parseInt(str, 16)); /* convert String -> UTF-16*/
            }
            printBytes(bytes, "description bytes: ");
            System.out.println("description: " + outputX);

            // FRAME HEADER
            System.out.println("------------------------");
            System.out.println("    FRAME HEADER");
            System.out.println("------------------------");

            bytes = new byte[4];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "frame identifier: ", "TLEN");
            System.out.println("TLEN - LENGTH - length of the song (in ms)");

            bytes = new byte[4];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "frame header size: "); // 08 -> 8

            bytes = new byte[2];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "flags: ");

            // FRAME BODY
            System.out.println("------------------------");
            System.out.println("    FRAME BODY");
            System.out.println("------------------------");

            bytes = new byte[1];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "encoding: ");
            System.out.println("encoding: ISO-8859-1");

            bytes = new byte[6];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "flags: ", "f");

            bytes = new byte[1];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "last byte because Terminated strings are terminated with $00 if encoded with ISO-8859-1 and $00 00 if encoded as unicode: ");


            // FRAME HEADER
            System.out.println("------------------------");
            System.out.println("    FRAME HEADER");
            System.out.println("------------------------");

            bytes = new byte[4];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "frame identifier: ", "TPE2");
            System.out.println("TPE2 - BAND");

            bytes = new byte[4];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "frame header size: "); // 0f -> 15

            bytes = new byte[2];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "flags: ");


            // FRAME BODY
            System.out.println("------------------------");
            System.out.println("    FRAME BODY");
            System.out.println("------------------------");

            bytes = new byte[1];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "encoding: ");

            bytes = new byte[14];
            randomAccessFile.readFully(bytes);
            String hexC = "";
            for (int k = 0; k < bytes.length; k++) {
                if (bytes[k] > 0) {
                    hexC += UnicodeFormatter.byteToHex(bytes[k]); /* convert Byte -> Hex and store in a String */
                }
            }
            StringBuilder outputC = new StringBuilder();
            for (int i = 0; i < hexC.length(); i += 2) {
                String str = hexC.substring(i, i + 2);
                outputC.append((char) Integer.parseInt(str, 16)); /* convert String -> UTF-16*/
            }
            printBytes(bytes, "description bytes: ");
            System.out.println("description: " + outputC);

            // FRAME HEADER
            System.out.println("------------------------");
            System.out.println("    FRAME HEADER");
            System.out.println("------------------------");

            bytes = new byte[4];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "frame identifier: ", "TRCK");
            System.out.println("TRCK - TRACK NUMBER");

            bytes = new byte[4];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "frame header size: "); // 03 -> 3

            bytes = new byte[2];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "flags: ");

            // FRAME BODY
            System.out.println("------------------------");
            System.out.println("    FRAME BODY");
            System.out.println("------------------------");

            bytes = new byte[1];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "encoding: ");
            System.out.println("encoding: ISO-8859-1");

            bytes = new byte[1];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "flags: ", "f");

            bytes = new byte[1];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "last byte for ISO and UNICODE: ");

            // FRAME HEADER
            System.out.println("------------------------");
            System.out.println("    FRAME HEADER");
            System.out.println("------------------------");

            bytes = new byte[4];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "frame identifier: ", "TYER");
            System.out.println("TYER - YEAR");

            bytes = new byte[4];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "frame header size: "); // 06 -> 6

            bytes = new byte[2];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "flags: ");

            // FRAME BODY
            System.out.println("------------------------");
            System.out.println("    FRAME BODY");
            System.out.println("------------------------");

            bytes = new byte[1];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "encoding: ");
            System.out.println("encoding: ISO-8859-1");

            bytes = new byte[4];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "flags: ", "f");

            bytes = new byte[1];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "last byte for ISO and UNICODE: ");

            // FRAME HEADER
            System.out.println("------------------------");
            System.out.println("    FRAME HEADER");
            System.out.println("------------------------");

            bytes = new byte[4];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "frame identifier: ", "TSIZ");
            System.out.println("TSIZ - SIZE");

            // audio size tag, not mp3 file size !!

            bytes = new byte[4];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "frame header size: "); // 09 -> 9

            bytes = new byte[2];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "flags: ");

            // FRAME BODY
            System.out.println("------------------------");
            System.out.println("    FRAME BODY");
            System.out.println("------------------------");

            bytes = new byte[1];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "encoding: ");
            System.out.println("encoding: ISO-8859-1");

            bytes = new byte[7];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "flags: ", "f");

            bytes = new byte[1];
            randomAccessFile.readFully(bytes);
            printBytes(bytes, "last byte for ISO and UNICODE: ");

            // FRAME HEADER
            System.out.println("------------------------");
            System.out.println("    FRAME PADDING");
            System.out.println("------------------------");

            bytes = new byte[256];
            randomAccessFile.readFully(bytes);
            //printBytes(bytes, "padding: ");
            System.out.println("padding: always 256 bytes for ID3v2.30");

            int count = 0;
            for (int k = 0; k < bytes.length; k++) {
                if (bytes[k] == 0) {
                    System.out.print(UnicodeFormatter.byteToHex(bytes[k]));
                    count++;
                }
            }
            System.out.println("\n -" + count + "- ");

            System.out.println("\n\n\nfileLength: " + fileLength);
        }

        System.out.println("------------------------------------------------------------------------------------");

        RandomAccessFile raf = new RandomAccessFile(file, "rw");

        try (raf){
            // Will create a byte array of raf.length size that will hold our file
            byte[] bytes = new byte[(int) raf.length()]; // for whole file

            // read bytes file, from position, up to fileLength
            int bytesRead = raf.read(bytes, 0, bytes.length); /* array, offset, amount of bytes */


                while (bytesRead == bytes.length) {

                    StringBuffer hexString = new StringBuffer();

                    /* convert all bytes into String with hex values */
                    for (int k = 0; k < bytes.length; k++) {
                        //System.out.print("[" + UnicodeFormatter.byteToHex(bytes[k]) + "]");
                        hexString.append(UnicodeFormatter.byteToHex(bytes[k]));

                        bytesRead++;
                    }
                    System.out.println("");

                    /* General Tag Header Condition */
                    if (UnicodeFormatter.byteToHex(bytes[0]).equals("49")
                            && UnicodeFormatter.byteToHex(bytes[1]).equals("44")
                            && UnicodeFormatter.byteToHex(bytes[2]).equals("33")){

                        System.out.println("ID3v2 tag found!");

                        /*
                        HEADER TAG
                         */
                        // ID3v2 version
                        System.out.println("Version: " + Integer.parseInt(UnicodeFormatter.byteToHex(bytes[3])) + "." + UnicodeFormatter.byteToHex(bytes[4]));

                        // Check for flags
                        if (UnicodeFormatter.byteToHex(bytes[5]).equals("00")){
                            System.out.println("Flags: NO");
                        } else {
                            System.out.println("Flags: YES");
                        }

                        // Calculate the size of header tag
                        int tagHeaderSize = unpackSynchsafeInteger(bytes[6], bytes[7], bytes[8], bytes[9]) + 10;
                        System.out.println("Tag header size: " + tagHeaderSize + " bytes\n");


                    } else {
                        System.out.println("There is another version of ID3 rather than ID3v2");
                    } // end IF ELSE GENERAL CONDITION    ID3v2 Tag

                    int startPosition = 10;

                    checkHeader(hexString, bytes, bytesRead, startPosition);
                    System.out.println("Number of frames: " + FRAMES);

                } //end WHILE

        } //end TRY

    } //end MAIN

    public static void checkHeader(StringBuffer hexString, byte[] bytes, int bytesRead, int n) throws UnsupportedEncodingException {

        for (int k = HEADER; k < HEADER + n; k++) {
            //System.out.print("[" + UnicodeFormatter.byteToHex(bytes[k]) + "]");
            //hexString.append(UnicodeFormatter.byteToHex(bytes[k]));

            bytesRead++;
        }

        String tagString = new String(bytes, n, ID_LENGTH, "UTF-8");
        System.out.println("\n" + tagString);

        int headerSize = unpackInteger(bytes[n + ID_LENGTH], bytes[n + ID_LENGTH + 1], bytes[n + ID_LENGTH + 2], bytes[n + ID_LENGTH + 3]);
        System.out.println("Tag " + tagString + " size: " + headerSize + " bytes");


        if (headerSize != 0){

            /* print tag description */
            checkTag(tagString);

            /* check tag ID and prints the frame body */
            checkTagString(tagString, hexString, bytes, n, headerSize);

            n += headerSize;

            checkHeader(hexString, bytes, bytesRead, n + HEADER);
            FRAMES++;
        } else {
            System.out.println("----------------------");
        }
    }

    public static void checkTagString(String tagString, StringBuffer encString, byte[] bytes, int offset, int size) throws UnsupportedEncodingException {

        String encoding = String.valueOf(bytes[offset+10]);

        // check tagString
        if (tagString.charAt(0) == 'T'){
            //System.out.println("Txxx tag found");

            if (encoding.equals("1")){
                String content = new String(bytes, (offset + HEADER + 1), size-2, "UTF-16");
                System.out.println(content);
            } else if (encoding.equals("0")){
                String content = new String(bytes, (offset + HEADER + 1), size-2, "ISO-8859-1");
                System.out.println(content);
            } else {
                System.out.println("Unknown encoding type");
            }


        } else if (tagString.charAt(0) == 'C'){
            //System.out.println("Cxxx tag found");

            if (encoding.equals("1")){
                String comment = new String(bytes, (offset + HEADER + 4), size-4, "UTF-16");
                System.out.println(comment);
            } else if (encoding.equals("0")){
                String comment = new String(bytes, (offset + HEADER + 4), size-4, "UTF-8");
                System.out.println(comment);
            } else {
                System.out.println("Unknown encoding type");
            }

            String lang = new String(bytes, (offset+HEADER+1), 3, "UTF-8");
            System.out.println("Language: " + lang);
        }

    }

    public static void checkTag(String tagHeader){

        switch (tagHeader){
            case "AENC":
                System.out.println("Audio encryption");
                break;
            case "APIC":
                System.out.println("Attached picture");
                break;
            case "COMM":
                System.out.println("Comments");
                break;
            case "COMR":
                System.out.println("Commercial frame");
                break;
            case "ENCR":
                System.out.println("Encryption method registration");
                break;
            case "EQUA":
                System.out.println("Equalization");
                break;
            case "ETCO":
                System.out.println("Event timing codes");
                break;
            case "GEOB":
                System.out.println("General encapsulated object");
                break;
            case "GRID":
                System.out.println("Group identification registration");
                break;
            case "IPLS":
                System.out.println("Involved people list");
                break;
            case "LINK":
                System.out.println("Linked information");
                break;
            case "MCDI":
                System.out.println("Music CD identifier");
                break;
            case "MLLT":
                System.out.println("MPEG location lookup table");
                break;
            case "OWNE":
                System.out.println("Ownership frame");
                break;
            case "PRIV":
                System.out.println("Private frame");
                break;
            case "PCNT":
                System.out.println("Play counter");
                break;
            case "POPM":
                System.out.println("Popularimeter");
                break;
            case "POSS":
                System.out.println("Position synchronisation frame");
                break;
            case "RBUF":
                System.out.println("Recommended buffer size");
                break;
            case "RVAD":
                System.out.println("Relative volume adjustment");
                break;
            case "RVRB":
                System.out.println("Reverb");
                break;
            case "SYLT":
                System.out.println("Synchronized lyric/text");
                break;
            case "SYTC":
                System.out.println("Synchronized tempo codes");
                break;
            case "TALB":
                System.out.println("Album/Movie/Show title");
                break;
            case "TBPM":
                System.out.println("BPM (beats per minute)");
                break;
            case "TCOM":
                System.out.println("Composer");
                break;
            case "TCON":
                System.out.println("Content type");
                break;
            case "TCOP":
                System.out.println("Copyright message");
                break;
            case "TDAT":
                System.out.println("Date");
                break;
            case "TDLY":
                System.out.println("Playlist delay");
                break;
            case "TENC":
                System.out.println("Encoded by");
                break;
            case "TEXT":
                System.out.println("Lyricist/Text writer");
                break;
            case "TFLT":
                System.out.println("File type");
                break;
            case "TIME":
                System.out.println("Time");
                break;
            case "TIT1":
                System.out.println("Content group description");
                break;
            case "TIT2":
                System.out.println("Title/songname/content description");
                break;
            case "TIT3":
                System.out.println("Subtitle/Description refinement");
                break;
            case "TKEY":
                System.out.println("Initial key");
                break;
            case "TLAN":
                System.out.println("Language(s)");
                break;
            case "TLEN":
                System.out.println("Length");
                break;
            case "TMED":
                System.out.println("Media type");
                break;
            case "TOAL":
                System.out.println("riginal album/movie/show title");
                break;
            case "TOFN":
                System.out.println("Original filename");
                break;
            case "TOLY":
                System.out.println("Original lyricist(s)/text writer(s)");
                break;
            case "TOPE":
                System.out.println("Original artist(s)/performer(s)");
                break;
            case "TORY":
                System.out.println("Original release year");
                break;
            case "TOWN":
                System.out.println("File owner/licensee");
                break;
            case "TPE1":
                System.out.println("Lead performer(s)/Soloist(s)");
                break;
            case "TPE2":
                System.out.println("Band/orchestra/accompaniment");
                break;
            case "TPE3":
                System.out.println("Conductor/performer refinement");
                break;
            case "TPE4":
                System.out.println("Interpreted, remixed, or otherwise modified by");
                break;
            case "TPOS":
                System.out.println("Part of a set");
                break;
            case "TPUB":
                System.out.println("Publisher");
                break;
            case "TRCK":
                System.out.println("Track number/Position in set");
                break;
            case "TRDA":
                System.out.println("Recording dates");
                break;
            case "TRSN":
                System.out.println("Internet radio station name");
                break;
            case "TRSO":
                System.out.println("Internet radio station owner");
                break;
            case "TSIZ":
                System.out.println("Size");
                break;
            case "TSRC":
                System.out.println("ISRC (international standard recording code)");
                break;
            case "TSSE":
                System.out.println("Software/Hardware and settings used for encoding");
                break;
            case "TYER":
                System.out.println("Year");
                break;
            case "TXXX":
                System.out.println("User defined text information frame");
                break;
            case "UFID":
                System.out.println("Unique file identifier");
                break;
            case "USER":
                System.out.println("Terms of use");
                break;
            case "USLT":
                System.out.println("Unsychronized lyric/text transcription");
                break;
            case "WCOM":
                System.out.println("Commercial information");
                break;
            case "WCOP":
                System.out.println("Copyright/Legal information");
                break;
            case "WOAF":
                System.out.println("Official audio file webpage");
                break;
            case "WOAR":
                System.out.println("Official artist/performer webpage");
                break;
            case "WOAS":
                System.out.println("Official audio source webpage");
                break;
            case "WORS":
                System.out.println("Official internet radio station homepage");
                break;
            case "WPAY":
                System.out.println("Payment");
                break;
            case "WPUB":
                System.out.println("Publishers official webpage");
                break;
            case "WXXX":
                System.out.println("User defined URL link frame");
                break;
            default: System.out.println("Unknown ID3v2 frame");
        }

    }


    //============================================
    public static int unpackSynchsafeInteger(byte b1, byte b2, byte b3, byte b4) {
        int value = ((byte)(b4 & 0x7f));
        value += shiftByte((byte)(b3 & 0x7f), -7);
        value += shiftByte((byte)(b2 & 0x7f), -14);
        value += shiftByte((byte)(b1 & 0x7f), -21);
        return value;
    }

    public static int unpackInteger(byte b1, byte b2, byte b3, byte b4) {
        int value = b4 & 0xff;
        value += shiftByte(b3, -8);
        value += shiftByte(b2, -16);
        value += shiftByte(b1, -24);
        return value;
    }

    public static int shiftByte(byte c, int places) {
        int i = c & 0xff;
        if (places < 0) {
            return i << -places;
        } else if (places > 0) {
            return i >> places;
        }
        return i;
    }

    public static void printBytes(byte[] array, String name) {
        System.out.print(name);
        for (int k = 0; k < array.length; k++) {
            System.out.print("[" + UnicodeFormatter.byteToHex(array[k]) + "]");
        }
        System.out.println("");
    }

    public static void printBytes(byte[] array, String name_hex, String name_str) {
        System.out.print(name_hex);

        name_str = new String(array);

        for (int k = 0; k < array.length; k++) {
            System.out.print("[" + UnicodeFormatter.byteToHex(array[k]) + "]");
        }
        System.out.print(" => " + name_str + "\n");
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static void packSynchsafeInteger(int i, byte[] bytes, int offset) {
        bytes[offset + 3] = (byte) (i & 0x7f);
        bytes[offset + 2] = (byte) ((i >> 7) & 0x7f);
        bytes[offset + 1] = (byte) ((i >> 14) & 0x7f);
        bytes[offset + 0] = (byte) ((i >> 21) & 0x7f);
    }
}

class UnicodeFormatter  {

    static public String byteToHex(byte b) {
        // Returns hex String representation of byte b
        char hexDigit[] = {
                '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
        };
        char[] array = { hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f] };
        return new String(array);
    }

    static public String charToHex(char c) {
        // Returns hex String representation of char c
        byte hi = (byte) (c >>> 8);
        byte lo = (byte) (c & 0xff);
        return byteToHex(hi) + byteToHex(lo);
    }

} // class