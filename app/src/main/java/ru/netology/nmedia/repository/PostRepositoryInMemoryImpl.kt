package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl: PostRepository {

    private var post = Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        published = "21 мая в 18:36",
        likedByMe = false,
        sharedByMe = false,
        likes = 0
    )

    private val data = MutableLiveData(post) // MutableLiveData - данные за которыми можно наблюдать. В конструктор ( ) ему передается начальное значение этих данных

    override fun get() = data //будет возвращать переменную data

    override fun like() { //будет изменять пост
        post = post.copy(likedByMe = !post.likedByMe, likes = if(post.likedByMe) post.likes - 1 else post.likes + 1) //post является датаклассом со значениями val, поэтому, чтобы изменить какое-то значение, нам нужно создать копию
        data.value = post //в переменную data записываем новое значение.
    }
    // раньше мы все данные хранили в одном и том же посте, мы его обновляли и отображали, а сейчас на каждое обновление данных мы создаем новый экземпляр класса пост
    // оператор ! - это инвертирование Boolean переменной. Если было true, то вернет false. Если было false, то вернет true

}
