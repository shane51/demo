package utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TxTDataReader {
    public static String reciverCode;
    public static String senderCode;
    static List<String>inputFileDir = new ArrayList<String>();
    static List<String> inputFileName = new ArrayList<String>();
    static List<String> outputFileName = new ArrayList<String>();
    static File file = new File("");//参数为空
    public List<String> getOutputFileName(String batchRunDate, String folderName, String[] fileType) throws IOException {
        String[] inputFileCode = LoadConfig.Load("InputFileCode").split(",");
        int dirCount = inputFileCode.length;
        System.out.println("------dirCount:"+dirCount);
        for (int i = 0; i < inputFileCode.length; i++) {
            String tmpPath = file.getCanonicalPath() + "/src/main/resources/inputFiles/" + batchRunDate + "/" + folderName + "/" + inputFileCode[i];
            inputFileDir.add(tmpPath);
            File file1 = new File(inputFileDir.get(i));
            File[] files = file1.listFiles();
            System.out.println("---inputFileName:"+ files[0].getName());
            inputFileName.add(files[0].getName());
            getReciverSenderCode(inputFileName.get(i));
            outputFileName.add(String.format("OFD_%s_%s_%s_%s.TXT",reciverCode,senderCode,batchRunDate,fileType[i]));
            System.out.println("输出文件名：" + outputFileName.get(i));
        }
        return outputFileName;
    }
    public List<List<String>> getInputFileContent(){
        List<List<String>> data = new ArrayList<>();;
        for (int i =0; i < inputFileDir.size(); i++){
            String fileName = inputFileDir.get(i)+"/"+inputFileName.get(i);
            System.out.println("------inputFileDir:"+fileName);
            data.add(ReadFileInfo.getContent(fileName));
        }
        return data;
    }

    private void getReciverSenderCode(String inputFileName) {

        String [] arr = inputFileName.split("_");
        for(String ignored : arr){
            senderCode = arr[1];
            reciverCode = arr[2];
        }
    }
}
