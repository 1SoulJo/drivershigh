package humber.its.drivershigh.data.localdata

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface HistoryDao {
    @Query("INSERT INTO histories (routeId, date) VALUES(:routeId, :date)")
    suspend fun insertHistory(routeId: Int, date: String)

    @Query("SELECT * FROM histories")
    fun observeAllHistories(): LiveData<List<History>>

    @Query("DELETE FROM histories")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM histories")
    fun observeAllHistoryAndRoute(): LiveData<List<HistoryAndRoute>>
}