package com.example;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@MapperScan("com.example.mapper")
@ServletComponentScan
@EnableTransactionManagement
public class ProjectTakeOutApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectTakeOutApplication.class, args);
        log.info("项目启动");
    }

}
