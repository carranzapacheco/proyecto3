package com.example.colegio.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dxoxve2wv",
                "api_key", "385218441493673",
                "api_secret", "UmLkabCbG9cnallgDye4eTix1B4",
                "secure", true
        ));
    }
}
