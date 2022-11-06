package com.example.thesis.views.loggedin

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.thesis.R
import com.example.thesis.databinding.FragmentShareTargetSettingsBinding
import com.example.thesis.viewModels.LocationViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class ShareTargetSettingsFragment : Fragment() {

    private lateinit var binding: FragmentShareTargetSettingsBinding
    private lateinit var marker: Marker
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_share_target_settings, container, false)
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
            googleMap.setOnMapClickListener { target ->
                addMarker(googleMap, target)
                binding.fab.setOnClickListener{ view ->
                    val user = arguments?.get("user").toString()
                    val bundle = Bundle()
                    bundle.putString("user", user)
                    bundle.putString("target", target.toString())
                    bundle.putString("type", ShareTargetType.MAP.toString())
                    view.findNavController().navigate(R.id.action_shareTargetSettingsFragment_to_mapsFragment, bundle)
                }
            }
        }
    }

    private fun addMarker(googleMap: GoogleMap, latLng: LatLng) {
        val bitmap = context?.let { AppCompatResources.getDrawable(it, R.drawable.plus)?.toBitmap() }
        val resizedBitmap = bitmap?.let { Bitmap.createScaledBitmap(it, 80, 80, false) }

        if(this::marker.isInitialized) marker.remove()
        marker = googleMap.addMarker(
            MarkerOptions().position(latLng)
                .icon(resizedBitmap?.let { BitmapDescriptorFactory.fromBitmap(it) }))!!

    }

}