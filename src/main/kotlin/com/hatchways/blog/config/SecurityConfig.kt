package com.hatchways.blog.config

import com.hatchways.blog.util.AuthUtil
import com.hatchways.blog.exception.JwtAuthenticationEntryPoint
import com.hatchways.blog.service.UserService
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver
import org.springframework.security.oauth2.server.resource.web.HeaderBearerTokenResolver
import org.springframework.security.web.SecurityFilterChain
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val userService: UserService,
    private val unauthorizedHandler: JwtAuthenticationEntryPoint
) {

    @Value("\${blog.security.public-key}")
    lateinit var rsaPublicKey: RSAPublicKey

    @Value("\${blog.security.private-key}")
    lateinit var rsaPrivateKey: RSAPrivateKey

    @Value("\${blog.security.token.issuer}")
    private lateinit var tokenIssuer: String

    @Value("\${blog.security.token.expiration-time}")
    private var tokenExpirationTime: Int = 0

    companion object {
        const val AUTHENTICATION_HEADER = "x-access-token"
    }

    @Bean
    fun configure(http: HttpSecurity): SecurityFilterChain {
        http {
            cors { disable() }
            csrf { disable() }
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }
            authorizeRequests {
                authorize("/api/login", permitAll)
                authorize("/api/register", permitAll)
                authorize(anyRequest, authenticated)
            }
            oauth2ResourceServer { jwt {} }
            exceptionHandling { authenticationEntryPoint = unauthorizedHandler }
        }
        return http.build()
    }

    @Bean
    fun authenticationManagerBean(config: AuthenticationConfiguration): AuthenticationManager {
        // The userDetailsService and passwordEncoder are automatically picked up and used if we add this
        return config.authenticationManager
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build()
    }

    @Bean
    fun jwtEncoder(): JwtEncoder {
        val jwk: JWK = RSAKey.Builder(rsaPublicKey).privateKey(rsaPrivateKey).build()
        val jwkSet: JWKSet = JWKSet(jwk)
        val jwkSource: JWKSource<SecurityContext> = ImmutableJWKSet(jwkSet)
        return NimbusJwtEncoder(jwkSource)
    }

    @Bean
    fun authUtilBean(): AuthUtil {
        return AuthUtil(tokenIssuer, tokenExpirationTime, jwtEncoder())
    }

    @Bean
    fun bearerTokenResolver(): BearerTokenResolver {
        return HeaderBearerTokenResolver(AUTHENTICATION_HEADER)
    }
}
