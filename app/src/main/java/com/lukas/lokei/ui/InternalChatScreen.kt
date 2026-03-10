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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
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
import com.lukas.lokei.mock.ChatRepository

@Composable
fun InternalChatScreen(authViewModel: AuthViewModel, onBack: () -> Unit) {
    val currentUser = authViewModel.currentUser.value ?: return
    val threads = ChatRepository.threadsForUser(currentUser.email)
    val selectedThread = threads.firstOrNull()
    var messageText by remember { mutableStateOf("") }
    var sentMessages by remember(selectedThread?.id) { mutableStateOf(emptyList<String>()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Voltar")
            }
            Column {
                Text(
                    selectedThread?.title ?: "Chat",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (selectedThread != null) {
                    Text(
                        "${selectedThread.participantEmails.size} participantes",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        if (selectedThread == null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = MaterialTheme.shapes.large
            ) {
                Text(
                    "Nenhuma conversa disponível",
                    modifier = Modifier.padding(24.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            return@Column
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(selectedThread.messages) { message ->
                val isMe = message.senderEmail == currentUser.email
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isMe) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            if (!isMe) {
                                Text(message.senderEmail, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                            }
                            Text(message.text, style = MaterialTheme.typography.bodyMedium)
                            message.attachments.forEach { att ->
                                Text("📎 ${att.name}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                            }
                            Text(message.timestamp, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            items(sentMessages) { localMessage ->
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(localMessage, style = MaterialTheme.typography.bodyMedium)
                            Text("agora", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilledTonalIconButton(
                onClick = { sentMessages = sentMessages + "📎 comprovante_pagamento.pdf" }
            ) {
                Icon(Icons.Outlined.AttachFile, contentDescription = "Anexar arquivo", modifier = Modifier.size(20.dp))
            }
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = { Text("Mensagem...") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )
            FilledTonalIconButton(
                onClick = {
                    val normalized = messageText.trim()
                    if (normalized.isNotEmpty()) {
                        sentMessages = sentMessages + normalized
                        messageText = ""
                    }
                }
            ) {
                Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = "Enviar", modifier = Modifier.size(20.dp))
            }
        }
    }
}
