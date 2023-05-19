package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.PostService.formatNumber


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater) //функция, которая на базе верстки выдувает наш интерфейс. layoutInflater - занимается выдуванием компонентов
        setContentView(binding.root) //у любого binding есть специальная переменная root, которая всегда указывает на корневой элемент этого биндинга(ActivityMainBinding)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false,
            sharedByMe = false
        )

        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            if (post.likedByMe) {
                like?.setImageResource(R.drawable.ic_liked_24)
            }
            likeCount?.text = post.likes.toString() //Если пост понравился пользователю, то устанавливается соответствующая иконка и обновляется значение количества лайков.


            root.setOnClickListener {
                Log.d("stuff", "stuff")
            }

            avatar.setOnClickListener {
                Log.d("stuff", "avatar")
            }

            like?.setOnClickListener { //данный код настраивает обработчик клика для элемента like. При клике на элемент, выполняется изменение состояния лайка, обновление отображаемого изображения, обновление количества лайков и вывод сообщения в лог.
                Log.d("stuff", "like")
                post.likedByMe = !post.likedByMe //значение свойства likedByMe объекта post инвертируется. Если оно было true, то становится false, и наоборот.
                like.setImageResource( //устанавливается ресурс изображения для элемента like. если likedByMe равно true, то устанавливается иконка с ресурсом R.drawable.ic_liked_24, иначе - иконка с ресурсом R.drawable.ic_like_24.
                    if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
                )
                if (post.likedByMe) post.likes++ else post.likes-- //увеличивается или уменьшается значение свойства likes объекта post, в зависимости от значения likedByMe. Если likedByMe равно true, то значение likes увеличивается на 1, иначе - уменьшается на 1.
                likeCount?.text = formatNumber(post.likes)
            }

            share?.setOnClickListener { //данный код настраивает обработчик клика для элемента share.
                Log.d("stuff", "share")
                post.sharedByMe = !post.likedByMe //значение свойства sharedByMe объекта post инвертируется. Если оно было true, то становится false, и наоборот.
                if (post.sharedByMe) post.shares++ else post.shares-- //увеличивается или уменьшается значение свойства shares объекта post, в зависимости от значения sharedByMe. Если sharedByMe равно true, то значение shares увеличивается на 1, иначе - уменьшается на 1.
                shareCount?.text = formatNumber(post.shares)


            }


        }

    }
}


