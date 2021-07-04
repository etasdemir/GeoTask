package com.elacqua.geotask.ui.savedplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.elacqua.geotask.data.local.LocalRepository
import com.elacqua.geotask.data.model.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SavedPlaceViewModel(private val localRepository: LocalRepository) : ViewModel() {

    val placesLiveData = localRepository.getAllPlaces()

    fun deletePlace(place: Place) {
        viewModelScope.launch(Dispatchers.IO) {
            localRepository.deletePlace(place)
        }
    }

    fun deleteAllPlaces() {
        viewModelScope.launch {
            localRepository.deleteAllPlaces()
        }
    }

}

@Suppress("UNCHECKED_CAST")
class SavedPlaceViewModelFactory(private val localRepository: LocalRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SavedPlaceViewModel(localRepository) as T
    }
}