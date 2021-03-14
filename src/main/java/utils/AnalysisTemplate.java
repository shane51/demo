package utils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class AnalysisTemplate {
    List<String> inputFileTempPath = new ArrayList<>();
    List<List<String>> inputTempData = new ArrayList<>();
    public void readInputFile() throws IOException {
        //通过pdfVersion获取当前版本下所有模版文件
        String pdfVersion = LoadConfig.Load("pdfVersion");
        List<String> InputFileCodeList= Arrays.stream(LoadConfig.Load("InputFileCode").split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        for(int i = 0; i < InputFileCodeList.size(); i++){
            File file = new File("");//参数为
            inputFileTempPath.add(file.getCanonicalPath()+"/src/main/resources/fieldDefinitions/" + pdfVersion +"/"+ InputFileCodeList.get(i) +"/"+ "fieldDict.txt");
            System.out.println("********inputFileTempPath**************:"+inputFileTempPath.get(i));
        }
    }
    public List<List<String>>  getInputDefinitionData() throws IOException {
        System.out.println("*********读取模版文件*************");
        //读取模版文件
        readInputFile();
        for (int i = 0; i < inputFileTempPath.size(); i++){
            LoadTemplateFileInfo.getTempContent(inputFileTempPath.get(i));
        }
       // System.out.println("----inputTempData---:" + inputTempData);

        return inputTempData;
    }


    public void AnalysisFieldDefinitions() throws IOException {
        //取得input文件中需要的字段

    }

}
