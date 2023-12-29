package com.yxl.userphotosapp.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.yxl.userphotosapp.R
import com.yxl.userphotosapp.core.Utils
import com.yxl.userphotosapp.databinding.ItemPhotoBinding
import com.yxl.userphotosapp.main.model.PhotoResponse

class PhotosAdapter : PagingDataAdapter<PhotoResponse, PhotosAdapter.ViewHolder>(differCallBack){
    private var onClickListener: OnClickListener? = null

    class ViewHolder(private val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(photoResponse: PhotoResponse) = with(binding){
            tvDate.text = Utils.convertToDateTime(photoResponse.date, false)
            ivPhoto.load(photoResponse.url){
                crossfade(true)
                placeholder(R.drawable.ic_question)
                scale(Scale.FILL)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
        holder.itemView.setOnClickListener {
            if(onClickListener != null){
                onClickListener!!.onClick(position, getItem(position)!!)
            }
        }
        holder.itemView.setOnLongClickListener {
            if(onClickListener != null){
                onClickListener!!.onLongClick(position, getItem(position)!!)

            }
            return@setOnLongClickListener true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, photo: PhotoResponse)
        fun onLongClick(position: Int, photo: PhotoResponse)
    }

    companion object{
        private val differCallBack = object : DiffUtil.ItemCallback<PhotoResponse>(){
            override fun areItemsTheSame(oldItem: PhotoResponse, newItem: PhotoResponse): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PhotoResponse, newItem: PhotoResponse): Boolean {
                return oldItem == newItem
            }
        }
    }

}