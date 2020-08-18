package com.example.quanlysinhvien

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ActivityModal(
    var name: String = "",
    var field: String = "",
    var time: String = "",
    var amount: Int = 0,
    var empty: Int = 0,
    var request: String = "",
    var description: String = ""
) : Parcelable