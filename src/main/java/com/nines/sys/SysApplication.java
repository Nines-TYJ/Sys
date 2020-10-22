package com.nines.sys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author TYJ
 * @date 2020/10/20 17:21
 */
@SpringBootApplication
@EnableTransactionManagement
public class SysApplication {

    public static void main(String[] args) {
        SpringApplication.run(SysApplication.class ,args);
    }

}
