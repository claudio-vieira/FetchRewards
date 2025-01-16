package com.example.fetchrewards.ui.reward

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fetchrewards.data.Reward
import com.example.fetchrewards.repository.RewardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RewardViewModel @Inject constructor(
    private val repository: RewardRepository
) : ViewModel() {

    private val _rewards = MutableStateFlow<List<Reward>>(emptyList())
    val rewardsGroupedByListId = _rewards.map { items ->
        items.groupBy { it.listId }
    }

    fun fetchRewards() {
        viewModelScope.launch {
            try {
                val data = repository.getRewards()
                _rewards.value = data
                    .filter { it.name?.isNotEmpty() == true }
                    .sortedWith(compareBy<Reward> { it.listId }
                    .thenBy { it.id })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}