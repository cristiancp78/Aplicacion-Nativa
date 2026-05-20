package com.example.tecnotech.Mapas

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ActivitySeleccionarUbicacionBinding
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class SeleccionarUbicacionActivity : AppCompatActivity() , OnMapReadyCallback {

    private lateinit var binding: ActivitySeleccionarUbicacionBinding

    private companion object{
        private const val DEFAULT_ZOOM = 15
    }

    private var mMap: GoogleMap ?= null
    private var mPlaceClient : PlacesClient?= null
    private var mFusedLocationProviderClient : FusedLocationProviderClient?= null

    private var mLastKnownLocation : Location?= null
    private var selectedLatitud : Double?= null
    private var selectedLongitud : Double?= null
    private var address = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeleccionarUbicacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.listoLl.visibility = View.GONE

        val mapFragment = supportFragmentManager.findFragmentById(R.id.MapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        Places.initialize(this, getString(R.string.mi_google_maps_api_key))
        mPlaceClient = Places.createClient(this)
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val autoCompleteSupportMapFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        val placeList = arrayOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG
        )

        autoCompleteSupportMapFragment.setPlaceFields(listOf(*placeList))

        autoCompleteSupportMapFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val id = place.id
                val name = place.name
                val latLng = place.latLng

                selectedLatitud = latLng?.latitude
                selectedLongitud = latLng?.longitude
                address = place.address?: ""

                addMarker(latLng, name,address)
            }

            override fun onError(p0: Status) {
            }

        })

        binding.IbGps.setOnClickListener {
            if (isGpsActivated()){
                solicitarPermisoLacalizacion.launch(Manifest.permission.ACCESS_FINE_LOCATION)

            }else{
                Toast.makeText(this, "La ubicacion no esta activada", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnListo.setOnClickListener {
            val intent = Intent()
            intent.putExtra("latitud", selectedLatitud)
            intent.putExtra("longitud", selectedLongitud)
            intent.putExtra("direccion", address)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }

    private fun elegirLugarActual(){
        if(mMap==null){
            return
        }

        detectAndShowDiviceLocationMap()
    }
    @SuppressLint("MissingPermission")
    private fun detectAndShowDiviceLocationMap(){
        try {
            val locationResult = mFusedLocationProviderClient!!.lastLocation
            locationResult.addOnSuccessListener { location ->
                if(location!=null){
                    mLastKnownLocation = location
                    selectedLatitud = location.latitude
                    selectedLongitud = location.longitude

                    val latLng = LatLng(selectedLatitud!!, selectedLongitud!!)

                    mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM.toFloat()))
                    mMap!!.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM.toFloat()))

                    direccionLatLng(latLng)
                }
            }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "No se pudo obtener la ubicacion", Toast.LENGTH_SHORT).show()
                }

        }catch (e: Exception){

        }
    }
    fun isGpsActivated(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        var gpsEneble = false
        var networkEnable = false

        try {
            gpsEneble = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }catch (e: Exception){

        }

        try {
            networkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }catch (e: Exception){

        }

        return !(!gpsEneble && !networkEnable)
    }

    @SuppressLint("MissingPermission")
    private val solicitarPermisoLacalizacion : ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { seConcede ->
        if (seConcede){
            mMap!!.isMyLocationEnabled = true
            elegirLugarActual()
        }else{
            Toast.makeText(this, "Permiso de ubicacion denegado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap= googleMap
        val latitud = intent.getDoubleExtra("latitud", 0.0)
        val longitud = intent.getDoubleExtra("longitud", 0.0)

        mMap!!.setOnMapClickListener {latLng ->
            selectedLatitud = latLng.latitude
            selectedLongitud = latLng.longitude

            direccionLatLng(latLng)

        }

        if(latitud != 0.0 && longitud != 0.0){
            val latLng = LatLng(latitud, longitud)
            selectedLatitud = latitud
            selectedLongitud = longitud
            mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            direccionLatLng(latLng)
        }else{
            solicitarPermisoLacalizacion.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }



    }

    fun direccionLatLng(latLng: LatLng) {
        val geoCoder = Geocoder(this)
        try {
            val addressList = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            val myAddress = addressList!![0]
            val addressLine = myAddress.getAddressLine(0)
            val subLocality = myAddress.subLocality
            address = "${addressLine}"

            addMarker(latLng, "${subLocality}", "${addressLine}")
        }catch (e: Exception){

        }

    }

    private fun addMarker(latLng: LatLng, titulo: String, direccion: String) {
        mMap!!.clear()
        try {
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            markerOptions.title("${titulo}")
            markerOptions.snippet("${direccion}")
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))


            mMap!!.addMarker(markerOptions)
            mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM.toFloat()))
            binding.listoLl.visibility = View.VISIBLE
            binding.lugarSelecTv.text = direccion
        }catch (e: Exception){

        }

    }
}