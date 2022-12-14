//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.chain.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.chain.filter.Filter;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.web.util.matcher.RequestMatcher;

public final class DefaultSecurityFilterChain implements SecurityFilterChain {
    private static final Log logger = LogFactory.getLog(DefaultSecurityFilterChain.class);
    private final RequestMatcher requestMatcher;
    private final List<Filter> filters;

    public DefaultSecurityFilterChain(RequestMatcher requestMatcher, Filter... filters) {
        this(requestMatcher, Arrays.asList(filters));
    }

    public DefaultSecurityFilterChain(RequestMatcher requestMatcher, List<Filter> filters) {
        logger.info("Creating filter chain: " + requestMatcher + ", " + filters);
        this.requestMatcher = requestMatcher;
        this.filters = new ArrayList(filters);
    }

    public RequestMatcher getRequestMatcher() {
        return this.requestMatcher;
    }

    public List<Filter> getFilters() {
        return this.filters;
    }

    public boolean matches(HttpServletRequest request) {
        return this.requestMatcher.matches(request);
    }

    public String toString() {
        return "[ " + this.requestMatcher + ", " + this.filters + "]";
    }
}
