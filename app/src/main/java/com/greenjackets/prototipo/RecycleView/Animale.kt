package com.greenjackets.prototipo.RecycleView

import android.os.Parcelable
import androidx.versionedparcelable.VersionedParcelize
import kotlinx.android.parcel.Parcelize
@Parcelize

class Animale(var Età : String?= null ,var Nome : String?= null,var Sesso : String?= null,var Sterilizzato : String?= null,var Vaccinato : String?= null,var Peso : String?= null,var razza : String?= null,var qrcode : String?= null )
    : Parcelable