package com.yongji.interview.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.yongji.interview.network.RouteData

/**
 *  The [ViewModel] associated with the [DetailFragment], containing information about the selected
 *  [RouteData].
 */
class DetailViewModel(routeData: RouteData, app: Application) : AndroidViewModel(app) {
    private val _selectedRouteData = MutableLiveData<RouteData>()

    // The external LiveData for the selectedRouteData
    val selectedRouteData: LiveData<RouteData>
        get() = _selectedRouteData

    // Initialize the _selectedRouteData MutableLiveData
    init {
        _selectedRouteData.value = routeData
        Log.e("yongji", routeData.toString())
    }

}