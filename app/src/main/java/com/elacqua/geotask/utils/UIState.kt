package com.elacqua.geotask.utils

import androidx.lifecycle.MutableLiveData


object UIState {
    val state = MutableLiveData<State>()

    interface State
    object Success: State
    data class Error(val errorMessage: String, val exception: Exception? = null): State
    object Loading: State
}