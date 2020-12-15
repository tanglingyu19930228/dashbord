package com.search.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.function.Supplier;

@Configuration
public class WebConfiguration {

    @Bean
    public Filter webFilter() {
        Supplier<WebFilter> webFilterSupplier = WebFilter::new;
        return webFilterSupplier.get();
    }

    @Bean
    public FilterRegistrationBean webFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new DelegatingFilterProxy("webFilter"));
        registration.setName("webFilter");
        registration.addUrlPatterns("/*");
        return registration;
    }
}
