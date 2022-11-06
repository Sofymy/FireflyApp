package com.example.thesis.views.loggedin

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thesis.R
import com.example.thesis.databinding.FragmentHomeBinding
import com.example.thesis.model.User
import com.example.thesis.model.Weather
import com.example.thesis.network.WeatherTask
import com.example.thesis.network.OnWeatherTaskCompleted
import com.example.thesis.viewModels.LocationViewModel
import kotlin.collections.ArrayList


class HomeFragment : Fragment(), OnWeatherTaskCompleted {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var locationViewModel: LocationViewModel
    private val city: String = "budapest,hu"
    private val key: String = "01bf5337639c1e43aeb2ed865a4015aa" // Use API key
    private val task = WeatherTask(city, key, this).execute()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        locationViewModel = ViewModelProvider(this)[LocationViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerview = binding.recyclerview
        recyclerview.layoutManager = LinearLayoutManager(context)
        val data = ArrayList<User>()
        val adapter = ShareRequestsAdapter(ShareRequestsAdapter.OnClickListener { item ->
            val bundle = Bundle()
            bundle.putString("user", item.username)
            view.findNavController()
                .navigate(R.id.action_aboutMeFragment_to_shareFollowFragment, bundle)
        }, data)
        locationViewModel.getAllRequests(data, adapter)
        locationViewModel.getNewRequest(data, adapter)
        recyclerview.adapter = adapter

    }

    override fun onTaskCompleted(weather: Weather) {
        binding.tvWeatherDescription.text = weather.temp
    }

}