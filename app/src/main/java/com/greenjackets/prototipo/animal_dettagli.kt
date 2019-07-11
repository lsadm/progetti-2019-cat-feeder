package com.greenjackets.prototipo


import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation

import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_animal_dettagli.*
import com.google.firebase.database.DataSnapshot
import com.google.zxing.qrcode.encoder.QRCode

import java.lang.Exception



class animal_dettagli : Fragment() {

    val storageRef = FirebaseStorage.getInstance().getReference() //Per accedere allo storage , lo uso per creare il rif
    val database = FirebaseDatabase.getInstance().getReference() // creo il rif al database



    var dataRef: DatabaseReference? = null
    var imagRef: StorageReference? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_animal_dettagli, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // setHasOptionsMenu(true)
        activity?.requestedOrientation =
            (ActivityInfo.SCREEN_ORIENTATION_NOSENSOR) //impedisce la rotazione dello schermo


        arguments?.let {
            val animale: com.greenjackets.prototipo.RecycleView.Animale? = it.getParcelable("animale")
            animale?.let {
                val  QRCODE= animale.qrcode
                val b = Bundle() // se si decide di editare basta passare alla schermata "add" il qrcode da modificare

                b.putParcelable("animale", animale)    // metto nel bundle il qrcode

                imagRef= storageRef.child(QRCODE.toString()+"/gatto.jpg")
                dataRef = database.child(QRCODE.toString()+"/Animale")

                downloadFoto(imagRef)
                downloadDati()

                btn_edit.setOnClickListener {

                    Navigation.findNavController(view!!).navigate(R.id.action_animal_dettagli_to_addFragment,b) // e lo passo alla addFragment

                }
            }


        }








    }

    private fun downloadFoto(imagRef: StorageReference?) {
        val picture = ArrayList<ImageView>() //Arraylist di immagini per caricare
        picture.add(picture0)

        imagRef?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            // Use the bytes to display the image
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            picture.get(0).setImageBitmap(bitmap)
        }?.addOnFailureListener {
            // Handle any errors
        }
    }// DownloadFoto

    private fun downloadDati() {

        // Read from the database
        val postListener = object : ValueEventListener {  // creazione ValueEventListener

            override fun onDataChange(dataSnapshot: DataSnapshot) {

               val  animale = dataSnapshot.getValue(Animale::class.java)
                try {

                    txt_età_dett.text = animale?.Età.toString()
                    txt_nome_dett.text = animale?.Nome.toString()
                    txt_peso_dett.text = animale?.Peso.toString()
                    txt_sesso_dett.text = animale?.Sesso.toString()
                    txt_steril_dett.text = animale?.Sterilizzato.toString()
                    txt_vacc_dett.text = animale?.Vaccinato.toString()
                    txt_razza_dett.text = animale?.razza.toString()


                } catch (e: Exception) {}

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }


        }

        dataRef?.addValueEventListener(postListener)  // dichiarato sopra il ValueEventListener e poi chiamo la funzione passandoglielo



    }


}




