package com.starsoft.core.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public final class GZipUtil {

    /**
     * Do a gzip operation.
     */
    public static byte[] gzip(byte[] data) {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream(10240);
        GZIPOutputStream output = null;
        try {
            output = new GZIPOutputStream(byteOutput);
            output.write(data);
        }
        catch (IOException e) {}
        finally {
            if(output!=null) {
                try {
                    output.close();
                }
                catch (IOException e) {}
            }
        }
        return byteOutput.toByteArray();
    }

}
