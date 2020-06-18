package io.kubikl.omniscient.payload

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

class SignUpRequest {
    @NotBlank
    var name: String? = null

    @NotBlank
    @Email
    var email: String? = null

    @NotBlank
    var password: String? = null

}