package fr.com.majdi.parking.utils

import com.mapbox.mapboxsdk.geometry.LatLng

/**
 * Created by Majdi RABEH on 09/05/2021.
 * Email m.rabeh.majdi@gmail.com
 */
class Constants {
    companion object {
        const val BASE_URL = "https://data.orleans-metropole.fr"
        const val DATA_SET = "mobilite-places-disponibles-parkings-en-temps-reel"
        const val NUMBER_PARKING = 10
        const val ICON_ID = "ICON_ID"
        val orleansLocation = LatLng(47.8868649, 1.8735829)
        const val INTENT_SHEET_DIALOG = "Fields"

    }
}