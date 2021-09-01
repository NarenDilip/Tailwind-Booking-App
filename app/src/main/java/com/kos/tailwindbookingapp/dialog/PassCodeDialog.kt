package com.kos.tailwindbookingapp.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.kos.tailwindbookingapp.R
import com.kos.tailwindbookingapp.Util
import com.kos.tailwindbookingapp.model.LaneSession
import kotlinx.android.synthetic.main.dialog_login.closeView
import kotlinx.android.synthetic.main.dialog_passcode.*

class PassCodeDialog(val laneSession: LaneSession) : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_passcode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        isCancelable = true
        setupView(view)
        setupClickListeners(view)
    }

    private fun setupClickListeners(view: View) {
        closeView.setOnClickListener {
            context?.let { it1 -> Util.hideKeyboard(closeView, it1) }
            dismiss()
        }
    }

    private fun setupView(view: View) {
        lanePassCodeView.text = laneSession.passCode
        laneNumberView.text = "Lane ${laneSession.laneId} Code"
    }

    override fun onStart() {
        super.onStart()
        val width = (requireContext().resources.displayMetrics.widthPixels * 0.84)
        val height = (requireContext().resources.displayMetrics.heightPixels * 0.84)
        dialog?.window?.setLayout(
            width.toInt(),
            height.toInt()
        )
    }


}