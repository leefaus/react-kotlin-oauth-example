package io.kubikl.omniscient.security

import com.nimbusds.jose.JWSAlgorithm.HS512
import io.jsonwebtoken.*
import io.kubikl.omniscient.config.AppProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.security.SignatureException
import java.util.*


@Service
class TokenProvider(private val appProperties: AppProperties) {
    fun createToken(authentication: Authentication): String {
        val userPrincipal = authentication.getPrincipal() as UserPrincipal
        val now = Date()
        val expiryDate = Date(now.getTime() + appProperties.auth.tokenExpirationMsec)
        return Jwts.builder()
                .setSubject(java.lang.Long.toString(userPrincipal.id!!))
                .setIssuedAt(Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.auth.tokenSecret)
                .compact()
    }

    fun getUserIdFromToken(token: String?): Long {
        val claims: Claims = Jwts.parser()
                .setSigningKey(appProperties.auth.tokenSecret)
                .parseClaimsJws(token)
                .getBody()
        return claims.getSubject().toLong()
    }

    fun validateToken(authToken: String?): Boolean {
        try {
            Jwts.parser().setSigningKey(appProperties.auth.tokenSecret).parseClaimsJws(authToken)
            return true
        } catch (ex: SignatureException) {
            logger.error("Invalid JWT signature")
        } catch (ex: MalformedJwtException) {
            logger.error("Invalid JWT token")
        } catch (ex: ExpiredJwtException) {
            logger.error("Expired JWT token")
        } catch (ex: UnsupportedJwtException) {
            logger.error("Unsupported JWT token")
        } catch (ex: IllegalArgumentException) {
            logger.error("JWT claims string is empty.")
        }
        return false
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(TokenProvider::class.java)
    }

}