package com.search.bean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author tanglingyu
 */
@Component
@Data
public class SpringConfiguration {

    @Value("${EMAIL_REGEXP}")
    private String email;
}
