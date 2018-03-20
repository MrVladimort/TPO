package zad1;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.*;

public class Futil extends SimpleFileVisitor  {
    public static void processDir(String dirName, String resultFileName) {
        FileChannel resultChannel;
        Charset decode, code;
        
        try {
            resultChannel = FileChannel.open(Paths.get(resultFileName), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            decode = Charset.forName("Cp1250");
            code = Charset.forName("UTF-8");

            Files.walk(Paths.get(dirName)).filter(Files::isRegularFile).forEach((Path n)->{
                try {
                    FileChannel readChannel = FileChannel.open(n, StandardOpenOption.READ);
                    ByteBuffer byteBuffer = ByteBuffer.allocate((int)readChannel.size());
                    readChannel.read(byteBuffer);
                    byteBuffer.flip();

                    CharBuffer charBuffer = decode.decode(byteBuffer);
                    byteBuffer = code.encode(charBuffer);

                    resultChannel.write(byteBuffer);
                    readChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            resultChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}