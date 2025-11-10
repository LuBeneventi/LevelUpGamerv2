package com.levelupgamer.app.ui

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.levelupgamer.levelup.MyApp
import com.levelupgamer.levelup.R
import com.levelupgamer.levelup.data.repository.*
import com.levelupgamer.levelup.model.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

// --- NAVIGATION ROOT ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminFeature(mainNavController: NavHostController) {
    val adminNavController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val navBackStackEntry by adminNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(getAdminScreenTitle(currentRoute)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
                navigationIcon = {
                    if (adminNavController.previousBackStackEntry != null) {
                        IconButton(onClick = { adminNavController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
             if (currentRoute == "adminProducts" || currentRoute == "adminEvents" || currentRoute == "adminRewards") {
                FloatingActionButton(onClick = {
                    when (currentRoute) {
                        "adminProducts" -> adminNavController.navigate("productEdit")
                        "adminEvents" -> adminNavController.navigate("eventEdit")
                        "adminRewards" -> adminNavController.navigate("rewardEdit")
                    }
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir")
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController = adminNavController, startDestination = "adminHome", modifier = Modifier.padding(innerPadding)) {
            composable("adminHome") { AdminHomeScreen(adminNavController, mainNavController) }
            composable("adminProducts") { AdminProductsScreen(adminNavController) }
            composable("adminEvents") { AdminEventsScreen(navController = adminNavController) }
            composable("adminRewards") { AdminRewardsScreen(navController = adminNavController) }
            composable("adminOrders") { AdminOrdersScreen(navController = adminNavController) }
            composable("adminUsers") { AdminUsersScreen(navController = adminNavController) }
            composable("productEdit?productCode={productCode}", arguments = listOf(navArgument("productCode") { type = NavType.StringType; nullable = true })) {
                ProductEditScreen(adminNavController, it.arguments?.getString("productCode"), snackbarHostState)
            }
            composable("eventEdit?eventId={eventId}", arguments = listOf(navArgument("eventId") { type = NavType.StringType; nullable = true })) {
                EventEditScreen(adminNavController, it.arguments?.getString("eventId"), snackbarHostState)
            }
            composable("rewardEdit?rewardId={rewardId}", arguments = listOf(navArgument("rewardId") { type = NavType.StringType; nullable = true })) {
                RewardEditScreen(adminNavController, it.arguments?.getString("rewardId"), snackbarHostState)
            }
            composable("adminOrderDetail/{orderId}", arguments = listOf(navArgument("orderId") { type = NavType.StringType })) {
                AdminOrderDetailScreen(it.arguments?.getString("orderId") ?: "")
            }
        }
    }
}

// --- ADMIN HOME SCREEN ---
@Composable
private fun AdminHomeScreen(navController: NavController, mainNavController: NavHostController) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Panel de Administración", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(32.dp))
        Button(onClick = { navController.navigate("adminProducts") }, modifier = Modifier.fillMaxWidth()) { Text("Gestionar Productos") }
        Button(onClick = { navController.navigate("adminEvents") }, modifier = Modifier.fillMaxWidth()) { Text("Gestionar Eventos") }
        Button(onClick = { navController.navigate("adminRewards") }, modifier = Modifier.fillMaxWidth()) { Text("Gestionar Recompensas") }
        Button(onClick = { navController.navigate("adminOrders") }, modifier = Modifier.fillMaxWidth()) { Text("Gestionar Pedidos") }
        Button(onClick = { navController.navigate("adminUsers") }, modifier = Modifier.fillMaxWidth()) { Text("Gestionar Usuarios") }
        Spacer(Modifier.weight(1f))
        Button(onClick = {
            context.getSharedPreferences("user", Context.MODE_PRIVATE).edit().clear().apply()
            mainNavController.navigate("login") { popUpTo(mainNavController.graph.id) { inclusive = true } }
        }, modifier = Modifier.fillMaxWidth()) { Text("Cerrar Sesión") }
    }
}

// --- TITLE HELPER ---
private fun getAdminScreenTitle(route: String?): String {
    return when {
        route == "adminHome" -> "Panel de Admin"
        route == "adminProducts" -> "Gestionar Productos"
        route?.startsWith("productEdit") == true -> "Editar Producto"
        route == "adminEvents" -> "Gestionar Eventos"
        route?.startsWith("eventEdit") == true -> "Editar Evento"
        route == "adminRewards" -> "Gestionar Recompensas"
        route?.startsWith("rewardEdit") == true -> "Editar Recompensa"
        route == "adminOrders" -> "Gestionar Pedidos"
        route?.startsWith("adminOrderDetail") == true -> "Detalle del Pedido"
        route == "adminUsers" -> "Gestionar Usuarios"
        else -> "Admin"
    }
}

// --- ADMIN PRODUCTS ---
@Composable
private fun AdminProductsScreen(navController: NavController) {
    val context = LocalContext.current
    val productRepository = remember { ProductRepository((context.applicationContext as MyApp).database.productDao()) }
    val products by productRepository.getAllProducts().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(products, key = { it.code }) { product ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(product.name, fontWeight = FontWeight.Bold)
                        Text("Stock: ${product.quantity}")
                    }
                    IconButton(onClick = { navController.navigate("productEdit?productCode=${product.code}") }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = { scope.launch { productRepository.delete(product) } }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductEditScreen(navController: NavController, productCode: String?, snackbarHostState: SnackbarHostState) {
    val isEditing = productCode != null
    val context = LocalContext.current
    val productRepository = remember { ProductRepository((context.applicationContext as MyApp).database.productDao()) }
    val scope = rememberCoroutineScope()

    var productToEdit by remember { mutableStateOf<Product?>(null) }
    var code by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val productCategories = listOf("Consolas", "Videojuegos", "Accesorios", "Poleras", "Figuras")
    var isCategoryMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(productCode) {
        if (isEditing) {
            scope.launch {
                productToEdit = productRepository.getProductByCode(productCode!!)
                productToEdit?.let {
                    code = it.code; name = it.name; category = it.category; price = it.price.toString(); quantity = it.quantity.toString(); description = it.description
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        OutlinedTextField(value = code, onValueChange = { code = it }, label = { Text("Código (SKU)") }, enabled = !isEditing, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())

        ExposedDropdownMenuBox(expanded = isCategoryMenuExpanded, onExpandedChange = { isCategoryMenuExpanded = !isCategoryMenuExpanded }, modifier = Modifier.padding(vertical = 8.dp)) {
            OutlinedTextField(value = category, onValueChange = {}, label = { Text("Categoría") }, readOnly = true, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryMenuExpanded) }, modifier = Modifier.menuAnchor().fillMaxWidth())
            ExposedDropdownMenu(expanded = isCategoryMenuExpanded, onDismissRequest = { isCategoryMenuExpanded = false }) {
                productCategories.forEach { cat -> DropdownMenuItem(text = { Text(cat) }, onClick = { category = cat; isCategoryMenuExpanded = false }) }
            }
        }

        OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Precio") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = quantity, onValueChange = { quantity = it }, label = { Text("Stock") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth().height(100.dp))
        
        Spacer(Modifier.height(24.dp))

        Button(onClick = {
            val newOrUpdatedProduct = Product(
                code = code,
                name = name, 
                category = category, 
                price = price.toIntOrNull() ?: 0, 
                quantity = quantity.toIntOrNull() ?: 0, 
                description = description,
                imageResId = productToEdit?.imageResId ?: R.drawable.ic_launcher_foreground, // Conservar imagen existente o usar una por defecto
                averageRating = productToEdit?.averageRating ?: 0f
            )
            
            scope.launch {
                if (isEditing) productRepository.update(newOrUpdatedProduct) else productRepository.insert(newOrUpdatedProduct)
                snackbarHostState.showSnackbar("Producto guardado con éxito")
                navController.popBackStack()
            }
        }, modifier = Modifier.fillMaxWidth()) { Text(if (isEditing) "Guardar Cambios" else "Añadir Producto") }
    }
}

// --- ADMIN EVENTS ---
@Composable
private fun AdminEventsScreen(navController: NavController) { /* ... */ }
@Composable
private fun EventEditScreen(navController: NavController, eventId: String?, snackbarHostState: SnackbarHostState) { /* ... */ }

// --- ADMIN REWARDS ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdminRewardsScreen(navController: NavController) {
    val context = LocalContext.current
    val rewardRepository = remember { RewardRepository((context.applicationContext as MyApp).database.rewardDao()) }
    val rewards by rewardRepository.getAllRewards().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(rewards, key = { it.id }) { reward ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(reward.title, fontWeight = FontWeight.Bold)
                        Text("Costo: ${reward.pointsCost} puntos. Stock: ${reward.stock ?: "Ilimitado"}")
                    }
                    IconButton(onClick = { navController.navigate("rewardEdit?rewardId=${reward.id}") }) { Icon(Icons.Default.Edit, "Editar") }
                    IconButton(onClick = { scope.launch { rewardRepository.deleteReward(reward) } }) { Icon(Icons.Default.Delete, "Eliminar") }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RewardEditScreen(navController: NavController, rewardId: String?, snackbarHostState: SnackbarHostState) {
    val isEditing = rewardId != null
    val context = LocalContext.current
    val rewardRepository = remember { RewardRepository((context.applicationContext as MyApp).database.rewardDao()) }
    val scope = rememberCoroutineScope()

    var rewardToEdit by remember { mutableStateOf<Reward?>(null) }
    var id by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var pointsCost by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(RewardType.DISCOUNT_PERCENTAGE) }
    var value by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var productCode by remember { mutableStateOf("") }
    var isTypeMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(rewardId) {
        if (isEditing) {
            scope.launch {
                rewardToEdit = rewardRepository.getReward(rewardId!!)
                rewardToEdit?.let { r ->
                    id = r.id; title = r.title; description = r.description; pointsCost = r.pointsCost.toString()
                    type = RewardType.valueOf(r.type); value = r.value?.toString() ?: ""; stock = r.stock?.toString() ?: ""; productCode = r.productCode ?: ""
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        OutlinedTextField(value = id, onValueChange = { id = it }, label = { Text("ID Recompensa") }, enabled = !isEditing, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Título") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth().height(100.dp))
        OutlinedTextField(value = pointsCost, onValueChange = { pointsCost = it }, label = { Text("Costo en Puntos") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        
        ExposedDropdownMenuBox(expanded = isTypeMenuExpanded, onExpandedChange = { isTypeMenuExpanded = it }) {
             OutlinedTextField(value = type.name, onValueChange = {}, label = { Text("Tipo de Recompensa") }, readOnly = true, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTypeMenuExpanded) }, modifier = Modifier.menuAnchor().fillMaxWidth())
            ExposedDropdownMenu(expanded = isTypeMenuExpanded, onDismissRequest = { isTypeMenuExpanded = false }) {
                RewardType.values().forEach { rt -> DropdownMenuItem(text = { Text(rt.name) }, onClick = { type = rt; isTypeMenuExpanded = false }) }
            }
        }

        when (type) {
            RewardType.DISCOUNT_PERCENTAGE ->
                OutlinedTextField(value = value, onValueChange = { value = it }, label = { Text("Porcentaje de Descuento (ej: 10)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
            RewardType.DISCOUNT_AMOUNT ->
                OutlinedTextField(value = value, onValueChange = { value = it }, label = { Text("Monto de Descuento (ej: 5000)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
            RewardType.FREE_PRODUCT ->
                OutlinedTextField(value = productCode, onValueChange = { productCode = it }, label = { Text("Código de Producto (SKU)") }, modifier = Modifier.fillMaxWidth())
            RewardType.FREE_SHIPPING -> {}
        }

        OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("Stock (dejar en blanco para ilimitado)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(24.dp))
        
        Button(onClick = {
            val finalStock = if (stock.isBlank()) null else stock.toIntOrNull()
            val finalValue = if (value.isBlank()) null else value.toDoubleOrNull()
            val finalProductCode = if (productCode.isBlank()) null else productCode

            val reward = Reward(id, title, description, pointsCost.toIntOrNull() ?: 0, type.name, finalValue, finalStock, finalProductCode)
            scope.launch { 
                if (isEditing) rewardRepository.updateReward(reward) else rewardRepository.addReward(reward)
                snackbarHostState.showSnackbar("Recompensa guardada"); navController.popBackStack() 
            }
        }, modifier = Modifier.fillMaxWidth()) { Text(if (isEditing) "Guardar Cambios" else "Añadir Recompensa") }
    }
}

// --- ADMIN ORDERS & USERS ---
@Composable
private fun AdminOrdersScreen(navController: NavController) { /* ... */ }
@Composable
private fun AdminOrderDetailScreen(orderId: String) { /* ... */ }
@Composable
private fun AdminUsersScreen(navController: NavController) { /* ... */ }
@Composable
private fun UserListItem(user: User, onToggleStatus: () -> Unit) { /* ... */ }
@Composable
private fun AdminOrderItem(orderWithItems: OrderWithItems, onClick: () -> Unit, onStatusChange: (OrderStatus) -> Unit) { /* ... */ }
