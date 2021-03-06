package com.kos.tailwindbookingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kos.tailwindbookingapp.R


class PlayerAdapter internal constructor(
    private val context: Context,
    private var callback: Callback,
    private val players: ArrayList<Int>
) :
    RecyclerView.Adapter<PlayerAdapter.ViewHolder>() {

    private var rowIndex = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_player, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val lane = players[holder.adapterPosition]
            holder.playerView.setOnClickListener {
                callback.viewPlayer(lane, holder.adapterPosition)
                updatePlayerView(holder.adapterPosition)
            }
            holder.renderView(lane)
            if (rowIndex == holder.adapterPosition) {
                holder.playerView.setBackgroundResource(R.drawable.oval_shape_yellow)
                holder.playerView.setTextColor(ContextCompat.getColor(context, R.color.black))
            } else {
                holder.playerView.setBackgroundResource(R.drawable.black_circle)
                holder.playerView.setTextColor(ContextCompat.getColor(context, R.color.white))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updatePlayerView(position: Int){
        rowIndex = position
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return players.size
    }


    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var playerView: Button = v.findViewById(R.id.playerCountView)
        fun renderView(player: Int) {
            try {
                playerView.text = player.toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    interface Callback {
        fun viewPlayer(playerCount: Int, position: Int)
    }


}