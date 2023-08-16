package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import java.util.concurrent.TimeUnit


class PostRepositoryImpl(
//    private val dao: PostDao
) : PostRepository { // ременноо удалила конструктор у импл (postDao: PostDao)

    private val client = OkHttpClient.Builder()
        .callTimeout(30, TimeUnit.SECONDS)
//        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson() // потребуется парсинг ответов
    private val type = object : TypeToken<List<Post>>() {}.type // потребуется тайп токен

    private companion object {
        private const val BASE_URL = "http://10.0.2.2:9999/"
        val mediaType = "application/json".toMediaType()
    }

    override fun getAll(): List<Post> {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") } // string - прочитать полностью ответ и представить в виде строчки
            .let {
                gson.fromJson(it, type)
            }
    }

    override fun likeById(id: Long) {
        // TODO: do this in homework
    }

    override fun shareById(id: Long) {
        // TODO: do this in homework
    }

    override fun save(post: Post): Post {
        val request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .post(gson.toJson(post).toRequestBody(mediaType))
            .build()

        return  client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let {
                gson.fromJson(it, Post::class.java)
            }

    }

    override fun removeById(id: Long) {
        val request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()

        client.newCall(request)
            .execute()
//            .close()
    }
}
