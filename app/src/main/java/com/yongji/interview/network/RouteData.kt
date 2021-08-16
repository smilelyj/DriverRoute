package com.yongji.interview.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RouteData(
    @Json(name = "driver") val driver: String,
    @Json(name = "location") val location: String,
   ) : Parcelable