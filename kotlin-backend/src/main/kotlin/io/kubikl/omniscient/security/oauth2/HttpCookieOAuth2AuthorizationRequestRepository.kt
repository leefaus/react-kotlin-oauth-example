package io.kubikl.omniscient.security.oauth2

import io.kubikl.omniscient.util.CookieUtils.addCookie
import io.kubikl.omniscient.util.CookieUtils.deleteCookie
import io.kubikl.omniscient.util.CookieUtils.deserialize
import io.kubikl.omniscient.util.CookieUtils.getCookie
import io.kubikl.omniscient.util.CookieUtils.serialize
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.util.function.Function
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class HttpCookieOAuth2AuthorizationRequestRepository :  AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    override fun loadAuthorizationRequest(request: HttpServletRequest?): OAuth2AuthorizationRequest? {
        return getCookie(request!!, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(Function<Cookie, Any?> { cookie: Cookie? -> deserialize(cookie!!, OAuth2AuthorizationRequest::class.java) })
                .orElse(null) as OAuth2AuthorizationRequest?
    }

    override fun saveAuthorizationRequest(
            authorizationRequest: OAuth2AuthorizationRequest?,
            request: HttpServletRequest,
            response: HttpServletResponse?) {
        if (authorizationRequest == null) {
            deleteCookie(request, response!!, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
            deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME)
            return
        }
        addCookie(response!!, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, serialize(authorizationRequest), cookieExpireSeconds)
        val redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME)
        if (redirectUriAfterLogin.isNotEmpty()) {
            addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, cookieExpireSeconds)
        }
    }

    override fun removeAuthorizationRequest(request: HttpServletRequest?): OAuth2AuthorizationRequest? {
        return loadAuthorizationRequest(request)
    }

    fun removeAuthorizationRequestCookies(request: HttpServletRequest?, response: HttpServletResponse?) {
        deleteCookie(request!!, response!!, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
        deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME)
    }

    companion object {
        const val OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request"
        const val REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri"
        private const val cookieExpireSeconds = 180
    }
}