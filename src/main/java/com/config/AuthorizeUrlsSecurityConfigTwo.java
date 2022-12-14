package com.config;

import com.chain.builders.HttpSecurity;
import com.chain.configuration.WebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AuthorizeUrlsSecurityConfigTwo extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomSecurityMetadataSource filterInvocationSecurityMetadataSource;
    protected AuthorizeUrlsSecurityConfigTwo() {
        super(true);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.test();


    }

}

