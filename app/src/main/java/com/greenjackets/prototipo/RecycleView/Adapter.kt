package com.greenjackets.prototipo.RecycleView

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.greenjackets.prototipo.GlideApp
import com.greenjackets.prototipo.R


class Adapter (val dataset: ArrayList<Animale?>, val context: Context) : RecyclerView.Adapter<ViewHolderAnimale>() {


    val storage = FirebaseStorage.getInstance() //Per accedere allo storage , lo uso per creare il rif

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolderAnimale {
        // Crea e restituisce un viewholder, effettuando l'inflate del layout relativo alla riga
        return ViewHolderAnimale(LayoutInflater.from(context).inflate(R.layout.elemento_animale, viewGroup, false))
    }

    // Invocata per conoscere quanti elementi contiene il dataset
    override fun getItemCount(): Int {
        return dataset.size
    }

    // Invocata per visualizzare all'interno del ViewHolder i dati corrispondenti all'elemento
    override fun onBindViewHolder(viewHolder: ViewHolderAnimale, position: Int) {
        val animale=dataset.get(position)
        val imagRef =storage.getReference().child(animale?.qrcode+"/gatto.jpg")





        //carica gli elementi del viewholder con i dati del gioco
        viewHolder.Nome.text=animale?.Nome

        imagRef.downloadUrl.addOnSuccessListener {
            GlideApp.with(context).load(it).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(viewHolder.Immagine)
        }


        // Imposto il listner per passare alla schermata di pannello animale
        viewHolder.itemView.setOnClickListener {
            // Creo un bundle e vi inserisco l'animale da visualizzare
            val b = Bundle()
            b.putParcelable("animale",animale)
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_animal_fragment, b)
        }



    }




}