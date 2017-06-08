import org.apache.commons.io.IOUtils;
import sun.nio.cs.StandardCharsets;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by vivek on 08/06/2017.
 */
public class Util {

    public static String getFileNameContainsZip(ZipFile zfil,String filename) throws IOException{
        Enumeration<? extends ZipEntry> zfs = zfil.entries();
        while(zfs.hasMoreElements()){
            ZipEntry zf = zfs.nextElement();
            if(zf.getName().contains(filename)){
                return IOUtils.toString(zfil.getInputStream(zf), Charset.forName("utf-8"));
            }
        }
        return null;
    }



    public static String getFileNameContainsZip(ZipInputStream zfil,String filename) throws IOException{
        ZipEntry zf =zfil.getNextEntry();
        while(zf!=null){
            if(zf.getName().contains(filename)){
                return IOUtils.toString(zfil, Charset.forName("utf-8"));
            }
            zf =zfil.getNextEntry();
        }
        return null;
    }


}
