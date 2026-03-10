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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                }
                Icon(Icons.Outlined.Shield, contentDescription = null, modifier = Modifier.size(28.dp))
                Text(
                    "Painel de Moderação",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        items(cases) { moderation ->
            val status = localStatus[moderation.accountEmail] ?: moderation.status
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Outlined.Person, contentDescription = null)
                        Text(moderation.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Outlined.Email, contentDescription = null, modifier = Modifier.size(18.dp))
                        Text(moderation.accountEmail, style = MaterialTheme.typography.bodyMedium)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Outlined.Security, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text("Perfil: ${moderation.role}")
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(
                                if (moderation.isValidated) Icons.Outlined.VerifiedUser else Icons.Outlined.Block,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (moderation.isValidated) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                            )
                            Text(
                                if (moderation.isValidated) "Validado" else "Pendente",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (moderation.isValidated) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    Text(
                        "Status: $status",
                        fontWeight = FontWeight.SemiBold,
                        color = when (status) {
                            "Aprovado" -> MaterialTheme.colorScheme.primary
                            "Bloqueado" -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.onSurface
                        }
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { localStatus[moderation.accountEmail] = "Aprovado" },
                            modifier = Modifier.weight(1f).height(44.dp)
                        ) {
                            Icon(Icons.Outlined.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.size(6.dp))
                            Text("Aprovar")
                        }
                        FilledTonalButton(
                            onClick = { localStatus[moderation.accountEmail] = "Bloqueado" },
                            modifier = Modifier.weight(1f).height(44.dp)
                        ) {
                            Icon(Icons.Outlined.Block, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.size(6.dp))
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
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Outlined.Security, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.error)
        Text("Acesso Restrito", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Somente administradores podem moderar contas e conteúdo.")
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth().height(50.dp)) {
            Text("Voltar")
        }
    }
}
