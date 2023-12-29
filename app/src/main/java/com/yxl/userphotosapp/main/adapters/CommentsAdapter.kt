package com.yxl.userphotosapp.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yxl.userphotosapp.core.Utils
import com.yxl.userphotosapp.databinding.ItemCommentBinding
import com.yxl.userphotosapp.main.model.Comment

class CommentsAdapter: PagingDataAdapter<Comment, CommentsAdapter.ViewHolder>(differCallBack) {

    class ViewHolder(private val binding: ItemCommentBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(comment: Comment) = with(binding){
            tvComment.text = comment.text
            tvDate.text = Utils.convertToDateTime(comment.date, true)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding)
    }

    companion object{

        private val differCallBack = object : DiffUtil.ItemCallback<Comment>(){
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem == newItem
            }
        }
    }

}