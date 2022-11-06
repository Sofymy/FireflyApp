package com.example.thesis.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thesis.model.Location
import com.example.thesis.model.User
import com.example.thesis.repository.LocationRepository
import com.example.thesis.views.loggedin.ShareRequestsAdapter
import kotlinx.coroutines.launch

class LocationViewModel: ViewModel() {
    var location: MutableLiveData<Location> = MutableLiveData<Location>()
    var targetLocation: MutableLiveData<String> = MutableLiveData<String>()
    private var locationRepository = LocationRepository()

    fun addNewShare(username: String) {
        locationRepository.addNewShare(username)
    }

    fun getAllRequests(data: ArrayList<User>, adapter: ShareRequestsAdapter) {
        locationRepository.getAllRequests(data, adapter)
    }
    fun getNewRequest(data: ArrayList<User>, adapter: ShareRequestsAdapter) {
        locationRepository.getNewRequest(data, adapter)
    }

    fun shareMyLocation(location: android.location.Location) {
        locationRepository.shareMyLocation(location)
    }

    fun getLocation(username: String) = viewModelScope.launch {
        locationRepository.getLocation(username).collect{ value ->
            location.value = value
        }
    }

    fun addNewTarget(target: String) {
        locationRepository.addNewTarget(target)
    }

    fun getTarget(username: String) = viewModelScope.launch {
        locationRepository.getTarget(username).collect{ value ->
            targetLocation.value = value
        }
    }

    fun addNewManualTarget(target: String) {
        locationRepository.addNewManualTarget(target)
    }

    fun stopSharing(user: String) {
        locationRepository.stopSharing(user)
    }

}