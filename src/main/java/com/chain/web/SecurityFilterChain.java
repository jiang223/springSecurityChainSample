//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.chain.web;

import java.util.List;
import com.chain.filter.Filter;
import javax.servlet.http.HttpServletRequest;

public interface SecurityFilterChain {
    boolean matches(HttpServletRequest var1);

    List<Filter> getFilters();
}
