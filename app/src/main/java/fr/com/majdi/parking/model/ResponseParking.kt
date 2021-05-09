package fr.com.majdi.parking.model

data class ResponseParking(
    val nhits: Int,
    val parameters: Parameters,
    val records: List<Record>
)