package com.lukas.lokei.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Painel de Fidelidade", style = MaterialTheme.typography.headlineSmall)
            TextButton(onClick = onBack) { Text("Voltar") }
        }

        ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Saldo de pontos: ${summary.balance}", style = MaterialTheme.typography.titleMedium)
                Text("Nivel atual: ${summary.tier}")
            }
        }

        Text("Historico", style = MaterialTheme.typography.titleMedium)
        summary.history.forEach { entry ->
            ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(entry.description)
                        Text(entry.date, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(
                        text = if (entry.points >= 0) "+${entry.points}" else entry.points.toString(),
                        color = if (entry.points >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Text("Resgates", style = MaterialTheme.typography.titleMedium)
        summary.rewards.forEach { reward ->
            ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(reward.title, style = MaterialTheme.typography.titleSmall)
                    Text("Custo: ${reward.cost} pontos")
                    Button(
                        onClick = {
                            message = if (summary.balance >= reward.cost) {
                                "Resgate solicitado: ${reward.title}"
                            } else {
                                "Pontos insuficientes para ${reward.title}"
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Solicitar resgate")
                    }
                }
            }
        }

        if (message != null) {
            Text(message.orEmpty(), color = MaterialTheme.colorScheme.primary)
        }
    }
}
