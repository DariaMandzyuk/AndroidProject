package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import ru.netology.nmedia.dto.Post
import java.io.IOException
import java.util.concurrent.TimeUnit


class PostRepositoryImpl : PostRepository {

    private val client = OkHttpClient.Builder()
        .callTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()
    private val type = object : TypeToken<List<Post>>() {}.type

    private companion object {
        private const val BASE_URL = "http://10.0.2.2:9999/"
        private val POST_LIST_TYPE_TOKEN = object : TypeToken<List<Post>>() {}
        val mediaType = "application/json".toMediaType()
    }

    override fun getAllAsync(callback: PostRepository.RepositoryCallback<List<Post>>) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    println("1111")
                    try {
                        val body = response.body?.string() ?: throw RuntimeException("body is null")
                        callback.onSuccess(gson.fromJson(body, type))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

            })
        println("2222")
    }

    override fun likeByIdAsync(id: Long, callback: PostRepository.RepositoryCallback<Post>) {
        val request = Request.Builder()
            .post("".toRequestBody())
            .url("${BASE_URL}api/slow/posts/$id/likes")
            .build()

        client.newCall(request)
            .enqueue(getRequestCallback({
                gson.fromJson(it, Post::class.java)
            }, callback))
    }

    override fun unlikeByIdAsync(id: Long, callback: PostRepository.RepositoryCallback<Post>) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}api/slow/posts/$id/likes")
            .build()

        return client.newCall(request)
            .enqueue(getRequestCallback({
                gson.fromJson(it, Post::class.java)
            }, callback))
    }

    override fun saveAsync(post: Post, callback: PostRepository.RepositoryCallback<Unit>) {
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .post(gson.toJson(post).toRequestBody(mediaType))
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    callback.onSuccess(Unit)
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    override fun removeByIdAsync(id: Long, callback: PostRepository.RepositoryCallback<Unit>) {
        val request = Request.Builder()
            .delete()
            .url("${BASE_URL}api/slow/posts/$id")
            .build()

        client.newCall(request)
            .enqueue(getRequestCallback({}, callback))
    }

    private fun <T> getRequestCallback(
        success: (String) -> T,
        callback: PostRepository.RepositoryCallback<T>
    ): Callback {
        return object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.use { it.string() }
                try {
                    val result = success.invoke(requireNotNull(body))
                    callback.onSuccess(result)
                } catch (e: Exception) {
                    callback.onError(e)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }
        }
    }
}
