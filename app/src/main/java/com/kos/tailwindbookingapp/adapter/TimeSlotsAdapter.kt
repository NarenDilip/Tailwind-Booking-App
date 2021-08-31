package com.kos.tailwindbookingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.kos.tailwindbookingapp.R


class TimeSlotsAdapter internal constructor(
    private val context: Context,
    private var callback: Callback,
    private val players: ArrayList<Int>
) :
    RecyclerView.Adapter<TimeSlotsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_time_slot, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val lane = players[holder.adapterPosition]
            holder.itemView.setOnClickListener {
                callback.viewTimeSlot(lane)
            }
            holder.renderView(lane)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return players.size
    }


    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var timeSlotView: AppCompatTextView = v.findViewById(R.id.timeSlotView)
        fun renderView(player: Int) {
            try {
                timeSlotView.text = player.toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    interface Callback {
        fun viewTimeSlot(timeSlot: Int)
    }


}