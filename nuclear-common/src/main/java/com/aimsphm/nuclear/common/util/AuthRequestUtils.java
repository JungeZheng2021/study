package com.aimsphm.nuclear.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 功能描述: 田湾权限控制工具类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/08/19 11:07
 */
@Slf4j
@Component
public class AuthRequestUtils {
    /**
     * 根据userid获取权限接口地址
     */
    @Value("${customer.config.user-privileges-url:http://autogrant.jnpc.com.cn/autogrant/privileges/queryUserQXPrivileges.so}")
    private String userPrivilegesUrl;
    /**
     * 根据登录名获取用户userid接口地址
     */
    @Value("${customer.config.user-details-url:http://sv-epprd01.jnpc.com.cn:50000/UmpInterfaceService/UmpInterface}")
    private String userDetailsUrl;


    @Value("${privileges-sysCode:70}")
    private String sysCode;

    public String getUserIdByUserName(String username) {
        String userStr = queryUserDetails(username);
        log.debug("result:{}", userStr);
        JSONArray user = JSON.parseArray(userStr);
        if (Objects.isNull(user) || user.isEmpty()) {
            return null;
        }
        JSONObject jsonObject = user.getJSONObject(0);
        return jsonObject.getString("userid");
    }


    private String queryUserDetails(String username) {
        OkHttpClient client = new OkHttpClient();
        com.squareup.okhttp.MediaType mediaType = com.squareup.okhttp.MediaType.parse("text/xml");
        RequestBody body = RequestBody.create(mediaType,
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ejb=\"http://ejb.com/\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <ejb:getUserInfosV5>\n" +
                        "         <empIds>" + username + "</empIds>\n" +
                        "      </ejb:getUserInfosV5>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>");
        try {
            Request request = new Request.Builder().url(userDetailsUrl).method("POST", body).addHeader("Content-Type", "text/xml").build();
            Response response = client.newCall(request).execute();
            String bodyStr = response.body().string();
            return getUserIdFromXmlStr(bodyStr, "return");
        } catch (IOException e) {
            log.error("get user details failed from interface ，send the default value->{},error {}", "[]", e.getMessage());
            return "[]";
        }
    }

    private String getUserIdFromXmlStr(String bodyStr, String tagName) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bodyStr.getBytes(StandardCharsets.UTF_8));
            Document parse = db.parse(byteArrayInputStream);
            NodeList aReturn = parse.getElementsByTagName(tagName);
            Node item = aReturn.item(0);
            return item.getTextContent();
        } catch (Exception e) {
            log.error("parse xml failed： {}", e);
        }
        return "[]";
    }

    public Set<String> getUserPrivilegeByUsername(String username, String sysCode) {
        String userId = getUserIdByUserName(username);
        if (StringUtils.isEmpty(userId)) {
            return new HashSet<>();
        }
        return getUserPrivilegeRest(userId, sysCode);
    }

    /**
     * restTemplate请求
     *
     * @param userAccount
     * @param sysCode
     * @return
     */
    private Set<String> getUserPrivilegeRest(String userAccount, String sysCode) {
        RestTemplate template = new RestTemplate();
        String json = "?userAccount=%s&sysCode=%s";
        String format = String.format(json, userAccount, StringUtils.hasText(sysCode) ? sysCode : this.sysCode);
        ResponseEntity<String> entity = template.getForEntity(userPrivilegesUrl + format, String.class);
        log.debug("get用户权限结果为：{}", entity);
        int statusCodeValue = entity.getStatusCodeValue();
        if (statusCodeValue != 200) {
            return new HashSet<>();
        }
        String body = entity.getBody();
        return privileges(body);
    }

    /**
     * 田湾请求方式
     *
     * @param userAccount
     * @param sysCode
     * @return
     */
    Set<String> getUserPrivilege(String userAccount, String sysCode) {
        InputStream is = null;
        HttpURLConnection conn = null;
        DataOutputStream out = null;
        BufferedReader reader = null;
        InputStreamReader inputStreamReader = null;
        try {
            URL url = new URL(userPrivilegesUrl);
            //打开和url之间的连接
            conn = (HttpURLConnection) url.openConnection();
            //请求方式
            conn.setRequestMethod("POST");
            //设置请求编码
            conn.setRequestProperty("Charsert", "UTF-8");
            //设置通用的请求属性
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 维持长连接
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new DataOutputStream(conn.getOutputStream());
            String json = "userAccount=%s&sysCode=%s";
            String format = String.format(json, userAccount, sysCode);
            out.write(format.getBytes("UTF-8"));
            //缓冲数据
            out.flush();
            out.close();
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                //获取URLConnection对象对应的输入流
                is = conn.getInputStream();
                inputStreamReader = new InputStreamReader(conn.getInputStream());
                //构造一个字符流缓存
                reader = new BufferedReader(inputStreamReader);
                String lines;
                StringBuilder sb = new StringBuilder();
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), StandardCharsets.UTF_8);
                    sb.append(lines);
                }
                return privileges(sb.toString());
            } else {
                return new HashSet<>();
            }
        } catch (Exception e) {
            log.error("get user privilege failed --> userAccount:{} , sysCode:{}, error:{}", userAccount, sysCode, e);
        } finally {
            //关闭流
            try {
                if (Objects.nonNull(inputStreamReader)) {
                    inputStreamReader.close();
                }
                if (Objects.nonNull(reader)) {
                    reader.close();
                }
                if (Objects.nonNull(out)) {
                    out.close();
                }
                if (Objects.nonNull(is)) {
                    is.close();
                }
                if (Objects.nonNull(conn)) {
                    conn.disconnect();
                }
            } catch (IOException e) {
                log.error("{}", e);
            }
        }
        return new HashSet<>();
    }

    private Set<String> privileges(String sb) {
        JSONObject responseData = JSON.parseObject(sb);
        Integer result = responseData.getInteger("result");
        if (result != 1) {
            return new HashSet<>();
        }
        String message = responseData.getString("message");
        if (!StringUtils.hasText(message)) {
            return new HashSet<>();
        }

        return Arrays.stream(message.split(",")).collect(Collectors.toSet());
    }
}
