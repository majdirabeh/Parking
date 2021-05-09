package fr.com.majdi.parking.model

data class Parameters(
    val dataset: String,
    val format: String,
    val rows: Int,
    val start: Int,
    val timezone: String
)