package com.nookbook.backend.config

import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
open class SecurityConfiguration(private val keys: RSAKeyProperties) {
    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { csrf -> csrf.disable() }
        http.authorizeHttpRequests { authorize ->
            authorize
                .requestMatchers("/auth/**").permitAll() // for all users on stage of authentication
                .requestMatchers(HttpMethod.GET, "/user/picture").permitAll()
                .requestMatchers(HttpMethod.GET, "/user/friend/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "user/friend").authenticated()
                .requestMatchers(HttpMethod.POST, "/user/picture/upload").authenticated()
                .anyRequest().authenticated() // everything else should be authenticated
        }
        http.oauth2ResourceServer { oauth2 -> oauth2.jwt(Customizer.withDefaults()) }
        http.sessionManagement({ session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) })

        return http.build()
    }

    @Bean
    open fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    open fun authManager(userDetailsService: UserDetailsService): AuthenticationManager {
        val provider: DaoAuthenticationProvider = DaoAuthenticationProvider()

        provider.setUserDetailsService(userDetailsService)
        provider.setPasswordEncoder(passwordEncoder())

        return ProviderManager(provider)
    }

    @Bean
    open fun jwtDecoder(): JwtDecoder {
        return NimbusJwtDecoder.withPublicKey(keys.publicKey).build()
    }

    @Bean
    open fun jwtEncoder(): JwtEncoder {
        val jwk: JWK = RSAKey.Builder(keys.publicKey).privateKey(keys.privateKey).build()
        val jwks: JWKSource<SecurityContext> = ImmutableJWKSet<SecurityContext>(JWKSet(jwk))
        return NimbusJwtEncoder(jwks)
    }
}