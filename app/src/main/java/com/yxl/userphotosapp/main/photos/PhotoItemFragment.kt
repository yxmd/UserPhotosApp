package com.yxl.userphotosapp.main.photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.size.Scale
import com.yxl.userphotosapp.R
import com.yxl.userphotosapp.core.Utils
import com.yxl.userphotosapp.databinding.FragmentPhotoItemBinding
import com.yxl.userphotosapp.main.adapters.CommentsAdapter
import com.yxl.userphotosapp.main.model.PhotoResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class PhotoItemFragment : Fragment() {

    private lateinit var binding: FragmentPhotoItemBinding
    private val viewModel by viewModels<PhotosViewModel>(ownerProducer = { requireActivity() })
    private lateinit var commentsAdapter: CommentsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentPhoto.collect { photo ->
                if (photo != null) {
                    setPhotoItemView(photo)
                }
            }
        }

        setUpRecycler()

        binding.ivSend.setOnClickListener {
            viewModel.postComment(binding.etComment.text.toString())
            binding.etComment.text.clear()
        }

    }

    private fun setPhotoItemView(photo: PhotoResponse) = with(binding) {
        ivPhoto.load(photo.url) {
            crossfade(true)
            placeholder(R.drawable.ic_question)
            scale(Scale.FILL)
        }
        tvDate.text = Utils.convertToDateTime(photo.date, true)
    }

    private fun setUpRecycler(){
        commentsAdapter = CommentsAdapter()
        binding.rvComments.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = commentsAdapter
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.commentListPager.collectLatest {
                    withContext(Dispatchers.Main){
                        commentsAdapter.submitData(it)
                    }
                }
            }
        }

    }

}