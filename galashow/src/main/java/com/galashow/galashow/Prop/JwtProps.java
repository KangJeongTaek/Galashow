package com.galashow.galashow.Prop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@ConfigurationProperties(value = "com.galashow.jwt")
@Data
@Component
public class JwtProps {
    private String secretKey;
}
