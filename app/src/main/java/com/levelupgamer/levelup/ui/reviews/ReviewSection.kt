package com.levelupgamer.levelup.ui.reviews

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ReviewSection(productCode: String, viewModel: ReviewViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(productCode) {
        viewModel.loadReviews(productCode)
    }

    Column {
        // --- Formulario para nueva review ---
        Text("Deja tu opinión", style = MaterialTheme.typography.titleLarge)
        var userRating by remember { mutableStateOf(0) }
        var userComment by remember { mutableStateOf("") }

        RatingBar(rating = userRating, onRatingChanged = { userRating = it })
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = userComment,
            onValueChange = { userComment = it },
            label = { Text("Comentario") },
            modifier = Modifier.fillMaxWidth().height(100.dp)
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = { 
                if (userRating > 0) {
                    viewModel.addReview(productCode, userRating, userComment)
                    userRating = 0
                    userComment = ""
                }
             },
            enabled = userRating > 0,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Enviar Opinión")
        }

        Spacer(Modifier.height(24.dp))

        // --- Lista de reviews existentes ---
        Text("Opiniones de otros Gamers", style = MaterialTheme.typography.titleLarge)
        if (uiState.reviews.isEmpty()) {
            Text("Este producto aún no tiene opiniones. ¡Sé el primero!")
        } else {
            LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                items(uiState.reviews) {
                    ReviewItem(it)
                    Divider()
                }
            }
        }
    }
}

@Composable
fun RatingBar(rating: Int, onRatingChanged: (Int) -> Unit) {
    Row {
        (1..5).forEach { index ->
            IconButton(onClick = { onRatingChanged(index) }) {
                Icon(
                    imageVector = if (index <= rating) Icons.Filled.Star else Icons.Outlined.StarOutline,
                    contentDescription = "Estrella $index",
                    tint = if (index <= rating) Color.Yellow else Color.Gray
                )
            }
        }
    }
}

@Composable
fun ReviewItem(review: com.levelupgamer.levelup.model.Review) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text("Usuario ID: ${review.userId}", fontWeight = FontWeight.Bold) // Podríamos mostrar el nombre si lo tuviéramos
        RatingBar(rating = review.rating, onRatingChanged = {}) // Solo para mostrar
        Text(review.comment)
    }
}
