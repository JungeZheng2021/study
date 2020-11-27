package com.milla.pdf2word;

/**
 * @Package: com.milla.pdf2word
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/07/16 15:05
 * @UpdateUser: milla
 * @UpdateDate: 2020/07/16 15:05
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class Main {
    public static void main(String[] args) {
        String res = (new Pdf2Word()).pdf2Word("D:\\Desktop\\《快速转行做产品经理》_李三科-高清 .pdf");
        System.out.println(res);
    }
}
