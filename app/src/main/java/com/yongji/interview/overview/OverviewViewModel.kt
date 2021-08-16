package com.yongji.interview.overview

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yongji.interview.network.RouteData
import com.yongji.interview.utils.JsonUtil.getRoutes
import kotlinx.coroutines.*

enum class ApiStatus { LOADING, ERROR, DONE }
/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel(application: Application) : AndroidViewModel(application) {

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    @SuppressLint("StaticFieldLeak")
    private val context = application.getApplicationContext()

//  Internally, we use a MutableLiveData, because we will be updating the weather List
//  with new values
//  The external LiveData interface to the RouteData is immutable, so only this class can modify
    private val _routeData = MutableLiveData<List<RouteData>>()
    val routeData:LiveData<List<RouteData>>
        get() = _routeData

    private val _navigateToSelectedFactItem = MutableLiveData<RouteData>()

    val navigateToSelectedFactItem: LiveData<RouteData>
        get() = _navigateToSelectedFactItem

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /**
     * Call getAllRouteData() on init so we can display status immediately.
     */
    init {
        getAllRouteData(context)
    }


    public fun getAllRouteData(context: Context) {
        coroutineScope.async {
            // Get the Deferred object for our Retrofit request
//            Log.e("yongjiLog", routeData.value.toString())
            val getRouteDataListDeferred = getRoutes(context)

            try {
                _status.value = ApiStatus.LOADING
                val listResult = getRouteDataListDeferred
                Log.e("yongjiLog",listResult.toString())

                _status.value = ApiStatus.DONE
                _routeData.value = listResult
                Log.e("yongjiSuccess2", routeData.value.toString())

            } catch (e: Exception) {
                Log.e("yongjiError", e.message.toString())
                _status.value = ApiStatus.ERROR
                _routeData.value = ArrayList()

            }
        }
    }

    /**
     * After the navigation has taken place, make sure _navigateToSelectedItem is set to null
     */
    fun displayDetailsComplete() {
        _navigateToSelectedFactItem.value = null
    }
    /**
     * When the item is clicked, set the [_navigateToSelectedRoute] [MutableLiveData]
     * @param detailRoute The [RouteData] that was clicked on.
     */
    fun displayDetails(detailRoute: RouteData) {
        _navigateToSelectedFactItem.value = detailRoute
    }

    /**
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}