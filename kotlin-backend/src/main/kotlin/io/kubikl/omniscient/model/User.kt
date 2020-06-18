package io.kubikl.omniscient.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull

enum class AuthProvider {
    local, facebook, google, github
}

@Entity
@Table(name = "users", uniqueConstraints = [UniqueConstraint(columnNames = ["email"])])
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    var name: String? = null

    @Email
    @Column(nullable = false)
    var email: String? = null
    var imageUrl: String? = null

    @Column(nullable = false)
    var emailVerified = false

    @JsonIgnore
    var password: String? = null

    @NotNull
    @Enumerated(EnumType.STRING)
    var provider: AuthProvider? = null
    var providerId: String? = null

}