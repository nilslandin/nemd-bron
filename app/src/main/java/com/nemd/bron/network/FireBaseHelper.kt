package com.nemd.bron.network

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object FireBaseHelper {

    fun getFireBaseService(): FirebaseService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://europe-west1-nemd-bron.cloudfunctions.net/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create<FirebaseService>(FirebaseService::class.java)
    }

}