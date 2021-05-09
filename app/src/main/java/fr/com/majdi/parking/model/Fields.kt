package fr.com.majdi.parking.model

import java.io.Serializable

data class Fields(
    val coords: List<Double>,
    val datetime: String,
    val dispo: Int,
    val disponibilite: Double,
    val id: String,
    val name: String,
    val total: Int
) : Serializable