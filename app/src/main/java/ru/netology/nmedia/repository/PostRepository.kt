package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {

    //fun getAll(): List<Post>
    //fun likeById(id: Long): Post
    //fun unlikeById(id: Long): Post
    //fun save(post: Post): Post
    //fun shareById(id: Long)
    //fun removeById(id: Long)
    fun getAllAsync(callback: RepositoryCallback<List<Post>>)
    fun likeByIdAsync(id: Long, callback: RepositoryCallback<Post>)
    fun unlikeByIdAsync(id: Long, callback: RepositoryCallback<Post>)

    fun saveAsync(post: Post, callback: RepositoryCallback<Unit>)
    fun removeByIdAsync(id: Long, callback: RepositoryCallback<Unit>)

    interface RepositoryCallback<T> {
        fun onSuccess(result: T)
        fun onError(e: Exception)

    }
}
