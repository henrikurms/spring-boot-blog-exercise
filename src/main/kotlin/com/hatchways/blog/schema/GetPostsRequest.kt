package com.hatchways.blog.schema

import com.hatchways.blog.model.Direction
import com.hatchways.blog.model.PostSortBy
import jakarta.validation.constraints.NotEmpty


data class GetPostsRequest(

    @field:NotEmpty(message = "authorIds must not be empty")
    val authorIds: List<Long>,

    val sortBy: PostSortBy = PostSortBy.id,

    val direction: Direction = Direction.desc,
)
