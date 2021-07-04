package com.elacqua.geotask.ui.map

import androidx.lifecycle.*
import com.elacqua.geotask.data.local.LocalRepository
import com.elacqua.geotask.data.model.*
import com.elacqua.geotask.data.remote.RemoteRepository
import com.elacqua.geotask.utils.Helper
import com.elacqua.geotask.utils.UIState
import com.huawei.hms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GeoMapViewModel(localRepository: LocalRepository) : ViewModel() {

    private val _decodedPolyline = MutableLiveData<List<LatLng>>()
    val decodedPolyline: LiveData<List<LatLng>> = _decodedPolyline

    val places: LiveData<List<Place>> = localRepository.getAllPlaces()

    fun findPath(places: List<Place>, location: LatLng) {
        viewModelScope.launch(Dispatchers.IO) {
            val points: Optimization? = optimizePoints(places, location)
            if (points == null || points.routes.isEmpty()) {
                UIState.state.postValue(UIState.Error("Failed to find route"))
                return@launch
            }
            val steps = points.routes[0].steps
            val result = getRoute(steps)
            UIState.state.postValue(UIState.Success)
            _decodedPolyline.postValue(result)
        }
    }

    private suspend fun optimizePoints(
        places: List<Place>,
        location: LatLng
    ): Optimization? {
        val jobs = ArrayList<Job>()
        var id = 1
        for (index in places.indices) {
            val jobLocation = listOf(places[index].longitude, places[index].latitude)
            val job = Job(id++, jobLocation)
            jobs.add(job)
        }
        return RemoteRepository.optimizeEndPoints(jobs, location)
    }

    private suspend fun getRoute(steps: List<Step>): List<LatLng> {
        val points = ArrayList<List<Double>>()
        for (step in steps) {
            points.add(step.location)
        }
        val coordinates = Coordinates(points)
        val response = RemoteRepository.getRoutes(coordinates)
        if (response == null || response.polylines.isEmpty()) {
            return emptyList()
        }
        val polyline = response.polylines[0].geometry
        return Helper.decodePolyline(polyline)
    }

}

@Suppress("UNCHECKED_CAST")
class GeoMapViewModelFactory(private val localRepository: LocalRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GeoMapViewModel(localRepository) as T
    }
}