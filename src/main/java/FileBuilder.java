import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import utils.LoadConfig;

public class FileBuilder {
    public static String folderName ="";
    public static String date ="20210310";

    public static void main(String[] args) throws IOException {
        buildOneDateFile(folderName,date);
        //生成文件需要文件名和时间，eg: OFD_F6_X01_20100518_02.txt,调用buildOneDateFile方法生成文件
        //buildOneDateFile(System.getenv("folderName"), System.getenv("date"));
    }
    private static void buildOneDateFile(String folderName, String date) throws IOException {
        System.out.println("--------------"+LoadConfig.Load("TANameList"));
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
