package io.kubikl.omniscient.controller

import io.kubikl.omniscient.model.AuthProvider
import io.kubikl.omniscient.model.User
import io.kubikl.omniscient.payload.ApiResponse
import io.kubikl.omniscient.payload.AuthResponse
import io.kubikl.omniscient.payload.LoginRequest
import io.kubikl.omniscient.payload.SignUpRequest
import io.kubikl.omniscient.repository.UserRepository
import io.kubikl.omniscient.security.TokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import javax.validation.Valid
import javax.ws.rs.BadRequestException


@RestController
@RequestMapping("/auth")
class AuthController {
    @Autowired
    private val authenticationManager: AuthenticationManager? = null

    @Autowired
    private val userRepository: UserRepository? = null

    @Autowired
    private val passwordEncoder: PasswordEncoder? = null

    @Autowired
    private val tokenProvider: TokenProvider? = null

    @PostMapping("/login")
    fun authenticateUser(@RequestBody loginRequest: @Valid LoginRequest?): ResponseEntity<*> {
        val authentication: Authentication = authenticationManager!!.authenticate(
                UsernamePasswordAuthenticationToken(
                        loginRequest!!.email,
                        loginRequest.password
                                                   )
                                                                                 )
        SecurityContextHolder.getContext().authentication = authentication
        val token = tokenProvider!!.createToken(authentication)
        return ResponseEntity.ok(AuthResponse(token))
    }

    @PostMapping("/signup")
    fun registerUser(@RequestBody signUpRequest: @Valid SignUpRequest?): ResponseEntity<*> {
        if (userRepository!!.existsByEmail(signUpRequest!!.email)!!) {
            throw BadRequestException("Email address already in use.")
        }

        // Creating user's account
        val user = User()
        user.name = signUpRequest.name
        user.email = signUpRequest.email
        user.password = signUpRequest.password
        user.provider = AuthProvider.local
        user.password = passwordEncoder!!.encode(user.password)
        val result: User = userRepository.save(user)
        val location: URI = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.id).toUri()
        return ResponseEntity.created(location)
                .body(ApiResponse(true, "User registered successfully@"))
    }
}