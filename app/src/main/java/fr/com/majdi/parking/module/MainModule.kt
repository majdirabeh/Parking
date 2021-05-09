package fr.com.majdi.parking.module

import android.content.Context
import com.mapbox.mapboxsdk.Mapbox
import fr.com.majdi.parking.R
import fr.com.majdi.parking.network.ApiParking
import fr.com.majdi.parking.repository.ParkingRepository
import fr.com.majdi.parking.utils.Constants.Companion.BASE_URL
import fr.com.majdi.parking.ui.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Majdi RABEH on 09/05/2021.
 * Email m.rabeh.majdi@gmail.com
 */

val mainModule = module {
    single { createWebService() }
    single { ParkingRepository(get()) }
    viewModel { MainViewModel(get()) }
}



fun createWebService(): ApiParking {

    val interceptor = HttpLoggingInterceptor()
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(BASE_URL)
        .client(client)
        .build()

    return retrofit.create(ApiParking::class.java)
}