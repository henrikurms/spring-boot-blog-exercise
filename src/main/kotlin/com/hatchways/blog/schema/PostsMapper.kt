package com.hatchways.blog.schema

import com.hatchways.blog.model.Post
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface PostsMapper {

    @Mapping(source = "tagsList", target = "tags")
    fun postToResponse(post: Post): PostResponse

    @Mapping(target = "tags", ignore = true)
    @Mapping(source = "tags", target = "tagsList")
    fun requestToPost(req: PostRequest): Post
}