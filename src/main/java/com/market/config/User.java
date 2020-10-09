package com.market.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@Configuration
public class User {

    @Value("${jq.login.name}")
    private String name;

    @Value("${jq.login.password}")
    private String password;

}
