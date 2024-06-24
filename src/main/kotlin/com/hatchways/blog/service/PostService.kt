package com.hatchways.blog.service

import com.hatchways.blog.model.Direction
import com.hatchways.blog.model.Post
import com.hatchways.blog.model.PostSortBy
import com.hatchways.blog.schema.PostRequest

interface PostService {
    /** Create a new post in the database. */
    fun createPost(postRequestBody: PostRequest, username: String): Post

    fun findAllByUserIds(userIds: List<Long>, sortBy: PostSortBy, direction: Direction): List<Post>
}
