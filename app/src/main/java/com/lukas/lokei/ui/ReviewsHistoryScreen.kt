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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lukas.lokei.auth.AuthViewModel
import com.lukas.lokei.mock.ReviewHistoryRepository

@Composable
fun ReviewsHistoryScreen(authViewModel: AuthViewModel, onBack: () -> Unit) {
    val currentUser = authViewModel.currentUser.value ?: return
    val history = ReviewHistoryRepository.historyForUser(currentUser.email)

    var selectedTarget by remember(history.pendingMutual) {
        mutableStateOf(history.pendingMutual.firstOrNull()?.targetUser.orEmpty())
    }
    var score by remember { mutableFloatStateOf(5f) }
    var comment by remember { mutableStateOf("") }
    var sent by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Avaliacoes e Historico", style = MaterialTheme.typography.headlineSmall)
            TextButton(onClick = onBack) { Text("Voltar") }
        }

        ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Media recebida: ${history.averageScore}", style = MaterialTheme.typography.titleMedium)
                Text("Total de avaliacoes: ${history.received.size}")
            }
        }

        Text("Avaliacoes recebidas", style = MaterialTheme.typography.titleMedium)
        history.received.forEach { review ->
            ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("${review.author} • ${review.score}")
                    Text(review.comment)
                }
            }
        }

        if (history.pendingMutual.isNotEmpty()) {
            Text("Formulario de avaliacao mutua", style = MaterialTheme.typography.titleMedium)
            ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Destino: $selectedTarget")
                    Text("Pontuacao: ${"%.1f".format(score)}")
                    Slider(value = score, onValueChange = { score = it }, valueRange = 1f..5f)
                    OutlinedTextField(
                        value = comment,
                        onValueChange = { comment = it },
                        label = { Text("Comentario") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            if (comment.trim().isNotEmpty()) {
                                sent = true
                                comment = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Enviar avaliacao")
                    }
                    if (sent) {
                        Text("Avaliacao enviada com sucesso.", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}
