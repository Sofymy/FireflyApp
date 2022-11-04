package com.example.thesis.views.loggedin

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.thesis.R
import com.example.thesis.databinding.FragmentShareFollowBinding
import com.example.thesis.viewModels.LocationViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class ShareFollowFragment : Fragment() {

    private lateinit var binding: FragmentShareFollowBinding
    private lateinit var marker: Marker
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_share_follow, container, false)
        locationViewModel = ViewModelProvider(this)[LocationViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val supportMap = childFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment
        fusedLocationClient = context?.let { LocationServices.getFusedLocationProviderClient(it) }!!
        supportMap.getMapAsync { googleMap ->
            val refreshHandler = Handler()
            addMarker(googleMap)
            val runnable: Runnable = object : Runnable {
                @SuppressLint("MissingPermission")
                override fun run() {
                    val bundle = arguments?.get("user").toString()
                    locationViewModel.getLocation(bundle)
                    refreshHandler.postDelayed(this, 5 * 1000)
                }
            }
            refreshHandler.postDelayed(runnable, 5 * 1000)
        }
    }

    private fun addMarker(googleMap: GoogleMap) {
        val bitmap = context?.let { AppCompatResources.getDrawable(it, R.drawable.plus)?.toBitmap() }
        val resizedBitmap = bitmap?.let { Bitmap.createScaledBitmap(it, 80, 80, false) }

        val nameObserver = Observer<com.example.thesis.model.Location> { location ->
            if(this::marker.isInitialized) marker.remove()
            marker = googleMap.addMarker(
                MarkerOptions().position(
                    LatLng(
                        location.latitude,
                        location.longitude
                    )
                ).icon(resizedBitmap?.let { BitmapDescriptorFactory.fromBitmap(it) })
            )!!
        }

        locationViewModel.location.observe(viewLifecycleOwner, nameObserver)
    }

}
