package com.kos.tailwindbookingapp.adapter

import android.content.Context
import android.graphics.Point
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListPopupWindow
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kos.tailwindbookingapp.R

class TimeSlotsAdapter internal constructor(
    private val context: Context,
    private var callback: Callback,
    private val timeSlots: ArrayList<Int>,
    val view: View
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
                updateTimeView(holder.adapterPosition, holder, view)
                showPopup(holder)
            }

            holder.renderView(lane)
            if (rowIndex == holder.adapterPosition) {
                holder.timeSlotRootView.setBackgroundResource(R.drawable.rectangle_shape_yellow)
                holder.timeSlotView.setTextColor(ContextCompat.getColor(context, R.color.black))
                holder.minLabelView.setTextColor(ContextCompat.getColor(context, R.color.black))

            } else {
                holder.timeSlotRootView.setBackgroundResource(R.drawable.rectangle_shape_black)
                holder.timeSlotView.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.minLabelView.setTextColor(ContextCompat.getColor(context, R.color.white))
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateTimeView(position: Int, holder: ViewHolder, view: View) {
        rowIndex = position
        notifyDataSetChanged()
    }

    private fun showPopup(holder: ViewHolder) {
        val mPopupwindow: PopupWindow
        val v: View = LayoutInflater.from(context).inflate(
            R.layout.select_mins_extender,
            view.findViewById<ConstraintLayout>(R.id.popupmessage),
            false
        )

        val p = Point()
        val location = IntArray(2)
        holder.minLabelView.getLocationOnScreen(location);
        p.x = location.get(0)
        p.y = location.get(1)

        val OFFSET_X = -150
        val OFFSET_Y = 0

        mPopupwindow = PopupWindow(v, 300, 150, true)
        mPopupwindow.showAtLocation(
            holder.minLabelView,
            Gravity.NO_GRAVITY,
            p.x + OFFSET_X,
            p.y + OFFSET_Y
        )
        mPopupwindow.setInputMethodMode(ListPopupWindow.INPUT_METHOD_NOT_NEEDED)
        mPopupwindow.setBackgroundDrawable(null);
        mPopupwindow.setFocusable(true);
        mPopupwindow.setClippingEnabled(true);
        mPopupwindow.setOnDismissListener(PopupWindow.OnDismissListener { // TODO Auto-generated method stub
            Toast.makeText(context, "Dismissed", Toast.LENGTH_SHORT).show()
        })

        if (mPopupwindow.isShowing()) {
        }
    }

    override fun getItemCount(): Int {
        return timeSlots.size
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var timeSlotView: AppCompatTextView = v.findViewById(R.id.timeSlotView)
        var minLabelView: AppCompatTextView = v.findViewById(R.id.minLabelView)
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