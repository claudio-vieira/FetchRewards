package com.example.fetchrewards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fetchrewards.data.Reward
import com.example.fetchrewards.ui.reward.RewardViewModel
import com.example.fetchrewards.ui.theme.FetchRewardsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FetchRewardsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RewardListScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun RewardListScreen(
    modifier: Modifier = Modifier,
    viewModel: RewardViewModel = hiltViewModel()
) {
    val rewards by viewModel.rewardsGroupedByListId.collectAsState(initial = emptyMap())
    val expandedGroups = remember { mutableStateMapOf<Int, Boolean>() }

    LaunchedEffect(Unit) {
        viewModel.fetchRewards()
    }

    if (rewards.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            rewards.forEach { (listId, rewards) ->
                item {
                    GroupHeader(
                        listId = listId,
                        isExpanded = expandedGroups[listId] == true,
                        onToggle = {
                            expandedGroups[listId] = !(expandedGroups[listId] ?: false)
                        }
                    )
                }
                if (expandedGroups[listId] == true) {
                    items(rewards) { reward ->
                        RewardCard(reward)
                    }
                }
            }
        }
    }
}

@Composable
fun GroupHeader(
    listId: Int,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "List ID: $listId",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = if (isExpanded) "Collapse" else "Expand"
        )
    }
}

@Composable
fun RewardCard(item: Reward) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "ID: ${item.id}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "List ID: ${item.listId}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Name: ${item.name ?: "Unnamed"}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}