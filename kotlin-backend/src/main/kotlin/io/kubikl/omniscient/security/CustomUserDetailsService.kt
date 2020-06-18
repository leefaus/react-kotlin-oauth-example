package io.kubikl.omniscient.security

import io.kubikl.omniscient.exception.ResourceNotFoundException
import io.kubikl.omniscient.model.User
import io.kubikl.omniscient.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class CustomUserDetailsService : UserDetailsService {
    @Autowired
    lateinit var userRepository: UserRepository

    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails {
        val user: User? = userRepository.findByEmail(email)!!
                .orElseThrow { UsernameNotFoundException("User not found with email : $email") }
        return UserPrincipal.create(user)
    }

    @Transactional
    fun loadUserById(id: Long): UserDetails {
        val user: User? = userRepository.findById(id).orElseThrow { ResourceNotFoundException("User", "id", id) }
        return UserPrincipal.create(user)
    }
}