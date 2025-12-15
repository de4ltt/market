package ru.market.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // отключаем CSRF (для Postman)
                .csrf(csrf -> csrf.disable())

                // правила доступа
                .authorizeHttpRequests(auth -> auth

                                // открытые 
                                .requestMatchers("/auth/**").permitAll()

                                // // справочники должностей
                                // .requestMatchers("/positions/**")
                                // .hasAnyRole("DIRECTOR", "COMMODITY_EXPERT")

                                // // переработки
                                // .requestMatchers("/overtime/**")
                                // .hasRole("DIRECTOR")

                                // // отпуска
                                // .requestMatchers("/vacations/**")
                                // .hasAnyRole("SELLER", "STOREKEEPER")

                                // // всё остальное — авторизация
                                // .anyRequest().authenticated()
                                .anyRequest().permitAll()
                )

                .httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
