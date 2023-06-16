package me.pvaldes.blog.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(var jwtAuthConverter: JwtAuthConverter) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .cors(withDefaults())
            .csrf {
                csrf -> csrf.disable()
            }
            .authorizeHttpRequests { authorizeHttpRequests ->
                authorizeHttpRequests
                    .requestMatchers(HttpMethod.GET, "/api/V1/blogpost", "/api/V1/blogpost/**",
                            "/actuator/**").permitAll()
                    .requestMatchers("/api/V1/blogpost", "/api/V1/blogpost/**").authenticated()//.hasRole("ADMIN")
                    .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2ResourceServer { oauth2ResourceServer ->
                oauth2ResourceServer.jwt { jwt ->
                    jwt.jwtAuthenticationConverter(
                            jwtAuthConverter
                    )
                }
            }
            .sessionManagement { sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:3000")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
        configuration.allowedHeaders = listOf("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/api/**", configuration)
        return source
    }

    companion object {
        val BLOG_USER: String = "BLOG_USER"
        val BLOG_MANAGER: String = "BLOG_MANAGER"
    }
}