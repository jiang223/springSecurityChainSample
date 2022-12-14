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
package com.chain.configurers;

import com.chain.HttpSecurityBuilder;
import com.chain.builders.HttpSecurity;
import com.chain.filter.TestFilter;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import java.util.List;
import java.util.UUID;

/**
 * Configures Anonymous authentication (i.e. populate an {@link Authentication} that
 * represents an anonymous user instead of having a null value) for an
 * {@link HttpSecurity}. Specifically this will configure an
 * {@link AnonymousAuthenticationFilter} and an {@link AnonymousAuthenticationProvider}.
 * All properties have reasonable defaults, so no additional configuration is required
 * other than applying this {@link SecurityConfigurer}.
 *
 * @author Rob Winch
 * @since 3.2
 */
public final class TestConfigurer<H extends HttpSecurityBuilder<H>> extends
        AbstractHttpConfigurer<TestConfigurer<H>, H> {
	private String key;
	private AuthenticationProvider authenticationProvider;
	private TestFilter authenticationFilter;
	private Object principal = "anonymousUser";
	private List<GrantedAuthority> authorities = AuthorityUtils
			.createAuthorityList("ROLE_ANONYMOUS");

	/**
	 * Creates a new instance
	 * @see HttpSecurity#anonymous()
	 */
	public TestConfigurer() {
	}

	/**
	 * Sets the key to identify tokens created for anonymous authentication. Default is a
	 * secure randomly generated key.
	 *
	 * @param key the key to identify tokens created for anonymous authentication. Default
	 * is a secure randomly generated key.
	 * @return the {@link TestConfigurer} for further customization of anonymous
	 * authentication
	 */
	public TestConfigurer<H> key(String key) {
		this.key = key;
		return this;
	}

	/**
	 * Sets the principal for {@link Authentication} objects of anonymous users
	 *
	 * @param principal used for the {@link Authentication} object of anonymous users
	 * @return the {@link TestConfigurer} for further customization of anonymous
	 * authentication
	 */
	public TestConfigurer<H> principal(Object principal) {
		this.principal = principal;
		return this;
	}

	/**
	 * Sets the {@link Authentication#getAuthorities()}
	 * for anonymous users
	 *
	 * @param authorities Sets the
	 * {@link Authentication#getAuthorities()} for
	 * anonymous users
	 * @return the {@link TestConfigurer} for further customization of anonymous
	 * authentication
	 */
	public TestConfigurer<H> authorities(List<GrantedAuthority> authorities) {
		this.authorities = authorities;
		return this;
	}

	/**
	 * Sets the {@link Authentication#getAuthorities()}
	 * for anonymous users
	 *
	 * @param authorities Sets the
	 * {@link Authentication#getAuthorities()} for
	 * anonymous users (i.e. "ROLE_ANONYMOUS")
	 * @return the {@link TestConfigurer} for further customization of anonymous
	 * authentication
	 */
	public TestConfigurer<H> authorities(String... authorities) {
		return authorities(AuthorityUtils.createAuthorityList(authorities));
	}

	/**
	 * Sets the {@link AuthenticationProvider} used to validate an anonymous user. If this
	 * is set, no attributes on the {@link TestConfigurer} will be set on the
	 * {@link AuthenticationProvider}.
	 *
	 * @param authenticationProvider the {@link AuthenticationProvider} used to validate
	 * an anonymous user. Default is {@link AnonymousAuthenticationProvider}
	 *
	 * @return the {@link TestConfigurer} for further customization of anonymous
	 * authentication
	 */
	public TestConfigurer<H> authenticationProvider(
			AuthenticationProvider authenticationProvider) {
		this.authenticationProvider = authenticationProvider;
		return this;
	}

	/**
	 * Sets the {@link AnonymousAuthenticationFilter} used to populate an anonymous user.
	 * If this is set, no attributes on the {@link TestConfigurer} will be set on the
	 * {@link AnonymousAuthenticationFilter}.
	 *
	 * @param authenticationFilter the {@link AnonymousAuthenticationFilter} used to
	 * populate an anonymous user.
	 *
	 * @return the {@link TestConfigurer} for further customization of anonymous
	 * authentication
	 */
	public TestConfigurer<H> authenticationFilter(
			TestFilter authenticationFilter) {
		this.authenticationFilter = authenticationFilter;
		return this;
	}

	@Override
	public void init(H http) {
//		if (authenticationProvider == null) {
//			authenticationProvider = new AnonymousAuthenticationProvider(getKey());
//		}
		if (authenticationFilter == null) {
			authenticationFilter = new TestFilter(getKey(), principal,
					authorities);
		}
//		authenticationProvider = postProcess(authenticationProvider);
//		http.authenticationProvider(authenticationProvider);
	}

	@Override
	public void configure(H http) {
		authenticationFilter.afterPropertiesSet();
		http.addFilter(authenticationFilter);
	}

	private String getKey() {
		if (key == null) {
			key = UUID.randomUUID().toString();
		}
		return key;
	}
}
