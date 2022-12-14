//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.chain.filter;

import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

public class TestFilter extends GenericFilterBean implements InitializingBean {
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;
    private String key;
    private Object principal;
    private List<GrantedAuthority> authorities;

    public TestFilter(String key) {
        this(key, "anonymousUser", AuthorityUtils.createAuthorityList(new String[]{"ROLE_ANONYMOUS"}));
    }

    public TestFilter(String key, Object principal, List<GrantedAuthority> authorities) {
       // this.authenticationDetailsSource = new WebAuthenticationDetailsSource();
        Assert.hasLength(key, "key cannot be null or empty");
        Assert.notNull(principal, "Anonymous authentication principal must be set");
        Assert.notNull(authorities, "Anonymous authorities must be set");
        this.key = key;
        this.principal = principal;
        this.authorities = authorities;
    }

    public void afterPropertiesSet() {
        Assert.hasLength(this.key, "key must have length");
        Assert.notNull(this.principal, "Anonymous authentication principal must be set");
        //Assert.notNull(this.authorities, "Anonymous authorities must be set");
        logger.info("Anonymous authorities must be set");
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        logger.info("Test is call!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            SecurityContextHolder.getContext().setAuthentication(this.createAuthentication((HttpServletRequest)req));
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Populated SecurityContextHolder with anonymous token: '" + SecurityContextHolder.getContext().getAuthentication() + "'");
            }
        } else if (this.logger.isDebugEnabled()) {
            this.logger.debug("SecurityContextHolder not populated with anonymous token, as it already contained: '" + SecurityContextHolder.getContext().getAuthentication() + "'");
        }

        chain.doFilter(req, res);
    }

    protected Authentication createAuthentication(HttpServletRequest request) {
        AnonymousAuthenticationToken auth = new AnonymousAuthenticationToken(this.key, this.principal, this.authorities);
        auth.setDetails(this.authenticationDetailsSource.buildDetails(request));
        return auth;
    }

    public void setAuthenticationDetailsSource(AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        Assert.notNull(authenticationDetailsSource, "AuthenticationDetailsSource required");
        this.authenticationDetailsSource = authenticationDetailsSource;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public List<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }
}
