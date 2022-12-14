/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chain;

import com.chain.filter.Filter;

import com.chain.annotation.SecurityBuilder;
import com.chain.annotation.SecurityConfigurer;
import com.chain.builders.WebSecurity;
import com.chain.configuration.EnableWebSecurity;
import com.chain.configuration.WebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Configuration;


/**
 * Allows customization to the {@link WebSecurity}. In most instances users will use
 * {@link EnableWebSecurity} and a create {@link Configuration} that extends
 * {@link WebSecurityConfigurerAdapter} which will automatically be applied to the
 * {@link WebSecurity} by the {@link EnableWebSecurity} annotation.
 *
 * @see WebSecurityConfigurerAdapter
 *
 * @author Rob Winch
 * @since 3.2
 */
public interface WebSecurityConfigurer<T extends SecurityBuilder<Filter>> extends
		SecurityConfigurer<Filter, T> {

}
