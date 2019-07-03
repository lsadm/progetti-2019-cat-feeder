package com.greenjackets.prototipo.RecycleView

import android.os.Parcelable
import androidx.versionedparcelable.VersionedParcelize
import kotlinx.android.parcel.Parcelize
@Parcelize

class Animale(var Età : String ,var Nome : String,var Sesso : String,var Sterilizzato : String,var Vaccinato : String,var Peso : String,var razza : String,var qrcode : String )
    : Parcelable {}