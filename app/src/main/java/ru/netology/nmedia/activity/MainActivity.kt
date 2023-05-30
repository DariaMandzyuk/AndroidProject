package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {

    val viewModel: PostViewModel by viewModels() //получаем viewmodel

//    val interaction: OnInteractionListener = object : OnInteractionListener {
//        override fun onEdit(post: Post) {
//            viewModel.edit(post)
//        }
//
//        override fun onCancelEdit(post: Post) {
//            viewModel.cancelEdit()
//        }
//
//        override fun onLike(post: Post) {
//            viewModel.likeById(post.id)
//        }
//
//        override fun onRemove(post: Post) {
//            viewModel.removeById(post.id)
//        }
//
//        override fun onShare(post: Post) {
//            viewModel.shareById(post.id)
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun onCancelEdit(post: Post) {
                viewModel.cancelEdit()
            }

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
            }
        })
        binding.list.adapter = adapter

        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts) //после обновления сработает метод observe у data, присвоит это submitList и обновит на экране
        }

        viewModel.edited.observe(this) { post ->
            if (post.id == 0L) {
                return@observe
            }
            with(binding.content) {
                requestFocus()
                setText(post.content)
                binding.groupCancelEdit.visibility = View.VISIBLE
            }
        }

        binding.save.setOnClickListener { //вешаем обработчик на кнопку save
            with(binding.content) {
                if (text.isNullOrBlank()) { //проверяем, чтобы текст не был пустым
                    Toast.makeText( //если пустой - показываем всплывашкуошибку
                        this@MainActivity,
                        context.getString(R.string.error_empty_content),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                viewModel.changeContent(text.toString()) //вызываем во вью модели changeContent и в него передаем текст
                viewModel.save() //след шагом даем команду save. save разберется, если текст новый - добавит, если редактируемый - отредактирует

                setText("") // после того как сохранили, то очищаем все, строку ввода, фокус
                clearFocus()
                binding.groupCancelEdit.visibility = View.GONE
                AndroidUtils.hideKeyboard(this)
            }
        }

        binding.undoButton.setOnClickListener {
            with(binding.groupCancelEdit) {
                viewModel.cancelEdit()
                Toast.makeText(
                    this@MainActivity,
                    context.getString(R.string.editing_cancelled),
                    Toast.LENGTH_SHORT
                ).show()

                binding.content.setText("")
                clearFocus()
                binding.groupCancelEdit.visibility = View.GONE
                AndroidUtils.hideKeyboard(this)

                return@setOnClickListener

            }

        }

        viewModel.edited.observe(this) { post ->
            if (post.id == 0L) {
                return@observe
            }
            with(binding.content) {
                requestFocus()
                setText(post.content)
                binding.groupCancelEdit.visibility = View.VISIBLE
            }
        }

        binding.save.setOnClickListener { //вешаем обработчик на кнопку save
            with(binding.content) {
                if (text.isNullOrBlank()) { //проверяем, чтобы текст не был пустым
                    Toast.makeText( //если пустой - показываем всплывашкуошибку
                        this@MainActivity,
                        context.getString(R.string.error_empty_content),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                viewModel.changeContent(text.toString()) //вызываем во вью модели changeContent и в него передаем текст
                viewModel.save() //след шагом даем команду save. save разберется, если текст новый - добавит, если редактируемый - отредактирует

                setText("") // после того как сохранили, то очищаем все, строку ввода, фокус
                clearFocus()
                binding.groupCancelEdit.visibility = View.GONE
                AndroidUtils.hideKeyboard(this)
            }
        }

        binding.undoButton.setOnClickListener {
            with(binding.groupCancelEdit) {
                viewModel.cancelEdit()
                Toast.makeText(
                    this@MainActivity,
                    context.getString(R.string.editing_cancelled),
                    Toast.LENGTH_SHORT
                ).show()

                binding.content.setText("")
                clearFocus()
                binding.groupCancelEdit.visibility = View.GONE
                AndroidUtils.hideKeyboard(this)

                return@setOnClickListener

            }

        }
    }
}



