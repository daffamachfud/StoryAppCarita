package com.daffa.storyappcarita.ui.list

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.daffa.storyappcarita.databinding.ItemStoryAdapterBinding
import com.daffa.storyappcarita.model.response.ListStoryItem
import com.daffa.storyappcarita.ui.detail.DetailActivity

class ListAdapter :
    PagingDataAdapter<ListStoryItem, ListAdapter.MyViewHolder>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    class MyViewHolder(private val binding: ItemStoryAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListStoryItem) {
            binding.tvUsername.text = data.name
            binding.tvDesc.text = data.description
            Glide.with(itemView)
                .load(data.photoUrl)
                .into(binding.imageStory)
            binding.storyView.setOnClickListener {
                val intent =
                    Intent(binding.root.context, DetailActivity::class.java).apply {
                        putExtra(DetailActivity.DETAIL_NAME, data.name)
                        putExtra(DetailActivity.DETAIL_DESC, data.description)
                        putExtra(DetailActivity.DETAIL_IMG, data.photoUrl)
                    }
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemStoryAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
}