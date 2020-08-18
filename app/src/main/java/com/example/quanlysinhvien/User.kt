package com.example.quanlysinhvien

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var name: String = "",
    var dateOfBirth: String = "",
    var gender: String = "",
    var email: String = "",
    var password: String = "",
    var bitchAdmin: String = "",
    var ActivitiesJoined: List<ActivityModal> = emptyList()
) : Parcelable
//    var ActivitiesJoined: List<String>


