package com.milla.study.netbase.expert.poi;

import org.apache.ibatis.datasource.DataSourceException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.Semaphore;

/**
 * @Package: com.milla.study.netbase.expert.poi
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/6/18 14:15
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/18 14:15
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class Test {
    public static void main(String[] args) throws IOException, SQLException, InterruptedException {
//        excel();
        Semaphore semaphore = new Semaphore(0);
        System.out.println(semaphore);
        semaphore.acquire();
        Thread.sleep(800L);
        System.out.println(semaphore);
        semaphore.release();
        System.out.println(semaphore);

    }

    private static void excel() throws SQLException, IOException {
        //1.加载excel文件
        InputStream inputStream = new FileInputStream("C:\\Users\\milla\\Downloads\\新建 Microsoft Office Excel 2007 工作表.xlsx");
        //2、获取workbook
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        //3. 获取sheet的个数
        int numberOfSheets = workbook.getNumberOfSheets();
        //4.拿不到数据
        if (numberOfSheets == 0) {
            System.out.println("没有数据");
            return;
        }
        //5.拿到数据
        XSSFSheet sheetAt = workbook.getSheetAt(0);
        //6.获取数据
        int lastRowNum = sheetAt.getLastRowNum();
//


        for (int i = 0; i <= lastRowNum; i++) {
            System.out.println("第" + (i + 1) + "行数据：");
            //获取每一行
            XSSFRow row = sheetAt.getRow(i);
            if (row == null) {
                continue;
            }
            //获取每一列数
            short lastCellNum = row.getLastCellNum();
            //字符串容器
            StringBuilder sb = new StringBuilder("INSERT INTO `sys`.`room`(`name`, `source`, `status`, `owner`, `tel`, `type`, `category`, `city`, `amont`, `pay_type`, `contract`, `date`) VALUES (");
            for (int j = 0; j < lastCellNum; j++) {
                XSSFCell cell = row.getCell(j);
                if (j == lastCellNum - 1) {
                    sb.append("'").append(cell).append("')");
                } else {
                    sb.append("'").append(cell).append("',");
                }
            }
            System.out.println(sb);
            System.out.println();
            //1.获取数据库连接
            Connection connection = getConnection();
            //2.把sql给
            PreparedStatement ps = connection.prepareStatement(sb.toString());
            //3.执行sql
            boolean execute = ps.execute();
            System.out.println("执行结果： " + execute);


        }
    }

    //2.静态代码块（只执行一次）
    static {
        try {
            Class<?> aClass = Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new DataSourceException("加载不到驱动类");
        }
    }

    // 3.连接方法getConnection()
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://192.168.16.28:3306/nuclear_phm?characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=UTC", "root", "aims2016");
        } catch (SQLException e) {
            throw new DataSourceException("获取数据库连接失败");
        }
    }
}
