import utils.LoadConfig;
import utils.TxTDataReader;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public void build(String[] fileTypes) throws IOException {

        TxTDataReader txtDataReader = new TxTDataReader();
        //build file
        String fileCreationDate = batchRunDate;
        for(String fileType : fileTypes){
            populateOFDFile(fileCreationDate, fileType);
            String outputFileName = txtDataReader.getOutputFileName(batchRunDate, folderName, fileType);
            List list = txtDataReader.getInputContent();
            System.out.println(Arrays.toString(list.toArray()));
        }
    }

    private void populateOFDFile(String fileCreationDate, String fileType) throws IOException {
        String filename = String.format("OFD_%s_%s_%s_%s.TXT", fileReciverCode, fileCode, fileCreationDate, fileType);
        String outDir;
        try {
            outDir = LoadConfig.Load("output_dir");
        } catch (IOException err) {
            outDir = "src/main/resources/";
        }
        String outputDirPath = Paths.get(outDir, "output", fileCreationDate, fileCode).toString();
        new File(outputDirPath).mkdirs();
        String outputPath = Paths.get(outputDirPath, filename).toString();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath),
                "GBK"));
        writeHeader(writer, fileCreationDate, fileType);

        List<String> content = readContentFromInputFile();
        writeContent(writer, content.get(0), content.get(1));
        writer.close();
    }

    // TODO: 先mock 后续需要补充内容
    private List<String> readContentFromInputFile() {
        return Arrays.asList("12345678  ", "20201231");
    }

    private void writeHeader(BufferedWriter writer, String fileCreationDate, String fileType) throws IOException {
        writeWithNewLine(writer, dataFileStartCode);
        writeWithNewLine(writer, fileVersion);
        writeWithNewLine(writer, fileCode);
        writeWithNewLine(writer, fileReciverCode);
        writeWithNewLine(writer, fileCreationDate);
        writeWithNewLine(writer, aggregationNumber);
        writeWithNewLine(writer, fileType);
        writeWithNewLine(writer, "");
        writer.flush();
    }

    private void writeContent(BufferedWriter writer, String AppSheetSerialNo, String TransactionCfmDate) throws IOException {
        writeWithNewLine(writer,"AppSheetSerialNo");
        writeWithNewLine(writer, "TransactionCfmDate");

        writer.write(AppSheetSerialNo);
        writer.write(TransactionCfmDate);
        writer.flush();
    }

    private void writeWithNewLine(BufferedWriter writer, String code) throws IOException {
        writer.write(code);
        writer.write('\n');
    }

    private ArrayList<String> readFields(String fileType) {
        //read field from fieldDefinitions fields.txt
        return null;
    }
}
