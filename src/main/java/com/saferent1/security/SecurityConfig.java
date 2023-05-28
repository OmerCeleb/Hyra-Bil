package com.saferent1.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Låt oss berätta för springboot att det är config-klassen
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    //VÅRT SYFTE: SKAPA STRUKTURER SOM ENCODER , PROVIDER , AUTHTOKENFILTER

    // först behöver vi userdetail, så vi skapade variabel från userDetailsService
    @Autowired
    private UserDetailsService userDetailsService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and().
                authorizeRequests().
                antMatchers("/login",
                        "/register",
                        "/",
                        "/index.html").
                permitAll().anyRequest().authenticated();


        return http.build();
    }



}
