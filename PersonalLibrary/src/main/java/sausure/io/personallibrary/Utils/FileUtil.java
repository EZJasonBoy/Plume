package sausure.io.personallibrary.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import retrofit.client.Response;

/**
 * Created by JOJO on 2015/9/7.
 */
public class FileUtil
{
    public  static void saveFile(File file, byte[] bytes)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void saveFile(File file,Response response)
    {
        try
        {
            FileUtil.saveFile(file, FileUtil.getBytesFromStream(response.getBody().in()));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static byte[] getBytesFromStream(InputStream is) throws IOException
    {
        int len;
        int size = 1024;
        byte[] buf;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        buf = new byte[size];
        while((len = is.read(buf, 0, size)) != -1)
        {
            bos.write(buf, 0, len);
        }
        buf = bos.toByteArray();

        return buf;
    }
}
