package com.example.thesis.views.loggedin

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
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
import java.io.IOException
import java.util.*


class ShareFollowFragment : Fragment() {

    private lateinit var binding: FragmentShareFollowBinding
    private lateinit var marker: Marker
    var targetLocation: String = "INIT"
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
            val user = arguments?.get("user").toString()
            addTargetMarker(googleMap, user)
            addMarker(googleMap)
            locationViewModel.getTarget(user)
            locationViewModel.getLocation(user)
            val runnable: Runnable = object : Runnable {
                @SuppressLint("MissingPermission")
                override fun run() {
                    try {
                        locationViewModel.getTarget(user)
                        locationViewModel.getLocation(user)
                        refreshHandler.postDelayed(this, 5 * 1000)
                    }
                    catch(e: Exception){
                        Log.d("ended", "exception")
                    }
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

            if(location.state == ShareLocationState.SHARING)
                marker = googleMap.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            location.latitude,
                            location.longitude
                        )
                    ).icon(resizedBitmap?.let { BitmapDescriptorFactory.fromBitmap(it) })
                )!!

            else{
                view?.findNavController()?.navigate(R.id.action_shareFollowFragment_to_aboutMeFragment)
            }
        }

        locationViewModel.location.observe(viewLifecycleOwner, nameObserver)
    }

    private fun addTargetMarker(googleMap: GoogleMap, targetLocation:String) {
        val bitmap = context?.let { AppCompatResources.getDrawable(it, R.drawable.plus)?.toBitmap() }
        val resizedBitmap = bitmap?.let { Bitmap.createScaledBitmap(it, 80, 80, false) }

        val nameObserver = Observer<String> { loc ->

            if(loc.startsWith("lat/lng")) {

                val location = loc.substringAfter("(").substringBefore(")")
                val lat = location.split(",")[0].toDouble()
                val long = location.split(",")[1].toDouble()

                binding.textView.text = locationToAddress(lat,long)

                googleMap.addMarker(
                    MarkerOptions().position(LatLng(lat, long))
                        .icon(resizedBitmap?.let { BitmapDescriptorFactory.fromBitmap(it) })
                )!!

            }

            else{
                binding.textView.text = loc
            }


        }
        locationViewModel.targetLocation.observe(viewLifecycleOwner, nameObserver)
    }

    private fun locationToAddress(lat:Double, long:Double): String{
        val result = StringBuilder()
        try {
            val geocoder = context?.let { Geocoder(it, Locale.getDefault()) }
            val addresses = geocoder?.getFromLocation(lat, long, 1)
            if (addresses!!.size > 0) {
                val address = addresses[0]
                result.append(address.locality).append("\n")
                result.append(address.countryName)
            }
        } catch (e: IOException) {
            Log.e("tag", e.message.toString())
        }

        return result.toString()
    }

}
