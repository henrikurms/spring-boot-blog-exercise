package com.hatchways.blog.service

import com.hatchways.blog.model.Direction
import com.hatchways.blog.model.Post
import com.hatchways.blog.model.PostSortBy
import com.hatchways.blog.model.User
import com.hatchways.blog.repository.PostRepository
import com.hatchways.blog.repository.UserRepository
import com.hatchways.blog.schema.PostRequest
import com.hatchways.blog.schema.PostsMapper
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class PostServiceImpl(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val postsMapper: PostsMapper
) : PostService {

    override fun createPost(postRequestBody: PostRequest, username: String): Post {
        val post: Post = postsMapper.requestToPost(postRequestBody)
        val users: MutableSet<User> = mutableSetOf()
        val user: User = userRepository.findByUsername(username)
        users.add(user)
        post.users = users
        postRepository.save(post)
        postRepository.flush()
        return post
    }

    override fun findAllByUserIds(userIds: List<Long>, sortBy: PostSortBy, direction: Direction): List<Post> {
        val sortDirection = Sort.Direction.fromString(direction.name)
        return postRepository.findByUsersIdIn(userIds, Sort.by(sortDirection, sortBy.name))
    }
}
