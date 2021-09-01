package com.kos.tailwindbookingapp.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonObject
import com.kos.tailwindbookingapp.AppPreference
import com.kos.tailwindbookingapp.LaneListActivity
import com.kos.tailwindbookingapp.R
import com.kos.tailwindbookingapp.Util
import com.kos.tailwindbookingapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.dialog_login.*

class LoginDialog : DialogFragment() {
    private var userViewModel: UserViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_login, container, false)
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

        loginView.setOnClickListener {
            try {
                if(userNameView.text?.trim().toString().isEmpty()){
                    label.text = "USERNAME CAN'T BE EMPTY"
                    label.setTextColor(ContextCompat.getColor(requireContext(), R.color.errorColor))
                    return@setOnClickListener
                }
                if(passwordView.text?.trim().toString().isEmpty()){
                    label.text = "PASSWORD CAN'T BE EMPTY"
                    label.setTextColor(ContextCompat.getColor(requireContext(), R.color.errorColor))
                    return@setOnClickListener
                }

                if(passwordView.text?.trim().toString().length < 6){
                    label.text = "PASSWORD LENGTH MUST BE ABOVE 6"
                    label.setTextColor(ContextCompat.getColor(requireContext(), R.color.errorColor))
                    return@setOnClickListener
                }

                val jsonObject = JsonObject()
                jsonObject.addProperty("code", userNameView.text?.trim().toString())
                jsonObject.addProperty("password", passwordView?.text?.trim().toString())
                userViewModel!!.getLogin(jsonObject)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    private fun setupView(view: View) {
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel!!.init()
        userViewModel!!.loginResponseLiveData!!
            .observe(viewLifecycleOwner,
                { response ->
                    try {
                        if (response != null ){
                            if( response.responseMessage == "Success") {
                                AppPreference.put(requireContext(),"login_user", response.id!!)
                                context?.startActivity(Intent(context, LaneListActivity::class.java))
                            }else{
                                label.text = "INCORRECT USERNAME / PASSWORD"
                                label.setTextColor(ContextCompat.getColor(requireContext(), R.color.errorColor))
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                })
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