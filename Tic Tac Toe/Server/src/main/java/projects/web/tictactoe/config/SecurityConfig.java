package projects.web.tictactoe.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
      @Value("${data.origin}")
      private String origin;

      @Bean
      public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
            httpSecurity
                    .csrf(AbstractHttpConfigurer::disable)
                    .cors(Customizer.withDefaults())
                    .exceptionHandling(exception -> exception
                            .authenticationEntryPoint(
                                    (req, res, ex) -> res.sendError(
                                            HttpServletResponse.SC_UNAUTHORIZED,
                                            ex.getMessage()
                                    )
                            )
                    )
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers( "/account", "/ws/**").permitAll()
                            .anyRequest().authenticated()
                    )
                    .headers(headers -> headers
                            .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                    )
                    .httpBasic(Customizer.withDefaults());
            return httpSecurity.build();
      }

      @Bean
      public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.addAllowedOrigin(origin);
            configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
            configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
            configuration.setAllowCredentials(true);

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return source;
      }

      @Bean
      public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
      }
}
