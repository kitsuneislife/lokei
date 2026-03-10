package com.lukas.lokei.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lukas.lokei.mock.ExploreVehicleRepository

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Meus Veículos",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            "${ownerVehicles.size} veículo(s) cadastrado(s)",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        ownerVehicles.forEach { vehicle ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = MaterialTheme.shapes.large
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.DirectionsCar,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(vehicle.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(vehicle.location, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("R$ ${"%.0f".format(vehicle.dailyPrice)}/dia", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Star, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                            Text(" ${vehicle.rating}", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    "Cadastrar Veículo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                OutlinedTextField(
                    value = plate, onValueChange = { plate = it },
                    modifier = Modifier.fillMaxWidth(), label = { Text("Placa") },
                    singleLine = true, shape = MaterialTheme.shapes.medium
                )
                OutlinedTextField(
                    value = model, onValueChange = { model = it },
                    modifier = Modifier.fillMaxWidth(), label = { Text("Modelo") },
                    singleLine = true, shape = MaterialTheme.shapes.medium
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = year, onValueChange = { year = it },
                        modifier = Modifier.weight(1f), label = { Text("Ano") },
                        singleLine = true, shape = MaterialTheme.shapes.medium
                    )
                    OutlinedTextField(
                        value = dailyRate, onValueChange = { dailyRate = it },
                        modifier = Modifier.weight(1f), label = { Text("Diária (R$)") },
                        leadingIcon = { Icon(Icons.Outlined.AttachMoney, contentDescription = null) },
                        singleLine = true, shape = MaterialTheme.shapes.medium
                    )
                }
                OutlinedTextField(
                    value = rentalPolicy, onValueChange = { rentalPolicy = it },
                    modifier = Modifier.fillMaxWidth(), label = { Text("Políticas de locação") },
                    shape = MaterialTheme.shapes.medium
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilledTonalButton(
                        onClick = { photosUploaded = !photosUploaded },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            if (photosUploaded) Icons.Outlined.CheckCircle else Icons.Outlined.CloudUpload,
                            contentDescription = null, modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (photosUploaded) "Fotos ✓" else "Fotos")
                    }
                    FilledTonalButton(
                        onClick = { crlvUploaded = !crlvUploaded },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            if (crlvUploaded) Icons.Outlined.CheckCircle else Icons.Outlined.CloudUpload,
                            contentDescription = null, modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (crlvUploaded) "CRLV ✓" else "CRLV")
                    }
                }
            }
        }
    }
}
