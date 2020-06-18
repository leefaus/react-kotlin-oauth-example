package io.kubikl.omniscient.security.oauth2.user

import io.kubikl.omniscient.exception.OAuth2AuthenticationProcessingException
import io.kubikl.omniscient.model.AuthProvider


object OAuth2UserInfoFactory {
    fun getOAuth2UserInfo(
            registrationId: String,
            attributes: Map<String?, Any?>?): OAuth2UserInfo {
        return if (registrationId.equals(AuthProvider.google.toString(), ignoreCase = true)) {
            GoogleOAuth2UserInfo(attributes)
        } else if (registrationId.equals(AuthProvider.facebook.toString(), ignoreCase = true)) {
            FacebookOAuth2UserInfo(attributes)
        } else if (registrationId.equals(AuthProvider.github.toString(), ignoreCase = true)) {
            GitHubOAuth2UserInfo(attributes)
        } else {
            throw OAuth2AuthenticationProcessingException("Sorry! Login with $registrationId is not supported yet.")
        }
    }
}