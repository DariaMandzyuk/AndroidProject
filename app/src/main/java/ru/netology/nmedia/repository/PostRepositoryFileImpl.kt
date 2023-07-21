//package ru.netology.nmedia.repository
//
//import android.content.Context
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//import ru.netology.nmedia.dto.Post
//
//class PostRepositoryFileImpl(private val context: Context) : PostRepository {
//    private var nextId = 1L
//    private var posts = emptyList<Post>()
//        set(value) {
//            field = value
//            data.value = value // записываем в ливдата
//            sync()
//        }
//    private val gson = Gson().newBuilder().setPrettyPrinting().create() //вызываем конструктор класса гсон
//    private val fileName = "posts.json"
//    private val typeToken = TypeToken.getParameterized(List::class.java, Post::class.java).type
//    private val data = MutableLiveData(posts)
//
//
//    init {
//        val file =  context.filesDir.resolve(fileName)
//        if (file.exists()) {
//            context.openFileInput(fileName).bufferedReader().use {
//                posts = gson.fromJson(it, typeToken)
//                nextId = (posts.maxOfOrNull {it.id} ?: 0) + 1 // присваиваем максимальное значение из текущих, которые есть в списке
////                data.value = posts
//            }
//        }
//    }
//
//    private fun sync() { // запись
//        context.openFileOutput(fileName, Context.MODE_PRIVATE).bufferedWriter().use {
//            it.write(gson.toJson(posts))
//        }
//    }
//
//    override fun getAll(): LiveData<List<Post>> = data
//
//    override fun save(post: Post) {
//        if (post.id == 0L) {
//            posts = listOf(
//                post.copy(
//                    id = nextId++,
//                    author = "Me",
//                    likedByMe = false,
//                    published = "now"
//                )
//            ) + posts
//            return
//        }
//
//        posts = posts.map {
//            if (it.id != post.id) it else it.copy(content = post.content)
//
//        }
//    }
//
//
//    override fun likeById(id: Long) {
//        posts = posts.map {
//            if (it.id != id) {
//                it
//            } else {
//                it.copy(likedByMe = !it.likedByMe, likes = likes(it))
//            }
//
//        }
//    }
//
//    override fun shareById(id: Long) {
//        posts = posts.map {
//            if (it.id != id) it else it.copy(shares = it.shares + 1)
//        }
//
//    }
//
//
//    fun likes(post: Post): Int {
//        return if (post.likedByMe) post.likes - 1 else post.likes + 1
//    }
//
//    override fun removeById(id: Long) {
//        posts =
//            posts.filter { it.id != id }
//    }
//}
