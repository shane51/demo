import utils.TxTDataReader;
import utils.FieldDefinitionsReader;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class TAFileBuilder{
    public String folderName;
    public String batchRunDate;
    public String fileCode;
    public String fileVersion;
    public String aggregationNumber;
    public String senderName;
    public String receiverName;
    public String dataFileStartCode;
    public String indexFileStartCode;
    public String indexFileEndCode;
    public String fileReciverCode;
    public TAFileBuilder(String folderName,String date,String fileCode){
        this.folderName = folderName;
        this.batchRunDate = date;
        this.dataFileStartCode = "OFDCFDAT";
        this.fileVersion = "22";
        this.fileReciverCode = "F6";
        this.fileCode = fileCode;
        this.aggregationNumber = "000";
        this.indexFileStartCode = "OFDCFDAT";
        this.indexFileEndCode = "OFDCFEND";
        this.senderName = "";
        this.receiverName = "";
    }
    public void build(String[] fileTypes) throws FileNotFoundException {
        //build file
        String fileCreationDate = batchRunDate;
        for(String fileType : fileTypes){
            populateOFDFile(fileCreationDate, fileType);
        }
        //populatOFIFile in here
    }

    private void populateOFDFile(String fileCreationDate, String fileType) throws FileNotFoundException {

        String filename = String.format("OFD_%s_%s_s_%s.TXT",fileReciverCode,fileReciverCode,fileCreationDate,fileType);
        TxTDataReader.read(batchRunDate,  folderName, fileType);
    }
//        ArrayList<String> fieldsName = readFields(fileType);
//        ArrayList<HashMap<String, String>> data;
//        try {
//            data = TxTDataReader.read(String batchRunDate, String folderName, String fileType);
//        }catch (FileNotFoundException){
//            data = new ArrayList<>();
//        }
//        HashMap<String, HashMap<String,String>> fieldDefinitions = FieldDefinitionsReader.readFieldDefinitions(fileCode);
//        HashMap<String, Integer> fieldLengthMap = FieldDefinitionsReader.getFieldLength(fieldDefinitions);
//        HashMap<String, Integer> fieldPrecisionMap = FieldDefinitionsReader.fieldPrecisionMap(fieldDefinitions);
//        String outputDirPath = Paths.get(System.getProperty("user,dir"),"output",fileCreationDate,fileCode).toString();
//        new File(outputDirPath).mkdirs();
//        String outputPath = Paths.get(outputDirPath,filename).toString();
//        try (BufferedWriter writer = new BufferedWriter(
//            new OutputStreamWriter(new FileOutputStream(outputPath), "GBK"))){
//            writeWithNewLine(writer,dataFileStartCode);
//            writeWithNewLine(writer,fileVersion);
//            writeWithNewLine(writer,fileCode);
//            writeWithNewLine(writer,fileReciverCode);
//            writeWithNewLine(writer,fileCreationDate);
//            writeWithNewLine(writer,aggregationNumber);
//            writeWithNewLine(writer,fileType);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void writeWithNewLine(BufferedWriter writer, String dataFileStartCode) {
//    }
//
//    private ArrayList<String> readFields(String fileType) {
//        //read field from fieldDefinitions fields.txt}
}
