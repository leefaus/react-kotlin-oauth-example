package io.kubikl.omniscient.controller

import io.kubikl.omniscient.exception.ResourceNotFoundException
import io.kubikl.omniscient.model.User
import io.kubikl.omniscient.repository.UserRepository
import io.kubikl.omniscient.security.CurrentUser
import io.kubikl.omniscient.security.UserPrincipal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class UserController {
    @Autowired
    private val userRepository: UserRepository? = null

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    fun getCurrentUser(@CurrentUser userPrincipal: UserPrincipal): User? {
        return userRepository!!.findById(userPrincipal.id!!)
                .orElseThrow { ResourceNotFoundException("User", "id", userPrincipal.id) }
    }
}