package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post

class PostRepositorySharedPrefsImpl( context: Context) : PostRepository {
    //реализация интерфейса, в которой пост храниться в переменной post
    //в классе также есть свойство типа MutableLiveData куда записана переменная post
    //функция get возвращает эту переменную, то есть отдает пост
    //функция like создает копию поста у которого изменено поле likedByMe, и записывает измененный пост в переменную data в свойство value
    private var nextId = 1L
    private var posts = emptyList<Post>()
    private val gson = Gson() //вызываем конструктор класса гсон
    private val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    private val key = "posts"
    private val typeToken = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val data = MutableLiveData(posts)

    init {
        prefs.getString(key, null)?.let { //чтение
            posts = gson.fromJson(it, typeToken)
            nextId = (posts.maxOfOrNull {it.id} ?: 0) + 1 // присваиваем максимальное значение из текущих, которые есть в списке
            data.value = posts
        }
    }

    private fun sync() {
        prefs.edit().apply() {
            putString(key, gson.toJson(posts))
            apply()
        }
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun save(post: Post) {
        if (post.id == 0L) {
            posts = listOf(
                post.copy(
                    id = nextId++,
                    author = "Me",
                    likedByMe = false,
                    published = "now"
                )
            ) + posts
            data.value = posts
            sync()
            return
        }

        posts = posts.map { //если id совпали, значит произошло редактирование
            if (it.id != post.id) it else it.copy(content = post.content)

        }
        data.value = posts
        sync()
    }


    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) {
                it
            } else {
                it.copy(likedByMe = !it.likedByMe, likes = likes(it))
            }

        }
        data.value = posts
        sync()
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(shares = it.shares + 1)
        }
        data.value = posts
        sync()
    }


    fun likes(post: Post): Int {
        return if (post.likedByMe) post.likes - 1 else post.likes + 1
    }

    override fun removeById(id: Long) {
        posts =
            posts.filter { it.id != id }
        data.value = posts
        sync()
    }
}
//        listOf(
//        Post(
//            id = nextId++,
//            author = "Нетология. Университет интернет-профессий будущего",
//            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
//            published = "21 мая в 18:36",
//            likedByMe = false,
//            likes = 999,
//            shares = 99,
//            views = 1300000,
//            video = "https://youtube.com/shorts/HqNcgLskf3w?feature=share"
//        ),
//        Post(
//            id = nextId++,
//            author = "Нетология. Университет интернет-профессий будущего",
//            content = "Делиться впечатлениями о любимых фильмах легко, а что если рассказать так, чтобы все заскучали \uD83D\uDE34\n",
//            published = "22 сентября в 10:14",
//            likedByMe = false,
//            likes = 20000,
//            shares = 500000000,
//            views = 89,
//            video = null
//        ),
//        Post(
//            id = nextId++,
//            author = "Нетология. Университет интернет-профессий будущего",
//            content = "Таймбоксинг — отличный способ навести порядок в своём календаре и разобраться с делами, которые долго откладывали на потом. Его главный принцип — на каждое дело заранее выделяется определённый отрезок времени. В это время вы работаете только над одной задачей, не переключаясь на другие. Собрали советы, которые помогут внедрить таймбоксинг \uD83D\uDC47\uD83C\uDFFB",
//            published = "22 сентября в 10:12",
//            likedByMe = false,
//            likes = 999,
//            shares = 99,
//            views = 1300000,
//            video = null
//        ),
//        Post(
//            id = nextId++,
//            author = "Нетология. Университет интернет-профессий будущего",
//            content = "\uD83D\uDE80 24 сентября стартует новый поток бесплатного курса «Диджитал-старт: первый шаг к востребованной профессии» — за две недели вы попробуете себя в разных профессиях и определите, что подходит именно вам → http://netolo.gy/fQ",
//            published = "21 сентября в 10:12",
//            likedByMe = false,
//            likes = 109,
//            shares = 88,
//            views = 130,
//            video = null
//        ),
//        Post(
//            id = nextId++,
//            author = "Нетология. Университет интернет-профессий будущего",
//            content = "Диджитал давно стал частью нашей жизни: мы общаемся в социальных сетях и мессенджерах, заказываем еду, такси и оплачиваем счета через приложения.",
//            published = "20 сентября в 10:14",
//            likedByMe = false,
//            likes = 9,
//            shares = 99,
//            views = 13,
//            video = null
//        ),
//        Post(
//            id = nextId++,
//            author = "Нетология. Университет интернет-профессий будущего",
//            content = "Большая афиша мероприятий осени: конференции, выставки и хакатоны для жителей Москвы, Ульяновска и Новосибирска \uD83D\uDE09",
//            published = "19 сентября в 14:12",
//            likedByMe = false,
//            likes = 1000,
//            shares = 109,
//            views = 10,
//            video = null
//        ),
//        Post(
//            id = nextId++,
//            author = "Нетология. Университет интернет-профессий будущего",
//            content = "Языков программирования много, и выбрать какой-то один бывает нелегко. Собрали подборку статей, которая поможет вам начать, если вы остановили свой выбор на JavaScript.",
//            published = "19 сентября в 10:24",
//            likedByMe = false,
//            likes = 1239,
//            shares = 300000,
//            views = 1500000,
//            video = null
//        ),
//        Post(
//            id = nextId++,
//            author = "Нетология. Университет интернет-профессий будущего",
//            content = "Знаний хватит на всех: на следующей неделе разбираемся с разработкой мобильных приложений, учимся рассказывать истории и составлять PR-стратегию прямо на бесплатных занятиях \uD83D\uDC47",
//            published = "18 сентября в 10:12",
//            likedByMe = false,
//            likes = 12000000,
//            shares = 9,
//            views = 100,
//            video = null
//        ),
//        Post(
//            id = nextId++,
//            author = "Нетология. Университет интернет-профессий будущего",
//            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
//            published = "21 мая в 18:36",
//            likedByMe = false,
//            likes = 9,
//            shares = 999,
//            views = 130,
//            video = null
//        ),
//    ).reversed()
