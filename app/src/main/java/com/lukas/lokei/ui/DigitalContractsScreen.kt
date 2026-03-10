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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Draw
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
import com.lukas.lokei.mock.ContractsRepository

@Composable
fun DigitalContractsScreen(
    authViewModel: AuthViewModel,
    vehicleId: Int,
    rentalDays: Int,
    onBack: () -> Unit
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val contracts = ContractsRepository.listForUser(
        email = currentUser?.email.orEmpty(),
        activeVehicleId = vehicleId,
        rentalDays = rentalDays
    )
    val signedMap = remember { mutableStateMapOf<String, Boolean>() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Voltar")
                }
                Text(
                    "Contratos",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        items(contracts) { contract ->
            val isSigned = signedMap[contract.id] == true
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.Description,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(contract.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Período", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(contract.period, style = MaterialTheme.typography.bodySmall)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Valor", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(contract.amount, style = MaterialTheme.typography.bodySmall)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Status", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(contract.status, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            if (isSigned) Icons.Outlined.CheckCircle else Icons.Outlined.Draw,
                            contentDescription = null,
                            tint = if (isSigned) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            if (isSigned) "Assinado digitalmente" else "Pendente de assinatura",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isSigned) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (!isSigned) {
                        Button(
                            onClick = { signedMap[contract.id] = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Icon(Icons.Outlined.Draw, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Assinar contrato")
                        }
                    }
                }
            }
        }
    }
}
