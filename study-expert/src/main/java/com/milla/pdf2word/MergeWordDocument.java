package com.milla.pdf2word;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;

import java.io.File;

/**
 * @Package: com.milla.pdf2word
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/07/16 15:04
 * @UpdateUser: milla
 * @UpdateDate: 2020/07/16 15:04
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class MergeWordDocument {
    public MergeWordDocument() {
    }

    public static boolean merge(String docPath, String desPath) {
        File[] fs = getSplitFiles(docPath);
        System.out.println(docPath);
        Document document = new Document(docPath + "test0.docx");

        for (int i = 1; i < fs.length; ++i) {
            document.insertTextFromFile(docPath + "test" + i + ".docx", FileFormat.Docx_2013);
        }

        document.saveToFile(desPath);
        return true;
    }

    private static File[] getSplitFiles(String path) {
        File f = new File(path);
        File[] fs = f.listFiles();
        return fs == null ? null : fs;
    }
}
