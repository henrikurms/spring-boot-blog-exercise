package com.hatchways.blog.controller

import com.hatchways.blog.model.Post
import com.hatchways.blog.schema.GetPostsRequest
import com.hatchways.blog.schema.PostRequest
import com.hatchways.blog.schema.PostResponse
import com.hatchways.blog.schema.PostResponseWrapper
import com.hatchways.blog.schema.PostsMapper
import com.hatchways.blog.schema.PostsResponseWrapper
import com.hatchways.blog.service.PostService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/api")
class PostController(private val postService: PostService, private val postsMapper: PostsMapper) {

    /** Create a new post in the database. */
    @PostMapping("/posts")
    fun createPost(
        @Valid @RequestBody postRequestBody: PostRequest,
        authentication: Authentication
    ): ResponseEntity<PostResponseWrapper> {
        val post: Post = postService.createPost(postRequestBody, authentication.name)
        val postResponse: PostResponse = postsMapper.postToResponse(post)
        val response = PostResponseWrapper(postResponse)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/posts")
    fun getPosts(
        //By using a class here instead of @RequestParam we get spring to use status 400 instead of 500 on validation failures
        @Valid request: GetPostsRequest
    ): ResponseEntity<PostsResponseWrapper> {
        val posts: List<Post> = postService.findAllByUserIds(request.authorIds, request.sortBy, request.direction)
        val postsResponse = posts.map { post: Post -> postsMapper.postToResponse(post) }
        val response = PostsResponseWrapper(postsResponse)
        return ResponseEntity.ok(response)
    }
}
