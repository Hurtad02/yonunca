package com.example.yonunca_juegoparabeber.online.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.yonunca_juegoparabeber.R
import com.example.yonunca_juegoparabeber.databinding.ItemRoomsBinding
import com.example.yonunca_juegoparabeber.online.model.Room

class SearchGameAdapter(
    private val listener: OnJoinPress,
    private val list: MutableList<Room>
) : RecyclerView.Adapter<SearchGameAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ItemRoomsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val grayColor = ContextCompat.getColor(itemView.context, R.color.gray_light_trans)
        fun bind(item: Room) {
            binding.nameRoom.text = item.name
            binding.numberPlayers.text = binding.root.context.getString(R.string.number_players, item.players.size)
            if (item.isFull()){
                binding.joinButton.isEnabled = false
                binding.joinButton.setBackgroundColor(grayColor)
            } else {
                binding.joinButton.setOnClickListener {
                    listener.onJoinPressed(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRoomsBinding.inflate(
            inflater,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int =
        list.size

    fun updateData(data: List<Room>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

}

