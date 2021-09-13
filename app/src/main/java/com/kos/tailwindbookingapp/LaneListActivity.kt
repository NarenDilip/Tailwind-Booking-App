package com.kos.tailwindbookingapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.kos.tailwindbookingapp.adapter.LaneListAdapter
import com.kos.tailwindbookingapp.dialog.LaneDialog
import com.kos.tailwindbookingapp.viewmodel.LaneListViewModel

import com.kos.tailwindbookingapp.model.LaneSession
import com.kos.tailwindbookingapp.db.AppDatabase
import kotlinx.android.synthetic.main.activity_dashboard.*
import okhttp3.Request
import okhttp3.Response
import com.here.oksse.OkSse
import com.here.oksse.ServerSentEvent
import com.kos.tailwindbookingapp.dialog.ConfirmationDialog
import com.kos.tailwindbookingapp.dialog.LoginDialog
import com.kos.tailwindbookingapp.ui.LoginScreen
import com.kos.tailwindbookingapp.ui.SplashScreen


class LaneListActivity : AppCompatActivity() {
    private var viewModel: LaneListViewModel? = null
    private var adapter: LaneListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setupView()
            setOnClickListeners()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setOnClickListeners() {
        exitView.setOnClickListener {
            val confirmationDialog = ConfirmationDialog(object: ConfirmationDialog.Callback{
                override fun yes() {
                    startActivity(Intent(this@LaneListActivity, SplashScreen::class.java))
                }

                override fun no() {

                }

            }, "Are you sure? Do you want to exit the app?")
            confirmationDialog.show(supportFragmentManager, "Confirm")
        }
    }

    private fun setupView() {
        setContentView(R.layout.activity_dashboard)
        initAdapter()
        initViewModel()
        viewModel!!.getLanes()
        listenLaneSession(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(LaneListViewModel::class.java)
        viewModel!!.init()
        viewModel!!.laneListResponseLiveData!!
            .observe(this,
                { laneListResponse ->
                    try {
                        if (laneListResponse != null) {
                            for(lane in laneListResponse.lanes!!){
                                AppDatabase.getAppDatabase(this).databaseDao().insertLaneSession(lane)
                            }
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
                        val laneDialog = LaneDialog(laneSession = lane)
                        laneDialog.show(supportFragmentManager, "Lane")
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
            })
        val lanesLiveData = AppDatabase.getAppDatabase(this).databaseDao().listenLanes()
        adapter?.laneListChangeObserver?.let {
            lanesLiveData.observe(this, it)
        }
        lanesRecyclerView.adapter = adapter
        lanesRecyclerView.setHasFixedSize(true)
    }

    private fun listenLaneSession(context: Context) {
        val request: Request = Request.Builder()
            .url("${BuildConfig.SERVER_URL}on_update_sessions")
            .build()
        val okSse = OkSse()
        okSse.newServerSentEvent(request, object : ServerSentEvent.Listener {
            override fun onOpen(sse: ServerSentEvent?, response: Response?) {
                Log.d("Open Response", response.toString())
                AppPreference.put(context, "SSE_LANE_SESSION_ENABLE", 1)
            }

            override fun onMessage(
                sse: ServerSentEvent?,
                id: String?,
                event: String?,
                message: String?) {
                try {
                    Log.d("Listen Response", message.toString())
                    val gson = Gson()
                    val session = gson.fromJson(message, LaneSession::class.java)
                    AppDatabase.getAppDatabase(context).databaseDao().insertLaneSession(session)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onComment(sse: ServerSentEvent?, comment: String?) {
                Log.d("Comment Response", comment.toString())
            }

            override fun onRetryTime(sse: ServerSentEvent?, milliseconds: Long): Boolean {
                TODO("Not yet implemented")
            }

            override fun onRetryError(
                sse: ServerSentEvent?,
                throwable: Throwable?,
                response: Response?): Boolean {
                return true
            }

            override fun onClosed(sse: ServerSentEvent?) {
                Log.d("Closed Response", sse.toString())
                AppPreference.put(context, "SSE_LANE_SESSION_ENABLE", 0)
            }

            override fun onPreRetry(sse: ServerSentEvent?, originalRequest: Request?): Request {
                return originalRequest!!
            }

        })

    }

    override fun onBackPressed() {
        return
    }
}