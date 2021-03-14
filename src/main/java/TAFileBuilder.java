import utils.AnalysisTemplate;
import utils.LoadConfig;
import utils.LoadTemplateFileInfo;
import utils.TxTDataReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class TAFileBuilder {
    public String folderName;
    public String batchRunDate;
    public String fileVersion;
    public String aggregationNumber;
    public String senderName;
    public String receiverName;
    public String dataFileStartCode;
    public String indexFileStartCode;
    public String indexFileEndCode;
    public List<String> outputFileName;
    public List<String> outputPath =  new ArrayList<>();
    List<String>  fieldNames = new ArrayList<>();

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

    public void build(String[] fileTypes,int i) throws IOException {
        TxTDataReader txtDataReader = new TxTDataReader();
        //build file
        String fileCreationDate = batchRunDate;
        outputFileName = txtDataReader.getOutputFileName(fileCreationDate, folderName, fileTypes);
        TxTDataReader txtReader = new TxTDataReader();
           //Get input file content
        List<List<String>> data = txtReader.getInputFileContent();
        System.out.println("----input data---:" + data);
        this.senderName = txtDataReader.reciverCode;
        this.receiverName = txtDataReader.senderCode;
        populateOFDFile(fileCreationDate, fileTypes, data, i);
    }

    private void populateOFDFile(String fileCreationDate, String[] fileType, List<List<String>> data, int i) throws IOException {
        String outDir;
        try {
            outDir = LoadConfig.Load("output_dir");
        } catch (IOException err) {
            outDir = "src/main/resources/";
        }
        List<String> outputDirPath = new ArrayList<String>();
        for (int j = 0; j < fileType.length; j++) {
            outputDirPath.add(Paths.get(outDir, "output", fileCreationDate, LoadConfig.Load("TestScenarios"), fileType[j]).toString());
            new File(outputDirPath.get(j)).mkdirs();
            outputPath.add(Paths.get(outputDirPath.get(j), outputFileName.get(j)).toString());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath.get(j)),
                    "GBK"));
            writeHeader(writer, fileCreationDate, fileType[j]);
            List<List<String>> content = parseContent(data.get(j));
            content.forEach(c -> {
                try {
                    writeContent(writer, c.get(0), c.get(1));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private List<List<String>> parseContent(List<String> data) throws IOException {
        AnalysisTemplate templateFile = new AnalysisTemplate();
        List<List<String>> retContent = new ArrayList<>();
        Map<String, Integer> rules = templateFile.getInputDefinitionData();
        int titleCount = Integer.parseInt(data.get(TITLE_COUNT_INDEX));
        // 总共有几条数据
        int dataLength = Integer.parseInt(data.get(titleCount + TITLE_COUNT_INDEX + 1));
        // 数据起始index
        int startIndex = titleCount + TITLE_COUNT_INDEX + 2;

        for (int i = 0; i < dataLength; i++) {
            String wholeContent = data.get(startIndex + i);
            String fieldName = wholeContent.substring(rules.get("Fields_start"), rules.get("Fields_end"));
            retContent.add(Arrays.asList(fieldName, batchRunDate));
        }
        return retContent;
    }
    // 返回解析到的CSV规则
    // TODO: 先mock 后续需要补充内容

    private void getFieldsName() throws IOException {
        fieldNames= Arrays.stream(LoadConfig.Load("fieldNames").split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        System.out.println("FieldsName:"+fieldNames);
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
        getFieldsName();
        for(int i=0; i <fieldNames.size(); i++ ){
            writeWithNewLine(writer, fieldNames.get(i));
        }
//        writeWithNewLine(writer, "AppSheetSerialNo");
//        writeWithNewLine(writer, "TransactionCfmDate");
        writer.flush();
    }
    //需要再解析下outputfile得到字段名，再给字段填值

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
