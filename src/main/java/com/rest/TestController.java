package com.rest;

import com.chain.web.FilterChainProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.*;
import java.io.IOException;

@RestController
@RequestMapping("/menu")
public class TestController {
    @Qualifier("springSecurityFilterChain2")
    @Autowired
    Filter filter;

    @GetMapping
    public String a(ServletRequest var1, ServletResponse var2) throws ServletException, IOException {
        filter.doFilter(var1,var2,null);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return "a"+user.toString();


    }
    @GetMapping("/b")
    public String b(){
        return "b";
    }
}
