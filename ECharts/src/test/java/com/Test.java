package com;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.github.abel533.echarts.samples.bar.BarTest4;
import com.github.abel533.echarts.samples.line.Base64Utils;

import javax.script.ScriptException;
import java.io.IOException;

/**
 * @Package: com
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/4/25 12:19
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/25 12:19
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class Test {
    public static void main(String[] args) throws ScriptException, IOException, InterruptedException {

        try {
            final WebClient webClient = new WebClient();//新建一个模拟谷歌Chrome浏览器的浏览器客户端对象

            webClient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
            webClient.getOptions().setActiveXNative(false);
            webClient.getOptions().setCssEnabled(true);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
            webClient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX

            final HtmlPage page = webClient.getPage("file:///" + BarTest4.getHtml());
//            String content = page.getWebResponse().getContentAsString();
//            BASE64Encoder encoder = new BASE64Encoder();
//            String encode = encoder.encode(content.getBytes());
//            Base64Utils.GenerateImage(encode, "C:\\Users\\milla\\Desktop\\url-haha.png");
//
//            webClient.waitForBackgroundJavaScript(2000L);
            Thread.sleep(2000L);
            ScriptResult scriptResult = page.executeJavaScript("test()");
            String imageCode = scriptResult.getJavaScriptResult().toString();
            System.out.println(imageCode);
            String image = imageCode.replaceAll("data:image/png;base64,", "");
            Base64Utils.GenerateImage(image, "C:\\Users\\milla\\Desktop\\url-" + System.currentTimeMillis() + ".png");
            //            NodeList nodes = page.getElementsByTagName("canvas");
//            Iterator iterator = ((DomNodeList) nodes).iterator();
//            for (; iterator.hasNext(); ) {
//                HtmlCanvas canvas = (HtmlCanvas) iterator.next();
//                String textContent = canvas.asText();
//                System.out.println("------");
//                System.out.println("------");
//                System.out.println(textContent);
//
//                System.out.println("------");
//                System.out.println("------");
//            }

//            var url = canvas.toDataURL("image/png");//PNG格式
        } catch (IOException e) {
            e.printStackTrace();
        }


//        ScriptEngineManager m = new ScriptEngineManager();
//        String decode = UrlUtil.decode("http://echarts.baidu.com/gallery/vendors/echarts/echarts-all-3.js");
//        URL url = new URL(decode);
//        String file = url.getFile();
//        InputStream inputStream = url.openStream();
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//        System.out.println(file);
//        //获取JavaScript执行引擎
//        ScriptEngine engine = m.getEngineByName("JavaScript");
//        engine.eval(bufferedReader);
//        //执行JavaScript代码
//        engine.eval("// 基于准备好的dom，初始化echarts图表\n" +
//                "var myChart = echarts.init(document.getElementById('main'));\n" +
//                "\n" +
//                "var option = {\"calculable\": true,\"toolbox\": {\"feature\": {\"mark\": {\"show\": true,\"title\": {\"markUndo\": \"删除辅助线\",\"markClear\": \"清空辅助线\",\"mark\": \"辅助线开关\"},\"lineStyle\": {\"color\": \"#1e90ff\",\"type\": \"dashed\",\"width\": 2}},\"dataView\": {\"show\": true,\"title\": \"数据视图\",\"readOnly\": false,\"lang\": [\"数据视图\",\"关闭\",\"刷新\"]},\"magicType\": {\"show\": true,\"title\": {\"bar\": \"柱形图切换\",\"stack\": \"堆积\",\"tiled\": \"平铺\",\"line\": \"折线图切换\"},\"type\": [\"line\",\"bar\"]},\"restore\": {\"show\": true,\"title\": \"还原\"},\"saveAsImage\": {\"show\": true,\"title\": \"保存为图片\",\"type\": \"png\",\"lang\": [\"点击保存\"]}},\"show\": true},\"tooltip\": {\"trigger\": \"axis\",\"formatter\": \"Temperature : <br/>{b}km : {c}°C\"},\"legend\": {\"data\": [\"高度(km)与气温(°C)变化关系\"]},\"xAxis\": [{\"type\": \"value\",\"axisLabel\": {\"formatter\": \"{value} °C\"}}],\"yAxis\": [{\"type\": \"category\",\"axisLine\": {\"onZero\": false},\"axisLabel\": {\"formatter\": \"{value} km\"},\"boundaryGap\": false,\"data\": [0,10,20,30,40,50,60,70,80]}],\"series\": [{\"smooth\": true,\"name\": \"高度(km)与气温(°C)变化关系\",\"type\": \"line\",\"itemStyle\": {\"normal\": {\"lineStyle\": {\"shadowColor\": \"rgba(0,0,0,0.4)\"}}},\"data\": [15,-50,-56.5,-46.5,-22.1,-2.5,-27.7,-55.7,-76.5]}]};\n" +
//                "\n" +
//                "// 为echarts对象加载数据\n" +
//                "myChart.setOption(option);");
//    }
//
//    // 得到Reader(使用HttpURLConnection得到外部js文件的Reader流)
//    public static Reader getReader(String url, String code) {
//        URL obj = null;
//        HttpURLConnection conn = null;
//        InputStream in = null;
//        Reader br = null;
//        try {
//            obj = new URL(url);
//            conn = (HttpURLConnection) obj.openConnection();
//            // 设置get请求
//            conn.setRequestMethod("GET");
//            // 得到流
//            in = conn.getInputStream();
//            br = new InputStreamReader(in, code);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return br;
    }
}
