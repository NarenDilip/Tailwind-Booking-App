package com.kos.tailwindbookingapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kos.tailwindbookingapp.model.LaneSession


@Dao
interface DatabaseDao {

    @Query("select * from laneSession where id = :laneSessionId ")
    fun getLaneSession(laneSessionId: String): LaneSession

    @Query("select * from laneSession where id = :laneSessionId ")
    fun listenLaneSession(laneSessionId: String): LiveData<LaneSession>

    @Query("select * from laneSession")
    fun listenLanes(): LiveData<List<LaneSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLaneSession(laneSession: LaneSession)

    @Query("UPDATE laneSession SET status =:status where id = :laneSessionId ")
    fun updateLaneSession(status: String?, laneSessionId: String): Int



}
