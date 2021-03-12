package utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TxTDataReader {
    static String reciverCode;
    static String senderCode;
    static String outputFileDate;
    static String inputFileDir;

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
            inputFileDir = files[i].getPath();
        }
        getReciverSenderCode(inputFileName);
        System.out.println("输入文件名：" + inputFileName);
        getSystemDate();
        String outputFileName = String.format("OFD_%s_%s_%s_%s.TXT",reciverCode,senderCode,outputFileDate,fileType);
        System.out.println("输入文件名：" + outputFileName);
        return outputFileName;
    }
    public List<String> getInputContent(){
        List<String> list = new ArrayList<String>();
        try
        {
            String encoding = "GBK";
            File file = new File(inputFileDir);
            if (file.isFile() && file.exists()){
                {
                    InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    while ((lineTxt = bufferedReader.readLine()) != null)
                    {
                        list.add(lineTxt);

                    }
                    bufferedReader.close();
                    read.close();
                }
            }
            else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return list;

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
