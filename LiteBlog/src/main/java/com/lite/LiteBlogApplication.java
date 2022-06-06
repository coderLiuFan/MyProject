package com.lite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class LiteBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiteBlogApplication.class, args);
    }

}
