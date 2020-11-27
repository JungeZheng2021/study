package com.milla.pdf2word;

import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.widget.PdfPageCollection;

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
public class Pdf2Word {
    String splitPath = "./split/";
    String docPath = "./doc/";

    public Pdf2Word() {
    }

    public String pdf2Word(String srcPath) {
        String desPath = srcPath.substring(0, srcPath.length() - 4) + ".docx";
        boolean result = false;

        try {
            boolean flag = this.isPDFFile(srcPath);
            boolean flag1 = this.create();
            if (flag && flag1) {
                PdfDocument pdf = new PdfDocument();
                pdf.loadFromFile(srcPath);
                PdfPageCollection num = pdf.getPages();
                if (num.getCount() <= 10) {
                    pdf.saveToFile(desPath, FileFormat.DOCX);
                    return "转换成功";
                } else {
                    pdf.split(this.splitPath + "test{0}.pdf", 0);
                    File[] fs = this.getSplitFiles(this.splitPath);

                    for (int i = 0; i < fs.length; ++i) {
                        PdfDocument sonpdf = new PdfDocument();
                        sonpdf.loadFromFile(fs[i].getAbsolutePath());
                        sonpdf.saveToFile(this.docPath + fs[i].getName().substring(0, fs[i].getName().length() - 4) + ".docx", FileFormat.DOCX);
                    }

                    try {
                        result = MergeWordDocument.merge(this.docPath, desPath);
                        System.out.println(result);
                    } catch (Exception var15) {
                        var15.printStackTrace();
                    }

                    return "转换成功";
                }
            }

            System.out.println("输入的不是pdf文件");
        } catch (Exception var16) {
            var16.printStackTrace();
            return "转换成功";
        } finally {
            if (result) {
                clearFiles(this.splitPath);
                clearFiles(this.docPath);
            }

        }

        return "输入的不是pdf文件";
    }

    private boolean create() {
        File f = new File(this.splitPath);
        File f1 = new File(this.docPath);
        if (!f.exists()) {
            f.mkdirs();
        }

        if (!f.exists()) {
            f1.mkdirs();
        }

        return true;
    }

    private boolean isPDFFile(String srcPath2) {
        File file = new File(srcPath2);
        String filename = file.getName();
        return filename.endsWith(".pdf");
    }

    private File[] getSplitFiles(String path) {
        File f = new File(path);
        File[] fs = f.listFiles();
        return fs == null ? null : fs;
    }

    public void clearFiles(String workspaceRootPath) {
        File file = new File(workspaceRootPath);
        if (file.exists()) {
            this.deleteFile(file);
        }

    }

    public void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();

            for (int i = 0; i < files.length; ++i) {
                this.deleteFile(files[i]);
            }
        }

        file.delete();
    }
}
