package mx.ubam.inventario.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            org.springframework.security.crypto.password.PasswordEncoder encoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(encoder);
        return new ProviderManager(provider);
    }

    // 1) API (JWT)
    @Bean
    @Order(1)
    public SecurityFilterChain apiChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // Auth público
                        .requestMatchers("/api/auth/**").permitAll()

                        // ✅ Productos: ver productos (cliente/admin)
                        .requestMatchers(HttpMethod.GET, "/api/products/**")
                        .hasAnyRole("CLIENT", "ADMIN")

                        // ✅ Productos: alta/editar/eliminar (solo admin)
                        .requestMatchers(HttpMethod.POST, "/api/products/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/products/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**")
                        .hasRole("ADMIN")

                        // ✅ Ventas: crear compra (cliente/admin)
                        .requestMatchers(HttpMethod.POST, "/api/sales/**")
                        .hasAnyRole("CLIENT", "ADMIN")

                        // ✅ Ventas: consultar ventas (solo admin)
                        .requestMatchers(HttpMethod.GET, "/api/sales/**")
                        .hasRole("ADMIN")

                        // Todo lo demás requiere estar autenticado con token
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 2) ADMIN (web)
    @Bean
    @Order(2)
    public SecurityFilterChain adminChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/admin/**", "/login", "/logout", "/", "/redirect", "/css/**", "/img/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/css/**", "/img/**").permitAll()
                        .requestMatchers("/login").permitAll()
                        .anyRequest().hasRole("ADMIN")
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/redirect", true)
                        .permitAll()
                )
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout"))
                .csrf(Customizer.withDefaults());

        return http.build();
    }

    // 3) CLIENT (web)
    @Bean
    @Order(3)
    public SecurityFilterChain clientChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/client/**", "/logout", "/css/**", "/img/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/client/login", "/css/**", "/img/**").permitAll()
                        .anyRequest().hasRole("CLIENT")
                )
                .formLogin(form -> form
                        .loginPage("/client/login")
                        .loginProcessingUrl("/client/login")
                        .defaultSuccessUrl("/client/products", true)
                        .permitAll()
                )
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/client/login?logout"))
                .csrf(Customizer.withDefaults()); // CSRF ON

        return http.build();
    }

    // 4) Fallback
    @Bean
    @Order(4)
    public SecurityFilterChain otherChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}
