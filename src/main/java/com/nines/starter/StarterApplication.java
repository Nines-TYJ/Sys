package com.nines.starter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author TYJ
 * @date 2020/10/20 17:21
 */
@SpringBootApplication
@MapperScan("com.nines.starter.mapper")
@EnableTransactionManagement
public class StarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarterApplication.class ,args);
    }

}
