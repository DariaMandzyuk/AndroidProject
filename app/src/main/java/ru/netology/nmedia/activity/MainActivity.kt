//package ru.netology.nmedia.activity
//
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.content.Context
//import android.content.pm.ResolveInfo
//import android.net.Uri
//import android.os.Bundle
//import android.view.View
//import android.widget.LinearLayout
//import android.widget.Toast
//import androidx.activity.result.ActivityResultLauncher
//import androidx.activity.result.launch
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import androidx.constraintlayout.widget.Group
//import ru.netology.nmedia.R
//import ru.netology.nmedia.adapter.OnInteractionListener
//import ru.netology.nmedia.adapter.PostsAdapter
//import ru.netology.nmedia.databinding.ActivityMainBinding
//import ru.netology.nmedia.databinding.CardPostBinding
//import ru.netology.nmedia.dto.Post
//import ru.netology.nmedia.util.AndroidUtils
//import ru.netology.nmedia.viewmodel.PostViewModel
//
//class MainActivity : AppCompatActivity() {
//
//    val viewModel: PostViewModel by viewModels()
//
//    lateinit var newPostLauncher: ActivityResultLauncher<Unit>
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val adapter = PostsAdapter(object : OnInteractionListener {
//            override fun onEdit(post: Post) {
//                viewModel.edit(post)
//                (newPostLauncher.contract as NewPostResultContract).content = post.content
//                newPostLauncher.launch()
//            }
//
//            override fun onCancelEdit(post: Post) {
//                viewModel.cancelEdit()
//            }
//
//            override fun onLike(post: Post) {
//                viewModel.likeById(post.id)
//            }
//
//            override fun onRemove(post: Post) {
//                viewModel.removeById(post.id)
//            }
//
//            override fun onShare(post: Post) {
//                val intent = Intent().apply {
//                    action = Intent.ACTION_SEND
//                    type = "text/plain"
//                    putExtra(Intent.EXTRA_TEXT, post.content)
//                }
//
//                val chooser = Intent.createChooser(intent, getString(R.string.chooser_share_post))
//                startActivity(chooser)
//
//                viewModel.shareById(post.id)
//            }
//
//            override fun onVideo(post: Post) {
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
//                val chooser = Intent.createChooser(intent, getString(R.string.chooser_share_post))
//                startActivity(chooser)
//            }
//        })
//        binding.list.adapter = adapter
//        viewModel.data.observe(this) { posts ->
//            adapter.submitList(posts)
//        }
//
//        newPostLauncher = registerForActivityResult(NewPostResultContract()) { result ->
//            if (result != null) {
//                viewModel.changeContent(result)
//                viewModel.save()
//            }
//        }
//
//        binding.add.setOnClickListener {
//            (newPostLauncher.contract as NewPostResultContract).content = ""
//            newPostLauncher.launch()
//        }
//
//    }
//
//}


