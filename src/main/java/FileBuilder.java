import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import utils.LoadConfig;
import utils.TxTDataReader;

public class FileBuilder {
    public static String folderName ="OpenAccount";
    public static String date ="20210310";

    public static void main(String[] args) throws IOException {
        buildOneDateFile(folderName,date);
    }

    private static void buildOneDateFile(String folderName, String date) throws IOException {
        System.out.println("------TANameList--------"+LoadConfig.Load("TANameList"));
        List<String>  TAList= Arrays.stream(LoadConfig.Load("TANameList").split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        for(String eachTA : TAList){
            TAFileBuilder taFileBuilder = new TAFileBuilder(folderName, date, eachTA);
            String[] filesToBuild = {"02"};   //02是账户确认文件
            taFileBuilder.build(filesToBuild);
            System.out.println("-----------success----------");
        }

    }
}
