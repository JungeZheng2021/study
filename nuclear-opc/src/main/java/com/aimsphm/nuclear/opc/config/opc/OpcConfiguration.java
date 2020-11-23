package com.aimsphm.nuclear.opc.config.opc;

import com.aimsphm.nuclear.opc.client.Browser;
import com.aimsphm.nuclear.opc.client.OpcClient;
import com.aimsphm.nuclear.opc.config.rabbit.MqttGateway;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.openscada.opc.lib.da.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;

import java.util.concurrent.Executors;

/**
 * @ClassName OpcConfiguration
 * @Description opc的连接随着程序启动而运行
 * @Author Mr.zhang
 * @Date 2019/11/4 9:35
 */
@Slf4j
//@Component
public class OpcConfiguration implements CommandLineRunner {

    @Value("${spring.progId}")
    private String progId;

    @Value("${spring.user}")
    private String user;

    @Value("${spring.host}")
    private String host;

    @Value("${spring.password}")
    private String password;

    @Value("${spring.domain}")
    private String domain;

    @Autowired
    private MqttGateway mqttGateway;

    @Override
    public void run(String... args) {
        OpcClient client = new OpcClient();
        client.connectServer(host, progId, user, password, domain);
        Server server = client.getServer();
        try {
            //每1秒异步读取一次opcServer的数据
            Browser.readAsyn(server, Executors.newScheduledThreadPool(2), 1000L, (dataItems) -> {
//                System.out.println("-----------------");
//                System.out.println(JSON.toJSONString(dataItems));
//                System.out.println("-----------------");
                mqttGateway.sendToMqtt(JSON.toJSONString(dataItems), "pi/many");

                //                String path="D:\\data\\"+System.currentTimeMillis()+".txt";
//                File file = new File(path);
//                //如果没有文件就创建
//                if (!file.isFile()) {
//                    file.createNewFile();
//                }
//                BufferedWriter writer = new BufferedWriter(new FileWriter(path));
//                for (DataItem l:dataItems){
//                    writer.write(l + "\r\n");
//                }
//                writer.close();
//                Map<String, List<DataItem>> groupDataItem = new LinkedHashMap<>();
//                //根据机组的opc标签名进行归类
//                for (DataItem dataItem : dataItems) {
//                    String itemId = dataItem.getItemId();
//                    if (itemId.indexOf(" ") != -1 || itemId.indexOf("@") != -1 || itemId.indexOf("#") != -1 || itemId.indexOf("Random") != -1) {
//                        continue;
//                    }
//                    System.out.println("<》: " + dataItem);
//                    System.out.println("tag: " + dataItem.getItemId() + ",value: " + dataItem.getValue() + ",dataTime:" + dataItem.getDataTime() + ",sendTime: " + dataItem.getCurrMonment());
//                    String groupName = dataItem.getItemId();//.split("\\$")[1].substring(3);
//                    if (groupDataItem.containsKey(groupName)) {
//                        groupDataItem.get(groupName).add(dataItem);
//                    } else {
//                        List<DataItem> groupList = new LinkedList<>();
//                        groupList.add(dataItem);
//                        groupDataItem.put(groupName, groupList);
//                    }
//                }
////
//                //将不同机组的信息发送到kafka不同的topic
//                Iterator<Map.Entry<String, List<DataItem>>> iterator = groupDataItem.entrySet().iterator();
//                while (iterator.hasNext()) {
//                    Map.Entry<String, List<DataItem>> entry = iterator.next();
//                    Map map = new ConcurrentHashMap();
//                    for (DataItem dataItem : entry.getValue()) {
//                        map.put(dataItem.getItemId(), dataItem.getValue());
//                    }
//                    log.info("topic的值为:{}", entry.getKey());
//                }
            });
        } catch (Throwable throwable) {
            log.error(throwable.getMessage());
        }
    }

}
