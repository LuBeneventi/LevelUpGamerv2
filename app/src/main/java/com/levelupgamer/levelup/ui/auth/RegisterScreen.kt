package com.levelupgamer.levelup.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.levelupgamer.levelup.ui.components.visual_transformation.DateVisualTransformation
import com.levelupgamer.levelup.ui.components.visual_transformation.RutVisualTransformation
import com.levelupgamer.levelup.ui.viewmodel.ViewModelFactory

@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: RegisterViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()

    // Navegación cuando el registro es exitoso
    LaunchedEffect(uiState.registrationSuccess) {
        if (uiState.registrationSuccess) {
            // Podríamos mostrar un Toast o Snackbar de éxito aquí
            navController.navigate("login") { popUpTo("login") { inclusive = true } }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Crear Cuenta", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(value = uiState.name, onValueChange = viewModel::onNameChange, label = { Text("Nombre Completo") }, modifier = Modifier.fillMaxWidth(), isError = uiState.error != null)
            OutlinedTextField(value = uiState.rut, onValueChange = viewModel::onRutChange, label = { Text("RUT") }, visualTransformation = RutVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth(), isError = uiState.error != null)
            OutlinedTextField(value = uiState.birthDate, onValueChange = viewModel::onBirthDateChange, label = { Text("Fecha Nacimiento (ddmmyyyy)") }, visualTransformation = DateVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth(), isError = uiState.error != null)
            OutlinedTextField(value = uiState.phone, onValueChange = viewModel::onPhoneChange, label = { Text("Teléfono") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth(), isError = uiState.error != null)
            OutlinedTextField(value = uiState.email, onValueChange = viewModel::onEmailChange, label = { Text("Email") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth(), isError = uiState.error != null)
            OutlinedTextField(value = uiState.password, onValueChange = viewModel::onPasswordChange, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(), isError = uiState.error != null)
            OutlinedTextField(value = uiState.confirmPassword, onValueChange = viewModel::onConfirmPasswordChange, label = { Text("Confirmar Contraseña") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(), isError = uiState.error != null)
            OutlinedTextField(value = uiState.referralCode, onValueChange = viewModel::onReferralCodeChange, label = { Text("Código de Referido (Opcional)") }, modifier = Modifier.fillMaxWidth())

            uiState.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(vertical = 8.dp))
            }

            Button(
                onClick = viewModel::onRegisterClick,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text("Crear Cuenta")
            }

            TextButton(onClick = { navController.popBackStack() }) {
                 Text("¿Ya tienes cuenta? Inicia Sesión")
            }
        }
    }
}
