package com.alpms.al_paper_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/subjects/**").authenticated()   // protect sample CRUD
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults()) // default login page
                .logout(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable()); // enable later when you add forms with _csrf

        return http.build();
    }
}
