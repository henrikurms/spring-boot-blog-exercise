package com.hatchways.blog.repository

import com.hatchways.blog.model.Post
import com.hatchways.blog.model.User
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p JOIN p.users user WHERE user.id = :userId")
    fun findAllByUserId(userId: Long): List<Post>

    fun findDistinctByUsersIdIn(userIds: List<Long>, sort: Sort): List<Post>
}
