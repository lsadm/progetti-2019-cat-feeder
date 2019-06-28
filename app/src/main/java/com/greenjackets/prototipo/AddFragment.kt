package com.greenjackets.prototipo
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap

import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_add.*
import java.util.jar.Manifest



class AddFragment : Fragment() {
    val QRCODE : Int=5    // TODO: aggiungere qrcode variabile

    val REQUEST_IMAGE_CAPTURE = 1 // serve per la fotocamera
    val firebaseDatabase = FirebaseDatabase.getInstance()       //Per accedere a firebase

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)




    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val ref = firebaseDatabase.getReference("Utente/"+QRCODE)

        btn_fotocamera.setOnClickListener {
            // Imposta il funzionamento del pulsante per l'acqisizione dell'immagine
            // Creo un intent di tipo implicito per acquisire l'immagine
            val takePhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePhoto.resolveActivity(activity!!.packageManager)?.also {
                startActivityForResult(takePhoto, REQUEST_IMAGE_CAPTURE)
            }

        }

        //Bottone per aggiungere elementi su firebase
        btn_aggiungi.setOnClickListener {
            val nome = txt_nome.text.toString()
            val età = txt_eta.text.toString()
            val peso = txt_peso.text.toString()
            val razza = txt_razza.text.toString()
            val checkvacc = check_vaccino.isChecked
            val checkster= check_sterile.isChecked

            ref.child("Nome").setValue(nome)
            ref.child("Età").setValue(età)
            ref.child("peso").setValue(peso)
            ref.child("razza").setValue(razza)
            ref.child("Vaccinato").setValue(checkvacc)
            ref.child("Sterilizzato").setValue(checkster)



            //devo scrivere il contenuto nel database








        }
    }


    /**
     * Questo metodo viene invocato per gestire il risultato al ritorno da una activity
     * occorre determinare chi aveva generato la richiesta
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {     // Acquisizione immagine
            val immagineCatturata = data?.extras?.get("data") as Bitmap
            ProfilePic.setImageBitmap(immagineCatturata)
        }
    }

    }





