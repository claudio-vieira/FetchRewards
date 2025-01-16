package com.example.fetchrewards.interfaces

import com.example.fetchrewards.data.Reward
import retrofit2.http.GET

interface ApiService {
    @GET("hiring.json")
    suspend fun getRewards(): List<Reward>
}