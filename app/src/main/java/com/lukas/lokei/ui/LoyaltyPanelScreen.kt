package com.lukas.lokei.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Loyalty
import androidx.compose.material.icons.outlined.Redeem
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lukas.lokei.auth.AuthViewModel
import com.lukas.lokei.mock.LoyaltyRepository

@Composable
fun LoyaltyPanelScreen(authViewModel: AuthViewModel, onBack: () -> Unit) {
    val currentUser = authViewModel.currentUser.value ?: return
    val summary = LoyaltyRepository.summaryForUser(currentUser.email)
    var message by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
            }
            Icon(Icons.Outlined.Loyalty, contentDescription = null, modifier = Modifier.size(28.dp))
            Text(
                "Painel de Fidelidade",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Outlined.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text(
                        "${summary.balance}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text("Pontos", style = MaterialTheme.typography.bodySmall)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Outlined.EmojiEvents, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text(
                        summary.tier,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Nível", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Outlined.History, contentDescription = null, modifier = Modifier.size(20.dp))
            Text("Histórico", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }
        summary.history.forEach { entry ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.weight(1f)) {
                        Text(entry.description, fontWeight = FontWeight.SemiBold)
                        Text(entry.date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(
                        text = if (entry.points >= 0) "+${entry.points}" else entry.points.toString(),
                        color = if (entry.points >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Outlined.CardGiftcard, contentDescription = null, modifier = Modifier.size(20.dp))
            Text("Resgates Disponíveis", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }
        summary.rewards.forEach { reward ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Outlined.Redeem, contentDescription = null, modifier = Modifier.size(20.dp))
                        Text(reward.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    }
                    Text("Custo: ${reward.cost} pontos", style = MaterialTheme.typography.bodyMedium)
                    Button(
                        onClick = {
                            message = if (summary.balance >= reward.cost) {
                                "Resgate solicitado: ${reward.title}"
                            } else {
                                "Pontos insuficientes para ${reward.title}"
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(44.dp)
                    ) {
                        Text("Solicitar Resgate")
                    }
                }
            }
        }

        if (message != null) {
            Text(message.orEmpty(), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}
