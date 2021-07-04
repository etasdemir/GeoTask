package com.elacqua.geotask.data.local

import androidx.lifecycle.LiveData
import com.elacqua.geotask.data.local.dao.PlaceDao
import com.elacqua.geotask.data.model.Place

class LocalRepository private constructor(private val placeDao: PlaceDao) {

    fun getAllPlaces(): LiveData<List<Place>> = placeDao.getAllPlaces()

    suspend fun deletePlace(place: Place) = placeDao.deletePlace(place)

    suspend fun addPlace(place: Place) = placeDao.addPlace(place)

    suspend fun deleteAllPlaces() = placeDao.nukePlaceTable()

    companion object {
        @Volatile
        private lateinit var instance: LocalRepository

        fun getInstance(placeDao: PlaceDao): LocalRepository {
            synchronized(this) {
                if (!::instance.isInitialized) {
                    instance = LocalRepository(placeDao)
                }
                return instance
            }
        }
    }
}