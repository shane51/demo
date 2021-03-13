package utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TxTDataReader {
    public static String reciverCode = null;
    public static String senderCode = null;
    static String outputFileDate;
    static List<String>inputFileDir = new ArrayList<String>();
    static List<String> inputFileName = new ArrayList<String>();
    static List<String> outputFileName = new ArrayList<String>();
    //static List<String> inputFileDir = null;
    static File file = new File("");//参数为空
    public List<String> getOutputFileName(String batchRunDate, String folderName, String[] fileType) throws IOException {
        String[] inputFileCode = LoadConfig.Load("InputFileCode").split(",");
        int dirCount = inputFileCode.length;
        //outputFileName = new ArrayList<String>();
        System.out.println("------dirCount:"+dirCount);
        for (int i = 0; i < inputFileCode.length; i++) {
            String tmpPath = file.getCanonicalPath() + "/src/main/resources/inputFiles/" + batchRunDate + "/" + folderName + "/" + inputFileCode[i];
          //  System.out.println("------tmpPath:"+tmpPath);
            inputFileDir.add(tmpPath);
            File file1 = new File(inputFileDir.get(i));
            File[] files = file1.listFiles();
            System.out.println("---inputFileName:"+ files[0].getName());
            inputFileName.add(files[0].getName());
//            for (int j =0; j < files.length; j++){
//                inputFileName[j] = files[j].getName();
//                inputFileDir.get(j) = files[j].getPath();
                getReciverSenderCode(inputFileName.get(i));
                getSystemDate();
                String tmpName = String.format("OFD_%s_%s_%s_%s.TXT",reciverCode,senderCode,outputFileDate,fileType[i]);
                System.out.println("------tmpName:"+tmpName);
                outputFileName.add(String.format("OFD_%s_%s_%s_%s.TXT",reciverCode,senderCode,outputFileDate,fileType[i]));
                System.out.println("输出文件名：" + outputFileName.get(i));
            //}
        }
        return outputFileName;
    }
    public List<List<String>> getInputFileContent(){
        List<List<String>> data = new ArrayList<List<String>>();;
        for (int i =0; i < inputFileDir.size(); i++){
            String fileName = inputFileDir.get(i)+"/"+inputFileName.get(i);
            System.out.println("------inputFileDir:"+fileName);
            List<String> data1 = ReadFileInfo.getContent(fileName);
            System.out.println(data1);
            data.add(data1);
            data.add(ReadFileInfo.getContent(fileName));}
        return data;
    }
    private void getSystemDate(){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        outputFileDate = df.format(new Date());
    }

    private void getReciverSenderCode(String inputFileName) {

        String [] arr = inputFileName.split("_");
        for(String ignored : arr){
            senderCode = arr[1];
            reciverCode = arr[2];
        }
    }
}
