package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.LongArg
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg
    }
    private val viewModel: PostViewModel by activityViewModels()
//    private val viewModel: PostViewModel by viewModels()
//        ownerProducer = ::requireParentFragment


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )

        arguments?.textArg?.let(binding.edit::setText)

        binding.ok.setOnClickListener { // вот здесь какая-то проблема судя по всему кнопка не срабатывает должным образом
            viewModel.changeContent(binding.edit.text.toString())
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
        }

        viewModel.postCreated.observe(viewLifecycleOwner) {
            viewModel.loadPosts()
            findNavController().navigateUp()
        }

//        viewModel.edited.observe(viewLifecycleOwner) { post->
//            viewModel.loadPosts()
//            findNavController().navigateUp()
////            binding.edit.setText(post.content)
//        }

        return binding.root
    }
}
