package io.kubikl.omniscient.security

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class RestAuthenticationEntryPoint : AuthenticationEntryPoint {
    @Throws(IOException::class, ServletException::class)
    override fun commence(
            httpServletRequest: HttpServletRequest?,
            httpServletResponse: HttpServletResponse,
            e: AuthenticationException) {
        logger.error("Responding with unauthorized error. Message - {}", e.message)
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                                      e.localizedMessage)
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(RestAuthenticationEntryPoint::class.java)
    }
}