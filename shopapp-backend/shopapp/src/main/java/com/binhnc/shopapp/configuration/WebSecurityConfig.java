package com.binhnc.shopapp.configuration;

import com.binhnc.shopapp.filter.JwtTokenFilter;
import com.binhnc.shopapp.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
//@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers(
                                    String.format("%s/users/register", apiPrefix),
                                    String.format("%s/users/login", apiPrefix)
                            )
                            .permitAll()
                            // Category
                            .requestMatchers(GET,
                                    String.format("%s/categories**", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)
                            .requestMatchers(POST,
                                    String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(PUT,
                                    String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(DELETE,
                                    String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)
                            // Product
                            .requestMatchers(GET,
                                    String.format("%s/products**", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)
                            .requestMatchers(POST,
                                    String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(PUT,
                                    String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(DELETE,
                                    String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)
                            // Order
                            .requestMatchers(GET,
                                    String.format("%s/orders/**", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)
                            .requestMatchers(POST,
                                    String.format("%s/orders/**", apiPrefix)).hasAnyRole(Role.USER)
                            .requestMatchers(PUT,
                                    String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(DELETE,
                                    String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)
                            // Order detail
                            .requestMatchers(GET,
                                    String.format("%s/order_details/**", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)
                            .requestMatchers(POST,
                                    String.format("%s/order_details/**", apiPrefix)).hasAnyRole(Role.USER)
                            .requestMatchers(PUT,
                                    String.format("%s/order_details/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(DELETE,
                                    String.format("%s/order_details/**", apiPrefix)).hasRole(Role.ADMIN)
                            .anyRequest().authenticated();
                });
        return http.build();
    }
}
