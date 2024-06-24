package com.hatchways.blog.model

import jakarta.persistence.*
import kotlin.collections.Set

@Entity
@Table(name = "post")
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_sequence")
    @SequenceGenerator(name = "post_sequence", sequenceName = "post_sequence", initialValue = 5)
    var id: Long? = null,

    @Column
    var text: String? = null,

    @Column
    var tags: String? = null,

    @Column
    var likes: Long? = null,

    @Column
    var reads: Long? = null,

    @Column(precision = 7, scale = 2)
    var popularity: Float? = null,

    @ManyToMany
    @JoinTable(
        name = "user_post",
        joinColumns = [JoinColumn(name = "post_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    var users: Set<User>? = null
) {
    constructor(
        text: String?,
        tags: String?,
        likes: Long?,
        reads: Long?,
        popularity: Float?
    ) : this() {
        this.text = text
        this.tags = tags
        this.likes = likes
        this.reads = reads
        this.popularity = popularity
    }

    // Had to rename this so the mapping could be instructed to use this instead of the tags field
    fun getTagsList(): List<String> {
        return tags?.split(",") ?: emptyList()
    }

    fun setTagsList(tags: List<String>) {
        this.tags = tags.joinToString(",")
    }
}
