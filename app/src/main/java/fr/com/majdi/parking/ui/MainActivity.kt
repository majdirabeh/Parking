package fr.com.majdi.parking.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.gson.JsonElement
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.utils.BitmapUtils
import fr.com.majdi.parking.R
import fr.com.majdi.parking.databinding.ActivityMainBinding
import fr.com.majdi.parking.utils.Constants.Companion.ICON_ID
import fr.com.majdi.parking.utils.Constants.Companion.INTENT_SHEET_DIALOG
import fr.com.majdi.parking.utils.Constants.Companion.orleansLocation
import fr.com.majdi.parking.utils.Tools
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(), OptionsFragment.ItemClickListener {

    private lateinit var activityMainBinding: ActivityMainBinding
    private val viewMainModel by viewModel<MainViewModel>()

    private var mapboxMap: MapboxMap? = null
    private lateinit var symbolManager: SymbolManager
    private var listOptions = mutableListOf<SymbolOptions>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = activityMainBinding.root
        setContentView(view)

        if (Tools.initPermission(this)) {
            initMapBox(savedInstanceState)
        } else {
            Tools.showToast(this, resources.getString(R.string.message_permission))
        }

    }

    private fun initMapBox(savedInstanceState: Bundle?) {
        activityMainBinding.mapView.onCreate(savedInstanceState)
        activityMainBinding.mapView.getMapAsync { mapbox ->
            mapboxMap = mapbox


            val position = CameraPosition.Builder()
                .target(orleansLocation)
                .zoom(10.0)
                .build()

            mapboxMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(position))


            mapboxMap!!.setStyle(Style.MAPBOX_STREETS) { style ->
                val selectedMarkerIconDrawable = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.parking,
                    null
                )

                style.addImage(
                    ICON_ID,
                    BitmapUtils.getBitmapFromDrawable(selectedMarkerIconDrawable)!!
                )

                this.symbolManager = SymbolManager(activityMainBinding.mapView, mapboxMap!!, style)

                getListParking()

                symbolManager.addClickListener {
                    showOptionPopup(it.data!!.asJsonObject)
                    false
                }
            }
        }
    }

    private fun showOptionPopup(jsonElement: JsonElement) {
        supportFragmentManager.let {
            val fields = Tools.convertJsonToModel(jsonElement)
            val bundle = Bundle()
            bundle.putSerializable(INTENT_SHEET_DIALOG, fields)
            OptionsFragment.newInstance(bundle).apply {
                show(it, tag)
            }
        }
    }

    private fun getListParking() {
        viewMainModel.listParking.observe(this, {
            if (it.records.isNotEmpty()) {
                for (parking in it.records) {
                    val latLng = LatLng(parking.fields.coords[0], parking.fields.coords[1])
                    val jsonElement = Tools.convertToJson(parking.fields)
                    listOptions.add(
                        SymbolOptions()
                            .withLatLng(latLng)
                            .withIconImage(ICON_ID)
                            .withData(jsonElement)
                            .withIconSize(0.1f)
                    )
                }
                symbolManager.create(listOptions)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        activityMainBinding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        activityMainBinding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        activityMainBinding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        activityMainBinding.mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        activityMainBinding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        activityMainBinding.mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        activityMainBinding.mapView.onSaveInstanceState(outState)
    }

    override fun onItemClick(parkingLocation: LatLng, mode: Char) {
        Tools.navigateToMap(
            this, orleansLocation.latitude, orleansLocation.longitude,
            parkingLocation.latitude, parkingLocation.longitude, mode
        )
    }

}