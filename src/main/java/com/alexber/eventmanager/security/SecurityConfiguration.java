package com.alexber.eventmanager.security;

import com.alexber.eventmanager.security.exception.CustomAccessDeniedHandler;
import com.alexber.eventmanager.security.exception.CustomAuthenticationEntryPoint;
import com.alexber.eventmanager.security.jwt.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    private final CustomUserDetailService customUserDetailService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final JwtTokenFilter jwtTokenFilter;

    public SecurityConfiguration(CustomUserDetailService customUserDetailService, CustomAuthenticationEntryPoint customAuthenticationEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler, JwtTokenFilter jwtTokenFilter) {
        this.customUserDetailService = customUserDetailService;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(HttpMethod.GET, "/locations")
                                .hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers(HttpMethod.POST, "/locations")
                                .hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/locations/**")
                                .hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/locations/**")
                                .hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers(HttpMethod.PUT, "/locations/**")
                                .hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/users")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/users/**")
                                .hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/users/auth")
                                .permitAll()
                                .requestMatchers(HttpMethod.POST, "/events")
                                .hasAnyAuthority("USER")
                                .requestMatchers(HttpMethod.DELETE, "/events/*")
                                .hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers(HttpMethod.GET, "/events/*")
                                .hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers(HttpMethod.PUT, "/events/*")
                                .hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers(HttpMethod.POST, "/events/search")
                                .hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers(HttpMethod.GET, "/events/my")
                                .hasAnyAuthority("USER")
                                .requestMatchers(HttpMethod.POST, "/events/registrations/*")
                                .hasAnyAuthority("USER")
                                .requestMatchers(HttpMethod.DELETE, "/events/registrations/cancel/*")
                                .hasAnyAuthority("USER")
                                .requestMatchers(HttpMethod.GET, "/events/registrations/my")
                                .hasAnyAuthority("USER")
                                .requestMatchers(
                                        "/v3/api-docs/**",
                                        "/swagger-ui.html",
                                        "/swagger-ui/**",
                                        "/openapi.yaml"
                                ).permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling(exceptions ->
                        exceptions
                                .authenticationEntryPoint(customAuthenticationEntryPoint)
                                .accessDeniedHandler(customAccessDeniedHandler)
                )
                .addFilterBefore(jwtTokenFilter, AnonymousAuthenticationFilter.class)
                .build();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(customUserDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
