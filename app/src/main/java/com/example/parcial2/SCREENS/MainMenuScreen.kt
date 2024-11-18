package com.example.parcial2.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.parcial2.R
import com.example.parcial2.viewmodel.UserViewModel

@Composable
fun MainMenuScreen(
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            // Logo y Título
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo Restaurante",
                    modifier = Modifier
                        .size(180.dp) // Tamaño del logo aumentado
                        .padding(bottom = 16.dp)
                )
                Text(
                    text = "Restaurante HC",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Secciones en tarjetas
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NavigationCard(
                    iconId = R.drawable.mesa,
                    label = "Mesas",
                    description = "Gestiona las mesas disponibles",
                    onClick = { navController.navigate("mesa") }
                )

                if (userViewModel.isAdmin()) {
                    NavigationCard(
                        iconId = R.drawable.comida,
                        label = "Productos",
                        description = "Administra los productos del menú",
                        onClick = { navController.navigate("producto") }
                    )
                }

                NavigationCard(
                    iconId = R.drawable.pedidos,
                    label = "Pedidos",
                    description = "Visualiza los pedidos en curso",
                    onClick = { navController.navigate("pedido") }
                )
            }

            // Botón de cerrar sesión
            OutlinedButton(
                onClick = {
                    userViewModel.clearRol()
                    navController.navigate("login") {
                        popUpTo("main_menu") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Cerrar Sesión", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

// Composable para tarjetas de navegación
@Composable
fun NavigationCard(iconId: Int, label: String, description: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconId),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}