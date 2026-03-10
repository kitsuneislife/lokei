package com.lukas.lokei.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lukas.lokei.auth.AuthViewModel
import com.lukas.lokei.mock.AdminBackofficeRepository

@Composable
fun AdminModerationScreen(authViewModel: AuthViewModel, onBack: () -> Unit) {
    val currentUser by authViewModel.currentUser.collectAsState()
    if (currentUser?.role != "ADMIN") {
        AdminAccessDenied(onBack = onBack)
        return
    }

    val cases = AdminBackofficeRepository.moderationCases()
    val localStatus = remember { mutableStateMapOf<String, String>() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Painel de Moderação", style = MaterialTheme.typography.headlineSmall)
                TextButton(onClick = onBack) { Text("Voltar") }
            }
        }

        items(cases) { moderation ->
            val status = localStatus[moderation.accountEmail] ?: moderation.status
            ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(moderation.name, style = MaterialTheme.typography.titleMedium)
                    Text("Conta: ${moderation.accountEmail}")
                    Text("Perfil: ${moderation.role}")
                    Text("Documentos validados: ${if (moderation.isValidated) "Sim" else "Não"}")
                    Text("Status: $status")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { localStatus[moderation.accountEmail] = "Aprovado" },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Aprovar")
                        }
                        Button(
                            onClick = { localStatus[moderation.accountEmail] = "Bloqueado" },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Bloquear")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminAccessDenied(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Acesso restrito", style = MaterialTheme.typography.headlineSmall)
        Text("Somente administradores podem moderar contas e conteúdo.")
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Voltar")
        }
    }
}
