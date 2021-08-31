package com.kos.tailwindbookingapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.kos.tailwindbookingapp.adapter.LaneListAdapter
import com.kos.tailwindbookingapp.viewmodel.LaneListViewModel

import com.tailwind.kos.model.LaneSession
import kotlinx.android.synthetic.main.activity_dashboard.*


class LaneListActivity : AppCompatActivity() {
    private var viewModel: LaneListViewModel? = null
    private var adapter: LaneListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_dashboard)
            initAdapter()
            initViewModel()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            viewModel!!.getLanes()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(LaneListViewModel::class.java)
        viewModel!!.init()
        viewModel!!.laneListResponseLiveData!!
            .observe(this,
                { laneListResponse ->
                    try {
                        if (laneListResponse != null) {
                            adapter!!.setResults(laneListResponse.lanes!!)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                })
    }

    private fun initAdapter() {
        val linearLayoutManager = GridLayoutManager(this@LaneListActivity, 4)
        lanesRecyclerView.layoutManager = linearLayoutManager
        adapter =
            LaneListAdapter(this@LaneListActivity, object : LaneListAdapter.Callback {
                override fun viewLane(lane: LaneSession) {
                    try {

                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }

            })
        lanesRecyclerView.adapter = adapter
    }

    override fun onBackPressed() {
        return
    }

}