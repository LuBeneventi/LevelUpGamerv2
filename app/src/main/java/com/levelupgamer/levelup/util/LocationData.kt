package com.levelupgamer.levelup.util

object LocationData {

    val regionsAndCommunes: Map<String, List<String>> = mapOf(
        "Región Metropolitana de Santiago" to listOf("Santiago", "Providencia", "Las Condes", "Maipú", "Puente Alto", "La Florida", "Ñuñoa"),
        "Región de Valparaíso" to listOf("Valparaíso", "Viña del Mar", "Quilpué", "Concón", "Villa Alemana"),
        "Región del Biobío" to listOf("Concepción", "Talcahuano", "San Pedro de la Paz", "Coronel", "Lota", "Hualpén"),
        "Región de la Araucanía" to listOf("Temuco", "Padre Las Casas", "Villarrica", "Pucón", "Angol"),
        "Región de Antofagasta" to listOf("Antofagasta", "Calama", "Tocopilla")
    )

    val regions: List<String> = regionsAndCommunes.keys.sorted()
}
