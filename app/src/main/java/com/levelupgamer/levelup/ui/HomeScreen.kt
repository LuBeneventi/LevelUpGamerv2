package com.levelupgamer.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.levelupgamer.levelup.model.Product

@Composable
fun HomeScreen(
    navController: NavController,
    products: List<Product>,
    cartProducts: List<Product>,
    onProductAdded: (String) -> Unit
) {
    val featuredProducts = products.shuffled().take(4)

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("¡Bienvenido a Level-Up Gamer!", style = MaterialTheme.typography.headlineMedium)
        Text("Tu próximo nivel, nuestro compromiso.", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(24.dp))
        Text("Productos Destacados", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(featuredProducts, key = { it.code }) { product ->
                ProductCard(
                    product = product, 
                    navController = navController, 
                    isInCart = cartProducts.any { it.code == product.code },
                    onProductAdded = { onProductAdded(product.code) }
                )
            }
        }
    }
}