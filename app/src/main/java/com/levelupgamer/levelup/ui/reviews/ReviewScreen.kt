package com.levelupgamer.levelup.ui.reviews

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.levelupgamer.levelup.ui.viewmodel.ViewModelFactory

@Composable
fun ReviewScreen(navController: NavController, productCode: String) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: ReviewViewModel = viewModel(factory = factory)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Opiniones del Producto", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(16.dp))
        ReviewSection(productCode = productCode, viewModel = viewModel)
    }
}
