package com.project.chatkeep.config;

import com.project.chatkeep.jwt.TokenAuthenticationFilter;
import com.project.chatkeep.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;

    /**
     * 정적 리소스에 대한 스프링 시큐리티 사용을 비활성화
     */
    @Bean
    public WebSecurityCustomizer configure() {
        return web -> web.ignoring()
                // 정적 리소스에 대한 스프링 시큐리티 사용을 비활성화
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /**
     * 특정 HTTP 요청에 대한 웹 기반 보안 구성
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic(basic->basic.disable())
                .formLogin(form -> form.disable())
                .csrf(csrf->csrf.disable())
                .cors(cors -> cors
                        .configurationSource(request -> {
                            CorsConfiguration config = new CorsConfiguration();
                            config.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
                            config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE","PATCH"));
                            config.setAllowedHeaders(Arrays.asList("*"));
                            return config;
                        }))
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/api/auth/**").permitAll();
                    authorize.anyRequest().authenticated();}
                )

                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }


}