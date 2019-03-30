package com.nemd.bron.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface FirebaseService {

    @GET("addPendingRequestAndNotifyPatient")
    fun addPendingRequest(@Query("ssn") ssn: String,
                          @Query("hpid") hpid: String): Observable<RequestResponse>


    @GET("registerRequestApprovedRejected")
    fun sendConsent(@Query("requestId") requestId: String,
                    @Query("accepted") accepted: String): Observable<RequestResponse>

}