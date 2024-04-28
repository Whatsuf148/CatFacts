package com.example.catfacts.data

import com.example.catfacts.model.CatFacts
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET("/fact")
    suspend fun getRandomFacts() : Response<CatFacts>
}