package com.aimsphm.nuclear.data;

import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.data.mapper.MeasurePointVOMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@EnableAsync
@EnableCaching
@EnableFeignClients
@MapperScan({"com.aimsphm.nuclear.*.mapper**"})
//@ImportResource({"classpath*:mapper/**/*.xml"})
@SpringBootApplication(scanBasePackages = "com.aimsphm.nuclear")
@RestController
public class NuclearDataApplication {
    @Autowired
    private MeasurePointVOMapper mapper;

    public static void main(String[] args) {
        SpringApplication.run(NuclearDataApplication.class, args);

    }

    @GetMapping("test")
    public List<MeasurePointVO> test() {
        return mapper.selectMeasurePoints(1);
    }
}
