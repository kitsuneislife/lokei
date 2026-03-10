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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
            Icon(Icons.Outlined.StarRate, contentDescription = null, modifier = Modifier.size(28.dp))
            Text(
                "Avaliações e Histórico",
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
                        "${history.averageScore}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text("Média", style = MaterialTheme.typography.bodySmall)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Outlined.Person, contentDescription = null)
                    Text(
                        "${history.received.size}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Avaliações", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Outlined.Star, contentDescription = null, modifier = Modifier.size(20.dp))
            Text("Avaliações Recebidas", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }
        history.received.forEach { review ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Outlined.Person, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text(review.author, fontWeight = FontWeight.SemiBold)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Outlined.Star, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                            Text("${review.score}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        }
                    }
                    Text(review.comment, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        if (history.pendingMutual.isNotEmpty()) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Outlined.Edit, contentDescription = null, modifier = Modifier.size(20.dp))
                Text("Avaliação Mútua", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Outlined.Person, contentDescription = null, modifier = Modifier.size(18.dp))
                        Text("Destino: $selectedTarget", fontWeight = FontWeight.SemiBold)
                    }
                    Text("Pontuação: ${"%.1f".format(score)}")
                    Slider(value = score, onValueChange = { score = it }, valueRange = 1f..5f)
                    OutlinedTextField(
                        value = comment,
                        onValueChange = { comment = it },
                        label = { Text("Comentário") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            if (comment.trim().isNotEmpty()) {
                                sent = true
                                comment = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.size(8.dp))
                        Text("Enviar Avaliação")
                    }
                    if (sent) {
                        Text("Avaliação enviada com sucesso.", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}
