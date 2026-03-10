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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.material.icons.automirrored.outlined.Subject
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
            }
            Icon(Icons.Outlined.SupportAgent, contentDescription = null, modifier = Modifier.size(28.dp))
            Text(
                "Central de Ajuda",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Outlined.QuestionAnswer, contentDescription = null, modifier = Modifier.size(20.dp))
            Text("Perguntas Frequentes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }
        data.faq.forEach { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.AutoMirrored.Outlined.HelpOutline, contentDescription = null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
                        Text(item.question, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    }
                    Text(item.answer, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Outlined.ConfirmationNumber, contentDescription = null, modifier = Modifier.size(20.dp))
            Text("Abrir Chamado", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Assunto") },
                    leadingIcon = { Icon(Icons.AutoMirrored.Outlined.Subject, contentDescription = null) },
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
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Enviar Chamado")
                }
                if (generatedTicket != null) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Outlined.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
                        Text("Chamado criado: $generatedTicket", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Outlined.ConfirmationNumber, contentDescription = null, modifier = Modifier.size(20.dp))
            Text("Seus Tickets", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }
        data.tickets.forEach { ticket ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(ticket.id, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        Text(ticket.status, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodySmall)
                    }
                    Text(ticket.subject, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}
