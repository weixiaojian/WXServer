package com.zhitengda;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author langao_q
 * @create 2020-07-17 18:04
 * SpringBoot启动类
 */
@Slf4j
@EnableScheduling
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    /**
     * SpringBoot启动方法
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.info("=====================Spring Boot Application 启动成功====================");
    }

    /**
     * 打成war 部署tomcat配置
     * @param application
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

}
