package com.yongji.interview.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yongji.interview.detail.DetailViewModel
import com.yongji.interview.network.RouteData

/*
* Simple ViewModel factory that provides the Business and context to the ViewModel.
*/
class DetailViewModelFactory(
    private val routeData: RouteData,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(routeData, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
