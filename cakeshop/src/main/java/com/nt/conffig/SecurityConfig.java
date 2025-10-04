package com.nt.conffig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.nt.repository.IUserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final IUserRepository userRepository;

    public SecurityConfig(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/", "/cakes", "/about", "/cart", "/buy", "/add-to-cart",
                                 "/generate-bill", "/submit-contact", "/success",
                                 "/login", "/register", "/logout",
                                 "/css/**", "/js/**", "/uploads/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/cakes", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable()); // disable CSRF for testing only

        return http.build();
    }

    /**
     * Combine in-memory admin + DB users
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        // 1️⃣ In-memory admin user
        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        // 2️⃣ Custom DB-based user loader
        return new InMemoryUserDetailsManager(admin) {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                // First, check if it’s the in-memory admin
                if (username.equals("admin")) {
                    return admin;
                }

                // Otherwise, load from DB
                com.nt.model.User user = userRepository.findByUsername(username);
                if (user == null) {
                    throw new UsernameNotFoundException("User not found: " + username);
                }

                return User.withUsername(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole().replace("ROLE_", ""))
                        .build();
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
