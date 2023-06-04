package com.posgrado.ecommerce.security;

import com.posgrado.ecommerce.security.jwt.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AllArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

  private JwtAuthenticationFilter jwtAuthenticationFilter;

  private static final String[] SWAGGER_WHITE_LIST = {
      "/v3/api-docs/**",
      "/swagger-ui/**",
      "/swagger-ui.html",
      "/swagger-resources/**",
      "/webjars/**",
      "/configuration/**"
  };

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf().disable();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.authorizeHttpRequests()
        .requestMatchers(SWAGGER_WHITE_LIST).permitAll()
        .requestMatchers("/auth/**").permitAll()
        .requestMatchers(HttpMethod.GET,"/categories/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
        .requestMatchers(HttpMethod.GET,"auth/**").permitAll()
        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
        .requestMatchers(HttpMethod.POST, "/products").hasAuthority("ADMIN")
        .requestMatchers(HttpMethod.GET, "/roles/**").hasAuthority("ADMIN")
        .requestMatchers(HttpMethod.GET, "/users/**").hasAuthority("ADMIN")
        .requestMatchers(HttpMethod.POST, "/orders/**").hasAuthority("USER")
        .anyRequest().authenticated();
    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
