package com.example.hecmybatis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.hecbatch"})
public class HecMybatisApplication {


    public static void main(String[] args) {
        SpringApplication.run(HecMybatisApplication.class, args);
    }

}
