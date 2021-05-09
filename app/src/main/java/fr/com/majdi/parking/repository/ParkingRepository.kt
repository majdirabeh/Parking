package fr.com.majdi.parking.repository

import fr.com.majdi.parking.model.ResponseParking
import fr.com.majdi.parking.network.ApiParking
import fr.com.majdi.parking.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Created by Majdi RABEH on 09/05/2021.
 * Email m.rabeh.majdi@gmail.com
 */
class ParkingRepository(private val apiParking: ApiParking) {

    suspend fun getListParking(): Flow<ResponseParking?> {
        return flow {
            val result = apiParking.getParking(Constants.DATA_SET, Constants.NUMBER_PARKING)
            if (result.isSuccessful) {
                emit(result.body())
            }
        }.flowOn(Dispatchers.IO)
    }

}