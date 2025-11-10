package com.levelupgamer.levelup.ui.address

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.levelupgamer.levelup.model.UserAddress
import com.levelupgamer.levelup.ui.viewmodel.ViewModelFactory
import com.levelupgamer.levelup.address.AddressViewModel
import com.levelupgamer.levelup.util.LocationData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(navController: NavController) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: AddressViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()

    var showEditDialog by remember { mutableStateOf(false) }
    var addressToEdit by remember { mutableStateOf<UserAddress?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                addressToEdit = null
                showEditDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Dirección")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.addresses.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No tienes direcciones guardadas. ¡Añade una!")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.addresses, key = { it.id }) { address ->
                        AddressItem(
                            address = address,
                            onEdit = {
                                addressToEdit = address
                                showEditDialog = true
                            },
                            onDelete = { viewModel.deleteAddress(address) },
                            onSetPrimary = { viewModel.setPrimaryAddress(address) }
                        )
                    }
                }
            }
        }

        if (showEditDialog) {
            AddressEditDialog(
                address = addressToEdit,
                onDismiss = { showEditDialog = false },
                onConfirm = { street, number, commune, region, isPrimary ->
                    viewModel.addOrUpdateAddress(street, number, commune, region, isPrimary, addressToEdit?.id)
                    showEditDialog = false
                }
            )
        }
    }
}

@Composable
fun AddressItem(address: UserAddress, onEdit: () -> Unit, onDelete: () -> Unit, onSetPrimary: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text("${address.street} ${address.numberOrApt}", fontWeight = FontWeight.Bold)
            Text("${address.commune}, ${address.region}")
            if (address.isPrimary) {
                Text("(Dirección Principal)", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodySmall)
            }
            Row(modifier = Modifier.padding(top = 8.dp)) {
                if (!address.isPrimary) {
                    Button(onClick = onSetPrimary) {
                        Text("Hacer Principal")
                    }
                    Spacer(Modifier.width(8.dp))
                }
                IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, "Editar") }
                IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, "Eliminar") }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressEditDialog(address: UserAddress?, onDismiss: () -> Unit, onConfirm: (String, String, String, String, Boolean) -> Unit) {
    var street by remember { mutableStateOf(address?.street ?: "") }
    var number by remember { mutableStateOf(address?.numberOrApt ?: "") }
    var region by remember { mutableStateOf(address?.region ?: "") }
    var commune by remember { mutableStateOf(address?.commune ?: "") }
    var isPrimary by remember { mutableStateOf(address?.isPrimary ?: false) }

    var isRegionExpanded by remember { mutableStateOf(false) }
    var isCommuneExpanded by remember { mutableStateOf(false) }

    var availableCommunes by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(region) {
        availableCommunes = LocationData.regionsAndCommunes[region] ?: emptyList()
        if (address?.region != region) { // Si el usuario cambia de región, limpiar la comuna
            commune = ""
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (address == null) "Añadir Dirección" else "Editar Dirección") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = street, onValueChange = { street = it }, label = { Text("Calle") })
                OutlinedTextField(value = number, onValueChange = { number = it }, label = { Text("Número / Depto") })

                ExposedDropdownMenuBox(expanded = isRegionExpanded, onExpandedChange = { isRegionExpanded = it }) {
                    OutlinedTextField(
                        value = region,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Región") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isRegionExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = isRegionExpanded, onDismissRequest = { isRegionExpanded = false }) {
                        LocationData.regions.forEach { regionName ->
                            DropdownMenuItem(
                                text = { Text(regionName) },
                                onClick = {
                                    region = regionName
                                    isRegionExpanded = false
                                }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(expanded = isCommuneExpanded, onExpandedChange = { isCommuneExpanded = it }) {
                    OutlinedTextField(
                        value = commune,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Comuna") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCommuneExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        enabled = availableCommunes.isNotEmpty()
                    )
                    ExposedDropdownMenu(expanded = isCommuneExpanded, onDismissRequest = { isCommuneExpanded = false }) {
                        availableCommunes.forEach { communeName ->
                            DropdownMenuItem(
                                text = { Text(communeName) },
                                onClick = {
                                    commune = communeName
                                    isCommuneExpanded = false
                                }
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isPrimary, onCheckedChange = { isPrimary = it })
                    Text("Marcar como principal")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(street, number, commune, region, isPrimary) },
                enabled = street.isNotBlank() && number.isNotBlank() && commune.isNotBlank() && region.isNotBlank()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
