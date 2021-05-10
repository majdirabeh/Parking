package fr.com.majdi.parking.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.mapbox.mapboxsdk.geometry.LatLng
import com.tbruyelle.rxpermissions2.RxPermissions
import fr.com.majdi.parking.model.Fields


/**
 * Created by Majdi RABEH on 10/05/2021.
 * Email m.rabeh.majdi@gmail.com
 */
@SuppressLint("CheckResult")
class Tools {
    companion object {

        val gson = GsonBuilder().create()
        private var rxPermissions: RxPermissions? = null

        fun convertToJson(model: Fields): JsonElement {
            return gson.toJsonTree(model)
        }

        fun convertJsonToModel(jsonElement: JsonElement): Fields {
            return gson.fromJson(jsonElement, Fields::class.java)
        }

        fun showToast(context: Context, message: String) {
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_LONG
            ).show()
        }

        fun initPermission(activity: FragmentActivity): Boolean {
            var isGranted = false
            rxPermissions = RxPermissions(activity)
            rxPermissions!!
                .request(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .subscribe { granted ->
                    isGranted = granted
                }
            return isGranted
        }

        fun distanceToParking(latLng: LatLng, parkingLatLng: LatLng): Int {
            return latLng.distanceTo(parkingLatLng).toInt()
        }

        fun navigateToMap(
            context: Context,
            sourceLatitude: Double,
            sourceLongitude: Double,
            destinationLatitude: Double,
            destinationLongitude: Double,
            mode: Char
        ) {
            val uri =
                "http://maps.google.com/maps?saddr=" +
                        sourceLatitude.toString() + "," + sourceLongitude.toString() +
                        "&daddr=" + destinationLatitude.toString() + "," + destinationLongitude + "&mode=" + mode
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.setPackage("com.google.android.apps.maps")
            context.startActivity(intent)
        }

    }
}