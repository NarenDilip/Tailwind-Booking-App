package com.kos.tailwindbookingapp.adapter

import android.content.Context
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kos.tailwindbookingapp.R
import com.kos.tailwindbookingapp.Util
import com.kos.tailwindbookingapp.model.LaneSession
import java.util.concurrent.TimeUnit


class TimeSlotsAdapter internal constructor(
    private val context: Context,
    private var callback: Callback,
    private val timeSlots: ArrayList<Int>,
    val view: View,
    val laneSession: LaneSession
) :
    RecyclerView.Adapter<TimeSlotsAdapter.ViewHolder>() {
    private var rowIndex = -1
    private var isLoadFirst = true
    var mPopupwindow: PopupWindow? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_time_slot, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val v: View = LayoutInflater.from(context).inflate(
            R.layout.select_mins_extender,
            view.findViewById<ConstraintLayout>(R.id.popupmessage),
            false
        )

        mPopupwindow = PopupWindow(v, 300, 150, true)
        mPopupwindow = PopupWindow(
            v, ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val remainingTimeView = v.findViewById<TextView>(R.id.remainingTimeView)
        remainingTimeView.text = getRemainingTime(laneSession = laneSession)
        val extendView = v.findViewById<TextView>(R.id.extendView)
        extendView.setOnClickListener {
            mPopupwindow!!.dismiss()
            callback.laneTimeExtend()
        }
        try {
            val lane = timeSlots[holder.adapterPosition]
            holder.renderView(lane)
            if (rowIndex == holder.adapterPosition) {
                holder.timeSlotRootView.setBackgroundResource(R.drawable.rectangle_shape_yellow)
                if (laneSession.status == "IDLE" || laneSession.status == "TIMEOUT" || laneSession.status == "ACTIVE"){
                    holder.arrow.visibility = View.VISIBLE
                }
                holder.timeSlotView.setTextColor(ContextCompat.getColor(context, R.color.black))
                holder.minLabelView.setTextColor(ContextCompat.getColor(context, R.color.black))
                if (isLoadFirst && laneSession.isOccupied) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        showPopup(
                            holder,
                            mPopupwindow!!
                        )
                    }, 50)

                }
            } else {
                holder.timeSlotRootView.setBackgroundResource(R.drawable.rectangle_shape_black)
                holder.arrow.visibility = View.GONE
                if (validateTimeSlot(lane = laneSession, index = holder.adapterPosition)) {
                    holder.timeSlotRootView.isEnabled = false
                    holder.timeSlotView.setTextColor(
                        ContextCompat.getColor(context, R.color.app_lite_grey)
                    )
                    holder.minLabelView.setTextColor(
                        ContextCompat.getColor(context, R.color.app_lite_grey)
                    )
                } else {
                    holder.timeSlotView.setTextColor(ContextCompat.getColor(context, R.color.white))
                    holder.minLabelView.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
            }
            holder.timeSlotRootView.setOnClickListener {
                callback.viewTimeSlot(lane, holder.adapterPosition)
                if (laneSession.isOccupied) {
                    showPopup(holder, mPopupwindow!!)
                }
                isLoadFirst = false
                updateTimeView(holder.adapterPosition)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateTimeView(position: Int, holder: ViewHolder? = null) {
        rowIndex = position
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
        } else if (laneSession.status == "TIMEOUT") {
            return "Time Elapsed";
        } else {
            return "";
        }
    }

    private fun validateTimeSlot(lane: LaneSession, index: Int):
            Boolean {
        if (lane.status == "ACTIVE" || lane.status == "TIMEOUT") {
            if (lane.status == "ACTIVE") {
                val minutes: Long =
                    TimeUnit.MILLISECONDS.toMinutes(Util.getRemainingTimeInMilliseconds(lane))
                return lane.duration - minutes > timeSlots[index]
            } else {
                return lane.duration > timeSlots[index]
            }
        }
        return false
    }

    private fun showPopup(holder: ViewHolder, mPopupwindow: PopupWindow) {

        val p = Point()
        val location = IntArray(2)
        holder.minLabelView.getLocationOnScreen(location);
        p.x = location.get(0)
        p.y = location.get(1)

        val OFFSET_X = -200
        val OFFSET_Y = 0

        mPopupwindow.setBackgroundDrawable(BitmapDrawable())
        mPopupwindow.setOutsideTouchable(true)

        mPopupwindow.showAtLocation(
            holder.minLabelView,
            Gravity.NO_GRAVITY,
            p.x + OFFSET_X,
            p.y + OFFSET_Y
        )


    }

    override fun getItemCount(): Int {
        return timeSlots.size
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var timeSlotView: AppCompatTextView = v.findViewById(R.id.timeSlotView)
        var minLabelView: AppCompatTextView = v.findViewById(R.id.minLabelView)
        var arrow: AppCompatTextView = v.findViewById(R.id.arrow)
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
        fun viewTimeSlot(timeSlotSelection: Int, position: Int)
        fun laneTimeExtend()
    }
}