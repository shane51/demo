package utils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class LoadTemplateFileInfo {

    private static Map<String, Integer> readFieldDefinitions(String lineTxt) throws IOException {
        String fieldNames = lineTxt.split(",")[1];
        int fieldLength = Integer.parseInt(lineTxt.split(",")[3]);
        //取得input的fieldDefinitions
        Map<String, Integer> rules = new HashMap<>();
        // 字段起始位置
        rules.put(fieldNames, fieldLength);
        for(Map.Entry entry: rules.entrySet()){
            System.out.println(entry.getKey()+" = "+entry.getValue());
        }
        return rules;
        // 字段结束位置
    }
    public static Map<String, Integer> AnalysisFieldDefinitions(Map<String, Integer> parseRule) throws IOException {
        //Map<String, Integer> parseRule = new HashMap<>();
        List<String>  fieldNames= Arrays.stream(LoadConfig.Load("fieldNames").split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        //System.out.println("FieldsName:"+fieldNames);
        //rule 字段名：对应长度
        for(int i = 0; i < fieldNames.size(); i++) {
            for(Map.Entry entry: parseRule.entrySet()){
                if (fieldNames.get(i).equals(entry.getKey())){
                    int sum = getMapSum(parseRule);
                    parseRule.put("Fields_start",sum);
                    parseRule.put("Fields_end",Integer.parseInt(entry.getValue().toString())+sum);
                    System.out.println("-----Fields_end----"+Integer.parseInt(entry.getValue().toString())+sum);
                };
            }
        }
        return parseRule;
    }

    private static int getMapSum(Map<String, Integer> rule) {
        return 15;
    }


    private static Map<String, Integer> parseRule() {
        Map<String, Integer> rules = new HashMap<>();
        // 字段起始位置
        rules.put("AppSheetSerialNo_start", 15);
        // 字段结束位置
        rules.put("AppSheetSerialNo_end", 30);
        return rules;
    }
    public static Map<String, Integer>  getTempContent(String fileDir) {
        Map<String, Integer> parseRule = new HashMap<>();
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
                    parseRule = AnalysisFieldDefinitions(rule);
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
        return parseRule;
    }
}
