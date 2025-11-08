package com.alpms.al_paper_management.config;


import com.alpms.al_paper_management.auth.model.User;
import com.alpms.al_paper_management.auth.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(UserRepository users) {
        return username -> {
            User u = users.findByEmailIgnoreCase(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return new org.springframework.security.core.userdetails.User(
                    u.getEmail(),
                    u.getPassword(),
                    // boolean getter for a boolean field:
                    u.isEnabled(),
                    true,
                    true,
                    true,
                    u.authorities()
            );
        };
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/auth/**", "/css/**", "/js/**","/images/**").permitAll()
                        .requestMatchers("/papers", "/papers/").authenticated()          // list
                        .requestMatchers("/papers/upload", "/papers/{id}/delete", "/papers").hasAnyRole("ADMIN","TEACHER")
                        .requestMatchers("/subjects/**").hasAnyRole("ADMIN","TEACHER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/student/**").authenticated()

                        .anyRequest().authenticated()
                )

                .formLogin(login -> login
                        .loginPage("/auth/login")          // GET  -> render login page
                        .loginProcessingUrl("/auth/login") // POST -> Spring Security processes credentials here
                        .failureUrl("/auth/login?error")   // on bad credentials
                        .defaultSuccessUrl("/?success", true)      // on success
                        .permitAll()
                )

                .logout(Customizer.withDefaults());
        // CSRF is ON by default for form posts. No need to call .csrf(...)

        return http.build();
    }
}
