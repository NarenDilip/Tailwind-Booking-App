package com.kos.tailwindbookingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.kos.tailwindbookingapp.R


class TimeSlotsAdapter internal constructor(
    private val context: Context,
    private var callback: Callback,
    private val timeSlots: ArrayList<Int>
) :
    RecyclerView.Adapter<TimeSlotsAdapter.ViewHolder>() {

    private var rowIndex = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_time_slot, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val lane = timeSlots[holder.adapterPosition]
            holder.timeSlotRootView.setOnClickListener {
                callback.viewTimeSlot(lane)
                rowIndex = holder.adapterPosition
                notifyItemChanged(holder.adapterPosition)
            }
            holder.renderView(lane)
            if (rowIndex == holder.adapterPosition) {
                holder.timeSlotRootView.setBackgroundResource(R.drawable.rectangle_shape_yellow)
            } else {
                holder.timeSlotRootView.setBackgroundResource(R.drawable.rectangle_shape_black)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return timeSlots.size
    }


    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var timeSlotView: AppCompatTextView = v.findViewById(R.id.timeSlotView)
        var timeSlotRootView: ConstraintLayout = v.findViewById(R.id.timeSlotRootView)
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