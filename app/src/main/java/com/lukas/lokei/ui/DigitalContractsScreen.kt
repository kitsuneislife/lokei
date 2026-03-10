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
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Painel de Contratos", style = MaterialTheme.typography.headlineSmall)
                TextButton(onClick = onBack) { Text("Voltar") }
            }
        }

        items(contracts) { contract ->
            val isSigned = signedMap[contract.id] == true
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(contract.title, style = MaterialTheme.typography.titleMedium)
                    Text("ID: ${contract.id}")
                    Text("Periodo: ${contract.period}")
                    Text("Valor: ${contract.amount}")
                    Text("Status: ${contract.status}")
                    Text(
                        if (isSigned) "Assinatura digital: concluida" else "Assinatura digital: pendente",
                        color = if (isSigned) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Button(
                        onClick = { signedMap[contract.id] = true },
                        enabled = !isSigned,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (isSigned) "Contrato assinado" else "Assinar digitalmente")
                    }
                }
            }
        }
    }
}
