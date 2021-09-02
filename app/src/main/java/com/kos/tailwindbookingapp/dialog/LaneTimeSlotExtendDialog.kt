package com.kos.tailwindbookingapp.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.kos.tailwindbookingapp.R
import com.kos.tailwindbookingapp.model.LaneSession
import kotlinx.android.synthetic.main.dialog_time_extender.*

class LaneTimeSlotExtendDialog(val laneSession: LaneSession, val callBack: Callback):DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_time_extender, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        isCancelable = true
        setupView(view)
        setupClickListeners(view)
    }

    private fun setupClickListeners(view: View) {
        confirmView.setOnClickListener {
            if(extended_time.text.toString().isEmpty()){
                header_layer_content.text = "Please enter valid time"
                header_layer_content.setTextColor(ContextCompat.getColor(requireContext(),R.color.orange))
                return@setOnClickListener
            }
            if(extended_time.text.toString() != ""){
                callBack.extendTimeSlot(extended_time.text.toString().toInt())
                dismiss()
            }
        }
        time_one.setOnClickListener {
            extended_time.setText("15")
        }
        time_two.setOnClickListener {
            extended_time.setText("30")
        }
        time_three.setOnClickListener {
            extended_time.setText("45")
        }

    }

    private fun setupView(view: View) {

    }

    override fun onStart() {
        super.onStart()
        val width = (requireContext().resources.displayMetrics.widthPixels * 0.30)
        val height = (requireContext().resources.displayMetrics.heightPixels * 0.40)
        dialog?.window?.setLayout(
            width.toInt(),
            height.toInt()
        )
    }

    interface Callback {
        fun extendTimeSlot(time:Int)

    }
}