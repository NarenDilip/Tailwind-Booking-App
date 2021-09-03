package com.kos.tailwindbookingapp.adapter

import android.content.Context
import android.graphics.Point
import android.view.*
import android.widget.ListPopupWindow
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kos.tailwindbookingapp.R
import com.kos.tailwindbookingapp.Util
import com.kos.tailwindbookingapp.model.LaneSession

class TimeSlotsAdapter internal constructor(
    private val context: Context,
    private var callback: Callback,
    private val timeSlots: ArrayList<Int>,
    val view: View,
    val laneSession: LaneSession
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
                callback.viewTimeSlot(lane, holder.adapterPosition)
                updateTimeView(holder.adapterPosition)
                showPopup(holder)
            }
            holder.renderView(lane)
            if (rowIndex == holder.adapterPosition) {
                holder.timeSlotRootView.setBackgroundResource(R.drawable.rectangle_shape_yellow)
                holder.timeSlotView.setTextColor(ContextCompat.getColor(context, R.color.black))
                holder.minLabelView.setTextColor(ContextCompat.getColor(context, R.color.black))
            } else {
                if (laneSession.startedOn != null && validateTimeSlot(laneSession, position,
                        timeSlots)) {
                    holder.timeSlotRootView.isEnabled = false
                    holder.timeSlotView.setTextColor(ContextCompat.getColor(context, R.color.app_lite_grey))
                    holder.minLabelView.setTextColor(ContextCompat.getColor(context, R.color.app_lite_grey))
                } else {
                    holder.timeSlotRootView.setBackgroundResource(R.drawable.rectangle_shape_black)
                    holder.timeSlotView.setTextColor(ContextCompat.getColor(context, R.color.white))
                    holder.minLabelView.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateTimeView(position: Int, holder: ViewHolder? = null) {
        rowIndex = position
        //showPopup(view)
        notifyDataSetChanged()
    }

    private fun getRemainingTime(laneSession: LaneSession): String {
        if (laneSession.status == "ACTIVE" || laneSession.status == "IDLE") {
            if (laneSession.startedOn != null) {
                val min =
                    (Util.getRemainingTimeInMilliseconds(laneSession) / 1000) / 60
                return "$min mins left";
            }
            return "Yet to start";
        } else {
            return "Time Elapsed";
        }
    }

    private fun validateTimeSlot(
        lane: LaneSession,
        index: Int,
        timeSlots: ArrayList<Int>
    ): Boolean {
        return if (lane.status == "ACTIVE" || lane.status == "TIMEOUT") {
            if (lane.status == "ACTIVE") {
                val min =
                    (Util.getRemainingTimeInMilliseconds(laneSession) / 1000) / 60
                lane.duration - min > timeSlots[index]
            } else {
                lane.duration > timeSlots[index]
            }
        } else {
            false
        }
    }


    private fun showPopup(holder: ViewHolder) {
        var mPopupwindow: PopupWindow? = null
        val v: View = LayoutInflater.from(context).inflate(
            R.layout.select_mins_extender,
            view.findViewById<ConstraintLayout>(R.id.popupmessage),
            false
        )
        val remainingTimeView = v.findViewById<TextView>(R.id.remainingTimeView)
        remainingTimeView.text = getRemainingTime(laneSession = laneSession)

        val extendView = v.findViewById<TextView>(R.id.extendView)
        extendView.setOnClickListener {
            mPopupwindow!!.dismiss()
            callback.laneTimeExtend()
        }

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
        fun viewTimeSlot(timeSlot: Int, position: Int)
        fun laneTimeExtend()
    }
}