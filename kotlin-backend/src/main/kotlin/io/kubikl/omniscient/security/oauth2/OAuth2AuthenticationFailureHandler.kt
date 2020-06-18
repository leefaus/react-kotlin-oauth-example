package io.kubikl.omniscient.security.oauth2

import io.kubikl.omniscient.util.CookieUtils.getCookie
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import io.kubikl.omniscient.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.Companion.REDIRECT_URI_PARAM_COOKIE_NAME


@Component
class OAuth2AuthenticationFailureHandler : SimpleUrlAuthenticationFailureHandler() {
    @Autowired
    var httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository? = null

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationFailure(
            request: HttpServletRequest?,
            response: HttpServletResponse?,
            exception: AuthenticationException) {
        var targetUrl: String? = getCookie(request!!, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map<Any>(Cookie::getValue)
                .orElse("/") as String?
        targetUrl = UriComponentsBuilder.fromUriString(targetUrl!!)
                .queryParam("error", exception.getLocalizedMessage())
                .build().toUriString()
        httpCookieOAuth2AuthorizationRequestRepository?.removeAuthorizationRequestCookies(request, response)
        redirectStrategy.sendRedirect(request, response, targetUrl)
    }
}