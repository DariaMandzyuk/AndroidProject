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
//        AppDb.getInstance(context = application).postDao


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
        edited.value?.let {
            repository.save(it)
            _postCreated.postValue(Unit)
        }
        edited.postValue(empty)
        }
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

    fun likeById(id: Long) = repository.likeById(id)
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
