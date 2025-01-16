package com.example.fetchrewards.repository

import com.example.fetchrewards.data.Reward
import com.example.fetchrewards.interfaces.ApiService
import javax.inject.Inject

class RewardRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getRewards(): List<Reward> {
        return apiService.getRewards()
    }
}