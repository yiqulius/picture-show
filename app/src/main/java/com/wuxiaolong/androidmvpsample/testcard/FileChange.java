package com.wuxiaolong.androidmvpsample.testcard;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class FileChange {
    public static void writeToFile(String fileName, String result)
            throws IOException {
        String filePath = "D:\\" + fileName+".txt";
        File file = new File(filePath);
        if (!file.isFile()) {
            file.createNewFile();
            DataOutputStream out = new DataOutputStream(new FileOutputStream(
                    file));
            out.writeBytes(result);
        }
    }

    // 读文件，返回字符串
    public static String ReadFile(String path) {
        File file = new File(path);
        BufferedReader reader = null;
        String laststr = "";
        try {
            // System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                laststr = laststr + tempString;
                ++line;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return laststr;
    }

}
