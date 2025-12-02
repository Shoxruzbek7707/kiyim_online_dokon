package uz.pdp.kiyim_online_dokon.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uz.pdp.kiyim_online_dokon.jwt.JwtFilter;
import uz.pdp.kiyim_online_dokon.security.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // @PreAuthorize ishlashi uchun
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CSRF ni o'chirish
        http.csrf(AbstractHttpConfigurer::disable);

        // Authorization sozlamalari
        http.authorizeHttpRequests(auth -> auth
                // ✅ Swagger va API Documentation yo'llari ochiq
                .requestMatchers(
                        "/",
                        "/api/v1/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/configuration/**"
                ).permitAll()

                // ✅ Auth endpointlari ochiq (register, login)
                .requestMatchers("/api/auth/**").permitAll()

                // ✅ Qolgan barcha endpointlar JWT bilan himoyalangan
                .anyRequest().authenticated()
        );

        // Session yaratmaslik (JWT uchun)
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // JWT Filter qo'shish
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}