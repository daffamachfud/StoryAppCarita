package com.daffa.storyappcarita.view.main

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.daffa.storyappcarita.databinding.ItemStoryAdapterBinding
import com.daffa.storyappcarita.model.ListStoryItem
import com.daffa.storyappcarita.view.detail.DetailActivity

class StoriesAdapter : RecyclerView.Adapter<StoriesAdapter.WishViewHolder>() {
    private var list = ArrayList<ListStoryItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun setStoriesList(stories: List<ListStoryItem>) {
        this.list.clear()
        this.list.addAll(stories)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WishViewHolder {
        val itemStoryAdapterBinding =
            ItemStoryAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WishViewHolder(itemStoryAdapterBinding)
    }

    override fun onBindViewHolder(holder: WishViewHolder, position: Int) {
        val stories = list[position]
        holder.bind(stories)
    }

    override fun getItemCount(): Int = list.size

    class WishViewHolder(private val binding: ItemStoryAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            Glide.with(itemView)
                .load(story.photoUrl)
                .into(binding.imageStory)

            binding.tvUsername.text = story.name
            binding.tvDesc.text = story.description

            binding.storyView.setOnClickListener {
                val intent =
                    Intent(binding.root.context, DetailActivity::class.java).apply {
                        putExtra(DetailActivity.DETAIL_NAME, story.name)
                        putExtra(DetailActivity.DETAIL_DESC, story.description)
                        putExtra(DetailActivity.DETAIL_IMG, story.photoUrl)
                    }
                binding.root.context.startActivity(intent)
            }
        }
    }
}