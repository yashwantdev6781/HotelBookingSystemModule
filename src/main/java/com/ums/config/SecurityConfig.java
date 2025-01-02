package com.ums.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class SecurityConfig {
    private JWTResponseFilter jwtResponseFilter;

    public SecurityConfig(JWTResponseFilter jwtResponseFilter) {
        this.jwtResponseFilter = jwtResponseFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        http.csrf().disable().cors().disable();
        http.addFilterBefore(jwtResponseFilter, AuthorizationFilter.class);
        http.authorizeHttpRequests().anyRequest().permitAll();
              //  requestMatchers("/api/v1/auth/login","/api/v1/auth/addUser")
             //   .permitAll().requestMatchers("api/v1/countries/addCountry")
               // .hasRole("ADMIN").requestMatchers("api/v1/users/profile").hasAnyRole("ADMIN","USER")
                //anyRequest().authenticated();
        return http.build();
    }
}
