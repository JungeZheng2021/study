package com.study.cache.redis;

import com.google.common.collect.Lists;

import java.io.*;
import java.util.List;

/**
 * @Package: com.study.cache.redis
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/08/26 13:12
 * @UpdateUser: milla
 * @UpdateDate: 2020/08/26 13:12
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class Test {

    public static void main(String[] args) throws IOException {
        String path = "D:\\Java\\workspace\\tenement\\tenement-user\\src\\main\\java\\com\\home\\user";
        File file = new File(path);
        replace(file);
    }

    private static void replace(File root) throws IOException {
        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                replace(file);
            } else {
                List<String> list = Lists.newLinkedList();
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = null;
                StringBuilder fileText = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("/*") && line.contains("*/")) {
                        line = line.substring(line.indexOf("*/") + 2);
                    } else if (line.contains("/* Location:") || line.contains("* Qualified Name:") || line.contains("* JD-Core Version:") || line.equals("*/") || line.equals(" */")) {
                        continue;
                    }
                    list.add(line);
                }
                reader.close();
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                for (String text : list) {
                    writer.write(text);
                    writer.newLine();
                }
                writer.flush();
            }
        }
    }
}

