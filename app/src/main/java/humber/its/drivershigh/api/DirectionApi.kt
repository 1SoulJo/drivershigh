package humber.its.drivershigh.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionApi {
    @GET("directions/json")
    fun getRoute(
        @Query("origin") origin: String,
        @Query("destination") dest: String,
        @Query("key") key: String
    ) : Call<ResponseBody>
}