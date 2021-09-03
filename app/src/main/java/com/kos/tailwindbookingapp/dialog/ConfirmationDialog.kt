package com.kos.tailwindbookingapp.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.kos.tailwindbookingapp.R
import com.kos.tailwindbookingapp.Util
import kotlinx.android.synthetic.main.dialog_confirmation_popup.*
import kotlinx.android.synthetic.main.dialog_login.*

class ConfirmationDialog(val callBack: Callback, val titleContent:String):DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_confirmation_popup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        isCancelable = true
        setupView(view)
        setupClickListeners(view)
    }

    private fun setupClickListeners(view: View) {
        yesView.setOnClickListener {
            dismiss()
            callBack.yes()
        }
        noView.setOnClickListener {
            dismiss()
            callBack.no()
        }
    }

    private fun setupView(view: View) {
        confirmationLabelView.text = titleContent
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

    interface Callback {
        fun yes()
        fun no()
    }
}