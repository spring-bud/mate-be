package com.example.mate.common.config;

import com.example.mate.auth.infrastructure.security.JwtAuthenticationFilter;
import com.example.mate.auth.infrastructure.security.JwtAuthenticationProvider;
import com.example.mate.auth.infrastructure.security.handler.CustomAuthenticationEntryPoint;
import com.example.mate.auth.infrastructure.security.oauth.CustomAuthorizationRequestRepository;
import com.example.mate.auth.infrastructure.security.oauth.OAuthSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuthSuccessHandler oAuthSuccessHandler;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAuthorizationRequestRepository customAuthorizationRequestRepository;

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChainBasic(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/actuator/**")   //엔드포인트 보안을 처리합니다
                .cors(Customizer.withDefaults())            //비활성화
                .csrf(AbstractHttpConfigurer::disable)      //비활성화
                .logout(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(basic -> basic.authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }


    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChainJwt(HttpSecurity http) throws Exception {

        return http
                .securityMatcher("/**")
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/index.html").permitAll()
                        .requestMatchers("/websocket.html").permitAll()
                        .requestMatchers("/api/v1/auth/reissue").permitAll()
                        .requestMatchers("/oauth2/authorization/kakao").permitAll()
                        .requestMatchers("/api/v1/users/{userId}").permitAll()
                        .requestMatchers("/api/v1/products/srch").permitAll()
                        .requestMatchers("/api/v1/products/{productId}").permitAll()
                        .requestMatchers("/api/v1/products/users/{userId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/products").permitAll()
                        .requestMatchers("/api/v1/products/tagName").permitAll()
                        .requestMatchers("/api/v1/products/mostTag").permitAll()
                        .requestMatchers("/api/v1/users/popularity").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(new DefaultOAuth2UserService())
                        )
                        .authorizationEndpoint(authorizationEndpointConfig -> authorizationEndpointConfig
                                .authorizationRequestRepository(customAuthorizationRequestRepository)
                        )
                        .successHandler(oAuthSuccessHandler)
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    private JwtAuthenticationFilter jwtAuthenticationFilter() {
        ProviderManager providerManager = new ProviderManager(jwtAuthenticationProvider);
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(generatedRequestMatcher());
        filter.setAuthenticationManager(providerManager);
        filter.setAuthenticationFailureHandler(new AuthenticationEntryPointFailureHandler(authenticationEntryPoint));
        return filter;
    }

    //jwt 인증을 제외할 경로 설정
    private RequestMatcher generatedRequestMatcher() {
        return new NegatedRequestMatcher(
                new OrRequestMatcher(
                        new AntPathRequestMatcher("/"),
                        new AntPathRequestMatcher("/ws/**"),
                        new AntPathRequestMatcher("/swagger-ui/**"),
                        new AntPathRequestMatcher("/swagger-ui.html"),
                        new AntPathRequestMatcher("/v3/api-docs/**"),
                        new AntPathRequestMatcher("/index.html"),
                        new AntPathRequestMatcher("/websocket.html"),
                        new AntPathRequestMatcher("/api/v1/auth/reissue"),
                        new AntPathRequestMatcher("/oauth2/authorization/kakao"),
                        new AntPathRequestMatcher("/api/v1/users/{userId}", "GET"),
                        new AntPathRequestMatcher("/api/v1/products/srch"),
                        new AntPathRequestMatcher("/api/v1/products/{productId}", "GET"),
                        new AntPathRequestMatcher("/api/v1/products/users/{userId}", "GET"),
                        new AntPathRequestMatcher("/api/v1/products", "GET"),
                        new AntPathRequestMatcher("/api/v1/products/tagName", "PSOT"),
                        new AntPathRequestMatcher("/api/v1/products/mostTag", "POST"),
                        new AntPathRequestMatcher("/api/v1/users/popularity", "GET")
                )
        );
    }

}
