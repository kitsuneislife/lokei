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
import com.lukas.lokei.mock.ChatRepository

@Composable
fun InternalChatScreen(authViewModel: AuthViewModel, onBack: () -> Unit) {
    val currentUser = authViewModel.currentUser.value ?: return
    val threads = ChatRepository.threadsForUser(currentUser.email)
    val selectedThread = threads.firstOrNull()
    var messageText by remember { mutableStateOf("") }
    var sentMessages by remember(selectedThread?.id) { mutableStateOf(emptyList<String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Chat Interno", style = MaterialTheme.typography.headlineSmall)
            TextButton(onClick = onBack) { Text("Voltar") }
        }

        if (selectedThread == null) {
            Text("Nenhuma conversa disponivel no momento.")
            return@Column
        }

        ElevatedCard(
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(selectedThread.title, style = MaterialTheme.typography.titleMedium)
                Text("Troca segura de mensagens e arquivos entre locatario e proprietario")
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(selectedThread.messages) { message ->
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text("${message.senderEmail} • ${message.timestamp}")
                        Text(message.text)
                        message.attachments.forEach { attachment ->
                            Text("Anexo: ${attachment.name} (${attachment.type})")
                        }
                    }
                }
            }

            items(sentMessages) { localMessage ->
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text("${currentUser.email} • agora")
                        Text(localMessage)
                    }
                }
            }
        }

        OutlinedTextField(
            value = messageText,
            onValueChange = { messageText = it },
            label = { Text("Digite uma mensagem") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    sentMessages = sentMessages + "[Arquivo] comprovante_pagamento.pdf"
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Enviar arquivo")
            }
            Button(
                onClick = {
                    val normalized = messageText.trim()
                    if (normalized.isNotEmpty()) {
                        sentMessages = sentMessages + normalized
                        messageText = ""
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Enviar")
            }
        }
    }
}
