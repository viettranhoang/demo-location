package com.vit.demolocation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.*
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tbruyelle.rxpermissions2.RxPermissions
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    val locationManager by lazy { getSystemService(Context.LOCATION_SERVICE) as LocationManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RxPermissions(this).request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe {
                    if (it) {
                        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

                        fusedLocationClient.lastLocation
                                .addOnSuccessListener { location : Location? ->
                                    val gcd = Geocoder(baseContext,
                                            Locale.getDefault())
                                    val addresses: List<Address>
                                        addresses = gcd.getFromLocation(location!!.latitude,
                                                location.longitude, 1)

                                        Log.i("jhdhf", addresses.toString())
                                        Toast.makeText(this@MainActivity, addresses.toString(), Toast.LENGTH_SHORT).show()

                                }
                                .addOnFailureListener {
                                    Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT).show()

                                }
//                        getLocation()
                    } else {
                        Toast.makeText(this@MainActivity, "Permission", Toast.LENGTH_SHORT).show()

                    }
                }
    }

    @SuppressLint("MissingPermission")
    fun getLocation() {
        val provider = locationManager.getBestProvider(Criteria(), false)
        val location = locationManager.getLastKnownLocation(provider)

        val gcd = Geocoder(baseContext,
                Locale.getDefault())
        val addresses: List<Address>
        try {
            addresses = gcd.getFromLocation(location.latitude,
                    location.longitude, 1)

            Log.i("jhdhf", addresses.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
