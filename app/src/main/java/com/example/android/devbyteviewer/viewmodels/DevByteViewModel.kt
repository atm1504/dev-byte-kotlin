package com.example.android.devbyteviewer.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.android.devbyteviewer.domain.Video
import com.example.android.devbyteviewer.network.Network
import com.example.android.devbyteviewer.network.asDomainModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.IOException

class DevByteViewModel(application: Application) : AndroidViewModel(application) {


    private val _playlist = MutableLiveData<List<Video>>()

    val playlist: LiveData<List<Video>>
        get() = _playlist

    init {
        refreshDataFromNetwork()
    }


    private fun refreshDataFromNetwork() = viewModelScope.launch {
        try {
            val playlist = Network.devbytes.getPlaylist().await()
            _playlist.postValue(playlist.asDomainModel())
        } catch (networkError: IOException) {
            // Show an infinite loading spinner if the request fails
            // challenge exercise: show an error to the user if the network request fails
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DevByteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DevByteViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
