package com.greenjackets.prototipo


import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.Navigation

import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_animal_dettagli.*
import com.google.firebase.database.DataSnapshot
import com.google.zxing.qrcode.encoder.QRCode

import java.lang.Exception
import java.nio.file.Files.delete

import java.io.*
import java.nio.file.Files.delete








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



                btn_delete.setOnClickListener {

                    database.child(QRCODE.toString()).removeValue()  // Rimuove da database
                    storageRef.child(QRCODE.toString()).delete()

                    // Rimuove da file interno
                    val filename = "Qrcodes.txt" // nome del file


                    removeLineFromFile(filename,QRCODE)




                    // Torna alla schermata di partenza
                    Navigation.findNavController(view!!).navigate(R.id.action_animal_dettagli_to_homeFragment)
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
                    if(animale?.Sterilizzato.toString() == "true")
                        txt_steril_dett.text = "si"
                    else
                        txt_steril_dett.text= "no"
                    if(animale?.Vaccinato.toString() == "true")
                        txt_vacc_dett.text = "si"
                    else
                        txt_vacc_dett.text= "no"

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


    fun removeLineFromFile(filename: String, lineToRemove: String?) {
        try {


            val QRS  = ArrayList<String>()
            val filestream= context?.openFileInput(filename)
            val bufferedreader =filestream?.bufferedReader()
            var i=0


            bufferedreader?.forEachLine {
                if(it!=lineToRemove)
                    QRS.add(it)
            }
            bufferedreader?.close()

            context?.openFileOutput(filename, Context.MODE_PRIVATE).use {
                while( i < QRS.size && QRS[i]!=null)
                {it?.write((QRS[i]+"\n").toByteArray())
                    i++}
            }





        } catch (e: FileNotFoundException) {
            //catch errors opening file
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }



    }


}




