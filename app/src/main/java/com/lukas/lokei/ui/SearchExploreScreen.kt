package com.lukas.lokei.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lukas.lokei.mock.ExploreVehicleRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchExploreScreen(initialQuery: String = "", onOpenDetails: (Int) -> Unit) {
    var query by remember { mutableStateOf(initialQuery) }
    var selectedType by remember { mutableStateOf("Todos") }
    var maxPrice by remember { mutableStateOf(400f) }
    var minRating by remember { mutableStateOf(4.0f) }
    var location by remember { mutableStateOf("") }

    val typeOptions = listOf("Todos", "SUV", "Hatch", "Sedan")

    val filteredVehicles = remember(query, selectedType, maxPrice, minRating, location) {
        ExploreVehicleRepository.all().filter { vehicle ->
            val queryMatch = query.isBlank() || vehicle.title.contains(query, ignoreCase = true)
            val typeMatch = selectedType == "Todos" || vehicle.type == selectedType
            val priceMatch = vehicle.dailyPrice <= maxPrice.toDouble()
            val ratingMatch = vehicle.rating >= minRating.toDouble()
            val locationMatch = location.isBlank() || vehicle.location.contains(location, ignoreCase = true)
            queryMatch && typeMatch && priceMatch && ratingMatch && locationMatch
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "Buscar veículos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            OutlinedTextField(
                value = query, onValueChange = { query = it },
                label = { Text("Pesquisar por nome") },
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(), singleLine = true,
                shape = MaterialTheme.shapes.medium
            )
        }

        item {
            OutlinedTextField(
                value = location, onValueChange = { location = it },
                label = { Text("Localização") },
                leadingIcon = { Icon(Icons.Outlined.LocationOn, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(), singleLine = true,
                shape = MaterialTheme.shapes.medium
            )
        }

        item {
            Text("Tipo", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
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
            Slider(value = maxPrice, onValueChange = { maxPrice = it }, valueRange = 100f..500f)
        }

        item {
            Text("Avaliação mínima: ${"%.1f".format(minRating)}", style = MaterialTheme.typography.labelLarge)
            Slider(value = minRating, onValueChange = { minRating = it }, valueRange = 3.5f..5f)
        }

        item {
            Text(
                "${filteredVehicles.size} veículo(s) encontrado(s)",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (filteredVehicles.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = MaterialTheme.shapes.large
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Outlined.DirectionsCar, contentDescription = null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("Nenhum veículo encontrado", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        items(filteredVehicles) { vehicle ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenDetails(vehicle.id) },
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
                        Text("${vehicle.type} · ${vehicle.location}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("R$ ${"%.0f".format(vehicle.dailyPrice)}", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Star, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                            Text(" ${vehicle.rating}", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
    }
}
