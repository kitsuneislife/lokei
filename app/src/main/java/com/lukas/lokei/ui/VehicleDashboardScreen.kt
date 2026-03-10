package com.lukas.lokei.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lukas.lokei.mock.ExploreVehicleRepository

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun VehicleDashboardScreen() {
    var plate by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var dailyRate by remember { mutableStateOf("") }
    var rentalPolicy by remember { mutableStateOf("") }
    var photosUploaded by remember { mutableStateOf(false) }
    var crlvUploaded by remember { mutableStateOf(false) }
    val ownerVehicles = ExploreVehicleRepository.forOwner("proprietario")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Dashboard de Veículos", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "Cadastro e edição (placeholders)",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = plate,
                    onValueChange = { plate = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Placa") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = model,
                    onValueChange = { model = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Modelo") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = year,
                    onValueChange = { year = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Ano") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = dailyRate,
                    onValueChange = { dailyRate = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Preço diária") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = rentalPolicy,
                    onValueChange = { rentalPolicy = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Políticas de locação") }
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = photosUploaded,
                        onClick = { photosUploaded = !photosUploaded },
                        label = {
                            Text(if (photosUploaded) "Fotos enviadas" else "Upload de fotos")
                        }
                    )
                    FilterChip(
                        selected = crlvUploaded,
                        onClick = { crlvUploaded = !crlvUploaded },
                        label = {
                            Text(if (crlvUploaded) "CRLV enviado" else "Upload de CRLV")
                        }
                    )
                }

                Text("Veículos mock do proprietário (proprietario)")
                ownerVehicles.forEach { vehicle ->
                    Text("• ${vehicle.title} — ${vehicle.location} — R$ ${vehicle.dailyPrice}/dia")
                }
            }
        }
    }
}
