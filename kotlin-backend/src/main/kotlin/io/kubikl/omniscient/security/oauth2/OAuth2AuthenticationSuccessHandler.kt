package io.kubikl.omniscient.security.oauth2

import io.kubikl.omniscient.config.AppProperties
import io.kubikl.omniscient.security.TokenProvider
import io.kubikl.omniscient.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.Companion.REDIRECT_URI_PARAM_COOKIE_NAME
import io.kubikl.omniscient.util.CookieUtils.getCookie
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.io.IOException
import java.net.URI
import java.util.*
import javax.servlet.ServletException
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.ws.rs.BadRequestException


@Component
class OAuth2AuthenticationSuccessHandler @Autowired internal constructor(
        private val tokenProvider: TokenProvider,
        private val appProperties: AppProperties,
        httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository) : SimpleUrlAuthenticationSuccessHandler() {
    private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(
            request: HttpServletRequest?,
            response: HttpServletResponse,
            authentication: Authentication?) {
        val targetUrl = determineTargetUrl(request, response, authentication)
        if (response.isCommitted) {
            logger.debug("Response has already been committed. Unable to redirect to $targetUrl")
            return
        }
        clearAuthenticationAttributes(request, response)
        redirectStrategy.sendRedirect(request, response, targetUrl)
    }

    override fun determineTargetUrl(
            request: HttpServletRequest?,
            response: HttpServletResponse?,
            authentication: Authentication?): String {
        val redirectUri: Optional<Any>? = getCookie(request!!, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map<Any>(Cookie::getValue)
        if (redirectUri != null) {
            if (redirectUri.isPresent && !isAuthorizedRedirectUri(redirectUri.get() as String)) {
                throw BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication")
            }
        }
        val targetUrl: String = redirectUri?.orElse(defaultTargetUrl) as String
        val token = authentication?.let { tokenProvider.createToken(it) }
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token)
                .build().toUriString()
    }

    protected fun clearAuthenticationAttributes(
            request: HttpServletRequest?,
            response: HttpServletResponse?) {
        super.clearAuthenticationAttributes(request)
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)
    }

    private fun isAuthorizedRedirectUri(uri: String): Boolean {
        val clientRedirectUri: URI = URI.create(uri)
        return appProperties.oauth2.authorizedRedirectUris
                .stream()
                .anyMatch { authorizedRedirectUri: String? ->
                    // Only validate host and port. Let the clients use different paths if they want to
                    val authorizedURI: URI = URI.create(authorizedRedirectUri)
                    if (authorizedURI.host.equals(clientRedirectUri.host, true)
                            && authorizedURI.port === clientRedirectUri.port) return@anyMatch true
                    false
                }
    }

}