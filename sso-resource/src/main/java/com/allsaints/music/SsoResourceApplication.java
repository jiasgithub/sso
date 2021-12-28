package com.allsaints.music;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@EnableCreateCacheAnnotation
public class SsoResourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoResourceApplication.class, args);
    }

}
