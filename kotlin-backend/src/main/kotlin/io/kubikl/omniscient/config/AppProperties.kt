package io.kubikl.omniscient.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.util.*

@ConfigurationProperties(prefix = "app")
@Component
class AppProperties {
    val auth = Auth()
    val oauth2 = OAuth2()

    class Auth {
        var tokenSecret: String? = null
        var tokenExpirationMsec: Long = 0

    }

    class OAuth2 {
        var authorizedRedirectUris: List<String> = ArrayList()
            private set

        fun authorizedRedirectUris(authorizedRedirectUris: List<String>): OAuth2 {
            this.authorizedRedirectUris = authorizedRedirectUris
            return this
        }
    }

}