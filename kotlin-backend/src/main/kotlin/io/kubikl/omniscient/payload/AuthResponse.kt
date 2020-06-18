package io.kubikl.omniscient.payload

class AuthResponse(var accessToken: String) {
    var tokenType = "Bearer"
}