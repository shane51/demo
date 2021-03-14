package utils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class LoadTemplateFileInfo {

    public static Map<String, Integer> readFieldDefinitions(String lineTxt) throws IOException {
        String fieldNames = lineTxt.split(",")[1];
        int fieldLength = Integer.parseInt(lineTxt.split(",")[3]);
        //取得input的fieldDefinitions
        Map<String, Integer> rules = new HashMap<>();
        // 字段起始位置
        rules.put(fieldNames, fieldLength);
        return rules;
        // 字段结束位置
    }
    public static void AnalysisFieldDefinitions(Map<String, Integer> rule) throws IOException {
        List<String>  fieldNames= Arrays.stream(LoadConfig.Load("fieldNames").split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        //String key = fieldNames.get(i);
        System.out.println("FieldsName:"+rule);
        for(int i = 0; i < fieldNames.size(); i++) {
        }
        //取得input文件中需要的字段
    }
    public static void getTempContent(String fileDir) {
        Map<String, Integer> rule = new HashMap<>();
        try
        {
            String encoding = "GBK";
            File file = new File(fileDir);
            if (file.isFile() && file.exists()){
                {
                    InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
                    BufferedReader bufferedReader = new BufferedReader(read);
                    bufferedReader.readLine();
                    String lineTxt;
                    while ((lineTxt = bufferedReader.readLine()) != null)
                    {
                        //readFieldDefinitions(lineTxt);
                        rule = readFieldDefinitions(lineTxt);
                        //fileContent.add(lineTxt);
                    }
                    AnalysisFieldDefinitions(rule);
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
    }
}
