package fr.com.majdi.parking.network

import fr.com.majdi.parking.model.ResponseParking
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Majdi RABEH on 09/05/2021.
 * Email m.rabeh.majdi@gmail.com
 */
interface ApiParking {

    @GET("/api/records/1.0/search/")
    suspend fun getParking(
        @Query("dataset") dataset: String,
        @Query("rows") results: Int
    ): Response<ResponseParking>

}