package com.maxxxwk.gamescoreapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Team(
    val name: String,
    var score: Int
) : Parcelable