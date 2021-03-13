import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import utils.LoadConfig;

public class FileBuilder {
    public static String date;
    public static void main(String[] args) throws IOException {
        date = LoadConfig.Load("batchRunDate");
        String[] folderName = LoadConfig.Load("TestScenarios").split(",");
        for (int i = 0; i < folderName.length; i++){
            buildDateFile(folderName[i],date);
        }
    }

    private static void buildDateFile(String folderName, String date) throws IOException {
       // System.out.println(date);
       // System.out.println("------TANameList--------"+LoadConfig.Load("TANameList"));
        List<String>  TAList= Arrays.stream(LoadConfig.Load("TANameList").split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        for(int i = 0; i < TAList.size(); i++){
            TAFileBuilder taFileBuilder = new TAFileBuilder(folderName, date);
            //String[] filesToBuild = {"02"};   //02是账户确认文件
            String[] filesToBuild = LoadConfig.Load("OutputFileCode").split(",");
           // TANameList = 1, build 一份文件
            taFileBuilder.build(filesToBuild,i);
            System.out.println("-----------success----------");
        }

    }
}
