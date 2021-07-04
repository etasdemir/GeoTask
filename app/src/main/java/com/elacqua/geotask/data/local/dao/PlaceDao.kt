package com.elacqua.geotask.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.elacqua.geotask.data.model.Place

@Dao
interface PlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlace(place: Place)

    @Query("select * from Place")
    fun getAllPlaces(): LiveData<List<Place>>

    @Delete
    suspend fun deletePlace(place: Place)

    @Query("delete from Place")
    suspend fun nukePlaceTable()
}