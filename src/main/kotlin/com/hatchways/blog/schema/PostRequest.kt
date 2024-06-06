package com.hatchways.blog.schema

data class PostRequest(
    var text: String? = null,
    var tags: List<String>? = null
)
