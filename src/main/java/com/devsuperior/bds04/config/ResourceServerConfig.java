package com.devsuperior.bds04.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer // process configuration to implements OAuth2 resource server
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	@Autowired
	private Environment env;

	@Autowired
	private JwtTokenStore tokenStore;

	// open area
	private static final String[] PUBLIC_ROUTES = {
			"/oauth/token",
			"/h2-console/**"};
	private static final String[] GET_ROUTES= {
			"/cities/**",
			"/events/**"/* add more as needed */ };
	// /** means everything from that resource
	private static final String[] POST_CLIENT_ADMIN = {
			"/events/**" }; // only for admins and operators


	@Override
	public void configure(ResourceServerSecurityConfigurer resources)
			throws Exception {
		resources.tokenStore(tokenStore);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		//H2
		if(Arrays.asList(env.getActiveProfiles() ).contains("test")){
			http.headers().frameOptions().disable();
		}
		
		http.authorizeRequests()
				.antMatchers(PUBLIC_ROUTES).permitAll() // for all resources listed in PUBLIC, free access
				.antMatchers(HttpMethod.GET, GET_ROUTES).permitAll()
				.antMatchers(HttpMethod.POST, POST_CLIENT_ADMIN).hasAnyRole("CLIENT","ADMIN") 
				.anyRequest().hasRole("ADMIN"); //for any other resource, user must be authenticated
	}

}
