package com.example.geocoding

import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.IOException

private const val TAG = "GEOCODE_PLACE_ACTIVITY"

class MainActivity : AppCompatActivity() {

    // Resource components
    private lateinit var placeNameInput: EditText
    private lateinit var mapButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find components and initialize
        placeNameInput = findViewById(R.id.place_name_input)
        mapButton = findViewById(R.id.map_button)

        // Event listeners
        mapButton.setOnClickListener {
            val placeName = placeNameInput.text.toString()
            // Validation not empty
            if (placeName.isEmpty()) {
                Toast.makeText(this, getString(R.string.no_place_entered_error), Toast.LENGTH_SHORT).show()
            } else {
                // Log statements
                Log.d(TAG, "About to geocode $placeName")
                // Call function
                showMapForPlace(placeName)
            }
        }
    }
    // Function for placeName
    private fun showMapForPlace(placeName: String) {
        // geocode place name to get list of locations
        val geocoder = Geocoder(this)
        try {
            val addresses = geocoder.getFromLocationName(placeName, 1)
            val address = addresses?.firstOrNull()
            // use and intent to launch map app, for first location, if a location is found

            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val address = addresses.first()
                    Log.d(TAG, "First address is $address")
                    val geoUriString =
                        "geo:${address.latitude},${address.longitude}" // " geo:45,-90" Minneapolis
                    // Log for geo tag
                    Log.d(TAG, "Using geo uri $geoUriString")
                    val geoUri = Uri.parse(geoUriString)
                    val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
                    Log.d(TAG, "Launching map activity")
                    startActivity(mapIntent)
                } else {
                    // Log for no places
                    Log.d(TAG, "No places found for string $placeName")
                    Toast.makeText(this, getString(R.string.no_places_found), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Unable to geocode place $placeName", e)
            Toast.makeText(this, "Sorry, unable to geocode place. Are you online?",
                Toast.LENGTH_LONG).show()
        }
    }
}