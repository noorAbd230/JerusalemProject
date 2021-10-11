package com.example.jerusalemapplication.activites

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.jerusalemapplication.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.jerusalemapplication.databinding.ActivityMapsBinding
import com.example.jerusalemapplication.model.SharedPref
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var mMap: GoogleMap
    lateinit var sharedPref: SharedPref
    private lateinit var binding: ActivityMapsBinding
    var mLocation: Location? = null
    var lat = 0.0
    var lng = 0.0
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPref = SharedPref(this)
        if (sharedPref.loadNightModeState()){
            setTheme(R.style.DarkTheme)
        }else{
            setTheme(R.style.AppTheme)
        }

        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.app_name)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        firebaseAnalytics = Firebase.analytics
        trackScreen("Library Screen")
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        ) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return
        }
        mMap.isMyLocationEnabled = true
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle))

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mFusedLocationClient!!.requestLocationUpdates(
            getLocationRequest(),
            mLocationCallback,
            Looper.myLooper()
        )

        val sydney = LatLng(31.776242474804636, 35.235780202949236)
        mMap.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).position(sydney).title("المسجد الاقصى").snippet("القدس - فلسطين"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14f))

        mMap.setOnMapClickListener { latLng ->
            mMap.clear()
            mMap.addMarker(
                MarkerOptions().position(latLng)
            )
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
            var d = calculateDistance(LatLng(lat,lng), LatLng(latLng.latitude,latLng.longitude))
            Toast.makeText(
                this,
                "يبعد عنك هذا المكان $d كم ",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    private var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                mLocation = location
            }

            lat = mLocation!!.latitude
            lng = mLocation!!.longitude
            var distance = calculateDistance(LatLng(lat, lng),LatLng(31.77624, 35.23576))
            textSpace.text = "$distance كم "
            Log.e("mmm",textSpace.text.toString())
            val location = LatLng(lat, lng)

            mMap.addMarker(MarkerOptions().position(location).title("Your Location"))


            Log.e("MapsFragment", "$lat $lng")
            if (mFusedLocationClient != null) {
                mFusedLocationClient!!.removeLocationUpdates(this)
            }
        }
    }




        fun getLocationRequest(): LocationRequest? {
            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 3000
            return locationRequest
        }
private fun trackScreen(screenName:String) {
    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
        param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        param(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity")
    }

}
}
        private fun calculateDistance(x: LatLng, y: LatLng) : Float {
            var location = Location("")
            location.latitude = x.latitude
            location.longitude = x.longitude

            var location2 = Location("")
            location2.latitude = y.latitude
            location2.longitude = y.longitude

            var distance = location.distanceTo(location2)
            var km = distance / 1000
            return km
}

