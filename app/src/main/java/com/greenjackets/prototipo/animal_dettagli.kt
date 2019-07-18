package com.greenjackets.prototipo


import android.app.AlertDialog
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
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast

import androidx.navigation.Navigation

import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_animal_dettagli.*
import com.google.firebase.database.DataSnapshot
import com.google.zxing.qrcode.encoder.QRCode
import com.greenjackets.prototipo.RecycleView.Animale
import kotlinx.android.synthetic.main.fragment_add.*

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
            val animale: Animale? = it.getParcelable("animale")
            animale?.let {
                val  QRCODE= animale.qrcode
                val b = Bundle() // se si decide di editare basta passare alla schermata "add" il qrcode da modificare

                b.putParcelable("animale", animale)    // metto nel bundle il qrcode

                imagRef= storageRef.child(QRCODE.toString()+getString(R.string.slash_gattojpg))
                dataRef = database.child(QRCODE.toString()+getString(R.string.slash_animale))
 
                downloadFoto(imagRef)
                CaricaAnimale(it)

                btn_edit.setOnClickListener {

                    Navigation.findNavController(view).navigate(R.id.action_animal_dettagli_to_addFragment,b) // e lo passo alla addFragment

                }



                btn_delete.setOnClickListener {

                    val builder = AlertDialog.Builder(this.activity)


                    builder.setMessage(getString(R.string.delete_profile))


                    builder.setPositiveButton(getString(R.string.Yes)){dialog, which ->
                        // Do something when user press the positive button
                        database.child(QRCODE.toString()).removeValue()  // Rimuove da database
                        storageRef.child(QRCODE.toString()).delete()
                        // Rimuove da file interno
                        val filename = getString(R.string.QRCODEStxt) // nome del file
                        removeLineFromFile(filename,QRCODE)
                        // Torna alla schermata di partenza
                        Navigation.findNavController(view).navigate(R.id.action_animal_dettagli_to_homeFragment)

                    }

                    // Display a negative button on alert dialog
                    builder.setNegativeButton(getString(R.string.No)){dialog,which ->
                        Toast.makeText(context,getString(R.string.Canceled), Toast.LENGTH_SHORT).show()
                    }

                    // Crea l'alert dialog
                    val dialog: AlertDialog = builder.create()

                    // Display the alert
                    dialog.show()



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

    private fun CaricaAnimale( animale : Animale?) {

        txt_età_dett.text = animale?.Età.toString()
        txt_nome_dett.text = animale?.Nome.toString()
        txt_peso_dett.text = animale?.Peso.toString()
        txt_sesso_dett.text = animale?.Sesso.toString()
        if(animale?.Sterilizzato.toString() == getString(R.string.true_string))
            txt_steril_dett.text = getString(R.string.Yes)
        else
            txt_steril_dett.text= getString(R.string.No)
        if(animale?.Vaccinato.toString() == getString(R.string.true_string))
            txt_vacc_dett.text = getString(R.string.Yes)
        else
            txt_vacc_dett.text= getString(R.string.No)

        txt_razza_dett.text = animale?.razza.toString()
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
                while( i < QRS.size )
                {it?.write((QRS[i]+"\n").toByteArray())
                    i++}
            }

        } catch (e: FileNotFoundException) {e.printStackTrace()}
        catch (e: IOException) {e.printStackTrace()}



    }


}




