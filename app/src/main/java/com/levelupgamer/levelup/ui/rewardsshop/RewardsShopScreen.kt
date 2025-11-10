package com.levelupgamer.levelup.ui.rewardsshop

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.levelupgamer.levelup.model.Reward
import com.levelupgamer.levelup.ui.viewmodel.ViewModelFactory

@Composable
fun RewardsShopScreen() {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: RewardsShopViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Tienda de Recompensas", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(8.dp))
        Text("Tus Puntos: ${uiState.userPoints}", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(16.dp))

        if (uiState.rewards.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay recompensas disponibles en este momento.")
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(uiState.rewards, key = { it.id }) { reward ->
                    RewardShopItem(
                        reward = reward,
                        userPoints = uiState.userPoints,
                        onPurchase = { viewModel.purchaseReward(reward) }
                    )
                }
            }
        }
    }
}

@Composable
fun RewardShopItem(reward: Reward, userPoints: Int, onPurchase: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(reward.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(reward.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(12.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("${reward.pointsCost} Puntos", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                reward.stock?.let {
                    Text("Quedan: $it", style = MaterialTheme.typography.bodySmall)
                }
            }
            
            Spacer(Modifier.height(8.dp))

            Button(
                onClick = onPurchase,
                enabled = userPoints >= reward.pointsCost && (reward.stock == null || reward.stock > 0),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Canjear")
            }
        }
    }
}
