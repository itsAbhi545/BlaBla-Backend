package com.example.BlaBlaBackend.config;

import com.example.BlaBlaBackend.Dto.ApiResponse;
import com.example.BlaBlaBackend.EntryPoint.CustomAuthenticationEntryPoint;
import com.example.BlaBlaBackend.Filters.CustomAuthenticationFilter;
import com.example.BlaBlaBackend.Filters.CustomAuthorizationFilter;
import com.example.BlaBlaBackend.service.UserService;
import com.example.BlaBlaBackend.service.UserTokensService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecConfig {
    private final UserDetailsService userDetailsService;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final UserTokensService userTokensService;

    public SecConfig(UserDetailsService userDetailsService, AuthenticationEntryPoint authenticationEntryPoint, JwtProvider jwtProvider, UserService userService, UserTokensService userTokensService) {
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.userTokensService = userTokensService;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        //Authentication Entry point!!!
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        return authProvider;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter filter=new CustomAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)),jwtProvider,userService,userTokensService);
        filter.setFilterProcessesUrl("/api/login");
        http.csrf().disable();//withDefaults()//110001000
        http.cors().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeHttpRequests().requestMatchers("/api/login","/api/signup","/health-check","/favicon.ico","/error").permitAll();
        http.authorizeHttpRequests().requestMatchers("/api/verify-user/email").permitAll();
        http.authorizeHttpRequests().anyRequest().authenticated();
        http.addFilter(filter);
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        http.addFilterBefore(new CustomAuthorizationFilter(userTokensService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    //This is must if we want to stop generating security password by system!!!
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public ApiResponse createApiResponse(){
        return new ApiResponse();
    }
}
