package com.example.parcial2.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.parcial2.R
import com.example.parcial2.database.AppDatabase
import com.example.parcial2.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(200.dp)
                .padding(32.dp)
        )

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {
                scope.launch {
                    try {
                        isLoading = true
                        error = ""

                        val database = AppDatabase.getDatabase(context)
                        val usuario = database.usuarioDao().login(username, password)

                        if (usuario != null) {
                            userViewModel.updateRol(usuario.rol)
                            navController.navigate("main_menu") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            error = "Usuario o contraseña incorrectos"
                        }
                    } catch (e: Exception) {
                        error = "Error al iniciar sesión: ${e.message}"
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && username.isNotBlank() && password.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Iniciar Sesión")
            }
        }

        // Botón de registro
        TextButton(
            onClick = { navController.navigate("registro") },
            enabled = !isLoading
        ) {
            Text("Registrar nuevo mesero")
        }
    }
}