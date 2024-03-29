package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
//import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException
import kotlin.concurrent.thread


private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    likes = 0,
    published = "",
    shares = 0,
    views = 0,
    video = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel> = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit> = _postCreated
    init {
        loadPosts()
    }

    fun loadPosts() {
        thread {
            _data.postValue(FeedModel(loading = true)) // флаг загрузки
            try {
                val data = repository.getAll() // сетевой вызов
                FeedModel(posts = data, empty = data.isEmpty()) // успех empty = data.isEmpty()
            } catch (e: Exception) {
                FeedModel(error = true) // ошибка
            }.also {
                _data.postValue(it)
            }
        }
    }

    fun save() {
        thread {
            edited.value?.let { edited ->
                val post = repository.save(edited)
                val oldPosts = _data.value?.posts.orEmpty()
                val newPosts = if (edited.id != 0L) {
                    oldPosts.map { if (it.id == edited.id) post else it }
                } else {
                    listOf(post) + oldPosts
                }
                _data.postValue(
                    _data.value?.copy(
                        posts = newPosts
                    )
                )
                _postCreated.postValue(Unit)
            }
            edited.postValue(empty)
        }
//        thread {
//        edited.value?.let {
//            repository.save(it)
//            _postCreated.postValue(Unit)
//        }
//        edited.postValue(empty)
//        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) { // функция изменения контента
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun likePost(post: Post) {
        if (post.likedByMe) {
            unlikeById(post.id)
        } else {
            likeById(post.id)
        }
    }

    private fun likeById(id: Long) {
        val old = _data.value?.posts.orEmpty()
        thread {
            try {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .map {
                            if (it.id == id) repository.likeById(id) else it
                        }
                    )
                )
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        }
    }

    private fun unlikeById(id: Long) {
        val old = _data.value?.posts.orEmpty()
        thread {
            try {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .map {
                            if (it.id == id) repository.unlikeById(id) else it
                        }
                    )
                )
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        }
    }

    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) {
        thread {
            val old = _data.value
            _data.postValue(
                old?.copy(
                    posts = old.posts.filter {
                        it.id != id
                    }
                )
            )
            try {
                repository.removeById(id)
            }catch (e: Exception) {
                _data.postValue(old)
            }
        }
    }
}
