package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ReadFileInfo {

    public static List<String> getContent(String fileDir) {
        List<String> fileContent = new ArrayList<>();
        try
        {
            String encoding = "GBK";
            File file = new File(fileDir);
            if (file.isFile() && file.exists()){
                {
                    InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt;
                    while ((lineTxt = bufferedReader.readLine()) != null)
                    {
                        fileContent.add(lineTxt);

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
        return fileContent;
    }
}
