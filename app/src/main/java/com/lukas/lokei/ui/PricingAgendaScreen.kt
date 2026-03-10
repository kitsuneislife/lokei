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
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PricingAgendaScreen() {
    var availability by remember { mutableStateOf("Seg-Sex: 08h-20h") }
    var priceMode by remember { mutableStateOf("Manual") }
    var manualPrice by remember { mutableStateOf("250") }
    var dynamicRule by remember { mutableStateOf("+20% fim de semana") }
    var rentalPolicy by remember { mutableStateOf("Sem fumar, sem pets") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Agenda e Precificação",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Configure disponibilidade e preços",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.CalendarMonth, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Disponibilidade", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                }
                OutlinedTextField(
                    value = availability, onValueChange = { availability = it },
                    modifier = Modifier.fillMaxWidth(), label = { Text("Agenda") },
                    shape = MaterialTheme.shapes.medium
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.AttachMoney, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Precificação", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = priceMode == "Manual", onClick = { priceMode = "Manual" })
                    Text("Manual", modifier = Modifier.weight(1f))
                    RadioButton(selected = priceMode == "Dinâmico", onClick = { priceMode = "Dinâmico" })
                    Text("Dinâmico")
                }

                if (priceMode == "Manual") {
                    OutlinedTextField(
                        value = manualPrice, onValueChange = { manualPrice = it },
                        modifier = Modifier.fillMaxWidth(), label = { Text("Preço diária (R$)") },
                        leadingIcon = { Icon(Icons.Outlined.AttachMoney, contentDescription = null) },
                        singleLine = true, shape = MaterialTheme.shapes.medium
                    )
                } else {
                    OutlinedTextField(
                        value = dynamicRule, onValueChange = { dynamicRule = it },
                        modifier = Modifier.fillMaxWidth(), label = { Text("Regra dinâmica") },
                        leadingIcon = { Icon(Icons.AutoMirrored.Outlined.TrendingUp, contentDescription = null) },
                        shape = MaterialTheme.shapes.medium
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Policy, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Políticas", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                }
                OutlinedTextField(
                    value = rentalPolicy, onValueChange = { rentalPolicy = it },
                    modifier = Modifier.fillMaxWidth(), label = { Text("Políticas de locação") },
                    shape = MaterialTheme.shapes.medium
                )
            }
        }
    }
}
