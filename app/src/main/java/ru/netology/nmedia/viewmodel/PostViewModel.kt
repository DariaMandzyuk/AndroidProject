package ru.netology.nmedia.viewmodel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

class PostViewModel: ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl() //создаем переменную repository типа PostRepository и записываем туда конкретную реализацию PostRepositoryInMemoryImpl()
    val data = repository.get() // объявляем свойство data и оно будет ссылаться на данные из repository(через метод get)
    fun like() = repository.like()
}