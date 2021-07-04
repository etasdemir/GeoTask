package com.elacqua.geotask.ui.savelocation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.elacqua.geotask.data.local.LocalRepository
import com.elacqua.geotask.data.model.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SaveLocationViewModel(private val localRepository: LocalRepository) : ViewModel() {

    fun savePlace(place: Place) {
        viewModelScope.launch(Dispatchers.IO) {
            localRepository.addPlace(place)
        }
    }

}

@Suppress("UNCHECKED_CAST")
class SaveLocationViewModelFactory(private val localRepository: LocalRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SaveLocationViewModel(localRepository) as T
    }
}