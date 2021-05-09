package fr.com.majdi.parking.ui

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.utils.BitmapUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import fr.com.majdi.parking.R
import fr.com.majdi.parking.databinding.ActivityMainBinding
import fr.com.majdi.parking.model.Fields
import fr.com.majdi.parking.model.ResponseParking
import fr.com.majdi.parking.utils.Constants.Companion.ICON_ID
import fr.com.majdi.parking.utils.Constants.Companion.ORLEANS_LATITUDE
import fr.com.majdi.parking.utils.Constants.Companion.ORLEANS_LONGITUDE
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(), OptionsFragment.ItemClickListener {

    private val viewMainModel by viewModel<MainViewModel>()
    private var rxPermissions: RxPermissions? = null
    lateinit var activityMainBinding: ActivityMainBinding
    private var mapboxMap: MapboxMap? = null
    private lateinit var symbolManager: SymbolManager
    private val gson = GsonBuilder().create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = activityMainBinding.root
        setContentView(view)
        initPermission(savedInstanceState)

    }

    private fun showOptionPopup(jsonElement: JsonElement) {
        supportFragmentManager.let {

            val fields = gson.fromJson(jsonElement, Fields::class.java)

            val bundle = Bundle()
            bundle.putSerializable("Fields", fields)
            OptionsFragment.newInstance(bundle).apply {
                show(it, tag)
            }
        }
    }


    @SuppressLint("CheckResult")
    private fun initPermission(savedInstanceState: Bundle?) {
        rxPermissions = RxPermissions(this)
        rxPermissions!!
            .request(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .subscribe { granted ->
                if (granted) { // Always true pre-M
                    initMapBox(savedInstanceState)
                } else {
                    Toast.makeText(
                        this,
                        "Please accept permission location to start map",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

    }

    private fun initMapBox(savedInstanceState: Bundle?) {
        activityMainBinding.mapView.onCreate(savedInstanceState)
        activityMainBinding.mapView.getMapAsync { mapbox ->
            mapboxMap = mapbox

            val orleansLocation = LatLng(ORLEANS_LATITUDE, ORLEANS_LONGITUDE)

            val position = CameraPosition.Builder()
                .target(orleansLocation)
                .zoom(10.0)
                .build()

            mapboxMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(position))


            mapboxMap!!.setStyle(Style.MAPBOX_STREETS) { style ->
                val selectedMarkerIconDrawable = ResourcesCompat.getDrawable(
                    this.resources,
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

    private fun getListParking() {
        viewMainModel.listParking.observe(this, Observer {
            if (it.records.isNotEmpty()) {
                for (parking in it.records) {
                    Log.e("MainActivity", parking.toString())
                    // Add symbol at specified lat/lon.
                    val latLng = LatLng(parking.fields.coords[0], parking.fields.coords[1])


                    val jsonElement = gson.toJsonTree(parking.fields)

                    symbolManager.create(
                        SymbolOptions()
                            .withLatLng(latLng)
                            .withIconImage(ICON_ID)
                            .withData(jsonElement)
                            .withIconSize(0.1f)
                    )
                }
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

    override fun onItemClick(item: String) {

    }

}