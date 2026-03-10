package com.lukas.lokei.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lukas.lokei.auth.AuthViewModel
import com.lukas.lokei.mock.SupportRepository

@Composable
fun HelpSupportScreen(authViewModel: AuthViewModel, onBack: () -> Unit) {
    val currentUser = authViewModel.currentUser.value ?: return
    val data = SupportRepository.dataForUser(currentUser.email)

    var subject by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var generatedTicket by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Central de Ajuda e Suporte", style = MaterialTheme.typography.headlineSmall)
            TextButton(onClick = onBack) { Text("Voltar") }
        }

        Text("FAQ", style = MaterialTheme.typography.titleMedium)
        data.faq.forEach { item ->
            ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(item.question, style = MaterialTheme.typography.titleSmall)
                    Text(item.answer)
                }
            }
        }

        Text("Abrir chamado", style = MaterialTheme.typography.titleMedium)
        ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Assunto") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = details,
                    onValueChange = { details = it },
                    label = { Text("Descreva o problema") },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        if (subject.trim().isNotEmpty() && details.trim().isNotEmpty()) {
                            generatedTicket = SupportRepository.nextTicketId(data.tickets.size)
                            subject = ""
                            details = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Enviar chamado")
                }
                if (generatedTicket != null) {
                    Text("Chamado criado: $generatedTicket", color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        Text("Tickets", style = MaterialTheme.typography.titleMedium)
        data.tickets.forEach { ticket ->
            ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(ticket.id, style = MaterialTheme.typography.titleSmall)
                    Text(ticket.subject)
                    Text("Status: ${ticket.status}")
                }
            }
        }
    }
}
