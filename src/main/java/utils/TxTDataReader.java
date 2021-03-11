package utils;

import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;

public class TxTDataReader {

    public static void read(String batchRunDate, String folderName, String fileType) throws FileNotFoundException {
        //Generate output file name
        String path = "src/main/resources/inputFiles" + batchRunDate + folderName + fileType;
        File file = new File(path);
        String outputFileName = file.getName();
        System.out.println(outputFileName);
    }
}
