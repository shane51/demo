package utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AnalysisTemplate {
    List<String> inputFileTempPath;
    List<List<String>> inputTempData;
    public void readInputFile() throws IOException {
        //通过pdfVersion获取当前版本下所有模版文件
        String pdfVersion = LoadConfig.Load("pdfVersion");
        List<String> InputFileCodeList= Arrays.stream(LoadConfig.Load("InputFileCode").split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        for(String eachTemp : InputFileCodeList){
            File file = new File("");//参数为空
            String inputFileTempPath = file.getCanonicalPath() + "/src/main/resources/fieldDefinitions/" + pdfVersion +"/"+ eachTemp +"/"+ "fieldDict.txt";
            System.out.println("--------inputFileTempPath:"+inputFileTempPath);
        }
    }
    public List<List<String>>  getInputDefinitionData() throws IOException {
        readInputFile();
        //得到模版文件
        for (int i = 0; i< inputFileTempPath.size(); i++){
            inputTempData.add(ReadFileInfo.getContent(inputFileTempPath.get(i).toString()));
            System.out.println(inputTempData);
        }
        return inputTempData;
    }

    public void readFieldDefinitions(){
        //取得input的fieldDefinitions


    }
    public void AnalysisFieldDefinitions(){
        //取得input文件中需要的字段
    }

}
