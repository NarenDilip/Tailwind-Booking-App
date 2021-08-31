package com.kos.tailwindbookingapp.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.kos.tailwindbookingapp.LaneListActivity
import com.kos.tailwindbookingapp.R
import com.kos.tailwindbookingapp.Util
import com.kos.tailwindbookingapp.adapter.PlayerAdapter
import com.kos.tailwindbookingapp.adapter.TimeSlotsAdapter
import com.kos.tailwindbookingapp.viewmodel.LaneSessionViewModel
import com.kos.tailwindbookingapp.viewmodel.UserViewModel
import com.tailwind.kos.model.LaneSession
import kotlinx.android.synthetic.main.dialog_lane.*
import kotlinx.android.synthetic.main.dialog_login.*
import kotlinx.android.synthetic.main.dialog_login.closeView


class LaneDialog(val laneSession: LaneSession) : DialogFragment() {
    private var laneSessionViewModel: LaneSessionViewModel? = null
    var players = arrayListOf<Int>(1, 2, 3, 4, 5, 6, 7, 8)
    var defaultTimeSlots = arrayListOf<Int>(30, 90, 120, 150, 180, 210, 240)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_lane, container, false)
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

        setPackageView.setOnClickListener {
            try {
               /* if(userNameView.text?.trim().toString().isEmpty()){
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
                userViewModel!!.getLogin(jsonObject)*/

                activateLaneSession()
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    private fun setupView(view: View) {
        laneSessionViewModel =
            ViewModelProvider(this).get(LaneSessionViewModel::class.java)
        laneSessionViewModel!!.init()
        laneSessionViewModel!!.laneSessionResponseLiveData!!
            .observe(viewLifecycleOwner,
                { response ->
                    try {
                        if (response != null) {
                            if (response.responseMessage == "Success") {

                            }
                        }
                    } catch (e: Exception) {

                        e.printStackTrace()
                    }
                })

        val playerAdapter = PlayerAdapter(requireActivity(), object : PlayerAdapter.Callback {
            override fun viewPlayer(playerCount: Int) {
              laneSession.noOfPlayers = playerCount
            }
        }, players)
        playerRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        playerRecyclerView?.adapter = playerAdapter

        val timeSlotsAdapter = TimeSlotsAdapter(requireActivity(), object : TimeSlotsAdapter.Callback {
            override fun viewTimeSlot(timeSlot: Int) {
               laneSession.duration = timeSlot
            }
        }, defaultTimeSlots)
        timeSlotRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        timeSlotRecyclerView?.adapter = timeSlotsAdapter
    }

    private fun activateLaneSession() {
        val jsonObject = JsonObject()

        jsonObject.addProperty("lane_id", laneSession.laneId)
        jsonObject.addProperty("id", laneSession.id)
        jsonObject.addProperty("created_by", "")
        jsonObject.addProperty("duration", laneSession.duration)
        jsonObject.addProperty("extra_time", 0)
        jsonObject.addProperty("no_of_players",laneSession.noOfPlayers)


        laneSessionViewModel?.updateLaneSession(jsonObject, laneSession.laneId.toString())
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