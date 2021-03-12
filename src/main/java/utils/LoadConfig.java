package utils;

import java.io.*;
import java.util.Properties;

public class LoadConfig {
     public static String Load(String propertyName) throws IOException {
         Properties properties = new Properties();
         // 使用InPutStream流读取properties文件
         BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/TAs.properties"));
         properties.load(bufferedReader);
        // 获取key对应的value值
         return properties.getProperty(propertyName);
     }
}
