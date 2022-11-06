package com.example.thesis.views.loggedin

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.thesis.R
import com.example.thesis.databinding.FragmentMapsBinding
import com.example.thesis.viewModels.LocationViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment


class MapsFragment : Fragment(),
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var binding: FragmentMapsBinding
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_maps, container, false)
        locationViewModel = ViewModelProvider(this)[LocationViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val supportMap = childFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment
        fusedLocationClient = context?.let { LocationServices.getFusedLocationProviderClient(it) }!!
        supportMap.getMapAsync(this)


        binding.fabStop.setOnClickListener{
            val user = arguments?.get("user").toString()
            locationViewModel.stopSharing(user)
            view.findNavController().navigate(R.id.action_mapsFragment_to_aboutMeFragment)
        }

        binding.fab.setOnClickListener {
            binding.fab.isEnabled = false
            binding.fab.text = getString(R.string.Sharei)
            arguments?.get("user")?.let { user ->
                locationViewModel.addNewShare(user.toString())
                val type = arguments?.get("type")
                val target = arguments?.get("target")
                if (type == "MAP")
                    locationViewModel.addNewTarget(target.toString())
                if (type == "MANUAL")
                    locationViewModel.addNewManualTarget(target.toString())

            }
        }

            val refreshHandler = Handler()
            val runnable: Runnable = object : Runnable {
                @SuppressLint("MissingPermission")
                override fun run() {
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location : Location? ->
                            if (location != null) {
                                locationViewModel.shareMyLocation(location)
                            }
                        }
                    refreshHandler.postDelayed(this, 5 * 1000)
                }
            }
            refreshHandler.postDelayed(runnable, 5 * 1000)

    }

    @SuppressLint("MissingPermission")
    fun enableMyLocation() {

        // 1. Check if permissions are granted, if so, enable the my location layer
        if (context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED || context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            return
        }

        // 2. If if a permission rationale dialog should be shown
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            return
        }

        // 3. Otherwise, request permission
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(context, "MyLocation button clicked", Toast.LENGTH_SHORT)
            .show()
        return false
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(context, "Current location:\n${location.longitude}", Toast.LENGTH_LONG)
            .show()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
            )
            return
        }
        enableMyLocation()

    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        googleMap.setOnMyLocationButtonClickListener(this)
        googleMap.setOnMyLocationClickListener(this)
        enableMyLocation()

    }
}
