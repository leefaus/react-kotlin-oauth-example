package io.kubikl.omniscient.security

import io.kubikl.omniscient.model.User
import org.springframework.security.core.GrantedAuthority

import org.springframework.security.core.authority.SimpleGrantedAuthority

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User
import java.util.*


class UserPrincipal(
        val id: Long?,
        val email: String?,
        private val password: String?,
        private var authorities: Collection<GrantedAuthority>
        ) : OAuth2User, UserDetails {

    private val attributes: MutableMap<String, Any> = HashMap<String, Any>()

    override fun getName(): String {
        return id.toString()
    }

    override fun getAttributes(): MutableMap<String, Any> {
        return attributes
    }

    override fun getPassword(): String? {
        return password
    }

    override fun getUsername(): String? {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    companion object {
        fun create(user: User?): UserPrincipal {
            val authorities: List<GrantedAuthority> = Collections.singletonList(SimpleGrantedAuthority("ROLE_USER"))
            return UserPrincipal(
                    user?.id,
                    user?.email,
                    user?.password,
                    authorities

                                )
        }

        fun create(user: User, attributes: Map<String, Any>): UserPrincipal {
            var userPrincipal = create(user)
            if (attributes != null) {
                userPrincipal.attributes.putAll(attributes)
            }
            return userPrincipal
        }
    }

}