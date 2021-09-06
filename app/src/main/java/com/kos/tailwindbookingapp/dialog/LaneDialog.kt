package com.kos.tailwindbookingapp.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.kos.tailwindbookingapp.AppPreference
import com.kos.tailwindbookingapp.R
import com.kos.tailwindbookingapp.adapter.PlayerAdapter
import com.kos.tailwindbookingapp.adapter.TimeSlotsAdapter
import com.kos.tailwindbookingapp.model.LaneSession
import com.kos.tailwindbookingapp.viewmodel.LaneSessionViewModel
import kotlinx.android.synthetic.main.dialog_lane.*

class LaneDialog(val laneSession: LaneSession) : DialogFragment() {
    private var laneSessionViewModel: LaneSessionViewModel? = null
    var players = arrayListOf(1, 2, 3, 4, 5, 6, 7, 8)
    var defaultTimeSlots = arrayListOf(30, 90, 120, 150, 180, 210, 240)
    var playerPosition:Int = -1
    var timeSlotPosition:Int = -1
    var timeSlotsAdapter:TimeSlotsAdapter?=null
    var playerAdapter:PlayerAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_lane, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try{
            dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
            isCancelable = true
            setupView(view)
            setupClickListeners(view)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun setupClickListeners(view: View) {
        closeView.setOnClickListener {
            dismiss()
        }
        setPackageView.setOnClickListener {
            try {
                validateLaneSession()
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        endSessionView.setOnClickListener {
            try{
                laneSession.status = "END"
                updateLaneSession(laneSession)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    private fun setupView(view: View) {
        if(laneSession.isOccupied){
            sessionPassCodeView.visibility = View.VISIBLE
            sessionPassCodeView.text = "PASS CODE : ${laneSession.passCode}"
        }
        laneLabelView.text = "Lane ${laneSession.laneId}"
        laneSessionViewModel =
            ViewModelProvider(this).get(LaneSessionViewModel::class.java)
        laneSessionViewModel!!.init()
        laneSessionViewModel!!.laneSessionResponseLiveData!!
            .observe(viewLifecycleOwner,
                { response ->
                    try {
                        if (response != null) {
                            if (response.responseMessage == "Success") {
                                if(!laneSession.isOccupied){
                                    passcode_dialog_view.visibility = View.VISIBLE
                                    lane_dialog_view.visibility = View.GONE
                                    lanePassCodeView.text = response.passCode
                                    laneNumberView.text = "Lane ${laneSession.laneId} Code"
                                }else{
                                    dismiss()
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                })
        playerAdapter = PlayerAdapter(requireActivity(), object : PlayerAdapter.Callback {
            override fun viewPlayer(playerCount: Int, position:Int) {
              laneSession.noOfPlayers = playerCount
                playerPosition = position
            }
        }, players)
        playerRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        playerRecyclerView?.adapter = playerAdapter
        timeSlotsAdapter = TimeSlotsAdapter(requireActivity(), object : TimeSlotsAdapter.Callback {
            override fun viewTimeSlot(timeSlot: Int, position: Int) {
                timeSlotPosition = position
                laneSession.duration = timeSlot
            }

            override fun laneTimeExtend() {
                val laneTimeSlotExtendDialog = LaneTimeSlotExtendDialog(laneSession = laneSession,
                    callBack = object:LaneTimeSlotExtendDialog.Callback{
                        override fun extendTimeSlot(time: Int) {
                            val updatedTime =
                                defaultTimeSlots[
                                        timeSlotPosition] +
                                        time
                            updateTimeSlots(updatedTime)
                        }

                    })
                laneTimeSlotExtendDialog.show(childFragmentManager, "Time Extend")
            }
        }, defaultTimeSlots,view, laneSession)
        timeSlotRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        timeSlotRecyclerView?.adapter = timeSlotsAdapter
        updateLaneData()
    }

    private fun updateLaneData() {
        if(laneSession.isOccupied){
            endSessionView.visibility = View.VISIBLE
            playerAdapter?.updatePlayerView(players.indexOf(laneSession.noOfPlayers))
            timeSlotsAdapter!!.updateTimeView(defaultTimeSlots.indexOf(laneSession.duration))
            playerPosition = players.indexOf(laneSession.noOfPlayers)
            timeSlotPosition = defaultTimeSlots.indexOf(laneSession.duration)
        }
        if(defaultTimeSlots.indexOf(laneSession.duration) == -1){
            updateTimeSlots(laneSession.duration)
        }
    }

    private fun updateTimeSlots(extraTime:Int){
        updateExtendedTime(extraTime)
        timeSlotsAdapter?.notifyDataSetChanged()
        timeSlotPosition = defaultTimeSlots.indexOf(extraTime)
        timeSlotsAdapter!!.updateTimeView(timeSlotPosition)
    }

    private fun updateExtendedTime(updatedTime:Int){
        defaultTimeSlots
            .add(updatedTime)
        val hashSet = HashSet<Int>()
        hashSet.addAll(defaultTimeSlots)
        defaultTimeSlots.clear()
        defaultTimeSlots.addAll(hashSet)
        defaultTimeSlots.sort()
    }

    private fun validateLaneSession() {
        if(playerPosition == -1){
            player_empty.visibility = View.VISIBLE
            return
        }
        if(timeSlotPosition == -1){
            player_empty.visibility = View.GONE
            timer_empty.visibility = View.VISIBLE
            return
        }
        timer_empty.visibility = View.GONE

        val isSameTimeSlot = defaultTimeSlots[timeSlotPosition] == laneSession.duration
        val isSamePlayerSlot = players[playerPosition] == laneSession.noOfPlayers

        if (timeSlotPosition != -1 && !isSameTimeSlot) {
            val timeDuration = laneSession.duration
            val extraTime = defaultTimeSlots[timeSlotPosition] - timeDuration;
            laneSession.extraTime = extraTime
        } else {
            laneSession.extraTime = 0
        }
        if (playerPosition != -1 && !isSamePlayerSlot) {
            laneSession.noOfPlayers = players[playerPosition]
        }

        if(laneSession.status == "TIMEOUT"){
            laneSession.status = "ACTIVE"
            updateLaneSession(laneSession)
        } else if(laneSession.status == "IDLE" || laneSession.status == "ACTIVE"){
            updateLaneSession(laneSession)
        }else{
            postLaneSessionRequest()
        }

    }
    private fun postLaneSessionRequest(){
        val jsonObject = JsonObject()
        jsonObject.addProperty("lane_id", laneSession.laneId)
        jsonObject.addProperty("created_by", AppPreference[requireContext(), "login_user", ""])
        jsonObject.addProperty("duration", laneSession.duration)
        jsonObject.addProperty("extra_time", 0)
        jsonObject.addProperty("no_of_players",laneSession.noOfPlayers)
        print("Update Lane session - $jsonObject");
        laneSessionViewModel?.updateLaneSession(jsonObject, laneSession.laneId.toString())
    }

    private fun updateLaneSession(laneSession: LaneSession){
        val gson = Gson()
        val laneSessionString = gson.toJson(laneSession)
        val convertedObject: JsonObject =
            Gson().fromJson(laneSessionString, JsonObject::class.java)
        laneSessionViewModel?.updateLaneSession(convertedObject, laneSession.laneId.toString())
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