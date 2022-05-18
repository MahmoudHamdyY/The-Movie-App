package com.mhamdy.movieapp.home.movie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mhamdy.movieapp.R
import com.mhamdy.movieapp.databinding.ImageTitleCardItemBinding
import java.io.Serializable

class ImageTitleCardAdapter :
    ListAdapter<ImageTitleCardAdapter.ImageNameItem, ImageTitleCardAdapter.ImageTitleViewHolder>(
        imageTitleDiffUtil()
    ) {

    companion object {
        private fun imageTitleDiffUtil() = object : DiffUtil.ItemCallback<ImageNameItem>() {
            override fun areItemsTheSame(oldItem: ImageNameItem, newItem: ImageNameItem): Boolean =
                true

            override fun areContentsTheSame(
                oldItem: ImageNameItem,
                newItem: ImageNameItem
            ): Boolean = oldItem.imageUrl == newItem.imageUrl && oldItem.title == newItem.title

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageTitleViewHolder =
        ImageTitleViewHolder(
            ImageTitleCardItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ImageTitleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ImageTitleViewHolder(private val binding: ImageTitleCardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageNameItem: ImageNameItem) {
            with(binding) {
                titleTv.text = imageNameItem.title
                Glide.with(root.context)
                    .load(imageNameItem.imageUrl).fitCenter()
                    .placeholder(R.drawable.image_empty)
                    .error(R.drawable.image_empty)
                    .into(imageIv)
            }
        }

    }

    data class ImageNameItem(val imageUrl: String, val title: String) : Serializable

}