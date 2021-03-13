import sun.lwawt.macosx.CSystemTray;
import utils.AnalysisTemplate;
import utils.LoadConfig;
import utils.TxTDataReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
import java.util.*;

public class TAFileBuilder {
    public String folderName;
    public String batchRunDate;
 // public String fileCode;
    public String fileVersion;
    public String aggregationNumber;
    public String senderName;
    public String receiverName;
    public String dataFileStartCode;
    public String indexFileStartCode;
    public String indexFileEndCode;
    public List<String> outputFileName;
    public List<String> outputPath =  new ArrayList<>();

    // title数量开始的位置，为固定的，所以写死
    private static final int TITLE_COUNT_INDEX = 9;
    public TAFileBuilder(String folderName, String date) throws IOException {
        this.folderName = folderName;
        this.batchRunDate = date;
        this.dataFileStartCode = "OFDCFDAT";
        this.fileVersion = LoadConfig.Load("pdfVersion");
        this.aggregationNumber = "000";
        this.indexFileStartCode = "OFDCFDAT";
        this.indexFileEndCode = "OFDCFEND";
        this.senderName = "";
        this.receiverName = "";
    }

    public void build(String[] fileTypes) throws IOException {
        TxTDataReader txtDataReader = new TxTDataReader();
        System.out.println("senderName:"+senderName);
        //build file
        String fileCreationDate = batchRunDate;
        outputFileName = txtDataReader.getOutputFileName(fileCreationDate, folderName, fileTypes);
        TxTDataReader txtReader = new TxTDataReader();
           //Get input file content
        List<List<String>> data = txtReader.getInputFileContent();
        System.out.println(data);
        this.senderName = txtDataReader.reciverCode;
        this.receiverName = txtDataReader.senderCode;
        populateOFDFile(fileCreationDate, fileTypes, data);

    }

    private void populateOFDFile(String fileCreationDate, String[] fileType, List<List<String>> data) throws IOException {
        String outDir;
        try {
            outDir = LoadConfig.Load("output_dir");
        } catch (IOException err) {
            outDir = "src/main/resources/";
        }
        List<String> outputDirPath = new ArrayList<String>();
        for (int i = 0; i < fileType.length; i++) {
            outputDirPath.add(Paths.get(outDir, "output", fileCreationDate, LoadConfig.Load("TestScenarios"), fileType[i]).toString());
            new File(outputDirPath.get(i)).mkdirs();
            outputPath.add(Paths.get(outputDirPath.get(i), outputFileName.get(i)).toString());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath.get(i)),
                    "GBK"));
            writeHeader(writer, fileCreationDate, fileType[i]);
            List<List<String>> content = parseContent(data.get(i));
            content.forEach(c -> {
                try {
                    writeContent(writer, c.get(0), c.get(1));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            writer.close();
        }

    }

    private List<List<String>> parseContent(List<String> data) throws IOException {
        AnalysisTemplate templateFile = new AnalysisTemplate();
        List<List<String>> retContent = templateFile.getInputDefinitionData();
        System.out.println("------------retContent:"+retContent);
        //String date = "20201231";
        int titleCount = Integer.parseInt(data.get(TITLE_COUNT_INDEX));
        // 总共有几条数据
        int dataLength = Integer.parseInt(data.get(titleCount + TITLE_COUNT_INDEX + 1));
        // 数据起始index
        int startIndex = titleCount + TITLE_COUNT_INDEX + 2;
        Map<String, Integer> rules = parseRule();

        for (int i = 0; i < dataLength; i++) {
            String wholeContent = data.get(startIndex + i);
            String AppSheetSerialNo = wholeContent.substring(rules.get("AppSheetSerialNo_start"),
                    rules.get("AppSheetSerialNo_end"));
            retContent.add(Arrays.asList(AppSheetSerialNo, batchRunDate));
        }
        return retContent;
    }
    // 返回解析到的CSV规则
    // TODO: 先mock 后续需要补充内容
    private Map<String, Integer> parseRule() {
        Map<String, Integer> rules = new HashMap<>();
        // 字段起始位置
        rules.put("AppSheetSerialNo_start", 15);
        // 字段结束位置
        rules.put("AppSheetSerialNo_end", 30);
        return rules;
    }

    private void writeHeader(BufferedWriter writer, String fileCreationDate, String fileType) throws IOException {
        writeWithNewLine(writer, dataFileStartCode);
        writeWithNewLine(writer, fileVersion);
        writeWithNewLine(writer, senderName);
        writeWithNewLine(writer, receiverName);
        writeWithNewLine(writer, fileCreationDate);
        writeWithNewLine(writer, aggregationNumber);
        writeWithNewLine(writer, fileType);
        writeWithNewLine(writer, "");
        writeWithNewLine(writer, "AppSheetSerialNo");
        writeWithNewLine(writer, "TransactionCfmDate");
        writer.flush();
    }

    private void writeContent(BufferedWriter writer, String AppSheetSerialNo, String TransactionCfmDate) throws IOException {
        writer.write(AppSheetSerialNo);
        writer.write(" ");
        writer.write(TransactionCfmDate);
        writer.write("\n");
        writer.flush();
    }

    private void writeWithNewLine(BufferedWriter writer, String code) throws IOException {
        writer.write(code);
        writer.write('\n');
    }

    private List<String> readFields(String fileType) {
        //read field from fieldDefinitions fields.txt
        return null;
    }
}
