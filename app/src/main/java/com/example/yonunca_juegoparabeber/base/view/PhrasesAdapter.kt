package com.example.yonunca_juegoparabeber.base.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yonunca_juegoparabeber.R
import com.example.yonunca_juegoparabeber.base.model.Phrase
import com.example.yonunca_juegoparabeber.community.view.OnLikePressed
import com.example.yonunca_juegoparabeber.databinding.ItemCommunityWordsBinding
import com.example.yonunca_juegoparabeber.online.view.SearchGameAdapter

class PhrasesAdapter(
    private val listener: OnLikePressed,
    private val list: MutableList<Phrase>
) : RecyclerView.Adapter<PhrasesAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ItemCommunityWordsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Phrase, position: Int) {
            binding.communityWords.text = item.mensaje
            binding.likes.text = item.likes.toString()
            if (item.isLiked) {
                binding.icLike.setImageResource(R.drawable.ic_red_heart)
            } else {
                binding.icLike.setImageResource(R.drawable.ic_white_heart)
            }
            binding.icLike.setOnClickListener {
                if (!item.isLiked) {
                    listener.onLikeAdded(item)
                    item.isLiked = true
                    notifyItemChanged(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCommunityWordsBinding.inflate(
            inflater,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int =
        list.size

    fun updateData(data: List<Phrase>){
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

}

