import utils.LoadConfig;
import utils.TxTDataReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TAFileBuilder {
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

    // title数量开始的位置，为固定的，所以写死
    private static final int TITLE_COUNT_INDEX = 9;

    public TAFileBuilder(String folderName, String date, String fileCode) {
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
        for (String fileType : fileTypes) {
            String outputFileName = txtDataReader.getOutputFileName(batchRunDate, folderName, fileType);
            List<String> data = txtDataReader.getInputContent();
            System.out.println(data);
            populateOFDFile(fileCreationDate, fileType, data);
//            System.out.println(Arrays.toString(data.toArray()));
        }
    }

    private void populateOFDFile(String fileCreationDate, String fileType, List<String> data) throws IOException {
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
        List<List<String>> content = parseContent(data);
        content.forEach(c -> {
            try {
                writeContent(writer, c.get(0), c.get(1));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        writer.close();
    }

    private List<List<String>> parseContent(List<String> data) {
        List<List<String>> retContent = new ArrayList<>();
        String date = "20201231";

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
            retContent.add(Arrays.asList(AppSheetSerialNo, date));
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
        writeWithNewLine(writer, fileCode);
        writeWithNewLine(writer, fileReciverCode);
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

    private ArrayList<String> readFields(String fileType) {
        //read field from fieldDefinitions fields.txt
        return null;
    }
}
