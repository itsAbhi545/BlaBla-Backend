package com.example.BlaBlaBackend.config;

import com.example.BlaBlaBackend.Dto.ApiResponse;
import com.example.BlaBlaBackend.EntryPoint.CustomAuthenticationEntryPoint;
import com.example.BlaBlaBackend.Filters.CustomAuthenticationFilter;
import com.example.BlaBlaBackend.Filters.CustomAuthorizationFilter;
import com.example.BlaBlaBackend.authenticationFailureHandler.CustomAuthenticationFailureHandler;
import com.example.BlaBlaBackend.service.UserProfileService;
import com.example.BlaBlaBackend.service.UserService;
import com.example.BlaBlaBackend.service.UserTokensService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
public class SecConfig {
    private final UserDetailsService userDetailsService;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final UserTokensService userTokensService;
    private final ObjectMapper objectMapper;
    private final UserProfileService userProfileService;
//    private final CustomAuthenticationFailureHandler authenticationFailureHandler;
    

    public SecConfig(UserDetailsService userDetailsService, CustomAuthenticationEntryPoint authenticationEntryPoint, JwtProvider jwtProvider, UserService userService, UserTokensService userTokensService, ObjectMapper objectMapper, UserProfileService userProfileService) {
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.userTokensService = userTokensService;
        this.objectMapper = objectMapper;
        this.userProfileService = userProfileService;
//        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        //Authentication Entry point!!!
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        authProvider.setHideUserNotFoundExceptions(false);
        return authProvider;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter filter=new CustomAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)),jwtProvider,userService,userTokensService,objectMapper,userProfileService);
        filter.setFilterProcessesUrl("/api/login");
        http.csrf().disable().cors().configurationSource(corsConfigurationSource());
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeHttpRequests().requestMatchers("/api/login","/api/signup","/health-check","/favicon.ico","/error").permitAll();
        http.authorizeHttpRequests().requestMatchers("/api/verify-user/email/*","/api/confirm-account/*","/images/**").permitAll();
        http.authorizeHttpRequests().requestMatchers("/api/forgetPassword","/api/resetPassword").permitAll();
        http.authorizeHttpRequests().requestMatchers("/api/verify-user/email","/api/confirm-account/*").permitAll();
        http.authorizeHttpRequests().requestMatchers("/api/forgetPassword","/api/resetPassword").permitAll();

        http.authorizeHttpRequests().anyRequest().authenticated();
        http.addFilter(filter);

        http.exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());
        http.addFilterBefore(new CustomAuthorizationFilter(userTokensService,jwtProvider), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    //This is must if we want to stop generating security password by system!!!
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final var configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");

        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("*"));

        final var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }
}
