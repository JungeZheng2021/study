package com.aimsphm.nuclear.core.test;

import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.service.CommonMeasurePointService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class NuclearCoreApplicationTest {
    //	@Autowired
//	TestformybatisplusMapper tmapper;
    private static final Logger log = LoggerFactory.getLogger(NuclearCoreApplicationTest.class);

    @Autowired
    private CommonMeasurePointService serviceExt;

    @Test
    public void testContent() {
        List<CommonMeasurePointDO> list = serviceExt.list();
        System.out.println(list);

    }
}
