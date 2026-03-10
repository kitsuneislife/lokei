package com.lukas.lokei.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lukas.lokei.auth.AuthViewModel
import com.lukas.lokei.ui.components.AppToastType
import com.lukas.lokei.ui.components.ShowStandardToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    val error by authViewModel.errorMessage.collectAsState()
    val success by authViewModel.successMessage.collectAsState()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var cnh by remember { mutableStateOf("") }
    var uploadedCpf by remember { mutableStateOf(false) }
    var uploadedCnh by remember { mutableStateOf(false) }
    var documentsValidated by remember { mutableStateOf(false) }

    ShowStandardToast(
        message = error,
        type = AppToastType.ERROR,
        onConsumed = authViewModel::clearError
    )
    ShowStandardToast(
        message = success,
        type = AppToastType.SUCCESS,
        onConsumed = authViewModel::clearSuccess
    )

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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Criar conta", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "Preencha os dados para se cadastrar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Senha") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = cpf,
                    onValueChange = { cpf = it },
                    label = { Text("CPF") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = cnh,
                    onValueChange = { cnh = it },
                    label = { Text("CNH") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    "Envio de documentos (placeholder)",
                    style = MaterialTheme.typography.labelLarge
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = uploadedCpf,
                        onClick = {
                            uploadedCpf = true
                            authViewModel.clearError()
                            authViewModel.clearSuccess()
                        },
                        label = { Text(if (uploadedCpf) "CPF enviado" else "Enviar CPF") }
                    )
                    FilterChip(
                        selected = uploadedCnh,
                        onClick = {
                            uploadedCnh = true
                            authViewModel.clearError()
                            authViewModel.clearSuccess()
                        },
                        label = { Text(if (uploadedCnh) "CNH enviada" else "Enviar CNH") }
                    )
                }

                AssistChip(
                    onClick = {
                        documentsValidated = uploadedCpf && uploadedCnh
                    },
                    label = {
                        Text(
                            if (documentsValidated) {
                                "Documentos autenticados"
                            } else {
                                "Autenticar documentos (simulado)"
                            }
                        )
                    }
                )

                if (!documentsValidated && (uploadedCpf || uploadedCnh)) {
                    Text(
                        "Envie CPF e CNH para autenticar.",
                        color = Color(0xFFFFB74D),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Button(
                    onClick = {
                        authViewModel.register(
                            name = name.trim(),
                            email = email.trim(),
                            password = password,
                            cpf = cpf.trim(),
                            cnh = cnh.trim(),
                            documentsValidated = documentsValidated
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cadastrar")
                }

                TextButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Já tenho conta")
                }
            }
        }
    }
}
