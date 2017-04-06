package utils;

import javax.tools.SimpleJavaFileObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

/**
 * Created by Petec on 6.11.2016.
 */
public class FileJavaSource extends SimpleJavaFileObject {
    private File file;


    public FileJavaSource(File file) {
        super(file.toURI(),Kind.SOURCE);
        this.file = file;
    }

    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileChannel fc = fis.getChannel();

        // Create a read-only CharBuffer on the file
        ByteBuffer bbuf = null;
        try {
            bbuf = fc.map(FileChannel.MapMode.READ_ONLY, 0,
                    (int) fc.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        CharBuffer cbuf = null;
        try {
            cbuf = Charset.forName("UTF-8").newDecoder().decode(bbuf);
        } catch (CharacterCodingException e) {
            e.printStackTrace();
        }
        return cbuf;
    }
}
