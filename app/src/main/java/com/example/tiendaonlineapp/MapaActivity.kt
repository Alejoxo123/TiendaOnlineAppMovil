package com.example.tiendaonlineapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tiendaonlineapp.data.local.AppDatabase
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapaActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val LOCATION_PERMISSION_REQUEST = 1001


    private val tiendaLatLng = LatLng(4.65, -74.06)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        mMap.addMarker(
            MarkerOptions()
                .position(tiendaLatLng)
                .title("ShopPoint - Tienda principal")
        )


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tiendaLatLng, 14f))


        verificarPermisosUbicacion()
    }

    private fun verificarPermisosUbicacion() {
        val permisoFine = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (permisoFine != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
        } else {
            habilitarMiUbicacion()
        }
    }

    private fun habilitarMiUbicacion() {
        try {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                mMap.isMyLocationEnabled = true

                // Opcional: centrar en la ubicación actual
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            val miLatLng = LatLng(location.latitude, location.longitude)
                            mMap.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(miLatLng, 15f)
                            )
                        }
                    }
            }
        } catch (e: SecurityException) {
            Toast.makeText(this, "Permiso de ubicación no disponible", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                habilitarMiUbicacion()
            } else {
                Toast.makeText(this, "Se necesita permiso de ubicación", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
