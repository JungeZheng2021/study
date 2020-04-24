package com.github.abel533.echarts.samples.line;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @Package: com.github.abel533.echarts.samples.line
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/4/24 9:57
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/24 9:57
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("C:\\Users\\milla\\Desktop\\url.txt")));
        String s = bufferedReader.readLine();
        Base64Utils.GenerateImage(s, "C:\\Users\\milla\\Desktop\\url-new.png");
    }
}
