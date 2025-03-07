package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService userDetailsService;

    private final AuthenticationSuccessHandler successHandler;

    @Lazy
    public WebSecurityConfig(CustomUserDetailsService userDetailsService, AuthenticationSuccessHandler successHandler) {
        this.userDetailsService = userDetailsService;
        this.successHandler = successHandler;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/login","/register","/addRole","/favicon.ico").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN") // Проверяет ROLE_ADMIN
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN") // Проверяет ROLE_USER и ROLE_ADMIN
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .successHandler(successHandler)
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }
}