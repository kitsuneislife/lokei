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
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material.icons.outlined.Security
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
fun AdminDisputesScreen(authViewModel: AuthViewModel, onBack: () -> Unit) {
    val currentUser by authViewModel.currentUser.collectAsState()
    if (currentUser?.role != "ADMIN") {
        DisputesAccessDenied(onBack = onBack)
        return
    }

    val disputes = AdminBackofficeRepository.disputeCases()
    val payoutStatus = remember { mutableStateMapOf<String, String>() }

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
                Icon(Icons.Outlined.Gavel, contentDescription = null, modifier = Modifier.size(28.dp))
                Text(
                    "Central de Disputas",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        items(disputes) { dispute ->
            val status = payoutStatus[dispute.id] ?: dispute.status
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Outlined.Report, contentDescription = null)
                            Text("Disputa ${dispute.id}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        }
                        Text(
                            status,
                            color = when (status) {
                                "Fundo liberado" -> MaterialTheme.colorScheme.primary
                                "Disputa encerrada" -> MaterialTheme.colorScheme.error
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Outlined.Receipt, contentDescription = null, modifier = Modifier.size(18.dp))
                        Text("Reserva: ${dispute.reservationId}")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Outlined.Person, contentDescription = null, modifier = Modifier.size(18.dp))
                        Text("Solicitante: ${dispute.claimant}")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Outlined.Gavel, contentDescription = null, modifier = Modifier.size(18.dp))
                        Text("Motivo: ${dispute.reason}")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Outlined.AttachMoney, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text("Valor solicitado:")
                        }
                        Text(
                            "R$ ${"%.2f".format(dispute.requestedAmount)}",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { payoutStatus[dispute.id] = "Fundo liberado" },
                            modifier = Modifier.weight(1f).height(44.dp)
                        ) {
                            Icon(Icons.Outlined.AccountBalance, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.size(6.dp))
                            Text("Autorizar")
                        }
                        FilledTonalButton(
                            onClick = { payoutStatus[dispute.id] = "Disputa encerrada" },
                            modifier = Modifier.weight(1f).height(44.dp)
                        ) {
                            Icon(Icons.Outlined.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.size(6.dp))
                            Text("Encerrar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DisputesAccessDenied(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Outlined.Security, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.error)
        Text("Acesso Restrito", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Somente administradores podem mediar disputas e liberar fundo de garantia.")
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth().height(50.dp)) {
            Text("Voltar")
        }
    }
}
