package com.example.products.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/","/search",
                    "/sort-product", "/sort-product-by-category", "/sort-product-by-name", "/sort-product-by-description", "/sort-product-by-price",
                    "/sell-product/**" ,"/api/auth/login", "/login", "/process-login", "/css/**", "/images/**").permitAll()
                .anyRequest().authenticated()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            )
            .addFilterBefore(new JwtSessionAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
