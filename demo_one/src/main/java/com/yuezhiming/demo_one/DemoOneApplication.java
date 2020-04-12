package com.yuezhiming.demo_one;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@ServletComponentScan
public class DemoOneApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoOneApplication.class, args);
    }

}
