package com.james.shorturl;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@Configurable
@EnableCaching
public class ShorturlApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShorturlApplication.class, args);
    }


}
