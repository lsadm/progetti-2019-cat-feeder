package com.greenjackets.prototipo.RecycleView

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.elemento_animale.view.*

class ViewHolderAnimale (view: View) : RecyclerView.ViewHolder(view) {
        var Immagine =view.img_animale
        var Nome= view.nome_animale
}