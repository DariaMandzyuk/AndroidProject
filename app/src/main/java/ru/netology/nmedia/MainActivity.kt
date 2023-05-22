package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.PostService.formatNumber
import ru.netology.nmedia.viewmodel.PostViewModel


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            ActivityMainBinding.inflate(layoutInflater) //функция, которая на базе верстки выдувает наш интерфейс. layoutInflater - занимается выдуванием компонентов
        setContentView(binding.root) //у любого binding есть специальная переменная root, которая всегда указывает на корневой элемент этого биндинга(ActivityMainBinding)

//        binding.shareCount?.text = formatNumber(post.shares)
//        binding.likeCount?.text = formatNumber(post.likes)
//        binding.viewsCount?.text = formatNumber(post.views)

        val viewModel by viewModels<PostViewModel>() //инициализация переменной viewModel была с помощью делегирования специальной дженерик функции viewModels, ее тип PostViewModel
        viewModel.data.observe(this) { post ->

            with(binding) {
                author.text = post.author //обращаюсь к элементу разметки author, у него есть свойство text и туда записываю текущее значение post.author. В разметке можно просто указать что есть текстовое поле и указать зависимости от соседей
                published.text = post.published
                content.text = post.content
                like?.setImageResource(
                    if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
                )
                likeCount?.text = post.likes.toString()

            }
        }
        binding.like?.setOnClickListener {
            viewModel.like()
        }

//           binding.share.setOnClickListener {
//               viewModel.
//           }

//        share?.setOnClickListener {
//            Log.d("stuff", "share")
//            post.sharedByMe = !post.sharedByMe
//            if (post.sharedByMe) post.shares++ else post.shares--
//            shareCount?.text = formatNumber(post.shares)
//        }

    }
}


