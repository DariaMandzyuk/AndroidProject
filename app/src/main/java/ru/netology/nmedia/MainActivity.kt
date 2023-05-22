package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.PostService.formatNumber
import ru.netology.nmedia.viewmodel.PostViewModel


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) //у любого binding есть специальная переменная root, которая всегда указывает на корневой элемент этого биндинга(ActivityMainBinding)


        val viewModel by viewModels<PostViewModel>() //инициализация переменной viewModel была с помощью делегирования специальной дженерик функции viewModels, ее тип PostViewModel
        viewModel.data.observe(this) { post ->

            with(binding) {
                author.text = post.author //обращаюсь к элементу разметки author, у него есть свойство text и туда записываю текущее значение post.author. В разметке можно просто указать что есть текстовое поле и указать зависимости от соседей
                published.text = post.published
                content.text = post.content
                likeCount?.text = formatNumber(post.likes)
                shareCount?.text = formatNumber(post.shares)
                viewsCount?.text = formatNumber(post.views)

                like?.setImageResource(
                    if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
                )
                likeCount?.text = formatNumber(post.likes)

            }
        }
        binding.like?.setOnClickListener {
            viewModel.like()
        }

           binding.share?.setOnClickListener {
               viewModel.share()
           }
    }
}


