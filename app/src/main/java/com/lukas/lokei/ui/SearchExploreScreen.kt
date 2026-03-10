package com.lukas.lokei.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchExploreScreen(onOpenDetails: (Int) -> Unit) {
    var selectedType by remember { mutableStateOf("Todos") }
    var maxPrice by remember { mutableStateOf(400f) }
    var minRating by remember { mutableStateOf(4.0f) }
    var location by remember { mutableStateOf("") }

    val typeOptions = listOf("Todos", "SUV", "Hatch", "Sedan")

    val filteredVehicles = remember(selectedType, maxPrice, minRating, location) {
        ExploreVehicleRepository.all().filter { vehicle ->
            val typeMatch = selectedType == "Todos" || vehicle.type == selectedType
            val priceMatch = vehicle.dailyPrice <= maxPrice.toDouble()
            val ratingMatch = vehicle.rating >= minRating.toDouble()
            val locationMatch = location.isBlank() || vehicle.location.contains(location, ignoreCase = true)
            typeMatch && priceMatch && ratingMatch && locationMatch
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Busca e Exploração", style = MaterialTheme.typography.headlineSmall)
            Text(
                "Mapa + veículos próximos com filtros",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text("Mapa (placeholder)", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        item {
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Localização") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        item {
            Text("Tipo", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                typeOptions.forEach { type ->
                    FilterChip(
                        selected = selectedType == type,
                        onClick = { selectedType = type },
                        label = { Text(type) }
                    )
                }
            }
        }

        item {
            Text("Preço máximo: R$ ${maxPrice.toInt()}", style = MaterialTheme.typography.labelLarge)
            Slider(
                value = maxPrice,
                onValueChange = { maxPrice = it },
                valueRange = 100f..500f
            )
        }

        item {
            Text("Avaliação mínima: ${"%.1f".format(minRating)}", style = MaterialTheme.typography.labelLarge)
            Slider(
                value = minRating,
                onValueChange = { minRating = it },
                valueRange = 3.5f..5f
            )
        }

        item {
            AssistChip(
                onClick = { },
                label = { Text("${filteredVehicles.size} veículo(s) encontrado(s)") }
            )
        }

        items(filteredVehicles) { vehicle ->
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenDetails(vehicle.id) },
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(vehicle.title, style = MaterialTheme.typography.titleMedium)
                    Text("${vehicle.type} • ${vehicle.location}")
                    Text("R$ ${vehicle.dailyPrice}/dia • ⭐ ${vehicle.rating}")
                    Text("Toque para ver detalhes", color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}
