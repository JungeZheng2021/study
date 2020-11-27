package com.milla.study.netbase.expert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author milla
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class StudyExpertApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyExpertApplication.class, args);


    }
}