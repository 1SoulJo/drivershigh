package humber.its.drivershigh.data.localdata

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RouteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoute(route: Route)

    @Delete
    suspend fun deleteRoute(route: Route)

    @Query("DELETE FROM routes")
    suspend fun deleteAll()

    @Query("SELECT * FROM routes")
    fun observeAllRoutes(): LiveData<List<Route>>

    @Query("SELECT count(*) FROM routes")
    fun observeTotalNumber(): LiveData<Int>
}