package com.levelupgamer.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun EditProfileScreen(navController: NavController) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text("Cambiar Contraseña", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(value = currentPassword, onValueChange = { currentPassword = it }, label = { Text("Contraseña Actual") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = newPassword, onValueChange = { newPassword = it }, label = { Text("Nueva Contraseña") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = confirmNewPassword, onValueChange = { confirmNewPassword = it }, label = { Text("Confirmar Nueva Contraseña") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        
        if (message.isNotEmpty()) {
            Text(message, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(vertical = 8.dp))
        }

        Button(onClick = {
             if (newPassword.isNotEmpty()) {
                if (newPassword.length < 6) {
                    message = "La nueva contraseña debe tener al menos 6 caracteres"
                } else if (newPassword != confirmNewPassword) {
                    message = "Las nuevas contraseñas no coinciden"
                } else {
                    // Aquí iría la lógica para llamar al ViewModel y cambiar la contraseña en la BD
                    message = "Contraseña actualizada con éxito"
                }
            } else {
                 message = ""
            }
        }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Text("Actualizar Contraseña")
        }

        Divider(modifier = Modifier.padding(vertical = 24.dp))

        Text("Mis Direcciones", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(8.dp))
        Text("Aquí puedes añadir, editar o eliminar tus direcciones de envío.")
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("address") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gestionar Mis Direcciones")
        }
    }
}
