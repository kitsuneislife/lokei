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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PricingAgendaScreen() {
    var availability by remember { mutableStateOf("Seg-Sex: 08h-20h") }
    var priceMode by remember { mutableStateOf("Manual") }
    var manualPrice by remember { mutableStateOf("250") }
    var dynamicRule by remember { mutableStateOf("+20% fim de semana") }
    var rentalPolicy by remember { mutableStateOf("Sem fumar, sem pets") }

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
                Text("Agenda e Precificação", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "Disponibilidade e regras (placeholders)",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = availability,
                    onValueChange = { availability = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Agenda / disponibilidade") }
                )

                Text("Modo de preço", style = MaterialTheme.typography.labelLarge)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = priceMode == "Manual",
                        onClick = { priceMode = "Manual" }
                    )
                    Text("Manual")
                    RadioButton(
                        selected = priceMode == "Dinâmico",
                        onClick = { priceMode = "Dinâmico" }
                    )
                    Text("Dinâmico")
                }

                OutlinedTextField(
                    value = manualPrice,
                    onValueChange = { manualPrice = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Preço manual") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = dynamicRule,
                    onValueChange = { dynamicRule = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Regra dinâmica") }
                )
                OutlinedTextField(
                    value = rentalPolicy,
                    onValueChange = { rentalPolicy = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Políticas de locação") }
                )
            }
        }
    }
}
