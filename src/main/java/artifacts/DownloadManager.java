package artifacts;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class DownloadManager {

    public byte[] DownloadFile(String  url) throws IOException {
        ReadableByteChannel byteChannel = Channels.newChannel(new URL(url).openStream());
        InputStream inputStream = Channels.newInputStream(byteChannel);
        return readFully(inputStream);
    }

    public static byte[] readFully(InputStream input) throws IOException {
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        return output.toByteArray();
    }
}
