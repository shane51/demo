package utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TxTDataReader {
    static String reciverCode;
    static String senderCode;
    static String outputFileDate;

    public String getOutputFileName(String batchRunDate, String folderName, String fileType) throws IOException {
         String inputFileName = null;
         String inputfileType="01";

        //get input filename
        File file = new File("");//参数为空
        String path = file.getCanonicalPath() + "/src/main/resources/inputFiles/" + batchRunDate +"/"+ folderName +"/"+ inputfileType;
        File file1 = new File(path);
        File[] files = file1.listFiles();
        for (int i =0; i < files.length; i++){
            inputFileName = files[i].getName();
        }
        getReciverSenderCode(inputFileName);
        System.out.println("输入文件名：" + inputFileName);
        getSystemDate();
        String outputFileName = String.format("OFD_%s_%s_%s_%s.TXT",reciverCode,senderCode,outputFileDate,fileType);
        System.out.println("输入文件名：" + outputFileName);
        return outputFileName;
    }
    private void getSystemDate(){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        outputFileDate = df.format(new Date());
        System.out.println("-----outputFileDate----"+outputFileDate);//
    }

    private void getReciverSenderCode(String inputFileName) {

        String [] arr = inputFileName.split("_");
        for(String ss : arr){
            senderCode = arr[1];
            reciverCode = arr[2];
        }
    }
}
