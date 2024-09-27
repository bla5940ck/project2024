package com.kucw.security.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
public class MySecurityConfig {

    @Autowired
    private MyOAuth2UserService myOAuth2UserService;

    @Autowired
    private MyOidcUserService myOidcUserService;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
//                .csrf(csrf -> csrf
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                        .csrfTokenRequestHandler(createCsrfHandler())
//                        // csrf 忽略register,index
//                        .ignoringRequestMatchers("/members/register", "/index", "/")
//                        )
                .httpBasic(Customizer.withDefaults() )
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/members/register", "/products").permitAll()
                        .requestMatchers("/welcome", "/products/*").hasAnyRole("ADMIN", "VIP_MEMBER", "NORMAL_MEMBER")
                        .anyRequest().denyAll()
//                        .anyRequest().permitAll()
                )

                // 表單登入（即是使用帳號密碼登入）
                .formLogin(Customizer.withDefaults())


                // OAuth 2.0 社交登入
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(infoEndpoint -> infoEndpoint
                                .userService(myOAuth2UserService)
                                .oidcUserService(myOidcUserService)
                        )
                )
                // OAuth 2.0 Resource sever 的 JWT 驗證
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults()))

                .build();
    }

    private CsrfTokenRequestAttributeHandler createCsrfHandler() {
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
        csrfTokenRequestAttributeHandler.setCsrfRequestAttributeName(null);
        return csrfTokenRequestAttributeHandler;
    }
}
