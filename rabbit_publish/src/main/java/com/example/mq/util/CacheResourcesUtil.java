package com.example.mq.util;

import com.example.mq.util.entity.DataQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @Package: com.example.mq.util
 * @Description: <缓存工具类>
 * @Author: MILLA
 * @CreateDate: 2020/6/1 14:19
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/1 14:19
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
public class CacheResourcesUtil extends CacheUtil<DataQuery, List<String>> {
    private final RestTemplate restTemplate = new RestTemplate();

    public void setParams(DataQuery params) {
        this.params = params;
    }

    @Override
    List<String> query(DataQuery object) {
        return initResources(params);
    }

    /**
     * 初始化资源
     *
     * @param params 参数
     * @return
     */
    private List<String> initResources(DataQuery params) {
        List<String> resources = new ArrayList<>();
        if (StringUtils.isEmpty(params.getUrl())) {
            return resources;
        }
        Integer pages = params.getPages() == null ? 2 : params.getPages();

        for (int m = 1; m <= pages; m++) {
            log.info("initial resources....page->{}.....", m);
            ResponseEntity<String> forEntity = this.restTemplate.getForEntity(params.getUrl() + "/article/list/" + m, String.class, new Object[0]);
            String splitText = "article-item-box csdn-tracking-statistics";
            String body = forEntity.getBody();
            String preText = "\" data-articleid=\"";
            String[] split = body.split(splitText);

            for (int i = 0; i < split.length; ++i) {
                String s = split[i];
                if (StringUtils.hasText(s) && s.startsWith(preText)) {
                    String number = "/readCountWhite.png\" alt=\"\">";
                    String startText = "href=\"";
                    String endText = "\" target=\"_blank\"";
                    int start = s.indexOf(startText);
                    int end = s.indexOf(endText);
                    String index = s.substring(start + startText.length(), end);
                    resources.add(index);
                    String id = index.substring(index.lastIndexOf("/") + 1);
                    if (s.contains(number)) {
                        try {
                            String substring = s.substring(s.indexOf(number));
                            String substring1 = substring.substring(number.length(), substring.indexOf("</"));
                            data.put(id, Integer.parseInt(substring1));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return resources;
    }

    /**
     * 定期清空缓存
     */
    public void delayClear() {
        data.clear();
    }
}