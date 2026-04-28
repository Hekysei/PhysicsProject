package com.example.isib.kirchhoff;

import com.example.isib.kirchhoff.auth.FileUserAccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class KirchhoffSecurityConfiguration {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/kirchhoff/login",
                "/kirchhoff/register",
                "/kirchhoff/css/**",
                "/kirchhoff/js/**",
                "/error",
                "/swagger-ui/**",
                "/v3/api-docs/**")
            .permitAll()
            .anyRequest().authenticated())
        .formLogin(form -> form
            .loginPage("/kirchhoff/login")
            .loginProcessingUrl("/kirchhoff/login")
            .defaultSuccessUrl("/kirchhoff", true)
            .permitAll())
        .logout(logout -> logout
            .logoutSuccessUrl("/kirchhoff/login?logout"));

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
