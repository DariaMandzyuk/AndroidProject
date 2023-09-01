package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by activityViewModels()

//    private val viewModel: PostViewModel by viewModels()
//        ownerProducer = ::requireParentFragment


//    var _binding: FragmentFeedBinding? = null
//    val binding: FragmentFeedBinding
//        get() = _binding!!

    override fun onCreateView( // у фрагмента нет своего инфлейтера, поэтому мы не можем к нему получить доступ
        inflater: LayoutInflater, // поэтому в функции onCreateView передается инфлейтер который умеет из верстки загружать весь наш интерфейс
        container: ViewGroup?, // передается контейнер внутри которого этот фрагмент крутится. контейнером выступает R.layout.activity_app
        savedInstanceState: Bundle? // если мы выходим из режима сохраненного состояния, то передается переменная savedInstanceState и Bundle как список параметров
    ): View {
        val binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )

        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onEdit(post: Post) {
                viewModel.edit(post)
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment,
                Bundle().apply {
                    textArg = post.content
                }
            )
            }

            override fun onLike(post: Post) {
                viewModel.likePost(post)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }
        })

//            override fun onClickToNewPost(post: Post) {
////                findNavController().navigate(R.id.action_feedFragment_to_postFragment,  Bundle().apply { postId = post.id })
//            }

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { state -> // подписывается и получает состояния
            binding.errorGroup.isVisible = state.error
            binding.emptyText.isVisible = state.empty
            binding.progress.isVisible = state.loading
            adapter.submitList(state.posts)
        }

        binding.retryButton.setOnClickListener {
            viewModel.loadPosts()
        }

        binding.add.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
            Bundle().apply {
                textArg = ""
            }
        }

        return binding.root // это корневой элемент верстки нашего фрагмента activity.FeedFragment
    }
}

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }

