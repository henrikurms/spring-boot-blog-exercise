package com.hatchways.blog.schema

import jakarta.validation.constraints.NotBlank

data class AuthRequest(
    @NotBlank(message = "username is required")
    val username: String,

    @NotBlank(message = "password is required")
    val password: String
)
