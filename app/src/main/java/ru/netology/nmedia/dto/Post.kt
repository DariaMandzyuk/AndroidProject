package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likes: Int = 10,
    var likedByMe: Boolean = false,
    var shares: Int = 888888,
    var sharedByMe: Boolean = false,
    var views: Int = 0
)

