package com.levelupgamer.app.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.levelupgamer.levelup.MyApp
import com.levelupgamer.levelup.data.repository.UserRepository
import com.levelupgamer.levelup.model.User
import com.levelupgamer.levelup.util.UserManager

@Composable
fun ProfileScreen(navController: NavController, onLogout: () -> Unit) {
    val context = LocalContext.current
    val userRepository = remember { UserRepository((context.applicationContext as MyApp).database.userDao()) }
    var user by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(Unit) {
        val userId = UserManager.getLoggedInUserId(context)
        if (userId != null && userId != -1) { // No cargar para el admin
            user = userRepository.getUserById(userId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        user?.let {
            UserInfoCard(user = it)
            Spacer(Modifier.height(24.dp))
        }

        ProfileOption(navController, "myOrders", Icons.Default.ListAlt, "Mis Compras")
        ProfileOption(navController, "editProfile", Icons.Default.AccountCircle, "Editar Perfil")
        ProfileOption(navController, "address", Icons.Default.LocationOn, "Gestión de Dirección")

        Spacer(Modifier.weight(1f))

        Button(
            onClick = {
                // Limpiar SharedPreferences y navegar al login
                val sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)
                sharedPreferences.edit().clear().apply()
                onLogout()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar Sesión")
            Spacer(Modifier.width(8.dp))
            Text("Cerrar Sesión")
        }
    }
}

@Composable
fun UserInfoCard(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Hola, ${user.name}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Divider()
            Text("Puntos Disponibles: ${user.points}", style = MaterialTheme.typography.titleMedium)
            Text("Tu Código de Referido: ${user.referralCode}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileOption(
    navController: NavController,
    route: String,
    icon: ImageVector,
    text: String
) {
    Card(
        onClick = { navController.navigate(route) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = text)
            Spacer(Modifier.width(16.dp))
            Text(text, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
