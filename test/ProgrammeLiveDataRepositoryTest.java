import static org.junit.Assert.*;
import org.apache.commons.io.*;
import org.junit.*;
import java.io.*;
import java.net.*;

public class ProgrammeLiveDataRepositoryTest {
    @Test
    public void renameFiles() {
        for(int j = 0; j < 3 ; j++){
            File dir1 = new File("src/XML/" + j);
            File[] fileList1 = dir1.listFiles();
            for(int k = 0; k < fileList1.length; k++){
                String filename = fileList1[k].getName();
                int newFileNumber = j - 1;
                fileList1[k].renameTo(new File("src/XML/" + newFileNumber + "/" + filename));
            }
        }
    }
    @Test
    public void deleteFiles() {
        File dir = new File("src/XML/-1");
        File[] fileList = dir.listFiles();
        for(int j = 0; j < fileList.length ; j ++){
            fileList[j].delete();
        }
    }

    @Test
    public void getFiles() throws java.io.IOException,java.net.MalformedURLException{
        File bbc1 = new File("src/XML/2/bbc1.xml");
        FileUtils.copyURLToFile(new URL("http://bleb.org/tv/data/listings/" + 2 + "/bbc1.xml"),bbc1);
    }
}