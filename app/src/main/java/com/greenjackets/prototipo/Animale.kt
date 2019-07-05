package com.greenjackets.prototipo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Animale(var età : String? = null ,var nome : String? = null,val sesso : String? = null,var sterilizzato : String? = null,var vaccinato : String? = null,var peso : String? = null,var razza : String? = null) : Parcelable