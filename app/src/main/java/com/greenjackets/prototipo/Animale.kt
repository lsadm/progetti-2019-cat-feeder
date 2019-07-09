package com.greenjackets.prototipo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

//TODO: Ci sono 2 animali
data class Animale(var Età : String?= null ,var Nome : String?= null,var Sesso : String?= null,var Sterilizzato : String?= null,var Vaccinato : String?= null,var Peso : String?= null,var razza : String?= null,var qrcode : String?= null ) : Parcelable