package com.search.bean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class SpringConfiguration {

    @Value("${AES.PASSWORD}")
    private String key;
}
