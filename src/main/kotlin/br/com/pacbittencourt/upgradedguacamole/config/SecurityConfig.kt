package br.com.pacbittencourt.upgradedguacamole.config

import br.com.pacbittencourt.upgradedguacamole.secutiry.jwt.JwtTokenFilter
import br.com.pacbittencourt.upgradedguacamole.secutiry.jwt.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
@Configuration
class SecurityConfig {

    @Autowired
    private lateinit var tokenProvider: JwtTokenProvider

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        val encoders: MutableMap<String, PasswordEncoder> = hashMapOf()
        val pbkdf2PasswordEncoder = Pbkdf2PasswordEncoder("", 8, 185000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256)
        encoders["pbkdf2"] = pbkdf2PasswordEncoder
        val passwordEncoder = DelegatingPasswordEncoder("pbkdf2", encoders)
        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2PasswordEncoder)
        return passwordEncoder
    }

    @Bean
    fun authenticationManagerBean(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val customFilter = JwtTokenFilter(tokenProvider)
        return http
                .httpBasic { basic -> basic.disable() }
                .csrf { obj -> obj.disable() }
                .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter::class.java)
                .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
                .authorizeHttpRequests { auth ->
                    auth.requestMatchers(
                            "/auth/signin",
                            "/auth/refresh/**",
                            "/v3/api-docs/**",
                            "/swagger-ui/**"
                    )
                            .permitAll()
                            .requestMatchers("/api/**").authenticated()
                            .requestMatchers("/users").denyAll()
                }
                .cors { }
                .build()
    }

}