package com.nookbook.backend.core.services

import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class TokenService(private val jwtEncoder: JwtEncoder, private val jwtDecoder: JwtDecoder) {
    fun generateToken(auth: Authentication): String {
        val now = Instant.now()

        val scope: String = auth.authorities.joinToString(" ") { it.authority }

        val claims =
            JwtClaimsSet.builder().issuer("self").issuedAt(now).subject(auth.name).claim("scope", scope).build()

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).tokenValue
    }

    fun getUsernameFromToken(token: String): String {
        if (token.substring(0, 6) != "Bearer")
            throw InvalidBearerTokenException("Token is not a Bearer token")

        val strippedToken = token.substring(7)
        val decoded = jwtDecoder.decode(strippedToken)
        val username = decoded.subject
        return username
    }
}