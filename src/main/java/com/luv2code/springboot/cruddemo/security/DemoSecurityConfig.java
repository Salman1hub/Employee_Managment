package com.luv2code.springboot.cruddemo.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class DemoSecurityConfig {

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){ 
JdbcUserDetailsManager theUserDetailsManager = new JdbcUserDetailsManager(dataSource);
      theUserDetailsManager.setUsersByUsernameQuery(" SELECT user_id,pw, active FROM employee_directory.members where user_id = ? ");
theUserDetailsManager.setAuthoritiesByUsernameQuery("SELECT user_id,role FROM employee_directory.roles where user_id = ? ");
        return theUserDetailsManager ;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws  Exception{
        http.authorizeHttpRequests(configurer ->
                configurer.requestMatchers(HttpMethod.GET,"/api/employees").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET,"/api/employees/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.POST,"/api/employees").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT,"/api/employees/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PATCH,"/api/employees/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE,"/api/employees/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
        );
        http.httpBasic(org.springframework.security.config.Customizer.withDefaults());
       http.csrf(csrf -> csrf.disable());
        return http.build();
    }
}

