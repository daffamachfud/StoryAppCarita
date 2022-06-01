package com.daffa.storyappcarita.ui.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.daffa.storyappcarita.R
import com.daffa.storyappcarita.databinding.FragmentMapBinding
import com.daffa.storyappcarita.util.UserPreference
import com.daffa.storyappcarita.util.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private lateinit var mMap: GoogleMap
    private lateinit var viewModel: MapViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var boundsBuilder = LatLngBounds.Builder()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)


        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        initViewModel()
        return binding.root
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                UserPreference.getInstance(requireContext().dataStore),
                requireContext()
            )
        )[MapViewModel::class.java]
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.setOnMarkerClickListener {
            if (it.snippet != null) {
                val modalBottomSheet = MapBottomSheetFragment(it.snippet)
                modalBottomSheet.show(childFragmentManager, MapBottomSheetFragment.TAG)
                Toast.makeText(requireContext(), "Memuat Gambar", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Story tidak punya gambar", Toast.LENGTH_LONG)
                    .show()
            }
            true
        }
        try {
            val success =
                mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireContext(),
                        R.raw.map_style
                    )
                )
            if (!success) {
                Log.e("onFailure", "Style Parsing Failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e("onFailure", "Can't find style. Error: ", exception)
        }

        getMyLastLocation()
        getLocationStories()
    }

    private fun getLocationStories() {
        viewModel.getUser().observe(requireActivity()) {
            if (it.token != null) {
                if (view != null) {
                    viewModel.getAllStoriesMap("Bearer ${it.token}")
                        .observe(viewLifecycleOwner) { result ->
                            result.forEach { data ->
                                val location = LatLng(data.lat, data.lon)
                                boundsBuilder.include(location)
                                val bounds: LatLngBounds = boundsBuilder.build()
                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(location)
                                        .title(data.name)
                                        .snippet(data.photoUrl)
                                )
                                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10))
                            }
                        }
                }
            } else {
                Toast.makeText(requireContext(), "Gagal mengambil data stories", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }


    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    showMyLocation(location)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                    getLocationStories()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                    getLocationStories()
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    private fun showMyLocation(location: Location) {
        val startLocation = LatLng(location.latitude, location.longitude)
        mMap.addMarker(
            MarkerOptions()
                .position(startLocation)
                .title("My Location").icon(
                    BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_ORANGE
                    )
                )
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 17f))
    }


}