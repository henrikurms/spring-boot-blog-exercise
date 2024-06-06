package com.hatchways.blog.schema

import com.hatchways.blog.model.Post

data class PostsResponseWrapper(
    var posts: List<PostResponse>
)
