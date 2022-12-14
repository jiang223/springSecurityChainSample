//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.chain.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.firewall.FirewalledRequest;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.web.filter.GenericFilterBean;

public class FilterChainProxy extends GenericFilterBean {
    private static final Log logger = LogFactory.getLog(FilterChainProxy.class);
    private static final String FILTER_APPLIED = FilterChainProxy.class.getName().concat(".APPLIED");
    private List<SecurityFilterChain> filterChains;
    private FilterChainValidator filterChainValidator;
    private HttpFirewall firewall;

    public FilterChainProxy() {
        this.filterChainValidator = new NullFilterChainValidator();
        this.firewall = new StrictHttpFirewall();
    }

    public FilterChainProxy(SecurityFilterChain chain) {
        this(Arrays.asList(chain));
    }

    public FilterChainProxy(List<SecurityFilterChain> filterChains) {
        this.filterChainValidator = new NullFilterChainValidator();
        this.firewall = new StrictHttpFirewall();
        this.filterChains = filterChains;
    }

    public void afterPropertiesSet() {
        this.filterChainValidator.validate(this);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean clearContext = request.getAttribute(FILTER_APPLIED) == null;
        if (clearContext) {
            try {
                request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
                this.doFilterInternal(request, response, chain);
            } finally {
                SecurityContextHolder.clearContext();
                request.removeAttribute(FILTER_APPLIED);
            }
        } else {
            this.doFilterInternal(request, response, chain);
        }

    }

    private void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        FirewalledRequest fwRequest = this.firewall.getFirewalledRequest((HttpServletRequest)request);
        HttpServletResponse fwResponse = this.firewall.getFirewalledResponse((HttpServletResponse)response);
        List<Filter> filters = this.getFilters((HttpServletRequest)fwRequest);
        if (filters != null && filters.size() != 0) {
            VirtualFilterChain vfc = new VirtualFilterChain(fwRequest, chain, filters);
            vfc.doFilter(fwRequest, fwResponse);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug(UrlUtils.buildRequestUrl(fwRequest) + (filters == null ? " has no matching filters" : " has an empty filter list"));
            }

            fwRequest.reset();
            if (chain!=null)
            chain.doFilter(fwRequest, fwResponse);
        }
    }

    private List<Filter> getFilters(HttpServletRequest request) {
        Iterator var2 = this.filterChains.iterator();

        SecurityFilterChain chain;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            chain = (SecurityFilterChain)var2.next();
        } while(!chain.matches(request));

        return chain.getFilters();
    }

    public List<Filter> getFilters(String url) {
        return this.getFilters((HttpServletRequest)this.firewall.getFirewalledRequest((new FilterInvocation(url, "GET")).getRequest()));
    }

    public List<SecurityFilterChain> getFilterChains() {
        return Collections.unmodifiableList(this.filterChains);
    }

    public void setFilterChainValidator(FilterChainValidator filterChainValidator) {
        this.filterChainValidator = filterChainValidator;
    }

    public void setFirewall(HttpFirewall firewall) {
        this.firewall = firewall;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FilterChainProxy[");
        sb.append("Filter Chains: ");
        sb.append(this.filterChains);
        sb.append("]");
        return sb.toString();
    }

    private static class NullFilterChainValidator implements FilterChainValidator {
        private NullFilterChainValidator() {
        }

        public void validate(FilterChainProxy filterChainProxy) {
        }
    }

    public interface FilterChainValidator {
        void validate(FilterChainProxy var1);
    }

    private static class VirtualFilterChain implements FilterChain {
        private final FilterChain originalChain;
        private final List<Filter> additionalFilters;
        private final FirewalledRequest firewalledRequest;
        private final int size;
        private int currentPosition;


        private VirtualFilterChain(FirewalledRequest firewalledRequest, FilterChain chain, List<Filter> additionalFilters) {
            this.currentPosition = 0;
            this.originalChain = chain;
            this.additionalFilters = additionalFilters;
            this.size = additionalFilters.size();
            this.firewalledRequest = firewalledRequest;
        }

        public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
            if (this.currentPosition == this.size) {
                if (FilterChainProxy.logger.isDebugEnabled()) {
                    FilterChainProxy.logger.debug(UrlUtils.buildRequestUrl(this.firewalledRequest) + " reached end of additional filter chain; proceeding with original chain");
                }

                this.firewalledRequest.reset();
                if (this.originalChain!=null)
                this.originalChain.doFilter(request, response);
            } else {
                ++this.currentPosition;
                Filter nextFilter = (Filter)this.additionalFilters.get(this.currentPosition - 1);
                if (FilterChainProxy.logger.isDebugEnabled()) {
                    FilterChainProxy.logger.debug(UrlUtils.buildRequestUrl(this.firewalledRequest) + " at position " + this.currentPosition + " of " + this.size + " in additional filter chain; firing Filter: '" + nextFilter.getClass().getSimpleName() + "'");
                }

                nextFilter.doFilter(request, response, this);
            }

        }
    }
}
